package edu.bzu.project.domain.bmob;

import cn.bmob.v3.BmobObject;

public class PrivateInfo extends BmobObject{

	private User user;//一个隐私 对应一个表
	
	private String hight;//身高
	
	private String wight;//体重
	
	private String qq;//qq号
	
	private String phone;
	//private String birthday;//生日
	
	//private boolean isMate ;//
	
	//private String photo;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getHight() {
		return hight;
	}

	public void setHight(String hight) {
		this.hight = hight;
	}

	public String getWight() {
		return wight;
	}

	public void setWight(String wight) {
		this.wight = wight;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	
	
}
