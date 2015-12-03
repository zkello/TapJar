package edu.cwru.tpt6.stripeapilib;

import com.stripe.android.model.Card;

/**
 * Created by timlax on 12/1/15.
 */
public class ChargeWrapper
{
    private final String id; //Id of charge
    private final int amount; //amount charged
    private final TokenWrapper token; //Token used to create this charge
    private final Card card; //card
    private final AccountWrapper acct; //Employee we are tipping

    /**
     * Should not be invoked by code outside of library. Used by responses to create tokens.
     **/
    public ChargeWrapper(String id, int amount, TokenWrapper token, AccountWrapper acct)
    {
        if(id == null || token == null || acct == null) {
            throw new NullPointerException("A value passed to ChargeWrapper constructor was null");
        }

        this.id = id;
        this.amount = amount;
        this.token = token;
        this.card = token.getCard();
        this.acct = acct;
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
