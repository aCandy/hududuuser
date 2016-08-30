package com.jizhi.hududu.uclient.bean;

import java.util.List;


public class DinnerState {
	private int state;
	private List<Dinner> resp;
	
	private String oid;
	
	private int code;
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public List<Dinner> getResp() {
		return resp;
	}
	public void setResp(List<Dinner> resp) {
		this.resp = resp;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	
}
