package com.example.fwms.myapplication.base.http;


import com.example.fwms.myapplication.base.model.FileUploadModel;
import com.example.fwms.myapplication.net.modle.CompanyInfoModle;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;


/**
 * 公司：杭州融科网络科技
 * 刘宇飞 创建 on 2017/3/15.
 * 描述：api
 */

public interface HttpTask<T> {
    //共同请求
    @POST("{prefixurl}/{url}")
    Observable<BaseRespose<T>> requestHttp(@Path(value = "prefixurl") String prefixurl, @Path(value = "url") String url, @Field("jsonBean") String pram);

    //共同请求   必须加上@FormUrlEncoded 防止乱码
    @FormUrlEncoded
    @POST("{url}")
    Observable<BaseRespose> requestHttp(@Path(value = "url") String url, @Field(value = "jsonBean") String pram);

    //文件上传
    @Multipart
    @POST(Api.FILEUPLOD)
    Observable<BaseRespose<FileUploadModel>> postFile(@PartMap Map<String, RequestBody> bodyMap);

    //文件下载
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);



    //11.6.6业主绑定 点击单位名称查看详情
    @FormUrlEncoded
    @POST(Api.QUERYAPP_COMPANYBYCODE)
    Observable<BaseRespose<CompanyInfoModle>> requestCompanybycode(@Field("jsonBean") String pram);
}
