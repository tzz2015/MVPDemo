package com.example.fwms.myapplication.net.modle;

/**
 * Created by User on 2017/2/24.
 */


import com.example.fwms.myapplication.base.model.BaseRecyclerModel;

/**
 * 个人信息，业主绑定的信息对象
 */
public class BangDingInfoModel extends BaseRecyclerModel {
   // companyCode公司编码、companyName公司名称、relationId是否绑定（为空未绑定，不为空已绑定
    public String companyCode;
    public String companyName;
    public String relationId;


}
