package com.jizhi.hududu.uclient.bean;

import java.util.List;

public class HouseHoldCleaningState {
	private int state;
	private int code;
	private List<HouseholdCleaningBean> list;
	private String oid;
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

	public List<HouseholdCleaningBean> getList() {
		return list;
	}

	public void setList(List<HouseholdCleaningBean> list) {
		this.list = list;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}




}
