package edu.bzu.project.fragment;


import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

import edu.bzu.project.R;
import edu.bzu.project.activity.BBS.AttentionActivity;
import edu.bzu.project.activity.BBS.AttentionInfoActivity;
import edu.bzu.project.activity.map.BMapActivity.MyLocationListenner;
import edu.bzu.project.adapter.PK_guanzhu_adapter.PkOnListener;
import edu.bzu.project.adapter.LookAttentiom_adapter;
import edu.bzu.project.adapter.Pk_fujin_adapter;
import edu.bzu.project.domain.bmob.Attention;
import edu.bzu.project.domain.bmob.Map_domain;
import edu.bzu.project.domain.bmob.Pk_domain;
import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.receiver.Pk_Utils;
import edu.bzu.project.utils.CommonUtils;
import edu.bzu.project.utils.LogUtils;
import edu.bzu.project.utils.MapLocalUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/***
 * �������� pk
 *
 *
 */
public class PK_fujin_fragment extends BaseFragment implements PkOnListener {

	//��λ
	private LocationClient local =null;
	private BDLocationListener myListener =new MyLocationListenner();
	//��γ��
	private BmobGeoPoint weizhi =null;
	
	//listview 
	private List<User> lists =new ArrayList<User>();;
	private List<Attention> attentions =null;
	private User user_ =null;
	private Pk_fujin_adapter adapter =null;
	private ListView listView =null;
	
	//�Ի���
	private AlertDialog dialog =null;
	@Override
	public String getFragmentName() {
		// TODO Auto-generated method stub
		return "PK_fujin_fragment";
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
		//��λ ����λ��
		View v =inflater.inflate(R.layout.item_pk, container, false);
		listView =(ListView)v.findViewById(R.id.pk_listview);
		
		local =MapLocalUtils.startLocation(myListener);
		return v;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		local.stop();
	}
	
	public class MyLocationListenner implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation bdLocation) {
			// TODO Auto-generated method stub
			if(bdLocation == null){
				LogUtils.i("��ȡλ��ʧ��", "pkʱ �����ĺ���");
				return;
			}
			//��ȡ���� λ��
			if(CommonUtils.isNetworkAvailable(getActivity())){
				//���� ��ǰλ��,�������˲���
				User user =BmobUser.getCurrentUser(getActivity(), User.class);
				User update =new User();
				weizhi =new BmobGeoPoint(bdLocation.getLongitude(), bdLocation.getLatitude());
				update.setWeizhi(weizhi);
				update.update(getActivity(), user.getObjectId(), new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						//���Ҹ�������
						findUser();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
					}
				});
				
			}else{
				Toast.makeText(getActivity(), "û������", 0).show();
			}
		}
	}
	
	private void findUser() {
		// TODO Auto-generated method stub
		//�����û� �Ȳ����ע ��ȥ�� 
 		BmobQuery<Attention> query =new BmobQuery<Attention>();
 		query.addWhereEqualTo("user", BmobUser.getCurrentUser(getActivity(), User.class));
 		query.findObjects(getActivity(), new FindListener<Attention>() {
			
			@Override
			public void onSuccess(List<Attention> attentions) {
				// TODO Auto-generated method stub
				Attention a =new Attention();
				a.setUser_(BmobUser.getCurrentUser(getActivity(), User.class));//����Լ�
				attentions.add(a);
				//ȥ����ע�ĺ��ѻ����Լ�Ŷ
				BmobQuery<User> query1 =new BmobQuery<User>();
				List<String> objects=new ArrayList<String>();
				for(Attention attention:attentions){
					objects.add(attention.getUser_().getObjectId());
				}
				query1.addWhereNear("weizhi", weizhi);
				query1.addWhereNotContainedIn("objectId", objects);
				query1.setLimit(20);
				query1.findObjects(getActivity(), new FindListener<User>() {
					
					@Override
					public void onSuccess(List<User> users) {
						// TODO Auto-generated method stub
						//�鵽����
						lists.clear();//���һ��
						lists.addAll(users);
						LogUtils.i("�������˵ĸ���", lists.size()+"");
						//
						adapter =new Pk_fujin_adapter(lists, getActivity(), weizhi);
						adapter.setListener(PK_fujin_fragment.this);
						listView.setAdapter(adapter);
						
						listView.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(
									AdapterView<?> parent, View view,
									int position, long id) {
								// TODO Auto-generated method stub
								Intent intent =new Intent(getActivity(), AttentionInfoActivity.class);
								intent.putExtra("pk_funjin", lists.get(position));
								
								startActivity(intent);
							}
						});
					}
					
					@Override
					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						
					}
				});
			}
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}	
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		
	}

	/**�ӿڵ� �ص�*/
	@Override
	public void pkListener(final User user, final User user_,final int position) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Pk_Utils utils =new Pk_Utils();
				utils.getResult(user, user_, handler,position,null,getActivity());
			}
		}).start();
	}
	
	public Handler handler =new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			View v =View.inflate(getActivity(), R.layout.dialog_pk_result, null);
			dialog =CommonUtils.getAlertDialog(getActivity(), v);
			TextView pk_result =(TextView)v.findViewById(R.id.pk_result);
			Button btn =(Button)v.findViewById(R.id.pk_btndialog);
			int str_result =0;//���
			if(msg.what == 1){//ʤ��
				pk_result.setText("��Ӯ��");
				str_result =1;
			}else{
				pk_result.setText("������");
				str_result =0;
			}
			final int position =msg.arg1;
			//��������
			Pk_domain domain =new Pk_domain();
			domain.setUser(BmobUser.getCurrentUser(getActivity(), User.class));
			domain.setResult(str_result);
			domain.setUser_((User)msg.obj);
			domain.save(getActivity(), new SaveListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					//����ɹ�
					LogUtils.i("pk�ɹ�", "");
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}
			});
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					//��������
					lists.remove(position);
					adapter.notifyDataSetChanged();
				}
			});
		}
		
	};
	
}
