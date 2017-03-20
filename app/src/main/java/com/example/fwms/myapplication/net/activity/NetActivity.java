package com.example.fwms.myapplication.net.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fwms.myapplication.R;
import com.example.fwms.myapplication.base.activtiy.BaseActivity;
import com.example.fwms.myapplication.net.contract.NetActivityContract;
import com.example.fwms.myapplication.net.presenter.NetActivityPresenter;
import com.example.fwms.myapplication.utils.Util;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * MVP 写法
 */
public class NetActivity extends BaseActivity<NetActivityPresenter> implements NetActivityContract.View {


    @Bind(R.id.bt_left)
    ImageView btLeft;
    @Bind(R.id.ll_left)
    LinearLayout llLeft;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.ll_right)
    LinearLayout llRight;
    @Bind(R.id.activity_net)
    LinearLayout activityNet;
    @Bind(R.id.tv_show)
    TextView tvShow;


    @Override
    protected void initPresenter() {
      mPresenter.setView(this);
    }

    @Override
    protected void initView() {
        tvTitle.setText("网络示例");
    }

    @OnClick({R.id.ll_left, R.id.bt_1, R.id.bt_2, R.id.bt_3, R.id.bt_4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_left:
                finish();
                break;
            case R.id.bt_1:
                mPresenter.requestDemo1();
                break;
            case R.id.bt_2:
                mPresenter.requestDemo2();
                break;
            case R.id.bt_3:
                mPresenter.requestDemo3();
                break;
            case R.id.bt_4:
                mPresenter.requestDemo4();
                break;
        }
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.activity_net;
    }


    @Override
    public void setView(String s) {
        if(Util.isNotEmpty(s)){
            tvShow.setText(s);
        }
    }


}
