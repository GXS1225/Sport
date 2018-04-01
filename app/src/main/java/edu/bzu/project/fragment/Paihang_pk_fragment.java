package edu.bzu.project.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindStatisticsListener;
import cn.bmob.v3.listener.GetListener;
import edu.bzu.project.R;
import edu.bzu.project.adapter.PaiHang_adapter;
import edu.bzu.project.adapter.PaiHang_pk_adapter;
import edu.bzu.project.domain.PaiHang_domain;
import edu.bzu.project.domain.bmob.Map_domain;
import edu.bzu.project.domain.bmob.Pk_domain;
import edu.bzu.project.domain.bmob.User;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
/**
 * 排行之pk的Fragment
 *
 */
public class Paihang_pk_fragment extends BaseFragment{

	private List<PaiHang_domain> domains =null;
	private ListView listView =null;
	private PaiHang_pk_adapter adapter =null;
	@Override
	public String getFragmentName() {
		// TODO Auto-generated method stub
		return "Paihang_pk_fragment";
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View root =inflater.inflate(R.layout.item_pk, container, false);
		listView =(ListView)root.findViewById(R.id.pk_listview);

//		//查找pk的排行榜
		BmobQuery<Pk_domain> query =new BmobQuery<Pk_domain>();
		query.sum(new String[]{"result"});
		query.groupby(new String[]{"user"});
		query.setHasGroupCount(true);
		query.findStatistics(getActivity(), Pk_domain.class, new FindStatisticsListener() {
			
			@Override
			public void onSuccess(Object object) {
				// TODO Auto-generated method stub
				JSONArray ary = (JSONArray) object;
		        if(ary!=null){
		            final int length = ary.length();
		            try {
		            	if(domains ==null){
		            		domains =new ArrayList<PaiHang_domain>();
		            	}
		                for (int i = 0; i < length; i++) {
		                    JSONObject obj = ary.getJSONObject(i);
		                    final double sumScore =obj.getDouble("_sumResult");
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
				                    	Collections.sort(domains);
						                adapter =new PaiHang_pk_adapter(getActivity(), domains);
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
