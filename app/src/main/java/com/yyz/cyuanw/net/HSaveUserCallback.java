//package com.yyz.cyuanw.net;
//
//import com.exingxiao.insureexpert.model.CommonRealmUtils;
//import com.exingxiao.insureexpert.model.been.UserBeen;
//import com.exingxiao.insureexpert.receiver.leancloud.Leancloud;
//import com.exingxiao.insureexpert.tools.GlobalDatas;
//import com.exingxiao.insureexpert.tools.Json;
//
///**
// * Created by Administrator on 2017/7/25 0025.
// */
//
//public class HSaveUserCallback extends HCallback {
//    private boolean needInitLeancloud = true;
//
//    public HSaveUserCallback() {
//
//    }
//
//    public HSaveUserCallback(boolean needInitLeancloud) {
//        this.needInitLeancloud = needInitLeancloud;
//    }
//
//    @Override
//    public void onResponse(HResult result) {
//        if (result.getCode() == 200) {
//            GlobalDatas.clearUser();
//
//            UserBeen userBeen = Json.parseObject(result.getData(), UserBeen.class);
//            userBeen.setState(UserBeen.STATE_CURR_LOGIN);
//            CommonRealmUtils.i(userBeen);
//
//            if (needInitLeancloud) {
//                Leancloud leancloud = new Leancloud();
//                leancloud.initInstallation(userBeen.getUser_uuid(), userBeen.getUser_level(), userBeen.getSpecialty(), userBeen.getAddress(),userBeen.getAddress_code());
//            }
//        }
//    }
//}
