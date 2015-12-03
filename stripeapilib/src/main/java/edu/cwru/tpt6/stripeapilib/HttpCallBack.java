package edu.cwru.tpt6.stripeapilib;

/**
 * Created by timlax on 12/3/15.
 */
public interface HttpCallBack
{
    void processResponse(String response);
    void processFailure(Exception e);
}
