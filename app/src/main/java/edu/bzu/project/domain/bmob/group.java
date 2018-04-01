package edu.bzu.project.domain.bmob;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class group extends BmobObject implements Serializable{

	private String groupName =null;
	private BmobFile groupIcon =null;//Õº∆¨
	private String groupGeyan =null;//∏Ò—‘
	private Integer groupType;
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public BmobFile getGroupIcon() {
		return groupIcon;
	}
	public void setGroupIcon(BmobFile groupIcon) {
		this.groupIcon = groupIcon;
	}
	public String getGroupGeyan() {
		return groupGeyan;
	}
	public void setGroupGeyan(String groupGeyan) {
		this.groupGeyan = groupGeyan;
	}
	public Integer getGroupType() {
		return groupType;
	}
	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}
	
	
}
