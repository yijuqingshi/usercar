package com.kuxiao.usercar.utils;

import android.os.Environment;

public class FileUtils {
	
	private static FileUtils mFileUtils = null;
	

	private FileUtils()
	{
         		
	}
	
	
	//����
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
	 * ���SdCard�Ƿ�װ��
	 * @return
	 */
	public boolean isHasSdCard()
	{
		 if(Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED))
			 return true;
		 
		 return false;
	}
	
	/**
	 * ��ȡSD����·��
	 * @return
	 */
	public String getSdCardDir()
	{   
		  if(isHasSdCard())
		  return Environment.getExternalStorageDirectory().toString();
		  return null;
	}
	
	/**
	 * �Ƿ����ĳ�ļ�
	 * @param path
	 * @return
	 */
	public boolean checkIsExistsPath(String path)
	{
		 return false;
	}
	
	
	
}
