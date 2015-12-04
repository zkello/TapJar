package com.tapjar.tapjar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "ACCOUNT_NUMBER";

    //Have to make message a instance variable in order to access it from the dialog inner class
    // in start payment activity
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        message = "";
        Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }


    public void startBeamActivity(View view){
        Intent intent = new Intent(view.getContext(), BeamActivity.class);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String accountNumber = sharedPref.getString(getString(R.string.account_number), "Default Value");
        intent.putExtra(EXTRA_MESSAGE, accountNumber);
        startActivity(intent);
    }

    public void startSignInActivity(View view){
        Intent intent = new Intent(view.getContext(), SignInActivity.class);
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