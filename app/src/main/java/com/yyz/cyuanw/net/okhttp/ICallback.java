package com.yyz.cyuanw.okhttp;

        import java.io.IOException;

        import okhttp3.Request;
        import okhttp3.Response;


public interface ICallback {
    void onResponse(Response response) throws IOException;
    void onFailure(Request arg0, Exception arg1);
}

