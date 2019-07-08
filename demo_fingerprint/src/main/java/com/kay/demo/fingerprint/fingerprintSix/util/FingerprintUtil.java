package com.kay.demo.fingerprint.fingerprintSix.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class FingerprintUtil {
    private static final String TAG = FingerprintUtil.class.getSimpleName();

    public static void openFingerPrintSettingPage(Context context) {
        String pcgName = "com.android.settings";
        String clsName = "com.android.settings.Settings";
        if (SystemUtil.isHuaWei()){
            clsName = "com.android.settings.fingerprint.FingerprintSettingsActivity";
            Log.e(TAG,"此设备是华为...");
        }
        if (SystemUtil.isOppo()){
            pcgName = "com.coloros.fingerprint";
            clsName = "com.coloros.fingerprint.FingerLockActivity";
            Log.e(TAG,"此设备是OPPO...");
        }
        if (SystemUtil.isSony()){
            clsName = "com.android.settings.Settings$FingerprintEnrollSuggestionActivity";
            Log.e(TAG,"此设备是Sony...");
        }

        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(pcgName, clsName);
        intent.setComponent(componentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,"打开设置界面异常:" + e.getMessage());
            Intent theIntent = new Intent();
            ComponentName thecomponentname = new ComponentName("com.android.settings", "com.android.settings.Settings");
            theIntent.setComponent(thecomponentname);
            theIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            theIntent.setAction(Intent.ACTION_VIEW);
            context.startActivity(theIntent);
        }
    }

}
