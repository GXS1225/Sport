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
 * ������֮����Fragment
 *
 */
public class SettingFragment extends BaseFragment {
	
	/**����*/
	private static final int TAKE_PICTURE =1;
	/**���*/
	public static final int ALUM_CODE =2;
	/**���*/
	private static final int PHOTO_REQUEST_CUT = 3;	
	
	private ImageView user_photo =null;//ͷ��
	
	//�ؼ�
	private LinearLayout alter_userinfo =null;//�޸��û���Ϣ
	private LinearLayout alter_aim =null;//�޸��û�Ŀ��
	private LinearLayout submit_mark =null;//�ύ�ɼ�
	private LinearLayout my_dongtai =null;//�ҵĶ�̬
	private LinearLayout setting_gengxin =null;
	private LinearLayout out_lay = null;//�˳�
	
	private TextView tv =null;
	/**ͷ��*/
	private PopupWindow popwWindow =null;
	private LinearLayout camera_lay =null;
	
	private Intent intent =null;
	//����߳� ����������
	private HandlerThread handlerThread =null;
	
	private User user =null;
	private Handler handler =new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0://û������
				Toast.makeText(getActivity(), "û�����ݿɴ�", 0).show();
				break;
			case 1:
				Toast.makeText(getActivity(), "������������", 0).show();
				break;
			case 2:
				Toast.makeText(getActivity(), "�ֶ��ύ�ɼ��ɹ�", 0).show();
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
		//�˳��˺�
		return rootv;
	}
	private void initDate() {
		// TODO Auto-generated method stub
		//������Ϣ
		
		if(user != null){
			tv.setText(user.getUserName());
			//�����ж�,δ�汾�� �ȴ�����ȡ ����Ĭ��
			if(CommonUtils.isNetworkAvailable(getActivity())){
				BmobFile photo =user.getUserPhoto();
				if(photo !=null){
					String uri =photo.getFileUrl(getActivity());
					ImageLoader.getInstance().displayImage(uri, user_photo, 
							MyApplication.getInstance().getOptionsCircle(R.drawable.title));
				}
			}
		}else{//Ϊ���û���ʱ��  ���������ó�δ��¼
			tv.setText("���¼");
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
				Toast.makeText(getActivity(), "�������°汾", 0).show();
				break;
			//�޸ĸ�����Ϣ
			case R.id.alter_userinfo:
				CommonUtils.launchActivity(getActivity(), Guide.class);
				break;
			//�޸ĸ���Ŀ��
			case R.id.alter_aim:
				Intent i =new Intent(getActivity(),SetAim.class);
				Userinfo info =new Userinfo();
				i.putExtra("info", info);
				startActivity(i);
				break;
			//�ҵĶ�̬
			case R.id.my_dongtai:
			   if(user !=null){
				   if(CommonUtils.isNetworkAvailable(getActivity())){
					   intent =new Intent(getActivity(),AttentionInfoActivity.class);
					   intent.putExtra("pk_funjin", user);
					   startActivity(intent);
				   }else{
					   Toast.makeText(getActivity(), "������", 0).show();
				   }
			   }else{
				   Toast.makeText(getActivity(), "���¼", 0).show();
			   }
				break;
			//�ֶ��ύ�ɼ�
			case R.id.submit_mark:
				//�� �Ʋ��ĳɼ��ύ��
				//��ѯ��û�мƲ������ݣ��ڻ�ȡ�ϴμ�¼��ʱ��
				if(user!=null){
					if(CommonUtils.isNetworkAvailable(getActivity())){
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								LogUtils.i("�ύ�ɼ�", "");
								Pedometer p =DataSupport.findFirst(Pedometer.class);
								if(p == null || p.getMax() ==0) {
									handler.sendEmptyMessage(0);
								}else{
									//��ȡ���� ��װ֮
									long time =(Long) SPUtils.get(getActivity(), ConstantValues.PK_TIME, 0L);
									List<Pedometer>  pedometers=new ArrayList<Pedometer>();
									if(time == 0){//��һ���ύ�ɼ�
										pedometers =DataSupport.findAll(Pedometer.class);
									}else{
										pedometers =DataSupport.where("time > ?",time+"").find(Pedometer.class);
									}
									//������ʱ
									if(pedometers.size() == 0){
										handler.sendEmptyMessage(1);
									}else{
										//������
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
										//�ϴ�������
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
						Toast.makeText(getActivity(), "������", 0).show();
					}
				}else{
					Toast.makeText(getActivity(), "���¼", 0).show();
				}
				break;
			case R.id.out_lay:
				BmobUser.logOut(getActivity());//ע��
				user =null;
				if(user == null){
					//ָ���¼��ҳ�� 
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
						Button btn1 =(Button)pop.findViewById(R.id.item_popupwindows_camera);//���
						Button btn2 =(Button)pop.findViewById(R.id.item_popupwindows_Photo);//���
						Button btn3 =(Button)pop.findViewById(R.id.item_popupwindows_cancel);//ȡ��
						
						parentview.setOnClickListener(new OnClickListener() {//����ⲿ��ͼƬʱȡ������
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								popwWindow.dismiss();
								camera_lay.clearAnimation();//�������
							}
						});
						//�������
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
						//���ѡ��
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
						Toast.makeText(getActivity(), "������", 1000).show();
					}
				}else{
					Toast.makeText(getActivity(), "���¼", 1000).show();
				}
				break;
			}
		}
	};
	
	/**��׿�Դ��ļ���ϵͳ������ú����Ƭ��*/
	private void startPhotoZoom(Uri uri, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// cropΪtrue�������ڿ�����intent��������ʾ��view���Լ���
		intent.putExtra("crop", "true");
		
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY �Ǽ���ͼƬ�Ŀ��
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		//ͼƬ
		case TAKE_PICTURE:
			SPUtils.put(getActivity(), ConstantValues.USER_ICON,  FileUtils.getImageAbsolutePath(getActivity(), data.getData()));
			startPhotoZoom(data.getData(), 150);
			break;
		case ALUM_CODE:
			SPUtils.put(getActivity(), ConstantValues.USER_ICON,  FileUtils.getImageAbsolutePath(getActivity(), data.getData()));
			LogUtils.i("��Ƭ��·��", FileUtils.getImageAbsolutePath(getActivity(), data.getData()));
			startPhotoZoom(data.getData(), 150);
			break;
		case PHOTO_REQUEST_CUT:
			if (data != null)
				setPicToView(data);
			break;
		}
	};
	/**�����м��ú��ͼƬ��ʾ��UI������*/
	private void setPicToView(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (bundle != null) {
			Bitmap photo = bundle.getParcelable("data");
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			//�����ͼ��ѹ���ķ��������������ֱ���ѹ�����ͼ��ĸ�ʽ��JPEG����ͼ����ʾ��������0��100��
			//��100��ʾ���������ͼ������������out����
			photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);
			//�ϴ�������
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
							LogUtils.i("�޸�ͼƬ�ɹ�", "");
							//��ѯchu��
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
							LogUtils.i("�޸�ͼƬʧ��", "");
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
