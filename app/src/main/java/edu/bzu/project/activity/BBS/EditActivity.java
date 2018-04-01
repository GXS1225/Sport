package edu.bzu.project.activity.BBS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bmob.BmobProFile;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

import edu.bzu.project.R;
import edu.bzu.project.activity.setting.AddExercise_Acivity;
import edu.bzu.project.activity.setting.AddSport_Activity;
import edu.bzu.project.activity.setting.AlbumActivity;
import edu.bzu.project.adapter.camera_GridAdapter;
import edu.bzu.project.domain.ImageItem;
import edu.bzu.project.domain.Sport_item;
import edu.bzu.project.domain.bmob.Topic;
import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.domain.bmob.group;

import edu.bzu.project.utils.CommonUtils;
import edu.bzu.project.utils.MyApplication;
import edu.bzu.project.utils.ConstantValues;
import edu.bzu.project.utils.FileUtils;
import edu.bzu.project.utils.ImageUtils;
import edu.bzu.project.utils.LogUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ToggleButton;
/**
 * ������Ľ���
 */
public class EditActivity extends Activity{

	private static String TAG ="EditActivity";
	
	//���ؼ�
	private Button back =null;
	private Button commit =null;
	

	private EditText feel_topic =null;
	/**���*/
	private camera_GridAdapter gridAdapter =null;
	private GridView gridView =null;
	private PopupWindow popupWindow =null;
	
	private LinearLayout camear_linearLayout =null;
	public static Bitmap bitmap =null;
	private static final int TAKE_PICTURE = 0x000011;//������յķ����� 
	public static final int ALUM_CODE =2;
	
	//�Ի���ѡ����
	private AlertDialog dialog =null;
	private Button bc =null;
	private Button bbc =null;
	private Button qx =null;
	
	private group item =null;
	
