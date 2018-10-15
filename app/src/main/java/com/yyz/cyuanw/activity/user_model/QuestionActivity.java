package com.yyz.cyuanw.activity.user_model;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.view.CustomProgress;

import butterknife.BindView;
import butterknife.OnClick;

public class QuestionActivity extends BaseActivity{

    @BindView(R.id.id_tv_title) TextView titleView;
    @BindView(R.id.id_webview) WebView webView;

    private String url;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_question;
    }

    @Override
    public void initView() {
        setTitle(titleView,"常见问题");

        url = getIntent().getStringExtra("url");

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        //进度监听
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    CustomProgress.dismis();
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setBuiltInZoomControls(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);

        CustomProgress.show(this, "加载中...", true, null);

        if (url != null){
            webView.loadUrl(url);
        }else{
            webView.loadUrl(Constant.QUESTION);
        }

    }

    @Override
    public void initData() {

    }

}


