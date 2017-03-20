package com.example.commonlibary.pickerview.takephoto.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.commonlibary.R;
import com.example.commonlibary.pickerview.takephoto.util.StatusBarCompat;
import com.example.commonlibary.pickerview.takephoto.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

import cn.finalteam.galleryfinal.widget.zoonview.PhotoView;
import cn.finalteam.galleryfinal.widget.zoonview.PhotoViewAttacher;

/**
 * 图片预览界面
 */
public class ShowPhotoActivity extends BaseActivity implements ViewPager.OnPageChangeListener {




    /**
     * 当前展示图片的下标
     */
    private int index;

    /**
     * 图片链接列表
     */
    private List<String> imageUrls;

    /**
     * 需展示图片的总数量
     */
    private String numString;

    private SamplePagerAdapter samplePagerAdapter;
    private PhotoViewViewPager pvvpPhoto;
    private TextView tvSave;
    private TextView tvNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, getResources().getColor(R.color.color_00ABF1));
        setContentView(R.layout.activity_show_photo);

        initUI();
    }

    private void initUI() {
        pvvpPhoto = (PhotoViewViewPager) findViewById(R.id.pvvpPhoto);
        tvSave = (TextView) findViewById(R.id.tvSave);
        tvNum = (TextView) findViewById(R.id.tvNum);

        index = getIntent().getIntExtra("index", 0);
        imageUrls = getIntent().getStringArrayListExtra("imageUrls");
        numString = String.valueOf(imageUrls.size());



        samplePagerAdapter = new SamplePagerAdapter();
        pvvpPhoto.setAdapter(samplePagerAdapter);
        pvvpPhoto.setOnPageChangeListener(this);
//        setClickListener(R.id.tvSave);

        setTvNum();

    }

    /**
     * 展示当前展示第几张图片
     */
    public void setTvNum() {
        tvNum.setText(String.valueOf(index + 1) + "/" + numString);
        pvvpPhoto.setCurrentItem(index);
    }



    /**
     * 保存路径
     */
//    private final String SAVE_PATH = Environment.getExternalStorageDirectory() + Fields.SAVE_PIC_PATH;


    /**
     * 图片保存
     *
     */
//    private void saveImageToLocal(final String url) {
//        if (!Util.isEmpty(url) && url.startsWith("http")) {
//            try {
//                Observable.just(url)
//                        .subscribeOn(Schedulers.newThread())//请求网络在子线程中
//                        .map(new Func1<String, File>() {
//                            @Override
//                            public File call(String s) {
//                                File fromFile = null;
//                                try {
//                                    fromFile = Glide.with(ShowPhotoActivity.this).load(url).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                                return fromFile;
//                            }
//                        })
//                        .observeOn(AndroidSchedulers.mainThread())//回调在主线程中
//                        .subscribe(new Action1<File>() {
//                            @Override
//                            public void call(File fromFile) {
//                                String filename = url.substring(url.lastIndexOf("/"));
//                                File toFile = new File(SAVE_PATH + filename);
//
//                                if (copyFile(fromFile, toFile, true)) {
//                                    showToast("图片成功保存到" + SAVE_PATH);
//                                } else {
//                                    showToast("图片下载失败");
//                                }
//                            }
//                        });
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        index = position;
        setTvNum();

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 图片适配器
     */
    class SamplePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imageUrls != null ? imageUrls.size() : 0;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(ShowPhotoActivity.this);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            photoView.setLayoutParams(layoutParams);

            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    finish();
                }
            });
            String imageUrl = imageUrls.get(position);
            if (imageUrl.startsWith("http")) {
//                if (imageUrl.endsWith(".gif")) {
//                    LinearLayout parent = new LinearLayout(container.getContext());
//                    parent.setGravity(Gravity.CENTER);
//                    GifViewManager gifView = new GifViewManager(container.getContext(), parent);
//                    gifView.setResource(imageUrl);
//                    container.addView(parent, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

//                    ImageView imageView = new ImageView(ShowPhotoActivity.this);
//                    Glide.with(ShowPhotoActivity.this)
//                            .load(imageUrl)
//                            .fitCenter()
//                            .placeholder(R.drawable.default_img)
//                            .into(imageView);
//                    container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                    return parent;
//                } else {
                    Glide.with(ShowPhotoActivity.this)
                            .load(imageUrl)
                            .fitCenter()
                            .placeholder(R.drawable.default_img)
                            .into(photoView);

                    container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    return photoView;
//                }
            } else {
                photoView.setImageResource(Util.getImgIdByImgName(ShowPhotoActivity.this, imageUrl));
                container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                return photoView;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public static Intent getIntent(Context context, int index, List<String> imageUrls) {
        Intent intent = new Intent(context, ShowPhotoActivity.class);
        intent.putExtra("index", index);
        intent.putExtra("imageUrls", (Serializable) imageUrls);
        return intent;
    }

    public static boolean copyFile(File srcFile, File destFile,
                                   boolean overlay) {
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            return false;
        } else if (!srcFile.isFile()) {
            return false;
        }

        // 判断目标文件是否存在
        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖
            if (overlay) {
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
                destFile.delete();
            }
        } else {
            // 如果目标文件所在目录不存在，则创建目录
            if (!destFile.getParentFile().exists()) {
                // 目标文件所在目录不存在
                if (!destFile.getParentFile().mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    return false;
                }
            }
        }

        // 复制文件
        int byteread = 0; // 读取的字节数
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
