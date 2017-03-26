package com.example.fwms.myapplication;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;

import com.example.commonlibary.pickerview.city.PickUtil;
import com.example.commonlibary.pickerview.takephoto.util.GlideImageLoader;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;

/**
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

        //初始化城市选择
        PickUtil.initPickView(this);

        initFallerFinal();

    }

    private void initFallerFinal() {
        //设置主题
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.rgb(0x38, 0x42, 0x48))
                .setFabNornalColor(Color.rgb(0x38, 0x42, 0x48))
                .setFabPressedColor(Color.rgb(0x20, 0x25, 0x28))
                .setCheckSelectedColor(getResources().getColor(R.color.color_2e8df4))
                .setCropControlColor(Color.rgb(0x38, 0x42, 0x48))
                .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(false)
                .setEnablePreview(true)
                .build();

        //配置imageloader
        cn.finalteam.galleryfinal.ImageLoader imageloader = new GlideImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageloader, theme)
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);
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
