package edu.cwru.tpt6.stripeapilib;

import com.stripe.android.model.Card;

/**
 * Created by timlax on 12/3/15.
 */
public final class TokenWrapper
{
    private final String id;
    private final Card card;

    public TokenWrapper(String id, Card card)
    {
        if(id == null || card == null) throw new NullPointerException();

        this.id = id;
        this.card = card;
    }

    public String getId() {
        return id;
    }

    public Card getCard() {
        return card;
    }
}
