package edu.bzu.project.adapter;

import java.util.List;

import edu.bzu.project.R;
import edu.bzu.project.domain.bmob.Commit;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Topic_Comment extends BaseAdapter {

	private List<Commit> items =null;
	private Context context =null;
	
	
	public Topic_Comment(List<Commit> items, Context context) {
		super();
		this.items = items;
		this.context = context;
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
		viewHolder v =null;
		if(convertView == null){
			convertView =View.inflate(context, R.layout.item_commit, null);
			v =new viewHolder();
			v.commit_uer =(TextView)convertView.findViewById(R.id.commit_user);
			v.lou =(TextView)convertView.findViewById(R.id.commit_lou);
			convertView.setTag(v);
		}else {
			v =(viewHolder)convertView.getTag();
		}
		v.lou.setText(position+1+"¥");
		v.commit_uer.setText(items.get(position).getCommentContent());
		return convertView;
	}
	class viewHolder{
		TextView lou;
		TextView commit_uer;
	}
}
