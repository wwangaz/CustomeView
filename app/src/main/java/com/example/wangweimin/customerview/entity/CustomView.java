package com.example.wangweimin.customerview.entity;

import java.io.Serializable;

/**
 * Created by wangweimin on 16/10/18.
 */

public class CustomView implements Serializable {
    public String name;
    public int iconId;

    public CustomView(String mName, int mId){
        name = mName;
        iconId = mId;
    }

    public CustomView(String mName){
        name = mName;
    }
}
