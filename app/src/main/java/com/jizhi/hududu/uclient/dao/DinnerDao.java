package com.jizhi.hududu.uclient.dao;

import java.util.ArrayList;
import java.util.List;

import com.jizhi.hududu.uclient.bean.Dinner;
import com.jizhi.hududu.uclient.bean.Dishes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class DinnerDao {
	
	public String tableName = "t_dinner";
	
	DBOpenHelper dbopenhelder = null;
	
	
	public DinnerDao(Context context){
		this.dbopenhelder =  DBOpenHelper.getInstance(context);
	}
	
	public static final String COLUMN_NAME_ID = "_id";
	public static final String COLUMN_NAME_ORDERTOTAL = "ordertotal";// 服客完成订单数
	public static final String COLUMN_NAME_PIC = "pic";// 服客头像
	public static final String COLUMN_NAME_ICON = "icno";  // 服客身份证
	public static final String COLUMN_NAME_AGE = "age";// 服客年龄
	public static final String COLUMN_NAME_AVGRATE = "avgrate";// 服客获得评价平均分
	public static final String COLUMN_NAME_BESTCOOKING_PIC = "bestcooking_pic";
	public static final String COLUMN_NAME_BESTCOOKING_NAME = "bestcooking_name";
	public static final String COLUMN_NAME_LID = "lid";// 服客id
	public static final String COLUMN_NAME_FAR = "far";// 相隔距离
	
	
	
	/**
	 * 是否存在数据
	 * @return
	 */
	public boolean exis(){
		SQLiteDatabase db = dbopenhelder.getWritableDatabase();
		Cursor cur = db.rawQuery("select * from "+tableName+"", new String[]{});
		if(cur!=null){
			if(cur.moveToNext()){
				cur.close();
				return true;
			}
		}
		cur.close();
		return false;
	}
	
	public void delete(){
		SQLiteDatabase db = dbopenhelder.getWritableDatabase();
		db.execSQL("delete from "+tableName);
		
	}
	
	/**
	 * 保存厨师
	 * @param list
	 */
	public void save(List<Dinner> list){
		if(exis()){
			delete();
		}
		SQLiteDatabase db = dbopenhelder.getWritableDatabase();
		for(Dinner bean:list){
			ContentValues cvs = new ContentValues();
			cvs.put("ordertotal",bean.getOrdertotal());
			cvs.put("name",bean.getName());
			cvs.put("pic", bean.getPic());
			cvs.put("icno",bean.getIcno());
			cvs.put("age", bean.getAge());
			cvs.put("avgrate",bean.getAvgrate());
			cvs.put("bestcooking_pic",bean.getBeseCookingData()[0]);
			cvs.put("bestcooking_name",bean.getBeseCookingData()[1]);
			cvs.put("lid", bean.getLid());
			cvs.put("far", bean.getFar());
			cvs.put("choose","0");
			db.insert(tableName, null, cvs);
		}
	}
	
	
	
	
	
	
	
	/**
	 * 变更选择状态
	 * @param noticeId
	 * @return
	 */
	public void updateStatus(String lid,boolean state){
		SQLiteDatabase db = dbopenhelder.getWritableDatabase();
		if(state){
			String update = "update "+tableName+" set choose=? where lid=?";
			db.execSQL(update,new Object[]{new String("1"),new String(lid)});
		}else{
			String update = "update "+tableName+" set choose=? ";
			db.execSQL(update,new Object[]{new String("0")});
		}
	}
	
	
	
	
	
	/**
	 * 根据最近的服客排序
	 * @param noticeId
	 * @return
	 */
	public List<Dinner> byFarOrder(){
		List<Dinner> list = null;
		SQLiteDatabase db = dbopenhelder.getWritableDatabase();
		Cursor cur = db.rawQuery("select pic,name,far,avgrate,icno,lid,bestcooking_name,bestcooking_pic,age,choose from "+tableName+" where 1=1 order by far asc", new String[]{});
		if(cur!=null){
			list = new ArrayList<Dinner>();
			while(cur.moveToNext()){
				Dinner dinner = new Dinner();
				dinner.setPic(cur.getString(0));
				dinner.setName(cur.getString(1));
				dinner.setFar(cur.getDouble(2));
				dinner.setAvgrate(cur.getFloat(3));
				dinner.setIcno(cur.getString(4));
				dinner.setLid(cur.getString(5));
				List<Dishes> tempList = new ArrayList<Dishes>();
				String[] dishname = cur.getString(6).split(",");
				String[] dishpic = cur.getString(7).split(",");
				if(!cur.getString(6).equals("蛋花,啊哈,煎蛋")){
					for(int i = 0;i<dishname.length;i++){
						Dishes bean = new Dishes();
						bean.setDishname(dishname[i]);
						bean.setDishpic(dishpic[i]);
						tempList.add(bean);
					}
				}
				dinner.setBestcooking(tempList);
				dinner.setAge(cur.getInt(8));
				if(cur.getString(9).equals("0")){
					dinner.setChoose(false);
				}else{
					dinner.setChoose(true);
				}
				list.add(dinner);
			}
			cur.close();
		}
		return list;
	}
	
	
	/**
	 * 根据星级排序服客
	 * @param noticeId
	 * @return
	 */
	public List<Dinner> byAvgrateOrder(){
		List<Dinner> list = null;
		SQLiteDatabase db = dbopenhelder.getWritableDatabase();
		Cursor cur = db.rawQuery("select pic,name,far,avgrate,icno,lid,bestcooking_name,bestcooking_pic,age,choose from "+tableName+" where 1=1 order by avgrate desc", new String[]{});
		if(cur!=null){
			list = new ArrayList<Dinner>();
			while(cur.moveToNext()){
				Dinner dinner = new Dinner();
				dinner.setPic(cur.getString(0));
				dinner.setName(cur.getString(1));
				dinner.setFar(cur.getDouble(2));
				dinner.setAvgrate(cur.getFloat(3));
				dinner.setIcno(cur.getString(4));
				dinner.setLid(cur.getString(5));
				List<Dishes> tempList = new ArrayList<Dishes>();
				String[] dishname = cur.getString(6).split(",");
				String[] dishpic = cur.getString(7).split(",");
				if(!cur.getString(6).equals("蛋花,啊哈,煎蛋")){
					for(int i = 0;i<dishname.length;i++){
						Dishes bean = new Dishes();
						bean.setDishname(dishname[i]);
						bean.setDishpic(dishpic[i]);
						tempList.add(bean);
					}
				}
				dinner.setBestcooking(tempList);
				dinner.setAge(cur.getInt(8));
				if(cur.getString(9).equals("0")){
					dinner.setChoose(false);
				}else{
					dinner.setChoose(true);
				}
				list.add(dinner);
			}
			cur.close();
		}
		return list;
	}
	
	
	
	/**
	 * 查询服客
	 * @param 
	 * @return
	 */
	public List<Dinner> query(){
		List<Dinner> list = null;
		SQLiteDatabase db = dbopenhelder.getWritableDatabase();
		Cursor cur = db.rawQuery("select pic,name,far,avgrate,icno,lid,bestcooking_name,bestcooking_pic,age,choose from "+tableName+"", new String[]{});
		if(cur!=null){
			list = new ArrayList<Dinner>();
			while(cur.moveToNext()){
				Dinner dinner = new Dinner();
				dinner.setPic(cur.getString(0));
				dinner.setName(cur.getString(1));
				dinner.setFar(cur.getDouble(2));
				dinner.setAvgrate(cur.getFloat(3));
				dinner.setIcno(cur.getString(4));
				dinner.setLid(cur.getString(5));
				List<Dishes> tempList = new ArrayList<Dishes>();
				String[] dishname = cur.getString(6).split(",");
				String[] dishpic = cur.getString(7).split(",");
				if(!cur.getString(6).equals("蛋花,啊哈,煎蛋")){
					for(int i = 0;i<dishname.length;i++){
						Dishes bean = new Dishes();
						bean.setDishname(dishname[i]);
						bean.setDishpic(dishpic[i]);
						tempList.add(bean);
					}
				}
				dinner.setBestcooking(tempList);
				dinner.setAge(cur.getInt(8));
				if(cur.getString(9).equals("0")){
					dinner.setChoose(false);
				}else{
					dinner.setChoose(true);
				}
				list.add(dinner);
			}
			cur.close();
		}
		return list;
	}
	
	
}
