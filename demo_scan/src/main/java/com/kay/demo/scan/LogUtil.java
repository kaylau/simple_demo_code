package com.kay.demo.scan;

import android.text.TextUtils;
import android.util.Log;

import com.kay.demo.scan.zbar.utils.Config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogUtil {

    public static final boolean DEBUG = true;

    /**
     * 日志输出格式: 时间 +日志内容   时间格式: "yyyy-MM-dd HH:mm:ss"
     * 文件日志内容输出格式 ： 文件名---函数名---输出内容
     */
    public static void e(String fileName, String funName, String msg) {
        if (fileIsExist()) {
            writeLogFromSDCard(fileName, funName, msg);
        }

        if (DEBUG) {
            if (!TextUtils.isEmpty(msg)) {
                Log.e(fileName, getFunctionName());
                Log.e(fileName, "-----pay--" + fileName + "--" + msg);
            }
        }
    }

    public static void i(String fileName, String funName, String msg) {
        if (fileIsExist()) {
            writeLogFromSDCard(fileName, funName, msg);
        }

        if (DEBUG) {
            if (!TextUtils.isEmpty(msg)) {
                Log.i(fileName, getFunctionName());
                Log.i(fileName, "-----pay--" + fileName + "--" + msg);
            }
        }
    }

    /**
     * 日志输出格式: 时间 +日志内容   时间格式: "yyyy-MM-dd HH:mm:ss"
     * 文件日志内容输出格式 ： 文件名---函数名---输出内容
     */
    public static void e2cp(String fileName, String funName, String msg) {
        if (fileIsExist()) {
            writeLogFromSDCard("", "", msg);
        }
        Log.e(fileName, getFunctionName());
        Log.e(fileName, "-----pay--" + fileName + "--" + msg);
    }

    public static void d(String tag, String msg) {
        if (fileIsExist()) {
            writeLogFromSDCard(tag, "", msg);
        }
        if (DEBUG) {
            if (!TextUtils.isEmpty(msg)) {
                Log.d(tag, getFunctionName());
                Log.d(tag, "-----pay--" + tag + "--"  + msg);
            }
        }
    }

    /**
     * 输出到控制台
     *
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (fileIsExist()) {
            writeLogFromSDCard(tag, "", msg);
        }
        if (DEBUG) {
            if (!TextUtils.isEmpty(msg)) {
                Log.e(tag, getFunctionName());
                Log.e(tag, "-----pay--" + tag + "--" + msg);
            }
        }
    }

    public static void i(String tag, String msg) {
        if (fileIsExist()) {
            writeLogFromSDCard("", "", msg);
        }
        if (DEBUG) {
            if (!TextUtils.isEmpty(msg)) {
                Log.i(tag, getFunctionName());
                Log.i(tag, "-----pay--" + tag + "--" + msg);
            }
        }
    }

    /**
     * 判断outdebug文件是否存在
     **/
    private static boolean fileIsExist() {
        String sDStateString = android.os.Environment.getExternalStorageState();
        if (sDStateString.equals(android.os.Environment.MEDIA_MOUNTED)) {
            try {
                File SDFile = android.os.Environment.getExternalStorageDirectory();
                File logDir = new File(SDFile.getAbsolutePath() + File.separator + Config.PLATFORMID_FOLDER);
                File logFlagFile = new File(SDFile.getAbsolutePath() + File.separator + Config.PLATFORMID_FOLDER + File.separator + "outdebug");
                File logFile = new File(SDFile.getAbsolutePath() + File.separator + Config.PLATFORMID_FOLDER + File.separator + "outdebuginfo.log");
                if (!logDir.exists()) {
                    logDir.mkdir();
                    return false;
                }

                if (!logFlagFile.exists()) {
                    if (logFile.exists()) {
                        logFile.delete();
                    }
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    /**
     * Log info 写入SD卡
     */
    private static void writeLogFromSDCard(String fileName, String funName, String msg) {
        String sDStateString = android.os.Environment.getExternalStorageState();
        String date_msg;
        if (sDStateString.equals(android.os.Environment.MEDIA_MOUNTED)) {
            try {
                File SDFile = android.os.Environment.getExternalStorageDirectory();
                File myDir = new File(SDFile.getAbsolutePath() + File.separator + Config.PLATFORMID_FOLDER);
                File myFile = new File(SDFile.getAbsolutePath() + File.separator + Config.PLATFORMID_FOLDER + File.separator + "outdebuginfo.log");
                if (!myDir.exists()) {
                    myDir.mkdir();
                    myFile.createNewFile();
                } else if (!myFile.exists()) {
                    myFile.createNewFile();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date date = new Date();
                String dateString = sdf.format(date);
                if (TextUtils.isEmpty(msg)) {
                    date_msg = dateString + "(" + date.getTime() + ")" + "    " + fileName + "---" + funName + "\r\n";
                } else {
                    date_msg = dateString + "(" + date.getTime() + ")" + "    " + fileName + "---" + funName + "---" + msg + "\r\n";
                }
                BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(myFile, true)));
                outputStream.write(date_msg);
                outputStream.close();
            } catch (Exception e) {

            }
        }
    }

    private static String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts != null) {
            for (StackTraceElement st : sts) {
                if (st.isNativeMethod()) {
                    continue;
                }
                if (st.getClassName().equals(Thread.class.getName())) {
                    continue;
                }
                if (st.getClassName().equals(LogUtil.class.getName())) {
                    continue;
                }
//                return "[ Thread:" + Thread.currentThread().getName() + ", at " + st.getClassName() + "." + st.getMethodName()
//                        + "(" + st.getFileName() + ":" + st.getLineNumber() + ")" + " ]";
                return "[" + st.getMethodName() + "(" + st.getFileName() + ":" + st.getLineNumber() + ")" + "]";
            }
        }
        return null;
    }

}
