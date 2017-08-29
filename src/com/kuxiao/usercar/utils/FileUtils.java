package com.kuxiao.usercar.utils;

import android.os.Environment;

public class FileUtils {
	
	private static FileUtils mFileUtils = null;
	

	private FileUtils()
	{
         		
	}
	
	
	//单例
	public static FileUtils getInstance()
	{
		   if(mFileUtils == null)
		   {
			    synchronized (FileUtils.class) {
					if( mFileUtils == null)
					{
						 mFileUtils = new FileUtils();
					}
				}
		   }
		   return mFileUtils;
	}
	
	
	/**
	 * 检查SdCard是否装好
	 * @return
	 */
	public boolean isHasSdCard()
	{
		 if(Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED))
			 return true;
		 
		 return false;
	}
	
	/**
	 * 获取SD卡的路径
	 * @return
	 */
	public String getSdCardDir()
	{   
		  if(isHasSdCard())
		  return Environment.getExternalStorageDirectory().toString();
		  return null;
	}
	
	/**
	 * 是否存在某文件
	 * @param path
	 * @return
	 */
	public boolean checkIsExistsPath(String path)
	{
		 return false;
	}
	
	
	
}
