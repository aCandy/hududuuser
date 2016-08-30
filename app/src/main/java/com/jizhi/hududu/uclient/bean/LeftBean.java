package com.jizhi.hududu.uclient.bean;

import android.graphics.drawable.Drawable;
/**
 * Slidmenu 所需
 * @author Xuj
 * @date 2015年8月26日 13:06:29
 */
public class LeftBean {
	private String menu_text; //菜单名称
	private Drawable menu_image; //菜单图片
	private boolean isNotice; //是否有通知
	private String menu_value; //通知显示的值
	public String getMenu_text() {
		return menu_text;
	}
	public void setMenu_text(String menu_text) {
		this.menu_text = menu_text;
	}
	public Drawable getMenu_image() {
		return menu_image;
	}
	public void setMenu_image(Drawable menu_image) {
		this.menu_image = menu_image;
	}
	public boolean isNotice() {
		return isNotice;
	}
	public void setNotice(boolean isNotice) {
		this.isNotice = isNotice;
	}
	public String getMenu_value() {
		return menu_value;
	}
	public void setMenu_value(String menu_value) {
		this.menu_value = menu_value;
	}
	public LeftBean(String menu_text, Drawable menu_image) {
		super();
		this.menu_text = menu_text;
		this.menu_image = menu_image;
	}
	
}
