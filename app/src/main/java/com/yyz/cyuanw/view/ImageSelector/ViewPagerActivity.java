package com.yyz.cyuanw.view.ImageSelector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import com.gyf.barlibrary.ImmersionBar;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.view.ImageSelector.constant.Constant;
import com.yyz.cyuanw.view.ImageSelector.widget.HackyViewPager;

import java.util.ArrayList;

public class ViewPagerActivity extends AppCompatActivity {
    private ArrayList<String> sDrawables = new ArrayList<>();
    private RequestManager mGlideRequestManager;
    private static int tartgetPos;
    public static final String TRANSIT_PIC = "pickture";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).init();

        setContentView(R.layout.activity_view_pager);
        init();

    }

    private void init() {
        ViewPager mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        //ViewCompat.setTransitionName(mViewPager, TRANSIT_PIC);
        mGlideRequestManager = Glide.with(this);

        setupIntent();

        setupViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager mViewPager) {
        SamplePagerAdapter adapter = new SamplePagerAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(tartgetPos);

        final TextView mIndicatorTv = (TextView) findViewById(R.id.indicator_tv);

        mIndicatorTv.setText(getString(R.string.__unv2_select, tartgetPos + 1, sDrawables.size()));

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndicatorTv.setText(getString(R.string.__unv2_select, position + 1, sDrawables.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupIntent() {
        Intent mIntent = getIntent();
        sDrawables = mIntent.getStringArrayListExtra(Constant.VIEW_PAGER_PATH);
        tartgetPos = mIntent.getIntExtra(Constant.VIEW_PAGER_POS, 0);
    }

    class SamplePagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return sDrawables.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            //PhotoView imageView = new PhotoView(container.getContext());
            ImageView imageView = new ImageView(container.getContext());
            mGlideRequestManager
                    .load(sDrawables.get(position))
                    //.crossFade()
                    .thumbnail(0.5f)
                    //.error(App.context.getResources().getDrawable(R.mipmap.__picker_ic_broken_image_black_48dp))
                    .into(imageView);
            container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
//                @Override
//                public void onViewTap(View view, float x, float y) {
//                    onBackPressed();
//                }
//            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}