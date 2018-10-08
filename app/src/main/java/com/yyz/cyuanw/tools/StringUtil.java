package com.yyz.cyuanw.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class StringUtil {

    public static boolean isNotNull(String str) {
        if (str != null && !"".equals(str.trim()) && !"null".equalsIgnoreCase(str.trim())&&!"[]".equals(str.trim()))
            return true;
        return false;
    }

    public static String getDayFormat(String year, String month, String day){
        return year+"-"+month+"-"+day;
    }

    public static String getMonthFormat(String year, String month){
        return year+"-"+month;
    }

    public static String getYesterDayDate(String date, int flag){
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date parseDate = simpleDateFormat.parse(date);

            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(parseDate);

            int day = calendar.get(Calendar.DATE);
            if (flag == 0){
                calendar.set(Calendar.DATE, day - 1);
            }else if(flag == 1){
                calendar.set(Calendar.DATE, day + 1);
            }else{
                calendar.set(Calendar.DATE, day - 7);
            }

            String format = simpleDateFormat.format(calendar.getTime());

            return format;
        }catch (Exception e){
            return date;
        }

    }

    public static int compareDate(String date1, String date2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static String stringCenter(String str) {
        String s = "1234567890";
        byte[] b = s.getBytes();
        byte[] c = new byte[b.length];
        for (int i = b.length - 1; i >= 0; i--) {
            c[i] = b[b.length - i - 1];
        }
        s = new String(c);
        return s;
    }

    public static String formatPrice(Double price){
        return String.format("%.2f", price).toString();
    }

    public static String getCarDetailHtml(List<String> imgs){

        StringBuilder data = new StringBuilder();
        data.append("<html><head><meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no\" /><link rel=\"stylesheet\" href=\"http://sf.vsea.com.cn/mobile1//assets/css/common.css\"/></head>");
        int size = imgs.size();
        for (int i = 0; i < size; i++) {
            data.append("<img src=\"").append(imgs.get(i)).append("\"").append("<br>");
        }
        data.append("</html>");

        return data.toString();
    }

}
