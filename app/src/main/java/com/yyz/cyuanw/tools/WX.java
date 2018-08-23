package com.yyz.cyuanw.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bumptech.glide.util.Util;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yyz.cyuanw.App;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

public class WX {
    private static final String APP_ID = "wxf8f374f3066cae24";
    private IWXAPI api;

    public WX(){
        regToWx();
    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(App.context, APP_ID, true);
        api.registerApp(APP_ID);
    }

    public void share(boolean friendsCircle, String title, String content, String img, String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = url;//分享url
                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = title;
                msg.description = content;

                byte[] arr = getBitmap(img);
                if (null != arr) {
                    msg.thumbData = arr;
                }


                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = msg;
                req.scene = friendsCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                api.sendReq(req);
            }
        }).start();



    }

    private byte[] getBitmap(String url) {
        try {
            Bitmap thumb = BitmapFactory.decodeStream(new URL(url).openStream());
            //注意下面的这句压缩，120，150是长宽。
            //一定要压缩，不然会分享失败
            Bitmap thumbBmp = Bitmap.createScaledBitmap(thumb, 120, 150, true);
            //Bitmap回收
            thumb.recycle();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public void share(String title,String content,String img,String url){
//        WXWebpageObject webpage = new WXWebpageObject();
//        webpage.webpageUrl = url;
//
//        WXMediaMessage msg = new WXMediaMessage(webpage);
//        msg.title = title;
//        msg.description = content;
//
//
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buidTansaction("");
//
//        req.message = msg;
//        req.scene =
//    }

}
