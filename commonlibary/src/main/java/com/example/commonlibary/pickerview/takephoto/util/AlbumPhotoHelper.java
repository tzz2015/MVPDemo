package com.example.commonlibary.pickerview.takephoto.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;

import com.example.commonlibary.pickerview.takephoto.Model.ImageBucket;
import com.example.commonlibary.pickerview.takephoto.Model.PhotoItem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 
 * 相册专辑帮助类. <br>
 * <p>
 * Copyright: Copyright (c) 2015年5月18日 下午2:43:07
 * <p>
 * Company: 苏州宽连信息技术有限公司
 * <p>
 * 
 * @author nilei@c-platform.com
 * @version 1.0.0
 */
public class AlbumPhotoHelper {
	final String TAG = getClass().getSimpleName();
	Context context;
	ContentResolver cr;

	// 缩略图列表
	HashMap<String, String> thumbnailList = new HashMap<String, String>();
	// 专辑列表
	HashMap<String, ImageBucket> bucketList = new HashMap<String, ImageBucket>();

	List<String> bucketNameList = new ArrayList<String>();

	List<PhotoItem> allImageList = new ArrayList<PhotoItem>();

	public HashMap<String, ImageBucket> getBucketList() {
		return bucketList;
	}
	
	public List<String> getBucketNameList() {
		return bucketNameList;
	}

	private static AlbumPhotoHelper instance;

	private AlbumPhotoHelper() {
	}

	public static AlbumPhotoHelper getHelper() {
		if (instance == null) {
			instance = new AlbumPhotoHelper();
		}
		return instance;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		if (this.context == null) {
			this.context = context;
			cr = context.getContentResolver();
		}
	}

	/**
	 * 从数据库中得到缩略图
	 * 
	 * @param cur
	 */
	private void getThumbnailColumnData(Cursor cur) {
		thumbnailList.clear();
		if (cur.moveToFirst()) {
			int _id;
			int image_id;
			String image_path;
			int _idColumn = cur.getColumnIndex(Thumbnails._ID);
			int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
			int dataColumn = cur.getColumnIndex(Thumbnails.DATA);

			do {
				// Get the field values
				_id = cur.getInt(_idColumn);
				image_id = cur.getInt(image_idColumn);
				image_path = cur.getString(dataColumn);

				thumbnailList.put("" + image_id, image_path);
			} while (cur.moveToNext());
		}
	}

	/**
	 * 得到缩略图
	 */
	private void getThumbnail() {
		String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA };
		Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
		getThumbnailColumnData(cursor);
	}

	/**
	 * 得到图片集
	 */
	public void buildImagesBucketList(List<String> drr) {
		bucketList.clear();
		bucketNameList.clear();
		allImageList.clear();
		long startTime = System.currentTimeMillis();

		// 构造缩略图索引
		getThumbnail();

		// 构造相册索引

		String columns[] = new String[] { Media._ID, Media.BUCKET_ID, Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE, Media.SIZE, Media.BUCKET_DISPLAY_NAME };
		// 得到一个游标
		Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns, null, null, Media.DATE_ADDED + " desc");
		if (cur.moveToFirst()) {
			// 获取指定列的索引
			int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
			int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
			int photoNameIndex = cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
			int photoTitleIndex = cur.getColumnIndexOrThrow(Media.TITLE);
			int photoSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE);
			int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
			int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);
			int picasaIdIndex = cur.getColumnIndexOrThrow(Media.PICASA_ID);
			// 获取图片总数
			int totalNum = cur.getCount();

			do {
				String _id = cur.getString(photoIDIndex);
				String name = cur.getString(photoNameIndex);
				String path = cur.getString(photoPathIndex);
				String title = cur.getString(photoTitleIndex);
				String size = cur.getString(photoSizeIndex);
				String bucketName = cur.getString(bucketDisplayNameIndex);
				String bucketId = cur.getString(bucketIdIndex);
				String picasaId = cur.getString(picasaIdIndex);

				String pathTempString = path;
				File file = new File(path);
				if (!pathTempString.toLowerCase().contains("cache") && file.exists() && file.length() > 0) {
					if (!bucketNameList.contains(bucketName)) {
						ImageBucket ib = new ImageBucket();
						ib.bucketName = bucketName;
						ib.count = 1;
						ib.imageURL = path;
						ib.thumbURL = thumbnailList.get(_id);
						bucketNameList.add(bucketName);
						bucketList.put(bucketName, ib);
					}else{
						ImageBucket ib = bucketList.get(bucketName);
						if(ib != null)
							ib.count++;
					}
					PhotoItem photoItem = new PhotoItem();
					photoItem.imageId = _id;
					photoItem.imagePath = path;
					photoItem.thumbnailPath = thumbnailList.get(_id);
					photoItem.bucketName = bucketName;
					if(drr != null && drr.size() > 0){
						photoItem.isSelected = drr.contains(path);
					}
					allImageList.add(photoItem);
				}
			} while (cur.moveToNext());
		}

		long endTime = System.currentTimeMillis();
	}

	/**
	 * 得到图片集
	 * 
	 * @return
	 */
	public List<PhotoItem> getImagesItemList() {
		return allImageList;
	}
	
}
