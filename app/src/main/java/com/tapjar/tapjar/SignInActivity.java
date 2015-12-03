package com.tapjar.tapjar;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    public void getAccountNumber(){

        String accountNumber = "test account number";
        saveAccountNumber(accountNumber);
    }

    public void saveAccountNumber(String accountNumber){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.account_number), accountNumber);
        editor.commit();
    }
}
