package com.example.fwms.myapplication.net.presenter;

import android.os.Environment;

import com.example.fwms.myapplication.base.http.Api;
import com.example.fwms.myapplication.base.http.ComRequestUtil;
import com.example.fwms.myapplication.base.http.HttpClient;
import com.example.fwms.myapplication.base.http.HttpPresenter;
import com.example.fwms.myapplication.base.http.HttpTask;
import com.example.fwms.myapplication.base.http.HttpTaskListener;
import com.example.fwms.myapplication.base.interfaces.AccountInfo;
import com.example.fwms.myapplication.base.interfaces.Constants;
import com.example.fwms.myapplication.base.model.FileUploadModel;
import com.example.fwms.myapplication.net.contract.NetActivityContract;
import com.example.fwms.myapplication.net.modle.CompanyInfoModle;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * 杭州融科网络
 * 刘宇飞 创建 on 2017/3/20.
 * 描述：
 */

public class NetActivityPresenter extends NetActivityContract.Presenter implements HttpTaskListener {
    HttpTask yibanTask;

    /**
     * 公共请求  是指返回的对象相同 url 和参数不同  我目前的项目比较多这一类请求
     */
    @Override
    public void requestDemo1() {
        // http://106.14.20.156:8083/AppServiceProvider/appUserCheckLogin.htm?
        // jsonBean={userName:15158137302,password:123456}
        Map<String, String> map = new HashMap<>();
        map.put("userName", "15158137302");
        map.put("password", "123456");
        //最后一个参数是 请求id  不输入默认（）
        ComRequestUtil.postCommonInfo(Api.APPUSER_CHECKLOGIN, Map2String(map), mContext, this);
    }

    /**
     * 一般请求
     */
    @Override
    public void requestDemo2() {
        // url =http://106.14.20.156:8083/MiddleServer/queryAppCompanyByCode.htm?jsonBean=
        // {isBingCompanyCode:'20170314-124521-5',companyCode:'20170314-115614-2'}

        Map<String, String> map = new HashMap<>();
        map.put("isBingCompanyCode", "20170314-124521-5");
        map.put("companyCode", "20170314-115614-2");
        //这里是自定义baseUrl的请求 Api.BASE_ZHONGJIAN_URL
        //如果是默认请求 直接使用基类已经初始化的httpTask
        //项目中示例： Observable observable = httpTask.requestRelationlog(pram);
//        HttpPresenter<List<ShenQingInfoModel>> httpPresenter=new HttpPresenter<>();
//        httpPresenter.request(observable, Constants.REQUESTID_0,this,this);
        if (yibanTask == null) {
            yibanTask = HttpClient.createRequest(HttpTask.class, Api.BASE_ZHONGJIAN_URL);
        }
        Observable observable = yibanTask.requestCompanybycode(Map2String(map));
        // HttpPresenter<请求成功返回的对象，也可以是list<对象>>  如： HttpPresenter<List<CompanyInfoModle>>
        HttpPresenter<CompanyInfoModle> httpPresenter = new HttpPresenter<>();
        httpPresenter.request(observable, Constants.REQUESTID_0, this, mContext);
    }

    /**
     * 文件上传
     */
    @Override
    public void requestDemo3() {
        AccountInfo.companyCode = "20170314-124521-5";
        AccountInfo.versionCode = "20161201093636-FREE";
        String path = Environment.getExternalStorageDirectory() + "/test.apk";
        //可写请求id
        ComRequestUtil.uploadFile(path, mContext, this);
    }

    /**
     * 文件下载
     */
    @Override
    public void requestDemo4() {
        String path="http://b.hiphotos.baidu.com/zhidao/pic/item/d833c895d143ad4b787f4d0487025aafa50f06a4.jpg";
      ComRequestUtil.downFile(path,Constants.REQUESTID_DOWN,mContext,this);
    }

    /**
     * @param requestId 请求id
     * @param object    请求成功返回的对象 不用判断是否成功 直接强转
     */
    @Override
    public void onSuccess(int requestId, Object object) {
        switch (requestId) {
            case Constants.REQUESTID_COMMON://公共请求id 不指定就默认
                mContext.showToast("用户已经登录");
                mView.setView("公共请求成功");
                break;
            case Constants.REQUESTID_0://一般请求
                if (object instanceof CompanyInfoModle) {
                    CompanyInfoModle modle = (CompanyInfoModle) object;
                    mView.setView(modle.toString());
                }
                break;
            case Constants.REQUESTID_FILE://上传文件
                if (object instanceof FileUploadModel) {
                    mView.setView("文件上传成功，服务器返回上传文件的存放地址："
                            + ((FileUploadModel) object).downloadPath);
                }
                break;
            case Constants.REQUESTID_DOWN://文件下载
                String savePath= (String) object;
                mView.setView("文件下载成功,保存在："+savePath);
                break;
        }
    }

    /**
     * @param requestId 请求id
     * @param code      请求失败的code
     */
    @Override
    public void onException(int requestId, int... code) {

    }
}
