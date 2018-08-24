package com.yyz.cyuanw.oss.sample;

import android.os.Handler;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.yyz.cyuanw.oss.app.Config;

import java.lang.ref.WeakReference;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by zhouzhuo on 12/3/15.
 */
public class BatchUploadSamples extends BaseSamples {

    private String testBucket;
    private List<String> uploadFilePaths;
    private WeakReference<Handler> handler;

    public BatchUploadSamples(OSS client, String testBucket, List<String> uploadFilePaths, Handler handler) {
        this.oss = client;
        this.testBucket = testBucket;
        this.uploadFilePaths = uploadFilePaths;
        this.handler = new WeakReference<Handler>(handler);
    }

    // Resumable upload without checkpoint directory.
    public void upload() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < uploadFilePaths.size(); i++) {
                    String filePath = uploadFilePaths.get(i);
                    syncPutObject(testBucket, getFileName(filePath), filePath);
                }
                if (handler.get() != null) {
                    handler.get().sendEmptyMessage(Config.UPLOAD_SUC);
                }
            }
        });
        thread.start();
    }


    public static String getFileName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('/');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    // 从本地文件上传，采用阻塞的同步接口
    public void syncPutObject(String bucket, String object, String filePath) {
        Log.d(TAG, "filePath : " + filePath);
        Log.d(TAG, "object : " + object);
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucket, object, filePath);

        try {
            PutObjectResult putResult = oss.putObject(put);
            Log.d("PutObject", "UploadSuccess");

            Log.d("ETag", putResult.getETag());
            Log.d("RequestId", putResult.getRequestId());

        } catch (ClientException e) {
            // 本地异常如网络异常等
            e.printStackTrace();
        } catch (ServiceException e) {
            // 服务异常
            Log.e("RequestId", e.getRequestId());
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
        }
    }


}
