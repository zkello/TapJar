package edu.cwru.tpt6.stripeapilib;

import android.os.AsyncTask;

import com.stripe.android.model.Card;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by timlax on 12/1/15.
 */
public class StripeHelper {

    private static final String credentialKey = "sk_test_JeEPsy1W0H1stdpnBE2Ezx6c";
    public int test = 0;
    //Create a stripeHelper from the key you are given
    public StripeHelper() {
        setAuthenticator();
    }

    private static void setAuthenticator()
    {
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(credentialKey,
                        "".toCharArray());

            }
        });
    }

    public void createChargeWithID(TokenWrapper token, AccountWrapper employeeAcct, int amnt,
            HttpCallBack responseHandler)
    {
        List<PostParameter> params = new LinkedList<>();

        params.add(new PostParameter("amount", "" + amnt));
        params.add(new PostParameter("currency", "usd"));
        params.add(new PostParameter("source", token.getId()));

        new AsyncHTTPPostRequest(
                responseHandler, "https://api.stripe.com/v1/charges", params, employeeAcct.getId()
        ).execute();
    }

    public void openAcctWithEmail(HttpCallBack responseHandler, String employeeEmail)
    {
        List<PostParameter> params = new LinkedList<>();

        params.add(new PostParameter("country", "US"));
        params.add(new PostParameter("managed", "false" ));
        params.add(new PostParameter("email", employeeEmail));

        new AsyncHTTPPostRequest(
                responseHandler, "https://api.stripe.com/v1/accounts", params).execute();
    }

    public void createTokenWithDefaultCard(HttpCallBack httpCallBack) {
        List<PostParameter> params = new LinkedList<>();
        new Card("4242424242424242", 12, 2016, "123");
        params.add(new PostParameter("card[number]", "4242424242424242"));
        params.add(new PostParameter("card[exp_month]", "12" ));
        params.add(new PostParameter("card[exp_year]", "2016"));
        params.add(new PostParameter("card[cvc]", "123"));

        new AsyncHTTPPostRequest(
                httpCallBack, "https://api.stripe.com/v1/tokens", params).execute();
    }

    public void createTokenWithCard(Card card, HttpCallBack httpCallBack) {
        List<PostParameter> params = new LinkedList<>();

        params.add(new PostParameter("card[number]", card.getNumber()));
        params.add(new PostParameter("card[exp_month]", card.getExpMonth().toString()));
        params.add(new PostParameter("card[exp_year]", card.getExpYear().toString()));
        params.add(new PostParameter("card[cvc]", card.getCVC().toString()));

        new AsyncHTTPPostRequest(
                httpCallBack, "https://api.stripe.com/v1/tokens", params).execute();
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

    private final static class PostParameter
    {
        private final String key;
        private final String value;

        public PostParameter(String key, String value)
        {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    private static String createEncodedParams(List<PostParameter> params)
            throws UnsupportedEncodingException {
        StringBuffer result = new StringBuffer();
        boolean first = true;

        for (PostParameter keyValuePair : params) {
            if (first) {
                first = false;
            }
            else {
                result.append("&");
            }
            result.append(URLEncoder.encode(keyValuePair.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(keyValuePair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public void accessCharges(HttpCallBack httpCallBack) {
        new AsyncHTTPGetRequest(httpCallBack, "https://api.stripe.com/v1/charges").execute();
    }
    private static class AsyncHTTPPostRequest extends AsyncTask<Void, Void, HTTPSResponseWrapper>
    {
        public final HttpCallBack handler;
        public final String url;
        public final List<PostParameter> parameterList;
        public final String employeeAcctIdNumber;

        public AsyncHTTPPostRequest(
                HttpCallBack handler, String url, List<PostParameter> parameterList)
        {
            this.url = url;
            this.handler = handler;
            this.parameterList = new ArrayList(parameterList);
            this.employeeAcctIdNumber = null; //is not a charge request by default
        }

        public AsyncHTTPPostRequest(
                HttpCallBack handler,
                String url, List<PostParameter> parameterList,
                String employeeAcctIdNumber)
        {
            this.url = url;
            this.handler = handler;
            this.parameterList = new ArrayList(parameterList);
            this.employeeAcctIdNumber = employeeAcctIdNumber; //is chargeRequest If Specified
        }

        @Override
        protected HTTPSResponseWrapper doInBackground(Void... params) {
            try {
                URL obj = new URL(url);

                HttpsURLConnection connector = (HttpsURLConnection) obj.openConnection();

                connector.setRequestMethod("POST");
                if(employeeAcctIdNumber != null)
                {
                    connector.setRequestProperty("Stripe-Account",employeeAcctIdNumber);
                }
                //3 seconds before timeout
                connector.setReadTimeout(3000);
                connector.setConnectTimeout(3000);
                connector.setDoInput(true);
                connector.setDoOutput(true);

                //Write
                OutputStream os = connector.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(createEncodedParams(parameterList));
                writer.flush();
                writer.close();
                os.close();

                //get received info now
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
                    throw new Exception("Received invalid responseCode: "+ responseCode);
                }

                return new HTTPSResponseWrapper(response.toString(), null);

            } catch (Exception e) {
                return new HTTPSResponseWrapper(null, e);
            }
        }

        @Override
        protected void onPostExecute(HTTPSResponseWrapper response) {
            scheduleCallback(response, handler);
        }
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
}
