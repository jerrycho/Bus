package com.jerry.bus.domain;

public class ComingTime
{
    private String w;

    private String ex;

    private String eot;

    private String t;

    private String ei;

    public String getW ()
    {
        return w;
    }

    public void setW (String w)
    {
        this.w = w;
    }

    public String getEx ()
    {
        return ex;
    }

    public void setEx (String ex)
    {
        this.ex = ex;
    }

    public String getEot ()
    {
        return eot;
    }

    public void setEot (String eot)
    {
        this.eot = eot;
    }

    public String getT ()
    {
        return t;
    }

    public void setT (String t)
    {
        this.t = t;
    }

    public String getEi ()
    {
        return ei;
    }

    public void setEi (String ei)
    {
        this.ei = ei;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [w = "+w+", ex = "+ex+", eot = "+eot+", t = "+t+", ei = "+ei+"]";
    }
}