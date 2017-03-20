package com.example.commonlibary.pickerview.takephoto.view;

import android.content.Context;
import android.content.Intent;

import com.example.commonlibary.pickerview.takephoto.CameraActivity;
import com.example.commonlibary.pickerview.takephoto.Model.PhotoModel;
import com.example.commonlibary.pickerview.takephoto.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

;

/**
 * 照片选择类
 */
public class PhotoDialog {

    public static String pathName = "image_view_";

    public static OnHanlderResultCallback mCallback;

    public static Intent intentCurrent;

    public static int maxSize = 0;

    public static BottomSelectDialog dialog;

    public static List<String> list = new ArrayList<>();

    /**裁剪类型 1-圆形*/
    public static int type = 0;


    public static void imageSelect(Context context, PhotoModel photoModel){
        mCallback = photoModel.callback;
        intentCurrent = photoModel.intentCurrent;
        maxSize = photoModel.maxSize;
        type = photoModel.type;
        list.clear();
        list.addAll(photoModel.stringList);
        context.startActivity(new Intent(context, CameraActivity.class));
    }
    /**
     * 处理结果
     */
    public static interface OnHanlderResultCallback {
        /**
         * 处理成功
         * @param reqeustCode
         * @param resultList
         * @param  degree 旋转角度
         */
        public void onHanlderSuccess(int reqeustCode, List<String> resultList, int degree);

        /**
         * 处理失败或异常
         * @param requestCode
         * @param errorMsg
         */
        public void onHanlderFailure(int requestCode, String errorMsg);
    }

    /**
     * 清除临时文件夹中数据
     */
    public static void clearAll(){
        FileUtils.deleteDirShq();
    }
}
