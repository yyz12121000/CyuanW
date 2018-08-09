package com.yyz.cyuanw.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yyz.cyuanw.R;

import java.util.ArrayList;
import java.util.List;

public class BasePopuwindow extends PopupWindow {
    protected Activity context;


    public BasePopuwindow(Activity context) {
        this.context = context;

        int height = context.getWindowManager().getDefaultDisplay().getHeight();
        int width = context.getWindowManager().getDefaultDisplay().getWidth();

        // 设置弹出的popuwindow的宽
        this.setWidth(width);
        // 设置弹出的popuwndow的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置popuwindow的弹出窗体是否可以点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个colorDrawable颜色为半透明
        ColorDrawable colorDrawable = new ColorDrawable(0000000000);
        // 点击返回键和其他地方使其消失，设置了这个才能触发OnDismissListener,设置其他控件变化等操作
        this.setBackgroundDrawable(colorDrawable);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.AnimationPreview);

    }
    protected IOnListItemClickListenner listenner;

    public void setItemListenner(IOnListItemClickListenner listenner) {
        this.listenner = listenner;
    }

    public interface IOnListItemClickListenner {
        void onItemClick(int position, String text);
    }
}
