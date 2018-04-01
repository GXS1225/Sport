package edu.bzu.project.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.litepal.crud.DataSupport;

import cn.bmob.v3.BmobUser;

import edu.bzu.project.domain.Userinfo;
import edu.bzu.project.domain.bmob.User;
/**
 * �˶����㹤����
 *
 */
public class SportUtils {
	private static double  pace=0;
	
	/**
	 * ����ģʽ,��ȡ����  ����ɹ���
	 * 
	 * */
	public static double getInstantPace(){
		if(pace == 0){
			pace =getPace()/100000;
		}
		return pace;
	}
	/**
	 * ������߼��㲽��
	 * 
	 * */
	public static double getPace(){
		/**��������ߵĹ�ʽ
			�˵ĳ�ų�Լ����ߵ�1/7
			��������166cm���ϵ�һ��Ϊ�߸�
			���=������+1/3�㼣��
			��������148cm--166cm���ϵ�һ��Ϊ�и�
			���=������+1/2�㼣��
			��������140cm���µ�һ��Ϊ����
			���=������+2/3�㼣��*/
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
	/**ת��Ϊ С������λ km*/
	public static double getFormatKM(double km){
		DecimalFormat format =new DecimalFormat("0.00");
		String str =format.format(km);
		return Double.parseDouble(str);
	}
	/**��ת ǧ�� ����1λ*/
	public static double getDistanceByPace(int bushu){
		DecimalFormat   fnum  =   new  DecimalFormat("0.0");
		//��ȡ�û��Ĳ���
		String str =fnum.format((bushu*getInstantPace()));
		return Double.parseDouble(str);
	}
	//��תΪ ǧ��
	public static double getKM(double mi){
		DecimalFormat   fnum  =   new  DecimalFormat("0.0");
		String str =fnum.format(mi*0.001);
		return Double.parseDouble(str);
		
	}
	/**���Ӽ���ǧ��
	 * ����  һ����4.25
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
	/**���ؾ��� ���㿨��·
	 * ����  1.53
	 * �ﳵ  0.8
	 * �ܲ�  1.036
	 * */
	public static double getKLL(double juli,SportType type){
		DecimalFormat   fnum  =   new  DecimalFormat("0.0");
		String str = null;
		int weight =DataSupport.find(Userinfo.class, 1).getWeight();//��ȡ����
		if(weight>0 && weight<200){
			BigDecimal b1 =new BigDecimal(String.valueOf(weight*juli));
			BigDecimal b2 =null;
			switch (type.getValue()) {
			
			case 4:
				 b2 =new BigDecimal("1.53");
				//����
				break;
			case 3:
				 b2 =new BigDecimal("0.8");
				//�ﳵ
				break;
			case 2:
				 b2 =new BigDecimal("1.036");
				//�ܲ�
				break;
			}
			str =fnum.format(b1.multiply(b2));
		}else{
			str ="70";
		}
		return Double.parseDouble(str);
	}
	
	/**�� --�� ����*/
	public static int getMinuteBySecond(float miao){
		return (int)miao/60;
	
	}
}
