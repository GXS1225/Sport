package edu.bzu.project.activity.BBS;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.w3c.dom.ls.LSInput;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.FindStatisticsListener;

import edu.bzu.project.R;
import edu.bzu.project.adapter.LookAttentiom_adapter;
import edu.bzu.project.domain.bmob.Attention;
import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.utils.MyApplication;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
/**
 * 查找关注好友的界面
 */
public class LookAttentionActivity extends FragmentActivity {

	private Context context =null;
	private Button back =null;
	
	private SearchView friend_search =null;
	private ListView friend_listview =null;
	//适配器
	private List<User> lists =new ArrayList<User>();//推荐好友
	private List<User> AllUsers =new ArrayList<User>();//全部的好友
	private LookAttentiom_adapter adapter =null;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		context =this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplication.getInstance().addActivity(LookAttentionActivity.this);
		setContentView(R.layout.activity_lookattention);
		initView();
		initData();
		initEvent();
	}
	private void initEvent() {
		// TODO Auto-generated method stub
		//
		friend_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent =new Intent(LookAttentionActivity.this,AttentionInfoActivity.class);
				intent.putExtra("pk_funjin", lists.get(position));
				startActivity(intent);
			}
			
		});
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyApplication.getInstance().removeActivity(LookAttentionActivity.this);
				finish();
			}
		});
		//模糊查询
		friend_search.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String newText) {
				// TODO Auto-generated method stub
				
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(newText)){
					adapter =new LookAttentiom_adapter(context, lists.subList(0, lists.size()>10?10:lists.size()/2));//按顺序截取，可设计各种规则
					friend_listview.setAdapter(adapter);
				}else{
					
				}
				return false;
			}
		});
		
	}
 	private void initData() {
		// TODO Auto-generated method stub
 		if(lists == null){
 			lists =new ArrayList<User>();
 		}
 		findUser();
	}
	private void findUser() {
		// TODO Auto-generated method stub
		//推荐好友    去除以关注的好友
		//查找 全部的好友
 		BmobQuery<Attention> query =new BmobQuery<Attention>();
 		query.addWhereEqualTo("user", BmobUser.getCurrentUser(context, User.class));
 		query.include("user_");
 		query.findObjects(context, new FindListener<Attention>() {
			
			@Override
			public void onSuccess(List<Attention> attentions) {
				// TODO Auto-generated method stub
				Attention a =new Attention();
				a.setUser_(BmobUser.getCurrentUser(context, User.class));//添加自己
				attentions.add(a);
				//去除关注的好友还有自己哦
				BmobQuery<User> query =new BmobQuery<User>();
				List<String> objects=new ArrayList<String>();
				for(Attention attention:attentions){
					objects.add(attention.getUser_().getObjectId());
				}
				query.addWhereNotContainedIn("objectId", objects);
				query.findObjects(context, new FindListener<User>() {
					
					@Override
					public void onSuccess(List<User> users) {
						// TODO Auto-generated method stub
						//查到好友
						lists.clear();
						lists.addAll(users);
						if(adapter!=null){
							adapter.notifyDataSetChanged();
						}else{
							adapter =new LookAttentiom_adapter(context, lists.subList(0, lists.size()>10?10:lists.size()/2));//按顺序截取，可设计各种规则
							friend_listview.setAdapter(adapter);
						}
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
	private void initView() {
		// TODO Auto-generated method stub
		back =(Button)findViewById(R.id.friend_back);
		friend_search =(SearchView)findViewById(R.id.friend_search);
		friend_listview =(ListView)findViewById(R.id.friend_listview);
		friend_search.setSubmitButtonEnabled(true);//显示搜索
	}
}
