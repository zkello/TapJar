package edu.cwru.tpt6.stripeapilib;

import com.stripe.android.model.Card;

/**
 * Created by timlax on 12/1/15.
 */
public class ChargeWrapper
{
    public final String id; //Id of charge
    public final int amount; //amount charged
    public final TokenWrapper token; //Token used to create this charge
    public final Card card; //card

    /**
     * Should not be invoked by code outside of library. Used by responses to create tokens.
     **/
    public ChargeWrapper(String id, int amount, TokenWrapper token)
    {
        this.id = id;
        this.amount = amount;
        this.token = token;
        this.card = token != null ? token.getCard() : null;
    }

    public String getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public TokenWrapper getToken() {
        return token;
    }

    public Card getCard() {
        return card;
    }
}
