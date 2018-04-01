package edu.bzu.project.activity.map;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.datatype.BmobGeoPoint;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
import com.baidu.mapapi.utils.DistanceUtil;

import edu.bzu.project.R;

import edu.bzu.project.activity.setting.AddExercise_Acivity;
import edu.bzu.project.domain.bmob.Map_domain;
import edu.bzu.project.service.StepService;
import edu.bzu.project.utils.CommonUtils;
import edu.bzu.project.utils.ConstantValues;
import edu.bzu.project.utils.DateStyle;
import edu.bzu.project.utils.DateUtils;
import edu.bzu.project.utils.LogUtils;
import edu.bzu.project.utils.MyApplication;
import edu.bzu.project.utils.SPUtils;
import edu.bzu.project.utils.SportType;
import edu.bzu.project.utils.SportUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
/**
 * ��ͼ�˶�ҳ��
 * @author houenshuo
 *
 */
public class BMapActivity extends Activity {
	
	//��λ
	private LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode=LocationMode.FOLLOWING;
	private BitmapDescriptor mCurrentMarker;
	//��ͼ
	private MapStatusUpdate msu;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	//���
	private Marker marker_start, marker_end;
	//����
	private String mode =null;
	private TextView map_title =null;
	private Button map_jiesuo =null;
	private Button map_btn_jixu =null;
	private Button map_btn_jieshu =null;
	private Chronometer chronometer =null;
	
	private TextView map_suduShow =null;
	private TextView map_juliShow =null;
	private TextView map_qiankaShow =null;
	private long timeCha ;
	private boolean isChronometer =true;
	private boolean isStop =false;
	//����������
	
	double distanceSum =0.0;
	// �켣���
	private List<LatLng> latlngs = null;
	private List<BmobGeoPoint> points =null;
	private int count = 0, count_point = 0;// ��λ�㼯�ϵĴ�С
	
	private AlertDialog dialog =null;
	private TextView dialog_title =null;
	private Button bc =null;
	private Button bbc =null;
	private Button qx =null;
	
	private int miao =0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplication.getInstance().addActivity(BMapActivity.this);
		setContentView(R.layout.activity_bmap);
		//�ر� �Ʋ�
		if(StepService.FLAG){
			Intent intent =new Intent(BMapActivity.this, StepService.class);
			stopService(intent);
		}
		initView();
	//��ͼ�ĳ�ʼ��
		initMap();
	//��λ�ĳ�ʼ��
		location();
	
