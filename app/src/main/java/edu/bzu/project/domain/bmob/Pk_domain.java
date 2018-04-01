package edu.bzu.project.domain.bmob;

import cn.bmob.v3.BmobObject;

public class Pk_domain extends BmobObject {
	private User user =null;
	private int result;//1 0 ”Æ  ‰
	private User user_ =null;//∂‘∑Ω
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public User getUser_() {
		return user_;
	}
	public void setUser_(User user_) {
		this.user_ = user_;
	}
}
