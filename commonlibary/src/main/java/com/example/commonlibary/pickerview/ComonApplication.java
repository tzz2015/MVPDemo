package com.example.commonlibary.pickerview;

import android.app.Application;
import android.graphics.Color;

import com.example.commonlibary.R;
import com.example.commonlibary.pickerview.city.PickUtil;
import com.example.commonlibary.pickerview.takephoto.util.GlideImageLoader;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;

/**
 * 杭州融科网络
 * 刘宇飞 创建 on 2017/3/20.
 * 描述：
 */

public class ComonApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
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

}
