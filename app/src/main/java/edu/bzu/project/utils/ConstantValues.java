package edu.bzu.project.utils;

import java.util.ArrayList;

import edu.bzu.project.domain.ImageItem;
/**
 * ��Ŀ�е�һЩ��̬��־
 *
 */
public class ConstantValues {

		/**fragment ����*/
		// ��ҳfragment����ֵ
		public static final int HOME_FRAGMENT_INDEX = 0;
		// С��fragment����ֵ
		public static final int GROUP_FRAGMENT_INDEX = 1;
		// ��ʼfragment����ֵ
		public static final int START_FRAGMENT_INDEX = 2;
		// ����fragment����ֵ
		public static final int FIND_FRAGMENT_INDEX = 3;
		// ����fragment����ֵ
		public static final int SETTING_FRAGMENT_INDEX =4;
		
		/**shard key*/
		public static final String isFirstIn="isFirstIn";//�Ƿ��һ�ε�¼
		public static final String is_Alarm ="is_Alarm";//��ʱ
		public static final String BU_SHU="BU_SHU";//ÿ��Ĳ���
		public static final String HUO_YUE ="HUO_YUE";//ÿ��Ļ�Ծ��
		public static final String LIN_CHEN ="LIN_CHEN";//ÿ�������
		public static final String First_Time ="First_Time";//��һ��ʹ�õ�ʱ��
		public static final String USER_ICON ="USER_ICON";
		public static final String PK_TIME ="PK_TIME";//��¼��pkʱ��
		
	   /**ͼƬ�ļ���*/
		public static int max = 0;
		public static ArrayList<ImageItem> tempSelectBitmap = new ArrayList<ImageItem>();  
		
		/**�Ƿ��¼*/
		public static boolean is_login =false;
		
		/**map ���*/
		public static final String BDKey = "P8cDGriwgFwEsRvQHMfQq50F";

		// Ҫ�õ���Boolean����
		public static boolean isRecord = false;
		public static boolean isFirst = true;//��һ�ζ�λ
		public static boolean isTrack = false;//�ع�
		public static boolean isStop = true;//ֹͣ

		// Handler��һЩMessage����
		public static final int MESSAGE_DRAWLINE = 0;
		public static final int MESSAGE_DRAWTRACK = 1;
		public static final int MESSAGE_TRACKOVER = 2;

		// ��Markert��һЩ����
		public static final int MARKERT_START = 0;
		public static final int MARKERT_END = 1;
		
		//�����һҳ�ĸ���
		public static final int PAGE_SIZE =10;
}
