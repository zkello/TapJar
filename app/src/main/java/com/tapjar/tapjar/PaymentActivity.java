package com.tapjar.tapjar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class PaymentActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "ACCOUNT_NUMBER";

    private String acctNum;

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
}
