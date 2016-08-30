package com.jizhi.hududu.uclient.bean;

/**
 * 验证码
 * @author huChangSheng
 * @time 2015-8-21 下午2:38:27
 * @version 1.0
 */
public class GetCode {
	private String sessid;
	private String code;

	public String getSessid() {
		return sessid;
	}

	public void setSessid(String sessid) {
		this.sessid = sessid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
