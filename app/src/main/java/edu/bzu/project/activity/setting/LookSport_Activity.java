package edu.bzu.project.activity.setting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.litepal.crud.DataSupport;



import edu.bzu.project.R;
import edu.bzu.project.R.layout;
import edu.bzu.project.R.menu;
import edu.bzu.project.adapter.LookSport_adapter;
import edu.bzu.project.adapter.LookSport_adapter.onFirstSelectListener;
import edu.bzu.project.adapter.LookSport_item_adapter;
import edu.bzu.project.domain.Pedometer;
import edu.bzu.project.domain.Sport_add;
import edu.bzu.project.utils.ConstantValues;
import edu.bzu.project.utils.MyApplication;
import edu.bzu.project.utils.DateStyle;
import edu.bzu.project.utils.DateUtils;
import edu.bzu.project.utils.LogUtils;
import edu.bzu.project.utils.SPUtils;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 查看记录运动的历史信息
 *
 * @author houenshuo
 *
 */
public class LookSport_Activity extends Activity implements OnGestureListener,onFirstSelectListener{
	private static String TAG ="LookSport_Activity";

	private Button looksport_back =null;
	private Button back_today =null;
	
	private ViewFlipper viewFlipper =null;
	private GestureDetector gestureDetector =null;
	private GridView gridView = null;
	private ListView listView =null;
	private TextView looksport_bushu =null;
	private LookSport_item_adapter listview_adapter =null;

	private LookSport_adapter adapter =null;
	/**某时间的日期变量*/
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private int week_c = 0;//某月的第几个星期
	private int week_num = 0;//某月的总星期数
	//当前的日期变量
	private int currentYear;
	private int currentMonth;
	private int currentWeek;//当前时间是当前月的第几个星期
	private int currentDay;
	private int currentNum;//当前月的总星期数
	
	private String currentDate = "";
	/**某月的总天数*/
	private int daysOfMonth = 0;  
	/**某月的第一天是星期几*/
	private int dayOfWeek = 0; 
	/***/
	private int weeksOfMonth = 0;
	/**是否为闰年*/
	private boolean isLeapyear = false;
	
	private TextView tv_date =null;
	private int selectPostion = 0;//选择的位置：星期的位置
	private int oldSelectPostion =0;
	private String dayNumbers[] = new String[7];//日期号数组
	
