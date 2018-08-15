package com.yyz.cyuanw.tools;

import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;


import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;

/**
 * Created by Administrator on 2017/6/27 0027.
 */

public class Img {
    public static void load(ImageView iv, String url) {
        load(iv, url, -1);
    }

    public static void load(ImageView iv, String url, int w) {
//        if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
//            if (w == -1) {
//                w = Final.IMG_W_500;
//            }
//            url += "?x-oss-process=image/resize,w_" + w + ",limit_0";
//        }
        Glide.with(App.context)
                .load(url).dontAnimate()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(iv);
    }

    public static void loadTop(ImageView iv, String url) {
        loadTop(iv, url, -1);
    }

    public static void loadTop(ImageView iv, String url, int w) {
        if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
            if (w == -1) {
                w = Final.IMG_W_500;
            }
            url += "?x-oss-process=image/resize,w_" + w + ",limit_0";
        }
        Glide.with(App.context)
                .load(url).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.ic_launcher)
                .bitmapTransform(new CropTransformation(App.context, w, w, CropTransformation.CropType.TOP))
                .placeholder(R.mipmap.ic_launcher)
                .into(iv);


        // 使用构造方法 CropTransformation(Context context, int width, int height, CropType cropType)
        // width : 剪裁宽度
        // height : 剪裁高度
        // cropType : 剪裁类型（指定剪裁位置，可以选择上、中、下其中一种）
//        Glide.with(this)
//                .load(url)
//                .bitmapTransform(new CropTransformation(this, 600, 200, CropTransformation.CropType.CENTER))
//                .into(mImageView2);

    }

    public static void loadC(ImageView iv, String url) {
        loadC(iv, url, -1);
    }

    public static void loadC(ImageView iv, String url, int w) {
        if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
            if (w == -1) {
                w = Final.IMG_W_150;
            }
            url += "?x-oss-process=image/resize,w_" + w + ",limit_0";
        }
        Glide.with(App.context)
                .load(url).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .bitmapTransform(new CropTransformation(App.context, w, w, CropTransformation.CropType.TOP), new CropCircleTransformation(App.context))
                .crossFade(1000)
                .into(iv);
    }

    public static void loadLocalC(ImageView iv, String url) {
        Glide.with(App.context)
                .load(url).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .bitmapTransform(new CropCircleTransformation(App.context))
                .crossFade(1000)
                .into(iv);
    }

    /*public static void loadC(ImageView iv, String url,int r) {
        Glide.with(App.context)
                .load(url)
                .placeholder(R.mipmap.default_icon)
                .override(2*r,2*r)
                .bitmapTransform(new CropCircleTransformation(App.context))
                .crossFade(r)
                .into(iv);
    }*/
    public static void loadWH(final ImageView iv, String url) {
        loadWH(iv, url, -1);
    }

    /**
     * 自适应宽度加载图片。保持图片的长宽比例不变，通过修改imageView的高度来完全显示图片。
     */
    public static void loadWH(final ImageView iv, String url, int w) {
        if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
            if (w == -1) {
                w = Final.IMG_W_500;
            }
            url += "?x-oss-process=image/resize,w_" + w + ",limit_0";
        }
        Glide.with(App.context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (iv == null) {
                            return false;
                        }
                        if (iv.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            iv.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        ViewGroup.LayoutParams params = iv.getLayoutParams();
                        int vw = iv.getWidth() - iv.getPaddingLeft() - iv.getPaddingRight();
                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
                        params.height = vh + iv.getPaddingTop() + iv.getPaddingBottom();
                        iv.setLayoutParams(params);
                        return false;
                    }
                })
                .into(iv);
    }
}
