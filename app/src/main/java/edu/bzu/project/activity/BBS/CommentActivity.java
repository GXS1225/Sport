package edu.bzu.project.activity.BBS;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.nostra13.universalimageloader.core.ImageLoader;

import edu.bzu.project.R;
import edu.bzu.project.adapter.LookTopic_adapter;
import edu.bzu.project.adapter.Topic_Comment;
import edu.bzu.project.domain.bmob.Attention;
import edu.bzu.project.domain.bmob.Commit;
import edu.bzu.project.domain.bmob.Topic;
import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.utils.CommonUtils;
import edu.bzu.project.utils.DateStyle;
import edu.bzu.project.utils.DateUtils;
import edu.bzu.project.utils.LogUtils;
import edu.bzu.project.utils.MyApplication;
import android.app.Activity;
import android.app.DownloadManager.Query;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 评论的界面
 */
public class CommentActivity extends Activity {

	private static String TAG ="CommentActivity";
	
	private Context context ;
	private Button back =null;
	private ImageView user_icon =null;
	private TextView user_name =null;
	private TextView user_time =null;
	private TextView is_guanzhu =null;
	private TextView user_content =null;
	private ImageView photo1 =null;
	private ImageView photo2 =null;
	private ImageView photo3 =null;
	
	private ListView listView =null;
	private Topic_Comment adapter =null;
	private EditText comment =null;
	private Button btn_comment =null;
	
