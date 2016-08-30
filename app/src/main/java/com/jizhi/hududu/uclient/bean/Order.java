package com.jizhi.hududu.uclient.bean;

import java.io.Serializable;
import java.util.List;
/**
 * 订单
 * @author Xuj
 * @date 2015年8月26日 13:06:46
 */
public class Order implements Serializable {

	private String project_name;
	private String oid; //订单id
	private String cid; //用户id
	private int job; //工作内容
	private String address; //订单地址
	private String pic; //服客头像
	private String create_time; //订单创建时间
	private String status;
	private String lname; //服客名字
	private String location; //订单坐标
	private String wtype;
	private String cname; //用户名
	private String cpic; //用户头像
	private String working_hours;//服务时长
	private String fhpic; //图片
	private String lid; //服客电话号码
	private int server_time; 
	private String desc;
	private String wage; //订单总价
	private String payway; //支付方式
	private long receive_time; //完成时间
	private String realpay; //实际付款
	private String unit;
	private String dishcount; //做菜个数
	private String anonymous = "h";
	
	private List<String> caiPic;
	
	
	private int weight; //重量
	
	private String working_hours_unit;
	
	public int getJob() {
		return job;
	}
	public void setJob(int job) {
		this.job = job;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getWtype() {
		return wtype;
	}
	public void setWtype(String wtype) {
		this.wtype = wtype;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getCpic() {
		return cpic;
	}
	public void setCpic(String cpic) {
		this.cpic = cpic;
	}
	public String getWorking_hours() {
		return working_hours;
	}
	public void setWorking_hours(String working_hours) {
		this.working_hours = working_hours;
	}
	public int getServer_time() {
		return server_time;
	}
	public void setServer_time(int server_time) {
		this.server_time = server_time;
	}
	public String getWage() {
		return wage;
	}
	public void setWage(String wage) {
		this.wage = wage;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getLid() {
		return lid;
	}
	public void setLid(String lid) {
		this.lid = lid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFhpic() {
		return fhpic;
	}
	public void setFhpic(String fhpic) {
		this.fhpic = fhpic;
	}
	public String getPayway() {
		return payway;
	}
	public void setPayway(String payway) {
		this.payway = payway;
	}
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getWorking_hours_unit() {
		return working_hours_unit;
	}
	public void setWorking_hours_unit(String working_hours_unit) {
		this.working_hours_unit = working_hours_unit;
	}
	public String getRealpay() {
		return realpay;
	}
	public void setRealpay(String realpay) {
		this.realpay = realpay;
	}
	public long getReceive_time() {
		return receive_time;
	}
	public void setReceive_time(long receive_time) {
		this.receive_time = receive_time;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public List<String> getCaiPic() {
		return caiPic;
	}
	public void setCaiPic(List<String> caiPic) {
		this.caiPic = caiPic;
	}
	public String getDishcount() {
		return dishcount;
	}
	public void setDishcount(String dishcount) {
		this.dishcount = dishcount;
	}
	public String getAnonymous() {
		return anonymous;
	}
	public void setAnonymous(String anonymous) {
		this.anonymous = anonymous;
	}

	
	
	
	
	
}
