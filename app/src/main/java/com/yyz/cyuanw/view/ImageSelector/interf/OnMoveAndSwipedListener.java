package com.yyz.cyuanw.view.ImageSelector.interf;

/**
 * 側滑以及長按拖动事件
 */
public interface OnMoveAndSwipedListener {
    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int pos);
}