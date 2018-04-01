package edu.bzu.project.adapter;

import java.util.List;

import com.baidu.platform.comapi.map.e;
import com.nostra13.universalimageloader.core.ImageLoader;

import edu.bzu.project.R;
import edu.bzu.project.domain.bmob.group;
import edu.bzu.project.utils.MyApplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Group_gridAdapter extends BaseAdapter {

	private List<group> lists =null;
	private Context context =null;
	
	public Group_gridAdapter(List<group> lists, Context context) {
		super();
		this.lists = lists;
		this.context = context;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		viewHolder v =null;
		if(convertView == null){
			convertView =View.inflate(context, R.layout.item_grid_group, null);
			v =new viewHolder();
			v.group_icon =(ImageView)convertView.findViewById(R.id.group_item_icon);
			v.group_title =(TextView)convertView.findViewById(R.id.group_item_title);
			convertView.setTag(v);
		}else {
			v =(viewHolder)convertView.getTag();
		}
		v.group_title .setText(lists.get(position).getGroupName());
		String uri =lists.get(position).getGroupIcon().getFileUrl(context);
		if(uri!=null){
			//…Ë÷√Õº∆¨
			ImageLoader.getInstance().displayImage(uri, v.group_icon, MyApplication.getInstance().getOptions(R.drawable.ic_launcher));
		}
		return convertView;
	}
	
	class viewHolder{
		ImageView group_icon;
		TextView group_title;
	}

}
