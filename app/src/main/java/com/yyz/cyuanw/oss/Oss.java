package com.yyz.cyuanw.oss;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.tools.LogManager;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import rx.Observer;

public class Oss {
    public static String AccessKeyId = "";
    public static String AccessKeySecret = "";
    public static String Expiration = "";
    public static String SecurityToken = "";

    public static void loadInfo() {
        HttpData.getInstance().ossToken(new Observer<ResponseBody>() {
            @Override
            public void onCompleted() {
//                App.showToast("999");
            }

            @Override
            public void onError(Throwable e) {
//                App.showToast("服务器请求超时");
                LogManager.e(e.getMessage());
            }

            @Override
            public void onNext(ResponseBody result) {
                try {
                    String body = result.string();
                    JSONObject obj = new JSONObject(body);
                    if (obj.getInt("StatusCode") == 200) {
                        AccessKeyId = obj.optString("AccessKeyId");
                        AccessKeySecret = obj.optString("AccessKeySecret");
                        Expiration = obj.optString("Expiration");
                        SecurityToken = obj.optString("SecurityToken");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Oss(String uploadFilePath) {
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        // 在移动端建议使用STS的方式初始化OSSClient，更多信息参考：[访问控制]
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(AccessKeyId, AccessKeySecret, SecurityToken);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSS oss = new OSSClient(App.context, endpoint, credentialProvider, conf);


        // 构造上传请求
//        PutObjectRequest put = new PutObjectRequest("<bucketName>", "<objectKey>", "<uploadFilePath>");
        PutObjectRequest put = new PutObjectRequest("bucketName_CyuanW", "objectKey_CyuanW", uploadFilePath);
// 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");
            }
            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
// task.cancel(); // 可以取消任务
// task.waitUntilFinished(); // 可以等待直到任务完成
    }


}
