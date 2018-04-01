package edu.bzu.project.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.litepal.crud.DataSupport;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.baidu.platform.comapi.map.o;
import com.nostra13.universalimageloader.core.ImageLoader;

import edu.bzu.project.R;
import edu.bzu.project.activity.Guide;
import edu.bzu.project.activity.LoginActivity;
import edu.bzu.project.activity.BBS.AttentionInfoActivity;
import edu.bzu.project.activity.setting.AddSport_Activity;
import edu.bzu.project.activity.setting.LookSport_Activity;
import edu.bzu.project.activity.setting.SetAim;
import edu.bzu.project.domain.Pedometer;
import edu.bzu.project.domain.Userinfo;
import edu.bzu.project.domain.bmob.Pedometer_Server;
import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.utils.CommonUtils;
import edu.bzu.project.utils.ConstantValues;
import edu.bzu.project.utils.DateUtils;
import edu.bzu.project.utils.FileUtils;
import edu.bzu.project.utils.ImageUtils;
import edu.bzu.project.utils.LogUtils;
import edu.bzu.project.utils.MyApplication;
import edu.bzu.project.utils.SPUtils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 主界面之设置Fragment
 *
 */
public class SettingFragment extends BaseFragment {
	
	/**拍照*/
	private static final int TAKE_PICTURE =1;
	/**相册*/
	public static final int ALUM_CODE =2;
	/**结果*/
	private static final int PHOTO_REQUEST_CUT = 3;	
	
	private ImageView user_photo =null;//头像
	
	//控件
	private LinearLayout alter_userinfo =null;//修改用户信息
	private LinearLayout alter_aim =null;//修改用户目标
	private LinearLayout submit_mark =null;//提交成绩
	private LinearLayout my_dongtai =null;//我的动态
	private LinearLayout setting_gengxin =null;
	private LinearLayout out_lay = null;//退出
	
	private TextView tv =null;
	/**头像*/
	private PopupWindow popwWindow =null;
	private LinearLayout camera_lay =null;
	
	private Intent intent =null;
	//起个线程 来保存数据
	private HandlerThread handlerThread =null;
	
