package com.yyz.cyuanw.view.ImageSelector.interf;

import android.view.View;

import com.yyz.cyuanw.view.ImageSelector.domain.Pic;

/**
 * 图片选择回调listner
 */
public interface OnPickListener {
    /**
     * 点击一张PIC
     *
     * @param pic           图片资源
     * @param position      图片位置
     * @return 是否允许选中
     */
    void onItemClicked(View view, Pic pic, int position);

    /**
     * 点击图片右上角的toggle
     *
     * @param pic           图片资源
     * @param position      图片位置
     * @param isPreSelected 是否即将选中
     * @return 是否允许选中
     */
    boolean onToggleClicked(Pic pic, int position, boolean isPreSelected);


    /**
     * 点击拍照
     */
    void onCameraClick();

}
