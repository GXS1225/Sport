package edu.bzu.project.activity.BBS;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import edu.bzu.project.R;
import edu.bzu.project.adapter.LookMap_adapter;
import edu.bzu.project.adapter.LookTopic_adapter;
import edu.bzu.project.domain.bmob.Attention;
import edu.bzu.project.domain.bmob.Map_domain;
import edu.bzu.project.domain.bmob.Topic;
import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.receiver.Pk_Utils;
import edu.bzu.project.utils.CommonUtils;
import edu.bzu.project.utils.LogUtils;
import edu.bzu.project.utils.MyApplication;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 用户空间的详情展示界面
 */
public class AttentionInfoActivity extends Activity {

	private Context context =null;
	//控件
	private Button back =null;
	private Button look_success =null;
	private ImageView photo =null;
	private TextView name =null;
	private TextView guanzhu =null;
	private TextView fenshi =null;
	private LinearLayout status =null;
	private LinearLayout jilu =null;
	private View indecate1 =null;
	private View indecate2 =null;
	
	private Attention attention =null;
	private User user =null;
	
	//适配器等
	private ListView listView =null;
	private LookTopic_adapter adapter =null;
	private LookMap_adapter adapter_map =null;
	private List<Topic> topics =new ArrayList<Topic>();
	
	//对话框 查看战绩
	private AlertDialog dialog =null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_attentioninfo);
		MyApplication.getInstance().addActivity(this);
		context =this;
		user =(User)getIntent().getSerializableExtra("pk_funjin");
		attention =(Attention) getIntent().getSerializableExtra("attention");
		initView();
		initDate();
		initEvent();
	}
	private void initDate() {
		// TODO Auto-generated method stub
		//加载 动态 和记录
		BmobQuery<Topic> query =new BmobQuery<Topic>();
		query.order("-createdAt");
		BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
		query.addWhereLessThan("createdAt", date);
		query.addWhereEqualTo("author", user);
		query.include("author");
		query.findObjects(context, new FindListener<Topic>() {
			
			@Override
			public void onSuccess(final List<Topic> lists) {
				// TODO Auto-generated method stub
				topics.addAll(lists);
				adapter =new LookTopic_adapter(context, topics);
				listView.setAdapter(adapter);
				if(user.getObjectId().equals(BmobUser.getCurrentUser(AttentionInfoActivity.this, User.class).getObjectId())){
					listView.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> parent,
								View view, final int position, long id) {
							// TODO Auto-generated method stub
							//长按删除
							Topic topic =new Topic();
							topic.setObjectId(topics.get(position).getObjectId());
							topic.delete(context, new DeleteListener() {
								
								@Override
								public void onSuccess() {
									// TODO Auto-generated method stub
									topics.remove(position);
									adapter.notifyDataSetChanged();
								}
								
								@Override
								public void onFailure(int arg0, String arg1) {
									// TODO Auto-generated method stub
									
								}
							});
							return false;
						}
					});
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//加载 运动的记录（百度地图的运动）
		BmobQuery<Map_domain> query2 =new BmobQuery<Map_domain>();
		query2.addWhereEqualTo("user", user);
		query2.order("-createdAt");
		query2.addWhereLessThan("createdAt", date);
		query2.findObjects(context, new FindListener<Map_domain>() {
			
			@Override
			public void onSuccess(List<Map_domain> maps) {
				// TODO Auto-generated method stub
				if(maps.size() == 0){
					adapter_map =new LookMap_adapter(null, context);
				}
				adapter_map =new LookMap_adapter(maps, context);
				listView.setAdapter(adapter_map);
				listView.setOnItemLongClickListener(null);
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	private void initEvent() {
		// TODO Auto-generated method stub
		//动态
		status.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				indecate1.setVisibility(View.VISIBLE);
				indecate2.setVisibility(View.GONE);
				if(adapter!= null){
					listView.setAdapter(adapter);
					if(user.getObjectId().equals(BmobUser.getCurrentUser(AttentionInfoActivity.this, User.class).getObjectId())){
						listView.setOnItemLongClickListener(new OnItemLongClickListener() {

							@Override
							public boolean onItemLongClick(AdapterView<?> parent,
									View view, final int position, long id) {
								// TODO Auto-generated method stub
								//长按删除
								Topic topic =new Topic();
								topic.setObjectId(topics.get(position).getObjectId());
								topic.delete(context, new DeleteListener() {
									
									@Override
									public void onSuccess() {
										// TODO Auto-generated method stub
										topics.remove(position);
										adapter.notifyDataSetChanged();
									}
									
									@Override
									public void onFailure(int arg0, String arg1) {
										// TODO Auto-generated method stub
										
									}
								});
								return false;
							}
						});
					}
					
				}
			}
		});
		//记录
		jilu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				indecate1.setVisibility(View.GONE);
				indecate2.setVisibility(View.VISIBLE);
				if(adapter_map!=null){
					listView.setAdapter(adapter_map);
					listView.setOnItemLongClickListener(null);
				}
				
			}
		});
		//返回
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyApplication.getInstance().removeActivity(AttentionInfoActivity.this);
				finish();
			}
		});
		//查看战绩
		look_success.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				View layout =View.inflate(AttentionInfoActivity.this, R.layout.dialog_score, null);
				dialog =CommonUtils.getAlertDialog(AttentionInfoActivity.this, layout);
				final TextView score =(TextView)layout.findViewById(R.id.score);
				final Button btn =(Button)layout.findViewById(R.id.score_btn);
				//线程 开启  查找战绩
				
				Pk_Utils utils =new Pk_Utils();
				utils.getSumScore(user,AttentionInfoActivity.this, score);
				btn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

			}
		});
	}
	private void initView() {
		// TODO Auto-generated method stub
		back =(Button)findViewById(R.id.attention_back);
		look_success =(Button)findViewById(R.id.look_success);
		photo =(ImageView)findViewById(R.id.attention_photo);
		name =(TextView)findViewById(R.id.attention_name);
		guanzhu =(TextView)findViewById(R.id.guanzhu);
		fenshi =(TextView)findViewById(R.id.fenshi);
		listView =(ListView)findViewById(R.id.info_listview);
		status =(LinearLayout)findViewById(R.id.attention_state);
		jilu =(LinearLayout)findViewById(R.id.attention_jilu);
		indecate1 =(View)findViewById(R.id.indecate1);
		indecate2 =(View)findViewById(R.id.indecate2);
		
		indecate2.setVisibility(View.GONE);
		
		//设置 数据 关注的数据
		if(user ==null){
			//头像
			user =attention.getUser_();
		}//为空时 是附近的人的数据
		BmobFile file =user.getUserPhoto();
		if(file != null){
			String uri =file.getFileUrl(context);
			ImageLoader.getInstance().displayImage(uri, photo, 
					MyApplication.getInstance().getOptionsCircle(R.drawable.title));
		}else{
			photo.setBackgroundResource(R.drawable.title);
		}
		//昵称
		name.setText(user.getUserName());
		//粉丝
		BmobQuery<Attention> query =new BmobQuery<Attention>();
		query.addWhereEqualTo("user_", user);
		query.count(context, Attention.class, new CountListener() {
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onSuccess(int count) {
				// TODO Auto-generated method stub
				fenshi.setText(String.valueOf(count));
			}
		});
		//关注
		BmobQuery<Attention> query1 =new BmobQuery<Attention>();
		query.addWhereEqualTo("user", user);
		query.count(context, Attention.class, new CountListener() {
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onSuccess(int count) {
				// TODO Auto-generated method stub
				guanzhu.setText(String.valueOf(count));
			}
		});
		
	}
}
