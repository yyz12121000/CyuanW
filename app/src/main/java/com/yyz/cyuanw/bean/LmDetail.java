package com.yyz.cyuanw.bean;

import java.util.List;

public class LmDetail {
    public int id,province_id,city_id,region_id,audit_status;
    public String name,logo,intro,qr_code,province_city_region_text;
    public Leader leader;
    public List<Data14> users;
}
