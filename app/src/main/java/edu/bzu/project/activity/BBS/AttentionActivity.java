package edu.bzu.project.activity.BBS;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import edu.bzu.project.R;
import edu.bzu.project.adapter.AttentionAdapter;
import edu.bzu.project.domain.bmob.Attention;
import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.utils.LogUtils;
import edu.bzu.project.utils.MyApplication;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
/**
 * 我的关注好友界面
 */
public class AttentionActivity extends Activity {
	private static final String TAG ="AttentionActivity";
	
	private Button back =null;
	private ListView listView =null;
	private AttentionAdapter adapter =null;
	
	private List<Attention> lists =null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guanzhu);
		initView();
		initData();
		initEvent();
	}
	private void initEvent() {
		// TODO Auto-generated method stub
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyApplication.getInstance().removeActivity(AttentionActivity.this);
				finish();
			}
		});
		
		//点击
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//查看好友的动态
				Intent intent =new Intent(AttentionActivity.this, AttentionInfoActivity.class);
				intent.putExtra("attention", lists.get(position));
				startActivity(intent);
			}
		});
	}
	private void initData() {
		// TODO Auto-generated method stub
		if(lists == null){
			lists = new ArrayList<Attention>();
			User user =BmobUser.getCurrentUser(AttentionActivity.this, User.class);
			BmobQuery<Attention> query =new BmobQuery<Attention>();
			query.addWhereEqualTo("user", user);
			query.include("user_");
			query.findObjects(AttentionActivity.this, new FindListener<Attention>() {
				
				@Override
				public void onSuccess(List<Attention> datas) {
					// TODO Auto-generated method stub
					lists.addAll(datas);//本用户所关注的所有人
					LogUtils.i("展示所有关注", lists.size()+"个");
					adapter =new AttentionAdapter(AttentionActivity.this, lists);
					listView.setAdapter(adapter);
				}
				
				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					LogUtils.i("展示所有关注失败", "");
				}
			});
		}
	}
	private void initView() {
		// TODO Auto-generated method stub
		back =(Button)findViewById(R.id.guanzhu_back);
		listView =(ListView)findViewById(R.id.listview_guanzhu);
	}
}