	private Topic item =null;
	private List<Commit> commits =null;
	private List<Attention> attentions =null;//关注
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_comment);
		if(CommonUtils.isNetworkAvailable(this)){
			item =(Topic) getIntent().getSerializableExtra("tipic");
			if(item !=null){
				context =this;
				initView();
				initDate();
			}
		}
		initEvent();
		
	}
	private void initEvent() {
		// TODO Auto-generated method stub
		is_guanzhu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Attention a =null;
				a =(Attention) is_guanzhu.getTag();
				if(a == null){
					//添加
					final Attention aa =new Attention();
					aa.setUser((User)BmobUser.getCurrentUser(CommentActivity.this, User.class));
					aa.setUser_(item.getAuthor());
					aa.save(CommentActivity.this, new SaveListener() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							is_guanzhu.setTag(aa);
							is_guanzhu.setBackgroundResource(R.drawable.friend_unfollow);
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							
						}
					});
				}else{//删除
					Attention delete =new Attention();
					delete.setObjectId(a.getObjectId());
					delete.delete(CommentActivity.this, new DeleteListener() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							is_guanzhu.setTag(null);
							is_guanzhu.setBackgroundResource(R.drawable.friend_follow);
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							
						}
					});
				}
			}
		});
		btn_comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(CommonUtils.isNetworkAvailable(context)){
					/*保存评论数据*/
					String commit_str =comment.getText().toString().trim();
					if(!TextUtils.isEmpty(commit_str)){
						User user =BmobUser.getCurrentUser(context, User.class);
						if(user == null){
							Toast.makeText(context, "请登录", 1000).show();
						}else{
							final Commit c =new Commit();
							c.setUser(user);
							c.setTopic(item);
							c.setCommentContent(commit_str);
							c.save(context, new SaveListener() {
								
								@Override
								public void onSuccess() {
									// TODO Auto-generated method stub
									Toast.makeText(context, "评论成功!", 1000).show();
									selectCommits();
									adapter.notifyDataSetChanged();
								}
								@Override
								public void onFailure(int arg0, String arg1) {
									// TODO Auto-generated method stub
									
								}
							});
						}
					}else{
						Toast.makeText(context, "请填写评论", 1000).show();
					}
				}else{
					Toast.makeText(context, "亲,请连接网络", 1000).show();
				}
			}
		});
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyApplication.getInstance().removeActivity(CommentActivity.this);
				finish();
			}
		});
	}
	private void initDate() {
		// TODO Auto-generated method stub
		/*话题的信息*/
		user_name.setText(item.getAuthor().getUserName());
		BmobFile photo =item.getAuthor().getUserPhoto();
		if(photo!=null){
			String uri =photo.getFileUrl(context);
			if(uri!=null){
				ImageLoader.getInstance().displayImage(uri, user_icon, 
						MyApplication.getInstance().getOptions(R.drawable.user_icon_default_main));
			}
		}else{
			ImageLoader.getInstance().displayImage(null, user_icon, 
					MyApplication.getInstance().getOptions(R.drawable.title));
		}

		String time =item.getCreatedAt();
		String currentTime =DateUtils.StringToString(DateUtils.DateToString(Calendar.getInstance()
				.getTime(), DateStyle.YYYY_MM_DD_HH_MM_SS),  DateStyle.YYYY_MM_DD_HH_MM_SS);
		user_time.setText(DateUtils.getDistanceTime(time, currentTime));
		user_content.setText(item.getTopicContent());
		if(item.getTopoicPhotos()!=null){
			ImageView[] views =new ImageView[]{photo1,photo2,photo3};
			List<BmobFile> files =item.getTopoicPhotos();
			for(int i =0 ;i<item.getTopoicPhotos().size();i++){
				views[i].setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(files.get(i).getFileUrl(CommentActivity.this), 
						views[i], MyApplication.getInstance().getOptions(R.drawable.bg_pic_loading));
			}
		}
		/*评论的信息*/
		adapter =new Topic_Comment(commits, CommentActivity.this);
		//设置没有时
		listView.setAdapter(adapter);
		
		
	}
	private void initView() {
		// TODO Auto-generated method stub
		back =(Button) findViewById(R.id.comment_back);
		user_icon =(ImageView) findViewById(R.id.user_logo);
		user_time =(TextView) findViewById(R.id.topic_time);
		user_name =(TextView) findViewById(R.id.user_name);
		is_guanzhu =(TextView) findViewById(R.id.is_guanzhu);
		user_content =(TextView) findViewById(R.id.content_text);
		photo1 =(ImageView) findViewById(R.id.topic1);
		photo2 =(ImageView) findViewById(R.id.topic2);
		photo3 =(ImageView) findViewById(R.id.topic3);
		
		listView =(ListView)findViewById(R.id.comment_list);
		comment =(EditText)findViewById(R.id.comment_content);
		btn_comment =(Button)findViewById(R.id.comment_commit);
		//评论的数据
		if(commits == null){
			commits =new ArrayList<Commit>();
			selectCommits();
		}
		if(attentions == null){
			attentions =new ArrayList<Attention>();
			selectAttention();
		}
	}
	private void selectAttention() {
		// TODO Auto-generated method stub
		User user =BmobUser.getCurrentUser(CommentActivity.this, User.class);
		BmobQuery<Attention> query =new BmobQuery<Attention>();
		query.addWhereEqualTo("user", user);
		query.include("user_");
		query.findObjects(CommentActivity.this, new FindListener<Attention>() {
			
			@Override
			public void onSuccess(List<Attention> lists) {
				// TODO Auto-generated method stub
				attentions.addAll(lists);//本用户所关注的所有人
				LogUtils.i("成功了关注", attentions.size()+"个");
				//关注
				User user =BmobUser.getCurrentUser(CommentActivity.this, User.class);
				if(user.getObjectId().equals(item.getAuthor().getObjectId())){
					
				}else{
					if(attentions.size() == 0){
						LogUtils.i("已经关注", attentions.size()+"");
						is_guanzhu.setVisibility(View.VISIBLE);
						is_guanzhu.setBackgroundResource(R.drawable.friend_follow);
					}else{
						for(Attention attention : attentions){
							if(attention.getUser_().getObjectId().equals(
									item.getAuthor().getObjectId())){
								is_guanzhu.setVisibility(View.VISIBLE);
								LogUtils.i("已经关注", "");
								is_guanzhu.setBackgroundResource(R.drawable.friend_unfollow);
								is_guanzhu.setTag(attention);//存入Tag 可以用于删除
							}
						}
					}
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				LogUtils.i("失败了", "关注");
			}
		});
	}
	private void selectCommits() {
		// TODO Auto-generated method stub
		BmobQuery<Commit> query =new BmobQuery<Commit>();
		query.addWhereEqualTo("topic", item);
		query.order("-createdAt");
		//先显示出来评论的内容吧
		query.findObjects(CommentActivity.this, new FindListener<Commit>() {
			
			public void onSuccess(List<Commit> lists) {
				// TODO Auto-generated method stub
				commits.addAll(lists);
				LogUtils.i("评论的条数", lists.size()+"");
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				LogUtils.i("读取评论出错", "");
			}
		});
		
	}
}
