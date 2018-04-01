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
 * 添加一项活动的界面
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
	//布局控件
	//返回键
	private Button back =null;
	private TextView commit =null;
	//列表 运动 食物 体重`
	private LinearLayout sport_lay =null;
	private TextView weight_value =null;
	private double weight ;
	private LinearLayout weight_lay =null;
	private TextView sport_value =null;
	private double sport ;
	private LinearLayout food_lay =null;
	private TextView food_value =null;
	private TextView feel_sport =null;

	//顶部的日期时间选择与显示
	private TextView riqi_ =null;
	private TextView shijian_ =null;
	private TextView riqi =null;
	private TextView week =null;
	private TextView day =null;

	//对话框选择器
	private AlertDialog dialog =null;
	private Button ok =null;
	private Button no =null;
	//体重选择
	private NumberPicker n1 =null;
	private NumberPicker n2 =null;
	//时间选择
	private TimePicker t =null;
	private DatePicker d =null;

	private Button bc =null;
	private Button bbc =null;
	private Button qx =null;
	/**相机*/
	private camera_GridAdapter gridAdapter =null;
	private GridView gridView =null;
	private PopupWindow popupWindow =null;

	private LinearLayout camear_linearLayout =null;
	public static Bitmap bitmap =null;

	private Thread thread;//线程
	private static final int TAKE_PICTURE = 0x000011;//相机拍照的返回码

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
		//三个布局的监听器
		sport_lay.setOnClickListener(oListener);
		weight_lay.setOnClickListener(oListener);
		food_lay.setOnClickListener(oListener);
		//日期的监听器
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

		riqi_ =(TextView)findViewById(R.id.addsport_mark);//图片框中的
		riqi_.setText(DateUtils.getDay(Calendar.getInstance().getTime())+"");
		shijian_ =(TextView)findViewById(R.id.create_time);//添加事件
		//初始化顶部视图时间
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
	/**初始化图片上传*/
	private void InitCamear() {
		// TODO Auto-generated method stub
		gridView =(GridView)findViewById(R.id.camear_grid);

		View popview =View.inflate(AddSport_Activity.this, R.layout.dialog_popwindow, null);

		camear_linearLayout =(LinearLayout)popview.findViewById(R.id.camear_popup);
		popupWindow =CommonUtils.getPopWindow(AddSport_Activity.this, popview);

		RelativeLayout parentview =(RelativeLayout)popview.findViewById(R.id.parent);
		Button btn1 =(Button)popview.findViewById(R.id.item_popupwindows_camera);//相机
		Button btn2 =(Button)popview.findViewById(R.id.item_popupwindows_Photo);//相册
		Button btn3 =(Button)popview.findViewById(R.id.item_popupwindows_cancel);//取消

		parentview.setOnClickListener(new OnClickListener() {//点击外部的图片时取消操作

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
				camear_linearLayout.clearAnimation();//清除动画
			}
		});
		//相机拍照
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
		//相册选择
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
				//点击最后一个图片时，弹出对话框
				if(position == ConstantValues.tempSelectBitmap.size()){
					camear_linearLayout.setAnimation(AnimationUtils.loadAnimation(AddSport_Activity.this, R.anim.activity_translate_in));
					popupWindow.showAtLocation(View.inflate(AddSport_Activity.this, R.layout.activity_add_sport, null), Gravity.BOTTOM, 0, 0);
				}
			}
		});

	}
	//刷新与保存数据
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					gridAdapter.notifyDataSetChanged();
					break;
				case 0:
					Toast.makeText(AddSport_Activity.this, "数据保存完成", 1000).show();
					ConstantValues.tempSelectBitmap.clear();
					MyApplication.getInstance().removeActivity(AddSport_Activity.this);
					finish();
					break;
			}
		}
	};
	//启动线程不断刷新，检查到有更新就刷新
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

	//布局事件的监听器
	OnClickListener oListener =new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			LinearLayout layout =null;
			switch (v.getId()) {
				//返回按钮
				case R.id.addsport_back:
					//当有数据时，出现提示框
					//加载布局
					if(lists.size()>0||weight>0||ConstantValues.tempSelectBitmap.size()>0){
						View v1 =View.inflate(AddSport_Activity.this, R.layout.dialog_back, null);
						bbc =(Button)v1.findViewById(R.id.dialog_bbc);
						bc =(Button)v1.findViewById(R.id.dialog_bc);
						qx =(Button)v1.findViewById(R.id.dialog_qx);
						bbc.setOnClickListener(clickListener);
						bc.setOnClickListener(clickListener);
						qx.setOnClickListener(clickListener);
						//对话框
						dialog =new AlertDialog.Builder(AddSport_Activity.this).setView(v1).show();
					}else{
						MyApplication.getInstance().removeActivity(AddSport_Activity.this);
						finish();
					}
					break;
				//提交按钮
				case R.id.addsport_add:
					//首先判断下日期，
					saveDate();
					break;


//					//保存为常用运动
//					List<Sport_often> oftens =new ArrayList<Sport_often>();
//					for(Sport_item item:lists){
//						//判断数据库是否有，有则增加order
//						Sport_often o =(Sport_often) DataSupport.where("mode = ?",item.getName()).find(Sport_often.class);
//						if(o!=null){
//							//更新
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
//					Toast.makeText(AddSport_Activity.this, "亲，请选择数据", 2000).show();
//				}

				//日期选择
				case R.id.addsport_mark:

					layout =(LinearLayout)getLayoutInflater().inflate(R.layout.dialog_date, null);
					d =(DatePicker)layout.findViewById(R.id.datepicker);
					//获取按钮
					ok =(Button)layout.findViewById(R.id.dialog_date_ok);
					no =(Button)layout.findViewById(R.id.dialog_date_no);
					//设置属性
					d.setMaxDate(Calendar.getInstance().getTimeInMillis());//当前时间

					dialog =new AlertDialog.Builder(AddSport_Activity.this).setView(layout).show();
					//对话框的按钮监听器
					ok.setOnClickListener(clickListener);
					no.setOnClickListener(clickListener);
					break;
				//时间选择
				case R.id.create_time:

					layout =(LinearLayout)getLayoutInflater().inflate(R.layout.dialog_time, null);
					t =(TimePicker)layout.findViewById(R.id.timepicker);
					//获取按钮
					ok =(Button)layout.findViewById(R.id.dialog_time_ok);
					no =(Button)layout.findViewById(R.id.dialog_time_no);
					//设置属性
					t.setIs24HourView(true);//设置24小时制 去掉上午下午
					t.setCurrentHour(DateUtils.getHour(Calendar.getInstance().getTime()));
					t.setCurrentMinute(DateUtils.getMinute(Calendar.getInstance().getTime()));
					dialog =new AlertDialog.Builder(AddSport_Activity.this).setView(layout).show();
					//对话框的按钮监听器
					ok.setOnClickListener(clickListener);
					no.setOnClickListener(clickListener);
					break;
				//体重选择
				case R.id.addsport_item1:
					layout =(LinearLayout)getLayoutInflater().inflate(R.layout.dialog_weight, null);
					TextView title =(TextView)layout.findViewById(R.id.dialog_title);
					title.setText("选择体重");
					n1 =(NumberPicker)layout.findViewById(R.id.numberpicker1);
					n2 =(NumberPicker)layout.findViewById(R.id.numberpicker2);
					//获取按钮
					ok =(Button)layout.findViewById(R.id.dialog_weight_ok);
					no =(Button)layout.findViewById(R.id.dialog_weight_no);

					//设置数值选择器左边的的基本属性
					n1.setValue(50);
					n1.setMaxValue(300);
					n1.setMinValue(30);

					//自定义右边的数值
					String value[] =new String[]{"0kg","1kg","2kg","3kg","4kg","5kg","6kg","7kg","8kg"
							,"9kg"};
					n2.setMinValue(0);
					n2.setMaxValue(value.length-1);
					n2.setDisplayedValues(value);

					//创建对话框
					dialog =new AlertDialog.Builder(AddSport_Activity.this).setView(layout).show();
					//对话框的按钮监听器
					ok.setOnClickListener(clickListener);
					no.setOnClickListener(clickListener);
					break;
				//运动
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
		//有数据时 进行上传保存
		final String feel_str =feel_sport.getText().toString();
		if(lists.size()>0||weight>0||ConstantValues.tempSelectBitmap.size()>0||!TextUtils.isEmpty(feel_str)){
			Toast.makeText(AddSport_Activity.this, "正在提交数据", 1000).show();
			//开启线程来保存数据
			thread =new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Connector.getDatabase();
					//获取日期的格式字符串 yyyy年mm月dd日
					String date_str =riqi.getText().toString().trim()+riqi_.getText()
							.toString().trim()+"日";
					/**创建数据库对象*/
					Sport_add add =new Sport_add();
					//获取日期格式化对象yyyy年mm月dd日HH：ss
					String time =date_str+" "+shijian_.getText().toString().trim();
					add.setTime(Calendar.getInstance().getTime());
					//保存日期
					add.setRiqi(time);

					//保存体重
					if(weight>0){
						add.setWeight(weight);
					}
					//保存感受
					if(!TextUtils.isEmpty(feel_str)){
						add.setText(feel_str);
					}
					//保存图片
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
					//数据log查看
					LogUtils.i("数据",
							"/n时间"+add.getRiqi()+"/n感受"+add.getText()+"/n体重"+add.getWeight()+"/n照片1"+add.getPhoto1()+"/n照片2"+add.getPhoto2()+
									"/n照片3"+add.getPhoto3()
					);
					//add.setItems(lists);
					add.saveThrows();//保存数据库
					//保存运动
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

	//对话框的监听器
	OnClickListener clickListener =new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
				//体重选择 对话框
				case R.id.dialog_weight_ok:
					//设置体重
					String value[] =n2.getDisplayedValues();
					String you =value[n2.getValue()];//获取自定义的数值

					weight =Double.parseDouble(n1.getValue()+"."+you.substring(0, 1));
					weight_value.setText(String.valueOf(weight)+"kg");
					dialog.dismiss();
					break;
				case R.id.dialog_weight_no:
					dialog.dismiss();
					weight_value.setText("----");
					break;
				//设置时间
				case R.id.dialog_time_ok:
					shijian_.setText(t.getCurrentHour()+":"+t.getCurrentMinute());
					dialog.dismiss();
					break;
				case R.id.dialog_time_no:
					dialog.dismiss();
					break;
				//设置时间
				case R.id.dialog_date_ok:
					//设置日期
					riqi.setText(d.getYear()+"年"+d.getMonth()+"月");
					dialog.dismiss();
					break;
				case R.id.dialog_date_no:
					dialog.dismiss();
					break;
				//对话框的保存
				case R.id.dialog_bc:
					saveDate();
					break;
				//对话框的不保存
				case R.id.dialog_bbc:
					//初始化话数据
					lists.clear();
					ConstantValues.tempSelectBitmap.clear();
					weight =0;
					dialog.dismiss();
					MyApplication.getInstance().removeActivity(AddSport_Activity.this);
					finish();
					break;
				//对话框的取消
				case R.id.dialog_qx:
					//什么也不做，取消对话框
					dialog.dismiss();
					break;
				default:
					break;
			}
		}
	};
	//返回
	@Override
	protected void onActivityResult(int requestcode, int resultcode, Intent initent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestcode, resultcode, initent);
		switch (resultcode) {
			//图片
			case RESULT_OK:
				if(ConstantValues.tempSelectBitmap.size()<3){
					//成功拍照成功
					//创建图片的名称
					String pathname =ImageUtils.getTempFileName();
					//获取成功拍照的图片
					Bitmap success =(Bitmap)initent.getExtras().get("data");
					//保存到sd卡中
					FileUtils.saveBitmap(success, pathname);
					//保存到实体类及拍照集合中
					ImageItem item =new ImageItem();
					item.setBitmap(success);
					ConstantValues.tempSelectBitmap.add(item);
					gridAdapter.notifyDataSetChanged();
				}
				break;
			case ALUM_CODE:
				gridAdapter.notifyDataSetChanged();//更新一下数据
				break;
			//数据
			case CODE_NO:
				lists.clear();
				sport_value.setText("----");
				break;
			case CODE:
				Bundle b =initent.getExtras();
				qianka =b.getDouble("qianka");
				lists =(ArrayList<Sport_item>)b.getSerializable("sports");//取得数据
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
