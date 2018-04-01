package edu.bzu.project.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import edu.bzu.project.R;
import edu.bzu.project.activity.MainActivity;
public class SelectPicPopupWindow extends PopupWindow {

	private Context context =null;
	private Button btn_work, btn_cycle, btn_cancel,btn_run;
	private View mMenuView;
	private ImageView imageView1,imageView2;

	public SelectPicPopupWindow(Context context,OnClickListener itemsOnClick) {
		super(context);
		this.context =context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.alert_dialog, null);
		btn_work = (Button) mMenuView.findViewById(R.id.btn_work);
		btn_cycle = (Button) mMenuView.findViewById(R.id.btn_cycle);
		btn_run = (Button) mMenuView.findViewById(R.id.btn_run);
		btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
		
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.traslate_btn);
		Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.traslate_btn1);
		Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.btn_rotate);
		btn_work.startAnimation(animation);
		btn_cycle.startAnimation(animation1);
		btn_run.startAnimation(animation);
		btn_cancel.startAnimation(animation2);
		
		//取消按钮
		btn_cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//销毁弹出框
				dismiss();
			}
		});
		//设置按钮监听
		btn_work.setOnClickListener(itemsOnClick);
		btn_cycle.setOnClickListener(itemsOnClick);
		btn_run.setOnClickListener(itemsOnClick);
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true;
			}
		});

	}

}
