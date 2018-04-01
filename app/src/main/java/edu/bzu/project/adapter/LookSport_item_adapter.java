package edu.bzu.project.adapter;

import java.util.List;

import edu.bzu.project.R;
import edu.bzu.project.domain.Sport_add;
import edu.bzu.project.domain.Sport_item;
import edu.bzu.project.utils.DateStyle;
import edu.bzu.project.utils.DateUtils;
import edu.bzu.project.utils.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class LookSport_item_adapter extends BaseAdapter {

	private List<Sport_add> items =null;
	private Context context =null;
	
	public LookSport_item_adapter(Context context,List<Sport_add> items) {
		super();
		// TODO Auto-generated constructor stub
		this.context =context;
		this.items =items;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolde v =null;
		if(convertView == null){
			convertView =View.inflate(context, R.layout.item_looksport, null);
			v =new ViewHolde();
			v.time =(TextView)convertView.findViewById(R.id.time);
			v.photo_lay =(LinearLayout)convertView.findViewById(R.id.photo_lay);
			v.photo1 =(ImageView)convertView.findViewById(R.id.photo1);
			v.photo2 =(ImageView)convertView.findViewById(R.id.photo2);
			v.photo3 =(ImageView)convertView.findViewById(R.id.photo3);
			v.weigth_lay =(LinearLayout)convertView.findViewById(R.id.weigth_lay);
			v.weigth_value =(TextView)convertView.findViewById(R.id.weigth_value);
			v.sport_lay =(LinearLayout)convertView.findViewById(R.id.sport_lay);
			v.item_value =(TextView)convertView.findViewById(R.id.item_value);
			v.qianka_value =(TextView)convertView.findViewById(R.id.qianka_value);
			v.ganshou_lay =(LinearLayout)convertView.findViewById(R.id.ganshou_lay);
			v.ganshou =(TextView)convertView.findViewById(R.id.ganshou);
			
			convertView.setTag(v);
		}else {
			v =(ViewHolde)convertView.getTag();
		}
		//�ж����ݵ����� ����̬��ʾ����
		//1 ͼƬ
		if(!TextUtils.isEmpty(items.get(position).getPhoto1())){
			v.photo_lay.setVisibility(View.VISIBLE);
			v.photo1.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().loadImage(items.get(position).getPhoto1(), v.photo1);//����
			if(!TextUtils.isEmpty(items.get(position).getPhoto2())){
				v.photo2.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().loadImage(items.get(position).getPhoto2(), v.photo2);//����
				if(!TextUtils.isEmpty(items.get(position).getPhoto3())){
					v.photo3.setVisibility(View.VISIBLE);
					ImageLoader.getInstance().loadImage(items.get(position).getPhoto3(), v.photo3);//����
				}
			}
		}else {
			v.photo_lay.setVisibility(View.GONE);
		}
		//2 ����
		if(items.get(position).getWeight()>0){
			v.weigth_lay.setVisibility(View.VISIBLE);
			v.weigth_value.setText(items.get(position).getWeight()+"");
		}else{
			v.weigth_lay.setVisibility(View.GONE);
		}
		//3 �˶�
		if(items.get(position).getItems()!=null && items.get(position).getItems().size()>0){
			v.sport_lay.setVisibility(View.VISIBLE);
			v.item_value.setText(items.get(position).getItems().size()+"");
			double sum =0.0;
			for(Sport_item i:items.get(position).getItems()){
				sum+= i.getQianka();
			}
			v.qianka_value.setText(sum+"");
		}else {
			v.sport_lay.setVisibility(View.GONE);
		}
		//4 ����
		v.time.setText(DateUtils.StringToString(items.get(position).getRiqi(), DateStyle.HH_MM));
		//5����
		if(!TextUtils.isEmpty(items.get(position).getText())){
			v.ganshou_lay.setVisibility(View.VISIBLE);
			v.ganshou.setText(items.get(position).getText());
		}else{
			v.ganshou_lay.setVisibility(View.GONE);
		}
		return convertView;
	}
	class ViewHolde{
		TextView time;
		//��Ƭ
		LinearLayout photo_lay;
		ImageView photo1;
		ImageView photo2;
		ImageView photo3;
		//����
		LinearLayout weigth_lay;
		TextView weigth_value;
		//�˶�
		LinearLayout sport_lay;
		TextView item_value;
		TextView qianka_value;
		//����
		LinearLayout ganshou_lay;
		TextView ganshou;
	}
}
