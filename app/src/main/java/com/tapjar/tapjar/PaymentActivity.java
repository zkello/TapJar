package com.tapjar.tapjar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.stripe.android.model.Card;

import org.json.JSONException;

import java.sql.Wrapper;

import edu.cwru.tpt6.stripeapilib.ChargeWrapper;
import edu.cwru.tpt6.stripeapilib.HttpCallBack;
import edu.cwru.tpt6.stripeapilib.JSONDecoder;
import edu.cwru.tpt6.stripeapilib.StripeHelper;
import edu.cwru.tpt6.stripeapilib.TokenWrapper;

public class PaymentActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "ACCOUNT_NUMBER";

    private String acctNum;

    private StripeHelper stripeInst;
    private Card card;
    private TokenWrapper token;
    private ChargeWrapper charge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Bundle extras = getIntent().getExtras();

        //Detect if account number given is valid
        if(extras != null) {
            acctNum = extras.getString(EXTRA_MESSAGE);

            if(acctNum != null && !acctNum.isEmpty()) {
                Toast.makeText(
                        this, "This is the employee Id that was given: " + acctNum,
                        Toast.LENGTH_LONG).show();

                stripeInst = new StripeHelper();
                card = new Card("4242424242424242", 12, 2016, "123");
                stripeInst.createTokenWithCard(card, new HttpCallBack() {
                    @Override
                    public void processResponse(String response) {
                        token = null;
                        try {
                            token = JSONDecoder.getTokenFromResponse(response, card);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (token != null)
                        {
                            stripeInst.createChargeWithID(token, acctNum, 100, new HttpCallBack() {
                                @Override
                                public void processResponse(String response) {
                                     charge = null;
                                    try {
                                        charge = JSONDecoder.getChargeFromResponse(response, token, acctNum);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if(charge != null)
                                    {
                                        Toast.makeText(
                                                PaymentActivity.this,"CHARGE SUCCEED! :D",
                                                Toast.LENGTH_LONG).show();

                                        Log.d("TapJar", "Charge went through");
                                    }
                                    else
                                    {
                                        Log.d("TapJar", "Charge");
                                    }
                                }


                                @Override
                                public void processFailure(Exception e) {
                                    Log.d("TapJar", "Charge Post failed\n" + e.getMessage());
                                }
                            });
                        }
                        else
                        {
                            Log.d("TapJar", "Decode Token failed");
                        }
                    }

                    @Override
                    public void processFailure(Exception e) {

                    }
                });
            }
            else {
                Toast.makeText(this, "Account number is empty or null", Toast.LENGTH_LONG).show();
                //TODO check to make sure acct number is valid here
                finish();
            }
        }
        else {
            Toast.makeText(
                    this, "AccountNumber does not exist in bundle", Toast.LENGTH_LONG).show();
            finish();
        }
    }

//    //Test method to buy 1.00 worth of stuff using
//    public static void createChargeWithID() throws InterruptedException {
//        Card tipperCard = new Card("4242-4242-4242-4242", 12, 2016, "123");
//
//        if(!tipperCard.validateCard())
//        {
//            throw new RuntimeException("Invalid card"); //TODO make more specific handling
//        }
//
//        Log.d("TapJar", "Creating token");
//        final CountDownLatch signal = new CountDownLatch(1);
//        new Stripe().createToken(tipperCard, "pk_test_kdv47KNfq73mrgECsbKryveD", new TokenCallback() {
//            @Override
//            public void onError(Exception error) {
//                Log.d("TapJar", "token creation unsuccessful. Error msg: " + error.getMessage());
//                //responseHandler.processFailure(error);
//            }
//
//            @Override
//            public void onSuccess(Token token) {
//                Log.d("TapJar", "token creation succesful" + token.getId());
//                //responseHandler.processResponse(token.getId());
//            }
//        });
//        Log.d("TapJar", "awaiting response");
//        signal.await();
//        Log.d("TapJar", "Token created");
//    }
}