	private Thread thread =null;
	private Topic topic =null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edittopic);
		item =(group) getIntent().getSerializableExtra("group");
		initView();
		initevent();
	}
	
	 private void initevent() {
		// TODO Auto-generated method stub
		back.setOnClickListener(onClick);
		commit.setOnClickListener(onClick);
	}
	 //�����¼�
	 OnClickListener onClick =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.add_topic:
				//�ύ����Ķ�̬
				upDate();
				break;
			case R.id.back_top:
				//����
				//�ж��Ƿ�������
				if(ConstantValues.tempSelectBitmap.size()>0 || !TextUtils.isEmpty(feel_topic.getText().toString())){
					View v1 =View.inflate(EditActivity.this, R.layout.dialog_back, null);
					bbc =(Button)v1.findViewById(R.id.dialog_bbc);
					bc =(Button)v1.findViewById(R.id.dialog_bc);
					qx =(Button)v1.findViewById(R.id.dialog_qx);
					bbc.setOnClickListener(onClickListener);
					bc.setOnClickListener(onClickListener);
					qx.setOnClickListener(onClickListener);
					//�Ի���
					dialog =new AlertDialog.Builder(EditActivity.this).setView(v1).show();
				}else{
					MyApplication.getInstance().removeActivity(EditActivity.this);
					finish();
				}
				break;
			}
		}
	};
	//�Ի���ļ�����
	OnClickListener onClickListener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent =null;
			switch (v.getId()) {
			//�Ի���ı���
			case R.id.dialog_bc:
				//�ϴ�������
				//�ж� ���ĵ����ݲ���Ϊ��
				upDate();
				break;
			//�Ի���Ĳ�����
			case R.id.dialog_bbc:
				dialog.dismiss();
				ConstantValues.tempSelectBitmap.clear();
				MyApplication.getInstance().removeActivity(EditActivity.this);
				finish();
				break;
			//�Ի����ȡ��
			case R.id.dialog_qx:
				//ʲôҲ������ȡ���Ի���
				dialog.dismiss();
				break;
			default:
				break;
			}
		}
		
	};
	public void upDate(){
		if(TextUtils.isEmpty(feel_topic.getText().toString())){
			Toast.makeText(EditActivity.this, "��,���Ĳ���Ϊ��Ŷ��", 3000).show();
		}else{
			Toast.makeText(EditActivity.this, "�����ϴ�������", 0).show();
			thread =new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					upLoad();
				}
			});
			thread.start();
			
		}
	}
	/**�ϴ�*/
	private void upLoad() {
		// TODO Auto-generated method stub
		//���滰�����ȡ������û����ڱ����������ֶ�
		User user =BmobUser.getCurrentUser(EditActivity.this, User.class);
		
		//�����û�
		topic =new Topic();
		topic.setGroup(item);
		topic.setAuthor(user);
		//������
		//��������
		topic.setTopicContent(feel_topic.getText().toString().trim());
		//��������
		if(ConstantValues.tempSelectBitmap.size()>0){
				List<String> imagepaths =new ArrayList<String>();
				for(int i=0;i<ConstantValues.tempSelectBitmap.size();i++){
					String url =ConstantValues.tempSelectBitmap.get(i).getImagePath();
					imagepaths.add(url);
				}
				final String[] files_str =imagepaths.toArray(new String[imagepaths.size()]);
				
				Bmob.uploadBatch(EditActivity.this, files_str, new UploadBatchListener() {
					
					@Override
					public void onSuccess(List<BmobFile> files, List<String> arg1) {
						// TODO Auto-generated method stub
						LogUtils.i("�ϴ�����ͼƬ", files.size()+"");
						topic.setTopoicPhotos(files);
						if(files_str.length == files.size()){
							handler.sendEmptyMessage(0);
						}
					}
					
					@Override
					public void onProgress(int arg0, int arg1, int arg2, int arg3) {
						// TODO Auto-generated method stub
					}
					@Override
					public void onError(int statuscode, String errormsg) {
						// TODO Auto-generated method stub
						LogUtils.i(TAG, "�ϴ��ļ�    ������"+statuscode +",����������"+errormsg);
					}
				});
		}else{
			handler.sendEmptyMessage(0);
		}
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		 back =(Button )findViewById(R.id.back_top);
		 commit =(Button)findViewById(R.id.add_topic);
		 
		 feel_topic =(EditText)findViewById(R.id.feel_topic);
		 InitCamear();
	}

	/**��ʼ��ͼƬ�ϴ�*/
		private void InitCamear() {
			// TODO Auto-generated method stub
			gridView =(GridView)findViewById(R.id.camear_edit_grid);
			
			View popview =View.inflate(EditActivity.this, R.layout.dialog_popwindow, null);
			popupWindow =CommonUtils.getPopWindow(EditActivity.this,popview);
			camear_linearLayout =(LinearLayout)popview.findViewById(R.id.camear_popup);
			
			RelativeLayout parentview =(RelativeLayout)popview.findViewById(R.id.parent);
			Button btn1 =(Button)popview.findViewById(R.id.item_popupwindows_camera);//���
			Button btn2 =(Button)popview.findViewById(R.id.item_popupwindows_Photo);//���
			Button btn3 =(Button)popview.findViewById(R.id.item_popupwindows_cancel);//ȡ��
			
			parentview.setOnClickListener(new OnClickListener() {//����ⲿ��ͼƬʱȡ������
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					popupWindow.dismiss();
					camear_linearLayout.clearAnimation();//�������
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
					popupWindow.dismiss();
					camear_linearLayout.clearAnimation();
				}
			});
			//���ѡ��
			btn2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent =new Intent(EditActivity.this,AlbumActivity.class);
					startActivityForResult(intent, ALUM_CODE);
					overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
					popupWindow.dismiss();
					camear_linearLayout.clearAnimation();
				}
			});
			btn3.setOnClickListener(new OnClickListener() {
		
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					popupWindow.dismiss();
				}
			});
			gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
			gridAdapter =new camera_GridAdapter(EditActivity.this);
			gridView.setAdapter(gridAdapter);
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					//������һ��ͼƬʱ�������Ի���
					if(position == ConstantValues.tempSelectBitmap.size()){
						camear_linearLayout.setAnimation(AnimationUtils.loadAnimation(EditActivity.this, R.anim.activity_translate_in));
						popupWindow.showAtLocation(View.inflate(EditActivity.this, R.layout.activity_add_sport, null), Gravity.BOTTOM, 0, 0);
					}
				}
			});
			
		}
		//ˢ���뱣������
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 2:
					
					break;
				//���
				case 1:
					gridAdapter.notifyDataSetChanged();
					break;
				//����������
				case 0:
					//��������
					topic.save(EditActivity.this, new SaveListener() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							ConstantValues.tempSelectBitmap.clear();
							Toast.makeText(EditActivity.this, "���ݱ������", 1000).show();
							MyApplication.getInstance().removeActivity(EditActivity.this);
							finish();
						}
						
						@Override
						public void onFailure(int statuscode, String errormsg) {
							// TODO Auto-generated method stub
							LogUtils.i(TAG, "��������     ������"+statuscode +",����������"+errormsg);
						}
					});
					
					break;
				}
			}
		};
		
		//�����̲߳���ˢ�£���鵽�и��¾�ˢ��
		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (ConstantValues.max == ConstantValues.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							ConstantValues.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
		@Override
		protected void onActivityResult(int requestcode, int resultcode, Intent initent) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestcode, resultcode, initent);
			switch (resultcode) {
			//ͼƬ
			case TAKE_PICTURE:
				if(ConstantValues.tempSelectBitmap.size()<3){
					//�ɹ����ճɹ�
					//����ͼƬ������
					String pathname =ImageUtils.getTempFileName();
					//��ȡ�ɹ����յ�ͼƬ
					Bitmap success =(Bitmap)initent.getExtras().get("data");
					//���浽sd����
					FileUtils.saveBitmap(success, pathname);
					//���浽ʵ���༰���ռ�����
					ImageItem item =new ImageItem();
					item.setBitmap(success);
					ConstantValues.tempSelectBitmap.add(item);
					loading();
				}
				break;
			case ALUM_CODE:
				loading();//����һ������
				break;
			}
		}
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				MyApplication.getInstance().removeActivity(this);
				finish();
			}
			return true;
		}
}
