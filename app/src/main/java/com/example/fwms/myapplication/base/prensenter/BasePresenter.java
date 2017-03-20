package com.example.fwms.myapplication.base.prensenter;


import com.example.fwms.myapplication.base.activtiy.BaseActivity;
import com.example.fwms.myapplication.base.http.HttpTask;

import java.util.Map;

/**
 * 公司：杭州融科网络科技
 * 刘宇飞 创建 on 2017/3/6.
 * 描述：p 基类
 */

public abstract class BasePresenter<T>{
    public BaseActivity mContext;
    public T mView;
    public HttpTask httpTask;


    public void setView(T v) {
        this.mView = v;
        this.onStart();
    }
    /**
     * 传入map，转换成符合格式的url请求
     *
     * @param map
     * @return
     */
    public  String Map2String(Map<String, String> map) {
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
//            String s1="\""+key+"\""+":"+value+",";
            String s1 = key + ":'" + value + "',";
            sb.append(s1);
        }
        String s = sb.toString();
        String s1 = s.substring(0, s.length() - 1);
        String result = s1 + "}";
        return result;



    }


    public void onStart(){
    };
    public void onDestroy() {
         mView=null;

    }
}