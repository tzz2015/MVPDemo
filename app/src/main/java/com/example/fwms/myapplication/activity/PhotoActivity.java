package com.example.fwms.myapplication.activity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commonlibary.pickerview.takephoto.CameraActivity;
import com.example.commonlibary.pickerview.takephoto.Model.PhotoModel;
import com.example.commonlibary.pickerview.takephoto.activity.ShowPhotoActivity;
import com.example.commonlibary.pickerview.takephoto.util.Bimp;
import com.example.commonlibary.pickerview.takephoto.util.SelectPhotoBack;
import com.example.commonlibary.pickerview.takephoto.view.PhotoDialog;
import com.example.fwms.myapplication.R;
import com.example.fwms.myapplication.adapter.PublicImageAdapter;
import com.example.fwms.myapplication.base.activtiy.BaseActivity;
import com.example.fwms.myapplication.utils.ImageLoaderUtils;
import com.example.fwms.myapplication.utils.Util;
import com.example.fwms.myapplication.view.InScrollGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 杭州融科网络
 * 刘宇飞 创建 on 2017/3/26.
 * 描述：
 */

public class PhotoActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.bt_single_photo)
    Button btSinglePhoto;
    @Bind(R.id.bt_single_grop)
    Button btSingleGrid;
    @Bind(R.id.bt_left)
    ImageView btLeft;
    @Bind(R.id.iv_show)
    ImageView ivShow;
    @Bind(R.id.gridview)
    InScrollGridView gridview;
    private PublicImageAdapter gridViewAdapter;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    gridViewAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    gridViewAdapter.loading();
                    break;

            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView() {
        tvTitle.setText("图片选择");
        initGridView();
    }
    /**
     * 初始化照片选择
     *
     */

    public void initGridView() {
        Bimp.initBimp();
        gridViewAdapter = new PublicImageAdapter(context, handler);
        gridview.setAdapter(gridViewAdapter);
        gridview.setOnItemClickListener(this);
    }

    @OnClick({R.id.bt_single_photo, R.id.bt_single_grop, R.id.bt_brower})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_single_photo://单张图片
                Util.selectSinglePhoto(this, new SelectPhotoBack() {
                    @Override
                    public void selectPath(String path) {
                        ImageLoaderUtils.display(getApplicationContext(), ivShow, new File(path));
                    }
                });
                break;
            case R.id.bt_single_grop://单张图片并裁剪
                Util.selectSinglePhotoCrop(getApplicationContext(), new SelectPhotoBack() {
                    @Override
                    public void selectPath(String path) {
                        ImageLoaderUtils.display(getApplicationContext(), ivShow, new File(path));
                    }
                });
                break;
            case R.id.bt_brower:
                List<String> list = new ArrayList<>();
                list.add("http://img1.imgtn.bdimg.com/it/u=66250564,3253305393&fm=23&gp=0.jpg");
                list.add("http://img0.imgtn.bdimg.com/it/u=646265142,702968958&fm=23&gp=0.jpg");
                list.add("http://img4.imgtn.bdimg.com/it/u=787324823,4149955059&fm=23&gp=0.jpg");
                startActivity(ShowPhotoActivity.getIntent(this, 0, list));
                break;
        }
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.activity_photo;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == gridview.getId() && position == Bimp.bmp.size()) {// 点击图片
            showSelectDialog();
        }
    }
    /**
     * 显示照片选择器
     */
    public void showSelectDialog() {
        final PhotoModel photoModel = new PhotoModel();
        photoModel.stringList.addAll(Bimp.drr);
        photoModel.maxSize = 9;
        photoModel.callback = new PhotoDialog.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<String> resultList, final int degree) {
                if (resultList != null && !resultList.isEmpty()) {
                    final String path = resultList.get(0);
                    if (CameraActivity.CAMERA_REQUEST_CODE == reqeustCode) {//拍照
                        Bimp.drr.add(path);
                    }  else {//多选
                        Bimp.drr.clear();
                        Bimp.drr.addAll(resultList);
                    }
                    gridViewAdapter.loading();

                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
            }
        };
        PhotoDialog.imageSelect(this, photoModel);
    }

}
