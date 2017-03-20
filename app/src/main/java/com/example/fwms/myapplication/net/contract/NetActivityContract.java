package com.example.fwms.myapplication.net.contract;

import com.example.fwms.myapplication.base.prensenter.BasePresenter;
import com.example.fwms.myapplication.base.view.BaseView;

/**
 * 杭州融科网络
 * 刘宇飞 创建 on 2017/3/20.
 * 描述：
 */

public interface NetActivityContract {
    interface View extends BaseView{
        void setView(String s);//渲染界面
    }
    abstract class Presenter extends BasePresenter<View>{
        public abstract void requestDemo1();
        public abstract void requestDemo2();
        public abstract void requestDemo3();
        public abstract void requestDemo4();

    }
}
