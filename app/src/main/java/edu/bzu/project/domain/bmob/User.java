package edu.bzu.project.domain.bmob;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import edu.bzu.project.domain.Userinfo;
/**
 * ��¼�û��״�ʹ�ñ�����ģ���ߣ����أ����䣩��Ϣ
 */
@SuppressWarnings("serial")
public class User extends BmobUser {
	
	private String userName =null;//�ǳ�
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
