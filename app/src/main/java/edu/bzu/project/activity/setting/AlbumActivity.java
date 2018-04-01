package edu.bzu.project.activity.setting;

import java.util.ArrayList;
import java.util.List;

import edu.bzu.project.R;
import edu.bzu.project.adapter.AlbumGridViewAdapter;
import edu.bzu.project.adapter.AlbumGridViewAdapter.OnItemClickListener;
import edu.bzu.project.domain.ImageBucket;
import edu.bzu.project.domain.ImageItem;
import edu.bzu.project.utils.MyApplication;
import edu.bzu.project.utils.AlbumUtils;
import edu.bzu.project.utils.ConstantValues;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AlbumActivity extends Activity {
	private static String TAG ="AlbumActivity图片展示";
	private GridView gridView =null;
	private AlbumGridViewAdapter adapter =null;
	private Button back =null;
	private Button commit =null;
	private TextView tv_no =null;//没有图片时的显示
	
	private Context context =null;
	private AlbumUtils albumUtils =null;
	private ArrayList<ImageItem> items =null;//单个文件夹下的图片实体类集合(包含原图和缩略图)
	private List<ImageBucket> bucketlists =null;//所有图片的 文件夹集合
	
	private static Bitmap bitmap =null;//没有图片时的显示，默认的图片
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_camera_album);
		context =this;
		bitmap =BitmapFactory.decodeResource(getResources(), R.drawable.plugin_camera_no_pictures);
		initData();
		initView();
		initEvent();
	}
   private void initEvent() {
		// TODO Auto-generated method stub
	   //实现自定义的接口
		adapter.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(final ToggleButton view, int position, boolean isChecked,
					Button chooseBt) {
				// TODO Auto-generated method stub
				/*点击图片的列表时 回调的方法
				 * 分几种情况
				 * 1，当达到图片顶峰数量时:先不可用。如果选择过此图片，则移除此图片。如果没有选择过，这不能触发选择状态。
				 * 2，当没有达到顶峰数量时：如果选择过此图片，则移除图片，如果没有选择过，则添加到选择的图片集合。
				 * 设置按钮的效果
				 */
				if(ConstantValues.tempSelectBitmap.size()>=3){
					view.setChecked(false);
					chooseBt.setVisibility(View.GONE);
					if(ConstantValues.tempSelectBitmap.contains(items.get(position))){
						ConstantValues.tempSelectBitmap.remove(items.get(position));
					}
					commit.setText("选择"+ConstantValues.tempSelectBitmap.size()+"/"+3+"项");
					return;
				}
				if(isChecked){
					//将选择过的图片，显示样式
					chooseBt.setVisibility(View.VISIBLE);
					ConstantValues.tempSelectBitmap.add(items.get(position));
				}else {
					//以前选择过
					chooseBt.setVisibility(View.GONE);
					ConstantValues.tempSelectBitmap.remove(items.get(position));
				}
				commit.setText("选择"+ConstantValues.tempSelectBitmap.size()+"/"+3+"项");
				//改变一下界面按钮的效果
				if(ConstantValues.tempSelectBitmap.size()>0){
					commit.setEnabled(true);
				}else {
					commit.setEnabled(false);
				}
			}
		});
		commit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
				AlbumActivity.this.setResult(AddSport_Activity.ALUM_CODE);
				MyApplication.getInstance().removeActivity(AlbumActivity.this);
				finish();
			}
		});
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ConstantValues.tempSelectBitmap.clear();
				AlbumActivity.this.setResult(AddSport_Activity.ALUM_CODE);
				MyApplication.getInstance().removeActivity(AlbumActivity.this);
				finish();
			}
		});
	}
	private void initData() {
		// TODO Auto-generated method stub
		albumUtils =AlbumUtils.getHelper();
		albumUtils.init(AlbumActivity.this);
		//读取所有的图片，按照文件夹存放到集合中
		bucketlists =albumUtils.getImagesBucketList(false);
		//封装图片文件的 图片集合
		items = new ArrayList<ImageItem>();
		for(int i=0;i<bucketlists.size();i++){//获取所有文件夹的图片
			items.addAll(bucketlists.get(i).imageList);//
		}
		//全部的图片集合，展示
		adapter =new AlbumGridViewAdapter(context, items, ConstantValues.tempSelectBitmap);
		
	}
	private void initView() {
		// TODO Auto-generated method stub
		back =(Button)findViewById(R.id.back);
		commit =(Button)findViewById(R.id.ok_button);
		gridView =(GridView)findViewById(R.id.myGrid);
		tv_no =(TextView)findViewById(R.id.tv_no);
		
		gridView.setAdapter(adapter);
		gridView.setEmptyView(tv_no);
		
		commit.setText("选择"+ConstantValues.tempSelectBitmap.size()+"/"+3+"项");
		
	}
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ConstantValues.tempSelectBitmap.clear();
			MyApplication.getInstance().removeActivity(this);
			finish();
		}
		return true;
	}
}
