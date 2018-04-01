package edu.bzu.project.utils;

import java.util.regex.Pattern;




import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.Toast;

public class CommonUtils {

	private static final String TAG = "CommonUtils";
	//1、正则表达式
	 public static boolean isNumeric1(String str){
	  Pattern pattern = Pattern.compile("^[-//+]?//d+(//.//d*)?|//.//d+$");
	  return pattern.matcher(str).matches();
	 }

	/**
	 * 开启activity
	 */
	public static void launchActivity(Context context, Class<?> activity) {
		Intent intent = new Intent(context, activity);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		context.startActivity(intent);
	}

	public static void launchActivityForResult(Activity context,
			Class<?> activity, int requestCode,Bundle b) {
		Intent intent = new Intent(context, activity);
		if(b!=null){
			intent.putExtras(b);
		}
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		context.startActivityForResult(intent, requestCode);
	}
	/**
	 * 退出
	 */
	private static long exitTime = 0;

	public static void ExitApp(Context context) {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(context, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			//退出
			MyApplication.getInstance().exit();
		}
	}

	/**
	 * 隐藏软键盘
	 */
	public static void hideSoftKeybord(Activity activity) {

		if (null == activity) {
			return;
		}
		try {
			final View v = activity.getWindow().peekDecorView();
			if (v != null && v.getWindowToken() != null) {
				InputMethodManager imm = (InputMethodManager) activity
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		} catch (Exception e) {

		}
	}
	/**
	 * 判断字符串是否为空
	 * 
	 * @param text
	 * @return true null false !null
	 */
	public static boolean isNull(String text) {
		if (text == null || "".equals(text.trim()) || "null".equals(text))
			return true;
		return false;
	}
	
	/**
	 * 抖动动画
	 */
//	public static void startShakeAnim(Context context, View view) {
//		Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
//		view.startAnimation(shake);
//	}

	/**
	 * 判断网络是否可用
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 显示进度
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param indeterminate
	 * @param cancelable
	 * @return
	 */
	public static ProgressDialog showProgress(Context context,
			CharSequence title, CharSequence message, boolean indeterminate,
			boolean cancelable) {
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setIndeterminate(indeterminate);
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(false);
		// dialog.setDefaultButton(false);
		dialog.show();
		return dialog;
	}
	
	public static String softVersion(Context context) {
    	PackageInfo info = null;
		try {
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return info.versionName;
    }
	
	public static PopupWindow getPopWindow(Context context,View view){
		PopupWindow popupWindow =new PopupWindow(context);
		
		popupWindow.setWidth(LayoutParams.MATCH_PARENT);
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		//点击外部可以取消
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setContentView(view); 
		return popupWindow;
		
	}
	
	public static AlertDialog getAlertDialog(Context context,View view){
		AlertDialog aDialog =null;
		aDialog =new AlertDialog.Builder(context).setView(view).show();
		return aDialog;
	}
	
}
