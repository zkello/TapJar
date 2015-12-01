package edu.cwru.tpt6.stripeapilib;

/**
 * Created by timlax on 12/1/15.
 */
public abstract class ChargeCallback {
    public abstract void onError(Exception error);
    public abstract void onSuccess(ChargeInfo charge);
}
