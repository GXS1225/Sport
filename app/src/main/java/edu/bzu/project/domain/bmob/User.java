package edu.bzu.project.domain.bmob;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import edu.bzu.project.domain.Userinfo;
/**
 * 记录用户首次使用本软件的（身高，体重，年龄）信息
 */
@SuppressWarnings("serial")
public class User extends BmobUser {
	
	private String userName =null;//昵称
	private BmobFile userPhoto =null;
	private BmobGeoPoint weizhi =null;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public BmobFile getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(BmobFile userPhoto) {
		this.userPhoto = userPhoto;
	}
	public BmobGeoPoint getWeizhi() {
		return weizhi;
	}
	public void setWeizhi(BmobGeoPoint weizhi) {
		this.weizhi = weizhi;
	}
	
	
}
