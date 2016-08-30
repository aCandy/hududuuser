package com.jizhi.hududu.uclient.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 服客
 * @author Administrator
 *
 */
public class ServerDetailState implements Serializable{
	
	private int state;
	private ServerDetail resp;
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public ServerDetail getResp() {
		return resp;
	}
	public void setResp(ServerDetail resp) {
		this.resp = resp;
	}
	
	
	
	
}
