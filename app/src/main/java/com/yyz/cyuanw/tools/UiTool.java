/**
 * @author xiaohuan
 * 2015年6月13日
 */
package com.yyz.cyuanw.tools;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.didschool.dida.R;
import com.didschool.dida.view.weel.CustomDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tam.media.FrameGrabber;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author
 * @date
 */
public class UiTool {
    /**
     * dp 转 px
     *
     * @param context 上下文
     * @param dp      dp值
     * @return px值
     */
    public static int dpToPx(Context context, float dp) {

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 获取屏幕的参数
     *
     * @param activity
     */
    public static void getScreenConfig(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        // Config.SrceenWidth = metric.widthPixels; // 屏幕宽度（像素）
        // Config.SrceenHeight = metric.heightPixels; // 屏幕高度（像素）
        // Config.SrceenDensity = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
    }

    public static void showToast(Context context, String text) {
        if (StringTool.isNotNull(text))
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 删除webview缓存
     */
    public static void deleteWebviewCache(Context context) {
        try {
            CookieSyncManager.createInstance(context);
            CookieManager.getInstance().removeAllCookie();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void hideKeyboard(Activity context) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            // 得到InputMethodManager的实例
            if (imm.isActive()) {
                // 如果开启
                imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0); // 强制隐藏键盘
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取控件的高度，如果获取的高度为0，则重新计算尺寸后再返回高度
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredHeight(View view) {
        calcViewMeasure(view);
        return view.getMeasuredHeight();
    }

    /**
     * 获取控件的宽度，如果获取的宽度为0，则重新计算尺寸后再返回宽度
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredWidth(View view) {
        calcViewMeasure(view);
        return view.getMeasuredWidth();
    }

    /**
     * 测量控件的尺寸
     *
     * @param view
     */
    public static void calcViewMeasure(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
    }

    public static void setDialog(Activity context, Dialog dialog, int position, int animId, double scaleW, double scaleH) {
        Window window = dialog.getWindow();
        window.setGravity(position); // 此处可以设置dialog显示的位置
        if (animId != -1) {
            window.setWindowAnimations(R.style.dialogWindowAnim); // 添加动画
        }
        dialog.show();
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * scaleW); // 设置宽度
        if (scaleH > 0) {
            lp.height = (int) (display.getHeight() * scaleH);
        }
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(true);
    }

    /**
     * 处理表情字符串
     *
     * @param txt
     * @param context
     * @return
     */
    public static SpannableStringBuilder dealImageString(String txt, Context context) {
        if (!StringTool.isNotNull(txt)) return null;
        txt = txt.replaceAll("\\r\\n", "<br/>");
        txt = txt.replaceAll("\\n", "<br/>");
        txt = txt.replaceAll(" ", "&nbsp;");
        Spanned content = Html.fromHtml(txt);
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        String rexgString = "(\\{img:[^{}]*\\})";
        Pattern pattern = Pattern.compile(rexgString);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            CharSequence name = content.subSequence(matcher.start() + 5,
                    matcher.end() - 1);
            int resID = context.getResources().getIdentifier(name.toString(), "drawable", context.getPackageName());
            Drawable drawable = context.getResources().getDrawable(resID);
            drawable.setBounds(0, 0, 40, 40);//这里设置图片的大小
            ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
            builder.setSpan(imageSpan, matcher.start(), matcher
                    .end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }




    public static ProgressDialog showProgress(Context context, ProgressDialog mProgressDialog) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("数据正在加载中...");
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }

        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
        return mProgressDialog;
    }


    public static void closeProgress(ProgressDialog mProgressDialog) {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从给定的路径加载图片，并指定是否自动旋转方向
     */
    public static Bitmap loadBitmap(String imgpath) throws Exception {
        Bitmap bm = CommonTool.revitionImageSize(imgpath);
        int digree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imgpath);
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }
        if (exif != null) {
            // 读取图片中相机方向信息
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            // 计算旋转角度
            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    digree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    digree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    digree = 270;
                    break;
                default:
                    digree = 0;
                    break;
            }
        }
        if (digree != 0) {
            // 旋转图片
            Matrix m = new Matrix();
            m.postRotate(digree);
            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), m, true);
        }
        saveBitmap(imgpath, bm);
        return bm;
    }

    public static void saveBitmap(String path, Bitmap bm) {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Bitmap getFrameBmp(String videoFilePath) {
        boolean useMMDR = false;
        int captureFrame = 5;
        Bitmap bmp = useMMDR ? getFrameAtTimeByMMDR(videoFilePath, 33333 * captureFrame)
                : getFrameAtTimeByFrameGrabber(videoFilePath, 33333 * captureFrame);

        return bmp;
    }

    private static Bitmap getFrameAtTimeByMMDR(String path, long time) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        Bitmap bmp = mmr.getFrameAtTime(time, MediaMetadataRetriever.OPTION_CLOSEST);
        mmr.release();
        return bmp;
    }

    private static Bitmap getFrameAtTimeByFrameGrabber(String path, long time) {
        FrameGrabber mFrameGrabber = new FrameGrabber();
        mFrameGrabber.setDataSource(path);
        mFrameGrabber.setTargetSize(1280, 720);
        mFrameGrabber.init();
        Bitmap bmp=mFrameGrabber.getFrameAtTime(time);
        mFrameGrabber.release();
        return bmp;
    }

    public static Class<?> getUiClass(Activity act, String uiKey) {
        return ((BaApplication) act.getApplication()).getUiClass(uiKey);
    }

    public static void showPic(final Context context, String url) {
        if (StringTool.isNotNull(url)) {
            final CustomDialog customDialog = new CustomDialog(context);
            customDialog.bindImgLayout(url);
            UiTool.setDialog((Activity) context, customDialog, Gravity.CENTER, -1, 1, 1);
        }
    }
    public static void showPic(final Context context, String url, SimpleDraweeView.ScaleType scaleType) {
        if (StringTool.isNotNull(url)) {
            final CustomDialog customDialog = new CustomDialog(context);
            customDialog.bindImgLayout(url,scaleType);
            UiTool.setDialog((Activity) context, customDialog, Gravity.CENTER, -1, 1, 1);
        }
    }
    public static void showPic(final Context context, Bitmap bmp) {
        if (bmp!=null) {
            final CustomDialog customDialog = new CustomDialog(context);
            customDialog.bindImgLayout(bmp);
            UiTool.setDialog((Activity) context, customDialog, Gravity.CENTER, -1, 1, 1);
        }
    }
    public static void showPic(final Context context, int id) {
        final CustomDialog customDialog = new CustomDialog(context);
        customDialog.bindImgLayout(id);
        UiTool.setDialog((Activity) context, customDialog, Gravity.CENTER, -1, 1, 1);
    }

    public static void openSysPhone(Context context, String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


}
