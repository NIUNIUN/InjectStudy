package com.qinglianyun.testapplication;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.meituan.robust.PatchExecutor;

/**
 * Created by tang_xqing on 2020/8/11.
 */
public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new PatchExecutor(this, new PatchManipulateImp(), new RobustCallBackSample()).start();
    }
}
