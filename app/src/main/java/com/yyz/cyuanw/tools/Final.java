package com.yyz.cyuanw.tools;

import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;

/**
 * Created by Administrator on 2017/5/5.
 */

public class Final {
   public static final boolean IS_DEBUG = true;

    public static final String KEY_A = "key_a";
    public static final String KEY_B = "key_b";
    public static final String KEY_C = "key_c";
    public static final String KEY_D = "key_d";
    public static final String KEY_E = "key_e";
    public static final String KEY_F = "key_f";

    public static final String KEY_IS_FIRST = "key_is_first";
    public static final String KEY_DETAILS_AREA_VERSION = "key_details_area_version";

    public static final String TEST_IMG_URL = "http://imgsrc.baidu.com/imgad/pic/item/3b87e950352ac65c7a39bf50f1f2b21193138ad1.jpg";
    public static final String DOWNLOAD_FILE_NAME = App.context.getString(R.string.app_name) + "Download";


    public static final long FILE_CEILING_AVATAR = 1024 * 50;//头像大小上限   50kb
    public static final long FILE_CEILING_IMG = 1024 * 1024 * 2;//图片大小上限   2M

    public static final int IMG_W_150 = 150;//
    public static final int IMG_W_200 = 200;//
    public static final int IMG_W_500 = 500;//
    public static final int IMG_W_2000 = 2000;//

    /**
     * 网络交互超时时间
     */
    public static final int CONNECT_TIMEOUT = 10;
    public static final int WRITE_TIMEOUT = 20;
    public static final int READ_TIMEOUT = 60;
}
