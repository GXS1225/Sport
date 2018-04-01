package edu.bzu.project.adapter;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.SaveListener;

import com.nostra13.universalimageloader.core.ImageLoader;

import edu.bzu.project.R;
import edu.bzu.project.adapter.AttentionAdapter.viewHolder;
import edu.bzu.project.domain.bmob.Attention;
import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.utils.LogUtils;
import edu.bzu.project.utils.MyApplication;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class LookAttentiom_adapter extends BaseAdapter {

	private Context context =null;
	private List<User> lists =null;
	
	
	public LookAttentiom_adapter(Context context, List<User> lists) {
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
		if (convertView == null) {
			convertView =View.inflate(context, R.layout.item_attention, null);
			v =new viewHolder();
			v.icon =(ImageView) convertView.findViewById(R.id.attention_icon);
			v.name =(TextView) convertView.findViewById(R.id.attention_name);
			v.guanzhu =(TextView)convertView.findViewById(R.id.attention_guanzhu);
			v.attention_guanzhuphoto =(ImageButton)convertView.findViewById(R.id.attention_guanzhuphoto);
			v.guanzhu.setVisibility(View.GONE);
			v.attention_guanzhuphoto.setVisibility(View.VISIBLE);
			convertView.setTag(v);
		}else{
			v =(viewHolder)convertView.getTag();
		}
		BmobFile photo =lists.get(position).getUserPhoto();
		if(photo != null){
			String uri =photo.getFileUrl(context);
			if(uri !=null){
				ImageLoader.getInstance().displayImage(uri, v.icon, 
						MyApplication.getInstance().getOptionsCircle(R.drawable.user_icon_default_main));
			}
		}else{
			ImageLoader.getInstance().displayImage(null, v.icon, 
					MyApplication.getInstance().getOptionsCircle(R.drawable.title));
		}
		
		v.name.setText(lists.get(position).getUserName());
		v.attention_guanzhuphoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final ImageButton attention_guanzhuphoto =(ImageButton)v;
				Attention attention =new Attention();
				attention.setUser(BmobUser.getCurrentUser(context, User.class));
				attention.setUser_(lists.get(position));
				attention.save(context, new SaveListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
					
						attention_guanzhuphoto.setImageResource(R.drawable.friend_unfollow);
						attention_guanzhuphoto.setEnabled(false);
					}
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						
					}
				});	
			}
		});
		
		return convertView;
	}
	
	class viewHolder{
		ImageView icon;
		TextView name;
		TextView guanzhu;
		ImageButton attention_guanzhuphoto;
	}

}
