package com.example.commonlibary.pickerview.takephoto.Model;

import android.content.Intent;

import com.example.commonlibary.pickerview.takephoto.view.PhotoDialog;

import java.io.Serializable;
import java.util.ArrayList;



public class PhotoModel implements Serializable {

	public PhotoDialog.OnHanlderResultCallback callback;  //回调函数

	public  Intent intentCurrent; //裁剪

	public  int maxSize = 0; //上传个数0表示单个

	public ArrayList<String> stringList = new ArrayList<>(); //数据源

	public int type = 0;//裁剪类型
}
