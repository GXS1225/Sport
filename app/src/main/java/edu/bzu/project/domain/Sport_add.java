package edu.bzu.project.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.litepal.crud.DataSupport;

public class Sport_add extends DataSupport{
	private int id;
	private String riqi;//日期
	private double weight;//体重
	private String text;//心得
	
	//照片
	private String photo1;
	private String photo2;
	private String photo3;
	
	private Date time;
	private List<Sport_item> items =new ArrayList<Sport_item>();//运动集合
	
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
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<Sport_item> getItems() {
		return DataSupport.where("sport_add_id = ?", String.valueOf(id)).find(Sport_item.class);
	}
	public void setItems(List<Sport_item> items) {
		this.items = items;
	}
	public String getPhoto1() {
		return photo1;
	}
	public void setPhoto1(String photo1) {
		this.photo1 = photo1;
	}
	public String getPhoto2() {
		return photo2;
	}
	public void setPhoto2(String photo2) {
		this.photo2 = photo2;
	}
	public String getPhoto3() {
		return photo3;
	}
	public void setPhoto3(String photo3) {
		this.photo3 = photo3;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	
	
}
