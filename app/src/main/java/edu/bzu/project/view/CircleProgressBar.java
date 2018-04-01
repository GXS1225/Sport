package edu.bzu.project.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.SweepGradient;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
/**
 * �Զ������ʽ���ν�����
 * 2015/7/19 15:34
 *
 */
public class CircleProgressBar extends View {
	/**����ֵ*/
	private int progress =100;
	/**Ŀ�경�����ֵ*/
	private int max =100;
	 
	/**����--���ƽ���*/
	private Paint pathPaint =null;
	/**����--��������Բ������*/
	private Paint fillPaint =null;
	private RectF rect =null;
	/**���Ƚ��ݽ�����ɫ*/
	private int ladderColors[] ={ 0xFF48cbdc, 0xFF4c9fda, 0xFFeac83d,
			0xFFc7427e, 0xFF48cbdc, 0xFF48cbdc};
	/**���ȵĻ�ɫ��ɫ*/
	private int pathColor =0xFFF0EEDF;
	private int pathBorderColor =0xFFD2D1C4;
	/**���ʵĿ��*/
	private int pathwidth =35;
	/**view�Ŀ�͸�*/
	private int width,height;
	/**Ĭ��Բ�İ뾶*/
	private int radius =120;
	private float mSweepAnglePer;
	
	/**MaskFilter�����ΪPaint�����ԵЧ��*/
	/** EmbossMaskFilter  ָ���˹�Դ�ķ���ͻ�����ǿ������Ӹ���Ч����*/
	private EmbossMaskFilter memboss =null;
	// ���ù�Դ�ķ���
	float[] direction = new float[] { 1, 1, 1 };
	// ���û���������
	float light = 0.4f;
	// ѡ��ҪӦ�õķ���ȼ�
	float specular = 6;
	// �� maskӦ��һ�������ģ��
	float blur = 3.5f;
	/**BlurMaskFilter   ָ����һ��ģ������ʽ�Ͱ뾶������Paint�ı�Ե��*/
	private BlurMaskFilter mblur =null;
	
	/**��������*/
	private BarAnimation animation =null;
	
	/**�ػ��־*/
	private boolean reset =false;

	public CircleProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		//��ʼ������
		pathPaint =new Paint();
		pathPaint.setAntiAlias(true);
		pathPaint.setFlags(Paint.ANTI_ALIAS_FLAG);//�����������
		
		pathPaint.setStyle(Style.STROKE);
		pathPaint.setDither(true);//���÷��������Ե�ƽ��
		pathPaint.setStrokeJoin(Join.ROUND);//�������Ӵ�����ʽ
		
		fillPaint =new Paint();
		fillPaint.setAntiAlias(true);
		fillPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		
		fillPaint.setStyle(Style.STROKE);
		fillPaint.setDither(true);
		fillPaint.setStrokeJoin(Join.ROUND);
		
		rect =new RectF();
		//��ʼ����ԵЧ���������
		memboss =new EmbossMaskFilter(direction, light, specular, blur);
		mblur =new BlurMaskFilter(20, Blur.NORMAL);
		//��ʼ����������
		animation =new BarAnimation();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(reset){
			canvas.drawColor(Color.TRANSPARENT);
			reset =false;
		}
		//��ȡ���ߣ��뾶������
		this.width =getMeasuredWidth();
		this.height =getMeasuredHeight();
		this.radius =width/2-pathwidth;//��Ϊ���б߿����ԣ�����ʣ��Ŀռ�
		//���û��ʵ�����
		pathPaint.setColor(pathColor);
		pathPaint.setStrokeWidth(pathwidth);
		pathPaint.setMaskFilter(memboss);//����Ч��
		/**��������Բ���İ���*/
		canvas.drawCircle(width/2, height/2, radius, pathPaint);//�뾶���յ��Ǳʿ��һ��
		/**��������Բ���ı߿�*/
		pathPaint.setStrokeWidth(0.5f);
		pathPaint.setColor(pathBorderColor);
		canvas.drawCircle(width/2, height/2, radius+pathwidth/2+0.5f,pathPaint);
		canvas.drawCircle(width/2, height/2, radius-pathwidth/2-0.5f, pathPaint);
		
		//����������
		SweepGradient sweepGradient =new SweepGradient(width/2, height/2, ladderColors, null);
		fillPaint.setShader(sweepGradient);
		//Բ���������ͻ�Բ��ʱ�뾶�������Ǻϣ�����Բ��������
		rect.set(width/2-radius, height/2-radius, width/2+radius, height/2+radius);
		//���û��ʵ�����
		fillPaint.setMaskFilter(mblur);//ģ��Ч��
		fillPaint.setStrokeCap(Cap.ROUND);//��ˢ����
		fillPaint.setStrokeWidth(pathwidth);//��Բ���ʿ�һ��
		/**������Բ��*/
		canvas.drawArc(rect, -90, mSweepAnglePer,false, fillPaint);//flaseΪ����
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = View.MeasureSpec.getSize(heightMeasureSpec);
		int width = View.MeasureSpec.getSize(widthMeasureSpec);
		setMeasuredDimension(width, height);
	}
	
	/**����������ʹ�ö��������ϵĻ�ȡ�Ƕ��ػ������*/
	class BarAnimation extends Animation{

		/**������д�ķ������Զ��ص��˷��� ��ͣ�ػ���view����*/
		/**������interpolatedTime����������ʱ����б�
		 * Transformation ��ͬʱ�̶�ͼ�λ�����ı��γ̶�*/
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			// TODO Auto-generated method stub
			super.applyTransformation(interpolatedTime, t);
			//ʱ���С��1��ʱ��ʹ��ʱ��ȼ�������ȵĽǶȣ�������������
			if(interpolatedTime<1.0f){
				mSweepAnglePer =interpolatedTime * 360 * progress/max;
			}else {
				mSweepAnglePer =progress * 360/max;
			}
			postInvalidate();//�ػ�
		}
		
	}
	
	public interface OnProgressListener{
		public void onProgress(int progress); 
	    
	    public void onComplete(int progress); 

	}
	private OnProgressListener onProgressListener =null;

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public OnProgressListener getOnProgressListener() {
		return onProgressListener;
	}

	public void setOnProgressListener(OnProgressListener onProgressListener) {
		this.onProgressListener = onProgressListener;
	}

	public int getProgress() {
		return progress;
	}

	/**���ý��ȣ����������ػ�view*/
	public void setProgress(int progress, int time) {
		this.progress = progress;
		animation.setDuration(time);
		startAnimation(animation);//��������
	}
	/**���ý���*/
	private void reset(){
		reset =true;
		progress =0;
		this.invalidate();
	}

}