	//数据库
	private List<Sport_add> items =new ArrayList<Sport_add>();
	
	
	public LookSport_Activity() {
		// TODO Auto-generated constructor stub
		initToday();
		
	}
	private void initToday() {
		// TODO Auto-generated method stub
		currentYear =year_c =DateUtils.getYear(Calendar.getInstance().getTime());
		currentMonth =month_c =DateUtils.getMonth(Calendar.getInstance().getTime())+1;
		currentDay =day_c =DateUtils.getDay(Calendar.getInstance().getTime());
		
		//获取星期
		isLeapyear =DateUtils.isLeapYear(currentYear);//是否闰年
		daysOfMonth =DateUtils.getDaysOfMonth(isLeapyear, currentMonth);
		dayOfWeek =DateUtils.getWeekdayOfMonth(currentYear, currentMonth);
		//星期的总数
		currentNum =week_num =getWeeksOfMonth();//计算当前的周数(参数，某月的第一天是星期几，一月多少天)
		currentNum = week_num;
		//是第几个星期
		if (dayOfWeek == 7) {
			week_c = day_c / 7 + 1;
		} else {
			if (day_c <= (7 - dayOfWeek)) {
				week_c = 1;
			} else {
				if ((day_c - (7 - dayOfWeek)) % 7 == 0) {
					week_c = (day_c - (7 - dayOfWeek)) / 7 + 1;
				} else {
					week_c = (day_c - (7 - dayOfWeek)) / 7 + 2;
				}
			}
		}
		currentWeek =week_c;
		getCurrent();
	}
	public int getWeeksOfMonth() {
		// getCalendar(year, month);
		int preMonthRelax = 0;
		if (dayOfWeek != 7) {
			preMonthRelax = dayOfWeek;
		}
		if ((daysOfMonth + preMonthRelax) % 7 == 0) {
			weeksOfMonth = (daysOfMonth + preMonthRelax) / 7;
		} else {
			weeksOfMonth = (daysOfMonth + preMonthRelax) / 7 + 1;
		}
		return weeksOfMonth;
	}
	/**
	 * 重新计算当前的年月，用户拖动屏幕时都要重新计算日期，根据一月的周数
	 */
	public void getCurrent() {
		//1，当日期数大于 总星期数时
		if (currentWeek > currentNum) {//当前第几星期数大于总星期数
			if (currentMonth + 1 <= 12) {//加月份 到一年则
				currentMonth++;
			} else {				//加一年
				currentMonth = 1;
				currentYear++;
			}
			currentWeek = 1;//初始为1
			currentNum = DateUtils.getWeeksOfMonth(currentYear, currentMonth);//根据月份在获得总星期数
		} else if (currentWeek == currentNum) {//当前星期数 等于 总星期数
			if (getLastDayOfWeek(currentYear, currentMonth) == 6) {
			} else {
				if (currentMonth + 1 <= 12) {
					currentMonth++;
				} else {
					currentMonth = 1;
					currentYear++;
				}
				currentWeek = 1;
				currentNum = DateUtils.getWeeksOfMonth(currentYear, currentMonth);
			}

		} else if (currentWeek < 1) {
			if (currentMonth - 1 >= 1) {
				currentMonth--;
			} else {
				currentMonth = 12;
				currentYear--;
			}
			currentNum = DateUtils.getWeeksOfMonth(currentYear, currentMonth);
			currentWeek = currentNum - 1;
		}
	}
	/**
	 * 返回最后一天 是几个星期
	 * @param year
	 * @param month
	 */
	public int getLastDayOfWeek(int year, int month) {
		return DateUtils.getWeekDayOfLastMonth(year, month,
				DateUtils.getDaysOfMonth(isLeapyear, month));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(LookSport_Activity.this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_look_sport);
		LogUtils.i(TAG, "onCreate()");
		initView();
		initData();
		initEvent();	
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		
	}
	private void initEvent() {
		// TODO Auto-generated method stub
		//返回
		looksport_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyApplication.getInstance().removeActivity(LookSport_Activity.this);
				finish();
			}
		});
		//今天
		back_today.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewFlipper.removeAllViews();//清除
				addGridView();
				initToday();
				tv_date.setText(year_c + "年" + month_c + "月" + day_c + "日");//设置当前日期
				adapter = new LookSport_adapter(LookSport_Activity.this, getResources(), currentYear,
						currentMonth, currentWeek, currentNum, selectPostion,
						currentWeek == 1 ? true : false);
				adapter.setoFirstSelectListener(LookSport_Activity.this);
				dayNumbers = adapter.getDayNumbers();//日期号
				selectPostion = adapter.getTodayPosition();//获得当前日期的位置
				gridView.setSelection(selectPostion);//设置当前的选项
				gridView.setAdapter(adapter);//执行 开始绘制
				viewFlipper.addView(gridView, 0);//添加组件
			}
		});
	}
	private void initView() {
		// TODO Auto-generated method stub
		looksport_back =(Button)findViewById(R.id.looksport_back);
		back_today =(Button)findViewById(R.id.back_today);
		tv_date =(TextView)findViewById(R.id.tv_date);
		listView =(ListView)findViewById(R.id.look_listview);
		looksport_bushu =(TextView)findViewById(R.id.looksport_bushu);
		tv_date.setText(year_c + "年" + month_c + "月" + day_c + "日");//设置当前日期
	
		viewFlipper =(ViewFlipper)findViewById(R.id.flipper1);
		//用当前时间日期 来初始化适配器
		
		adapter =new LookSport_adapter(LookSport_Activity.this, getResources(), currentYear,
				currentMonth, currentWeek, currentNum, selectPostion,
				currentWeek == 1 ? true : false);
		
		adapter.setoFirstSelectListener(this);
		addGridView();//增加grideview
		dayNumbers = adapter.getDayNumbers();//日期号
		selectPostion = adapter.getTodayPosition();//获得当前日期的位置
		gridView.setSelection(selectPostion);//设置当前的选项
		gridView.setAdapter(adapter);//执行 开始绘制
		viewFlipper.addView(gridView, 0);//添加组件
		gestureDetector =new GestureDetector(this, this);
	}
	private void addGridView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		gridView = new GridView(this);
		gridView.setNumColumns(7);
		gridView.setGravity(Gravity.CENTER_VERTICAL);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(1);
		gridView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);//交给了手势处理
			}
		});
		//监听器
		gridView.setOnItemClickListener(new OnItemClickListener() {
			//在左右滑动时需要判断日期
			//在点击事件中也要判断日期
			//这样就能保证 在不合法的日期 没有数据
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//判断是否超期
				String user_date =adapter.getCurrentYear(position) + "年"
						+ adapter.getCurrentMonth(position) + "月"
						+ dayNumbers[position] + "日";
				//第一次使用该软件
				String is_firstTime =DateUtils.StringToString((String) SPUtils.get(LookSport_Activity.this, ConstantValues.First_Time, ""),
						DateStyle.YYYY_MM_DD_CN);
				if(isOver(user_date.trim()) || DateUtils.getIntervalDays(user_date, is_firstTime,false)<0){
					//点击后无反应
					Toast.makeText(LookSport_Activity.this, "日期不合法", 0).show();
				}else{
					selectPostion = position;
					adapter.setSeclection(position);
					adapter.notifyDataSetChanged();
					tv_date.setText(adapter.getCurrentYear(selectPostion) + "年"
							+ adapter.getCurrentMonth(selectPostion) + "月"
							+ dayNumbers[position] + "日");
					//数据库操作：
					//1 查询到了指定日期的数据
					items =selectDate(tv_date.getText().toString().trim());
					//判断
					if(items!=null){
						LogUtils.i("加载数据", "数据个数"+items.size());
						listview_adapter.notifyDataSetChanged();
					}else {
					}
				}
			}
		});
		gridView.setLayoutParams(params);
	}
	//模糊查找每天数据
	private List<Sport_add> selectDate(String user_date){
		String selectDate =DateUtils.StringToString(user_date, DateStyle.YYYY_MM_DD_CN);
		//查找步数
		String curr_time =DateUtils.DateToString(Calendar.getInstance()
				.getTime(), DateStyle.YYYY_MM_DD_CN);
		if(selectDate.equals(curr_time)){//本天  则从shared取
			int bushu =(Integer) SPUtils.get(LookSport_Activity.this, ConstantValues.BU_SHU, 0);
			looksport_bushu.setText(String.valueOf(bushu));
		}else{
			int bushu=0;
			List<Pedometer> pedometer =DataSupport.where("riqi = ?",selectDate).find(Pedometer.class);
			if(pedometer.size()<0){
				 bushu =(Integer) SPUtils.get(LookSport_Activity.this, ConstantValues.BU_SHU, 0);
			}else{
				 bushu =pedometer.get(0).getBushul();
			}
			looksport_bushu.setText(String.valueOf(bushu));
		}
		//查询运动
		items =DataSupport.where("riqi like ?",selectDate+"%").order("time desc").find(Sport_add.class);
		return items;
	}
	private boolean isOver(String user_date){
		//检查日期的正确性，
			//当前的日期
		Date currdate =DateUtils.StringToDate(DateUtils.getYear(Calendar.getInstance().getTime())+"年"+
				(Calendar.getInstance().get(Calendar.MONTH)+1)+"月"+ //月份加1
				DateUtils.getDay(Calendar.getInstance().getTime())+"日", DateStyle.YYYY_MM_DD_CN);
		Date selectDate =DateUtils.StringToDate(user_date);
		LogUtils.i("当前时间", user_date);
		LogUtils.i("当前时间", DateUtils.getYear(Calendar.getInstance().getTime())+"年"+
				(Calendar.getInstance().get(Calendar.MONTH)+1)+"月"+ //月份加1
				DateUtils.getDay(Calendar.getInstance().getTime())+"日");
		return currdate.getTime()<selectDate.getTime();
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		int gvFlag = 0;
		/**
		 * 向左滑动的时候，要判断是否超期
		 * 	1 超期：不能滑动
		 *  2 不超期：
		 * 	   指示点的位置超期时：回到当前
		 * 	   指示点不超期时，指示点不动
		 * 注意，在判断是否超期时，若最后的指示点是数组最后一个时，需要加上一天的时间。
		 */
		//取得当前最后一天
		String user_date =adapter.getCurrentYear(selectPostion) + "年"
				+ adapter.getCurrentMonth(selectPostion) + "月"
				+ dayNumbers[6] + "日";
		//取得当前最早一天
		String user_date_zao =adapter.getCurrentYear(selectPostion) + "年"
				+ adapter.getCurrentMonth(selectPostion) + "月"
				+ dayNumbers[0] + "日";
		user_date =DateUtils.addDay(user_date, 1);
		user_date_zao =DateUtils.addDay(user_date_zao, -1);
		if (e1.getX() - e2.getX() > 75) {//右滑
			//左滑
			//当前显示的最后一个日期 是否超时
			if(isOver(user_date.trim())){
				Toast.makeText(LookSport_Activity.this, "亲，没有数据了！", 1000).show();
			}else{
				// 向左滑
				addGridView();
				currentWeek++;//加一周
				getCurrent();//重新计算日期
				//重新创建适配器
				adapter = new LookSport_adapter(this, getResources(), currentYear,
						currentMonth, currentWeek, currentNum, selectPostion,
						currentWeek == 1 ? true : false);
				dayNumbers = adapter.getDayNumbers();
				//取得滑动后 上一指示点的位置的日期，也就是下一周，提前判断是否超期--》调整位置
				String user_date1 =adapter.getCurrentYear(selectPostion) + "年"
						+ adapter.getCurrentMonth(selectPostion) + "月"
						+ dayNumbers[selectPostion] + "日";
				if(isOver(user_date1.trim())){//调整指示点的位置(当前日期的位置)
					//设置为当前时间
					selectPostion = adapter.getTodayPosition();//获得当前日期的位置
				}
				adapter.setoFirstSelectListener(this);
//				dayNumbers = adapter.getDayNumbers();
				gridView.setAdapter(adapter);
				tv_date.setText(adapter.getCurrentYear(selectPostion) + "年"
						+ adapter.getCurrentMonth(selectPostion) + "月"
						+ dayNumbers[selectPostion] + "日");
				gvFlag++;
				viewFlipper.addView(gridView, gvFlag);
				adapter.setSeclection(selectPostion);
				this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_left_in));
				this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_left_out));
				this.viewFlipper.showNext();
				viewFlipper.removeViewAt(0);
				return true;
			}
		} else if (e1.getX() - e2.getX() < -75) {//左滑
			//首次使用软件之前 日期不能选择
			String is_firstTime =DateUtils.StringToString((String) SPUtils.get(LookSport_Activity.this, ConstantValues.First_Time, ""),
						DateStyle.YYYY_MM_DD_CN);
			
			if(DateUtils.getIntervalDays(user_date_zao, is_firstTime,false)>0){
				addGridView();
				currentWeek--;
				getCurrent();
				adapter = new LookSport_adapter(this, getResources(), currentYear,
						currentMonth, currentWeek, currentNum, selectPostion,
						currentWeek == 1 ? true : false);
				String user_date1 =adapter.getCurrentYear(selectPostion) + "年"
						+ adapter.getCurrentMonth(selectPostion) + "月"
						+ dayNumbers[selectPostion] + "日";
				if(isOver(user_date1.trim())){
					//超时的情况，需要重新定位指标  并设置为当前时间
					selectPostion = adapter.getTodayPosition();//获得当前日期的位置
				}
				adapter.setoFirstSelectListener(this);
				dayNumbers = adapter.getDayNumbers();
				gridView.setAdapter(adapter);
				tv_date.setText(adapter.getCurrentYear(selectPostion) + "年"
						+ adapter.getCurrentMonth(selectPostion) + "月"
						+ dayNumbers[selectPostion] + "日");
				gvFlag++;
				viewFlipper.addView(gridView, gvFlag);
				adapter.setSeclection(selectPostion);
				this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_right_in));
				this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_right_out));
				this.viewFlipper.showPrevious();
				viewFlipper.removeViewAt(0);
				return true;
			}else{
				Toast.makeText(LookSport_Activity.this, "没有更多的数据", 0).show();
			}
			
			// }
		}
		return false;
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogUtils.i(TAG, "onPause()");
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtils.i(TAG, "onDestroy()");
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LogUtils.i(TAG, "onResume()");
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.look_sport_, menu);
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void firstSelectListener(int position) {
		// TODO Auto-generated method stub
		//取得用户点击日期时间
		String user_date =adapter.getCurrentYear(selectPostion) + "年"
				+ adapter.getCurrentMonth(selectPostion) + "月"
				+ dayNumbers[position] + "日";
		//listview的数据
		items =selectDate(user_date.trim());
		listview_adapter =new LookSport_item_adapter(LookSport_Activity.this, items);
		listView.setAdapter(listview_adapter);
	}

}
