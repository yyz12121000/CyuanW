package com.yyz.cyuanw.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/30 0030.
 */

public class Json {
    public static final <T> List<T> parseArray(String json, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            list = JSON.parseArray(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static final <T> T parseObject(String json, Class<T> clazz) {
        T t = null;
        try {
            t = JSON.parseObject(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
    public static Map<String, Object> jsonToMap(String jsonStr){
        Map<String, Object> map = JSON.parseObject(
                jsonStr,new TypeReference<Map<String, Object>>(){} );
        return map;
    }
}
