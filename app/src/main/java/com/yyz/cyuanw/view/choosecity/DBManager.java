package com.yyz.cyuanw.view.choosecity;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.yyz.cyuanw.bean.Data9;
import com.zaaach.citypicker.model.City;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.zaaach.citypicker.db.DBConfig.COLUMN_C_CODE;
import static com.zaaach.citypicker.db.DBConfig.COLUMN_C_NAME;
import static com.zaaach.citypicker.db.DBConfig.COLUMN_C_PINYIN;
import static com.zaaach.citypicker.db.DBConfig.COLUMN_C_PROVINCE;
import static com.zaaach.citypicker.db.DBConfig.LATEST_DB_NAME;
import static com.zaaach.citypicker.db.DBConfig.TABLE_NAME;

/**
 * Author Bro0cL on 2016/1/26.
 */
public class DBManager {
    private static final int BUFFER_SIZE = 1024;

    private String DB_PATH;
    private Context mContext;

    public DBManager(Context context) {
        this.mContext = context;
        DB_PATH = File.separator + "data"
                + Environment.getDataDirectory().getAbsolutePath() + File.separator
                + context.getPackageName() + File.separator + "databases" + File.separator;
//        copyDBFile();
    }

//    private void copyDBFile(){
//        File dir = new File(DB_PATH);
//        if (!dir.exists()){
//            dir.mkdirs();
//        }
//        //如果旧版数据库存在，则删除
//        File dbV1 = new File(DB_PATH + DB_NAME_V1);
//        if (dbV1.exists()){
//            dbV1.delete();
//        }
//        //创建新版本数据库
//        File dbFile = new File(DB_PATH + LATEST_DB_NAME);
//        if (!dbFile.exists()){
//            InputStream is;
//            OutputStream os;
//            try {
//                is = mContext.getResources().getAssets().open(LATEST_DB_NAME);
//                os = new FileOutputStream(dbFile);
//                byte[] buffer = new byte[BUFFER_SIZE];
//                int length;
//                while ((length = is.read(buffer, 0, buffer.length)) > 0){
//                    os.write(buffer, 0, length);
//                }
//                os.flush();
//                os.close();
//                is.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    public List<City> getAllCities(){
//        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + LATEST_DB_NAME, null);
//        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
//        List<City> result = new ArrayList<>();
//        City city;
//        while (cursor.moveToNext()){
//            String name = cursor.getString(cursor.getColumnIndex(COLUMN_C_NAME));
//            String province = cursor.getString(cursor.getColumnIndex(COLUMN_C_PROVINCE));
//            String pinyin = cursor.getString(cursor.getColumnIndex(COLUMN_C_PINYIN));
//            String code = cursor.getString(cursor.getColumnIndex(COLUMN_C_CODE));
//            city = new City(name, province, pinyin, code);
//            result.add(city);
//        }
//        cursor.close();
//        db.close();
//        Collections.sort(result, new CityComparator());
//        return result;
//    }

    public List<City> searchCity(final String keyword) {
        String sql = "select * from " + TABLE_NAME + " where "
                + COLUMN_C_NAME + " like ? " + "or "
                + COLUMN_C_PINYIN + " like ? ";
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + LATEST_DB_NAME, null);
        Cursor cursor = db.rawQuery(sql, new String[]{"%" + keyword + "%", keyword + "%"});

//        List<City> result = new ArrayList<>();
//        while (cursor.moveToNext()) {
//            String name = cursor.getString(cursor.getColumnIndex(COLUMN_C_NAME));
//            String province = cursor.getString(cursor.getColumnIndex(COLUMN_C_PROVINCE));
//            String pinyin = cursor.getString(cursor.getColumnIndex(COLUMN_C_PINYIN));
//            String code = cursor.getString(cursor.getColumnIndex(COLUMN_C_CODE));
//            City city = new City(name, province, pinyin, code);
//            result.add(city);
//        }
//        cursor.close();
//        db.close();

