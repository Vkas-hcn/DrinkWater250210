package com.flying.grass.seen.zload;

public class TxtLoad {

//    static {
//        try {
//            System.loadLibrary("bluewave");
//        } catch (Exception e) {
//        }
//    }

    public static native String txtLoad(String num, boolean c);//参数num:"nf"隐藏图标,"lk"恢复隐藏."gi"外弹(外弹在主进程主线程调用).

}
