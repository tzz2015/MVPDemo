package com.example.fwms.myapplication.base.model;

import android.view.View;

import java.io.Serializable;

/**
 * CommonRecyclerModel. <br>
 * 所有recycler数据源的基类.
 *
 * @author lipeng
 * @version 1.0.0
 *          2016/2/19 18:08
 * @email 383355732@qq.com
 */
public class BaseRecyclerModel implements Serializable {
    /**
     * view的区分
     */
    public int viewType;

    /**
     * 事件
     */
    public View.OnClickListener listener;
}
