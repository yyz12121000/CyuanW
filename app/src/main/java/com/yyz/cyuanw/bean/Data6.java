package com.yyz.cyuanw.bean;

import java.util.List;

import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public class Data6 {
    public int id, urgent, wholesale, is_new, collection, sell_count, pageviews, power,car_style;
    public String car_number, name, position_number, cover, license_plate_time, mileage, exhibition_hall_quotation, color,car_style_string,
            gearbox, fuel_type, emission_standard, displacement, retail_offer, retail_bottom_price, location, describe, publish_time,
            power_message;
    public List<String> images;
    public Data7 user;
    public Data8 share_info;

}



