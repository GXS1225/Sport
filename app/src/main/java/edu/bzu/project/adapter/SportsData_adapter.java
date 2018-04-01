package edu.bzu.project.adapter;

import java.util.List;

import edu.bzu.project.R;
import edu.bzu.project.activity.setting.AddExercise_Acivity;
import edu.bzu.project.activity.setting.AddSport_Activity;
import edu.bzu.project.domain.Sport_w;

import edu.bzu.project.other.OnJiLuClilckListener;
import edu.bzu.project.utils.MyApplication;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class SportsData_adapter extends BaseAdapter{

	private Context context =null;
	private List<Sport_w> list =null;
	private static int position;
	
	
	public SportsData_adapter(Context context,List<Sport_w> list) {
		super();
		this.context =context;
		this.list =list;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		final int p =position;
		ViewEntity entity =null;
		if(convertView==null){
			convertView =View.inflate(context, R.layout.item_sport, null);
			entity =new ViewEntity();
			entity.sql_name =(TextView)convertView.findViewById(R.id.sport_name);
			entity.sql_value =(TextView)convertView.findViewById(R.id.sport_sql_value);
			entity.sport_value =(TextView)convertView.findViewById(R.id.sport_value);
			entity.sport_time =(TextView)convertView.findViewById(R.id.sport_time);
			entity.sport_add =(ImageButton)convertView.findViewById(R.id.sportitem_add);
			entity.layout =(LinearLayout)convertView.findViewById(R.id.sportitem_addvalue);
			convertView.setTag(entity);
		}else {
			entity =(ViewEntity)convertView.getTag();
		}
		//本地数据的加载显示
		entity.sql_name.setText(list.get(position).getMode());
		entity.sql_value.setText(list.get(position).getHeat()+"");
		//有无添加的运动(已经在对话框中选择，并且在fragment中已经显示)
		if(list.get(position).getItem()==null){//没有添加运动记录数据
			entity.sport_add.setVisibility(View.VISIBLE);
			entity.layout.setVisibility(View.GONE);
		}else {
			entity.sport_add.setVisibility(View.GONE);
			entity.layout.setVisibility(View.VISIBLE);
			//监听器 ，用于取消
			entity.layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(onJiLuClilckListener!=null){
						onJiLuClilckListener.onJiLuClick(p);
					}
				}
			});
		
			entity.sport_value.setText(String.valueOf(list.get(position).getItem().getQianka()));//总千卡
			entity.sport_time.setText(list.get(position).getItem().getHour()+" : "+
									list.get(position).getItem().getMinute());
		}
		//设置监听，用于增加
		entity.sport_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(onJiLuClilckListener!=null){
					onJiLuClilckListener.onJiLuClick(p);
				}
			}
		});
		return convertView;
	}
	public OnJiLuClilckListener onJiLuClilckListener =null;
	
	public OnJiLuClilckListener getOnJiLuClilckListener() {
		return onJiLuClilckListener;
	}

	public void setOnJiLuClilckListener(OnJiLuClilckListener onJiLuClilckListener) {
		this.onJiLuClilckListener = onJiLuClilckListener;
	}

	class ViewEntity{
		private TextView sql_name;
		private TextView sql_value;
		private TextView sport_value;
		private TextView sport_time;
		private ImageButton sport_add;
		private LinearLayout layout;
		
	}

	
}

