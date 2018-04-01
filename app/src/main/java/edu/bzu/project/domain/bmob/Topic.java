package edu.bzu.project.domain.bmob;

import java.util.ArrayList;
import java.util.List;

import com.bmob.BmobProFile;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class Topic extends BmobObject{


	private String topicContent =null;
	private List<BmobFile> topoicPhotos =null;
	private group group =null;//
	private User author= null;//
	private BmobRelation relation;
	
	public String getTopicContent() {
		return topicContent;
	}
	public void setTopicContent(String topicContent) {
		this.topicContent = topicContent;
	}
	
	
	public List<BmobFile> getTopoicPhotos() {
		return topoicPhotos;
	}
	public void setTopoicPhotos(List<BmobFile> topoicPhotos) {
		this.topoicPhotos = topoicPhotos;
	}
	public group getGroup() {
		return group;
	}
	public void setGroup(group group) {
		this.group = group;
	}
	
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	public BmobRelation getRelation() {
		return relation;
	}
	public void setRelation(BmobRelation relation) {
		this.relation = relation;
	}
	
	
}
