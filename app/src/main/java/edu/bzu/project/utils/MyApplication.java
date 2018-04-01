package edu.bzu.project.utils;

import java.util.LinkedList;
import java.util.List;

import org.litepal.LitePalApplication;

import com.baidu.mapapi.SDKInitializer;
import com.mob.MobApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import cn.bmob.v3.BmobUser;



import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.view.CircleBitmapDisplayer;
import edu.bzu.project.view.CircleDrawable;



import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;

/**自定义activity栈
 * 
 */
public class MyApplication extends LitePalApplication {
	// 用于保存activity
	private List<Activity> activityList = new LinkedList<Activity>();
	// 静态实例
	private static MyApplication instance;


	public static MyApplication getInstance() {
		return instance;
	}

	/**
	 * 可在activity的onCreate中调用
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	/**
	 * 可在activity的onDestroy中调用
	 * 
	 * @param activity
	 */
	public void removeActivity(Activity activity) {
		for (int i = 0; i < activityList.size(); i++) {
			if (activityList.get(i) == activity) {
				activityList.remove(i);
			}
		}
	}

	/**
	 * 在需要退出所有activity的地方调用
	 */
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}

	public List<Activity> getActivitiesList() {
		return activityList;
	}

	public void removeAll() {
		for (int i = 0; i < activityList.size(); i++) {
			activityList.remove(i);
		}
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		instance =this;
		SDKInitializer.initialize(instance.getApplicationContext());
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration  
                .createDefault(this);
        //Initialize ImageLoader with configuration.  
        ImageLoader.getInstance().init(configuration);
	}

	public DisplayImageOptions getOptions(int drawableId){
		return new DisplayImageOptions.Builder()
		.showImageOnLoading(drawableId)
		.showImageForEmptyUri(drawableId)
		.showImageOnFail(drawableId)
		.resetViewBeforeLoading(true)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}
	public DisplayImageOptions getOptionsCircle(int drawableId){
		return new DisplayImageOptions.Builder()
		.showImageOnLoading(drawableId)
		.showImageForEmptyUri(drawableId)
		.showImageOnFail(drawableId)
		.resetViewBeforeLoading(true)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.displayer(new RoundedBitmapDisplayer(30))
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}

}
