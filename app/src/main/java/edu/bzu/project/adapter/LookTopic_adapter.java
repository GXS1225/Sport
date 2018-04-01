package edu.bzu.project.adapter;

import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.SaveListener;

import com.baidu.a.a.a.a;
import com.nostra13.universalimageloader.core.ImageLoader;

import edu.bzu.project.R;
import edu.bzu.project.activity.BBS.CommentActivity;
import edu.bzu.project.domain.bmob.Attention;
import edu.bzu.project.domain.bmob.Topic;
import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.utils.DateStyle;
import edu.bzu.project.utils.DateUtils;
import edu.bzu.project.utils.LogUtils;
import edu.bzu.project.utils.MyApplication;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LookTopic_adapter extends BaseAdapter {

	private Context context =null;
	private List<Topic> lists =null;
	
	

	public LookTopic_adapter(Context context, List<Topic> lists) {
		super();
		this.context = context;
		this.lists = lists;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		viewHolder v =null;
		if(convertView == null){
			convertView =View.inflate(context, R.layout.item_looktopic, null);
			v =new viewHolder();
			v.user_icon =(ImageView)convertView.findViewById(R.id.user_logo);
			v.user_name =(TextView)convertView.findViewById(R.id.user_name);
			v.topic_time =(TextView)convertView.findViewById(R.id.topic_time);
			
			v.topic_content =(TextView)convertView.findViewById(R.id.content_text);
			v.photo1 =(ImageView)convertView.findViewById(R.id.topic1);
			v.photo2 =(ImageView)convertView.findViewById(R.id.topic2);
			v.photo3 =(ImageView)convertView.findViewById(R.id.topic3);
			convertView.setTag(v);
		}else{
			v =(viewHolder)convertView.getTag();
		}
		//加载数据了
		//加载头像
		BmobFile photo =lists.get(position).getAuthor().getUserPhoto();
		if(photo!=null){
			String uri =photo.getFileUrl(context);
			if(uri!=null){
				ImageLoader.getInstance().displayImage(uri, v.user_icon, 
						MyApplication.getInstance().getOptions(R.drawable.user_icon_default_main));
			}
		}else{
			ImageLoader.getInstance().displayImage(null, v.user_icon, 
					MyApplication.getInstance().getOptions(R.drawable.title));
		}
		
		v.user_name.setText(lists.get(position).getAuthor().getUserName());
		String time =lists.get(position).getCreatedAt();
		String currentTime =DateUtils.StringToString(DateUtils.DateToString(Calendar.getInstance()
				.getTime(), DateStyle.YYYY_MM_DD_HH_MM_SS),  DateStyle.YYYY_MM_DD_HH_MM_SS);
		v.topic_time.setText(DateUtils.getDistanceTime(time,currentTime));
		v.topic_content.setText(lists.get(position).getTopicContent());
		//图片
		if(lists.get(position).getTopoicPhotos()!=null){
			ImageView[] views =new ImageView[]{v.photo1,v.photo2,v.photo3};
			List<BmobFile> files =lists.get(position).getTopoicPhotos();
			LogUtils.i("上传的图片个数", files.size()+"");
			for(int i =0 ;i<files.size();i++){
				views[i].setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(files.get(i).getFileUrl(context), 
						views[i], MyApplication.getInstance().getOptions(R.drawable.ic_launcher));
			}
		}
		return convertView;
	}
	class viewHolder{
		public ImageView user_icon;
		public TextView user_name;
		public TextView topic_time;
		public TextView topic_content;
		public ImageView photo1;
		public ImageView photo2;
		public ImageView photo3;
	}
}
