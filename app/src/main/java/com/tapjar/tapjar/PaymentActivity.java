package com.tapjar.tapjar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.stripe.android.model.Card;
import org.json.JSONException;
import edu.cwru.tpt6.stripeapilib.ChargeWrapper;
import edu.cwru.tpt6.stripeapilib.HttpCallBack;
import edu.cwru.tpt6.stripeapilib.JSONDecoder;
import edu.cwru.tpt6.stripeapilib.StripeHelper;
import edu.cwru.tpt6.stripeapilib.TokenWrapper;

public class PaymentActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "ACCOUNT_NUMBER";
    private static final int MIN_TIP = 0;
    private static final int MAX_TIP = 9999;

    private String acctNum;

    private StripeHelper stripeInst;
    private Card card;
    private TokenWrapper token;
    private ChargeWrapper charge;

    private EditText cardNumbers0, cardNumbers1, cardNumbers2, cardNumbers3;
    private EditText expMonth, expYear;
    private EditText cvcField;
    private EditText tipDollars, tipCents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Bundle extras = getIntent().getExtras();
        onCreate();
        //Detect if account number given is valid
        if(extras != null) {
            acctNum = extras.getString(EXTRA_MESSAGE);

            if(acctNum != null && !acctNum.isEmpty()) {
                Log.d("TapJar", "This is the employee Id that was given: " + acctNum);
            }
            else {
                Toast.makeText(this, "Account number is empty or null", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        else {
            Toast.makeText(
                    this, "AccountNumber does not exist in bundle", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void onCreate()
    {
        cardNumbers0 = (EditText) findViewById(R.id.cardNumberField0);
        cardNumbers1 = (EditText) findViewById(R.id.cardNumberField1);
        cardNumbers2 = (EditText) findViewById(R.id.cardNumberField2);
        cardNumbers3 = (EditText) findViewById(R.id.cardNumberField3);

        expMonth = (EditText) findViewById(R.id.expMonthField);
        expYear = (EditText) findViewById(R.id.expYearField);

        tipDollars = (EditText) findViewById(R.id.tipDollarsField);
        tipCents = (EditText) findViewById(R.id.tipCentsField);
        cvcField = (EditText) findViewById(R.id.cvcField);
    }

    private Card createCardFromFields()
    {
        String cardNumberText = cardNumbers0.getText().toString() +
                cardNumbers1.getText().toString() +
                cardNumbers2.getText().toString() +
                cardNumbers3.getText().toString();

        String expMonthText = expMonth.getText().toString();
        String expYearText = expYear.getText().toString();

        Integer expMonthVal = -1;
        Integer expYearVal = -1;

        try {
            expMonthVal = Integer.parseInt(expMonthText);
            expYearVal = Integer.parseInt(expYearText);
        } catch (NumberFormatException e)
        {
            Log.d("TapJar", "Expiration date could not be parsed");
        }

        return new Card(cardNumberText, expMonthVal, expYearVal, cvcField.getText().toString());
    }

    private int getTipFromFields()
    {
        String centsText = tipCents.getText().toString();
        String dollarsText = tipDollars.getText().toString();
        if(centsText == null || centsText.isEmpty()) return -1;
        if(dollarsText == null || dollarsText.isEmpty()) return -1;

        int centsVal = -1;
        int dollarsVal = -1;

        try
        {
            centsVal = Integer.parseInt(centsText);
            dollarsVal = Integer.parseInt(dollarsText);
        } catch (NumberFormatException e)
        {
            Log.d("TapJar", "Expiration date could not be parsed");
        }

        if(centsVal < 0 || centsVal > 99 ) return -1;
        if(dollarsVal < 0 || dollarsVal > 99) return -1;

        return (dollarsVal * 100) + centsVal;
    }

    public void tipIt(View view) {
        stripeInst = new StripeHelper();
        card = createCardFromFields();

        if(card.validateCard())
        {
            int tipVal = getTipFromFields();
            if(tipVal >= MIN_TIP && tipVal <= MAX_TIP) {
                stripeInst.createTokenWithCard(card, new HttpCallBack() {
                    @Override
                    public void processResponse(String response) {
                        handleTokenSuccessResponse(response);
                    }

                    @Override
                    public void processFailure(Exception e) {
                        Log.d("TapJar", "Token Post failed with message: " + e.getMessage());
                    }
                });
            }
            else
            {
                Toast.makeText(
                        PaymentActivity.this,"Invalid tip given",
                        Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(
                    PaymentActivity.this,"Given card is invalid",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void handleTokenSuccessResponse(String response)
    {
        token = null;
        try {
            token = JSONDecoder.getTokenFromResponse(response, card);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (token != null)
        {
            int tipVal = getTipFromFields();
            if(tipVal >= MIN_TIP && tipVal <= MAX_TIP) {
                stripeInst.createChargeWithID(token, acctNum, 125, new HttpCallBack() {
                    @Override
                    public void processResponse(String response) {
                        handleChargeSuccessResponse(response);
                    }


                    @Override
                    public void processFailure(Exception e) {
                        Log.d("TapJar", "Charge Post failed\n" + e.getMessage());
                    }
                });
            }
            else
            {
                Toast.makeText(
                        PaymentActivity.this,"Invalid tip given",
                        Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Log.d("TapJar", "Decode Token failed");
        }
    }

    private void handleChargeSuccessResponse(String response) {
        charge = null;
        try {
            charge = JSONDecoder.getChargeFromResponse(response, token, acctNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(charge != null)
        {
            Toast.makeText(
                    PaymentActivity.this,"CHARGE SUCCEEDED!",
                    Toast.LENGTH_LONG).show();
            Log.d("TapJar", "Charge went through");
            finish();
        }
        else
        {
            Log.d("TapJar", "Charge");
        }
    }
}
