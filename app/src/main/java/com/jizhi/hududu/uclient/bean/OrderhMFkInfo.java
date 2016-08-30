package com.jizhi.hududu.uclient.bean;

import java.io.Serializable;

public class OrderhMFkInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String lid;
	private double wage;
	private String pic;
	private String gender;// 性别 01 女男
	private int age;
	private double latitude;
	private double longitude;
	private String curLocation;
	private String activityType;// 值为 FODiscount, 代表首单 
	// 如果为0表示不打折， 其他直接用ordertotal-discount==实际付款金额
	private double discount;
	private String icno;
	// 评星
	private double avgrate;
	// 完成订单数量
	private int ordertotal;// 订单价钱
	// 工作描述：专业保洁：1 简单打扫 ：2 饭后洗碗：3
	private int jobdesc;
	// 抢单人数
	private int sendcount;
	private int distance;// 距离
	private double workinghours;// 服务时间
	private String wtype;
	private String quote;
	
	

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public String getWtype() {
		return wtype;
	}

	public void setWtype(String wtype) {
		this.wtype = wtype;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public double getWorkinghours() {
		return workinghours;
	}

	public void setWorkinghours(double workinghours) {
		this.workinghours = workinghours;
	}

	public String getLid() {
		return lid;
	}

	public void setLid(String lid) {
		this.lid = lid;
	}

	public double getWage() {
		return wage;
	}

	public void setWage(double wage) {
		this.wage = wage;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getCurLocation() {
		return curLocation;
	}

	public void setCurLocation(String curLocation) {
		this.curLocation = curLocation;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getIcno() {
		return icno;
	}

	public void setIcno(String icno) {
		this.icno = icno;
	}

	public double getAvgrate() {
		return avgrate;
	}

	public void setAvgrate(double avgrate) {
		this.avgrate = avgrate;
	}

	public int getOrdertotal() {
		return ordertotal;
	}

	public void setOrdertotal(int ordertotal) {
		this.ordertotal = ordertotal;
	}

	public int getJobdesc() {
		return jobdesc;
	}

	public void setJobdesc(int jobdesc) {
		this.jobdesc = jobdesc;
	}

	public int getSendcount() {
		return sendcount;
	}

	public void setSendcount(int sendcount) {
		this.sendcount = sendcount;
	}
}
