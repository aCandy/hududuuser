package com.jizhi.hududu.uclient.bean;

import java.io.Serializable;
/**
 * 帮我买菜
 * @author Xuj
 * @date 2015年8月26日 13:05:50
 */
public class BuyVegetables implements Serializable {
	private String name; // 名称
	private String pic; // 果蔬图片
	private String fhnum; // 唯一编号
	private double fee; // 参考价
	private int allNumber; // 总数
	private double allPrice; // 总价
	private String fhtype; // 所属类型 1、蔬菜 2、水果 3、水产 4、肉
	public BuyVegetables() {

	}

	public BuyVegetables(String fhtype) {
		this.fhtype = fhtype;
	}

	public BuyVegetables(String name, String pic, String fhnum, double fee) {
		super();
		this.name = name;
		this.pic = pic;
		this.fhnum = fhnum;
		this.fee = fee;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getFhnum() {
		return fhnum;
	}

	public void setFhnum(String fhnum) {
		this.fhnum = fhnum;
	}

	public int getAllNumber() {
		return allNumber;
	}

	public void setAllNumber(int allNumber) {
		this.allNumber = allNumber;
	}

	public String getFhtype() {
		return fhtype;
	}

	public void setFhtype(String fhtype) {
		this.fhtype = fhtype;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public double getAllPrice() {
		return allPrice;
	}

	public void setAllPrice(double allPrice) {
		this.allPrice = allPrice;
	}

	@Override
	public String toString() {
		return "BuyVegetables [name=" + name + ", pic=" + pic + ", fhnum="
				+ fhnum + ", fee=" + fee + ", allNumber=" + allNumber + "]";
	}

}
