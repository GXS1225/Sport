package edu.bzu.project.domain.bmob;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

public class Pedometer_Server extends BmobObject{

	private User user;
	private int bushul;//����
	private int shijian;//��Ծ��:����
	private double qianka;//����
	private double meter;//����
	private int max;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
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
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
}
