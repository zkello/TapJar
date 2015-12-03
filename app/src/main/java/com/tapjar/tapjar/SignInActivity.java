package com.tapjar.tapjar;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    public void getAccountNumber(View view){

        String accountNumber = "test account number";
        saveAccountNumber(accountNumber);
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
