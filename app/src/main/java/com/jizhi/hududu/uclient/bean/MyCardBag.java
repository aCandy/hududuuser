package com.jizhi.hududu.uclient.bean;

public class MyCardBag {
	
	
	
	private String isuserd; //是否使用
	private String endtime; //最后使用时间
	private String modetype; //优惠券类型（cash：现金券，coupon：通用优惠券）
	private String count; //优惠金额
	private String[] desc; //优惠券上的内容描述

		

	
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getModetype() {
		return modetype;
	}
	public void setModetype(String modetype) {
		this.modetype = modetype;
	}
	public String getIsuserd() {
		return isuserd;
	}
	public void setIsuserd(String isuserd) {
		this.isuserd = isuserd;
	}
	public String[] getDesc() {
		return desc;
	}
	public void setDesc(String[] desc) {
		this.desc = desc;
	}
	
	
	
}
