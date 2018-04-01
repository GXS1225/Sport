package edu.bzu.project.fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.FindStatisticsListener;
import cn.bmob.v3.listener.GetListener;

import edu.bzu.project.R;
import edu.bzu.project.adapter.PaiHang_adapter;
import edu.bzu.project.domain.PaiHang_domain;
import edu.bzu.project.domain.bmob.Map_domain;
import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.utils.LogUtils;

import android.app.Activity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
/**
 * 排行之运动量的Fragment
 *
 */
public class PaiHang_fragment extends BaseFragment {

	private ListView listView =null;
	private PaiHang_adapter adapter =null;
	
	//集合
	private List<PaiHang_domain> domains =null;
	
	@Override
	public String getFragmentName() {
		// TODO Auto-generated method stub
		return "PaiHang_fragment";
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
		
		//排行,先查出人 在查各自的东西
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
				 
		        if(ary!=null){
		            final int length = ary.length();
		            List<String> userList =new ArrayList<String>();
		            try {
		            	if(domains ==null){
		            		domains =new ArrayList<PaiHang_domain>();
		            	}
		                for (int i = 0; i < length; i++) {
		                    JSONObject obj = ary.getJSONObject(i);
		                    final double sumScore =obj.getDouble("_sumJuli");
		                    JSONObject userobj =obj.getJSONObject("user");
		                    String user_str =userobj.getString("objectId");
		                    //查人
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
									domain.setUser(user);
				                    domains.add(domain);
				                    if(length == domains.size()){
				                    	//适配器
				                    	//排序
				                    	Collections.sort(domains);
						                adapter =new PaiHang_adapter(getActivity(), domains);
						                listView.setAdapter(adapter);
				                    }
								}
							});
		                    
		                }
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
		return root;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
}
