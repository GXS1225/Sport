package edu.bzu.project.activity;
import edu.bzu.project.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import edu.bzu.project.activity.setting.GuideGril2;
import edu.bzu.project.activity.setting.GuideBoy2;
import edu.bzu.project.domain.Userinfo;
import edu.bzu.project.utils.MyApplication;
import edu.bzu.project.utils.CommonUtils;
import edu.bzu.project.utils.SPUtils;
public class Guide extends Activity{
	// �����ImageView
	private ImageView imageView1,imageView2;
	private Userinfo info =new Userinfo();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
		setContentView(R.layout.guide1);
		MyApplication.getInstance().addActivity(this);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		imageView2 = (ImageView) findViewById(R.id.imageView2);
		// ����
		Animation animation1 = AnimationUtils.loadAnimation(
				Guide.this, R.anim.tutorail_rotate);
		LinearInterpolator lin = new LinearInterpolator();
		animation1.setInterpolator(lin);
		
		imageView1.setVisibility(View.VISIBLE);
		imageView1.startAnimation(animation1);

		imageView2.setVisibility(View.VISIBLE);
		imageView2.startAnimation(animation1);
	}
	
	public void boy(View view){
		Intent intent = new Intent(Guide.this ,GuideBoy2.class);
		info.setSex("��");
		intent.putExtra("info", info);
		startActivity(intent);
		finish();
	}
	
	public void gril(View view){
		info.setSex("Ů");
		Intent intent = new Intent(Guide.this ,GuideGril2.class);
		intent.putExtra("info", info);
		startActivity(intent);
		finish();
	}
	// �����η��ؼ��˳�
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	CommonUtils.ExitApp(this);
        return true;
    }
}
