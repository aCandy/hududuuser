package com.jizhi.hududu.uclient.bean;

import java.io.Serializable;
/**
 * 拿手菜
 * @author Xuj
 * @date 2015年8月27日 12:59:20
 */
public class Dishes implements Serializable{
	private String dishname; //拿手菜名称
	private String dishpic; //拿手菜图片
	public String getDishname() {
		return dishname;
	}
	public void setDishname(String dishname) {
		this.dishname = dishname;
	}
	public String getDishpic() {
		return dishpic;
	}
	public void setDishpic(String dishpic) {
		this.dishpic = dishpic;
	}
	
	
	
	
}
