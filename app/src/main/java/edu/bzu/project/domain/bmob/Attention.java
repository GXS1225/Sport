package edu.bzu.project.domain.bmob;

import cn.bmob.v3.BmobObject;

public class Attention extends BmobObject{

	private User user =null;//һ����ע ����һ���û�
	private User user_=null;//һ����עֻ��ֻ��עһ���û�
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
