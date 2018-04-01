package edu.bzu.project.utils;

import java.util.ArrayList;

import edu.bzu.project.domain.ImageItem;
/**
 * 项目中的一些静态标志
 *
 */
public class ConstantValues {

		/**fragment 索引*/
		// 主页fragment索引值
		public static final int HOME_FRAGMENT_INDEX = 0;
		// 小组fragment索引值
		public static final int GROUP_FRAGMENT_INDEX = 1;
		// 开始fragment索引值
		public static final int START_FRAGMENT_INDEX = 2;
		// 发现fragment索引值
		public static final int FIND_FRAGMENT_INDEX = 3;
		// 设置fragment索引值
		public static final int SETTING_FRAGMENT_INDEX =4;
		
		/**shard key*/
		public static final String isFirstIn="isFirstIn";//是否第一次登录
		public static final String is_Alarm ="is_Alarm";//定时
		public static final String BU_SHU="BU_SHU";//每天的步数
		public static final String HUO_YUE ="HUO_YUE";//每天的活跃度
		public static final String LIN_CHEN ="LIN_CHEN";//每天的日期
		public static final String First_Time ="First_Time";//第一次使用的时间
		public static final String USER_ICON ="USER_ICON";
		public static final String PK_TIME ="PK_TIME";//记录的pk时间
		
	   /**图片的集合*/
		public static int max = 0;
		public static ArrayList<ImageItem> tempSelectBitmap = new ArrayList<ImageItem>();  
		
		/**是否登录*/
		public static boolean is_login =false;
		
		/**map 相关*/
		public static final String BDKey = "P8cDGriwgFwEsRvQHMfQq50F";

		// 要用到的Boolean变量
		public static boolean isRecord = false;
		public static boolean isFirst = true;//第一次定位
		public static boolean isTrack = false;//回顾
		public static boolean isStop = true;//停止

		// Handler的一些Message参数
		public static final int MESSAGE_DRAWLINE = 0;
		public static final int MESSAGE_DRAWTRACK = 1;
		public static final int MESSAGE_TRACKOVER = 2;

		// 画Markert的一些常量
		public static final int MARKERT_START = 0;
		public static final int MARKERT_END = 1;
		
		//话题的一页的个数
		public static final int PAGE_SIZE =10;
}
