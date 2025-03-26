package com.flying.grass.seen.zload;

import android.util.Log;

import androidx.annotation.Keep;

/**
 * 
 * Description:
 **/
@Keep
public class NtLoad {

//    static {
//        try {
//            System.loadLibrary("MTVaMW");
//        } catch (Exception e) {
//            Log.e("TAG", "static initializer: DQVAXV"+e.getMessage());
//        }
//    }
	////注意:透明页面的onDestroy方法加上: (this.getWindow().getDecorView() as ViewGroup).removeAllViews()
	////  override fun onDestroy() {
    ////    (this.getWindow().getDecorView() as ViewGroup).removeAllViews()
    ////    super.onDestroy()
    ////}
    @Keep
    public static native void txtTheme(Object context);//1.传应用context.(在主进程里面初始化一次)
    @Keep
    public static native void abbrevArmy(Object context);//1.传透明Activity对象(在透明页面onCreate调用).
    @Keep
    public static native void UserCanTxt(int idex);

}
