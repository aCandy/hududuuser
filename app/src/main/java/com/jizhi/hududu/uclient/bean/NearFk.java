package com.jizhi.hududu.uclient.bean;

/**
 * 附近服客信息
 * 
 * @author huChangSheng
 * @time 2015-8-5 下午4:14:51
 * @version 1.0
 * 
 */
public class NearFk {

	private String name;
	private double latitude;
	private double longitude;
	private int ordertotal;
	private String pic;
	private String icno;
	private double avgrate;
	private String age;

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public int getOrdertotal() {
		return ordertotal;
	}

	public void setOrdertotal(int ordertotal) {
		this.ordertotal = ordertotal;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

}
