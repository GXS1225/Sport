package edu.bzu.project.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import com.baidu.platform.comapi.map.A;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindStatisticsListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import edu.bzu.project.R;
import edu.bzu.project.activity.BBS.AttentionActivity;
import edu.bzu.project.activity.BBS.EditActivity;
import edu.bzu.project.activity.BBS.LookAttentionActivity;
import edu.bzu.project.activity.BBS.PKActivity;
import edu.bzu.project.activity.BBS.PaiHangActivity;
import edu.bzu.project.activity.setting.AddSport_Activity;
import edu.bzu.project.activity.setting.AlbumActivity;
import edu.bzu.project.activity.setting.LookSport_Activity;
import edu.bzu.project.adapter.PaiHang_adapter;
import edu.bzu.project.adapter.PaiHang_pk_adapter;
import edu.bzu.project.domain.ImageItem;
import edu.bzu.project.domain.PaiHang_domain;
import edu.bzu.project.domain.Userinfo;
import edu.bzu.project.domain.bmob.Attention;
import edu.bzu.project.domain.bmob.Map_domain;
import edu.bzu.project.domain.bmob.Pk_domain;
import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.receiver.Pk_Utils;
import edu.bzu.project.utils.CommonUtils;
import edu.bzu.project.utils.ConstantValues;
import edu.bzu.project.utils.DateUtils;
import edu.bzu.project.utils.FileUtils;
import edu.bzu.project.utils.ImageUtils;
import edu.bzu.project.utils.LogUtils;
import edu.bzu.project.utils.SPUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
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
 *������֮����fragment
 *
 */
public class FindFragment extends BaseFragment {
	private static final String TAG ="FindFragment";
	
	//����
	private TextView fens =null;
	private TextView dengji =null;
	private TextView max_mubiao =null;
	
	private LinearLayout sport_add =null;//����˶�
	private LinearLayout sport_look =null;//�鿴�˶�
	
	private LinearLayout guanzhu_lay =null;//��ע�ĺ���
	private LinearLayout find_guanzhu =null;//���ҹ�ע
	private ImageView user_icon =null;//�û�ͷ��
	
	private LinearLayout look_pk =null;//�鿴ս��
	private LinearLayout layout_pk =null; //pk������
	
	private LinearLayout myScore_lay =null;//�ҵ�����
	private LinearLayout paihang_lay =null;//����
	/**ͷ��*/
	private PopupWindow popwWindow =null;
	private LinearLayout camera_lay =null;
	
	//�Ի���
	private AlertDialog dialog =null;
	private int sumScore;
	private int successScore;
	
	private Thread thread =null;
	private List<Integer> lists =new ArrayList<Integer>();
	
