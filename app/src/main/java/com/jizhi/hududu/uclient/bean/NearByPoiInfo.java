package com.jizhi.hududu.uclient.bean;

import java.io.Serializable;

public class NearByPoiInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String address;
	private String name;
	private double longitude;// 经度
	private double latitude;// 纬度

	public NearByPoiInfo(String address, String name, double longitude,
			double latitude) {
		super();
		this.address = address;
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
	}

//	public NearByPoiInfo() {
//
//	}
//
//	public static final Parcelable.Creator<NearByPoiInfo> CREATOR = new Parcelable.Creator<NearByPoiInfo>() {
//
//		@Override
//		public NearByPoiInfo createFromParcel(Parcel source) {
//
//			NearByPoiInfo model = new NearByPoiInfo();
//			model.address = source.readString();
//			model.name = source.readString();
//			model.longitude = source.readInt();
//			model.latitude = source.readInt();
//			return model;
//		}
//
//		@Override
//		public NearByPoiInfo[] newArray(int size) {
//			return new NearByPoiInfo[size];
//		}
//	};
//
//	@Override
//	public int describeContents() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeString(address);
//		dest.writeString(name);
//		dest.writeDouble(longitude);
//		dest.writeDouble(latitude);
//	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

}
