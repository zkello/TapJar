package edu.cwru.tpt6.stripeapilib;

import android.os.AsyncTask;
import android.util.Log;

import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.CountDownLatch;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by timlax on 12/1/15.
 */
public class StripeHelper {

    private static final String credentialKey = "sk_test_JeEPsy1W0H1stdpnBE2Ezx6c";

    //Create a stripeHelper from the key you are given
    public StripeHelper(String StripeKey) {
        if (StripeKey == null || StripeKey.length() == 0) {
            throw new IllegalArgumentException("Invalid Key: You must use a valid " +
                    "key to create a token.  For more info, see " +
                    "https://stripe.com/docs/stripe.js.");
        }
    }

    public void createChargeWithID(
            Token token, String employeeIdentifier, int amnt, ChargeCallback responseHandler) {
        //TODO to implement
    }

    //method to create a token with a given card object
    public static void createTokenWithCard(Card card, HttpCallBack responseHandler) throws InterruptedException {
        if(!card.validateCard())
        {
            throw new RuntimeException("Invalid card"); //TODO make more specific handling
        }

        Log.d("TapJar", "Creating token");
        final CountDownLatch signal = new CountDownLatch(1);
       // AsyncHTTPGetRequest = new AsyncHTTPGetRequest();

        signal.await();
        Log.d("TapJar", "Token created");
    }

    public void openAcctWithID()
    {

    }

    public static void setAuthenticator()
    {
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(credentialKey,
                        "".toCharArray());

            }
        });
    }

    public static void accessCharges(HttpCallBack httpCallBack) {
        new AsyncHTTPGetRequest(httpCallBack, "https://api.stripe.com/v1/charges").execute();
    }


    private static class AsyncHTTPGetRequest extends AsyncTask<Void, Void, HTTPSResponseWrapper>
    {
        public final HttpCallBack handler;
        public final String url;

        public AsyncHTTPGetRequest(HttpCallBack handler, String url)
        {
            this.url = url;
            this.handler = handler;
        }

        @Override
        protected HTTPSResponseWrapper doInBackground(Void... params) {
            try {
                URL obj = new URL(url);

                HttpsURLConnection connector = (HttpsURLConnection) obj.openConnection();

                // optional default is GET
                connector.setRequestMethod("GET");


                StringBuffer response = new StringBuffer();

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connector.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                return new HTTPSResponseWrapper(response.toString(), null);
            } catch (Exception e) {
                return new HTTPSResponseWrapper(null,e);
            }
        }

        @Override
        protected void onPostExecute(HTTPSResponseWrapper response) {
            scheduleCallback(response, handler);
        }
    }

    public static void createToken(HttpCallBack httpCallBack) {
        new AsyncHTTPPostRequest(httpCallBack, "https://api.stripe.com/v1/tokens").execute();
    }

    private static class AsyncHTTPPostRequest extends AsyncTask<Void, Void, HTTPSResponseWrapper>
    {
        public final HttpCallBack handler;
        public final String url;

        public AsyncHTTPPostRequest(HttpCallBack handler, String url)
        {
            this.url = url;
            this.handler = handler;
        }

        @Override
        protected HTTPSResponseWrapper doInBackground(Void... params) {
            try {
                URL obj = new URL(url);

                HttpsURLConnection connector = (HttpsURLConnection) obj.openConnection();

                // optional default is Post
                connector.setRequestMethod("POST");


                connector.setReadTimeout(600000);
                connector.setConnectTimeout(60000); //6 seconds of timeout

                connector.setRequestMethod("POST");
                connector.setDoInput(true);
                connector.setDoOutput(true);

                StringBuffer result = new StringBuffer();
                result.append(URLEncoder.encode("card[number]", "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode("4242424242424242", "UTF-8"));
                result.append("&");
                result.append(URLEncoder.encode("card[exp_month]", "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode("12", "UTF-8"));
                result.append("&");
                result.append(URLEncoder.encode("card[exp_year]", "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode("2016", "UTF-8"));
                result.append("&");
                result.append(URLEncoder.encode("card[cvc]", "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode("123", "UTF-8"));

                OutputStream os = connector.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(result.toString());
                writer.flush();
                writer.close();
                os.close();

                int responseCode = connector.getResponseCode();
                StringBuffer response = new StringBuffer("");

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(connector.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response.append(line);
                    }
                }
                else {
                    response = response.replace(0, response.length(), "" + responseCode);
                }

                return new HTTPSResponseWrapper(response.toString(), null);
            } catch (Exception e) {
                return new HTTPSResponseWrapper(null,e);
            }
        }

        @Override
        protected void onPostExecute(HTTPSResponseWrapper response) {
            scheduleCallback(response, handler);
        }
    }

    private static class HTTPSResponseWrapper {
        public final String response;
        public final Exception error;

        private HTTPSResponseWrapper(String response, Exception error)
        {
            this.error = error;
            this.response = response;
        }
    }

    private static void scheduleCallback(HTTPSResponseWrapper result, HttpCallBack callback) {
        if (result.response != null)
            callback.processResponse(result.response);
        else if (result.error != null)
            callback.processFailure(result.error);
        else
            callback.processFailure(
                    new RuntimeException("Did not receive https response or an error"));
    }

    private interface JsonCallBack
    {

    }
}
