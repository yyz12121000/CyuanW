package com.yyz.cyuanw.view.ImageSelector.interf;

import com.yyz.cyuanw.view.ImageSelector.domain.Pic;

import java.util.List;


/**
 * 选择接口
 */
public interface Selectable {

    void toggle(Pic mPic);

    List<Pic> getSelectedPic();

    List<String> getSelectedPicStr();

    List<Pic> getCurrentPicList();

    void clearAllSelection();

    int getSlectedPicSize();

    boolean isSelected(Pic mPic);

    void setHasSelected(List<String> selected);


}
