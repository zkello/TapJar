package edu.cwru.tpt6.stripeapilib;

import com.stripe.android.model.Card;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by timlax on 12/3/15.
 */
public class JSONDecoder
{

    public static TokenWrapper  getTokenFromResponse(String jsonInfo, Card card)
            throws JSONException {
        JSONObject jsonWrapper = new JSONObject(jsonInfo);
        return new TokenWrapper((String) jsonWrapper.get("id"), card);
    }

    public static ChargeWrapper getChargeFromResponse(String jsonInfo)
    {
        return null;
    }

    public static AccountWrapper getAcctFromResponse(String jsonInfo, String email)
    {
        return null;
    }
}