	private User user =null;
	private Handler handler =new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0://没有数据
				Toast.makeText(getActivity(), "没有数据可传", 0).show();
				break;
			case 1:
				Toast.makeText(getActivity(), "已是最新数据", 0).show();
				break;
			case 2:
				Toast.makeText(getActivity(), "手动提交成绩成功", 0).show();
				break;
			default:
				break;
			}
		}
		
	};
	@Override
	public String getFragmentName() {
		// TODO Auto-generated method stub
		return "SettingFragment";
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootv =inflater.inflate(R.layout.fragment_setting,container,false);
		
		alter_userinfo =(LinearLayout)rootv.findViewById(R.id.alter_userinfo);
		alter_aim =(LinearLayout)rootv.findViewById(R.id.alter_aim);
		user_photo =(ImageView)rootv.findViewById(R.id.user_setting_photo);
		submit_mark =(LinearLayout)rootv.findViewById(R.id.submit_mark);
		my_dongtai =(LinearLayout)rootv.findViewById(R.id.my_dongtai);
		out_lay = (LinearLayout)rootv.findViewById(R.id.out_lay);
		setting_gengxin =(LinearLayout)rootv.findViewById(R.id.setting_gengxin);
		tv =(TextView)rootv.findViewById(R.id.tv);
		
		user =BmobUser.getCurrentUser(getActivity(), User.class);
		initDate();
		initEvent();
		//退出账号
		return rootv;
	}
	private void initDate() {
		// TODO Auto-generated method stub
		//加载信息
		
		if(user != null){
			tv.setText(user.getUserName());
			//联网判断,未存本地 先从网获取 否则默认
			if(CommonUtils.isNetworkAvailable(getActivity())){
				BmobFile photo =user.getUserPhoto();
				if(photo !=null){
					String uri =photo.getFileUrl(getActivity());
					ImageLoader.getInstance().displayImage(uri, user_photo, 
							MyApplication.getInstance().getOptionsCircle(R.drawable.title));
				}
			}
		}else{//为空用户的时候  把网名设置成未登录
			tv.setText("请登录");
		}
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		alter_userinfo.setOnClickListener(onClickListener);
		alter_aim.setOnClickListener(onClickListener);
		out_lay.setOnClickListener(onClickListener);
		submit_mark.setOnClickListener(onClickListener);
		user_photo.setOnClickListener(onClickListener);
		my_dongtai.setOnClickListener(onClickListener);
		setting_gengxin.setOnClickListener(onClickListener);
	}

	public OnClickListener onClickListener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.setting_gengxin:
				Toast.makeText(getActivity(), "已是最新版本", 0).show();
				break;
			//修改个人信息
			case R.id.alter_userinfo:
				CommonUtils.launchActivity(getActivity(), Guide.class);
				break;
			//修改个人目标
			case R.id.alter_aim:
				Intent i =new Intent(getActivity(),SetAim.class);
				Userinfo info =new Userinfo();
				i.putExtra("info", info);
				startActivity(i);
				break;
			//我的动态
			case R.id.my_dongtai:
			   if(user !=null){
				   if(CommonUtils.isNetworkAvailable(getActivity())){
					   intent =new Intent(getActivity(),AttentionInfoActivity.class);
					   intent.putExtra("pk_funjin", user);
					   startActivity(intent);
				   }else{
					   Toast.makeText(getActivity(), "请联网", 0).show();
				   }
			   }else{
				   Toast.makeText(getActivity(), "请登录", 0).show();
			   }
				break;
			//手动提交成绩
			case R.id.submit_mark:
				//把 计步的成绩提交上
				//查询有没有计步的数据，在获取上次记录的时间
				if(user!=null){
					if(CommonUtils.isNetworkAvailable(getActivity())){
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								LogUtils.i("提交成绩", "");
								Pedometer p =DataSupport.findFirst(Pedometer.class);
								if(p == null || p.getMax() ==0) {
									handler.sendEmptyMessage(0);
								}else{
									//获取数据 封装之
									long time =(Long) SPUtils.get(getActivity(), ConstantValues.PK_TIME, 0L);
									List<Pedometer>  pedometers=new ArrayList<Pedometer>();
									if(time == 0){//第一次提交成绩
										pedometers =DataSupport.findAll(Pedometer.class);
									}else{
										pedometers =DataSupport.where("time > ?",time+"").find(Pedometer.class);
									}
									//无数据时
									if(pedometers.size() == 0){
										handler.sendEmptyMessage(1);
									}else{
										//服务器
										List<BmobObject> servers =new ArrayList<BmobObject>();
										for(Pedometer pp:pedometers){
											Pedometer_Server server =new Pedometer_Server();
											server.setBushul(pp.getBushul());
											server.setUser(user);
											server.setMax(pp.getMax());
											server.setMeter(pp.getMeter());
											server.setQianka(pp.getQianka());
											server.setShijian(pp.getShijian());
											servers.add(server);
										}
										//上传服务器
										new BmobObject().insertBatch(getActivity(), servers, new SaveListener() {
											
											@Override
											public void onSuccess() {
												// TODO Auto-generated method stub
												handler.sendEmptyMessage(2);
												SPUtils.put(getActivity(), ConstantValues.PK_TIME, Calendar.getInstance().getTime().getTime());
											}
											
											@Override
											public void onFailure(int arg0, String arg1) {
												// TODO Auto-generated method stub
												
											}
										});
									}
								}
							}
						}).start();
					}else{
						Toast.makeText(getActivity(), "请联网", 0).show();
					}
				}else{
					Toast.makeText(getActivity(), "请登录", 0).show();
				}
				break;
			case R.id.out_lay:
				BmobUser.logOut(getActivity());//注销
				user =null;
				if(user == null){
					//指向登录的页面 
				    intent =new Intent(getActivity(),LoginActivity.class);
					MyApplication.getInstance().removeActivity(getActivity());
					getActivity().finish();
				}
				break;
			case R.id.user_setting_photo:
				if(user!= null){
					if(CommonUtils.isNetworkAvailable(getActivity())){
						View pop =View.inflate(getActivity(), R.layout.dialog_popwindow, null);
						popwWindow =CommonUtils.getPopWindow(getActivity(), pop);
						camera_lay =(LinearLayout)pop.findViewById(R.id.camear_popup);
						
						RelativeLayout parentview =(RelativeLayout)pop.findViewById(R.id.parent);
						Button btn1 =(Button)pop.findViewById(R.id.item_popupwindows_camera);//相机
						Button btn2 =(Button)pop.findViewById(R.id.item_popupwindows_Photo);//相册
						Button btn3 =(Button)pop.findViewById(R.id.item_popupwindows_cancel);//取消
						
						parentview.setOnClickListener(new OnClickListener() {//点击外部的图片时取消操作
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								popwWindow.dismiss();
								camera_lay.clearAnimation();//清除动画
							}
						});
						//相机拍照
						btn1.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
							
								Intent openCamear =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								//openCamear.putExtra(name, value)
	
								startActivityForResult(openCamear, TAKE_PICTURE);
								popwWindow.dismiss();
								camera_lay.clearAnimation();
							}
						});
						//相册选择
						btn2.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent intent2 = new Intent(Intent.ACTION_PICK, null);
								intent2.setDataAndType(
										MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
								startActivityForResult(intent2, ALUM_CODE);
								getActivity().overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
								popwWindow.dismiss();
								camera_lay.clearAnimation();
							}
						});
						btn3.setOnClickListener(new OnClickListener() {
					
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								popwWindow.dismiss();
							}
						});
						camera_lay.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.activity_translate_in));
						popwWindow.showAtLocation(View.inflate(getActivity(), R.layout.activity_add_sport, null), Gravity.CENTER, 0, 0);
					}else{
						Toast.makeText(getActivity(), "请联网", 1000).show();
					}
				}else{
					Toast.makeText(getActivity(), "请登录", 1000).show();
				}
				break;
			}
		}
	};
	
	/**安卓自带的剪切系统（保存好后的照片）*/
	private void startPhotoZoom(Uri uri, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");
		
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		//图片
		case TAKE_PICTURE:
			SPUtils.put(getActivity(), ConstantValues.USER_ICON,  FileUtils.getImageAbsolutePath(getActivity(), data.getData()));
			startPhotoZoom(data.getData(), 150);
			break;
		case ALUM_CODE:
			SPUtils.put(getActivity(), ConstantValues.USER_ICON,  FileUtils.getImageAbsolutePath(getActivity(), data.getData()));
			LogUtils.i("照片的路径", FileUtils.getImageAbsolutePath(getActivity(), data.getData()));
			startPhotoZoom(data.getData(), 150);
			break;
		case PHOTO_REQUEST_CUT:
			if (data != null)
				setPicToView(data);
			break;
		}
	};
	/**将进行剪裁后的图片显示到UI界面上*/
	private void setPicToView(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (bundle != null) {
			Bitmap photo = bundle.getParcelable("data");
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			//这个是图像压缩的方法，三个参数分别是压缩后的图像的格式（JPEG），图像显示的质量（0—100）
			//，100表示最高质量，图像处理的输出流（out）。
			photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);
			//上传服务器
			final User user =BmobUser.getCurrentUser(getActivity(), User.class);
			String file_str =(String) SPUtils.get(getActivity(), ConstantValues.USER_ICON, "");
			final BmobFile file =new BmobFile(new File(file_str));
			file.uploadblock(getActivity(), new UploadFileListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					user.setUserPhoto(file);
					user.update(getActivity(), new UpdateListener() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							LogUtils.i("修改图片成功", "");
							//查询chu来
							BmobQuery<User> query =new BmobQuery<User>();
							query.addWhereEqualTo("objectId", user.getObjectId());
							query.findObjects(getActivity(), new FindListener<User>() {
								
								@Override
								public void onSuccess(List<User> user) {
									// TODO Auto-generated method stub
									ImageLoader.getInstance().displayImage(user.get(0).getUserPhoto().getFileUrl(getActivity()), user_photo, 
											MyApplication.getInstance().getOptionsCircle(R.drawable.title));
								}
								
								@Override
								public void onError(int arg0, String arg1) {
									// TODO Auto-generated method stub
									
								}
							});
						}
						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							LogUtils.i("修改图片失败", "");
						}
					});
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}
			});
			
		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initDate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
