package edu.bzu.project.domain;

import edu.bzu.project.domain.bmob.User;

public class PaiHang_domain implements Comparable<PaiHang_domain>{

	private User user;//用户
	private double sumScore;//总分数
	
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public double getSumScore() {
		return sumScore;
	}
	public void setSumScore(double sumScore) {
		this.sumScore = sumScore;
	}
	@Override
	public int compareTo(PaiHang_domain another) {
		// TODO Auto-generated method stub
		if(this.sumScore >another.getSumScore()){
			return -1;
		}else if(this.sumScore == another.getSumScore()){
			return 0;
		}
		return 1;
	}
	
}
