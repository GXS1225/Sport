package edu.bzu.project.utils;
/**
 * 运动类型的枚举类
 *
 */
public enum SportType{
	 BU_XING(1),
	 PAO_BU(2),
	 QI_CHE(3),
	 JING_ZOU(4);

	private int value;

	SportType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	
		
		
}
