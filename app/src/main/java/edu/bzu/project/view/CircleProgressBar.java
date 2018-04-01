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
 * 自定义填充式环形进度条
 * 2015/7/19 15:34
 *
 */
public class CircleProgressBar extends View {
	/**进度值*/
	private int progress =100;
	/**目标步数最大值*/
	private int max =100;
	 
	/**画笔--绘制进度*/
	private Paint pathPaint =null;
	/**画笔--绘制填充的圆形区域*/
	private Paint fillPaint =null;
	private RectF rect =null;
	/**进度阶梯渐变颜色*/
	private int ladderColors[] ={ 0xFF48cbdc, 0xFF4c9fda, 0xFFeac83d,
			0xFFc7427e, 0xFF48cbdc, 0xFF48cbdc};
	/**进度的灰色颜色*/
	private int pathColor =0xFFF0EEDF;
	private int pathBorderColor =0xFFD2D1C4;
	/**画笔的宽度*/
	private int pathwidth =35;
	/**view的宽和高*/
	private int width,height;
	/**默认圆的半径*/
	private int radius =120;
	private float mSweepAnglePer;
	
	/**MaskFilter类可以为Paint分配边缘效果*/
	/** EmbossMaskFilter  指定了光源的方向和环境光强度来添加浮雕效果。*/
	private EmbossMaskFilter memboss =null;
	// 设置光源的方向
	float[] direction = new float[] { 1, 1, 1 };
	// 设置环境光亮度
	float light = 0.4f;
	// 选择要应用的反射等级
	float specular = 6;
	// 向 mask应用一定级别的模糊
	float blur = 3.5f;
	/**BlurMaskFilter   指定了一个模糊的样式和半径来处理Paint的边缘。*/
	private BlurMaskFilter mblur =null;
	
	/**动画对象*/
	private BarAnimation animation =null;
	
	/**重绘标志*/
	private boolean reset =false;

	public CircleProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		//初始化画笔
		pathPaint =new Paint();
		pathPaint.setAntiAlias(true);
		pathPaint.setFlags(Paint.ANTI_ALIAS_FLAG);//帮助消除锯齿
		
		pathPaint.setStyle(Style.STROKE);
		pathPaint.setDither(true);//设置防抖动，显得平滑
		pathPaint.setStrokeJoin(Join.ROUND);//拐弯连接处的样式
		
		fillPaint =new Paint();
		fillPaint.setAntiAlias(true);
		fillPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		
		fillPaint.setStyle(Style.STROKE);
		fillPaint.setDither(true);
		fillPaint.setStrokeJoin(Join.ROUND);
		
		rect =new RectF();
		//初始化边缘效果处理对象
		memboss =new EmbossMaskFilter(direction, light, specular, blur);
		mblur =new BlurMaskFilter(20, Blur.NORMAL);
		//初始化动画对象
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
		//获取宽，高，半径等属性
		this.width =getMeasuredWidth();
		this.height =getMeasuredHeight();
		this.radius =width/2-pathwidth;//因为还有边框所以，会有剩余的空间
		//设置画笔的属性
		pathPaint.setColor(pathColor);
		pathPaint.setStrokeWidth(pathwidth);
		pathPaint.setMaskFilter(memboss);//浮雕效果
		/**画进度条圆环的凹槽*/
		canvas.drawCircle(width/2, height/2, radius, pathPaint);//半径的终点是笔宽的一半
		/**画进度条圆环的边框*/
		pathPaint.setStrokeWidth(0.5f);
		pathPaint.setColor(pathBorderColor);
		canvas.drawCircle(width/2, height/2, radius+pathwidth/2+0.5f,pathPaint);
		canvas.drawCircle(width/2, height/2, radius-pathwidth/2-0.5f, pathPaint);
		
		//渐变填充对象
		SweepGradient sweepGradient =new SweepGradient(width/2, height/2, ladderColors, null);
		fillPaint.setShader(sweepGradient);
		//圆弧轮廓，和画圆环时半径的坐标吻合，即是圆的正方形
		rect.set(width/2-radius, height/2-radius, width/2+radius, height/2+radius);
		//设置画笔的属性
		fillPaint.setMaskFilter(mblur);//模糊效果
		fillPaint.setStrokeCap(Cap.ROUND);//笔刷类型
		fillPaint.setStrokeWidth(pathwidth);//和圆环笔宽一致
		/**画进度圆弧*/
		canvas.drawArc(rect, -90, mSweepAnglePer,false, fillPaint);//flase为空心
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = View.MeasureSpec.getSize(heightMeasureSpec);
		int width = View.MeasureSpec.getSize(widthMeasureSpec);
		setMeasuredDimension(width, height);
	}
	
	/**动画，这里使用动画来不断的获取角度重绘进度条*/
	class BarAnimation extends Animation{

		/**必须重写的方法，自动回调此方法 不停地绘制view进度*/
		/**参数：interpolatedTime：代表动画的时间进行比
		 * Transformation 不同时刻对图形或组件的变形程度*/
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			// TODO Auto-generated method stub
			super.applyTransformation(interpolatedTime, t);
			//时间比小于1的时候使用时间比计算出进度的角度，否则填满进度
			if(interpolatedTime<1.0f){
				mSweepAnglePer =interpolatedTime * 360 * progress/max;
			}else {
				mSweepAnglePer =progress * 360/max;
			}
			postInvalidate();//重绘
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

	/**设置进度，开启动画重绘view*/
	public void setProgress(int progress, int time) {
		this.progress = progress;
		animation.setDuration(time);
		startAnimation(animation);//启动动画
	}
	/**重置进度*/
	private void reset(){
		reset =true;
		progress =0;
		this.invalidate();
	}

}

