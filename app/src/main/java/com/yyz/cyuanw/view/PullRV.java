package com.yyz.cyuanw.view;

import android.content.Context;
import android.util.AttributeSet;

import com.chanven.lib.cptr.PtrClassicFrameLayout;

public class PullRV extends PtrClassicFrameLayout {
    public int page = 1;

    public PullRV(Context context, AttributeSet attrs) {
        super(context, attrs);

        //        pullRV.setLastUpdateTimeRelateObject(this);//设置刷新支持时间
//        pullRV.setResistance(1.7f);
//        pullRV.setDurationToCloseHeader(1000);
//        // 默认为false
//        pullRV.setPullToRefresh(false);
//        // 默认为true
//        pullRV.setKeepHeaderWhenRefresh(true);
    }

    public void refreshFinish(int serverLastPage) {
        refreshComplete();//
        setLoadMoreEnable(true);
        checkhasMore(serverLastPage);
    }

    public void checkhasMore(int serverLastPage) {
        if (serverLastPage != -9) {
            if (page >= serverLastPage) {
                loadMoreComplete(false);
            }else {
                loadMoreComplete(true);
            }
        }
    }

}
