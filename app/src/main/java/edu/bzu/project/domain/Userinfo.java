package edu.bzu.project.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.litepal.crud.DataSupport;

import edu.bzu.project.domain.bmob.User;
public class Userinfo extends DataSupport implements Serializable{
	private int id;
	private String sex;
	private int age;
	private double height;
	private int weight;
	private double max;
	private double ibm;
	
	public User author =null;
	
	
	public double getIbm() {
		return ibm;
	}
	public void setIbm(double ibm) {
		this.ibm = ibm;
	}
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}


}
