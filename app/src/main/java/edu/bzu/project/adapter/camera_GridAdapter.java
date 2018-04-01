package edu.bzu.project.adapter;

import edu.bzu.project.R;
import edu.bzu.project.utils.ConstantValues;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

public class camera_GridAdapter extends BaseAdapter {

	private Context context =null;
	private int selectPosition =-1;//位置
	private boolean shape;
	public camera_GridAdapter(Context context) {
		super();
		// TODO Auto-generated constructor stub
		this.context =context;
	}

	public int getSelectPosition() {
		return selectPosition;
	}

	public void setSelectPosition(int selectPosition) {
		this.selectPosition = selectPosition;
	}

	public boolean isShape() {
		return shape;
	}

	public void setShape(boolean shape) {
		this.shape = shape;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(ConstantValues.tempSelectBitmap.size()>3){
			return 3;
		}else {
			return ConstantValues.tempSelectBitmap.size()+1;
		}
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return ConstantValues.tempSelectBitmap.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context,R.layout.item_published_grida,
					null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.item_grida_image);
			holder.canle =(ImageButton)convertView.findViewById(R.id.canle);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//根据位置和图片的数量 来显示添加的图片
		if (position ==ConstantValues.tempSelectBitmap.size()) {
			holder.imageView.setImageBitmap(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.icon_addpic_unfocused2));
			holder.canle.setVisibility(View.GONE);
			if (position == 3) {
				holder.imageView.setVisibility(View.GONE);
			}
		} else {
			//先从相册图片中取 ，没有的话从拍照的sd卡取
			holder.imageView.setImageBitmap(ConstantValues.tempSelectBitmap.get(position).getBitmap());
			holder.canle.setVisibility(View.VISIBLE);
			holder.canle.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ConstantValues.tempSelectBitmap.remove(position);
					camera_GridAdapter.this.notifyDataSetChanged();
				}
			});
		}
		
		return convertView;
	}
	class ViewHolder{
		ImageView imageView =null;
		private ImageButton canle;
	}
}
