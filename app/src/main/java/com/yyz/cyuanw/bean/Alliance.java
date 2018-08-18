package com.yyz.cyuanw.bean;

public class Alliance {
    public int id,province_id,city_id,region_id,leader_user_id,audit_status,is_dissolve,is_hot;
    public String name,logo,intro,qr_code,audit_status_text;
    private Province province;
    public City city;
    public Region region;
    public Leader leader;
}
