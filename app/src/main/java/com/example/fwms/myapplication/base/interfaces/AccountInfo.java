package com.example.fwms.myapplication.base.interfaces;

/**
 * 公司：杭州融科网络科技
 * 刘宇飞 创建 on 2017/3/15.
 * 描述：用户信息
 */

public class AccountInfo {
    /**
     * 登录的类型  type==1业主，type==2服务商
     */
    public static int type;

    /**
     * 名称
     */
    public static String name;
    /**
     * 用户id
     */

    public static String userId;

    /**
     * 用户头像
     */

    public static String avatar;
    /**
     * 员工id
     */
    public static String employeeId;
    /**
     * 地区
     */
    public static String region;
    /**
     * 地址
     */
    public static String address;
    /**
     * 技术资质id
     */
    public static String technicalQualif;
    /**
     * 当前公司code  废弃 统一使用 AccountInfo.CompanyInfo.companyCode
     */
    public static String code;
    /**
     * 当前公司名称
     */
    public static String companyName;
    /**
     * 身份证正面
     */
    public static String idCardPositive;
    /**
     * 身份证反面
     */
    public static String idCardNegative;
    /**
     * 邮箱
     */
    public static String email;
    /**
     * 手机号码
     */
    public static String mobilePhone;
    /**
     * 部门id
     */
    public static String departmentId;
    /**
     * 部门名称
     */
    public static String departmentName;
    /**
     * 技术专长
     */
    public static String professionalSkills;
    /**
     * 当前业主版本
     */
    public static String versionCode;
    /**
     * 当前业主公司Code
     */
    public static String companyCode;
    /**
     * 用户账号
     */
    public static String userName;
    /**
     * 用户密码
     */
    public static String password;
    /**
     * 公司是否认证
     */
    public static String checkFlag;
    /**
     * 公司信息
     */
    public static CompanyInfo companyInfo;

    /**
     * 企业资质是否上传有
     */
    public static String isHavaAppAffixInfoList;
    /**
     * 清空用户信息
     */
    public static void clearInfo() {
        type = 0;
        userId = null;
        name = null;
        avatar = null;
        employeeId = null;
        region = null;
        address = null;
        technicalQualif = null;
        code = null;
        idCardPositive = null;
        idCardNegative = null;
        email = null;
        mobilePhone = null;
        departmentId = null;
        departmentName = null;
        professionalSkills = null;
        versionCode = null;
        companyCode = null;
        userName = null;
        password = null;
        companyName=null;
        checkFlag=null;
        companyName=null;
        isHavaAppAffixInfoList=null;
    }

    public static class CompanyInfo {

        public static int beginNo;
        public static  String bufferingPeriodDate;
        public  static String businessScope;
        public  static String certificateNumber;
        public  static String certificateType;
        public  static String checkFlag;
        public  static String checkSay;
        public  static String cityId;
        public static  String cityName;
        public  static String companyCode;
        public  static int companyId;
        public static  String companyName;
        public static  String companyType;
        public  static String complainPhone;
        public  static String countryId;
        public  static String countryName;
        public  static String customerPhone;
        public  static String effectiveAge;
        public  static String effectiveDate;
        public  static int endNo;
        public  static String expirationDate;
        public  static String failureDate;
        public  static String invitationCode;
        public  static int latitude;
        public  static String lawer;
        public  static int longitude;
        public static  String mail;
        public static  String provinceId;
        public static  String provinceName;
        public static  String regionId;
        public  static String regionName;
        public  static String registerAddr;
        public  static String registerCode;
        public  static String registerDate;
        public  static int registerMoney;
        public  static String registerName;
        public  static String registerPhone;
        public static  int rowNo;
        public  static String trialStatus;
        public  static String type;
        public static String valibleEnd;
        public static String valibleStart;
        public static String versionCode;
        public static String versionName;

              
    }
}
