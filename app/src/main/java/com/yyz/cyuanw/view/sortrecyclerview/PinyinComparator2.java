package com.yyz.cyuanw.view.sortrecyclerview;

import com.yyz.cyuanw.bean.Data9;

import java.util.Comparator;

public class PinyinComparator2 implements Comparator<Data9> {

	public int compare(Data9 o1, Data9 o2) {
		if (o1.pinyin.equals("@")
				|| o2.pinyin.equals("#")) {
			return -1;
		} else if (o1.pinyin.equals("#")
				|| o2.pinyin.equals("@")) {
			return 1;
		} else {
			return o1.pinyin.compareTo(o2.pinyin);
		}
	}

}
