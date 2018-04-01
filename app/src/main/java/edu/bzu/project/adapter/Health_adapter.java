package edu.bzu.project.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;

import edu.bzu.project.R;
import edu.bzu.project.domain.bmob.health_domain;
import edu.bzu.project.utils.MyApplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Health_adapter extends BaseAdapter {

	private List<health_domain> domains =null;
	private Context context =null;
	
	public Health_adapter(List<health_domain> domains, Context context) {
		super();
		this.domains = domains;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return domains.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return domains.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		viewHolder v =null;
		if(convertView == null){
			convertView =View.inflate(context, R.layout.item_health, null);
			v =new viewHolder();
			v.photo =(ImageView)convertView.findViewById(R.id.health_photo);
			v.name =(TextView)convertView.findViewById(R.id.health_name);
			v.content =(TextView)convertView.findViewById(R.id.health_context);
			convertView.setTag(v);
		}else {
			v =(viewHolder)convertView.getTag();
		}
		String uri =domains.get(position).getPhoto().getFileUrl(context);
		if(uri!=null){
			//…Ë÷√Õº∆¨
			ImageLoader.getInstance().displayImage(uri, v.photo, MyApplication.getInstance().getOptions(R.drawable.ic_launcher));
		}
		v.name.setText(domains.get(position).getName());
		v.content.setText(domains.get(position).getContent());
		return convertView;
	}
	class viewHolder{
		ImageView photo;
		TextView name;
		TextView content;
	}
}
