package com.jizhi.hududu.uclient.bean;


public class OrderhMFkState {
	private int state;
	private int code;
	private OrderhMFkInfo fkInfo;

	public OrderhMFkInfo getFkInfo() {
		return fkInfo;
	}

	public void setFkInfo(OrderhMFkInfo fkInfo) {
		this.fkInfo = fkInfo;
	}

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

}
