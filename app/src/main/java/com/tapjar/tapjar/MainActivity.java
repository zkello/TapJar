package com.tapjar.tapjar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    public static final String EXTRA_MESSAGE = "ACCOUNT_NUMBER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void startBeamActivity(View view){
        Intent intent = new Intent(view.getContext(), BeamActivity.class);
        String message = "Account Number";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }


}