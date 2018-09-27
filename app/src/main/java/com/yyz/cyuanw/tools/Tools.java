package com.yyz.cyuanw.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


@SuppressLint("SimpleDateFormat")
@SuppressWarnings("deprecation")
public class Tools {

    /**
     * 屏幕的宽度
     */
    public static int getScreenW(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;

    }

    /**
     * 屏幕的高度
     */
    public static int getScreenH(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    /**
     * 检查是否存在SDCard
     *
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 通过文件路径判断是否是音频文件
     *
     * @param filePath
     * @return
     */
    public static boolean isAudioFile(String filePath) {
        return filePath.endsWith(".amr") || filePath.endsWith(".aac") || filePath.endsWith(".mp3");
    }

    /**
     * 通过文件路径猜想文件类型
     *
     * @param filePath
     * @return
     */
    public static String guessMimeType(String filePath) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(filePath);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    /**
     * 获取当前网络连接的类型信息
     *
     * @return
     */
    public static int getConnectedType() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) App.context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            if (mNetworkInfo.isAvailable() && mNetworkInfo.isConnected()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    /**
     * 关闭键盘
     */
    public static void closeBoard(Activity activity) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && imm.isActive()) {
                View view = activity.getCurrentFocus();
                if (view != null) {
                    IBinder mIBinder = view.getWindowToken();
                    if (mIBinder != null) {
                        imm.hideSoftInputFromWindow(mIBinder, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    //imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    /**
     * 获取栈顶的Activity的类名
     *
     * @param mContext
     * @return
     */
    public static String getStackTopActivity(Context mContext) {
        String className = "";
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = am.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            // 应用程序位于堆栈的顶层
            className = tasksInfo.get(0).topActivity.getClassName();
        }
        return className;
    }

    /**
     * Activity是否在栈顶
     *
     * @param mContext
     * @param className
     * @return
     */
    public static boolean isStackTopActivity(Context mContext, String className) {
        boolean isTopActivity = false;
        String stackTopClassName = getStackTopActivity(mContext);
        // 应用程序位于堆栈的顶层
        if (className.equals(stackTopClassName)) {
            isTopActivity = true;
        }
        return isTopActivity;
    }

    /**
     * 通过Activity的类名来判断app是否在RunningTasks的顶部
     *
     * @param mContext
     * @param packageName
     * @return
     */
    public static boolean isStackTopApp(Context mContext, String packageName) {
        boolean isStackTopApp = false;
        String stackTopClassName = getStackTopActivity(mContext);
        if (stackTopClassName != null) {
            if (stackTopClassName.contains(packageName)) {
                isStackTopApp = true;
            }
        }
        // 应用程序位于堆栈的顶层
        return isStackTopApp;
    }

    // 通过app的包名来判断应用是否在后台运行

    public static boolean isAppBackground(Context mContext, String packageName) {
        boolean isAppBackground = false;
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = am.getRunningTasks(Integer.MAX_VALUE);
        if (tasksInfo.size() > 1) {
            // 应用程序位于堆栈的顶层
            for (int i = 1; i < tasksInfo.size(); i++) {
                if (tasksInfo.get(i).topActivity.getClassName().contains(packageName)) {
                    isAppBackground = true;
                    break;
                }
            }
        }
        return isAppBackground;
    }

    // 通过Service的类名来判断是否启动某个服务
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(10);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 检查程序是否在前台运行
     *
     * @return 是否在前台运行
     */
    public static boolean isAppOnForeground(Context mContext, String packageName) {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }


    /**
     * 检查手机号码是否有效
     */
    public static boolean checkPhoneNumberValid(Context context, String number) {
        if (number == null || "".equals(number)) {
            //	Toast.makeText(context, R.string.phone_not_empty, Toast.LENGTH_LONG).show();
            return false;
        } else {
            String str = "^[1][3,4,5,7,8][0-9]{9}$";
            Pattern pattern = Pattern.compile(str);
            Matcher mc = pattern.matcher(number);
            return mc.matches();
        }
    }

    /**
     * 判断字符串是否是大于0的数字(整数或者浮点数)
     */
    public static boolean isGreaterThanZero(String str) {
        if (isNumeric(str)) {
            int num = Integer.parseInt(str);
            if (num > 0) {
                return true;
            }
        } else if (isFloatNumeric(str)) {
            Float num = Float.parseFloat(str);
            if (num > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串是否是大于0的数字
     */
    public static boolean isNumericThanZero(String str) {
        boolean isNumericThanZero = false;
        if (isNumeric(str)) {
            long num = Long.parseLong(str);
            if (num > 0l) {
                isNumericThanZero = true;
            }
        }
        return isNumericThanZero;
    }

    /**
     * 判断字符串是否由数字组成
     */
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]+");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断字符串是否由浮点数字组成
     */
    public static boolean isFloatNumeric(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]+\\.?[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 插入排序
     *
     * @param list
     * @return
     */
    public static List<?> insertSort(List<?> list) {
        /*
         * if (list != null && list.size() > 0) { for (int i = 1; i <
         * list.size(); i++)// 循环从第二个数组元素开始，因为arr[0]作为最初已排序部分 { TrackPoint temp
         * = list.get(i);// temp标记为未排序第一个元素 int j = i - 1; TrackPoint
         * mTrackPoint = list.get(j); String str1 = mTrackPoint.getTime();
         * String str2 = temp.getTime();
         *
         * long time1=Long.parseLong(str1); long time2=Long.parseLong(str2);
         * while (j >= 0 && time1 < time2)//将temp与已排序元素从小到大比较，寻找temp应插入的位置 {
         * list.set(j + 1, mTrackPoint); j--; } list.set(j + 1, temp); } }
         */
        return list;
    }

    /**
     * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width,
                                           int height, int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
     * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
     * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
     *
     * @param imagePath 图像的路径
     * @param width     指定输出图像的宽度
     * @param height    指定输出图像的高度
     * @return 生成的缩略图
     */
    public static Bitmap getImageThumbnail(String imagePath, int width,
                                           int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    public static String getDuration(String filePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        String duration = null;
        try {
            retriever.setDataSource(filePath);
            duration = retriever
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        } catch (Exception e) {
        } finally {
            try {
                retriever.release();
            } catch (Exception e) {
            }
        }
        return duration;
    }

    // 删除通知
    public static void clearNotification(Context context) {
        // 启动后删除之前我们定义的通知
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    /**
     * 正则表达式校验邮箱
     *
     * @param email 待匹配的邮箱
     * @return 匹配成功返回true 否则返回false;
     */
    public static boolean isEmail(String email) {
        if (TextUtils.isEmpty(email)) return false;
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        return email.matches(RULE_EMAIL);
    }

    /**
     * 大陆号码或香港号码均可
     */
    public static boolean isPhone(String str) throws PatternSyntaxException {
        if (TextUtils.isEmpty(str)) return false;
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     */
    public static boolean isChinaPhoneLegal(String str) {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        return str.matches(regExp);
//        Pattern p = Pattern.compile(regExp);
//        Matcher m = p.matcher(str);
//        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str) {
        String regExp = "^(5|6|8|9)\\d{7}$";
        return str.matches(regExp);
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static void showSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        //imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    public static boolean isShowSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //获取状态信息
        return imm.isActive();//true 打开
    }

    private static final String TAG = "Config";

    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo("com.yyz.cyuanw", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return verCode;
    }

    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo("com.yyz.cyuanw", 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return verName;

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float density, float dpValue) {
        return (int) (dpValue * density + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float density, float pxValue) {
        return (int) (pxValue / density + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取手机的密度
     */
    public static float getDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

//    /**
//     * 执行异步任务，在主线程回调
//     *
//     * @param func1
//     * @param action1
//     */
//    public static void async(Func1 func1, Action1 action1) {
//        Observable observable = Observable.just("").observeOn(Schedulers.io()).map(func1);
//        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(action1);
//    }

    public static void openSysPhone(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static String getNYR() {
        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
        Date d1=new Date(time);
        String t1=format.format(d1);
        return t1;
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

}
