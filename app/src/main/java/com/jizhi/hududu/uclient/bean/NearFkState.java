package com.jizhi.hududu.uclient.bean;

import java.util.List;

public class NearFkState {
	private int state;
	private int code;
	private List<NearFk> nearFks;

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

	public List<NearFk> getNearFks() {
		return nearFks;
	}

	public void setNearFks(List<NearFk> nearFks) {
		this.nearFks = nearFks;
	}

}
