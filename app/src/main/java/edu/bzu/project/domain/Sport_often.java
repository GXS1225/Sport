package edu.bzu.project.domain;

import org.litepal.crud.DataSupport;
/**
 * ���� �˶� ���ݿ�ʵ����
 *
 */
public class Sport_often extends DataSupport{
	private int id;
	private String mode;
	private double heat;
	private int orderid ;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public double getHeat() {
		return heat;
	}
	public void setHeat(double heat) {
		this.heat = heat;
	}
	public int getOrderid() {
		return orderid;
	}
	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}
	
	
}
