package edu.bzu.project.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import edu.bzu.project.R;
import edu.bzu.project.activity.BBS.AttentionActivity;
import edu.bzu.project.activity.BBS.AttentionInfoActivity;
import edu.bzu.project.adapter.AttentionAdapter;
import edu.bzu.project.adapter.PK_guanzhu_adapter;
import edu.bzu.project.adapter.PK_guanzhu_adapter.PkOnListener;
import edu.bzu.project.domain.pk_class;
import edu.bzu.project.domain.bmob.Attention;
import edu.bzu.project.domain.bmob.Map_domain;
import edu.bzu.project.domain.bmob.Pedometer_Server;
import edu.bzu.project.domain.bmob.Pk_domain;
import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.receiver.Pk_Utils;
import edu.bzu.project.utils.CommonUtils;
import edu.bzu.project.utils.ConstantValues;
import edu.bzu.project.utils.DateStyle;
import edu.bzu.project.utils.DateUtils;
import edu.bzu.project.utils.LogUtils;
import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
/**
 * pk用户关注人的Fragment
 *
 */
public class Pk_guanzhu_fragment extends BaseFragment implements PkOnListener {

	private ListView listView =null;
	private PK_guanzhu_adapter adapter =null;
	private List<Attention> lists =new LinkedList<Attention>();
	
	private double sumqianka =0;
	//对话框
	private AlertDialog dialog =null;
	//是否可以pk
	private List<pk_class> is_pks =new LinkedList<pk_class>();
	private User user_ =null;
	private int i=0;//记录人数
	@Override
	public String getFragmentName() {
		// TODO Auto-generated method stub
		return "Pk_guanzhu_fragment";
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
		View root =inflater.inflate(R.layout.item_pk, container, false);
		listView =(ListView)root.findViewById(R.id.pk_listview);
		
		//查找关注的用户
		User user =BmobUser.getCurrentUser(getActivity(), User.class);
		BmobQuery<Attention> query =new BmobQuery<Attention>();
		query.addWhereEqualTo("user", user);
		query.include("user_");
		query.findObjects(getActivity(), new FindListener<Attention>() {
			
			@Override
			public void onSuccess(List<Attention> datas) {
				// TODO Auto-generated method stub
				lists.addAll(datas);//本用户所关注的所有人
				LogUtils.i("个数", lists.size()+"");
				for(final Attention attention:lists){
					i++;
					BmobQuery<Pk_domain> dQuery =new BmobQuery<Pk_domain>();
					dQuery.addWhereEqualTo("user_", attention.getUser_());
					dQuery.order("-createdAt");
					dQuery.findObjects(getActivity(), new FindListener<Pk_domain>() {
					
						@Override
						public void onSuccess(List<Pk_domain> domains) {
							// TODO Auto-generated method stub
							//获取     注意各个信息不要错乱--异步的请求
							//列表出现的次序可能不一样，但要保证整体信息的正确性
						    pk_class pk_object =new pk_class();
							pk_object.setAttention(attention);
							if(domains.size() == 0){//没有pk过的数据
								pk_object.setIs_pk(true);
							}else{
								//是否有3天？
								Pk_domain domain =domains.get(0);
								String old =domain.getCreatedAt();
								Date old_date =DateUtils.StringToDate(old, DateStyle.YYYY_MM_DD_HH_MM_SS);
								int cha =DateUtils.getIntervalDays(Calendar.getInstance().getTime(), old_date,false);
								if(cha<=3){
									pk_object.setIs_pk(false);
								}else{
									pk_object.setIs_pk(true);
								}
							}
							is_pks.add(pk_object);
							if(i == lists.size()){
								handler2.sendEmptyMessage(0);
								LogUtils.i("发送了", lists.size()+"---"+is_pks.size());
							}
						}
						
						@Override
						public void onError(int arg0, String arg1) {
							// TODO Auto-generated method stub
							LogUtils.i("出错了", "");
						}
					});
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				LogUtils.i("展示所有关注失败", "");
			}
		});
		
		//给用户提供参考  关注的用户信息
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//
				Intent intent =new Intent(getActivity(), AttentionInfoActivity.class);
				intent.putExtra("attention", lists.get(position));
				startActivity(intent);
			}
		});
		return root;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	/**接口的 回调*/
	@Override
	public void pkListener(final User user, final User user_,final int position) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Pk_Utils utils =new Pk_Utils();
				utils.getResult(user, user_, handler,position,is_pks, getActivity());
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
			int str_result =0;//结果
			if(msg.what == 1){//胜利
				pk_result.setText("你赢了");
				str_result =1;
			}else{
				pk_result.setText("你输了");
				str_result =0;
			}
			//保存数据
			Pk_domain domain =new Pk_domain();
			domain.setUser(BmobUser.getCurrentUser(getActivity(), User.class));
			domain.setResult(str_result);
			domain.setUser_((User)msg.obj);
			domain.save(getActivity(), new SaveListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					//保存成功
					LogUtils.i("pk成功", "");
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
					//更新数据
					adapter.notifyDataSetChanged();
				}
			});
		}
	};
	
	public Handler handler2 =new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
				//封装
				adapter =new PK_guanzhu_adapter(getActivity(),is_pks);
				adapter.setListener(Pk_guanzhu_fragment.this);
				listView.setAdapter(adapter);
			}
	};
}

