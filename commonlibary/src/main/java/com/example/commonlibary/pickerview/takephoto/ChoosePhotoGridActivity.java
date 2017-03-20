package com.example.commonlibary.pickerview.takephoto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.example.commonlibary.R;
import com.example.commonlibary.pickerview.takephoto.Model.ImageBucket;
import com.example.commonlibary.pickerview.takephoto.Model.PhotoItem;
import com.example.commonlibary.pickerview.takephoto.activity.BaseActivity;
import com.example.commonlibary.pickerview.takephoto.activity.PhotoViewActivity;
import com.example.commonlibary.pickerview.takephoto.adapter.PhotoGridAdapter;
import com.example.commonlibary.pickerview.takephoto.util.AlbumPhotoHelper;
import com.example.commonlibary.pickerview.takephoto.util.NativeImageLoader;
import com.example.commonlibary.pickerview.takephoto.util.StatusBarCompat;
import com.example.commonlibary.pickerview.takephoto.util.Util;
import com.example.commonlibary.pickerview.takephoto.view.PhotoDialog;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 * 照片选择类
 */
public class ChoosePhotoGridActivity extends BaseActivity {


    private List<PhotoItem> dataList;

    private PhotoGridAdapter adapter;

    private AlbumPhotoHelper helper;

    private PopupWindow mPopupWindow;

    private List<PhotoItem> PhotoItemList;

    private List<String> bucketNameList;

    HashMap<String, ImageBucket> bucketList;

    private int totalImageCount;

    private String theLarge;
    
    int size = 0;

    private List<String> drrs = new ArrayList<>();


    private GridView gridview1;
    private GridView gridview;
    private Button btnDone;
    private Button scan;
    private TextView allImages;
    private ImageView iv_down_img;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        StatusBarCompat.compat(this, getResources().getColor(R.color.color_00ABF1));
        setContentView(R.layout.shq_activity_choose_imgs);

        for(String str: PhotoDialog.list){
            File file = new File(str);
            if(file.exists()){
                drrs.add(str);
            }
        }
        initView();

        helper = AlbumPhotoHelper.getHelper();
        helper.init(getApplicationContext());
        helper.buildImagesBucketList(drrs);

        PhotoItemList = helper.getImagesItemList();
        bucketNameList = helper.getBucketNameList();
        bucketList = helper.getBucketList();
        dataList = new ArrayList<PhotoItem>();
        dataList.clear();
        for (int i = 0; i < PhotoItemList.size(); ++i) {
            PhotoItem image = PhotoItemList.get(i);
            dataList.add(image);
        }
        totalImageCount = dataList.size();
        adapter = new PhotoGridAdapter(this, dataList, mHandler);
        gridview.setAdapter(adapter);
        adapter.setTextCallback(new PhotoGridAdapter.TextCallback() {
            public void onListen(int count) {
                changeChooseCount(count);
            }
        });
        adapter.selectList.clear();
        if(drrs != null){
            adapter.selectList.addAll(drrs);
        }

