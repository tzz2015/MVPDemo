package com.example.fwms.myapplication.net.modle;

/**
 * 公司：融科网络
 * 刘宇飞创建 on 2017/3/20.
 * 描述： 企业信息
 */

public class CompanyInfoModle {
   /* companyCode公司编码、companyName公司名称、registerAddr注册地址、
    checkFlag认证状态（ 0：未认证，1：待认证，2已认证，3未通过认证）
            、provinceId省、cityId市、regionId区、
    companyType公司类型（1业主2服务商）registerName公司注册人*/

    public String companyCode;
    public String companyName;
    public String registerAddr;
    public String checkFlag;
    public String provinceId;
    public String cityId;
    public String regionId;
    public String companyType;
    public String registerName;

    @Override
    public String toString() {
        return "CompanyInfoModle{" +
                "companyCode='" + companyCode + '\'' +
                ", companyName='" + companyName + '\'' +
                ", registerAddr='" + registerAddr + '\'' +
                ", checkFlag='" + checkFlag + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", cityId='" + cityId + '\'' +
                ", regionId='" + regionId + '\'' +
                ", companyType='" + companyType + '\'' +
                ", registerName='" + registerName + '\'' +
                '}';
    }
}
