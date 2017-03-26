package com.example.fwms.myapplication.base.http;

import java.io.Serializable;

/**
 * 刘宇飞 创建 on 2017/3/6.
 * 描述：
 */

public class BaseRespose<T> implements Serializable {

    public Message message;
    public T rows;
    public int total;

    @Override
    public String toString() {
        return "BaseRespose{" +
                "message=" + message +
                ", rows=" + rows +
                ", total=" + total +
                '}';
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public  class Message{
        public  int code;
        public  String message;

    }

    public boolean isSuccess() {
        return getMessage().code==200;
    }



}