        changeChooseCount(adapter.selectList.size());
        size = Util.dip2px(this, 70);
    }

    private void initView() {
        gridview = (GridView) findViewById(R.id.gridview);
        iv_down_img=(ImageView)findViewById(R.id.iv_down_img);
        btnDone = (Button) findViewById(R.id.bt_done);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIntent().putStringArrayListExtra("list",adapter.selectList);
                setResult(RESULT_OK,getIntent());
                finish();
            }
        });

        scan = (Button) findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.selectList.size() == 0) {
                    showToast("请选择图片");
                    return;
                }
                Intent    intent = new Intent(ChoosePhotoGridActivity.this, PhotoViewActivity.class);
                intent.putExtra("list", (Serializable) adapter.selectList);
                startActivityForResult(intent, 101);
            }
        });

        allImages = (TextView) findViewById(R.id.all_imgs);
        allImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showButketView();
             //   allImages.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.trade_up), null);
                iv_down_img.setImageResource(R.drawable.trade_up);
                findViewById(R.id.gray_bg).setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.btn_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    private void changeChooseCount(int count) {
        if (count == 0) {
            btnDone.setEnabled(false);
            scan.setEnabled(false);
            btnDone.setText("完成");
            scan.setText("预览");
        } else {
            btnDone.setEnabled(true);
            scan.setEnabled(true);
            btnDone.setText("(" + count + ")" + "完成");
            // scan.setText("预览" + "(" + count + ")");
            scan.setText("预览");
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Util.showToast(ChoosePhotoGridActivity.this, "最多选择"+ PhotoDialog.maxSize+"张图片");
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 相册列表adapter
     */
    private class BucketAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return bucketNameList != null ? (bucketNameList.size() + 1) : 1;
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            LayoutInflater inflater = LayoutInflater.from(ChoosePhotoGridActivity.this);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.shq_bucket_item, null);
                holder.img = (ImageView) convertView.findViewById(R.id.img);
                holder.text = (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (bucketList != null && bucketList.size() > 0) {
                String disPath = "";
                if (position == 0) {
                    String tempText = "<font color='#252525'>" + "全部图片 " + "</font><font color='#B9B9B9'>(" + totalImageCount + ")</font>";
                    holder.text.setText(Html.fromHtml(tempText));
                    disPath = PhotoItemList.get(0).imagePath;
                } else {
                    String bucketName = bucketNameList.get(position - 1);
                    ImageBucket bucket = bucketList.get(bucketName);
                    if (bucket != null) {
                        String tempText2 = "<font color='#252525'>" + bucket.bucketName + " " + "</font><font color='#B9B9B9'>(" + bucket.count + ")</font>";
                        holder.text.setText(Html.fromHtml(tempText2));
                        holder.img.setTag(bucket.imageURL);
                        disPath = bucket.imageURL;
                    }
                }
                final ViewHolder finalHolder = holder;
                Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(disPath, size, size, new NativeImageLoader.NativeImageCallBack() {
                    @Override
                    public void onImageLoader(Bitmap bitmap, String path) {
                        if (bitmap != null && finalHolder.img != null) {
                            finalHolder.img.setImageBitmap(bitmap);
                        }
                    }
                });
                if (bitmap != null) {
                    holder.img.setImageBitmap(bitmap);
                    /*if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                    }*/
                } else {
                    holder.img.setImageResource(R.drawable.add_gallery_icon);
                }
            }
            return convertView;
        }
        private class ViewHolder {
            ImageView img;
            TextView text;
        }

    }

    /**
     * 展示相册列表
     */
    private void showButketView() {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.shq_all_bucket_list, null);
        ListView bListView = (ListView) layout.findViewById(R.id.blist);
        BucketAdapter mainAdapter = new BucketAdapter();
        bListView.setAdapter(mainAdapter);
        mPopupWindow = new PopupWindow(this);
       // mPopupWindow.setAnimationStyle(R.style.popupwindow_anim_bottom_style);
        mPopupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                findViewById(R.id.gray_bg).setVisibility(View.GONE);
              //  allImages.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.trade_down), null);
                iv_down_img.setImageResource(R.drawable.trade_down);
            }
        });
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(Util.getHeight(ChoosePhotoGridActivity.this) - Util.dip2px(ChoosePhotoGridActivity.this, 200));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setContentView(layout);
        mPopupWindow.showAtLocation(findViewById(R.id.bottom_lay), Gravity.BOTTOM, 0, Util.dip2px(ChoosePhotoGridActivity.this, 50));
        bListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPopupWindow.dismiss();
                dataList.clear();
                if (position == 0) {
                    allImages.setText("全部图片");// 全部图片
                    for (int i = 0; i < PhotoItemList.size(); ++i) {
                        PhotoItem image = PhotoItemList.get(i);
                        dataList.add(image);
                    }
                } else {
                    allImages.setText(bucketNameList.get(position - 1));
                    String bucketName = bucketNameList.get(position - 1);
                    for (int j = 0; j < PhotoItemList.size(); j++) {
                        PhotoItem image = PhotoItemList.get(j);
                        if (image.bucketName.equals(bucketName))
                            dataList.add(image);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case 101:
                ArrayList<String> list = data.getStringArrayListExtra("list");
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        String path = list.get(i);
                        adapter.selectList.remove(path);
                        for (int j = 0; j < dataList.size(); j++) {
                            PhotoItem item = dataList.get(j);
                            if (item.imagePath.equals(path)) {
                                item.isSelected = false;
                            }
                        }
                    }
                    changeChooseCount(adapter.selectList.size());
                    adapter.notifyDataSetChanged();
                }
                break;
            case 200:
                if (adapter.selectList.size() < 9) {
                   /* adapter.selectList.add(theLarge);
                    Bimp.drr.clear();
                    Bimp.drr.addAll(adapter.selectList);
                    // setResult(RESULT_OK);
                    Intent in = new Intent(this, PublishDynamicActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    in.putExtra("from", from);
                    startActivity(in);
                    // finish();
                    finish();*/
                } else {
                    showToast("最多上传9张图片");
                }
                break;
            default:
                break;
        }
    }

}
