package com.example.fwms.myapplication.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.commonlibary.pickerview.takephoto.Model.PhotoModel;
import com.example.commonlibary.pickerview.takephoto.util.FileUtils;
import com.example.commonlibary.pickerview.takephoto.util.SelectPhotoBack;
import com.example.commonlibary.pickerview.takephoto.view.PhotoDialog;
import com.example.fwms.myapplication.App;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.fwms.myapplication.App.getHandler;


/**
 * 公司：杭州融科网络科技
 * 刘宇飞 创建 on 2017/3/2.
 * 描述：工具类
 */

public class Util {

    private static Intent intent;
    private static Map<String, String> map;

    /**
     * 获取全局上下文
     *
     * @return
     */
    public static Context getContext() {
        return App.getContext();
    }

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

    public static int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取客户端版本
     *
     * @return 客户端版本
     */
    public static String getVersion() {
        String version = "";
        try {
            // 获取PackageManager的实例
            PackageManager packageManager = App.getInstance()
                    .getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    App.getInstance().getPackageName(), 0);
            version = packInfo.versionName;
        } catch (Exception ex) {
        }
        return version;
    }


    /**
     * 去除非数字
     *
     * @param time
     * @return
     */
    public static String checkNumber(String time) {
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(time);
        return matcher.replaceAll("");
    }

    /**
     * 校验汉字
     */
    public static boolean checkChinese(String str) {
        String regEx = "[\\u4e00-\\u9fa5]*$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 判断是否是手机号码
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isMobile(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("1[0-9]{10}");
    }

    /**
     * 判断字符串是否为空
     *
     * @param value
     * @return
     */
    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    protected static Toast toast = null;

    public static void showToast(Context context, int res) {
        if (toast == null) {
            toast = Toast.makeText(context, res, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            toast.setText(context.getString(res));
            toast.show();
        }
    }


    public static void showToast(Context context, String title) {

        if (toast == null) {
            toast = Toast.makeText(context, title, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            toast.setText(title);
            toast.show();
        }
    }

    public static void showToast(String title) {

        if (toast == null) {
            toast = Toast.makeText(getContext(), title, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            toast.setText(title);
            toast.show();
        }
    }


    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo in = connectivity.getActiveNetworkInfo();
            if (in != null && in.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 直接拨打电话
     *
     * @param context 参数
     * @param phone   电话号码
     */
    public static void directTelephone(Context context, String phone) {
        try {
            Uri uri = Uri.parse("tel:" + phone);
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }

    /**
     * 获取屏幕的宽
     */
    public static int getWidth(Context ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }

    /**
     * 获取屏幕的宽
     */
    public static int getHeight(Context ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        return screenHeight;
    }

    /**
     * 显示输入法键盘
     *
     * @param context
     * @param view
     */
    public static void showSoftInputFromWindow(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    /**
     * 自定义dialog全屏展示
     *
     * @param activity
     * @param dialog
     */
    public static void FullScreen(Activity activity, Dialog dialog) {
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
        //p.height = (int) (d.getHeight() * 0.3);   //高度设置为屏幕的0.3
        p.width = (int) (d.getWidth());    //宽度设置为全屏
        dialog.getWindow().setAttributes(p);     //设置生效
    }

    /**
     * 自定义dialog全屏展示(宽高全屏)
     *
     * @param activity
     * @param dialog
     */
    public static void FullAllScreen(Activity activity, Dialog dialog) {
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
        //p.height = (int) (d.getHeight() * 0.3);   //高度设置为屏幕的0.3
        p.width = (int) (d.getWidth());    //宽度设置为全屏
        p.height = (int) (d.getHeight());
        dialog.getWindow().setAttributes(p);     //设置生效
    }


    /**
     * 根据日期获取星期
     *
     * @param timeStr
     * @return
     */
    public static String getWeek(String timeStr) {
        String Week = "星期";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//也可将此值当参数传进来
            SimpleDateFormat parseformat = new SimpleDateFormat("yyyy-MM-dd");//也可将此值当参数传进来
            Date curDate = format.parse(timeStr);
            String pTime = parseformat.format(curDate);
            Calendar c = Calendar.getInstance();
            c.setTime(parseformat.parse(pTime));
            switch (c.get(Calendar.DAY_OF_WEEK)) {
                case 1:
                    Week += "天";
                    break;
                case 2:
                    Week += "一";
                    break;
                case 3:
                    Week += "二";
                    break;
                case 4:
                    Week += "三";
                    break;
                case 5:
                    Week += "四";
                    break;
                case 6:
                    Week += "五";
                    break;
                case 7:
                    Week += "六";
                    break;
                default:
                    break;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Week;
    }

    /**
     * 设置文本
     *
     * @param value
     * @return
     */
    public static void setTextValue(TextView textView, String value, String... defValue) {
        if (isNotEmpty(value))
            textView.setText(value);
        else {
            if (defValue!=null&&defValue.length==1) {
                textView.setText(defValue[0]);
            } else {
                textView.setText("");
            }

        }


    }

    /**
     * @param runnable 将任务保证在主线程中运行的方法
     */
    public static void runInMainThread(Runnable runnable) {
        //获取调用此方法所在的线程
        if (android.os.Process.myTid() == getMainThreadId()) {
            //如果上诉的runnable就是在主线程中要去执行的任务,则直接运行即可
            runnable.run();
        } else {
            //如果上诉的runnable运行在子线程中,将其传递到主线程中去做执行
            getHandler().post(runnable);
        }
    }

    /**
     * @return 返回主线程id方法
     */
    public static int getMainThreadId() {
        return App.getMainThreadId();
    }

    public static boolean isEmail(String strEmail) {
        String strPattern = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 字符串是否为空
     *
     * @param input
     * @return
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 启动activity
     *
     * @param t
     * @param map
     */
    public static void startActivity(Class<?> t, Map<String, String> map) {
        intent = new Intent(getContext(), t);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (map != null) {
            for (String key : map.keySet()) {
                intent.putExtra(key, map.get(key));
            }
        }
        getContext().startActivity(intent);
    }

    /**
     * 获取颜色
     *
     * @param context
     * @param id
     * @return
     */
    public static int getColor(Context context, int id) {
        return context.getResources().getColor(id);
    }

    /**
     * 隐藏状态栏
     *
     * @param context
     */
    public static void hideStatusLan(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * x选择单张照片
     *
     * @param context
     * @param photoBack
     */
    public static void selectSinglePhoto(Context context, final SelectPhotoBack photoBack) {
        PhotoModel photoModel = new PhotoModel();
        photoModel.maxSize = 0;
        photoModel.callback = new PhotoDialog.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, final List<String> resultList, int degree) {
                if (resultList != null && !resultList.isEmpty()) {
                    String name = System.currentTimeMillis() + "_";
                    //压缩处理
                    String filePath = FileUtils.saveFileByPath(name, resultList.get(0));
                    photoBack.selectPath(filePath);
                }

            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        };
        PhotoDialog.imageSelect(context, photoModel);
    }
    /**
     * 选择单张照片并裁剪
     */
    public static void selectSinglePhotoCrop(Context context, final SelectPhotoBack photoBack){
        PhotoModel photoModel = new PhotoModel();
        //裁剪属性
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        photoModel.intentCurrent = intent;
        photoModel.maxSize = 0;
        photoModel.type = 1;
        photoModel.callback = new PhotoDialog.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<String> resultList, int degree) {
                if (resultList != null && resultList.size() > 0) {
                    String name = System.currentTimeMillis() + "xx";
                    String filePath = FileUtils.saveFileByPath(name, resultList.get(0));
                    photoBack.selectPath(filePath);
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                showToast("裁剪失败");
            }
        };
        PhotoDialog.imageSelect(context, photoModel);
    }


    /**
     * 获取map
     *
     * @return
     */
    public static Map<String, String> getMap() {
        if (map == null) {
            map = new HashMap<>();
        }
        map.clear();
        return map;
    }



/*
    *//**
     * 设置标签
     * @param list
     * @param flowLayout
     *//*
    public static void setMark(List<String> list, final FlowLayout flowLayout){
        final Map<Integer, Boolean> map = new HashMap<>();
        final int size = list.size();
//        final Boolean[] isCheck = {map.get(i)};
        for (int i = 0; i < list.size(); i++) {
            map.put(i, false);
            final View view = View.inflate(Util.getContext(), R.layout.publish_mark_textview,null);
            final TextView textView= (TextView) view.findViewById(R.id.tv_mark);
            textView.setText(list.get(i));
            textView.setBackgroundResource(R.drawable.grey_side_bg);
            textView.setTextColor(Util.getContext().getResources().getColor(R.color.color_666666));
            final int position = i;
            //点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Util.showToast(textView.getText().toString());
                    if (map.get(position)) {//取消选中
                        map.put(position, false);
                        textView.setBackgroundResource(R.drawable.grey_side_bg);
                        textView.setTextColor(Util.getContext().getResources().getColor(R.color.color_666666));
                    } else {//选中
                        map.put(position, true);
                        textView.setBackgroundResource(R.drawable.yellow_side_bg);
                        textView.setTextColor(Util.getContext().getResources().getColor(R.color.color_ffc806));
                    }
                }
            });
            flowLayout.addView(view);
        }
    }*/

    /**
     * 传入map，转换成符合格式的url请求
     *
     * @param map
     * @return
     */
    public static String Map2String(Map<String, String> map) {
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
//            String s1="\""+key+"\""+":"+value+",";
            String s1 = key + ":'" + value + "',";
            sb.append(s1);
        }
        String s = sb.toString();
        String s1 = s.substring(0, s.length() - 1);
        String result = s1 + "}";
        return result;
    }





}
