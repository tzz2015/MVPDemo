package com.example.fwms.myapplication.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.commonlibary.pickerview.util.GlideRoundTransformUtil;
import com.example.fwms.myapplication.base.http.Api;

import java.io.File;

/**
 * Description : 图片加载工具类 使用glide框架封装
 */
public class ImageLoaderUtils {

    public static void display(Context context, ImageView imageView, String url, int placeholder, int error) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).placeholder(placeholder)
                .error(error).crossFade().into(imageView);
    }

    public static void display(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        //项目中返回图片url 并不完整 需要拼接
        if(url!=null&&!url.contains("http")){
            url= Api.FILE_HOST+url;
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(com.example.commonlibary.R.drawable.ic_image_loading)
                .error(com.example.commonlibary.R.drawable.ic_empty_picture)
                .crossFade().into(imageView);
    }

    public static void display(Context context, ImageView imageView, File url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }

        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(com.example.commonlibary.R.drawable.ic_image_loading)
                .error(com.example.commonlibary.R.drawable.ic_empty_picture)
                .crossFade().into(imageView);
    }
    public static void displaySmallPhoto(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        //项目中返回图片url 并不完整 需要拼接
        if(url!=null&&!url.contains("http")){
            url= Api.FILE_HOST+url;
        }
        Glide.with(context).load(url).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(com.example.commonlibary.R.drawable.ic_image_loading)
                .error(com.example.commonlibary.R.drawable.ic_empty_picture)
                .thumbnail(0.5f)
                .into(imageView);
    }
    public static void displayBigPhoto(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        //项目中返回图片url 并不完整 需要拼接
        if(url!=null&&!url.contains("http")){
            url= Api.FILE_HOST+url;
        }
        Glide.with(context).load(url).asBitmap()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(com.example.commonlibary.R.drawable.ic_image_loading)
                .error(com.example.commonlibary.R.drawable.ic_empty_picture)
                .into(imageView);
    }
    public static void display(Context context, ImageView imageView, int url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }

        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(com.example.commonlibary.R.drawable.ic_image_loading)
                .error(com.example.commonlibary.R.drawable.ic_empty_picture)
                .crossFade().into(imageView);
    }
    public static void displayRound(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        //项目中返回图片url 并不完整 需要拼接
        if(url!=null&&!url.contains("http")){
           url= Api.FILE_HOST+url;
       }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(com.example.commonlibary.R.drawable.defaut_photo)
                .centerCrop().transform(new GlideRoundTransformUtil(context)).into(imageView);
    }

}
