package com.example.commonlibary.pickerview.city;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件缓存类，将图片缓存到sd卡中
 */
public class FileCache {

	/**
	 * TAG
	 */
	private static final String TAG = "FileCache";

	/**
	 * 单例
	 */
	private static FileCache diskCache;

	/**
	 * 上下文
	 */
	private Context context;

	/**
	 * 单例实例化
	 * 
	 * @param ctx
	 * @return
	 */
	public synchronized static FileCache newInstance(Context ctx) {
		if (diskCache == null) {
			diskCache = new FileCache(ctx);
		}
		return diskCache;
	}

	private FileCache(Context context) {
		this.context = context;
	}




	/**
	 * 保存json文件
	 */
	public void putTxt(String fileName,String content){
		String path = context.getFilesDir().getPath();
		FileOutputStream outputStream;
		try {
			File fileTemp = new File(path,fileName);
			if(fileTemp.exists()){
				fileTemp.delete();
			}
			outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			outputStream.write(content.getBytes("UTF-8"));
			outputStream.close();
        }
        catch (Exception e) {
	        e.printStackTrace();
        }

	}
	
	/**
	 * 拷贝文件到file目录下
	 * @param assetsSrc
	 * @param path
	 * @param fileName
	 * @return
	 */
	public boolean copyAssetsToFile(String assetsSrc,String path, String fileName){  
        InputStream istream = null;  
        FileOutputStream outputStream = null;
        try{  
			File fileTemp = new File(path,fileName);
			if(fileTemp.exists()){
				fileTemp.delete();
			}
			
            AssetManager am = context.getAssets();  
            istream = am.open(assetsSrc);  
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            byte[] buffer = new byte[1024];  
            int length;  
            while ((length = istream.read(buffer))>0){  
            	outputStream.write(buffer, 0, length);  
            }  
            istream.close();  
            outputStream.close();  
        }  
        catch(Exception e){  
            try{
				if (istream != null) {
					istream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}  
            }  
            catch(Exception ee){  
            }
            return false;  
        }  
        return true;  
    }
	
	/**
	 * 拷贝文件到databases目录下
	 * @param assetsSrc
	 * @param des
	 * @return
	 */
	public boolean copyAssetsToDatabases(String assetsSrc,String path, String fileName){  
        InputStream istream = null;  
        OutputStream ostream = null;  
        try{  
            AssetManager am = context.getAssets();  
            istream = am.open(assetsSrc);  
            File dir = new File(path);
            if(!dir.exists()){
            	dir.mkdir();
            }
            ostream = new FileOutputStream(path+"/"+fileName);  
            byte[] buffer = new byte[1024];  
            int length;  
            while ((length = istream.read(buffer))>0){  
                ostream.write(buffer, 0, length);  
            }  
            istream.close();  
            ostream.close();  
        }  
        catch(Exception e){  
            try{
                if(istream!=null)  
                    istream.close();  
                if(ostream!=null)  
                    ostream.close();  
            }  
            catch(Exception ee){  
            }
            return false;  
        }  
        return true;  
    }  
	
}
