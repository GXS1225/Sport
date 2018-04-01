package edu.bzu.project.adapter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

import com.nostra13.universalimageloader.core.ImageLoader;

import edu.bzu.project.R;
import edu.bzu.project.adapter.AttentionAdapter.viewHolder;
import edu.bzu.project.domain.pk_class;
import edu.bzu.project.domain.bmob.Attention;
import edu.bzu.project.domain.bmob.Map_domain;
import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.utils.DateUtils;
import edu.bzu.project.utils.LogUtils;
import edu.bzu.project.utils.MyApplication;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PK_guanzhu_adapter extends BaseAdapter {

	private Context context =null;
	
	private List<pk_class> is_pks =null;
	private double sum =0;//我
	
	
	public PK_guanzhu_adapter(Context context, List<pk_class> is_pks) {
		super();
		this.context = context;
		
		this.is_pks =is_pks;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return is_pks.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return is_pks.get(position);
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
			v.juli.setVisibility(View.GONE);
			convertView.setTag(v);
		}else{
			v =(viewHolder)convertView.getTag();
		}
		BmobFile photo =is_pks.get(position).getAttention().getUser_().getUserPhoto();
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
		
		v.name.setText(is_pks.get(position).getAttention().getUser_().getUserName());
		
		//查找所pk的人  3天内 不能重复的pk
		if(is_pks.size()>0){
			if(is_pks.get(position).getIs_pk()){
				v.pk.setText("pk吧    ");
				v.pk.setClickable(false);
				v.pk.setFocusable(false);
			}else{
				v.pk.setText("不能pk   ");
				v.pk.setEnabled(false);
			}
		}
		v.pk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//pk的代码
				//pk动画 ，，
				
				//pk的的比较,随机分层比较  随机分模式，分量比较
				if(listener!=null){
					listener.pkListener(BmobUser.getCurrentUser(context, User.class), 
							is_pks.get(position).getAttention().getUser_(),position);
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
	
	public interface PkOnListener{
		public void pkListener(User user,User user_,int position);
	}
	
	public PkOnListener listener;


	public PkOnListener getListener() {
		return listener;
	}

	public void setListener(PkOnListener listener) {
		this.listener = listener;
	}
}
