package com.example.fwms.myapplication.base.http;


import com.example.fwms.myapplication.base.activtiy.BaseActivity;
import com.example.fwms.myapplication.utils.Util;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 关于说明类. <br>
 * 类详细说明.
 *
 * @author lipeng
 * @version 1.0.0
 *          2016/2/24 17:08
 * @email 383355732@qq.com
 */
public class HttpPresenter<T> {
    /**
     *
     * @param observable
     * @param requestId  请求id
     * @param listener   监听
     * @param mContext
     * @return
     */
    public Subscription request(Observable observable, final  int requestId, final HttpTaskListener listener, final BaseActivity mContext) {
        mContext.showInfoProgressDialog();
        Subscription subscribe = observable
                .subscribeOn(Schedulers.newThread())//请求网络在子线程中
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程中
                .subscribe(new Subscriber<BaseRespose<T>>() {
                    @Override
                    public void onCompleted() {
                            mContext.hideInfoProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Util.showToast("网络异常,请稍候再试");
                        listener.onException(requestId);
                            mContext.hideInfoProgressDialog();

                    }

                    @Override
                    public void onNext(BaseRespose<T> baseResponseVo) {
                        try {
                            if(baseResponseVo.isSuccess()){
                                listener.onSuccess(requestId, baseResponseVo.rows);
                            }else {
                              /*  String decode = URLDecoder.decode(baseResponseVo.getMessage().message, "UTF-8");
                                LogUtil.getInstance().d(decode);
                                Util.showToast(baseResponseVo.getMessage().message);*/
                              listener.onException(requestId,baseResponseVo.message.code);
                            }
                        }catch (Exception e){
                            Util.showToast("网络异常,请稍候再试");
                        }
                            mContext.hideInfoProgressDialog();

                    }
                });
            //activity 或者fragment销毁时 必须销毁所有的请求 不然容易导致空指针
            mContext.mCompositeSubscription.add(subscribe);
        return subscribe;


    }


}
