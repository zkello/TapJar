package com.tapjar.tapjar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    public static final String EXTRA_MESSAGE = "ACCOUNT_NUMBER";

    //Have to make message a instance variable in order to access it from the dialog inner class
    // in start payment activity
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void startBeamActivity(View view){
        Intent intent = new Intent(view.getContext(), BeamActivity.class);
        message = "Account Number";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void startSignInActivity(View view){
        Intent intent = new Intent(view.getContext(), BeamActivity.class);
        message = "Account Number";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void startTipActivity(View view)
    {
        Intent intent = new Intent(view.getContext(), TipActivity.class);
        message = "Bananas!!!";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void startPaymentActivity(View view)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                message = input.getText().toString();
                Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }

        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();


    }

}