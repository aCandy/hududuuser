package com.jizhi.hududu.uclient.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 服客
 * @author Administrator
 *
 */
public class ServerDetail implements Serializable{
	
	private String name;//服客姓名
	private String icno;//身份证号		string
	private int age;	//年龄			int
	private String gender;//性别			string
	private String pic;	//身份证照		string
	private int total;//完成的服务次数		int
	private int raisecount;//评论数			int
	private String avgrate;//评分平均数		float
	private List<CustomDetail> customer;//评论的详情		array
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcno() {
		return icno;
	}
	public void setIcno(String icno) {
		this.icno = icno;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public List<CustomDetail> getCustomer() {
		return customer;
	}
	public void setCustomer(List<CustomDetail> customer) {
		this.customer = customer;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getRaisecount() {
		return raisecount;
	}
	public void setRaisecount(int raisecount) {
		this.raisecount = raisecount;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getAvgrate() {
		return avgrate;
	}
	public void setAvgrate(String avgrate) {
		this.avgrate = avgrate;
	}
	
	
	
	
}
