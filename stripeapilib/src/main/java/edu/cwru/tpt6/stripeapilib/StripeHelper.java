package edu.cwru.tpt6.stripeapilib;

import com.stripe.model.Token;
import com.stripe.exception.AuthenticationException;

/**
 * Created by timlax on 12/1/15.
 */
public class StripeHelper {

    //Create a stripeHelper from the key you are given
    public StripeHelper(String StripeKey) throws AuthenticationException {
        if (StripeKey == null || StripeKey.length() == 0) {
            throw new AuthenticationException("Invalid Key: You must use a valid " +
                    "key to create a token.  For more info, see " +
                    "https://stripe.com/docs/stripe.js.");
        }
    }

    public void createChargeWithID(
            Token token, String employeeIdentifier, int amnt, ChargeCallback responseHandler) {
        //TODO to implement
    }
}
