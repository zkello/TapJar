package edu.cwru.tpt6.stripeapilib;

/**
 * Created by timlax on 12/3/15.
 */
public interface HttpCallBack
{
    void processResponse(String response);//TODO maybechange response to a specific object
    void processFailure(Exception e);
}
