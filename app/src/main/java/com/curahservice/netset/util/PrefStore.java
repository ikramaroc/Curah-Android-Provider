package com.curahservice.netset.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.curahservice.netset.gsonModel.createAccount;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PrefStore {

    private Context mAct;
    public PrefStore(Context aAct) {
        this.mAct = aAct;
    }

    private SharedPreferences getPref() {
        return PreferenceManager.getDefaultSharedPreferences(mAct);
    }

    public void cleanPref() {
        SharedPreferences settings = getPref();
        settings.edit().clear().apply();
    }

    public boolean containValue(String key) {
        SharedPreferences settings = getPref();
        return settings.contains(key);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences settings = getPref();
        return settings.getBoolean(key, defaultValue);
    }

    public void setBoolean(String key, boolean value) {
        SharedPreferences settings = getPref();
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void saveString(String key, String value) {
        SharedPreferences settings = getPref();
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defaultVal) {
        SharedPreferences settings = getPref();
        return (settings.getString(key, defaultVal));
    }


    public void saveLong(String key, long value) {
        SharedPreferences settings = getPref();
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public long getLong(String key, long defaultVal) {
        SharedPreferences settings = getPref();
        return settings.getLong(key, defaultVal);
    }

    public void setInt(String subscription_id, int sbu_id) {
        SharedPreferences settings = getPref();
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(subscription_id, sbu_id);
        editor.apply();
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultVal) {
        SharedPreferences settings = getPref();
        return settings.getInt(key, defaultVal);
    }


    public void saveUserPojo(String key, createAccount.UserInfo value) {
        SharedPreferences settings = getPref();
        SharedPreferences.Editor editor = settings.edit();
        Gson gson=  new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(value); // myObject - instance of MyObject
        editor.putString(key, json);
        editor.apply();
    }

    public createAccount.UserInfo getUserPojo(String key) {

        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = getString(key, "");
        createAccount.UserInfo obj = gson.fromJson(json, createAccount.UserInfo.class);
        return obj;
    }



    public  void setSerVices(String servicesList,List<createAccount.Service> arrayList){
        SharedPreferences services = getPref();
        SharedPreferences.Editor editor = services.edit();
        Gson gson = new Gson();

        String json = gson.toJson(arrayList);

        editor.putString(servicesList, json);
        editor.commit();
    }

    public List<createAccount.Service> getServiceList(String servicesList){
        SharedPreferences services = getPref();
        Gson gson = new Gson();
        String json = services.getString(servicesList, "");
        Type type = new TypeToken<List<createAccount.Service>>() {}.getType();
        List<createAccount.Service> arrayList = gson.fromJson(json, type);
        return arrayList;
    }


    public <T extends Object> void setData(String value, ArrayList<createAccount.Service> datas) {
        getPref().edit().putString(value, ObjectSerializer.serialize(datas)).commit();
    }

    public <T extends Object> ArrayList<T> getData(String name) {
        return (ArrayList<T>) ObjectSerializer.deserialize(getPref().
                getString(name, ObjectSerializer.serialize(new ArrayList<T>())));
    }

}