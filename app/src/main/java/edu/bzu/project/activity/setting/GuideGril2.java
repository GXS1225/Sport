package edu.bzu.project.activity.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import edu.bzu.project.activity.Guide;
import edu.bzu.project.domain.Userinfo;
import edu.bzu.project.R;
import edu.bzu.project.utils.MyApplication;
import edu.bzu.project.utils.CommonUtils;
import edu.bzu.project.utils.SPUtils;
import edu.bzu.project.view.ObservableHorizontalScrollView;
import edu.bzu.project.view.ObservableHorizontalScrollView.OnScrollStopListner;
public class GuideGril2 extends Activity {
	private ImageView userinfo_head_2,userinfo_body_2,prevent_2,next_2,chizhi,chizhi1,chizhi2,chizhi3;
	private Context context;
	private TextView weight_value;
	private int weight;
	private Userinfo info =null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.guide_gril2);
		info =(Userinfo)getIntent().getSerializableExtra("info");
		// 动画
		userinfo_head_2 = (ImageView) findViewById(R.id.userinfo_head_2);
		userinfo_body_2 = (ImageView) findViewById(R.id.userinfo_body_2);
		prevent_2 = (ImageView) findViewById(R.id.prevent_2);
		next_2 = (ImageView) findViewById(R.id.next_2);
		chizhi = (ImageView) findViewById(R.id.chizhi);
		chizhi1 = (ImageView) findViewById(R.id.chizhi1);
		chizhi2 = (ImageView) findViewById(R.id.chizhi2);
		chizhi3 = (ImageView) findViewById(R.id.chizhi3);
		
		Animation animation0 = AnimationUtils.loadAnimation(
				GuideGril2.this, R.anim.tutorail_bottom);
		LinearInterpolator lin0 = new LinearInterpolator();
		animation0.setInterpolator(lin0);
		chizhi.setVisibility(View.VISIBLE);
		chizhi.startAnimation(animation0);
		chizhi1.setVisibility(View.VISIBLE);
		chizhi1.startAnimation(animation0);
		chizhi2.setVisibility(View.VISIBLE);
		chizhi2.startAnimation(animation0);
		chizhi3.setVisibility(View.VISIBLE);
		chizhi3.startAnimation(animation0);
		
		Animation animation1 = AnimationUtils.loadAnimation(
				GuideGril2.this, R.anim.tutorail_bottom);
		LinearInterpolator lin = new LinearInterpolator();
		animation1.setInterpolator(lin);
		userinfo_head_2.setVisibility(View.VISIBLE);
		userinfo_head_2.startAnimation(animation1);
		userinfo_body_2.setVisibility(View.VISIBLE);
		userinfo_body_2.startAnimation(animation1);
		
		Animation animation2 = AnimationUtils.loadAnimation(
				GuideGril2.this, R.anim.tutorail_lr);
		LinearInterpolator lin1 = new LinearInterpolator();
		animation2.setInterpolator(lin1);
		prevent_2.setVisibility(View.VISIBLE);
		prevent_2.startAnimation(animation2);
		
		Animation animation3 = AnimationUtils.loadAnimation(
				GuideGril2.this, R.anim.tutorail_lrs);
		LinearInterpolator lin2 = new LinearInterpolator();
		animation3.setInterpolator(lin2);
		next_2.setVisibility(View.VISIBLE);
		next_2.startAnimation(animation3);
		
        context=this;
		weight_value = (TextView) findViewById(R.id.weight_value);
		final ObservableHorizontalScrollView scrollView = (ObservableHorizontalScrollView) findViewById(R.id.weight_scrollview);
		scrollView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					scrollView.startScrollerTask();
				}
				return false;
			}
		});
		scrollView.setOnScrollStopListner(new OnScrollStopListner() {
			public void onScrollChange(int index) {
				if (index == 0) {
					weight_value.setText("25");
				} else {
					int value = px2dip(context,index);
					weight = value/7+25;
					weight_value.setText((value/7+25) + "");
				}
			}
		});
	}
	
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	// 按钮处理
	public void next(View view){
		// 下一步
		Intent intent = new Intent(GuideGril2.this ,GuideGril3.class);
		info.setWeight(weight);
		intent.putExtra("info", info);
		startActivity(intent);
		MyApplication.getInstance().removeActivity(this);
		finish();
		
	}
	
	public void prevent(View view){
		// 上一步
		Intent intent = new Intent(GuideGril2.this ,Guide.class);
		intent.putExtra("info", info);
		startActivity(intent);
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
