package edu.bzu.project.utils;

import java.util.ArrayList;

import android.os.SystemClock;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;

import edu.bzu.project.activity.map.BMapActivity.MyLocationListenner;
/**
 * 
 * 定位
 *
 */
public class MapLocalUtils {
	/** 定位*/
	public static LocationClient startLocation(BDLocationListener Listener) {
		//定位
		LocationClient mLocClient;
		BDLocationListener myListener = Listener;
		LocationMode mCurrentMode=LocationMode.FOLLOWING;
	// 定位初始化
		mLocClient = new LocationClient(MyApplication.getInstance().getApplicationContext());
		mLocClient.registerLocationListener(myListener);
		
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		mLocClient.setLocOption(option);
		mLocClient.start();
		return mLocClient;
	}
}
