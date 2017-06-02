package com.infoservices.lue.condomanagement;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by lue on 02-06-2017.
 */

public class StateId {
    int id;
    String name="";

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  StateId(JSONObject jsonObject){
        if (jsonObject!=null){
            try {
                this.id=jsonObject.getInt("id");
            }catch (Exception e){}
            try {
                this.name=jsonObject.getString("state_name");
            }catch (Exception e){}
        }

    }
}

