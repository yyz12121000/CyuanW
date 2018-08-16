package com.yyz.cyuanw.bean;

import java.util.List;

public class HttpListResult<T> {
    public int code;
    public String message;
    public List<T> data;

}
