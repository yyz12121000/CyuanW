package com.yyz.cyuanw.tools.zoom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.os.Environment;
import android.text.TextUtils;

import com.yyz.cyuanw.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



/**
 * Title: LocalFileTools.java Description: 文件操作相关工具类 Company: 中圈集团
 *
 * @author 杨亚洲
 * @version 1.0
 * @date 2014-1-3
 */
public class LocalFileTools {
    private static final String Quan = "Quan" + File.separator;// 圈目录
    private static final String DefaultDir = "DefaultDir" + File.separator;// 圈默认存储目录
    private static final String originalImage = "originalImage" + File.separator;// 原图
    private static final String bigImage = "bigImage" + File.separator;// 大图
    private static final String smallImage = "smallImage" + File.separator;// 小图

    public static final String FILE_SAVE_ROOT_PATH = "/bxdjz";
    public static final String IMG_SAVE_PATH = FILE_SAVE_ROOT_PATH + "/img";
    private static final String sekorm = IMG_SAVE_PATH.substring(1) + File.separator;// 世强目录

    // 获得缓存文件路径，磁盘空间不足或清除缓存时数据会被删掉，一般存放一些临时文件
// /data/data/<application package>/cache目录
//	File cacheDir = getCacheDir();

    public static String getImgTempPath() {
        File cacheDir = App.context.getCacheDir();
        return cacheDir.getAbsolutePath()+ File.separator + "img" + File.separator;
    }

    public static String getSekormDir() throws Exception {
        String s = getRootPath();
        if (TextUtils.isEmpty(s)) {
            return null;
        }
        s = s + sekorm;
        safeDirPath(s);
        return s;
    }

    public static String getQuan() throws Exception {
        String s = getRootPath();
        if (TextUtils.isEmpty(s)) {
            return null;
        }
        s = s + Quan;
        safeDirPath(s);
        return s;
    }

    public static String getDefaultdir() throws Exception {
        String s = getRootPath();
        if (TextUtils.isEmpty(s)) {
            return null;
        }
        s = s + Quan + DefaultDir;
        safeDirPath(s);
        return s;
    }

    /**
     * 获取原图存储路径
     *
     * @return
     * @throws IOException
     */
    public static String getOriginalimagePath() throws Exception {
        String s = getRootPath();
        if (TextUtils.isEmpty(s)) {
            return null;
        }
        s = s + Quan + originalImage;
        safeDirPath(s);
        return s;
    }

    public static String getBigimagePath() throws Exception {
        String s = getRootPath();
        if (TextUtils.isEmpty(s)) {
            return null;
        }
        s = s + Quan + bigImage;
        safeDirPath(s);
        return s;
    }

    /**
     * 获取小图储存路径
     *
     * @throws IOException
     */
    public static String getSmallimagePath() throws Exception {
        String s = getRootPath();
        if (TextUtils.isEmpty(s)) {
            return null;
        }
        s = s + Quan + smallImage;
        safeDirPath(s);
        return s;
    }

    /**
     * 获取SD卡跟目录
     *
     * @return
     */
    private static String getRootPath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 存在SD卡
            return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        } else {
            return null;
        }
    }

    public static String getFileName(File file) {
        return file.getName();
    }

    public static void safeFilePath(String filePath) throws Exception {
        File f = new File(filePath);
        safeFile(f);
    }

    public static void safeDirPath(String filePath) throws Exception {
        File f = new File(filePath);
        safeDir(f);
    }

    /**
     * 防止出现目录不存在异常
     *
     * @param file
     * @throws IOException
     */
    public static void safeDir(File file) throws Exception {
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 防止出现文件不存在异常
     *
     * @param file
     * @throws IOException
     */
    public static void safeFile(File file) throws Exception {
        if (file.exists()) {
            return;
        }
        File f = file.getParentFile();
        if (!f.exists()) {
            f.mkdirs();
        }
        file.createNewFile();
    }

    /**
     * 判断文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean pathExistsFile(String path) {
        try {
            File f = new File(path);
            return f.exists() && f.length() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 拷贝文件
     *
     * @param srcFilePath
     * @param desFilePath
     * @return
     * @throws Exception
     */
    public static boolean copeFileTo(String srcFilePath, String desFilePath) throws Exception {
        File srcFile = new File(srcFilePath);
        if (!srcFile.exists() || srcFile.length() < 1) {
            return false;
        }
        LocalFileTools.deleteImgByName(desFilePath.substring(desFilePath.lastIndexOf(File.separator) + 1));
        File desFile = new File(desFilePath);
        LocalFileTools.safeFile(desFile);
        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(desFile);
        byte[] b = new byte[1024 * 16];
        int len = 0;
        while ((len = fis.read(b)) > 0) {
            fos.write(b, 0, len);
        }
        fos.flush();
        try {
            fos.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 根据文件名删除图片
     *
     * @param name
     */
    public static void deleteImgByName(String name) {
        try {
            String[] dirs = {getOriginalimagePath(), getBigimagePath(), getSmallimagePath()};
            for (int i = 0; i < dirs.length; i++) {
                File file = new File(dirs[i] + name);
                if (file.exists()) {
                    file.delete();
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static boolean getImgWidthAndHeight(String path) {
        boolean isWidthLarge = true;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(path);
        } catch (FileNotFoundException e) {
        }
        BitmapFactory.Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(fs, null, options);
        if (options.outWidth < options.outHeight) {
            isWidthLarge = false;
        }

        return isWidthLarge;
    }

    /**
     * 传入图片Uri，判断Exif信息，处理图片角度 旋转bitmap
     *
     * @param b       图片对象
     * @param degrees 选择角度 顺时针
     * @return
     */
    public static Bitmap rotate(Bitmap b, int degrees) {
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                // 出现了内存不足异常，最好return 原始的bitmap对象。.
                return b;
            }
        }
        return b;
    }

}