package edu.cwru.tpt6.stripeapilib;

import com.stripe.android.model.Card;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by timlax on 12/3/15.
 */
public class JSONDecoder
{

    public static TokenWrapper  getTokenFromString(String jsonInfo, Card card)
            throws JSONException {
        JSONObject jsonWrapper = new JSONObject(jsonInfo);
        return new TokenWrapper((String) jsonWrapper.get("id"), card);
    }
}
