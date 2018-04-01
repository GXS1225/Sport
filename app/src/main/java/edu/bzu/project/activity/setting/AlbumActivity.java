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
	private static String TAG ="AlbumActivityͼƬչʾ";
	private GridView gridView =null;
	private AlbumGridViewAdapter adapter =null;
	private Button back =null;
	private Button commit =null;
	private TextView tv_no =null;//û��ͼƬʱ����ʾ
	
	private Context context =null;
	private AlbumUtils albumUtils =null;
	private ArrayList<ImageItem> items =null;//�����ļ����µ�ͼƬʵ���༯��(����ԭͼ������ͼ)
	private List<ImageBucket> bucketlists =null;//����ͼƬ�� �ļ��м���
	
	private static Bitmap bitmap =null;//û��ͼƬʱ����ʾ��Ĭ�ϵ�ͼƬ
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
	   //ʵ���Զ���Ľӿ�
		adapter.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(final ToggleButton view, int position, boolean isChecked,
					Button chooseBt) {
				// TODO Auto-generated method stub
				/*���ͼƬ���б�ʱ �ص��ķ���
				 * �ּ������
				 * 1�����ﵽͼƬ��������ʱ:�Ȳ����á����ѡ�����ͼƬ�����Ƴ���ͼƬ�����û��ѡ������ⲻ�ܴ���ѡ��״̬��
				 * 2����û�дﵽ��������ʱ�����ѡ�����ͼƬ�����Ƴ�ͼƬ�����û��ѡ���������ӵ�ѡ���ͼƬ���ϡ�
				 * ���ð�ť��Ч��
				 */
				if(ConstantValues.tempSelectBitmap.size()>=3){
					view.setChecked(false);
					chooseBt.setVisibility(View.GONE);
					if(ConstantValues.tempSelectBitmap.contains(items.get(position))){
						ConstantValues.tempSelectBitmap.remove(items.get(position));
					}
					commit.setText("ѡ��"+ConstantValues.tempSelectBitmap.size()+"/"+3+"��");
					return;
				}
				if(isChecked){
					//��ѡ�����ͼƬ����ʾ��ʽ
					chooseBt.setVisibility(View.VISIBLE);
					ConstantValues.tempSelectBitmap.add(items.get(position));
				}else {
					//��ǰѡ���
					chooseBt.setVisibility(View.GONE);
					ConstantValues.tempSelectBitmap.remove(items.get(position));
				}
				commit.setText("ѡ��"+ConstantValues.tempSelectBitmap.size()+"/"+3+"��");
				//�ı�һ�½��水ť��Ч��
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
		//��ȡ���е�ͼƬ�������ļ��д�ŵ�������
		bucketlists =albumUtils.getImagesBucketList(false);
		//��װͼƬ�ļ��� ͼƬ����
		items = new ArrayList<ImageItem>();
		for(int i=0;i<bucketlists.size();i++){//��ȡ�����ļ��е�ͼƬ
			items.addAll(bucketlists.get(i).imageList);//
		}
		//ȫ����ͼƬ���ϣ�չʾ
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
		
		commit.setText("ѡ��"+ConstantValues.tempSelectBitmap.size()+"/"+3+"��");
		
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
