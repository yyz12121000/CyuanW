package com.yyz.cyuanw.view;

import android.content.Context;
import android.util.AttributeSet;

import com.andview.refreshview.XRefreshView;

public class PullRV extends XRefreshView {
    public int page = 1;

    public PullRV(Context context, AttributeSet attrs) {
        super(context, attrs);

        //设置刷新完成以后，headerview固定的时间
        setPinnedTime(1000);
        setMoveForHorizontal(true);
        setPullLoadEnable(true);
        setAutoLoadMore(false);
//        adapter.setCustomLoadMoreView(new XRefreshViewFooter(this));
        enableReleaseToLoadMore(true);
        enableRecyclerViewPullUp(true);
        enablePullUpWhenLoadCompleted(true);
    }

    public void checkhasMore(int size) {
        if (size == 0) {
            // 刷新完成必须调用此方法停止加载
            stopLoadMore(true);
        } else {
            // 刷新完成必须调用此方法停止加载
            stopLoadMore(false);
        }

        //当数据加载失败 不需要隐藏footerview时，可以调用以下方法，传入false，不传默认为true
        // 同时在Footerview的onStateFinish(boolean hideFooter)，可以在hideFooter为false时，显示数据加载失败的ui
//                            xRefreshView1.stopLoadMore(false);
    }

}
