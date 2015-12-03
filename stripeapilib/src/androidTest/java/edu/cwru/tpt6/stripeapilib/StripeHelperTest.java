package edu.cwru.tpt6.stripeapilib;

import android.util.Log;

import com.stripe.android.model.Card;

import junit.framework.TestCase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by timlax on 12/3/15.
 */
public class StripeHelperTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
        Log.d("TapJar", "SetupRun");
        StripeHelper.setAuthenticator();
    }

    public void testCreateChargeWithID() throws Exception
    {
        //StripeHelper.setAuthenticator();
        //Card tipperCard = new Card("4242424242424242", 12, 2016, "123");

        //final CountDownLatch methodEnder = new CountDownLatch(1);

//        StripeHelper.accessCharges(new HttpCallBack() {
//            @Override
//            public void processResponse(String response) {
//                Log.d("Tapjar", "process response: "+ response);
//                //methodEnder.countDown();
//            }
//
//            @Override
//            public void processFailure(Exception e) {
//                Log.d("Tapjar", "processFailure: "+ e.getMessage());
//                //methodEnder.countDown();
//            }
//        });
       // methodEnder.await();

        //assertFalse("Test completed", true);
    }

    public void testAccessCharges() throws Exception {
        final CountDownLatch methodEnder = new CountDownLatch(1);

        StripeHelper.accessCharges(new HttpCallBack() {
            @Override
            public void processResponse(String response) {
                Log.d("Tapjar", "process response: " + response);
                methodEnder.countDown();
            }

            @Override
            public void processFailure(Exception e) {
                Log.d("Tapjar", "processFailure: " + e.getMessage());
                methodEnder.countDown();
            }
        });
        assertTrue(methodEnder.await(3,TimeUnit.SECONDS));
    }


    public void testCreateToken() throws Exception {
        final CountDownLatch methodEnder = new CountDownLatch(1);

        StripeHelper.createToken(new HttpCallBack() {
            @Override
            public void processResponse(String response) {
                Log.d("Tapjar", "process response: " + response);
                methodEnder.countDown();
            }

            @Override
            public void processFailure(Exception e) {
                Log.d("Tapjar", "processFailure: " + e.getMessage());
                methodEnder.countDown();
            }
        });
        assertTrue(methodEnder.await(3,TimeUnit.SECONDS));
    }
}