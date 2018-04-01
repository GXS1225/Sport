package edu.bzu.project.domain.bmob;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class health_domain extends BmobObject{

	private String name =null;
	private String content =null;
	private BmobFile photo =null;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public BmobFile getPhoto() {
		return photo;
	}
	public void setPhoto(BmobFile photo) {
		this.photo = photo;
	}
	
}
