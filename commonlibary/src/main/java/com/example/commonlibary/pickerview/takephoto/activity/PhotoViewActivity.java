/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.example.commonlibary.pickerview.takephoto.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.commonlibary.R;
import com.example.commonlibary.pickerview.takephoto.util.Bimp;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import cn.finalteam.galleryfinal.widget.zoonview.PhotoView;
import cn.finalteam.galleryfinal.widget.zoonview.PhotoViewAttacher;

/**
 *
 * 图片预览界面. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2015年5月19日 下午5:09:43
 * <p>
 * Company: 苏州宽连信息技术有限公司
 * <p>
 *
 * @author nilei@c-platform.com
 * @version 1.0.0
 */

public class PhotoViewActivity extends BaseActivity  {

	private PhotoViewViewPager mViewPager;
	private SamplePagerAdapter mAdapter;
	private ArrayList<String> mUrlList;
	private ArrayList<String> mUrlDelList;
	private int mCurrentIndex;
	private TextView num;
	//	private FinalBitmap mFb;
	private CheckBox check;
	private String fromString;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shq_activity_scanphotoview_pager);
//		mFb = FinalBitmap.create(this);
		mUrlList = new ArrayList<String>();
		mUrlDelList = new ArrayList<String>();
		// mUrlDelIndexList = new ArrayList<>();
		Intent intent = getIntent();
		mCurrentIndex = intent.getIntExtra("index", 0);
		fromString = intent.getStringExtra("from");
		mUrlList = (ArrayList<String>) getIntent().getSerializableExtra("list");
		check = (CheckBox) findViewById(R.id.check);
		num = (TextView) findViewById(R.id.num);
		mViewPager = (PhotoViewViewPager) findViewById(R.id.view_pager);
		findViewById(R.id.bt_done).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent data = new Intent();
				data.putStringArrayListExtra("list", mUrlDelList);
				setResult(RESULT_OK, data);
				finish();
			}
		});
		findViewById(R.id.btn_return).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mAdapter = new SamplePagerAdapter();
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				mCurrentIndex = arg0;
				num.setText((mCurrentIndex + 1) + "/" + mUrlList.size());
				if (mUrlDelList.contains(mUrlList.get(mCurrentIndex))) {
					check.setChecked(false);
				} else {
					check.setChecked(true);
				}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (check.isChecked()) {
					mUrlDelList.remove(mUrlList.get(mCurrentIndex));
				} else {
					mUrlDelList.add(mUrlList.get(mCurrentIndex));
				}
			}
		});
		mViewPager.setCurrentItem(mCurrentIndex);
		num.setText((mCurrentIndex + 1) + "/" + mUrlList.size());
	}

	class SamplePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mUrlList != null ? mUrlList.size() : 0;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(PhotoViewActivity.this);
			ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			photoView.setLayoutParams(layoutParams);

			photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
				@Override
				public void onPhotoTap(View view, float x, float y) {
					finish();
				}
			});
			String imageUrl = mUrlList.get(position);
			if (imageUrl.startsWith("http")) {
				Glide.with(PhotoViewActivity.this)
						.load(imageUrl)
						.fitCenter()
						.placeholder(R.drawable.default_img)
						.into(photoView);
				container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
				return photoView;
//                }
			} else {
				File file = new File(imageUrl);
				try {
					photoView.setImageBitmap(Bimp.revitionImageSize(imageUrl));
				} catch (IOException e) {
					e.printStackTrace();
				}
				//photoView.setImageResource(Util.getImgIdByImgName(PhotoViewActivity.this, imageUrl));
				container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
				return photoView;
			}

		}

		private byte[] imageToByteArray(String imgPath) {
			BufferedInputStream in;
			try {
				in = new BufferedInputStream(new FileInputStream(imgPath));
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int size = 0;
				byte[] temp = new byte[1024];
				while ((size = in.read(temp)) != -1) {
					out.write(temp, 0, size);
				}
				in.close();
				return out.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
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
	public void onBackPressed() {
		finish();
	}



}
