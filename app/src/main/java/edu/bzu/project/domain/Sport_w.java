package edu.bzu.project.domain;

import org.litepal.crud.DataSupport;
/**
 * 外部数据库数据实体类(只用于封装数据)
 * 自定义数据库实体类
 *
 */
public class Sport_w extends DataSupport{
	
	private int id;
	private String mode;
	private double heat;
	
	public Sport_item item ;
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
	public Sport_item getItem() {
		return item;
	}
	public void setItem(Sport_item item) {
		this.item = item;
	}
	
	
	

}
