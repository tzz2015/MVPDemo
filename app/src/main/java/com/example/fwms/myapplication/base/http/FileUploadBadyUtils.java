package com.example.fwms.myapplication.base.http;


import com.example.fwms.myapplication.base.interfaces.AccountInfo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 刘宇飞 创建 on 2017/3/16.
 * 描述：设置上传文件请求体
 */

public class FileUploadBadyUtils {


    /**
     * 单个文件的请求体
     */
    public static Map<String, RequestBody> getRequestBody(String path,int type) {
        File file = new File(path);

        //传递参数
        RequestBody tempPath = RequestBody.create(MediaType.parse("text/plain"), "/serviceconsole");
        RequestBody isControl = RequestBody.create(MediaType.parse("text/plain"), type+"");
        RequestBody versionCode = RequestBody.create(MediaType.parse("text/plain"), AccountInfo.versionCode);
        RequestBody companyCode = RequestBody.create(MediaType.parse("text/plain"), AccountInfo.companyCode);
        RequestBody fileuploadFileName = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        //传递文件
        RequestBody fileBody = RequestBody.create(MediaType.parse("*/*"), file);
        Map<String, RequestBody> params = new HashMap<>();
        params.put("tempPath", tempPath);
        params.put("isControl", isControl);
        params.put("versionCode", versionCode);
        params.put("companyCode", companyCode);
        params.put("fileuploadFileName", fileuploadFileName);
        params.put("fileupload\"; filename=\""+file.getName()+"\"", fileBody);



        return params;
    }


}