	private int flag =0;//��־λ ����Ŀ���û�
	private PaiHang_domain self =null;//�û�
	@Override
	public String getFragmentName() {
		// TODO Auto-generated method stub
		return "FindFragment";
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
		View rootv =inflater.inflate(R.layout.fragment_find,container,false);

		fens =(TextView)rootv.findViewById(R.id.find_fens);
		dengji =(TextView)rootv.findViewById(R.id.find_dengji);
		max_mubiao =(TextView)rootv.findViewById(R.id.find_mubiao);
		initDate();
		sport_add =(LinearLayout)rootv.findViewById(R.id.sport_add_lay);
		sport_look =(LinearLayout)rootv.findViewById(R.id.sport_look_lay);
		guanzhu_lay =(LinearLayout)rootv.findViewById(R.id.guanzhu_lay);
		layout_pk =(LinearLayout)rootv.findViewById(R.id.layout_pk);
		look_pk =(LinearLayout)rootv.findViewById(R.id.look_pk);
		paihang_lay =(LinearLayout)rootv.findViewById(R.id.paihang_lay);
		find_guanzhu =(LinearLayout)rootv.findViewById(R.id.find_guanzhu_lay);
		myScore_lay =(LinearLayout)rootv.findViewById(R.id.myScore_lay);
		
		fens.setOnClickListener(onClickListener);
		dengji.setOnClickListener(onClickListener);
		max_mubiao.setOnClickListener(onClickListener);
		sport_add.setOnClickListener(onClickListener);
		sport_look.setOnClickListener(onClickListener);
		guanzhu_lay.setOnClickListener(onClickListener);
		//user_icon.setOnClickListener(onClickListener);
		layout_pk.setOnClickListener(onClickListener);
		look_pk.setOnClickListener(onClickListener);
		paihang_lay.setOnClickListener(onClickListener);
		find_guanzhu.setOnClickListener(onClickListener);
		myScore_lay.setOnClickListener(onClickListener);
		return rootv;
	}
	private void initDate() {
		// TODO Auto-generated method stub
		Userinfo info =DataSupport.find(Userinfo.class, 1);
		max_mubiao.setText(String.valueOf((int)info.getMax()));
		//���ҷ�˿
		BmobQuery<Attention> query =new BmobQuery<Attention>();
		query.addWhereEqualTo("user_", BmobUser.getCurrentUser(getActivity(), User.class));
		query.count(getActivity(), Attention.class, new CountListener() {
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onSuccess(int count) {
				// TODO Auto-generated method stub
				fens.setText(String.valueOf(count));
			}
		});
		//�ȼ�  ����ʱ��������ȼ�
		BmobQuery<Map_domain> query2 =new BmobQuery<Map_domain>();
		query2.addWhereEqualTo("user", BmobUser.getCurrentUser(getActivity(), User.class));
		query2.sum(new String[]{"juli"});
		query2.findStatistics(getActivity(), Map_domain.class, new FindStatisticsListener() {
			
			@Override
			public void onSuccess(Object object) {
				// TODO Auto-generated method stub
				JSONArray ary = (JSONArray) object;
		        if(ary!=null){
		            int length = ary.length();
		            double sumDengji = 0; 
		            String str_dengji =null;
		            try {
		                for (int i = 0; i < length; i++) {
		                    JSONObject obj = ary.getJSONObject(i);  
		                    sumDengji =obj.getDouble("_sumJuli");
		                }
		                //��ʾ,�����˶��ľ���������
		                if(sumDengji>=0 && sumDengji<100){
		                	str_dengji ="Lv1";
		                }else if(sumDengji>=100 && sumDengji<200){
		                	str_dengji ="Lv2";
		                }else if(sumDengji>=200 && sumDengji<500){
		                	str_dengji ="Lv3";
		                }else{
		                	str_dengji ="��ǿ����";
		                }
		                dengji.setText(str_dengji);
		            } catch (JSONException e) {
		                e.printStackTrace();
		            }
		        } else {
		            
		        }
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	OnClickListener onClickListener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			switch (v.getId()) {
			/**�ж��û���¼������  �ɱ�д���� ʵ�ִ��븴���ع�*/
			//�ҵ�����
			case R.id.myScore_lay:
				if(BmobUser.getCurrentUser(getActivity(), User.class)!=null){
					if(CommonUtils.isNetworkAvailable(getActivity())){
						View layout1 =View.inflate(getActivity(), R.layout.dialog_score, null);
						dialog =CommonUtils.getAlertDialog(getActivity(), layout1);
						TextView score_title =(TextView)layout1.findViewById(R.id.score_title);
						score_title.setText("����");
						final TextView score =(TextView)layout1.findViewById(R.id.score);
						Button btn1 =(Button)layout1.findViewById(R.id.score_btn);
						
						btn1.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
						

						final List<PaiHang_domain> domains =new ArrayList<PaiHang_domain>();
						BmobQuery<Map_domain> query =new BmobQuery<Map_domain>();
						query.sum(new String[]{"juli"});
						query.groupby(new String[]{"user"});
						query.setLimit(100);
						query.setHasGroupCount(true);
						query.findStatistics(getActivity(), Map_domain.class, new FindStatisticsListener() {
							
							@Override
							public void onSuccess(Object object) {
								// TODO Auto-generated method stub
								JSONArray ary = (JSONArray) object;
								LogUtils.i("������", "");
						        if(ary!=null){
						            final int length = ary.length();
						            List<String> userList =new ArrayList<String>();
						            
						            try {
						                for (int i = 0; i < length; i++) {
						                	if(flag == 1){
						                		break;//�ҵ� �˳�
						                	}
						                    JSONObject obj = ary.getJSONObject(i);
						                    final double sumScore =obj.getDouble("_sumJuli");
						                    JSONObject userobj =obj.getJSONObject("user");
						                    String user_str =userobj.getString("objectId");
						                    //����
						                    BmobQuery<User> query =new BmobQuery<User>();
						                    query.getObject(getActivity(), user_str, new GetListener<User>() {
												
												@Override
												public void onFailure(int arg0, String arg1) {
													// TODO Auto-generated method stub
													
												}
												
												@Override
												public void onSuccess(User user) {
													// TODO Auto-generated method stub
													
													PaiHang_domain domain =new PaiHang_domain();
													domain.setSumScore(sumScore);
								                    User self1 =BmobUser.getCurrentUser(getActivity(), User.class);
								                    if(self1.getObjectId().equals(user.getObjectId())){
								                    	//���ñ�־ �ҵ����Լ�
								                    	flag =1;
								                    	self =domain;
								                    	self.setUser(user);
								                    	domains.add(self);
								                    }else{
								                    	 domain.setUser(user);
								                    	 domains.add(domain);
								                    }
							
								                    if(length == domains.size()){
								                    	if(flag == 1){//ֹͣ����   ���ҳ��Լ�������
									                    	Collections.sort(domains);
									                    	int weizhi =-1;
									                    	weizhi =domains.indexOf(self);
									                    	score.setText("��������ǵ�"+(weizhi+1)+"��");
									                    	
									                    }else{
									                    	score.setText("��û������Ŷ");
									                    }
								                    	
								                    }
								     
												}
											});
			  
						                }
						            } catch (JSONException e) {
						            	score.setText("��û������Ŷ");
						                e.printStackTrace();
						            }
						        } else {
						        	score.setText("��û������Ŷ");
						        }
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								// TODO Auto-generated method stub
								
							}
						});
						flag =0;//�ָ�
					}else{
						Toast.makeText(getActivity(), "������", 0).show();
					}
				}else{
					Toast.makeText(getActivity(), "���¼", 0).show();
				}
				
				break;
		//���а�
			case R.id.paihang_lay: 
				if(BmobUser.getCurrentUser(getActivity(), User.class)!=null){
					if(CommonUtils.isNetworkAvailable(getActivity())){
						CommonUtils.launchActivity(getActivity(), PaiHangActivity.class);		
					}else{
						Toast.makeText(getActivity(), "������", 0).show();
					}
				}else{
					Toast.makeText(getActivity(), "���¼", 0).show();
				}
				
				break;
		//�����˶�
			case R.id.sport_add_lay:
				if(BmobUser.getCurrentUser(getActivity(), User.class)!=null){
					CommonUtils.launchActivity(getActivity(), AddSport_Activity.class);
				}else{
					Toast.makeText(getActivity(), "���¼", 0).show();
				}
				
				break;
		//�鿴�˶�
			case R.id.sport_look_lay:
				if(BmobUser.getCurrentUser(getActivity(), User.class)!=null){
					CommonUtils.launchActivity(getActivity(), LookSport_Activity.class);
				}else{
					Toast.makeText(getActivity(), "���¼", 0).show();
				}
				
				break;
		//��ע
			case R.id.guanzhu_lay:
				if(BmobUser.getCurrentUser(getActivity(), User.class)!=null){
					if(CommonUtils.isNetworkAvailable(getActivity())){
						CommonUtils.launchActivity(getActivity(), AttentionActivity.class);	
					}else{
						Toast.makeText(getActivity(), "������", 0).show();
					}
				}else{
					Toast.makeText(getActivity(), "���¼", 0).show();
				}
				
				break;
		//���ҹ�ע
			case R.id.find_guanzhu_lay:
				if(BmobUser.getCurrentUser(getActivity(), User.class)!=null){
					if(CommonUtils.isNetworkAvailable(getActivity())){
						CommonUtils.launchActivity(getActivity(), LookAttentionActivity.class);
					}else{
						Toast.makeText(getActivity(), "������", 0).show();
					}
				}else{
					Toast.makeText(getActivity(), "���¼", 0).show();
				}
				
				break;
		//pk������
			case R.id.layout_pk:
				if(BmobUser.getCurrentUser(getActivity(), User.class)!=null){
					if(CommonUtils.isNetworkAvailable(getActivity())){
						CommonUtils.launchActivity(getActivity(), PKActivity.class);
					}else{
						Toast.makeText(getActivity(), "������", 0).show();
					}
				}else{
					Toast.makeText(getActivity(), "���¼", 0).show();
				}
				
				break;
		//�鿴ս��
			case R.id.look_pk:
				if(BmobUser.getCurrentUser(getActivity(), User.class)!=null){
					if(CommonUtils.isNetworkAvailable(getActivity())){
						View layout =View.inflate(getActivity(), R.layout.dialog_score, null);
						dialog =CommonUtils.getAlertDialog(getActivity(), layout);
						final TextView score1 =(TextView)layout.findViewById(R.id.score);
						final Button btn =(Button)layout.findViewById(R.id.score_btn);
						//�߳� ����  ����ս��
							//ʹ�������ഴ�� ����ʹ�������?
						Pk_Utils utils =new Pk_Utils();
						User user =BmobUser.getCurrentUser(getActivity(), User.class);
						utils.getSumScore(user,getActivity(), score1);
						btn.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
					}else{
						Toast.makeText(getActivity(), "������", 0).show();
					}
				}else{
					Toast.makeText(getActivity(), "���¼", 0).show();
				}
				
				break;
			}
		}
	};
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		LogUtils.i("����", "");
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
