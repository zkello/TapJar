package com.tapjar.tapjar;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import edu.cwru.tpt6.stripeapilib.AccountWrapper;
import edu.cwru.tpt6.stripeapilib.HttpCallBack;
import edu.cwru.tpt6.stripeapilib.JSONDecoder;
import edu.cwru.tpt6.stripeapilib.StripeHelper;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    public void getAccountNumber(View view){
        StripeHelper stripeInst = new StripeHelper();
        EditText editText = (EditText) findViewById(R.id.editText);
        final String email = editText.getText().toString();
        stripeInst.openAcctWithEmail(new HttpCallBack() {
            AccountWrapper accountWrapper;

            @Override
            public void processResponse(String response) {
                try {
                    accountWrapper = JSONDecoder.getAcctFromResponse(response, email);
                    saveAccountNumber(accountWrapper.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void processFailure(Exception e) {

            }
        }, email);

    }

    public void saveAccountNumber(String accountNumber){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.account_number), accountNumber);
        editor.commit();

        Toast.makeText(
                this, "Saved Account Number: " + accountNumber,
                Toast.LENGTH_LONG).show();

        finish();
    }


}
