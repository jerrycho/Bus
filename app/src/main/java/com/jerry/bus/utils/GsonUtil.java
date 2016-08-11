package com.jerry.bus.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

public class GsonUtil {

	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static <T> T fromJson(String json, Class<T> cls, boolean returnNullIfError){
		return fromJsonWithDateFormat(DATE_FORMAT,json,cls,returnNullIfError);
	}
	public static <T> T fromJsonWithDateFormat(String dateformat, String json, Class<T> cls, boolean returnNullIfError){
		Gson gson = new Gson();
		if (dateformat!=null){
			gson = new GsonBuilder().setDateFormat(dateformat).create();
		}
		try {
			return gson.fromJson(json, cls);
		}catch (JsonSyntaxException e){
			System.out.println(json);
			e.printStackTrace();
		}
		try {
			if (returnNullIfError){
				return null;
			}else{
				return cls.newInstance();
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T fromJson(String json, Class<T> cls){
		return fromJson(json, cls, false);
	}

	public static <T> T fromJsonWithDateFormat(String dateformat,String json, Type typeOfT){
		Gson gson = new Gson();
		if (dateformat!=null){
			gson = new GsonBuilder().setDateFormat(dateformat).create();
		}
		return gson.fromJson(json, typeOfT);
	}

	public static <T> T fromJson(String json, Type typeOfT){
	    return fromJsonWithDateFormat(DATE_FORMAT, json, typeOfT);
	}
	
	public static String toJson(Object obj){
		Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();
		return gson.toJson(obj);
	}
}