        List<City> list = getAllCities();
        List<City> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().contains(keyword)) {
                result.add(list.get(i));
            }
        }

        CityComparator comparator = new CityComparator();
        Collections.sort(result, comparator);
        return result;
    }

    /**
     * sort by a-z
     */
    private class CityComparator implements Comparator<City> {
        @Override
        public int compare(City lhs, City rhs) {
            String a = lhs.getPinyin().substring(0, 1);
            String b = rhs.getPinyin().substring(0, 1);
            return a.compareTo(b);
        }
    }

    public List<City> getAllCities() {
        return parseSheng();
    }

    private List<City> parseSheng() {
        String jsonStr = getJson("address.txt", mContext);
        List<City> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(jsonStr);
            for (int i = 0; i < array.length(); i++) {
                JSONObject sheng = array.optJSONObject(i);
                City sheng_data = new City("", "", "", "");
                sheng_data.setCode(sheng.optInt("id") + "");
                sheng_data.setName(sheng.optString("name"));
                sheng_data.setPinyin(sheng.optString("pinyin"));
                sheng_data.setName(sheng.optString("name"));
                list.add(sheng_data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(list, new CityComparator());
        return list;
    }
    public List<Data9> getQuByShengId(int id) {
        try {
            List<Data9> list = parse();
            for (int i = 0; i < list.size(); i++) {
                List<Data9> shi_list = list.get(i).son;
                for (int j = 0; j < shi_list.size(); j++) {
                    if (shi_list.get(j).id == id) {
                        return shi_list.get(j).son;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    public List<Data9> getCityByShengId(int id) {
//        String jsonStr = getJson("address.txt", mContext);
        try {
            List<Data9> list = parse();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).id == id) {
                    return list.get(i).son;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Data9 findLocationCity(String cityNmae) {
        String keyword = cityNmae;
        if (cityNmae.lastIndexOf("市") == cityNmae.length() - 1) {
            keyword = cityNmae.substring(0, cityNmae.length() - 1);
        }
        List<Data9> list = parse();
        for (int i = 0; i < list.size(); i++) {
            Data9 sheng = list.get(i);
            if (null == sheng.son) {
                continue;
            }
            for (int j = 0; j < sheng.son.size(); j++) {
                Data9 shi = sheng.son.get(j);
                if (shi.name.contains(keyword)) {
                    List<Data9> shengNewSon = new ArrayList<>();
                    shengNewSon.add(shi);
                    sheng.son = shengNewSon;
                    return sheng;
                }
            }
        }
        return null;
    }

    private List<Data9> parse() {
        String jsonStr = getJson("address.txt", mContext);
        List<Data9> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(jsonStr);
            for (int i = 0; i < array.length(); i++) {
                JSONObject sheng = array.optJSONObject(i);
                Data9 sheng_data = new Data9();
                sheng_data.id = sheng.optInt("id");
                sheng_data.name = sheng.optString("name");
                sheng_data.initials = sheng.optString("initials");
                sheng_data.pinyin = sheng.optString("pinyin");
                sheng_data.son = new ArrayList<>();

                JSONArray sheng_son = sheng.optJSONArray("son");
                if (null == sheng_son || sheng_son.length() == 0) {
                    continue;
                }
                for (int j = 0; j < sheng_son.length(); j++) {
                    JSONObject shi = sheng_son.optJSONObject(j);
                    Data9 shi_data = new Data9();
                    shi_data.id = shi.optInt("id");
                    shi_data.name = shi.optString("name");
                    shi_data.initials = shi.optString("initials");
                    shi_data.pinyin = shi.optString("pinyin");
                    shi_data.son = new ArrayList<>();
                    JSONArray shi_son = shi.optJSONArray("son");
                    if (null == shi_son || shi_son.length() == 0) {
                        continue;
                    }
                    for (int k = 0; k < shi_son.length(); k++) {
                        JSONObject qu = shi_son.optJSONObject(k);
                        Data9 qu_data = new Data9();
                        qu_data.id = qu.optInt("id");
                        qu_data.name = qu.optString("name");
                        qu_data.initials = qu.optString("initials");
                        qu_data.pinyin = qu.optString("pinyin");
                        shi_data.son.add(qu_data);
                    }
                    sheng_data.son.add(shi_data);
                }
                list.add(sheng_data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
