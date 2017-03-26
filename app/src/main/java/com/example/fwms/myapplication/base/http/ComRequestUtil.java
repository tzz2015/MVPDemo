package com.example.fwms.myapplication.base.http;

/**
 * 刘宇飞 创建 on 2017/3/16.
 * 描述：
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.commonlibary.pickerview.takephoto.util.FileUtils;
import com.example.commonlibary.pickerview.util.LogUtil;
import com.example.fwms.myapplication.base.activtiy.BaseActivity;
import com.example.fwms.myapplication.base.interfaces.Constants;
import com.example.fwms.myapplication.base.model.FileUploadModel;

import java.io.File;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */

public class ComRequestUtil {
    private static HttpTask httpTask;
    private static HttpTask commonTask;

    /**
     * 上传文件
     *
     * @param path     路径
     * @param mContext 上下文
     * @param listener 监听
     * @param arg      int 数组 int[0] 请求id  int[1] 默认为o
     */
    public static void uploadFile(String path, BaseActivity mContext, HttpTaskListener listener, int... arg) {
        int postId = Constants.REQUESTID_FILE;
        int type = 0;

        if (arg != null && arg.length >= 1) {
            postId = arg[0];
        }
        if (arg != null && arg.length >= 2) {
            type = arg[0];
        }

        Map<String, RequestBody> requestBody = FileUploadBadyUtils.getRequestBody(path, type);
        if (httpTask == null) {
            httpTask = HttpClient.createRequest(HttpTask.class, Api.FILE_HOST);
        }
        Observable observable = httpTask.postFile(requestBody);
        HttpPresenter<FileUploadModel> httpPresenter = new HttpPresenter<>();
        httpPresenter.request(observable, postId, listener, mContext);
    }

    /**
     * 公共请求
     */
    public static void postCommonInfo(String url, String pram, BaseActivity mContext, HttpTaskListener listener, int... arg) {
        int postId=Constants.REQUESTID_COMMON;
        if(arg!=null&&arg.length==1){
            postId=arg[0];
        }
        if (commonTask == null) {
            commonTask = HttpClient.createRequest(HttpTask.class);
        }
        HttpPresenter httpPresenter = new HttpPresenter<>();
        httpPresenter.request(commonTask.requestHttp(url, pram), postId, listener, mContext);
    }

    /**
     * 下载文件
     */
    public static void downFile(final String path, final int postId, final BaseActivity mContext, final HttpTaskListener listener) {
        if (commonTask == null) {
            commonTask = HttpClient.createRequest(HttpTask.class);
        }
        int indexOf = path.lastIndexOf("/");
        final String name = path.replaceAll("\"", "").substring(indexOf + 1, path.length());

        new AsyncTask<Void, Long, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mContext.showInfoProgressDialog();
            }

            @Override
            protected Void doInBackground(Void... params) {
                commonTask.downloadFile(path)
                        .subscribeOn(Schedulers.newThread())//请求网络在子线程中
                        .observeOn(AndroidSchedulers.mainThread())//回调在主线程中
                        .subscribe(new Subscriber<ResponseBody>() {

                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mContext.showToast("下载出错");
                                listener.onException(postId);
                                LogUtil.getInstance().e(e.getMessage());
                            }

                            @Override
                            public void onNext(ResponseBody requestBody) {
                                boolean disk = FileUtils.writeResponseBodyToDisk(requestBody, name);
                                if (disk) {
                                    listener.onSuccess(postId, FileUtils.SDPATH + name);
                                } else {
                                    mContext.showToast("存储出错");
                                }
                            }
                        });
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mContext.hideInfoProgressDialog();
            }
        }.execute();
    }

    /**
     * 打开文件
     *
     * @param file
     */
    public static void openFile(File file, Context context) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        String type = getMIMEType(file);
        //设置intent的data和Type属性。
        intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
        //跳转
        context.startActivity(intent);

    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    private static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
    /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    private static final String[][] MIME_MapTable = {
            //{后缀名， MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };
}

