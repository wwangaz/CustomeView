package com.example.wangweimin.customerview.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: wayne
 * Date: 2018/1/4
 * Description: describe the class here
 */

public class GroupItem {

    private int startPosition;//起始position
    private Map<String,Object> dataMap;

    public GroupItem(int startPosition){
        this.startPosition = startPosition;
        dataMap = new HashMap<>();
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setData(String key,Object data){
        dataMap.put(key,data);
    }

    public Object getData(String key){
        return dataMap.get(key);
    }

}
