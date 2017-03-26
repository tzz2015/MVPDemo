package com.example.fwms.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.commonlibary.pickerview.takephoto.util.Bimp;
import com.example.commonlibary.pickerview.takephoto.util.Util;
import com.example.fwms.myapplication.R;


/**
 * Created by zhuhuimin on 2016/10/12.
 */
public class PublicImageAdapter extends BaseAdapter {
    private Context context;
    private Handler handler;
    private Thread loadImgThread;

    public PublicImageAdapter(Context c, Handler h) {
        this.context = c;
        this.handler = h;
    }

    @Override
    public int getCount() {
        return (Bimp.bmp.size() + 1);
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
        final int coord = position;
        ViewHolder holder = null;
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.shq_item_published_grida, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
            holder.del = (ImageView) convertView.findViewById(R.id.del);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int screenW = Util.getWidth(context);
        int bmpW = screenW * 7 / 24;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.image.getLayoutParams();
        params.height = bmpW;
        params.width = bmpW;
        holder.image.setLayoutParams(params);

        if (position == Bimp.bmp.size()) {
            holder.del.setVisibility(View.GONE);
            holder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.tianjia_kuang2x));
            if (position == 9) {
                holder.image.setVisibility(View.GONE);
            }
        } else {
            holder.del.setVisibility(View.VISIBLE);
            holder.image.setImageBitmap(Bimp.bmp.get(position));
            holder.image.setVisibility(View.VISIBLE);
        }
        holder.del.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bimp.bmp.remove(coord);
                Bimp.drr.remove(coord);
                Bimp.max -= 1;
                Message message = new Message();
                message.what = 1;
                message.arg1 = 222;
                handler.sendMessageDelayed(message, 10);
            }
        });
        return convertView;
    }

    public void loading() {

        if (loadImgThread != null) {
            loadImgThread.interrupt();
        } else {
            // loadImgThread = new LoadImgAsyncTask();
        }
        // loadImgThread.execute();
        loadImgThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (Bimp.max == Bimp.drr.size()) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        break;
                    } else {
                        try {
                            if (Bimp.drr.size() > Bimp.max) {
                                String path = Bimp.drr.get(Bimp.max);
                                Bitmap bm = Bimp.revitionImageSize(path);
                                Bimp.bmp.add(bm);
//								String newStr = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
//								FileUtils.saveBitmap(bm, "" + newStr);
                                Bimp.max += 1;
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        loadImgThread.start();
    }

    public class ViewHolder {
        public ImageView image, del;
    }

}
