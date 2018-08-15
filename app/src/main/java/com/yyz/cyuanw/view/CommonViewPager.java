package com.yyz.cyuanw.view;

/**
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.adapter.AdViewPagerAdapter;
import com.yyz.cyuanw.tools.StringUtil;
import com.yyz.cyuanw.tools.UiTool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class CommonViewPager extends LinearLayout {
    public View view;
    public LayoutInflater inflater;
    public Context context;
    public LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

    private ViewPager viewpager;
    private LinearLayout circleLayout;

    private ArrayList<SimpleDraweeView> views = new ArrayList<SimpleDraweeView>();
    private ClickIF listener;
    /**
     * 图片滑动的切换的圆点
     */
    private List<ImageView> circleViews = new ArrayList<ImageView>();
    private int currentIndex = 0;
    /**
     * 定期执行指定的任务
     */
    private ScheduledExecutorService mScheduledExecutorService;

    private String[] drawableUris;
    private int[] drawableIds;
    private Bitmap[] bitmaps;
    private ArrayList<String> arrayList;
    private boolean clickable;
    private SimpleDraweeView.ScaleType scaleType = SimpleDraweeView.ScaleType.FIT_XY;
    private int holderImgRes = -1;

    public CommonViewPager(Context context) {
        super(context);
    }

    public CommonViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CommonViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public CommonViewPager(Context context, ArrayList<String> arrayList, boolean clickable, SimpleDraweeView.ScaleType scaleType) {
        super(context);
        this.context = context;
        this.arrayList = arrayList;
        this.clickable = clickable;
        this.scaleType = scaleType;
        initView();
    }

    public CommonViewPager(Context context, ArrayList<String> arrayList, boolean clickable) {
        super(context);
        this.context = context;
        this.arrayList = arrayList;
        this.clickable = clickable;
        initView();
    }

    public CommonViewPager(Context context, ArrayList<String> arrayList, boolean clickable, ClickIF listener) {
        super(context);
        this.context = context;
        this.arrayList = arrayList;
        this.clickable = clickable;
        this.listener = listener;
        initView();
    }

    public CommonViewPager(Context context, ArrayList<String> arrayList, boolean clickable, ClickIF listener, int holderImgRes) {
        super(context);
        this.context = context;
        this.arrayList = arrayList;
        this.clickable = clickable;
        this.listener = listener;
        this.holderImgRes = holderImgRes;
        initView();
    }

    public CommonViewPager(Context context, String[] drawableUris, boolean clickable) {
        super(context);
        this.context = context;
        this.drawableUris = drawableUris;
        this.clickable = clickable;
        initView();
    }

    public CommonViewPager(Context context, String[] drawableUris, boolean clickable, ClickIF listener) {
        super(context);
        this.context = context;
        this.drawableUris = drawableUris;
        this.clickable = clickable;
        this.listener = listener;
        initView();
    }

    public CommonViewPager(Context context, int[] drawableIds, boolean clickable) {
        super(context);
        this.context = context;
        this.drawableIds = drawableIds;
        this.clickable = clickable;
        initView();
    }

    public CommonViewPager(Context context, int[] drawableIds, boolean clickable, ClickIF listener) {
        super(context);
        this.context = context;
        this.drawableIds = drawableIds;
        this.clickable = clickable;
        this.listener = listener;
        initView();
    }

    public CommonViewPager(Context context, Bitmap[] bitmaps, boolean clickable) {
        super(context);
        this.context = context;
        this.bitmaps = bitmaps;
        this.clickable = clickable;
        initView();
    }

    public CommonViewPager(Context context, Bitmap[] bitmaps, boolean clickable, ClickIF listener) {
        super(context);
        this.context = context;
        this.bitmaps = bitmaps;
        this.clickable = clickable;
        this.listener = listener;
        initView();
    }

    public void initViewPager(Context context, ArrayList<String> arrayList, boolean clickable, ClickIF listener){
        this.context = context;
        this.arrayList = arrayList;
        this.clickable = clickable;
        this.listener = listener;
        initView();
    }

    public void initView() {
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.view_common_viewpager, null);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        viewpager.setOnPageChangeListener(new MyPagerChangeListener());
        circleLayout = (LinearLayout) view.findViewById(R.id.circleLayout);
        bindData();
        addView(view, lp);
    }

    public void setImageList(ArrayList<String> arrayList){
        this.arrayList = arrayList;

        bindData();
    }

    public void bindData() {
        //图片设置
        LayoutParams lp = new LayoutParams(UiTool.dpToPx(context, 15), UiTool.dpToPx(context, 15));
        lp.gravity = Gravity.CENTER;
        int len = 0;
        if (drawableUris != null) {
            len = len < drawableUris.length ? drawableUris.length : len;
        }
        if (bitmaps != null) {
            len = len < bitmaps.length ? bitmaps.length : len;
        }
        if (drawableIds != null) {
            len = len < drawableIds.length ? drawableIds.length : len;
        }
        if (arrayList != null) {
            len = len < arrayList.size() ? arrayList.size() : len;
        }
        if (len <= 1) {
            circleLayout.setVisibility(View.GONE);
        } else {
            circleLayout.setVisibility(View.VISIBLE);
        }

        views.clear();
        circleViews.clear();
        circleLayout.removeAllViews();

        for (int i = 0; i < len; i++) {
            final int index = i;
            //图片
            SimpleDraweeView picIv = new SimpleDraweeView(context);
            GenericDraweeHierarchy hierarchy = picIv.getHierarchy();
            hierarchy.setPlaceholderImage(R.drawable.normal);
            hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
            picIv.setHierarchy(hierarchy);
            picIv.setScaleType(scaleType);
            if (drawableUris != null) {
                final String path = drawableUris[i];
                if (StringUtil.isNotNull(path))
                    picIv.setImageURI(Uri.parse(drawableUris[i]));
                if (clickable) {
                    picIv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                listener.onPagerClick(index);
                            } else {
                                UiTool.showPic(context, path, scaleType);
                            }
                        }
                    });

                }
            }
            if (bitmaps != null) {
                final Bitmap bmp = bitmaps[i];
                if (bmp != null)
                    picIv.setImageBitmap(bmp);
                if (clickable) {

                    picIv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (listener != null) {
                                listener.onPagerClick(index);
                            } else {
                                UiTool.showPic(context, bmp);
                            }
                        }
                    });
                }
            }
            if (drawableIds != null) {
                final int id = drawableIds[i];
                picIv.setImageResource(id);
                if (clickable) {

                    picIv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                listener.onPagerClick(index);
                            } else {
                                UiTool.showPic(context, id);
                            }
                        }
                    });

                }
            }
            if (arrayList != null) {
                final String path = arrayList.get(i);
                if (StringTool.isNotNull(path))
                    picIv.setImageURI(Uri.parse(arrayList.get(i)));
                if (clickable) {
                    picIv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                listener.onPagerClick(index);
                            } else {
                                UiTool.showPic(context, path, scaleType);
                            }
                        }
                    });

                }
            }
            views.add(picIv);
            //圆点点
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(R.mipmap.lunbo_yuan);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            circleViews.add(imageView);
            circleLayout.addView(imageView, lp);

        }

        viewpager.setAdapter(new AdViewPagerAdapter(context, views));
        viewpager.setCurrentItem(currentIndex);
    }

    public void hideCircleView(){
        circleLayout.setVisibility(View.GONE);
    }

    /**
     * 切换当前的图片
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            viewpager.setCurrentItem(currentIndex);
        }

    };

    /**
     * 换行切换任务
     *
     * @author Administrator
     */
    private class ScrollTask implements Runnable {

        public void run() {
            synchronized (viewpager) {
                currentIndex = (currentIndex + 1) % views.size();
                mHandler.obtainMessage().sendToTarget(); // 通过Handler切换图片
            }
        }
    }

    public void start() {
        // TODO Auto-generated method stub
        mScheduledExecutorService = Executors
                .newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒钟切换一次图片显示
        mScheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 2, 4,
                TimeUnit.SECONDS);
    }

    public void stop() {
        // 当Activity不可见的时候停止切换
        mScheduledExecutorService.shutdown();
    }


    /**
     * 当ViewPager中页面的状态发生改变时调用
     */
    private class MyPagerChangeListener implements ViewPager.OnPageChangeListener {
        private int oldPosition = 0;

        public void onPageSelected(int position) {
            currentIndex = position;
            circleViews.get(oldPosition).setImageResource(
                    R.mipmap.lunbo_yuan);
            circleViews.get(position).setImageResource(
                    R.mipmap.lunbo_chang);
            oldPosition = position;
        }

        public void onPageScrollStateChanged(int arg0) {

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);//这句话的作用 告诉父view，我的单击事件我自行处理，不要阻碍我。
        return super.dispatchTouchEvent(ev);
    }

    public interface ClickIF {
        public void onPagerClick(int index);
    }
}

