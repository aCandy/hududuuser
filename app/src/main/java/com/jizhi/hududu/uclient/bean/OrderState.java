package com.jizhi.hududu.uclient.bean;

import java.util.ArrayList;
import java.util.List;

public class OrderState {
	
	private int state;
	
	private String error_meesage;
	
	private List<Order> resp = new ArrayList<Order>();
	
	private long receive_time; 
	
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	public List<Order> getResp() {
		return resp;
	}

	public void setResp(List<Order> resp) {
		this.resp = resp;
	}

	public long getReceive_time() {
		return receive_time;
	}

	public void setReceive_time(long receive_time) {
		this.receive_time = receive_time;
	}

	public String getError_meesage() {
		return error_meesage;
	}

	public void setError_meesage(String error_meesage) {
		this.error_meesage = error_meesage;
	}







}
