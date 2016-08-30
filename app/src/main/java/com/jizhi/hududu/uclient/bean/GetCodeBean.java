package com.jizhi.hududu.uclient.bean;

/**
 * 
 * @author huChangSheng
 * @time 2015-8-21 下午2:35:46
 * @version 1.0
 * 
 */
public class GetCodeBean {
	private int state;
	private int code;
	private GetCode resp;

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

	public GetCode getResp() {
		return resp;
	}

	public void setResp(GetCode resp) {
		this.resp = resp;
	}

}
