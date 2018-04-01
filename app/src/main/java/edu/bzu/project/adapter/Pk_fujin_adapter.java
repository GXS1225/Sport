package edu.bzu.project.adapter;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;

import com.nostra13.universalimageloader.core.ImageLoader;

import edu.bzu.project.R;
import edu.bzu.project.adapter.AttentionAdapter.viewHolder;
import edu.bzu.project.adapter.PK_guanzhu_adapter.PkOnListener;
import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.utils.MyApplication;
import edu.bzu.project.utils.SportUtils;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Pk_fujin_adapter extends BaseAdapter{

	private List<User> users =null;
	private Context context =null;
	private BmobGeoPoint weizhi =null;//我的位置
	private PkOnListener listener =null;
	
	
	public Pk_fujin_adapter(List<User> users, Context context,BmobGeoPoint weizhi) {
		super();
		this.users = users;
		this.context = context;
		this.weizhi =weizhi;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return users.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return users.get(position);
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
			convertView =View.inflate(context, R.layout.item_fujin_pk, null);
			v =new viewHolder();
			v.icon =(ImageView) convertView.findViewById(R.id.pkfunjin_icon);
			v.name =(TextView) convertView.findViewById(R.id.pkfunjin_name);
			v.pk =(TextView)convertView.findViewById(R.id.pkfunjin_guanzhu);
			v.juli =(TextView)convertView.findViewById(R.id.pkfunjin_juli);
			convertView.setTag(v);
		}else{
			v =(viewHolder)convertView.getTag();
		}
		//头像
		BmobFile photo =users.get(position).getUserPhoto();
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
		//名称
		v.name.setText(users.get(position).getUserName());
		//距离
		v.juli.setText("相距"+SportUtils.getFormatKM(weizhi.distanceInKilometersTo(users.get(position).getWeizhi()))+"Km");
		//pk的监听
		v.pk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(listener !=null){
					listener.pkListener(BmobUser.getCurrentUser(context, User.class), users.get(position),position);
				}
			}
		});
		return convertView;
	}
	
	class viewHolder{
		ImageView icon;
		TextView name;
		TextView juli;
		TextView pk;
	}

	public PkOnListener getListener() {
		return listener;
	}

	public void setListener(PkOnListener listener) {
		this.listener = listener;
	}

	
}
