package com.yyz.cyuanw.tools;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;


/**
 * 日志管理类
 * 当isOpen ＝ false时，将打印流程日志
 *
 * @author Louis
 */
public class LogManager {
    private static final String TAG = "DZJ";

    public static void d(String msg) {
        if (Final.IS_DEBUG) {
//            Logger.d(JsonFormat.format(msg));
        }
    }

    public static void w(String msg) {
        if (Final.IS_DEBUG) {
            Log.w(TAG, msg);
        } else {
//            Sentry.captureMessage(tag + "--" + msg, Sentry.SentryEventBuilder.SentryEventLevel.WARNING);
        }
    }

    public static void w(String msg, Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        if (Final.IS_DEBUG) {
            Log.e(TAG, msg + "--" + sw.toString());
        } else {
//            Sentry.captureMessage(msg + "-- e :" +sw.toString(), Sentry.SentryEventBuilder.SentryEventLevel.ERROR);
//            Sentry.captureException(e);
        }

    }

    public static void e(String msg) {
        if (Final.IS_DEBUG) {
            Log.e(TAG, msg);
        } else {
//            Sentry.captureMessage(tag + "--" + msg, Sentry.SentryEventBuilder.SentryEventLevel.ERROR);
        }
    }

    public static void e(Throwable e) {
//        StringWriter sw = new StringWriter();
//        PrintWriter pw = new PrintWriter(sw);
        if (Final.IS_DEBUG) {
            e.printStackTrace();
        } else {
//            Sentry.captureMessage(msg + "-- e :" +sw.toString(), Sentry.SentryEventBuilder.SentryEventLevel.ERROR);
//            Sentry.captureException(e);
        }
    }

    public static void captureError(String msg, String... extraLogs) {
        StringBuilder sb = new StringBuilder();
        if (extraLogs.length % 2 == 0) {
            for (int i = 0; i < extraLogs.length; i += 2) {
                sb.append(extraLogs[i] + ":" + extraLogs[i + 1] + ";");
            }
        }
        if (Final.IS_DEBUG) {
            Log.w("LogManager", "extraLogs:" + sb.toString());
        } else {
//            Sentry.captureMessage(msg+", extraLogs:"+sb.toString(), Sentry.SentryEventBuilder.SentryEventLevel.ERROR);
        }

    }

    public static void captureWarning(String msg, String... extraLogs) {
        StringBuilder sb = new StringBuilder();
        if (extraLogs.length % 2 == 0) {
            for (int i = 0; i < extraLogs.length; i += 2) {
                sb.append(extraLogs[i] + ":" + extraLogs[i + 1] + ";");
            }
        }
        if (Final.IS_DEBUG) {
            Log.w("LogManager", "extraLogs:" + sb.toString());
        } else {
//            Sentry.captureMessage(msg+", extraLogs:"+sb.toString(), Sentry.SentryEventBuilder.SentryEventLevel.WARNING);
        }
    }


}
