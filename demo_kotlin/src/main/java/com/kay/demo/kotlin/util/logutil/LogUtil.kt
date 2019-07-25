package com.kay.demo.kotlin.util.logutil

import android.text.TextUtils
import android.util.Log
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.*

object LogUtil {

    var debug = true
    private const val platFolder = ".kay"
    private const val kay = "kay"

    //                return "[ Thread:" + Thread.currentThread().getName() + ", at " + st.getClassName() + "." + st.getMethodName()
    //                        + "(" + st.getFileName() + ":" + st.getLineNumber() + ")" + " ]";
    private val functionName: String?
        get() {
            val sts = Thread.currentThread().stackTrace
            if (sts != null) {
                for (st in sts) {
                    if (st.isNativeMethod) {
                        continue
                    }
                    if (st.className == Thread::class.java.name) {
                        continue
                    }
                    if (st.className == LogUtil::class.java.name) {
                        continue
                    }
                    return "[" + st.methodName + "(" + st.fileName + ":" + st.lineNumber + ")" + "]"
                }
            }
            return null
        }

    /**
     * 日志输出格式: 时间 +日志内容   时间格式: "yyyy-MM-dd HH:mm:ss"
     * 文件日志内容输出格式 ： 文件名---函数名---输出内容
     */
    fun e(fileName: String, funName: String, msg: String) {
        if (fileIsExist()) {
            writeLogFromSDCard(fileName, funName, msg)
        }

        if (debug) {
            if (!TextUtils.isEmpty(msg)) {
                Log.e(fileName, "-----$kay----- $functionName")
                Log.e(fileName, "-----$kay----- $msg")
            }
        }
    }

    fun i(fileName: String, funName: String, msg: String) {
        if (fileIsExist()) {
            writeLogFromSDCard(fileName, funName, msg)
        }

        if (debug) {
            if (!TextUtils.isEmpty(msg)) {
                Log.w(fileName, "-----$kay----- $functionName")
                Log.i(fileName, "-----$kay----- $msg")
            }
        }
    }

    /**
     * 日志输出格式: 时间 +日志内容   时间格式: "yyyy-MM-dd HH:mm:ss"
     * 文件日志内容输出格式 ： 文件名---函数名---输出内容
     */
    fun e2cp(fileName: String, funName: String, msg: String) {
        if (fileIsExist()) {
            writeLogFromSDCard("", "", msg)
        }
        Log.w(fileName, "-----$kay----- $functionName")
        Log.e(fileName, "-----$kay----- $msg")
    }

    fun d(tag: String, msg: String) {
        if (fileIsExist()) {
            writeLogFromSDCard("", "", msg)
        }
        if (debug) {
            if (!TextUtils.isEmpty(msg)) {
                Log.w(tag, "-----$kay----- $functionName")
                Log.d(tag, "-----$kay----- $msg")
            }
        }
    }

    fun e(msg: String) {
        e("TAG", msg)
    }

    /**
     * 输出到控制台
     *
     * @param msg
     */
    fun e(tag: String, msg: String) {
        if (fileIsExist()) {
            writeLogFromSDCard("", "", msg)
        }
        if (debug) {
            if (!TextUtils.isEmpty(msg)) {
                Log.w(tag, "-----$kay----- $functionName")
                Log.e(tag, "-----$kay----- $msg")
            }
        }
    }

    fun i(tag: String, msg: String) {
        if (fileIsExist()) {
            writeLogFromSDCard("", "", msg)
        }
        if (debug) {
            if (!TextUtils.isEmpty(msg)) {
                Log.w(tag, "-----$kay----- $functionName")
                Log.i(tag, "-----$kay----- $msg")
            }
        }
    }

    /**
     * 判断outdebug文件是否存在
     */
    private fun fileIsExist(): Boolean {
        val sDStateString = android.os.Environment.getExternalStorageState()
        if (sDStateString == android.os.Environment.MEDIA_MOUNTED) {
            try {
                val SDFile = android.os.Environment.getExternalStorageDirectory()
                val logDir = File(SDFile.absolutePath + File.separator + platFolder)
                val logFlagFile =
                    File(SDFile.absolutePath + File.separator + platFolder + File.separator + "outdebug")
                val logFile =
                    File(SDFile.absolutePath + File.separator + platFolder + File.separator + "outdebuginfo.log")
                if (!logDir.exists()) {
                    logDir.mkdir()
                    return false
                }

                if (!logFlagFile.exists()) {
                    if (logFile.exists()) {
                        logFile.delete()
                    }
                    return false
                }
            } catch (e: Exception) {
                return false
            }

        }
        return true
    }

    /**
     * Log info 写入SD卡
     */
    private fun writeLogFromSDCard(fileName: String, funName: String, msg: String) {
        val sDStateString = android.os.Environment.getExternalStorageState()
        val date_msg: String
        if (sDStateString == android.os.Environment.MEDIA_MOUNTED) {
            try {
                val SDFile = android.os.Environment.getExternalStorageDirectory()
                val myDir = File(SDFile.absolutePath + File.separator + platFolder)
                val myFile =
                    File(SDFile.absolutePath + File.separator + platFolder + File.separator + "outdebuginfo.log")
                if (!myDir.exists()) {
                    myDir.mkdir()
                    myFile.createNewFile()
                } else if (!myFile.exists()) {
                    myFile.createNewFile()
                }
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val dateString = sdf.format(Date())
                if (TextUtils.isEmpty(msg)) {
                    date_msg = "$dateString    $fileName---$funName\r\n"
                } else {
                    date_msg = "$dateString    $fileName---$funName---$msg\r\n"
                }
                val outputStream =
                    BufferedWriter(OutputStreamWriter(FileOutputStream(myFile, true)))
                outputStream.write(date_msg)
                outputStream.close()
            } catch (e: Exception) {

            }

        }
    }

}
