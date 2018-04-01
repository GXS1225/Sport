package edu.bzu.project.view;

import java.util.ArrayList;
import java.util.List;

import edu.bzu.project.R;
import edu.bzu.project.exception.CustomException;
import edu.bzu.project.utils.LogUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.LabeledIntent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
/**
 * 自定义tab底部导航view
 *
 */
public class MyTabView extends LinearLayout {
	private static final String TAG ="MyTabView";
	
	/**底部tab背景图片*/
	private int bg_drawables[] =new int[]{R.drawable.bg_home,R.drawable.bg_group,R.drawable.bg_start
			,R.drawable.bg_find,R.drawable.bg_setting};
	/**存放底部导航的view集合*/
	private List<View> mViews =new ArrayList<View>();
	/**存放底部文字集合*/
	private List<CheckedTextView> mCheckedTextViews =new ArrayList<CheckedTextView>();
	/**文字数组*/
	private CharSequence [] labels =null;
	/**存放底部指示点集合*/
	private List<ImageView> mIndicates =new ArrayList<ImageView>();;
	
	/**接口*/
	private OnTabSelectedListener listener =null;
	//
	public MyTabView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	@SuppressLint("NewApi")
	public MyTabView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//获取自定义属性
		TypedArray a =context.obtainStyledAttributes(attrs, R.styleable.MyTabView, defStyle, 0);
		
		labels =a.getTextArray(R.styleable.MyTabView_bottom_Lables);
		//判断
		if(labels==null || labels.length<=0){
			try {
				throw new CustomException("底部导航文字数组未添加...");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}finally{
				LogUtils.i(TAG, MyTabView.class.getSimpleName()+"出错");
			}
		}
		a.recycle();
		Init(context);
				// TODO Auto-generated constructor stub
	}
	/**底部导航的初始化*/
	private void Init(final Context context) {
		// TODO Auto-generated method stub
		//设置布局的属性
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setBackgroundResource(R.drawable.index_bottom_bar);
		//创建并设置子控件的layoutparams
		LayoutInflater inflater =LayoutInflater.from(context);
		
		LinearLayout.LayoutParams lParams =new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.MATCH_PARENT);
		lParams.gravity =Gravity.CENTER;
		lParams.weight =1.0f;//权重
		
		//循环添加子控件view 并添加监听器
		for(int i=0;i<labels.length;i++){
			final int index =i;
			View v =inflater.inflate(R.layout.tab_item, this, false);
			//取得布局的控件，初始化之
			CheckedTextView c =(CheckedTextView) v.findViewById(R.id.tabitem_name);
			c.setText(labels[i]);
			c.setCompoundDrawablesWithIntrinsicBounds(null, context.getResources().getDrawable(bg_drawables[i]),
					null,null);
			ImageView indacate =(ImageView)v.findViewById(R.id.tabitem_indacate);//提示器
			
			addView(v,lParams);
			c.setTag(index);
			//封装到集合
			mViews.add(v);
			mCheckedTextViews.add(c);
			mIndicates.add(indacate);
			//监听器
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//动态点击事件时，tab的变化设置
					setTabsDisplay(context,index);
					if(listener!=null){//执行接口回调，变换fragment
						listener.OnTabSelecteed(index);
					}
				}
			});
			//设置第一个子控件为选中状态
			if(i==0){
				c.setChecked(true);
				c.setTextColor(Color.rgb(247, 88, 123));
				v.setBackgroundColor(Color.rgb(250, 250, 250));
			}else {
				c.setChecked(false);
				c.setTextColor(Color.rgb(19, 12, 14));
				v.setBackgroundColor(Color.rgb(250, 250, 250));
			}
		}
		
		
	}
	protected void setTabsDisplay(Context context, int index) {
		// TODO Auto-generated method stub
		int size =mCheckedTextViews.size();
		for(int j=0;j<size;j++){
			if(j!=2){
				CheckedTextView c =mCheckedTextViews.get(j);
				if((Integer)c.getTag() == index){
					LogUtils.i(TAG, c.getText()+"被点击了");
					c.setChecked(true);
					c.setTextColor(Color.rgb(247, 88, 123));
					mCheckedTextViews.get(j).setBackgroundColor(Color.rgb(250, 250, 250));
				}else {
					c.setChecked(false);
					c.setTextColor(Color.rgb(19, 12, 14));
					mCheckedTextViews.get(j).setBackgroundColor(Color.rgb(250, 250, 250));
				}
			}
		}
		
	}
	public MyTabView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	//重写测量方法
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMeasureMode =MeasureSpec.getMode(widthMeasureSpec);
		int widthMeasureSize =MeasureSpec.getSize(widthMeasureSpec);
		
		int heightMeasureMode =MeasureSpec.getMode(heightMeasureSpec);
		int heightMeasureSize =MeasureSpec.getSize(heightMeasureSpec);
		
		if(widthMeasureMode !=MeasureSpec.EXACTLY){//不是已知尺寸时
			widthMeasureSize =0;
		}
		if(heightMeasureMode !=MeasureSpec.EXACTLY){
			heightMeasureSize =0;
		}
		
		int width =Math.max(getMeasuredWidth(), widthMeasureSize);//取系统测量的
		int height =Math.max(this.getBackground().getIntrinsicHeight(), heightMeasureSize);
		setMeasuredDimension(width, height);
	}

	public interface OnTabSelectedListener{
		public void OnTabSelecteed(int index);
	}
	
	public void setOnTabSelectedListener(OnTabSelectedListener listener){
		this.listener =listener;
	}
}
