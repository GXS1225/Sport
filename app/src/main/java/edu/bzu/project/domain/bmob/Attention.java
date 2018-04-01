package edu.bzu.project.domain.bmob;

import cn.bmob.v3.BmobObject;

public class Attention extends BmobObject{

	private User user =null;//一个关注 属于一个用户
	private User user_=null;//一个关注只能只关注一个用户
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public User getUser_() {
		return user_;
	}
	public void setUser_(User user_) {
		this.user_ = user_;
	}
	
}
