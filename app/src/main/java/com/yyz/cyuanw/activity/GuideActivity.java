package com.yyz.cyuanw.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.activity.MainActivity;

import butterknife.BindView;

public class GuideActivity extends BaseActivity implements OnClickListener, OnPageChangeListener{
	@BindView(R.id.viewpager) ViewPager vp;
	@BindView(R.id.button) Button button;

	private ViewPagerAdapter vpAdapter;
	private List<View> views;  

	//引导图片资源  
	private static final int[] pics = { R.mipmap.guide1,R.mipmap.guide2,R.mipmap.guide3};

	//记录当前选中位置  
	private int currentIndex;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_guide;
	}

	@Override
	public void initView() {
		views = new ArrayList<View>();

		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

		//初始化引导图片列表
		for(int i=0; i<pics.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(mParams);
			iv.setScaleType(ImageView.ScaleType.CENTER);
			iv.setImageResource(pics[i]);
			views.add(iv);
		}
		//初始化Adapter
		vpAdapter = new ViewPagerAdapter(views);
		vp.setAdapter(vpAdapter);
		//绑定回调
		vp.setOnPageChangeListener(this);

		button.setOnClickListener(this);
	}

	@Override
	public void initData() {

	}

	/** 
	 *设置当前的引导页  
	 */  
	private void setCurView(int position)  
	{  
		if (position < 0 || position >= pics.length) {  
			return;  
		}  

		vp.setCurrentItem(position);  
	}  

	/** 
	 *这只当前引导小点的选中  
	 */  
	private void setCurDot(int positon)  
	{  
		if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {  
			return;  
		}

		currentIndex = positon;  
	}  

	//当滑动状态改变时调用  
	@Override  
	public void onPageScrollStateChanged(int arg0) {  

	}  

	//当当前页面被滑动时调用  
	@Override  
	public void onPageScrolled(int arg0, float arg1, int arg2) {  

	}  

	//当新的页面被选中时调用  
	@Override  
	public void onPageSelected(int arg0) {  
		//设置底部小点选中状态  
		setCurDot(arg0);  
		if(arg0 == 2){
			button.setVisibility(View.VISIBLE);
		}else{
			button.setVisibility(View.GONE);
		}
	}  

	@Override  
	public void onClick(View v) {
		if (v.getId() == R.id.button) {

			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		}else{
			int position = (Integer)v.getTag();  
			setCurView(position);  
			setCurDot(position);  
		}
	}

	class ViewPagerAdapter extends PagerAdapter {

		//界面列表
		private List<View> views;

		public ViewPagerAdapter (List<View> views){
			this.views = views;
		}

		//销毁position位置的界面
		@Override
		public void destroyItem(View view, int position, Object object) {
			((ViewPager) view).removeView(views.get(position));
		}

		@Override
		public void finishUpdate(View view) {
		}

		//获得当前界面数
		@Override
		public int getCount() {

			if (views != null){
				return views.size();
			}

			return 0;
		}


		//初始化position位置的界面
		@Override
		public Object instantiateItem(View view, int position) {

			((ViewPager) view).addView(views.get(position), 0);

			return views.get(position);
		}

		//判断是否由对象生成界面
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return (view == object);
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

	}
}