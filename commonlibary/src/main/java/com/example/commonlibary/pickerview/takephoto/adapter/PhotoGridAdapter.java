package com.example.commonlibary.pickerview.takephoto.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.commonlibary.R;
import com.example.commonlibary.pickerview.takephoto.CameraActivity;
import com.example.commonlibary.pickerview.takephoto.Model.PhotoItem;
import com.example.commonlibary.pickerview.takephoto.view.PhotoDialog;
import com.example.commonlibary.pickerview.takephoto.util.Util;
import com.example.commonlibary.pickerview.takephoto.util.BitmapCache;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.widget.GFImageView;

public class PhotoGridAdapter extends BaseAdapter {

    private TextCallback textcallback = null;

    Activity act;

    List<PhotoItem> dataList;

    BitmapCache cache;

    private Handler mHandler;

    public ArrayList<String> selectList = new ArrayList<String>();

    int size = 0;

    public interface TextCallback {
        void onListen(int count);
    }

    public void setTextCallback(TextCallback listener) {
        textcallback = listener;
    }

    public PhotoGridAdapter(Activity act, List<PhotoItem> list, Handler mHandler) {
        this.act = act;
        dataList = list;
        cache = new BitmapCache();
        this.mHandler = mHandler;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (dataList != null) {
            count = dataList.size();
        } else {
            count = 1;
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    class Holder {
        private GFImageView iv;
        private ImageView selected;
        private TextView text;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(act, R.layout.shq_item_image_grid, null);
            holder.iv = (GFImageView) convertView.findViewById(R.id.image);
            holder.selected = (ImageView) convertView.findViewById(R.id.isselected);
            holder.text = (TextView) convertView.findViewById(R.id.item_image_grid_text);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.iv.getLayoutParams();
            size = Util.getWidth(act) / 3 - 10;
            params.width = size;
            params.height = size;
            holder.iv.setLayoutParams(params);
            params.height = params.height + 2;
            params.width = params.width + 2;
            holder.text.setLayoutParams(params);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final PhotoItem item = dataList.get(position);
        holder.iv.setTag(item.imagePath);
        String disPath = null;
        if (!TextUtils.isEmpty(item.thumbnailPath)) {
            disPath = item.thumbnailPath;
        } else if (!TextUtils.isEmpty(item.imagePath)) {
            disPath = item.imagePath;
        }
        // fbBitmap.display(holder.iv, disPath,
        // R.drawable.shq_add_gallery_icon);
        // 利用NativeImageLoader类加载本地图片
       /* Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(disPath, size, size, new NativeImageLoader.NativeImageCallBack() {
            @Override
            public void onImageLoader(Bitmap bitmap, String path) {
                if (bitmap != null && holder.iv != null) {
                    holder.iv.setImageBitmap(bitmap);
                }
            }
        });
        if (bitmap != null) {
            holder.iv.setImageBitmap(bitmap);
        } else {
            holder.iv.setImageResource(R.drawable.shq_add_gallery_icon);
        }*/

        holder.iv.setImageResource(R.drawable.shq_add_gallery_icon);
        Drawable defaultDrawable = act.getResources().getDrawable(cn.finalteam.galleryfinal.R.drawable.ic_gf_default_photo);
        GalleryFinal.getCoreConfig().getImageLoader().displayImage(act, disPath, holder.iv, defaultDrawable, size, size);
        if(PhotoDialog.maxSize != 0){
            if (item.isSelected) {
                holder.selected.setImageResource(R.drawable.shq_icon_data_select);
                holder.text.setBackgroundResource(R.drawable.shq_bgd_relatly_line);
            } else {
                holder.selected.setImageResource(R.drawable.icon_data_unselect);
                holder.text.setBackgroundColor(0x00000000);
            }
        }
        holder.iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = dataList.get(position).imagePath;
                if(PhotoDialog.maxSize == 0){
                    ArrayList<String> list = new ArrayList<>();
                    list.add(item.imagePath);
                    act.getIntent().putStringArrayListExtra("list",list);
                    act.setResult(CameraActivity.IMAGE_RESULT_CODE,act.getIntent());
                    act.finish();
                    return;
                }else{
                    if ((selectList.size()) < PhotoDialog.maxSize) {
                        item.isSelected = !item.isSelected;
                        if (item.isSelected) {
                            holder.selected.setImageResource(R.drawable.shq_icon_data_select);
                            holder.text.setBackgroundResource(R.drawable.shq_bgd_relatly_line);
                            selectList.add(path);
                            if (textcallback != null)
                                textcallback.onListen(selectList.size());

                        } else {
                            holder.selected.setImageResource(R.drawable.icon_data_unselect);
                            holder.text.setBackgroundColor(0x00000000);
                            selectList.remove(path);
                            if (textcallback != null)
                                textcallback.onListen(selectList.size());
                        }
                    } else if (selectList.size() >= PhotoDialog.maxSize) {
                        if (item.isSelected == true) {
                            item.isSelected = !item.isSelected;
                            holder.selected.setImageResource(R.drawable.icon_data_unselect);
                            selectList.remove(path);
                        } else {
                            Message message = Message.obtain(mHandler, 0);
                            message.sendToTarget();
                        }
                    }
                }
            }

        });
        return convertView;
    }
}
