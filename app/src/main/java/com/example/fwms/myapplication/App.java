package com.example.fwms.myapplication;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * 杭州融科网络
 * 刘宇飞 创建 on 2017/3/20.
 * 描述：
 */

public class App extends Application {
    //主线程id
    private static int mMainThreadId;
    //全局的handler
    private static Handler mHandler;
    private static App instance;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        mHandler = new Handler();
        //主线程id,就是MyApplication(主线程)线程id,获取当前线程id
        mMainThreadId = android.os.Process.myTid();
        instance = this;

    }

    /**
     * 获取全局上下文
     *
     * @return
     */
    public static Context getContext() {
        return context;
    }

    public static int getMainThreadId() {
        return mMainThreadId;

    }

    public static App getInstance() {
        return instance;
    }

    public static Handler getHandler() {
        return mHandler;
    }


}
