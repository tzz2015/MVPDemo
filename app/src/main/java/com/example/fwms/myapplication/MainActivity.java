package com.example.fwms.myapplication;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fwms.myapplication.base.activtiy.BaseActivity;
import com.example.fwms.myapplication.net.activity.NetActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 非MVP写法
 */
public class MainActivity extends BaseActivity {

    @Bind(R.id.bt_net)
    Button btNet;

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView() {
        //检查权限
        String[] pmis= new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        boolean b = isGrantedAllPermission(pmis);
        if(!b){
            checkAllNeedPermissions(pmis);
        }
    }

    @OnClick({R.id.bt_net})
    public void onClick(View view){
        Intent intent=null;
        switch (view.getId()){
            case R.id.bt_net://网络请求例子
                intent=new Intent(this, NetActivity.class);
                break;
        }
        startActivity(intent);
    }
    @Override
    protected int getLayoutViewId() {
        return R.layout.activity_main;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
