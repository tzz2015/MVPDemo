package com.example.fwms.myapplication.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.commonlibary.pickerview.city.ChooseCityListener;
import com.example.commonlibary.pickerview.city.PickUtil;
import com.example.fwms.myapplication.R;
import com.example.fwms.myapplication.base.activtiy.BaseActivity;
import com.example.fwms.myapplication.utils.TimeUtils;

import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

public class SelectActivity extends BaseActivity {


    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.bt_city)
    Button btCity;
    @Bind(R.id.bt_time)
    Button btTime;
    @Bind(R.id.tv_show)
    TextView tvShow;

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView() {

    }
   @OnClick({R.id.bt_city,R.id.bt_time})
   public void onClick(View view){
       switch (view.getId()){
           case R.id.bt_city://城市选择
              showCity();
               break;
           case R.id.bt_time://时间选择
               showTime();
               break;
       }
   }

    private void showTime() {
      PickUtil.showTime(this, new PickUtil.SelectTimeLisener() {
          @Override
          public void timeBack(Date date) {
              tvShow.setText(TimeUtils.getTime(date.getTime(),TimeUtils.DATE_FORMAT_DATE));
          }
      });
    }

    private void showCity() {
        PickUtil.showCityPickView(this, new ChooseCityListener() {
            @Override
            public void chooseCity(String s) {
                tvShow.setText(s);
            }
        });
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.activity_select;
    }

}
