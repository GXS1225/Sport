package edu.bzu.project.service;

import edu.bzu.project.utils.LogUtils;
import android.R.bool;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 谷歌的算法
 * */
public class StepDetector implements SensorEventListener {

	public static int CURRENT_SETP = 0;
	public static float shijian =0;
	public static int BUFFER_SERP =-1;//缓存步数
	//灵敏度 设置固定值，也让用户可以通过手动设置，此算法不能智能，
	public static float SENSITIVITY = 5; // SENSITIVITY灵敏度
	private float mLastValues[] = new float[3 * 2];
	private float mScale[] = new float[2];
	private float mYOffset;
	private static long end = 0;
	private static long start = 0;
	/**
	 * 最后加速度方向
	 */
	private float mLastDirections[] = new float[3 * 2];
	private float mLastExtremes[][] = { new float[3 * 2], new float[3 * 2] };
	private float mLastDiff[] = new float[3 * 2];
	private int mLastMatch = -1;

	/**
	 * 传入上下文的构造函数
	 * 
	 * @param context
	 */
	public StepDetector(Context context) {
		super();
		int h = 480;
		mYOffset = h * 0.5f;
		mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
		mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
	}

	//当传感器检测到的数值发生变化时就会调用这个方法
	public void onSensorChanged(SensorEvent event) {
	//	LogUtils.i("触发了监听器11----------","" );
		Sensor sensor = event.sensor;
		synchronized (this) {
			if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			//	LogUtils.i("触发了监听器22----------","" );
				float vSum = 0;
				for (int i = 0; i < 3; i++) {
					final float v = mYOffset + event.values[i] * mScale[1];
					vSum += v;
				}
				int k = 0;
				float v = vSum / 3;

				float direction = (v > mLastValues[k] ? 1
						: (v < mLastValues[k] ? -1 : 0));
				if (direction == -mLastDirections[k]) {//改变方向
			//		LogUtils.i("触发了监听器33----------","" );
					// Direction changed
					int extType = (direction > 0 ? 0 : 1); // minumum or
															// maximum?
					mLastExtremes[extType][k] = mLastValues[k];
					float diff = Math.abs(mLastExtremes[extType][k]
							- mLastExtremes[1 - extType][k]);

					if (diff > SENSITIVITY) {
					//	LogUtils.i("触发了监听器44----------","" );
						boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
						boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
						boolean isNotContra = (mLastMatch != 1 - extType);
						BUFFER_SERP =CURRENT_SETP-1;//记录缓存，判断动与不动
						if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough
								&& isNotContra) {
							end = System.currentTimeMillis();
							if (end - start > 500) {// 此时判断为走了一步
								LogUtils.i("走了一步了----------", String.valueOf(CURRENT_SETP));
								shijian+=0.5;//消耗的时间 按照记录的步数 而 记录
								CURRENT_SETP++;
								mLastMatch = extType;
								start = end;
							}
						} else {
							mLastMatch = -1;
						}
					}
					mLastDiff[k] = diff;
				}
				mLastDirections[k] = direction;
				mLastValues[k] = v;
			}

		}

	}
	//当传感器的经度发生变化时就会调用这个方法，在这里没有用
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}
	
	

}
