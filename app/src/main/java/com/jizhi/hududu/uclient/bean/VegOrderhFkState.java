package com.jizhi.hududu.uclient.bean;

public class VegOrderhFkState {
	private int state;
	private int code;
	private Resp resp;

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

	public Resp getResp() {
		return resp;
	}

	public void setResp(Resp resp) {
		this.resp = resp;
	}

	public class Resp {
		private VegetableOrderhFkInfo info;

		public VegetableOrderhFkInfo getInfo() {
			return info;
		}

		public void setInfo(VegetableOrderhFkInfo info) {
			this.info = info;
		}

	}
}
