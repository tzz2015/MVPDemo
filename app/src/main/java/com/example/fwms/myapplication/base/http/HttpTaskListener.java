package com.example.fwms.myapplication.base.http;

/**
 * 关于说明类. <br>
 * 类详细说明.
 *
 * @author lipeng
 * @version 1.0.0
 *          2016/2/17 20:35
 * @email 383355732@qq.com
 */
public interface HttpTaskListener {
    void onSuccess(int requestId, Object object);
    void onException(int requestId, int... code);
}
