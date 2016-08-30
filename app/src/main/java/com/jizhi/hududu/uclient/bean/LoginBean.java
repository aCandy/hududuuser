package com.jizhi.hududu.uclient.bean;
/**
 * 登录
* @author huChangSheng
* @time 2015-8-21 下午4:30:51
* @version 1.0
*
 */
public class LoginBean {
	private int state;
	private int code;
	private UserInfo resp;
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public UserInfo getResp() {
		return resp;
	}
	public void setResp(UserInfo resp) {
		this.resp = resp;
	}
	
	
	
	
}
