package com.yyz.cyuanw.net;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/1 0001.
 */
public class Server {
    private static final String FINAL_HOST = "http://www.e-xingxiao.cn/";

    public static String HOST_SHOP = "";
    public static String HOST_IMG = "";
    public static String HOST = "";

//    public static String getCaseShare(int caseInfoId) {
//        String params = "?caseInfoId=" + caseInfoId + "&review_status=1";
//        return HOST + URL_SHARE_FORDETAILS + params;
//    }
//
//    /**
//     * 图片上传接口
//     *
//     * @param callback
//     */
//    public static void uploadImg(HashMap<String, String> filePaths, String bucketName, HCallback callback) {
//        Http.uploadImg(HOST_IMG + URL_UPLOAD_IMG, filePaths, bucketName, callback);
//    }
//
//    /**
//     * 发送验证码
//     *
//     * @param mobile
//     * @param callback
//     */
    public static void smsSend(String mobile, int type, HCallback callback) {
        HashMap params = new HashMap();
        params.put("mobile", mobile);
        if (type == 1) {
            params.put("type", type);
        }
        Http.post(HOST + "", params, callback);
    }
//
//    /**
//     * 邀请函预览
//     *
//     * @param map
//     * @return
//     */
//    public static String invitationPreview(HashMap<String, String> map) {
//        String json = JSON.toJSONString(map);
//        LogManager.d("加密前：" + json);
//        json = AESOperator.getInstance().encrypt(json);
//        json = json.replace("+", "nbsp");
//        return HOST + URL_SHARE_DETAIL + "?type=" + H5_TYPE_INVITATION + "&data=" + json + "&app_type=1";
//    }

}
