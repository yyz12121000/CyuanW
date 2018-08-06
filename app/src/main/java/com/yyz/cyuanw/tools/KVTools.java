//package com.exingxiao.insureexpert.tools;
//
//
//import com.exingxiao.insureexpert.App;
//import com.exingxiao.insureexpert.model.been.KeyValueBeen;
//
//import io.realm.Realm;
//
///**
// * Created by Administrator on 2017/5/21.
// */
//
//public class KVTools {
//    public static final String KEY_NOTICE_JSON_STR = "key_notice_json_str";
//
//
//    public static void put(final String key, final String value) {
//        Realm realm = Realm.getDefaultInstance();
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                KeyValueBeen keyValue = new KeyValueBeen();
//                keyValue.setKey(key);
//                keyValue.setValue(value);
//                realm.copyToRealmOrUpdate(keyValue);
//            }
//        });
//        realm.close();
//    }
//
//    public static String get(String key) {
//        Realm realm = Realm.getDefaultInstance();
//        KeyValueBeen keyValue = realm.where(KeyValueBeen.class).equalTo("key", key).findFirst();
//        realm.close();
//        if (null != keyValue) {
//            return keyValue.getValue();
//        }
//        return "";
//    }
//}
