package com.jizhi.hududu.uclient.bean;

import java.io.Serializable;

import android.graphics.Bitmap;
/**
 * 拍照
 * @author Xuj
 * @date 2015年8月26日 13:05:50
 */
public class Photos implements Serializable {

	
	private String url;
	private boolean add = false;
	private Bitmap bitmap;
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public boolean isAdd() {
		return add;
	}
	public void setAdd(boolean add) {
		this.add = add;
	}
	public Photos(boolean add, Bitmap bitmap) {
		super();
		this.add = add;
		this.bitmap = bitmap;
	}
	
	public Photos(Bitmap bitmap,String url){
		this.bitmap = bitmap;
		this.url = url;
	}
	public Photos(){
		
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
