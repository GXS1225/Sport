package edu.bzu.project.activity;

import cn.bmob.v3.Bmob;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
/**
 * 初始化Bmob
 */
public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// 初始化BmobSDK
		Bmob.initialize(this, "058c1abc19e9430ceab7bd1229181c7b");
		
	}
	
	public void toast(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
}
