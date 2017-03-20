package com.example.commonlibary.pickerview.takephoto;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.example.commonlibary.R;
import com.example.commonlibary.pickerview.takephoto.activity.BaseActivity;
import com.example.commonlibary.pickerview.takephoto.util.FileUtils;
import com.example.commonlibary.pickerview.takephoto.util.Util;
import com.example.commonlibary.pickerview.takephoto.view.PhotoDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CameraActivity extends BaseActivity {


    public static final int CAMERA = 111;

    /**
     * 选择本地照片返回标识
     */
    public static final int IMAGE_REQUEST_CODE = 200;

    /**
     * 选择本地照片返回标识
     */
    public static final int IMAGE_RESULT_CODE = 200;

    /**
     * 选择拍照返回标识
     */
    public static final int CAMERA_REQUEST_CODE = 201;

    /**
     * 裁剪图片返回标识
     */
    public static final int RESULT_REQUEST_CODE = 202;

    /**
     * 取消命令
     */
    public static final int RSEULT_FINISH_CODE = 0;


    private String imageLocationPath;

    private String imageCutPath;

    private int clickEvent = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            PhotoDialog.mCallback.onHanlderSuccess(CAMERA_REQUEST_CODE, fileList, bitmapDegree);
            hideInfoProgressDialog();
        }
    };
    private List<String> fileList;
    private int bitmapDegree;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        findViewById(R.id.ll_ps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEvent = 0;
                checkPermission();
            }
        });
        findViewById(R.id.ll_choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    clickEvent = 1;
                    Intent intent = new Intent(getBaseContext(), ChoosePhotoGridActivity.class);
                    intent.putExtra("list", new ArrayList<String>());
                    startActivityForResult(intent, IMAGE_REQUEST_CODE);


            }
        });
        findViewById(R.id.ll_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.llRoot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    public void startActionCamera() {
        String savePath = "";
        // 判断是否挂载了SD卡
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/";// 存放照片的文件夹
            File savedir = new File(savePath);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        }
        // 没有挂载SD卡，无法保存文件
        if (Util.isEmpty(savePath)) {
            Util.showToast(this, "无法保存照片，请检查SD卡是否挂载");
            return;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = "thq_" + timeStamp + ".jpg";// 照片命名
        File out = new File(savePath, fileName);
        Uri uri = Uri.fromFile(out);
        imageLocationPath = savePath + fileName;// 该照片的绝对路径
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     * 判断地图权限
     */
    protected void checkPermission() {
        //版本判断
        if (Build.VERSION.SDK_INT >= 23) {
            //减少是否拥有权限
            int checkPermissionResult = checkSelfPermission(Manifest.permission.CAMERA);
            if (checkPermissionResult != PackageManager.PERMISSION_GRANTED) {
                //弹出对话框接收权限
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA);
            } else {
                //获取到权限
                startActionCamera();
            }
        } else {
            startActionCamera();
            //获取到权限
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActionCamera();
                } else {
                    //没有获取到权限
                    showToast("无摄像头权限");
                    finish();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                switch (requestCode) {
                    case IMAGE_REQUEST_CODE://多张
                        List<String> list = data.getStringArrayListExtra("list");
                        if (list != null) {
                            for (String s : list) {
                                bitmapDegree = FileUtils.getBitmapDegree(s);
                                if (bitmapDegree > 0) {
                                    Bitmap picture = BitmapFactory.decodeFile(s);
                                    Bitmap resizePicture = rotatePicture(picture, bitmapDegree);
                                    saveBmpToPath(resizePicture, s);
                                }

                            }
                            PhotoDialog.mCallback.onHanlderSuccess(IMAGE_RESULT_CODE, list, 0);
                        }
                        break;
                    case CAMERA_REQUEST_CODE:
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + imageLocationPath)));
                        if (PhotoDialog.intentCurrent == null) {
                            fileList = new ArrayList<>();
                            fileList.add(imageLocationPath);
                            bitmapDegree = FileUtils.getBitmapDegree(imageLocationPath);
                            if (bitmapDegree > 0) {// 三星手机旋转 耗时操作 出现黑屏 后期处理
                              /*  Util.showToast("正在处理图片,请稍后...");
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        FileUtils.setRotateImg(imageLocationPath, bitmapDegree);
                                        handler.sendEmptyMessage(0);
                                    }
                                }).start();*/
                                Bitmap picture = BitmapFactory.decodeFile(imageLocationPath);
                                Bitmap resizePicture = rotatePicture(picture, bitmapDegree);
                                saveBmpToPath(resizePicture, imageLocationPath);


                            }
                            PhotoDialog.mCallback.onHanlderSuccess(CAMERA_REQUEST_CODE, fileList, bitmapDegree);


                        } else {
                            File tempFile = new File(imageLocationPath);
                            Intent intent = PhotoDialog.intentCurrent;
                            intent.setDataAndType(Uri.fromFile(tempFile), "image/*");
                            imageCutPath = FileUtils.createFileCut(tempFile.getName());
                            File newFile = new File(imageCutPath);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
                            startActivityForResult(intent, RESULT_REQUEST_CODE);
                            return;
                        }
                        break;
                    case RESULT_REQUEST_CODE:
                        if (data != null) {
                            //Bundle extras = data.getExtras();
                            try {
//                                Bitmap photo =  decodeUriAsBitmap(Uri.parse("file://" + imageCutPath));
                                Bitmap photo = decodeUriAsBitmap(Uri.fromFile(new File(imageCutPath)));
                                if (photo != null) {//略缩图没有不可剪切
                                    switch (PhotoDialog.type) {
                                        case 1:
                                            photo = Util.toRoundBitmap(photo);
                                            break;
                                    }
                                    FileUtils.saveFileByBitMap(photo, imageCutPath);
                                    if (!photo.isRecycled()) {
                                        photo.recycle();
                                    }
                                }
                                List<String> fileList = new ArrayList<>();
                                fileList.add(imageCutPath);
                                if (clickEvent == 1) {
                                    PhotoDialog.mCallback.onHanlderSuccess(IMAGE_RESULT_CODE, fileList, 0);
                                } else { //拍照
                                    PhotoDialog.mCallback.onHanlderSuccess(CAMERA_REQUEST_CODE, fileList, 0);
                                }
                                //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + imageCutPath)));
                            } catch (Exception e) {
                                Log.e("本地相册返回异常", e.getMessage());
                            }
                        }
                        break;
                    default:
                        break;
                }
                break;
            case IMAGE_RESULT_CODE: //单张
                if (data != null) {
                    List<String> list = data.getStringArrayListExtra("list");
                    if (list != null && list.size() > 0) {
                        imageLocationPath = list.get(0);
                        if (PhotoDialog.intentCurrent == null) {
                            List<String> fileList = new ArrayList<>();
                            fileList.add(imageLocationPath);
                            bitmapDegree = FileUtils.getBitmapDegree(imageLocationPath);
                            if (bitmapDegree > 0) {
                                Bitmap picture = BitmapFactory.decodeFile(imageLocationPath);
                                Bitmap resizePicture = rotatePicture(picture, bitmapDegree);
                                saveBmpToPath(resizePicture, imageLocationPath);
                            }
                            PhotoDialog.mCallback.onHanlderSuccess(IMAGE_RESULT_CODE, fileList, 0);
                        } else {
                            File tempFile = new File(imageLocationPath);
                            Intent intent = PhotoDialog.intentCurrent;
                            intent.setDataAndType(Uri.fromFile(tempFile), "image/*");
                            imageCutPath = FileUtils.createFileCut(tempFile.getName());
                            File newFile = new File(imageCutPath);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
                            startActivityForResult(intent, RESULT_REQUEST_CODE);
                            return;
                        }
                    }
                }
                break;
            case RSEULT_FINISH_CODE:
                switch (requestCode) {
                    case RESULT_REQUEST_CODE://裁剪的时候取消
                        if (data == null) {
                            if (clickEvent == 1) {
                                findViewById(R.id.ll_choose).performClick();
                            }
                            return;
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        finish();
    }

    public Bitmap rotatePicture(final Bitmap bitmap, final int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBitmap;
    }

    public boolean saveBmpToPath(final Bitmap bitmap, final String filePath) {
        if (bitmap == null || filePath == null) {
            return false;
        }
        boolean result = false; //默认结果
        File file = new File(filePath);
        OutputStream outputStream = null; //文件输出流
        try {
            outputStream = new FileOutputStream(file);
            result = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream); //将图片压缩为JPEG格式写到文件输出流，100是最大的质量程度
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close(); //关闭输出流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    public void onDestroy() {
        FileUtils.deleteMyBusiness(PhotoDialog.pathName);
        hideInfoProgressDialog();
        super.onDestroy();
    }


    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;

    }
}
