package edu.bzu.project.activity;

import cn.bmob.v3.BmobUser;
import edu.bzu.project.R;
import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.utils.ConstantValues;
import edu.bzu.project.utils.MyApplication;
import edu.bzu.project.utils.CommonUtils;
import edu.bzu.project.utils.SPUtils;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

public class Welcome extends BaseActivity{
	private boolean isFirstIn = false;
	private static final int TIME = 2000;
	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	// 建立一个线程
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;

			case GO_GUIDE:
				 goGuide();
				break;
			}
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.welcome);
		init();
	}
	
	private void init(){
		
		isFirstIn =(Boolean) SPUtils.get(this, ConstantValues.isFirstIn, true);
		if (!isFirstIn) {
			mHandler.sendEmptyMessageDelayed(GO_HOME, TIME);
		}else{
			mHandler.sendEmptyMessageDelayed(GO_GUIDE, TIME);
		}
		
	}
	
	private void goHome(){
		//获取 缓存的
		User user =BmobUser.getCurrentUser(this, User.class);
		if(user!=null){
			Intent i = new Intent(Welcome.this,MainActivity.class);
			startActivity(i);
		}else{
			Intent i = new Intent(Welcome.this,LoginActivity.class);
			startActivity(i);
		}
		MyApplication.getInstance().removeActivity(this);
		finish();
	}
	private void goGuide(){
		Intent i = new Intent(Welcome.this,Guide.class);
		startActivity(i);
		MyApplication.getInstance().removeActivity(this);
		finish();
	}
	// 按两次返回键退出
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	SPUtils.clear(this);//导航不成功 清空
    	CommonUtils.ExitApp(this);
        return true;
    }
}
