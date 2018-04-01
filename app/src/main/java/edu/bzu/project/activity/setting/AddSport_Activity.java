package edu.bzu.project.activity.setting;

import java.io.Serializable;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;




import edu.bzu.project.R;
import edu.bzu.project.R.layout;
import edu.bzu.project.R.menu;
import edu.bzu.project.adapter.AddSport_fragmentAdapter;
import edu.bzu.project.adapter.camera_GridAdapter;


import edu.bzu.project.domain.ImageItem;
import edu.bzu.project.domain.Sport_add;

import edu.bzu.project.domain.Sport_item;
import edu.bzu.project.domain.Sport_often;
import edu.bzu.project.fragment.Custom_fragment;
import edu.bzu.project.fragment.Hot_fragment;
import edu.bzu.project.fragment.Often_fragment;

import edu.bzu.project.utils.MyApplication;

import edu.bzu.project.utils.CommonUtils;
import edu.bzu.project.utils.ConstantValues;
import edu.bzu.project.utils.DateStyle;
import edu.bzu.project.utils.DateUtils;
import edu.bzu.project.utils.FileUtils;
import edu.bzu.project.utils.ImageUtils;
import edu.bzu.project.utils.LogUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Telephony.Sms.Conversations;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
/**
 * ���һ���Ľ���
 * @author houenshuo
 *
 */
public class AddSport_Activity extends FragmentActivity {

	private static String TAG ="AddSport_Activity";
	public static final int CODE =0;
	public static final int CODE_NO =100;

	public static final int ALUM_CODE =2;

	private ArrayList<Sport_item> lists =new ArrayList<Sport_item>();
	private double qianka;
	//���ֿؼ�
	//���ؼ�
	private Button back =null;
	private TextView commit =null;
	//�б� �˶� ʳ�� ����`
	private LinearLayout sport_lay =null;
	private TextView weight_value =null;
	private double weight ;
	private LinearLayout weight_lay =null;
	private TextView sport_value =null;
	private double sport ;
	private LinearLayout food_lay =null;
	private TextView food_value =null;
	private TextView feel_sport =null;

	//����������ʱ��ѡ������ʾ
	private TextView riqi_ =null;
	private TextView shijian_ =null;
	private TextView riqi =null;
	private TextView week =null;
	private TextView day =null;

	//�Ի���ѡ����
	private AlertDialog dialog =null;
	private Button ok =null;
	private Button no =null;
	//����ѡ��
	private NumberPicker n1 =null;
	private NumberPicker n2 =null;
	//ʱ��ѡ��
	private TimePicker t =null;
	private DatePicker d =null;

	private Button bc =null;
	private Button bbc =null;
	private Button qx =null;
	/**���*/
	private camera_GridAdapter gridAdapter =null;
	private GridView gridView =null;
	private PopupWindow popupWindow =null;

	private LinearLayout camear_linearLayout =null;
	public static Bitmap bitmap =null;

