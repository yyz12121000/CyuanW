package com.yyz.cyuanw.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.yyz.cyuanw.tools.UiTool;

import java.util.List;

/**
 */
public class AdViewPagerAdapter extends PagerAdapter {
    private List<SimpleDraweeView> views;
    private Context context;

    public AdViewPagerAdapter(Context context, List<SimpleDraweeView> views) {
        this.views = views;
        this.context=context;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public Object instantiateItem(View arg0, int arg1) {
        WindowManager wm = (WindowManager) context
                .getSystemService(context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams
                lp = new ViewGroup.LayoutParams(width, UiTool.dpToPx(context,180));
        ((ViewPager) arg0).addView(views.get(arg1),lp);
        return views.get(arg1);
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {

    }

    @Override
    public void finishUpdate(View arg0) {

    }
}


