package com.qinglianyun.tracelib;

import android.util.Log;

/**
 * Created by tang_xqing on 2020/8/27.
 */
public class TracePrint {
    private final static String TAG = TracePrint.class.getSimpleName();

    public static void print(String msg) {
        Log.d(TAG, "-------------> " + msg);
    }
}
