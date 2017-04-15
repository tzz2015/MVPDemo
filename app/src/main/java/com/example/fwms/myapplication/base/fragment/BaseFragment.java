package com.example.fwms.myapplication.base.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.commonlibary.pickerview.util.LogUtil;
import com.example.fwms.myapplication.base.activtiy.BaseActivity;
import com.example.fwms.myapplication.base.http.HttpTask;
import com.example.fwms.myapplication.base.prensenter.BasePresenter;
import com.example.fwms.myapplication.utils.TUtil;
import com.example.fwms.myapplication.utils.Util;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import rx.subscriptions.CompositeSubscription;


/**
 * 刘宇飞 创建 on 2017/3/2.
 * 描述：fragment 基类
 */

public abstract class BaseFragment<T extends BasePresenter> extends Fragment {
	private KProgressHUD mProgressDialog;
	private static final int REQUEST_CODE_PERMISSON = 2020; //权限请求码
	public Context context;
	public T mPresenter;
	public HttpTask httpTask;
	/**
	 * 使用CompositeSubscription来持有所有的Subscriptions
	 */
	public CompositeSubscription mCompositeSubscription;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.getInstance().e(this.getClass().toString());

	}

	@Override
	public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
		View view=inflater.inflate(getLayoutViewId(),null);
		ButterKnife.bind(this,view);
		registerEvenbus();
		context=getContext();
		mCompositeSubscription = new CompositeSubscription();
		mPresenter = TUtil.getT(this, 0);
		if(mPresenter!=null){
			if(getActivity() instanceof BaseActivity){
				mPresenter.mContext=(BaseActivity) this.getActivity();
				mPresenter.httpTask=httpTask;
			}

		}
		initPresenter();
		initView();
		return view;
	}
	private void registerEvenbus() {
		if(!EventBus.getDefault().isRegistered(this.getClass().toString())){
			EventBus.getDefault().register(this.getClass().toString());
		}
	}

	protected abstract void initPresenter();

	protected abstract void initView();

	protected abstract int getLayoutViewId();


	/**
	 * 检查所有权限，无权限则开始申请相关权限
	 */
	protected void checkAllNeedPermissions(String[] permissions) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
		List<String> needRequestPermissonList = getDeniedPermissions(permissions);
		if (null != needRequestPermissonList && needRequestPermissonList.size() > 0) {
			ActivityCompat.requestPermissions(getActivity(), needRequestPermissonList.toArray(
					new String[needRequestPermissonList.size()]), REQUEST_CODE_PERMISSON);
		}
	}
	/**
	 * 获取权限集中需要申请权限的列表
	 *
	 * @param permissions
	 * @return
	 */
	private List<String> getDeniedPermissions(String[] permissions) {
		List<String> needRequestPermissonList = new ArrayList<>();
		for (String permission : permissions) {
			if (ContextCompat.checkSelfPermission(getContext(), permission) !=
					PackageManager.PERMISSION_GRANTED ||
					ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
				needRequestPermissonList.add(permission);
			}
		}
		return needRequestPermissonList;
	}

	/**
	 * 所有权限是否都已授权
	 *
	 * @return
	 */
	protected boolean isGrantedAllPermission(String[] permissions) {
		List<String> needRequestPermissonList = getDeniedPermissions(permissions);
		return needRequestPermissonList.size() == 0;
	}

	/**
	 * 权限授权结果回调
	 *
	 * @param requestCode
	 * @param permissions
	 * @param paramArrayOfInt
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] paramArrayOfInt) {
		if (requestCode == REQUEST_CODE_PERMISSON) {
			if (!verifyPermissions(paramArrayOfInt)) {
				permissionGranted(false);
				showTipsDialog();

			} else permissionGranted(true);
		}
	}

	/**
	 * 是否授权成功
	 * @param isSuccess
	 */
	protected  void permissionGranted(boolean isSuccess){

	};

	/**
	 * 显示提示对话框
	 */
	protected void showTipsDialog() {
		new AlertDialog.Builder(getContext()).setTitle("提示信息").setMessage("当前应用缺少" + getDialogTipsPart()
				+ "权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startAppSettings();
					}
				}).show();
	}
	/**
	 * 启动当前应用设置页面
	 */
	private void startAppSettings() {
		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.parse("package:" +getContext(). getPackageName()));
		startActivity(intent);
	}

	/**
	 * 获取弹框提示部分内容
	 *
	 * @return
	 */
	protected String getDialogTipsPart() {
		return "必要";
	}



	/**
	 * 检测所有的权限是否都已授权
	 *
	 * @param grantResults
	 * @return
	 */
	private boolean verifyPermissions(int[] grantResults) {
		for (int grantResult : grantResults) {
			if (grantResult != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
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
			mProgressDialog = new KProgressHUD(context);
		}
		if (str.length == 0) {
			mProgressDialog.setLabel("加载中...");
		} else {
			mProgressDialog.setLabel(str[0]);
		}

		if (!mProgressDialog.isShowing()) {
			mProgressDialog.show();
		}
	}

	/**
	 * 显示提示信息
	 *
	 * @param res
	 *            资源id
	 */
	public final void showToast(final int res) {
//		Toast toast = Toast.makeText(this, res, Toast.LENGTH_LONG);
//		toast.setGravity(Gravity.CENTER, 0, 0);
//		toast.show();
		Util.showToast(getContext(), res);
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
		Util.showToast(title);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		hideInfoProgressDialog();
		EventBus.getDefault().unregister(this);
		if (mPresenter != null)
			mPresenter.onDestroy();
	}



	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(String info) {

	};
}
