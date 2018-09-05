package com.yyz.cyuanw.activity;

import android.content.Intent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.StringUtil;

import butterknife.BindView;

public class StartActivity extends BaseActivity {
	@BindView(R.id.app_start_view) LinearLayout view;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_start;
	}

	@Override
	public void initView() {
		AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
		aa.setDuration(1000);
		view.startAnimation(aa);
		aa.setAnimationListener(new Animation.AnimationListener()
		{
			@Override
			public void onAnimationEnd(Animation arg0) {
				redirectTo();
			}
			@Override
			public void onAnimationRepeat(Animation animation) {}
			@Override
			public void onAnimationStart(Animation animation) {}

		});
	}

	@Override
	public void initData() {

	}

	private void redirectTo(){
		Intent intent = new Intent();

		if (StringUtil.isNotNull(App.get(Constant.KEY_FIRST_START))){
			intent.setClass(this,MainActivity.class);
		}else{
			intent.setClass(this,GuideActivity.class);
		}

		App.set(Constant.KEY_FIRST_START,"true");

		startActivity(intent);
		overridePendingTransition(R.anim.fade, R.anim.hold);
		finish();
	}

}
