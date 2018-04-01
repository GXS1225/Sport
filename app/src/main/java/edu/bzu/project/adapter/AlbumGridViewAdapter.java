package edu.bzu.project.adapter;

import java.util.ArrayList;



import edu.bzu.project.R;
import edu.bzu.project.domain.ImageItem;
import edu.bzu.project.utils.ImageLoader;



import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
/**
 * 显示图片的使适配器
 * 选中与未选中 分别封装到集合中
 *
 */
public class AlbumGridViewAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<ImageItem> dataList;//图片的集合
	private ArrayList<ImageItem> selectedDataList;//选中的图片集合
	private DisplayMetrics dm;
	/**
	 * 图片 加载 工具类
	 */
	private ImageLoader loader;

	public AlbumGridViewAdapter(Context context,ArrayList<ImageItem> dataList,ArrayList<ImageItem> selectedDataList) {
		super();
		// TODO Auto-generated constructor stub
		mContext =context;
		this.dataList =dataList;
		this.selectedDataList =selectedDataList;
		loader =ImageLoader.getInstance();
		dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//1控件的初始化
		ViewHolder holder =null;
		if (convertView==null) {
			convertView =LayoutInflater.from(mContext).inflate(R.layout.item_album_adapter, parent, false);
			holder =new ViewHolder();
			holder.imageView =(ImageView)convertView.findViewById(R.id.image_view);
			holder.toggleButton = (ToggleButton) convertView
					.findViewById(R.id.toggle_button);
			holder.choosetoggle = (Button) convertView
					.findViewById(R.id.choosedbt);
			convertView.setTag(holder);
		}else {
			holder =(ViewHolder)convertView.getTag();
		}
		//2，加载图片
		String path;
		if (dataList != null && dataList.size() > position)
			path = dataList.get(position).imagePath;
		else
			path = "camera_default";
		if (path.contains("camera_default")) {
			holder.imageView.setImageResource(R.drawable.plugin_camera_no_pictures);//默认
		} else {
			final ImageItem item = dataList.get(position);
			holder.imageView.setTag(item.imagePath);//一个控件设置对应的 图片路径的标志，防止图片错乱
			/**加载图片*/
			loader.loadImage(item.imagePath, holder.imageView);
		}
		//3,设置 选择中的图片的样式
		holder.toggleButton.setTag(position);
		holder.choosetoggle.setTag(position);
		//对于将要点击时的自定义监听器接口
		holder.toggleButton.setOnClickListener(new ToggleClickListener(holder.choosetoggle));
		//选中的图片触发 样式
		if (selectedDataList.contains(dataList.get(position))) {
			holder.toggleButton.setChecked(true);
			holder.choosetoggle.setVisibility(View.VISIBLE);
		} else {
			holder.toggleButton.setChecked(false);
			holder.choosetoggle.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	//点击
	private class ToggleClickListener implements OnClickListener{
		Button chooseBt;
		public ToggleClickListener(Button choosebt){
			this.chooseBt = choosebt;
		}
		
		@Override
		public void onClick(View view) {
			if (view instanceof ToggleButton) {
				ToggleButton toggleButton = (ToggleButton) view;
				int position = (Integer) toggleButton.getTag();
				if (dataList != null && mOnItemClickListener != null
						&& position < dataList.size()) {
					mOnItemClickListener.onItemClick(toggleButton, position, toggleButton.isChecked(),chooseBt);
				}
			}
		}
	}
	/**
	 * 存放列表项控件句柄
	 */
	private class ViewHolder {
		public ImageView imageView;
		public ToggleButton toggleButton;//选中时的背景
		public Button choosetoggle;
		public TextView textView;
	}
	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(ToggleButton view, int position,
				boolean isChecked,Button chooseBt);
	}
}
