package edu.bzu.project.activity.map;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.platform.comapi.map.q;
import com.baidu.platform.comapi.map.s;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import edu.bzu.project.R;
import edu.bzu.project.domain.bmob.Map_domain;
import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.utils.ConstantValues;
import edu.bzu.project.utils.LogUtils;
import edu.bzu.project.utils.MyApplication;
import android.app.Activity;
import android.content.Context;
import android.location.GpsStatus.NmeaListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * 运动的展示清单
 * @author houenshuo
 *
 */
public class ShowInfoActivity extends Activity {

	private Map_domain domain =null;
	private Context context =null;
	
	//地图
	private MapStatusUpdate msu;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LocationMode mCurrentMode=LocationMode.FOLLOWING;
	private BitmapDescriptor mCurrentMarker;
	//标记
	private Marker marker_start, marker_end;
	private List<BmobGeoPoint> points =null;
	private List<LatLng> LatLngs =new ArrayList<LatLng>();
	
	//控件
	private Button show_back =null;
	private TextView mapinfo_juli=null; 
	private TextView mapinfo_time=null; 
	private TextView mapinfo_suduShow=null; 
	private TextView mapinfo_bushuShow=null; 
	private TextView mapinfo_qiankaShow=null; 
	
	private Button btn_baocun =null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context =this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_showmapinfo);
		//取出
		domain =(Map_domain) getIntent().getSerializableExtra("sportInfo");
		//初始化控件
		initView();
		//展示数据
		showDate();
		//
		initEvent();
		
	}
	private void initEvent() {
		// TODO Auto-generated method stub
		btn_baocun.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				domain.setUser(BmobUser.getCurrentUser(context, User.class));
				
				//提交到服务器
				domain.save(context, new SaveListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						//保存成功
						MyApplication.getInstance().removeActivity(ShowInfoActivity.this);
						finish();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
		show_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyApplication.getInstance().removeActivity(ShowInfoActivity.this);
				finish();
			}
		});
	}
	
	private void showDate() {
		// TODO Auto-generated method stub
		//显示地图的轨迹
		//画出运动的轨迹
		points =domain.getPoints();
		if(points !=null){
			for(int i =0 ;i<points.size();i++){
				LatLng latLng =new LatLng(points.get(i).getLatitude(), points.get(i).getLongitude());
				LatLngs.add(latLng);
				if(LatLngs.size()==1){//画起始图片
					DrewMarker(latLng, ConstantValues.MARKERT_START);
				}else if(LatLngs.size() == points.size()){//结束的图标
					DrewMarker(latLng, ConstantValues.MARKERT_END);
				}else{
					MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
					mBaiduMap.animateMapStatus(msu);
					OverlayOptions ooPolyline = new PolylineOptions()
					.width(10).color(0xff00ffff).points(LatLngs).zIndex(5);
					mBaiduMap.addOverlay(ooPolyline);
					LogUtils.i("正在花轨迹", i+"个");
				}
			}
		}
		//显示运动的结果
		mapinfo_juli.setText(String.valueOf(domain.getJuli()));
		mapinfo_time.setText(String.valueOf(domain.getSummiao()));
		mapinfo_suduShow.setText(String.valueOf(domain.getSudu()));
		mapinfo_bushuShow.setText(String.valueOf(domain.getJuli()));
		mapinfo_qiankaShow.setText(String.valueOf(domain.getQianka()));
//		//本次运动
//		String show_newinfo ="本次运动的类型"+domain.getMode()+
//				"--运动的时间"+domain.getSummiao()+
//				"--运动的距离"+domain.getJuli()+
//				"--运动的时速"+domain.getSudu()+
//				"--消耗的热量"+domain.getQianka();
//		sport_newinfo.setText(show_newinfo);
//		//运动的比较
//		//读取
//		User user =BmobUser.getCurrentUser(context, User.class);
//		BmobQuery<Map_domain> query1 =new BmobQuery<Map_domain>();
//		query1.addWhereEqualTo("user", user);
//		BmobQuery<Map_domain> query2 =new BmobQuery<Map_domain>();
//		query2.addWhereEqualTo("mode", domain.getMode());
//		List<BmobQuery<Map_domain>> queries =new ArrayList<BmobQuery<Map_domain>>();
//		queries.add(query1);
//		queries.add(query2);
//		BmobQuery<Map_domain> qBmobQuery =new BmobQuery<Map_domain>();
//		qBmobQuery.and(queries);
//		BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
//		qBmobQuery.addWhereLessThan("createdAt", date);
//		qBmobQuery.order("-createdAt");
//		qBmobQuery.setLimit(3);//读3天的数据
//		qBmobQuery.findObjects(context, new FindListener<Map_domain>() {
//			
//			public void onSuccess(List<Map_domain> lists) {
//				// TODO Auto-generated method stub
//				String show_oldinfo =null;
//				if(lists.size() == 0){
//					show_oldinfo="你第一次进行"+domain.getMode()+"运动，可喜可贺";
//				}else{
//					int i =0;
//					double sumQianka =0;
//					double sumJuli =0;
//					//取出来数据 比较之
//					for(Map_domain d:lists){
//						i++;
//						sumQianka+=domain.getQianka();
//						sumJuli+=domain.getJuli();
//					}
//				  show_oldinfo ="本次运动的类型"+domain.getMode()+
//						  "最近平均距离"+sumJuli/i+"最近平均消耗热量"
//						  +sumQianka/i;
//				}
//				sport_oldinfo.setText(show_oldinfo);
//			}
//			
//			@Override
//			public void onError(int arg0, String arg1) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
	}
	private void initView() {
		// TODO Auto-generated method stub
		show_back =(Button)findViewById(R.id.show_back);
		btn_baocun =(Button)findViewById(R.id.show_success);
		
		mapinfo_juli =(TextView)findViewById(R.id.mapinfo_juli);
		mapinfo_time =(TextView)findViewById(R.id.mapinfo_time);
		mapinfo_suduShow =(TextView)findViewById(R.id.mapinfo_suduShow);
		mapinfo_bushuShow =(TextView)findViewById(R.id.mapinfo_bushuShow);
		mapinfo_qiankaShow =(TextView)findViewById(R.id.mapinfo_qiankaShow);
		mMapView = (MapView) findViewById(R.id.mapview);
		initMap();
	}
	private void initMap() {
		// TODO Auto-generated method stub
		// 地图初始化
		mBaiduMap = mMapView.getMap();
		// 设置地图的缩放级别
		mBaiduMap.setMapStatus(MapStatusUpdateFactory
				.newMapStatus(new MapStatus.Builder().zoom(18).build()));
		//设置是否允许楼块效果
		 mBaiduMap.setBuildingsEnabled(true);
		 //	配置定位图层显示方式-自定义图标
		 mCurrentMarker = BitmapDescriptorFactory
					.fromResource(R.drawable.marker);
		 mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
					mCurrentMode, true, mCurrentMarker));
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
	}
	// 画Marker
	private void DrewMarker(LatLng latLng, int markertStart) {
		BitmapDescriptor start = BitmapDescriptorFactory
				.fromResource(R.drawable.v3_icon_qidian);
		BitmapDescriptor end = BitmapDescriptorFactory
				.fromResource(R.drawable.v3_icon_zhongdian);
		//起点
		if (markertStart == ConstantValues.MARKERT_START) {
			OverlayOptions startM = new MarkerOptions().position(latLng)
					.icon(start).zIndex(5);
			marker_start = (Marker) mBaiduMap.addOverlay(startM);
		} else {//终点
			OverlayOptions endM = new MarkerOptions().position(latLng)
					.icon(end).zIndex(5);
			marker_end = (Marker) mBaiduMap.addOverlay(endM);
		}
	}
}
