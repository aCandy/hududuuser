package com.jizhi.hududu.uclient.util;

import java.io.File;

import android.os.Environment;

public class UtilFile {
	public static String imgPath = "/hududuClient/ImageCache";

	/**
	 * 添加菜品临时路径
	 * 
	 * @return
	 */
	public static String createPicCache() {
		String filePath = Environment.getExternalStorageDirectory() + imgPath
				+ "/picCache";
		File fileParent = new File(filePath);
		if (fileParent.exists() == false) {
			fileParent.mkdirs();
		}
		return fileParent.getPath();
	}
	

	/**
	 * 头像-图片路径
	 * 
	 * @return
	 */
	public static String createHeadFileImg() {
		String filePath = Environment.getExternalStorageDirectory()
				+ "/jizhi/ImageCache/head/Img";
		File fileParent = new File(filePath);
		if (fileParent.exists() == false) {
			fileParent.mkdirs();
		}
		return fileParent.getPath();
	}
	
	/**
	 * 头像-拍照临时路径
	 * 
	 * @return
	 */
	public static String createHeadTempPhotoPath() {
		String filePath = Environment.getExternalStorageDirectory()
				+ "/jizhi/ImageCache/tempHead/img";
		File fileParent = new File(filePath);
		if (fileParent.exists() == false) {
			fileParent.mkdirs();
		}
		return fileParent.getPath();
	}
}
