package edu.bzu.project.domain;

import java.io.Serializable;

import org.litepal.crud.DataSupport;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *选择的运动，数据库表
 *
 * */
public class Sport_item extends DataSupport implements Serializable{
	private int id;
	public int position;//位置
	private int type;//类型1，本地数据2常用数据3自定义数据
	private String name;//名称
	private double qianka_value;//千卡
	//本次的运动量
	private int hour;//小时
	private int minute;//分钟
	private double qianka;//千卡
	
	private Sport_add sportadd;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Sport_add getSportadd() {
		return sportadd;
	}
	public void setSportadd(Sport_add sportadd) {
		this.sportadd = sportadd;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public double getQianka() {
		return qianka;
	}
	public void setQianka(double qianka) {
		this.qianka = qianka;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getQianka_value() {
		return qianka_value;
	}
	public void setQianka_value(double qianka_value) {
		this.qianka_value = qianka_value;
	}
	 
	
}
