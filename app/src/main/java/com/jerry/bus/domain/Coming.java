package com.jerry.bus.domain;

/**
 * Created by jerry.cho on 5/8/2016.
 */
public class Coming {

    private ComingTime[] response;

    private String updated;

    private String generated;

    private String responsecode;

    public ComingTime[] getResponse ()
    {
        return response;
    }

    public void setResponse (ComingTime[] response)
    {
        this.response = response;
    }

    public String getUpdated ()
    {
        return updated;
    }

    public void setUpdated (String updated)
    {
        this.updated = updated;
    }

    public String getGenerated ()
    {
        return generated;
    }

    public void setGenerated (String generated)
    {
        this.generated = generated;
    }

    public String getResponsecode ()
    {
        return responsecode;
    }

    public void setResponsecode (String responsecode)
    {
        this.responsecode = responsecode;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", updated = "+updated+", generated = "+generated+", responsecode = "+responsecode+"]";
    }
}