		initEvent();
	}
	private void initEvent() {
		// TODO Auto-generated method stub
		//����
		map_btn_jieshu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				map_title.setText(mode+"������");
				//��������,���ֶԻ���
				View v1 =View.inflate(BMapActivity.this, R.layout.dialog_back, null);
				dialog =CommonUtils.getAlertDialog(BMapActivity.this, v1);
				dialog_title =(TextView)v1.findViewById(R.id.sport_title);
				bbc =(Button)v1.findViewById(R.id.dialog_bbc);
				bc =(Button)v1.findViewById(R.id.dialog_bc);
				qx =(Button)v1.findViewById(R.id.dialog_qx);
				//�ж����ݵĶ���
				if(distanceSum>=0){//
					dialog_title.setVisibility(View.VISIBLE);
					dialog_title.setText("��ȷ���Ѿ��������𣿱��潫�ϴ�������");
				}else{//���о���̫��
					dialog_title.setVisibility(View.VISIBLE);
					bc.setVisibility(View.GONE);
				}
				bbc.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						MyApplication.getInstance().removeActivity(BMapActivity.this);
						finish();
					}
				});
				//����
				bc.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(latlngs!=null && latlngs.size()>0 && distanceSum>=0){
							//���� �˶���
							Map_domain domain =new Map_domain();
							domain.setJuli(SportUtils.getKM(distanceSum));
							domain.setQianka(Double.parseDouble(map_qiankaShow.getText().toString()));
							domain.setMode(mode);
							double sudu =distanceSum / miao;//�������ʱ��
							//domain.setSport_time(chronometer.getFormat());
							domain.setSudu(SportUtils.getFormatKM(sudu));
							domain.setSummiao(miao);
							domain.setPoints(points);
							//��ǰ��ʱ��
							String now_time =DateUtils.DateToString(Calendar.getInstance().getTime(), DateStyle.YYYY_MM_DD_HH_MM_CN);
							domain.setTime(now_time);
							Intent intent =new Intent(BMapActivity.this,ShowInfoActivity.class);
							intent.putExtra("sportInfo", domain);
							startActivity(intent);
							
							//�ر�
							dialog.dismiss();
							MyApplication.getInstance().removeActivity(BMapActivity.this);
							finish();
						}
					}
				});
				qx.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
			}
		});
		//����
		map_btn_jixu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				map_jiesuo.setVisibility(View.VISIBLE);
				map_btn_jieshu.setVisibility(View.GONE);
				map_btn_jixu.setVisibility(View.GONE);
				
				isStop =false;
				map_title.setText(mode+"���ڽ���");
				chronometer.setBase(SystemClock.elapsedRealtime()-timeCha);
				chronometer.start();
			}
		});
		//������ͣ
		map_jiesuo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				map_jiesuo.setVisibility(View.GONE);
				map_btn_jieshu.setVisibility(View.VISIBLE);
				map_btn_jixu.setVisibility(View.VISIBLE);
				
				isStop =true;
				map_title.setText(mode+"����ͣ");
				timeCha =SystemClock.elapsedRealtime() -chronometer.getBase();
				chronometer.stop();
			}
		});
	}
	private void initView() {
		// TODO Auto-generated method stub
		mMapView = (MapView) findViewById(R.id.mapview);
		map_title =(TextView)findViewById(R.id.map_title);
		map_jiesuo =(Button)findViewById(R.id.map_jiesuo);
		map_btn_jixu =(Button)findViewById(R.id.map_btn1);
		map_btn_jieshu =(Button)findViewById(R.id.map_btn2);
		chronometer =(Chronometer)findViewById(R.id.map_jishi);
		
		map_suduShow =(TextView)findViewById(R.id.map_suduShow);
		map_qiankaShow =(TextView)findViewById(R.id.map_qiankaShow);
		map_juliShow =(TextView)findViewById(R.id.map_juliShow);
		map_suduShow.setText("0.0");
		map_juliShow.setText("0.0");
		map_qiankaShow.setText("0.0");
		
		//��ȡģʽ
		mode =getIntent().getStringExtra("status");
		map_title.setText("����"+mode+"��");
	}
	private void initMap() {
		// TODO Auto-generated method stub
		// ��ͼ��ʼ��
		mBaiduMap = mMapView.getMap();
		// ���õ�ͼ�����ż���
		mBaiduMap.setMapStatus(MapStatusUpdateFactory
				.newMapStatus(new MapStatus.Builder().zoom(18).build()));
		//�����Ƿ�����¥��Ч��
		 mBaiduMap.setBuildingsEnabled(true);
		 //	���ö�λͼ����ʾ��ʽ-�Զ���ͼ��
		 mCurrentMarker = BitmapDescriptorFactory
					.fromResource(R.drawable.marker);
		 mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
					mCurrentMode, true, mCurrentMarker));
		// ������λͼ��
		mBaiduMap.setMyLocationEnabled(true);
	}
	/** ��λ*/
	private void location() {
	// ��λ��ʼ��
		mLocClient = new LocationClient(MyApplication.getInstance().getApplicationContext());
		mLocClient.registerLocationListener(myListener);
		
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(5000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

//	
	/**
	 * ��λSDK��������
	 */
	public class MyLocationListenner implements BDLocationListener {
	
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
			if (location == null || mMapView == null)
				return;
			LogUtils.i("��ͼ������", "������");
			//�ڵ�ͼ����ʾ��λ
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					.speed(location.getSpeed())
					// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
					.direction(location.getDirection()).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			
			//���õ�ͼ�������ĵ�
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			BmobGeoPoint point =new BmobGeoPoint();
			point.setLatitude(location.getLatitude());
			point.setLongitude(location.getLongitude());
			
			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
			mBaiduMap.animateMapStatus(msu);
			//��ʱ����ʼ
			if(isChronometer){
				chronometer.setBase(SystemClock.elapsedRealtime());
				chronometer.start();//����
				isChronometer =false;
				chronometer.setOnChronometerTickListener(new OnChronometerTickListener() {
					
					@Override
					public void onChronometerTick(Chronometer chronometer) {
						// TODO Auto-generated method stub
						miao+=1;
					}
				});
			}
			//�տ�ʼ
			if(latlngs == null){
				latlngs =new ArrayList<LatLng>();
				points =new ArrayList<BmobGeoPoint>();
				
				points.add(point);//��ӵ�
				latlngs.add(latLng);
				DrewMarker(latLng, ConstantValues.MARKERT_START);
			}else{//����
				if (!isStop) {
					count = latlngs.size();
					if (latlngs.get(count - 1) != latLng) {
						latlngs.add(latLng);
						points.add(point);//��ӵ�
						
						setViewDistance(location); 
					}
				}
			}
		}
		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	
	//�������
	private void setViewDistance(BDLocation location) {
		if(latlngs!=null && latlngs.size()>=1){
			double distance=DistanceUtil.getDistance(latlngs.get(latlngs.size()-2), latlngs.get(latlngs.size()-1));
			//�жϾ��� ����̫��ƫ�����ϸ�ָ���ģʽ��
			if(distance<50){
				OverlayOptions ooPolyline = new PolylineOptions()
					.width(10).color(0xff00ffff).points(latlngs).zIndex(5);
					mBaiduMap.addOverlay(ooPolyline);
				mBaiduMap.addOverlay(ooPolyline);
				distanceSum+=distance;
				//���þ���
				map_juliShow.setText(String.valueOf(SportUtils.getKM(distanceSum)));
				map_suduShow.setText(String.valueOf(SportUtils.getFormatKM(location.getSpeed())));
				//����ǧ��
				if(mode.equals("�ܲ�")){
					map_qiankaShow.setText(String.valueOf(SportUtils.getKLL(SportUtils.getKM(distanceSum), SportType.PAO_BU)));
				}else if(mode.equals("����")){
					map_qiankaShow.setText(String.valueOf(SportUtils.getKLL(SportUtils.getKM(distanceSum), SportType.JING_ZOU)));
				}else{
					map_qiankaShow.setText(String.valueOf(SportUtils.getKLL(SportUtils.getKM(distanceSum), SportType.QI_CHE)));
				}
			}else{//�Ƴ�
				latlngs.remove(latlngs.size()-1);
				points.remove(points.size()-1);
			}
		}
	}
	
	// ��Marker
	private void DrewMarker(LatLng latLng, int markertStart) {
		BitmapDescriptor start = BitmapDescriptorFactory
				.fromResource(R.drawable.v3_icon_qidian);
		BitmapDescriptor end = BitmapDescriptorFactory
				.fromResource(R.drawable.v3_icon_zhongdian);
		//���
		if (markertStart == ConstantValues.MARKERT_START) {
			OverlayOptions startM = new MarkerOptions().position(latLng)
					.icon(start).zIndex(5);
			marker_start = (Marker) mBaiduMap.addOverlay(startM);
		} else {//�յ�
			OverlayOptions endM = new MarkerOptions().position(latLng)
					.icon(end).zIndex(5);
			marker_end = (Marker) mBaiduMap.addOverlay(endM);
		}
	}
	
	private void ClearLine() {
		mBaiduMap.clear();
	}
	
	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		// �˳�ʱ���ٶ�λ
		mLocClient.stop();
		// �رն�λͼ��
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		if (ConstantValues.isTrack)
			ConstantValues.isTrack = false;
		if(!StepService.FLAG){
			Intent intent =new Intent(BMapActivity.this, StepService.class);
			startService(intent);
		}
		super.onDestroy();
	}
}
