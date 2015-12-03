package edu.cwru.tpt6.stripeapilib;

/**
 * Created by timlax on 12/3/15.
 */
public class AccountWrapper {

    public final String id; //Id of Acct
    public final String email;

    /**
     * Should not be invoked by code outside of library. Used by responses to create tokens.
     **/
    public AccountWrapper(String id, String email)
    {
        if(id == null || email == null) throw new NullPointerException("");
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
