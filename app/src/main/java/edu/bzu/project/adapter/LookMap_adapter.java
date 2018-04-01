package edu.bzu.project.adapter;

import java.util.List;

import edu.bzu.project.R;
import edu.bzu.project.domain.bmob.Map_domain;
import edu.bzu.project.utils.DateStyle;
import edu.bzu.project.utils.DateUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LookMap_adapter extends BaseAdapter {

	private List<Map_domain> maps =null;
	private Context context =null;
	
	public LookMap_adapter(List<Map_domain> maps, Context context) {
		super();
		this.maps = maps;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return maps.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return maps.get(position);
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
			convertView =View.inflate(context, R.layout.item_showmap, null);
			v =new viewHolder();
			v.time =(TextView)convertView.findViewById(R.id.show_time);
			v.time1 =(TextView)convertView.findViewById(R.id.show_time1);
			v.lookmap_juli =(TextView)convertView.findViewById(R.id.lookmap_juli);
			v.lookmap_mode =(TextView)convertView.findViewById(R.id.lookmap_mode);
			v.lookmap_time =(TextView)convertView.findViewById(R.id.lookmap_time);
			v.lookmap_qianka =(TextView)convertView.findViewById(R.id.lookmap_qianka);
			v.lookmap_pingjia =(TextView)convertView.findViewById(R.id.lookmap_pingjia);
			
			convertView.setTag(v);
		}else{
			v =(viewHolder)convertView.getTag();
		}
		String time =maps.get(position).getTime();
		v.time.setText(DateUtils.StringToString(time, DateStyle.YYYY_MM_DD));
		v.time1.setText(DateUtils.StringToString(time, DateStyle.HH_MM));
		v.lookmap_juli.setText(String.valueOf(maps.get(position).getJuli()));
		v.lookmap_mode.setText(maps.get(position).getMode());
		v.lookmap_pingjia.setText("се");
		v.lookmap_qianka.setText(String.valueOf(maps.get(position).getQianka()));
		v.lookmap_time.setText(String.valueOf(maps.get(position).getSummiao()));
		
		return convertView;
	}
	class viewHolder{
		TextView time;
		TextView time1;
		TextView lookmap_juli;
		TextView lookmap_mode;
		TextView lookmap_time;
		TextView lookmap_qianka;
		TextView lookmap_pingjia;
	}
}
