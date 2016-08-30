package com.jizhi.hududu.uclient.bean;

import java.io.Serializable;

/**
 * ( 基本菜品信息
 * 
 * @author huChangSheng
 * @time 2015-8-24 下午4:17:01
 * @version 1.0
 * 
 */
public class VegetableInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String pic;
	private double price;
	private double count;
	private double allPrice;
	private String num;
	
	

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public double getAllPrice() {
		return allPrice;
	}

	public void setAllPrice(double allPrice) {
		this.allPrice = allPrice;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

}
