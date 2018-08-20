package com.yyz.cyuanw.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

public class CommonPopupDialog extends Dialog {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}
	
	public CommonPopupDialog(Context context, int theme) {
		super(context, theme);
		setCanceledOnTouchOutside(true);
	}

	public void setAnimations(int anim) {
		getWindow().setWindowAnimations(anim);
	}

	public void showAtLocation(int gravity, int x, int y, int w, int h) {
		WindowManager.LayoutParams wl = getWindow().getAttributes();
		wl.x = x;
		wl.y = y;
		wl.gravity = gravity;
		wl.width = w;
		wl.height = h;
		getWindow().setAttributes(wl);

		show();
	}
}
