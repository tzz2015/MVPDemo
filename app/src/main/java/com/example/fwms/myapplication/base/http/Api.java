package com.example.fwms.myapplication.base.http;


/**
 * 刘宇飞 创建 on 2017/3/16.
 * 描述：
 */

public class Api {

    /**
     * 业主的基础URL
     */
    public static String BASE_YEZHU_URL = "http://ataits.com:8089/AppCloudServer/";
    /**
     * 中间平台的基础URL
     */
    public static String BASE_ZHONGJIAN_URL = "http://106.14.20.156:8083/MiddleServer/";

    /**
     * 基础地址
     */
    public static  String API_HOST =BASE_YEZHU_URL;

    /**
     * 文件服务器地址  业主端https
     */
    public static final String FILE_PROP = "https://www.easyitom.com:8786/CloudServerFile/";
    /**
     * 上传文件地址
     */
    public static  String FILE_HOST = FILE_PROP;

    /**
     * 43.0.0文件上传接口
     */
    public static final String FILEUPLOD = "appUploadFile.htm";

    /**
     * 检查登录
     */
    public static final String APPUSER_CHECKLOGIN="appUserCheckLogin.htm";

    /**
     * 11.6.6业主绑定 点击单位名称查看详情
     */
    public static final String QUERYAPP_COMPANYBYCODE="appQueryByEmployeeId.htm";

}
