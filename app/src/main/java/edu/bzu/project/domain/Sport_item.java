package edu.bzu.project.domain;

import java.io.Serializable;

import org.litepal.crud.DataSupport;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *ѡ����˶������ݿ��
 *
 * */
public class Sport_item extends DataSupport implements Serializable{
	private int id;
	public int position;//λ��
	private int type;//����1����������2��������3�Զ�������
	private String name;//����
	private double qianka_value;//ǧ��
	//���ε��˶���
	private int hour;//Сʱ
	private int minute;//����
	private double qianka;//ǧ��
	
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
