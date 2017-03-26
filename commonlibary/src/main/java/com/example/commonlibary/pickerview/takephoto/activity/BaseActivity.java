package com.example.commonlibary.pickerview.takephoto.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.example.commonlibary.R;
import com.example.commonlibary.pickerview.takephoto.util.StatusBarCompat;
import com.example.commonlibary.pickerview.takephoto.util.Util;


/**
 * 刘宇飞 创建 on 2017/3/7.
 * 描述：
 */

public class BaseActivity extends FragmentActivity {
    private ProgressDialog mProgressDialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        StatusBarCompat.compat(this, getResources().getColor(R.color.color_00ABF1));

    }

    /**
     * 隐藏等待条
     */
    public final void hideInfoProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public final void showInfoProgressDialog(final String... str) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        if (str.length == 0) {
            mProgressDialog.setMessage("加载中...");
        } else {
            mProgressDialog.setMessage(str[0]);
        }
//        mProgressDialog.setCanceledOnTouchOutside(false);
//        mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//
//            @Override
//            public boolean onKey(DialogInterface dialog,
//                                 int keyCode, KeyEvent event) {
//                if (KeyEvent.KEYCODE_BACK == keyCode) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }



    /**
     * 显示提示信息
     *
     * @param title
     *            显示字符串
     */
    public final void showToast(final String title) {
//		Toast toast = Toast.makeText(this, title, Toast.LENGTH_LONG);
//		toast.setGravity(Gravity.CENTER, 0, 0);
//		toast.setDuration(Toast.LENGTH_SHORT);
//		toast.show();
        Util.showToast(this, title);
    }
}
