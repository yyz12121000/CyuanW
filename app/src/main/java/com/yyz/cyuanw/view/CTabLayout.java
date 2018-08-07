package com.yyz.cyuanw.view;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyz.cyuanw.App;
import com.yyz.cyuanw.tools.Tools;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class CTabLayout extends TabLayout {
    public CTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CTabLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int dp10 = Tools.dip2px(App.context, 10);
        LinearLayout mTabStrip = (LinearLayout) this.getChildAt(0);
        try {
            Field mTabs = TabLayout.class.getDeclaredField("mTabs");
            mTabs.setAccessible(true);
            ArrayList<Tab> tabs = (ArrayList<Tab>) mTabs.get(this);
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                Tab tab = tabs.get(i);
                Field mView = tab.getClass().getDeclaredField("mView");
                mView.setAccessible(true);
                Object tabView = mView.get(tab);
                Field mTextView = App.context.getClassLoader().loadClass("android.support.design.widget.TabLayout$TabView").getDeclaredField("mTextView");
                mTextView.setAccessible(true);
                TextView textView = (TextView) mTextView.get(tabView);
                float textWidth = textView.getPaint().measureText(textView.getText().toString());
                View child = mTabStrip.getChildAt(i);
                child.setPadding(10, 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) textWidth, LinearLayout.LayoutParams.MATCH_PARENT);
                params.leftMargin = 20;
                params.rightMargin = 30;
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
