package com.jizhi.hududu.uclient.bean;

/**
 * 家庭保洁
 * @author Xuj
 * @date 2015年8月26日 13:06:13
 */
public class HouseholdCleaningBean {
	private int customer_ID; //顾客id
	private int job_desc; //1：日常保洁 2：饭后洗碗 3：简单打扫
	private String server_time; //上门服务的时间 timstamp
	private String address; //服务地址
	private String location; //服务地点坐标 "纬度，经度"
	private String working_hours; //工作时长 以小时为一个单位 
	private String desc; //工作描述
	private float price;
	private float finalPrice; //最终价格
	
	public int getCustomer_ID() {
		return customer_ID;
	}
	public void setCustomer_ID(int customer_ID) {
		this.customer_ID = customer_ID;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getWorking_hours() {
		return working_hours;
	}
	public void setWorking_hours(String working_hours) {
		this.working_hours = working_hours;
	}
	public String getServer_time() {
		return server_time;
	}
	public void setServer_time(String server_time) {
		this.server_time = server_time;
	}
	public int getJob_desc() {
		return job_desc;
	}
	public void setJob_desc(int job_desc) {
		this.job_desc = job_desc;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public float getFinalPrice() {
		return finalPrice;
	}
	public void setFinalPrice(float finalPrice) {
		this.finalPrice = finalPrice;
	}
	
	
	
}
