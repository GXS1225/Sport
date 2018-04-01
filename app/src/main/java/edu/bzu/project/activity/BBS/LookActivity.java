package edu.bzu.project.activity.BBS;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;


import com.baidu.a.a.a.c;
import com.nostra13.universalimageloader.core.ImageLoader;

import edu.bzu.project.R;
import edu.bzu.project.adapter.LookTopic_adapter;
import edu.bzu.project.domain.bmob.Attention;
import edu.bzu.project.domain.bmob.Topic;
import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.domain.bmob.group;

import edu.bzu.project.utils.CommonUtils;

import edu.bzu.project.utils.ConstantValues;
import edu.bzu.project.utils.LogUtils;
import edu.bzu.project.utils.MyApplication;
import edu.bzu.project.view.LoadMoreListView;
import edu.bzu.project.view.LoadMoreListView.OnLoadMore;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.method.QwertyKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 查看话题空间动态的界面
 */
public class LookActivity extends Activity implements OnRefreshListener,OnLoadMore{

	private static String TAG ="LookActivity";
	
	private Button back =null;
	private Button add_topic =null;
	private TextView group_title =null;
	
	private ImageView group_icon =null;
	private TextView group_name =null;
	private TextView group_geyan =null;
	
	private SwipeRefreshLayout swiplay =null;
	private LoadMoreListView load_listview= null;
	
	private group item =null;
	private List<Topic> lists =null;//话题

	private LookTopic_adapter adapter =null;
	private int pageNum;
	
	//状态
	public enum RefreshType{
		REFRESH,LOAD_MORE
	}
	private RefreshType mRefreshType = RefreshType.LOAD_MORE;//默认下拉
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_look_group);
		MyApplication.getInstance().addActivity(this);
		
		item =(group) getIntent().getSerializableExtra("group");
		initView();
		initEvent();
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		back.setOnClickListener(onClick);
		add_topic.setOnClickListener(onClick);
		load_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent commit =new Intent(LookActivity.this,CommentActivity.class);
				commit.putExtra("tipic", lists.get(position));
				startActivity(commit);
			}
		});
	}
	OnClickListener onClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.addtopic_back:
				MyApplication.getInstance().removeActivity(LookActivity.this);
				finish();
				break;
			case R.id.addtopic_add:
				//把组的数据 传入
				Intent intent = new Intent(LookActivity.this,EditActivity.class);
				Bundle b =new Bundle();
				b.putSerializable("group", item);
				intent.putExtras(b);
				startActivity(intent);
				break;
			}
		}
	};

	private void initView() {
		// TODO Auto-generated method stub
		back =(Button)findViewById(R.id.addtopic_back);
		add_topic =(Button)findViewById(R.id.addtopic_add);
		group_title =(TextView)findViewById(R.id.group_title);
		group_title.setText(item.getGroupName());
		group_icon =(ImageView)findViewById(R.id.group_icon);
		group_name =(TextView)findViewById(R.id.group_name);
		group_geyan =(TextView)findViewById(R.id.group_geyan);
		if(item!=null){
			ImageLoader.getInstance().displayImage(item.getGroupIcon().getFileUrl(LookActivity.this), group_icon, 
					MyApplication.getInstance().getOptions(R.drawable.ic_launcher));
			group_name.setText(item.getGroupName());
			group_geyan.setText(item.getGroupGeyan());
		}
		swiplay =(SwipeRefreshLayout)findViewById(R.id.swip_lay);
		load_listview =(LoadMoreListView)findViewById(R.id.topic_listView);
		//设置监听器
		load_listview.setOnLoadMore(this);
		swiplay.setOnRefreshListener(this);
		swiplay.setColorScheme(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
				android.R.color.holo_green_light);
		
		//查询数据,联网
		if(CommonUtils.isNetworkAvailable(LookActivity.this)){
			lists =new ArrayList<Topic>();
			adapter =new LookTopic_adapter(LookActivity.this, lists);
			if(lists.size() == 0){
				selectDate();
			}
			load_listview.setAdapter(adapter);
		}else{
			Toast.makeText(LookActivity.this, "请联网",Toast.LENGTH_LONG).show();
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

	//上拉加载
	@Override
	public void loadMore() {
		// TODO Auto-generated method stub
		if(CommonUtils.isNetworkAvailable(LookActivity.this)){
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					mRefreshType =RefreshType.LOAD_MORE;
					selectDate();
					load_listview.onLoadComplete();
				}
			}, 2000);
		}else {
			Toast.makeText(LookActivity.this, "请联网",Toast.LENGTH_LONG).show();
		}	
	}

	//下拉刷新
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if(CommonUtils.isNetworkAvailable(LookActivity.this)){
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					mRefreshType =RefreshType.REFRESH;
					pageNum = 0;//重新数据的加载页数，后面会情况数据
					selectDate();
					swiplay.setRefreshing(false);//停止
				}
			}, 2000);
		}else {
			Toast.makeText(LookActivity.this, "请联网",Toast.LENGTH_LONG).show();
		}
	}
	
	public void selectDate(){
		BmobQuery<Topic> query = new BmobQuery<Topic>();
		query.order("-createdAt");
		query.setLimit(ConstantValues.PAGE_SIZE);
		BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
		query.addWhereLessThan("createdAt", date);
		query.addWhereEqualTo("group", item);
		query.setSkip(ConstantValues.PAGE_SIZE *(pageNum++));
		query.include("author");
		
		query.findObjects(LookActivity.this, new FindListener<Topic>() {
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				LogUtils.i(TAG, "话题数据加载失败");
				pageNum--;
			}

			@Override
			public void onSuccess(List<Topic> lists_) {
				// TODO Auto-generated method stub
				//判断是否有数据
				if(lists_.size()!=0 && lists_.get(lists_.size()-1)!=null){
					//判断 上啦 下拉后  数据的不同变化
					if(mRefreshType == RefreshType.REFRESH){
						//下拉刷新时，重新加载数据
						lists.clear();
					}
					if(lists_.size()<ConstantValues.PAGE_SIZE){
						//上拉加载的时候  数据已经加载完后  要提示
						Toast.makeText(LookActivity.this, "亲，往下没有数据了~",Toast.LENGTH_LONG).show();
					}
					lists.addAll(lists_);//加载上查出的数据
					adapter.notifyDataSetChanged();
				}else{
					Toast.makeText(LookActivity.this, "没有数据",Toast.LENGTH_LONG).show();
					pageNum--;//重复最后一页 效果
				}
			}
		});
	}
}
