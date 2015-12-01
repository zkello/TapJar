package edu.cwru.tpt6.stripeapilib;

import com.stripe.model.Card;
import com.stripe.model.Token;

/**
 * Created by timlax on 12/1/15.
 */
public class ChargeInfo
{
    public final String id; //Id of charge
    public final int amount; //amount charged
    public final Token token; //Token used to create this charge
    public final Card card; //card

    /**
     * Should not be invoked by code outside of library. Used by responses to create tokens.
     **/
    public ChargeInfo(String id, int amount, Token token)
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

    public Token getToken() {
        return token;
    }

    public Card getCard() {
        return card;
    }
}
