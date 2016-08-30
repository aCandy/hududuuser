package com.jizhi.hududu.uclient.bean;

import java.io.Serializable;

public class ShareState implements Serializable{
	private int state;
	private int code;
	private Share resp;
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
	public Share getResp() {
		return resp;
	}
	public void setResp(Share resp) {
		this.resp = resp;
	}
	
	
}
