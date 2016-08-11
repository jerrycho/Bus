package com.jerry.bus.domain;


import com.orm.SugarRecord;

public class MyBusCallParam extends SugarRecord {
    public String direction;
    public String searchBusNo;
    public String busStopNameEN;
    public String busStopNameTC;

    public String bound;
    public String stopCode;
    public String stopSeq;
}
