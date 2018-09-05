package com.yyz.cyuanw.tools;

import android.graphics.Color;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;

public class Img {
    public static void loadC(ImageView iv, String url) {
        Glide.with(App.context).load(url)
                .apply(new RequestOptions()
                        .error(App.context.getResources().getDrawable(R.mipmap.ic_launcher))
                        //.placeholder(R.mipmap.ic_defaultphoto)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .transform(new GlideCircleTransformWithBorder(App.context, 1, Color.parseColor("#ccffffff")))
                ).into(iv);
    }

    public static void load(ImageView iv, String url) {
        Glide.with(App.context).load(url)
                .apply(new RequestOptions()
                        .error(App.context.getResources().getDrawable(R.mipmap.ic_launcher))
                        //.placeholder(R.mipmap.ic_launcher)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                ).into(iv);
    }

    public static void loadP(ImageView iv, String url,int defaultPic) {
        Glide.with(App.context).load(url)
                .apply(new RequestOptions()
                        .error(App.context.getResources().getDrawable(defaultPic))
                        .placeholder(defaultPic)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                ).into(iv);
    }
}
