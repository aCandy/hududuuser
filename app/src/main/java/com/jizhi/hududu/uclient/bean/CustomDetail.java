package com.jizhi.hududu.uclient.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 顾客资料
 * @author Xuj
 */
public class CustomDetail implements Serializable{
	private String cid;			//呼客
	private String appraise_time;		//评论时间	
	private String lappraise;		//评论内容
	private String lrate;			//评论的分数
	private List<String> appraisepic;		//评论的图片
	private String name;			//顾客姓名
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getAppraise_time() {
		return appraise_time;
	}
	public void setAppraise_time(String appraise_time) {
		this.appraise_time = appraise_time;
	}
	public String getLappraise() {
		return lappraise;
	}
	public void setLappraise(String lappraise) {
		this.lappraise = lappraise;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getAppraisepic() {
		return appraisepic;
	}
	public void setAppraisepic(List<String> appraisepic) {
		this.appraisepic = appraisepic;
	}
	public String getLrate() {
		return lrate;
	}
	public void setLrate(String lrate) {
		this.lrate = lrate;
	}
	
	
	
	
}
