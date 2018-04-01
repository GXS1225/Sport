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
 * �Զ���tab�ײ�����view
 *
 */
public class MyTabView extends LinearLayout {
	private static final String TAG ="MyTabView";
	
	/**�ײ�tab����ͼƬ*/
	private int bg_drawables[] =new int[]{R.drawable.bg_home,R.drawable.bg_group,R.drawable.bg_start
			,R.drawable.bg_find,R.drawable.bg_setting};
	/**��ŵײ�������view����*/
	private List<View> mViews =new ArrayList<View>();
	/**��ŵײ����ּ���*/
	private List<CheckedTextView> mCheckedTextViews =new ArrayList<CheckedTextView>();
	/**��������*/
	private CharSequence [] labels =null;
	/**��ŵײ�ָʾ�㼯��*/
	private List<ImageView> mIndicates =new ArrayList<ImageView>();;
	
	/**�ӿ�*/
	private OnTabSelectedListener listener =null;
	//
	public MyTabView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	@SuppressLint("NewApi")
	public MyTabView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//��ȡ�Զ�������
		TypedArray a =context.obtainStyledAttributes(attrs, R.styleable.MyTabView, defStyle, 0);
		
		labels =a.getTextArray(R.styleable.MyTabView_bottom_Lables);
		//�ж�
		if(labels==null || labels.length<=0){
			try {
				throw new CustomException("�ײ�������������δ���...");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}finally{
				LogUtils.i(TAG, MyTabView.class.getSimpleName()+"����");
			}
		}
		a.recycle();
		Init(context);
				// TODO Auto-generated constructor stub
	}
	/**�ײ������ĳ�ʼ��*/
	private void Init(final Context context) {
		// TODO Auto-generated method stub
		//���ò��ֵ�����
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setBackgroundResource(R.drawable.index_bottom_bar);
		//�����������ӿؼ���layoutparams
		LayoutInflater inflater =LayoutInflater.from(context);
		
		LinearLayout.LayoutParams lParams =new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.MATCH_PARENT);
		lParams.gravity =Gravity.CENTER;
		lParams.weight =1.0f;//Ȩ��
		
		//ѭ������ӿؼ�view ����Ӽ�����
		for(int i=0;i<labels.length;i++){
			final int index =i;
			View v =inflater.inflate(R.layout.tab_item, this, false);
			//ȡ�ò��ֵĿؼ�����ʼ��֮
			CheckedTextView c =(CheckedTextView) v.findViewById(R.id.tabitem_name);
			c.setText(labels[i]);
			c.setCompoundDrawablesWithIntrinsicBounds(null, context.getResources().getDrawable(bg_drawables[i]),
					null,null);
			ImageView indacate =(ImageView)v.findViewById(R.id.tabitem_indacate);//��ʾ��
			
			addView(v,lParams);
			c.setTag(index);
			//��װ������
			mViews.add(v);
			mCheckedTextViews.add(c);
			mIndicates.add(indacate);
			//������
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//��̬����¼�ʱ��tab�ı仯����
					setTabsDisplay(context,index);
					if(listener!=null){//ִ�нӿڻص����任fragment
						listener.OnTabSelecteed(index);
					}
				}
			});
			//���õ�һ���ӿؼ�Ϊѡ��״̬
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
					LogUtils.i(TAG, c.getText()+"�������");
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
	
	//��д��������
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMeasureMode =MeasureSpec.getMode(widthMeasureSpec);
		int widthMeasureSize =MeasureSpec.getSize(widthMeasureSpec);
		
		int heightMeasureMode =MeasureSpec.getMode(heightMeasureSpec);
		int heightMeasureSize =MeasureSpec.getSize(heightMeasureSpec);
		
		if(widthMeasureMode !=MeasureSpec.EXACTLY){//������֪�ߴ�ʱ
			widthMeasureSize =0;
		}
		if(heightMeasureMode !=MeasureSpec.EXACTLY){
			heightMeasureSize =0;
		}
		
		int width =Math.max(getMeasuredWidth(), widthMeasureSize);//ȡϵͳ������
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
