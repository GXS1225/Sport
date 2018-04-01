package edu.bzu.project.adapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;

import com.baidu.a.a.a.c;
import com.baidu.location.am;
import com.nostra13.universalimageloader.core.ImageLoader;

import edu.bzu.project.R;
import edu.bzu.project.domain.PaiHang_domain;
import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.utils.MyApplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PaiHang_adapter extends BaseAdapter {

	private Context context =null;
	private List<PaiHang_domain> lists =null;
	
	public PaiHang_adapter(Context context, List<PaiHang_domain> lists) {
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
			convertView =View.inflate(context, R.layout.item_paihang, null);
			v =new viewHolder();
			v.icon =(ImageView)convertView.findViewById(R.id.paihang_icon);
			v.name =(TextView)convertView.findViewById(R.id.paihang_name);
			v.juli =(TextView)convertView.findViewById(R.id.paihang_juli);
			v.paihang =(TextView)convertView.findViewById(R.id.paihang);
			convertView.setTag(v);
		 }else{
			 v =(viewHolder)convertView.getTag();
		 }
		
		BmobFile photo =lists.get(position).getUser().getUserPhoto();
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
		v.name.setText(lists.get(position).getUser().getUserName());
		v.juli.setText("总距离"+lists.get(position).getSumScore()+"公里");
		v.paihang.setText("第"+(position+1)+"名");
		return convertView;
	}
	class viewHolder{
		ImageView icon;
		TextView name;
		TextView juli;
		TextView paihang;
	}

}
