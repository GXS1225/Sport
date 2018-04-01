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
 * ��λ
 *
 */
public class MapLocalUtils {
	/** ��λ*/
	public static LocationClient startLocation(BDLocationListener Listener) {
		//��λ
		LocationClient mLocClient;
		BDLocationListener myListener = Listener;
		LocationMode mCurrentMode=LocationMode.FOLLOWING;
	// ��λ��ʼ��
		mLocClient = new LocationClient(MyApplication.getInstance().getApplicationContext());
		mLocClient.registerLocationListener(myListener);
		
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		mLocClient.setLocOption(option);
		mLocClient.start();
		return mLocClient;
	}
}
