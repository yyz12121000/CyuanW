package com.yyz.cyuanw.view.ImageSelector;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.view.CustomProgress;
import com.yyz.cyuanw.view.ImageSelector.constant.Constant;
import com.yyz.cyuanw.view.ImageSelector.domain.Pic;
import com.yyz.cyuanw.view.ImageSelector.interf.OnPickListener;
import com.yyz.cyuanw.view.ImageSelector.utils.PhotoHelper;
import com.yyz.cyuanw.view.ImageSelector.utils.PicLoader;

import java.util.ArrayList;


/**
 * 照片展示
 */
public class PicktureActivity extends FragmentActivity implements OnPickListener {
    PicktureFragment mPicktureFragment;
    PickBuilder pickBuilder = new PickBuilder();

    //private Toolbar mToolbar;
    private TextView mBackView;
    private TextView mCommintBtn;
    private int curSize;
    private PhotoHelper mPhotoHelper;
    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.color_statusbar_bg).init();

        setContentView(R.layout.activity_pick);

        if (getIntent().hasExtra(Pickture.PARAM_BUILDER))
            pickBuilder = (PickBuilder) getIntent().getSerializableExtra(Pickture.PARAM_BUILDER);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        initBase();
        initView();
        loadFrag();

    }

    private void initBase() {
        mPhotoHelper = new PhotoHelper(this);
    }

    private void initView() {
        //mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mBackView = (TextView) findViewById(R.id.id_tv_cancel);
        mCommintBtn = (TextView) findViewById(R.id.commit);
        //mToolbar.setTitle("图片");
        //setupToolbar(mToolbar, true);

        curSize = pickBuilder.getSelectedStrList().size();
        mCommintBtn.setText(getString(R.string.__unv2_select, curSize, pickBuilder.getMax()));
        mCommintBtn.setEnabled(curSize > 0);
        mCommintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomProgress.show(PicktureActivity.this, "图片处理中...", false, null);
                setResult(RESULT_OK, new Intent()
                        .putStringArrayListExtra(Pickture.PARAM_PICKRESULT,
                                (ArrayList<String>) mPicktureFragment.getPicAdapter().getSelectedPicStr()));
                finish();
            }
        });

        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

//    protected void setupToolbar(Toolbar mToolbar, boolean homeIconVisible) {
//        if (null == getSupportActionBar()) {
//            setSupportActionBar(mToolbar);
//        }
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(homeIconVisible);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//        }
//        return false;
//    }

    /**
     * 加载照片frag
     */
    private void loadFrag() {

        mPicktureFragment = PicktureFragment.newInstance(pickBuilder);
        getSupportFragmentManager().beginTransaction().replace(R.id.image_grid, mPicktureFragment).commitAllowingStateLoss();
    }

    @Override
    public void onItemClicked(View view, Pic pic, int position) {
//        if (pickBuilder.hasCamera) position--;
//        //显示大图
//        if (mPicktureFragment.getFolderList() != null) {
//            Intent mIntent = new Intent(this, ViewPagerActivity.class);
//            mIntent.putExtra(Constant.VIEW_PAGER_POS, position);
//            mIntent.putExtra(Constant.VIEW_PAGER_PATH, (ArrayList<String>) mPicktureFragment.getFolderList().get(PicLoader.INDEX_ALL_PHOTOS).getPhotoPaths());
//
//
////            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
////                    PicktureActivity.this, view, ViewPagerActivity.TRANSIT_PIC);
//            try {
//                ActivityCompat.startActivity(PicktureActivity.this, mIntent,null /*optionsCompat.toBundle() */);
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//                startActivity(mIntent);
//            }
//
//        }
    }

    @Override
    public boolean onToggleClicked(Pic pic, int position, boolean isPreSelected) {
        boolean canOperate = isPreSelected ? curSize < pickBuilder.getMax() : curSize > 0;
        if (canOperate) {
            curSize = isPreSelected ? ++curSize : --curSize;

            mCommintBtn.setEnabled(curSize > 0);
            mCommintBtn.setText(getString(R.string.__unv2_select, curSize, pickBuilder.getMax()));

            return true;
        } else {
            App.showToast(getString(R.string.__max_limit, pickBuilder.getMax()));
        }

        return false;
    }

    @Override
    public void onCameraClick() {
        mPhotoHelper.goCamera(Constant.REQUEST_TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constant.REQUEST_TAKE_PHOTO) {
            Pic pic = mPhotoHelper.onLoadPic();
            if (pic != null)
                if (mPicktureFragment != null) mPicktureFragment.invalide(pic);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPhotoHelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPhotoHelper.onRestoreInstanceState(savedInstanceState);
    }
}
