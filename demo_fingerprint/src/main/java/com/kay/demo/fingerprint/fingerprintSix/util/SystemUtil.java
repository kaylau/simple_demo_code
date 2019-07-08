package com.kay.demo.fingerprint.fingerprintSix.util;

import android.os.Build;
import android.text.TextUtils;


public class SystemUtil {
    private static final String TAG = SystemUtil.class.getSimpleName();
    public static final String SYS_EMUI = "sys_emui";
    public static final String SYS_MIUI = "sys_miui";
    public static final String SYS_FLYME = "sys_flyme";
    public static final String SYS_OPPO = "sys_opporom";
    public static final String SYS_SONY = "sys_sonyrom";
    public static final String SYS_SANSUMG = "sys_sansungrom";
    public static final String SYS_OTHER = "sys_other";
    /*
    *   华为——Huawei
        魅族——Meizu
        小米——Xiaomi
        索尼——Sony
        oppo——OPPO
        LG——LG
        vivo——vivo
        三星——samsung
        乐视——Letv
        中兴——ZTE
        酷派——YuLong
        联想——LENOVO
    * */
    //EMUI标识
    public static final String BRAND_EMUI1 = "huawei";
    public static final String BRAND_EMUI2 = "honor";

    public static final String BRAND_MIUI = "xiaomi";

    private static final String BRAND_MEIZU = "meizu";
    private static final String BRAND_OPPO = "oppo";
    private static final String BRAND_SONY = "sony";
    private static final String BRAND_SAMSUNG = "samsung";
    private static final String BRAND_XIAOMI = "Xiaomi";
    private static final String BRAND_LG = "LG";
    private static final String BRAND_VIVO = "vivo";
    private static final String BRAND_LETV = "Letv";
    private static final String BRAND_ZTE = "ZTE";
    private static final String BRAND_COOLPAD = "YuLong";
    private static final String BRAND_LENOVO = "LENOVO";
    public static String getSystem() {
        String sysType = "";
        if (TextUtils.isEmpty(sysType)) {
            try {
                sysType = SYS_OTHER;
                String brand = Build.BRAND;
                if (brand.toLowerCase().contains(BRAND_EMUI1) || brand.toLowerCase().contains(BRAND_EMUI2)){
                    sysType = SYS_EMUI;
                }
                if (brand.toLowerCase().contains(BRAND_MIUI)){
                    sysType = SYS_EMUI;
                }
                if (brand.toLowerCase().contains(BRAND_SONY)){
                    sysType = SYS_SONY;
                }
                if (brand.toLowerCase().contains(BRAND_MEIZU)){
                    sysType = SYS_FLYME;
                }
                if (brand.toLowerCase().contains(BRAND_OPPO)){
                    sysType = SYS_OPPO;
                }
                if (brand.toLowerCase().contains(BRAND_SAMSUNG)){
                    sysType = SYS_SANSUMG;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                sysType = SYS_OTHER;
            }
        }
        return sysType;
    }


    /**
     * 是否华为手机
     * @return
     */
    public static boolean isHuaWei(){
        return TextUtils.equals(getSystem(),SYS_EMUI);
    }

    /*是否OPPO手机
     * @return
     */
    public static boolean isOppo(){
        return TextUtils.equals(getSystem(),SYS_OPPO);
    }

    /**是否sony手机
     * @return
     */
    public static boolean isSony(){
        return TextUtils.equals(getSystem(),SYS_SONY);
    }

    /**
     * 是否魅族手机
     * @return
     */
    public static boolean isMeizu(){
        return TextUtils.equals(getSystem(),SYS_FLYME);
    }

    /**
     * 是否三星手机
     * @return
     */
    public static boolean isSumsung(){
        return TextUtils.equals(getSystem(),SYS_SANSUMG);
    }

    /**
     * 是否小米手机
     * @return
     */
    public static boolean isXiaoMi(){
        return TextUtils.equals(getSystem(),SYS_MIUI);
    }
}