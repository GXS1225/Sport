package edu.bzu.project.domain.bmob;

import java.io.Serializable;
import java.util.List;

import org.litepal.crud.DataSupport;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;


public class Map_domain extends BmobObject implements Serializable{
	private User user;
	private String time;//日期
	private int summiao;//运动的秒数
	private double qianka;
	private double juli;
	private double sudu;
	
	private List<BmobGeoPoint> points =null;
	
	private String mode;//运动模式
	private String title;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	public double getQianka() {
		return qianka;
	}

	public void setQianka(double qianka) {
		this.qianka = qianka;
	}

	public double getJuli() {
		return juli;
	}

	public void setJuli(double juli) {
		this.juli = juli;
	}

	public double getSudu() {
		return sudu;
	}

	public void setSudu(double sudu) {
		this.sudu = sudu;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getSummiao() {
		return summiao;
	}

	public void setSummiao(int summiao) {
		this.summiao = summiao;
	}

	public List<BmobGeoPoint> getPoints() {
		return points;
	}

	public void setPoints(List<BmobGeoPoint> points) {
		this.points = points;
	}
	
	
}
