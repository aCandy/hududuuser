package com.jizhi.hududu.uclient.bean;

import java.io.Serializable;
/**
 * 帮我买菜
 * @author Xuj
 * @date 2015年8月26日 13:05:50
 */
public class AppRaise implements Serializable {
	private String name;
	private String pic;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	
	
}