	private Thread thread;//�߳�
	private static final int TAKE_PICTURE = 0x000011;//������յķ�����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_add_sport);
		LogUtils.i(TAG, "oncreate()");
		initView();
		initEvent();
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		back.setOnClickListener(oListener);
		//�������ֵļ�����
		sport_lay.setOnClickListener(oListener);
		weight_lay.setOnClickListener(oListener);
		food_lay.setOnClickListener(oListener);
		//���ڵļ�����
		shijian_.setOnClickListener(oListener);
		riqi_.setOnClickListener(oListener);
		commit.setOnClickListener(oListener);
	}

	private void initView() {
		// TODO Auto-generated method stub
		back =(Button)findViewById(R.id.addsport_back);
		commit =(TextView) findViewById(R.id.addsport_add);
		weight_lay =(LinearLayout)findViewById(R.id.addsport_item1);
		weight_value =(TextView)findViewById(R.id.weight_value);
		sport_lay =(LinearLayout)findViewById(R.id.addsport_item2);
		sport_value =(TextView)findViewById(R.id.sport_value);
		food_lay =(LinearLayout)findViewById(R.id.addsport_item3);
		food_value =(TextView)findViewById(R.id.food_value);

		riqi_ =(TextView)findViewById(R.id.addsport_mark);//ͼƬ���е�
		riqi_.setText(DateUtils.getDay(Calendar.getInstance().getTime())+"");
		shijian_ =(TextView)findViewById(R.id.create_time);//����¼�
		//��ʼ��������ͼʱ��
		shijian_.setText(DateUtils.getHour(Calendar.getInstance().getTime())
				+":"+DateUtils.getMinute(Calendar.getInstance().getTime()));
		riqi =(TextView)findViewById(R.id.date_text);
		riqi.setText(DateUtils.DateToString(Calendar.getInstance().getTime(), DateStyle.YYYY_MM_CN));
		week =(TextView)findViewById(R.id.week_day);
		week.setText(DateUtils.getWeek(Calendar.getInstance().getTime()).getChineseName()+"");
		day =(TextView)findViewById(R.id.day_tip);
		feel_sport =(EditText)findViewById(R.id.feel_sport);
		InitCamear();
	}
	/**��ʼ��ͼƬ�ϴ�*/
	private void InitCamear() {
		// TODO Auto-generated method stub
		gridView =(GridView)findViewById(R.id.camear_grid);

		View popview =View.inflate(AddSport_Activity.this, R.layout.dialog_popwindow, null);

		camear_linearLayout =(LinearLayout)popview.findViewById(R.id.camear_popup);
		popupWindow =CommonUtils.getPopWindow(AddSport_Activity.this, popview);

		RelativeLayout parentview =(RelativeLayout)popview.findViewById(R.id.parent);
		Button btn1 =(Button)popview.findViewById(R.id.item_popupwindows_camera);//���
		Button btn2 =(Button)popview.findViewById(R.id.item_popupwindows_Photo);//���
		Button btn3 =(Button)popview.findViewById(R.id.item_popupwindows_cancel);//ȡ��

		parentview.setOnClickListener(new OnClickListener() {//����ⲿ��ͼƬʱȡ������

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
				camear_linearLayout.clearAnimation();//�������
			}
		});
		//�������
		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent openCamear =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//openCamear.putExtra(name, value)
				startActivityForResult(openCamear, TAKE_PICTURE);
				popupWindow.dismiss();
				camear_linearLayout.clearAnimation();
			}
		});
		//���ѡ��
		btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent =new Intent(AddSport_Activity.this,AlbumActivity.class);
				startActivityForResult(intent, ALUM_CODE);
				overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
				popupWindow.dismiss();
				camear_linearLayout.clearAnimation();
			}
		});
		btn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
			}
		});
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridAdapter =new camera_GridAdapter(AddSport_Activity.this);
		gridView.setAdapter(gridAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				//������һ��ͼƬʱ�������Ի���
				if(position == ConstantValues.tempSelectBitmap.size()){
					camear_linearLayout.setAnimation(AnimationUtils.loadAnimation(AddSport_Activity.this, R.anim.activity_translate_in));
					popupWindow.showAtLocation(View.inflate(AddSport_Activity.this, R.layout.activity_add_sport, null), Gravity.BOTTOM, 0, 0);
				}
			}
		});

	}
	//ˢ���뱣������
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					gridAdapter.notifyDataSetChanged();
					break;
				case 0:
					Toast.makeText(AddSport_Activity.this, "���ݱ������", 1000).show();
					ConstantValues.tempSelectBitmap.clear();
					MyApplication.getInstance().removeActivity(AddSport_Activity.this);
					finish();
					break;
			}
		}
	};
	//�����̲߳���ˢ�£���鵽�и��¾�ˢ��
	public void loading() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					if (ConstantValues.max == ConstantValues.tempSelectBitmap.size()) {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
						break;
					} else {
						ConstantValues.max += 1;
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
				}
			}
		}).start();
	}

	//�����¼��ļ�����
	OnClickListener oListener =new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			LinearLayout layout =null;
			switch (v.getId()) {
				//���ذ�ť
				case R.id.addsport_back:
					//��������ʱ��������ʾ��
					//���ز���
					if(lists.size()>0||weight>0||ConstantValues.tempSelectBitmap.size()>0){
						View v1 =View.inflate(AddSport_Activity.this, R.layout.dialog_back, null);
						bbc =(Button)v1.findViewById(R.id.dialog_bbc);
						bc =(Button)v1.findViewById(R.id.dialog_bc);
						qx =(Button)v1.findViewById(R.id.dialog_qx);
						bbc.setOnClickListener(clickListener);
						bc.setOnClickListener(clickListener);
						qx.setOnClickListener(clickListener);
						//�Ի���
						dialog =new AlertDialog.Builder(AddSport_Activity.this).setView(v1).show();
					}else{
						MyApplication.getInstance().removeActivity(AddSport_Activity.this);
						finish();
					}
					break;
				//�ύ��ť
				case R.id.addsport_add:
					//�����ж������ڣ�
					saveDate();
					break;


//					//����Ϊ�����˶�
//					List<Sport_often> oftens =new ArrayList<Sport_often>();
//					for(Sport_item item:lists){
//						//�ж����ݿ��Ƿ��У���������order
//						Sport_often o =(Sport_often) DataSupport.where("mode = ?",item.getName()).find(Sport_often.class);
//						if(o!=null){
//							//����
//							ContentValues values = new ContentValues();
//							values.put("orderid", o.getOrderid()+1);
//							DataSupport.update(Sport_often.class, values, o.getId());
//						}else {
//							Sport_often often =new Sport_often();
//							often.setMode(item.getName());
//							often.setHeat(item.getQianka_value());
//							oftens.add(often);
//						}
//
//					}
//					DataSupport.saveAll(oftens);
//				}else{
//					Toast.makeText(AddSport_Activity.this, "�ף���ѡ������", 2000).show();
//				}

				//����ѡ��
				case R.id.addsport_mark:

					layout =(LinearLayout)getLayoutInflater().inflate(R.layout.dialog_date, null);
					d =(DatePicker)layout.findViewById(R.id.datepicker);
					//��ȡ��ť
					ok =(Button)layout.findViewById(R.id.dialog_date_ok);
					no =(Button)layout.findViewById(R.id.dialog_date_no);
					//��������
					d.setMaxDate(Calendar.getInstance().getTimeInMillis());//��ǰʱ��

					dialog =new AlertDialog.Builder(AddSport_Activity.this).setView(layout).show();
					//�Ի���İ�ť������
					ok.setOnClickListener(clickListener);
					no.setOnClickListener(clickListener);
					break;
				//ʱ��ѡ��
				case R.id.create_time:

					layout =(LinearLayout)getLayoutInflater().inflate(R.layout.dialog_time, null);
					t =(TimePicker)layout.findViewById(R.id.timepicker);
					//��ȡ��ť
					ok =(Button)layout.findViewById(R.id.dialog_time_ok);
					no =(Button)layout.findViewById(R.id.dialog_time_no);
					//��������
					t.setIs24HourView(true);//����24Сʱ�� ȥ����������
					t.setCurrentHour(DateUtils.getHour(Calendar.getInstance().getTime()));
					t.setCurrentMinute(DateUtils.getMinute(Calendar.getInstance().getTime()));
					dialog =new AlertDialog.Builder(AddSport_Activity.this).setView(layout).show();
					//�Ի���İ�ť������
					ok.setOnClickListener(clickListener);
					no.setOnClickListener(clickListener);
					break;
				//����ѡ��
				case R.id.addsport_item1:
					layout =(LinearLayout)getLayoutInflater().inflate(R.layout.dialog_weight, null);
					TextView title =(TextView)layout.findViewById(R.id.dialog_title);
					title.setText("ѡ������");
					n1 =(NumberPicker)layout.findViewById(R.id.numberpicker1);
					n2 =(NumberPicker)layout.findViewById(R.id.numberpicker2);
					//��ȡ��ť
					ok =(Button)layout.findViewById(R.id.dialog_weight_ok);
					no =(Button)layout.findViewById(R.id.dialog_weight_no);

					//������ֵѡ������ߵĵĻ�������
					n1.setValue(50);
					n1.setMaxValue(300);
					n1.setMinValue(30);

					//�Զ����ұߵ���ֵ
					String value[] =new String[]{"0kg","1kg","2kg","3kg","4kg","5kg","6kg","7kg","8kg"
							,"9kg"};
					n2.setMinValue(0);
					n2.setMaxValue(value.length-1);
					n2.setDisplayedValues(value);

					//�����Ի���
					dialog =new AlertDialog.Builder(AddSport_Activity.this).setView(layout).show();
					//�Ի���İ�ť������
					ok.setOnClickListener(clickListener);
					no.setOnClickListener(clickListener);
					break;
				//�˶�
				case R.id.addsport_item2:
					if(lists.size()>0){
						Bundle bundle =new Bundle();
						bundle.putSerializable("sportsyet",  (Serializable)lists);
						LogUtils.w("lsit.size()-------", lists.size()+"");
						CommonUtils.launchActivityForResult(AddSport_Activity.this, AddExercise_Acivity.class,CODE,bundle);
						break;
					}
					CommonUtils.launchActivityForResult(AddSport_Activity.this, AddExercise_Acivity.class,CODE_NO,null);
					break;
				case R.id.addsport_item3:
					Toast.makeText(AddSport_Activity.this, "3", 3000).show();
					break;
			}
		}
	};

	private void saveDate(){
		//������ʱ �����ϴ�����
		final String feel_str =feel_sport.getText().toString();
		if(lists.size()>0||weight>0||ConstantValues.tempSelectBitmap.size()>0||!TextUtils.isEmpty(feel_str)){
			Toast.makeText(AddSport_Activity.this, "�����ύ����", 1000).show();
			//�����߳�����������
			thread =new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Connector.getDatabase();
					//��ȡ���ڵĸ�ʽ�ַ��� yyyy��mm��dd��
					String date_str =riqi.getText().toString().trim()+riqi_.getText()
							.toString().trim()+"��";
					/**�������ݿ����*/
					Sport_add add =new Sport_add();
					//��ȡ���ڸ�ʽ������yyyy��mm��dd��HH��ss
					String time =date_str+" "+shijian_.getText().toString().trim();
					add.setTime(Calendar.getInstance().getTime());
					//��������
					add.setRiqi(time);

					//��������
					if(weight>0){
						add.setWeight(weight);
					}
					//�������
					if(!TextUtils.isEmpty(feel_str)){
						add.setText(feel_str);
					}
					//����ͼƬ
					if(ConstantValues.tempSelectBitmap.size()>0){
						for(int i=0;i<ConstantValues.tempSelectBitmap.size();i++){
							switch (i) {
								case 0:
									add.setPhoto1(ConstantValues.tempSelectBitmap.get(i).imagePath);
									break;
								case 1:
									add.setPhoto2(ConstantValues.tempSelectBitmap.get(i).imagePath);
									break;
								case 2:
									add.setPhoto3(ConstantValues.tempSelectBitmap.get(i).imagePath);
									break;
							}
						}
					}
					//����log�鿴
					LogUtils.i("����",
							"/nʱ��"+add.getRiqi()+"/n����"+add.getText()+"/n����"+add.getWeight()+"/n��Ƭ1"+add.getPhoto1()+"/n��Ƭ2"+add.getPhoto2()+
									"/n��Ƭ3"+add.getPhoto3()
					);
					//add.setItems(lists);
					add.saveThrows();//�������ݿ�
					//�����˶�
					if(lists.size()>0){
						for(Sport_item item :lists){
							item.setSportadd(add);
						}
						DataSupport.saveAll(lists);
					}
					try {
						Thread.sleep(1000);

						Message msg =new Message();
						msg.what =0;
						handler.sendMessage(msg);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			thread.start();
		}
	}

	//�Ի���ļ�����
	OnClickListener clickListener =new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
				//����ѡ�� �Ի���
				case R.id.dialog_weight_ok:
					//��������
					String value[] =n2.getDisplayedValues();
					String you =value[n2.getValue()];//��ȡ�Զ������ֵ

					weight =Double.parseDouble(n1.getValue()+"."+you.substring(0, 1));
					weight_value.setText(String.valueOf(weight)+"kg");
					dialog.dismiss();
					break;
				case R.id.dialog_weight_no:
					dialog.dismiss();
					weight_value.setText("----");
					break;
				//����ʱ��
				case R.id.dialog_time_ok:
					shijian_.setText(t.getCurrentHour()+":"+t.getCurrentMinute());
					dialog.dismiss();
					break;
				case R.id.dialog_time_no:
					dialog.dismiss();
					break;
				//����ʱ��
				case R.id.dialog_date_ok:
					//��������
					riqi.setText(d.getYear()+"��"+d.getMonth()+"��");
					dialog.dismiss();
					break;
				case R.id.dialog_date_no:
					dialog.dismiss();
					break;
				//�Ի���ı���
				case R.id.dialog_bc:
					saveDate();
					break;
				//�Ի���Ĳ�����
				case R.id.dialog_bbc:
					//��ʼ��������
					lists.clear();
					ConstantValues.tempSelectBitmap.clear();
					weight =0;
					dialog.dismiss();
					MyApplication.getInstance().removeActivity(AddSport_Activity.this);
					finish();
					break;
				//�Ի����ȡ��
				case R.id.dialog_qx:
					//ʲôҲ������ȡ���Ի���
					dialog.dismiss();
					break;
				default:
					break;
			}
		}
	};
	//����
	@Override
	protected void onActivityResult(int requestcode, int resultcode, Intent initent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestcode, resultcode, initent);
		switch (resultcode) {
			//ͼƬ
			case RESULT_OK:
				if(ConstantValues.tempSelectBitmap.size()<3){
					//�ɹ����ճɹ�
					//����ͼƬ������
					String pathname =ImageUtils.getTempFileName();
					//��ȡ�ɹ����յ�ͼƬ
					Bitmap success =(Bitmap)initent.getExtras().get("data");
					//���浽sd����
					FileUtils.saveBitmap(success, pathname);
					//���浽ʵ���༰���ռ�����
					ImageItem item =new ImageItem();
					item.setBitmap(success);
					ConstantValues.tempSelectBitmap.add(item);
					gridAdapter.notifyDataSetChanged();
				}
				break;
			case ALUM_CODE:
				gridAdapter.notifyDataSetChanged();//����һ������
				break;
			//����
			case CODE_NO:
				lists.clear();
				sport_value.setText("----");
				break;
			case CODE:
				Bundle b =initent.getExtras();
				qianka =b.getDouble("qianka");
				lists =(ArrayList<Sport_item>)b.getSerializable("sports");//ȡ������
				sport_value.setText(String.valueOf(qianka));
				break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MyApplication.getInstance().removeActivity(this);
			finish();
		}
		return true;
	}
}
