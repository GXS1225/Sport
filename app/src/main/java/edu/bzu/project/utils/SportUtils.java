package edu.bzu.project.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.litepal.crud.DataSupport;

import cn.bmob.v3.BmobUser;

import edu.bzu.project.domain.Userinfo;
import edu.bzu.project.domain.bmob.User;
/**
 * 运动换算工具类
 *
 */
public class SportUtils {
	private static double  pace=0;
	
	/**
	 * 单例模式,获取步长  换算成公里
	 * 
	 * */
	public static double getInstantPace(){
		if(pace == 0){
			pace =getPace()/100000;
		}
		return pace;
	}
	/**
	 * 根据身高计算步长
	 * 
	 * */
	public static double getPace(){
		/**步幅和身高的公式
			人的赤脚长约是身高的1/7
			单步长在166cm以上的一般为高个
			身高=单步长+1/3足迹长
			单步长在148cm--166cm以上的一般为中个
			身高=单步长+1/2足迹长
			单步长在140cm以下的一般为矮个
			身高=单步长+2/3足迹长*/
		double hight =0;
		Userinfo userinfo =DataSupport.find(Userinfo.class, 1);
		double pace = 0;
		if(userinfo != null){
			hight =userinfo.getHeight();
			
			if (hight>0) {
				BigDecimal b2 =new BigDecimal(hight);
				if(hight>175){
					BigDecimal b1 =new BigDecimal(1/21);
					pace =(b2.subtract(b2.multiply(b1)).doubleValue())/2;
				}else if(hight<=175 && hight>158){
					BigDecimal b1 =new BigDecimal(1/14);
					pace =(b2.subtract(b2.multiply(b1)).doubleValue())/2;
				}else if(hight<=158){
					BigDecimal b1 =new BigDecimal(2/21);
					pace =(b2.subtract(b2.multiply(b1)).doubleValue())/2;
				}
			}
		}else{
			pace =65; 
		}
		
		return pace;
	}
	/**转化为 小数点两位 km*/
	public static double getFormatKM(double km){
		DecimalFormat format =new DecimalFormat("0.00");
		String str =format.format(km);
		return Double.parseDouble(str);
	}
	/**米转 千米 保留1位*/
	public static double getDistanceByPace(int bushu){
		DecimalFormat   fnum  =   new  DecimalFormat("0.0");
		//获取用户的步长
		String str =fnum.format((bushu*getInstantPace()));
		return Double.parseDouble(str);
	}
	//米转为 千米
	public static double getKM(double mi){
		DecimalFormat   fnum  =   new  DecimalFormat("0.0");
		String str =fnum.format(mi*0.001);
		return Double.parseDouble(str);
		
	}
	/**分钟计算千卡
	 * 步行  一份钟4.25
	 * */
	public static double getCalorie(double fenzhong,SportType type){
		DecimalFormat   fnum  =   new  DecimalFormat("0.00");
		String str = null;
		switch (type.getValue()) {
			case 1:
				str =fnum.format(fenzhong*4.25);
				break;
//			case 2:
//				str =fnum.format(fenzhong*11.67);
//				break;
//			case 3:
//				str =fnum.format(fenzhong*4.0);
//				break;
		}
		return Double.parseDouble(str);
	}
	/**体重距离 计算卡里路
	 * 竞走  1.53
	 * 骑车  0.8
	 * 跑步  1.036
	 * */
	public static double getKLL(double juli,SportType type){
		DecimalFormat   fnum  =   new  DecimalFormat("0.0");
		String str = null;
		int weight =DataSupport.find(Userinfo.class, 1).getWeight();//获取体重
		if(weight>0 && weight<200){
			BigDecimal b1 =new BigDecimal(String.valueOf(weight*juli));
			BigDecimal b2 =null;
			switch (type.getValue()) {
			
			case 4:
				 b2 =new BigDecimal("1.53");
				//竞走
				break;
			case 3:
				 b2 =new BigDecimal("0.8");
				//骑车
				break;
			case 2:
				 b2 =new BigDecimal("1.036");
				//跑步
				break;
			}
			str =fnum.format(b1.multiply(b2));
		}else{
			str ="70";
		}
		return Double.parseDouble(str);
	}
	
	/**秒 --》 分钟*/
	public static int getMinuteBySecond(float miao){
		return (int)miao/60;
	
	}
}
