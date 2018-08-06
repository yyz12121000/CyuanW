package com.exingxiao.insureexpert.tools.zoom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.yyz.cyuanw.tools.zoom.LocalFileTools;
import com.yyz.cyuanw.tools.zoom.Tailor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Title: ZoomLocalImg.java Description: 缩小本地图片
 *
 * @author 杨亚洲
 * @version 1.0
 * @date 2014-1-11
 */
public class ZoomLocalImg {
    private long fileCeiling = 1024 * 50;// 文件大小上限
    private int sizeCeiling = 1080 * 1920; // 尺寸像素上限

    public String zoomLocalImgToSekormNorm(String sdImgPath) {
        return zoomLocalImgToSekormNorm(sdImgPath, fileCeiling);
    }

    /**
     * 压缩指定路径图片至世强文件夹
     *
     * @param sdImgPath
     * @return
     */
    public String zoomLocalImgToSekormNorm(String sdImgPath, long fileCeiling) {
        this.fileCeiling = fileCeiling;
        if (null == sdImgPath || sdImgPath.length() == 0) {
            return "";
        }
        try {
            if (LocalFileTools.pathExistsFile(sdImgPath)) {// 此文件确实存在
//                String desFilePath = LocalFileTools.getSekormDir() + System.currentTimeMillis() + ".jpg";
                String desFilePath = LocalFileTools.getImgTempPath() + System.currentTimeMillis() + ".jpg";
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(sdImgPath, opts);
                if (this.sizeCeiling > opts.outWidth * opts.outHeight) {// 没有超过大图像素总数上限，那就不需要裁剪
                    File f = new File(sdImgPath);
                    if (f.length() < fileCeiling) {// 图片文件大小没有超过上传上限
                        if (LocalFileTools.copeFileTo(sdImgPath, desFilePath)) {
                            return desFilePath;
                        }
                        return "";
                    } else {
                        Bitmap bitmap = BitmapFactory.decodeFile(sdImgPath);
                        compressBmpToFile(bitmap, desFilePath);// 压缩文件大小并存至世强文件夹
                        return desFilePath;
                    }
                } else {// 需要裁剪
                    Bitmap bitmap = Tailor.createImageThumbnail(sdImgPath, this.sizeCeiling);
                    compressBmpToFile(bitmap, desFilePath);// 压缩文件大小并存至世强文件夹
                    return desFilePath;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 压缩指定路径图片至输入流
     *
     * @param sdImgPath
     * @param name
     * @return
     */
    public InputStream zoomLocalImgToInputSteam(String sdImgPath, String name) {
        name = "" + name.hashCode();
        try {
            if (LocalFileTools.pathExistsFile(sdImgPath)) {// 此文件确实存在
                String desFilePath = LocalFileTools.getBigimagePath() + name;
                if (LocalFileTools.copeFileTo(sdImgPath, desFilePath)) {

                }
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(sdImgPath, opts);
                if (this.sizeCeiling > opts.outWidth * opts.outHeight) {// 没有超过大图像素总数上限，那就不需要裁剪
                    File f = new File(sdImgPath);
                    if (f.length() < fileCeiling) {// 图片文件大小没有超过上传上限
                        return new FileInputStream(sdImgPath);
                    } else {
                        Bitmap bitmap = BitmapFactory.decodeFile(sdImgPath);
                        return compressBmpToInputStream(bitmap);
                    }
                } else {// 需要裁剪
                    Bitmap bitmap = Tailor.createImageThumbnail(sdImgPath, this.sizeCeiling);
                    return compressBmpToInputStream(bitmap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 压缩Bitmap并返回InputStream
     *
     * @param bmp
     * @return
     * @throws Exception
     */
    private InputStream compressBmpToInputStream(Bitmap bmp) throws Exception {
        ByteArrayOutputStream baos = compressBmpToByteArrayOutputStream(bmp);
        if (null == baos) {
            return null;
        }
        return new ByteArrayInputStream(baos.toByteArray());
    }

    private void compressBmpToFile(Bitmap bmp, String desFilePath) throws Exception {
        ByteArrayOutputStream baos = compressBmpToByteArrayOutputStream(bmp);
        if (null == baos) {
            return;
        }
        LocalFileTools.deleteImgByName(desFilePath.substring(desFilePath.lastIndexOf(File.separator) + 1));
        LocalFileTools.safeFilePath(desFilePath);
        FileOutputStream fos = new FileOutputStream(desFilePath);
        fos.write(baos.toByteArray());
        fos.flush();
        try {
            fos.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ByteArrayOutputStream compressBmpToByteArrayOutputStream(Bitmap bmp) throws Exception {
        if (null == bmp) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length > fileCeiling) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        return baos;
    }

    /*public long getFileCeiling() {
        return fileCeiling;
    }

    public void setFileCeiling(long fileCeiling) {
        this.fileCeiling = fileCeiling;
    }

    public int getSizeCeiling() {
        return sizeCeiling;
    }

    public void setSizeCeiling(int sizeCeiling) {
        this.sizeCeiling = sizeCeiling;
    }*/
}
