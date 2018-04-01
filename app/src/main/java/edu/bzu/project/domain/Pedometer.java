package edu.bzu.project.domain;

import java.util.Date;

import org.litepal.crud.DataSupport;

public class Pedometer extends DataSupport {
	private int id;
	private String riqi;
	private Date time;
	private int bushul;//步数
	private int shijian;//活跃度:分钟
	private double qianka;//消耗
	private double meter;//公里
	private int max;
	
	
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getRiqi() {
		return riqi;
	}
	public void setRiqi(String riqi) {
		this.riqi = riqi;
	}
	public int getBushul() {
		return bushul;
	}
	public void setBushul(int bushul) {
		this.bushul = bushul;
	}
	public int getShijian() {
		return shijian;
	}
	public void setShijian(int shijian) {
		this.shijian = shijian;
	}
	public double getQianka() {
		return qianka;
	}
	public void setQianka(double qianka) {
		this.qianka = qianka;
	}
	public double getMeter() {
		return meter;
	}
	public void setMeter(double meter) {
		this.meter = meter;
	}
	
	

}
