package com.jizhi.hududu.uclient.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 吃饭了
 * @author Xuj
 */
public class Dinner implements Serializable {
	private String name; // 服客姓名
	private double[] location; // 服客当前坐标
	private int ordertotal; // 服客完成订单数
	private String pic; // 服客头像
	private String icno; // 服客身份证
	private int age; // 服客年龄
	private float avgrate; // 服客获得评价平均分
	private List<Dishes> bestcooking; // 服客拿手菜 只当loadpic参数值为yes时才会返回
	private String lid; // 服客id
	private double far;
	private String names;
	private String[] beseCookingData = new String[2];
	
	
	
	private boolean isChoose = false;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public float getAvgrate() {
		return avgrate;
	}

	public void setAvgrate(float avgrate) {
		this.avgrate = avgrate;
	}

	public String getLid() {
		return lid;
	}

	public void setLid(String lid) {
		this.lid = lid;
	}

	public double[] getLocation() {
		return location;
	}

	public void setLocation(double[] location) {
		this.location = location;
	}

	public List<Dishes> getBestcooking() {
		return bestcooking;
	}

	public void setBestcooking(List<Dishes> bestcooking) {
		this.bestcooking = bestcooking;
	}
	
	
	public String getNames(){
		StringBuffer sb = new StringBuffer();
		if(bestcooking!=null && bestcooking.size()>0){
			for (int i = 0; i < bestcooking.size(); i++) {
				if(i<3){
					if(i == 0){
						sb.append(bestcooking.get(i).getDishname());
					}else{
						sb.append("、"+bestcooking.get(i).getDishname());
					}
				}
			}
		}
		return sb.toString();
	}

	public boolean isChoose() {
		return isChoose;
	}

	public void setChoose(boolean isChoose) {
		this.isChoose = isChoose;
	}

	public double getFar() {
		return far;
	}

	public void setFar(double far) {
		this.far = far;
	}
	
	
	
	public void setBeseCookingData(){
		StringBuffer sbPic = new StringBuffer();
		StringBuffer sbName = new StringBuffer();
		if(bestcooking!=null && bestcooking.size()>0){
			for (int i = 0; i < bestcooking.size(); i++) {
				Dishes bean = bestcooking.get(i);
				if(i == 0){
					sbName.append(bean.getDishname());
					sbPic.append(bean.getDishpic());
				}else{
					sbName.append(","+bean.getDishname());
					sbPic.append(","+bean.getDishpic());
				}
			}
		}
		beseCookingData[0] = sbPic.toString();
		beseCookingData[1] = sbName.toString();
	}

	public String[] getBeseCookingData() {
		return beseCookingData;
	}


}
