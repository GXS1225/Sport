package edu.bzu.project.activity;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.bzu.project.R;
import edu.bzu.project.domain.bmob.PrivateInfo;
import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.fragment.PK_fujin_fragment.MyLocationListenner;
import edu.bzu.project.utils.CommonUtils;
import edu.bzu.project.utils.LogUtils;
import edu.bzu.project.utils.MapLocalUtils;
import edu.bzu.project.utils.MyApplication;
/**
 * 注册的Activity
 */
public class RegisterActivity extends BaseActivity{
	private EditText edt_pass,edt_user,edt_name;
	private Button btn_regist;
	
	//定位
	private LocationClient local =null;
	private BDLocationListener myListener =new MyLocationListenner();
	private BmobGeoPoint weizhi =null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_register);
		initView();
		
		//定位
		MapLocalUtils.startLocation(myListener);
		btn_regist.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				registUser();
			}
		});
	}
	/**
	 * 用户注册的方法
	 */
	protected void registUser() {
		String pass=edt_pass.getText().toString();
		String user_user=edt_user.getText().toString();
		String user_name =edt_name.getText().toString();
		
		User user=new User();
		user.setUsername(user_user);
		user.setPassword(pass);
		user.setUserName(user_name);
		if (weizhi == null) {
			weizhi =new BmobGeoPoint(116.10675, 39.711669);
		}
		user.setWeizhi(weizhi);
		//注册的时候默认给图片
		
		user.signUp(this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
				
				MyApplication.getInstance().removeActivity(RegisterActivity.this);
				finish();
			}
			
			@Override
			public void onFailure(int i, String s) {
				  Toast.makeText(RegisterActivity.this,"注册失败"+s,Toast.LENGTH_SHORT).show();
			}
		});
	}
	private void initView() {
		
		edt_pass=(EditText) findViewById(R.id.edt_pass);
		edt_user=(EditText) findViewById(R.id.edt_user);
		edt_name =(EditText)findViewById(R.id.user_name);
		btn_regist=(Button) findViewById(R.id.btn_regist);
	}
	public class MyLocationListenner implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation bdLocation) {
			// TODO Auto-generated method stub
			if(local == null){
				LogUtils.i("获取位置失败", "pk时 附近的好友");
				return;
			}
			weizhi =new BmobGeoPoint(bdLocation.getLongitude(), bdLocation.getLatitude());
			Toast.makeText(RegisterActivity.this, "已经获取你的位置", 0).show();
		}	
	}
	public Handler handler =new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
		}
		
	};
}
