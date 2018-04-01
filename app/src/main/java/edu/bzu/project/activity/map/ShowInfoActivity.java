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
 * �˶���չʾ�嵥
 * @author houenshuo
 *
 */
public class ShowInfoActivity extends Activity {

	private Map_domain domain =null;
	private Context context =null;
	
	//��ͼ
	private MapStatusUpdate msu;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LocationMode mCurrentMode=LocationMode.FOLLOWING;
	private BitmapDescriptor mCurrentMarker;
	//���
	private Marker marker_start, marker_end;
	private List<BmobGeoPoint> points =null;
	private List<LatLng> LatLngs =new ArrayList<LatLng>();
	
	//�ؼ�
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
		//ȡ��
		domain =(Map_domain) getIntent().getSerializableExtra("sportInfo");
		//��ʼ���ؼ�
		initView();
		//չʾ����
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
				
				//�ύ��������
				domain.save(context, new SaveListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						//����ɹ�
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
		//��ʾ��ͼ�Ĺ켣
		//�����˶��Ĺ켣
		points =domain.getPoints();
		if(points !=null){
			for(int i =0 ;i<points.size();i++){
				LatLng latLng =new LatLng(points.get(i).getLatitude(), points.get(i).getLongitude());
				LatLngs.add(latLng);
				if(LatLngs.size()==1){//����ʼͼƬ
					DrewMarker(latLng, ConstantValues.MARKERT_START);
				}else if(LatLngs.size() == points.size()){//������ͼ��
					DrewMarker(latLng, ConstantValues.MARKERT_END);
				}else{
					MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
					mBaiduMap.animateMapStatus(msu);
					OverlayOptions ooPolyline = new PolylineOptions()
					.width(10).color(0xff00ffff).points(LatLngs).zIndex(5);
					mBaiduMap.addOverlay(ooPolyline);
					LogUtils.i("���ڻ��켣", i+"��");
				}
			}
		}
		//��ʾ�˶��Ľ��
		mapinfo_juli.setText(String.valueOf(domain.getJuli()));
		mapinfo_time.setText(String.valueOf(domain.getSummiao()));
		mapinfo_suduShow.setText(String.valueOf(domain.getSudu()));
		mapinfo_bushuShow.setText(String.valueOf(domain.getJuli()));
		mapinfo_qiankaShow.setText(String.valueOf(domain.getQianka()));
//		//�����˶�
//		String show_newinfo ="�����˶�������"+domain.getMode()+
//				"--�˶���ʱ��"+domain.getSummiao()+
//				"--�˶��ľ���"+domain.getJuli()+
//				"--�˶���ʱ��"+domain.getSudu()+
//				"--���ĵ�����"+domain.getQianka();
//		sport_newinfo.setText(show_newinfo);
//		//�˶��ıȽ�
//		//��ȡ
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
//		qBmobQuery.setLimit(3);//��3�������
//		qBmobQuery.findObjects(context, new FindListener<Map_domain>() {
//			
//			public void onSuccess(List<Map_domain> lists) {
//				// TODO Auto-generated method stub
//				String show_oldinfo =null;
//				if(lists.size() == 0){
//					show_oldinfo="���һ�ν���"+domain.getMode()+"�˶�����ϲ�ɺ�";
//				}else{
//					int i =0;
//					double sumQianka =0;
//					double sumJuli =0;
//					//ȡ�������� �Ƚ�֮
//					for(Map_domain d:lists){
//						i++;
//						sumQianka+=domain.getQianka();
//						sumJuli+=domain.getJuli();
//					}
//				  show_oldinfo ="�����˶�������"+domain.getMode()+
//						  "���ƽ������"+sumJuli/i+"���ƽ����������"
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
}
