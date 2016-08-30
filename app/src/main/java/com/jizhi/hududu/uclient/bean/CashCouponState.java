package com.jizhi.hududu.uclient.bean;

import java.io.Serializable;


/**
 * 添加现金券
 */
public class CashCouponState implements Serializable {
	private int state;
	private String errormsg;
	private CashCoupon resp;
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public CashCoupon getResp() {
		return resp;
	}
	public void setResp(CashCoupon resp) {
		this.resp = resp;
	}
	
}
