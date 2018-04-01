package edu.bzu.project.fragment;

import java.util.ArrayList;
import java.util.List;


import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import edu.bzu.project.R;
import edu.bzu.project.activity.BBS.Health_InfoActivity;
import edu.bzu.project.activity.BBS.LookActivity;
import edu.bzu.project.adapter.Group_gridAdapter;
import edu.bzu.project.adapter.Health_adapter;
import edu.bzu.project.domain.bmob.group;
import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.domain.bmob.health_domain;
import edu.bzu.project.utils.CommonUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 主界面之小组fragment
 *
 */
public class GroupFragment extends Fragment implements OnRefreshListener{

	private static String TAG ="GroupFragment";
	
	private Context context =null;
	//控件
	private Button group1 =null;
	private Intent intent =null;
	private TextView group_name =null;
	private GridView group_view =null;
	private Group_gridAdapter gridadapter =null;
	
	private SwipeRefreshLayout groupswip_lay =null;
	private ListView listView =null;
	private Health_adapter health_adapter =null; 
	
	private List<health_domain> domains =new ArrayList<health_domain>();
	private ArrayList<group> groups =new ArrayList<group>();;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		context =activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootv =inflater.inflate(R.layout.fragemnt_group, container, false);
		groupswip_lay =(SwipeRefreshLayout)rootv.findViewById(R.id.group_refresh);
		group_name =(TextView)rootv.findViewById(R.id.group_name);
		group_view =(GridView)rootv.findViewById(R.id.group_gird);
		listView =(ListView)rootv.findViewById(R.id.group_listView);
		groupswip_lay.setOnRefreshListener(this);
		groupswip_lay.setColorScheme(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
				android.R.color.holo_green_light);
		//查找数据
		if(BmobUser.getCurrentUser(getActivity(), User.class)!=null){
			if(CommonUtils.isNetworkAvailable(getActivity())){
				selectDate();
				initEvent();
			}else{
				TextView text =new TextView(getActivity());
				text.setText("什么也没有");
				group_view.setEmptyView(text);
			}
		}else{
			group_name.setText("请登录...");
		}
		return rootv;
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		group_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent i =new Intent(context,LookActivity.class);
				Bundle b =new Bundle();
				b.putSerializable("group", groups.get(position));
				i.putExtras(b);
				startActivity(i);
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent ii =new Intent(getActivity(),Health_InfoActivity.class);
				ii.putExtra("healthinfo", domains.get(position));
				startActivity(ii);
			}
			
		});
	}

	private void selectDate() {
		// TODO Auto-generated method stub
		//查找的组数据
		BmobQuery<group> query =new BmobQuery<group>();
		query.order("groupType");
		query.findObjects(getActivity(), new FindListener<group>() {
			@Override
			public void onSuccess(List<group> lists) {
				// TODO Auto-generated method stub
				if(lists!=null){
					groups.clear();
					groups.addAll(lists);
					gridadapter =new Group_gridAdapter(groups,getActivity());
					group_view.setAdapter(gridadapter);
				}
			}
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "出错了", Toast.LENGTH_LONG).show();
			}
		});
		//查找健康知识
		BmobQuery<health_domain> query2 =new BmobQuery<health_domain>();
		query2.findObjects(getActivity(), new FindListener<health_domain>() {
			
			@Override
			public void onSuccess(List<health_domain> lists) {
				// TODO Auto-generated method stub
				if(lists!=null){
					domains.clear();
					domains.addAll(lists);
					group_name.setText("运动小知识");
					health_adapter =new Health_adapter(domains, getActivity());
					listView.setAdapter(health_adapter);
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	//下拉刷新
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if(CommonUtils.isNetworkAvailable(getActivity())){
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					selectDate();
					initEvent();
					groupswip_lay.setRefreshing(false);//停止
				}
			}, 2000);
		}else {
			Toast.makeText(getActivity(), "请联网", Toast.LENGTH_LONG).show();
		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//查找数据
		if(BmobUser.getCurrentUser(getActivity(), User.class)!=null){
			if(CommonUtils.isNetworkAvailable(getActivity())){
				selectDate();
				initEvent();
			}else{
				TextView text =new TextView(getActivity());
				text.setText("什么也没有");
				group_view.setEmptyView(text);
			}
		}else{
			group_name.setText("请登录...");
		}
		
	}
}
