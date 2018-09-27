package com.yyz.cyuanw.view.ImageSelector.interf;

/**
 * 图片操作回调listner
 */
public interface OnOperateListener {
    /**
     * 单击一张PIC
     *
     * @param picPath  图片资源
     * @param position 图片位置
     */
    void onItemClicked(String picPath, int position);

    /**
     * 长按一张PIC
     *
     * @param picPath  图片资源
     * @param position 图片位置
     */
    void onItemLongClicked(String picPath, int position);

    /**
     * 删除一张PIC之后
     *
     * @param picPath  图片资源
     */
    void onRemoved(String picPath);


    void onClickAdd();

}
