package com.jizhi.hududu.uclient.bean;

import java.util.List;


public class BuyVegetablesState {

	private int state;
	private List<BuyVegetables> resp;
	private String oid;
	private String code;
	
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public List<BuyVegetables> getResp() {
		return resp;
	}
	public void setResp(List<BuyVegetables> resp) {
		this.resp = resp;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
}
