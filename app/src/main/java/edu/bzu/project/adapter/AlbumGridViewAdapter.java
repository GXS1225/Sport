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
 * ��ʾͼƬ��ʹ������
 * ѡ����δѡ�� �ֱ��װ��������
 *
 */
public class AlbumGridViewAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<ImageItem> dataList;//ͼƬ�ļ���
	private ArrayList<ImageItem> selectedDataList;//ѡ�е�ͼƬ����
	private DisplayMetrics dm;
	/**
	 * ͼƬ ���� ������
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
		//1�ؼ��ĳ�ʼ��
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
		//2������ͼƬ
		String path;
		if (dataList != null && dataList.size() > position)
			path = dataList.get(position).imagePath;
		else
			path = "camera_default";
		if (path.contains("camera_default")) {
			holder.imageView.setImageResource(R.drawable.plugin_camera_no_pictures);//Ĭ��
		} else {
			final ImageItem item = dataList.get(position);
			holder.imageView.setTag(item.imagePath);//һ���ؼ����ö�Ӧ�� ͼƬ·���ı�־����ֹͼƬ����
			/**����ͼƬ*/
			loader.loadImage(item.imagePath, holder.imageView);
		}
		//3,���� ѡ���е�ͼƬ����ʽ
		holder.toggleButton.setTag(position);
		holder.choosetoggle.setTag(position);
		//���ڽ�Ҫ���ʱ���Զ���������ӿ�
		holder.toggleButton.setOnClickListener(new ToggleClickListener(holder.choosetoggle));
		//ѡ�е�ͼƬ���� ��ʽ
		if (selectedDataList.contains(dataList.get(position))) {
			holder.toggleButton.setChecked(true);
			holder.choosetoggle.setVisibility(View.VISIBLE);
		} else {
			holder.toggleButton.setChecked(false);
			holder.choosetoggle.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	//���
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
	 * ����б���ؼ����
	 */
	private class ViewHolder {
		public ImageView imageView;
		public ToggleButton toggleButton;//ѡ��ʱ�ı���
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
