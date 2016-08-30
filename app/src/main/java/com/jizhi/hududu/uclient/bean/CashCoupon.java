package com.jizhi.hududu.uclient.bean;

import java.io.Serializable;


public class CashCoupon implements Serializable {
	private float count;
	private String uniqueid;
	public String getUniqueid() {
		return uniqueid;
	}
	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}
	public float getCount() {
		return count;
	}
	public void setCount(float count) {
		this.count = count;
	}

}
