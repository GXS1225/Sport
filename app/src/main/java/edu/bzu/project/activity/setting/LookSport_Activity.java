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
 * �鿴��¼�˶�����ʷ��Ϣ
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
	/**ĳʱ������ڱ���*/
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private int week_c = 0;//ĳ�µĵڼ�������
	private int week_num = 0;//ĳ�µ���������
	//��ǰ�����ڱ���
	private int currentYear;
	private int currentMonth;
	private int currentWeek;//��ǰʱ���ǵ�ǰ�µĵڼ�������
	private int currentDay;
	private int currentNum;//��ǰ�µ���������
	
	private String currentDate = "";
	/**ĳ�µ�������*/
	private int daysOfMonth = 0;  
	/**ĳ�µĵ�һ�������ڼ�*/
	private int dayOfWeek = 0; 
	/***/
	private int weeksOfMonth = 0;
	/**�Ƿ�Ϊ����*/
	private boolean isLeapyear = false;
	
	private TextView tv_date =null;
	private int selectPostion = 0;//ѡ���λ�ã����ڵ�λ��
	private int oldSelectPostion =0;
	private String dayNumbers[] = new String[7];//���ں�����
	
	//���ݿ�
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
		
		//��ȡ����
		isLeapyear =DateUtils.isLeapYear(currentYear);//�Ƿ�����
		daysOfMonth =DateUtils.getDaysOfMonth(isLeapyear, currentMonth);
		dayOfWeek =DateUtils.getWeekdayOfMonth(currentYear, currentMonth);
		//���ڵ�����
		currentNum =week_num =getWeeksOfMonth();//���㵱ǰ������(������ĳ�µĵ�һ�������ڼ���һ�¶�����)
		currentNum = week_num;
		//�ǵڼ�������
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
	 * ���¼��㵱ǰ�����£��û��϶���Ļʱ��Ҫ���¼������ڣ�����һ�µ�����
	 */
	public void getCurrent() {
		//1�������������� ��������ʱ
		if (currentWeek > currentNum) {//��ǰ�ڼ�������������������
			if (currentMonth + 1 <= 12) {//���·� ��һ����
				currentMonth++;
			} else {				//��һ��
				currentMonth = 1;
				currentYear++;
			}
			currentWeek = 1;//��ʼΪ1
			currentNum = DateUtils.getWeeksOfMonth(currentYear, currentMonth);//�����·��ڻ����������
		} else if (currentWeek == currentNum) {//��ǰ������ ���� ��������
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
	 * �������һ�� �Ǽ�������
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
		//����
		looksport_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyApplication.getInstance().removeActivity(LookSport_Activity.this);
				finish();
			}
		});
		//����
		back_today.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewFlipper.removeAllViews();//���
				addGridView();
				initToday();
				tv_date.setText(year_c + "��" + month_c + "��" + day_c + "��");//���õ�ǰ����
				adapter = new LookSport_adapter(LookSport_Activity.this, getResources(), currentYear,
						currentMonth, currentWeek, currentNum, selectPostion,
						currentWeek == 1 ? true : false);
				adapter.setoFirstSelectListener(LookSport_Activity.this);
				dayNumbers = adapter.getDayNumbers();//���ں�
				selectPostion = adapter.getTodayPosition();//��õ�ǰ���ڵ�λ��
				gridView.setSelection(selectPostion);//���õ�ǰ��ѡ��
				gridView.setAdapter(adapter);//ִ�� ��ʼ����
				viewFlipper.addView(gridView, 0);//������
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
		tv_date.setText(year_c + "��" + month_c + "��" + day_c + "��");//���õ�ǰ����
	
		viewFlipper =(ViewFlipper)findViewById(R.id.flipper1);
		//�õ�ǰʱ������ ����ʼ��������
		
		adapter =new LookSport_adapter(LookSport_Activity.this, getResources(), currentYear,
				currentMonth, currentWeek, currentNum, selectPostion,
				currentWeek == 1 ? true : false);
		
		adapter.setoFirstSelectListener(this);
		addGridView();//����grideview
		dayNumbers = adapter.getDayNumbers();//���ں�
		selectPostion = adapter.getTodayPosition();//��õ�ǰ���ڵ�λ��
		gridView.setSelection(selectPostion);//���õ�ǰ��ѡ��
		gridView.setAdapter(adapter);//ִ�� ��ʼ����
		viewFlipper.addView(gridView, 0);//������
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
				return gestureDetector.onTouchEvent(event);//���������ƴ���
			}
		});
		//������
		gridView.setOnItemClickListener(new OnItemClickListener() {
			//�����һ���ʱ��Ҫ�ж�����
			//�ڵ���¼���ҲҪ�ж�����
			//�������ܱ�֤ �ڲ��Ϸ������� û������
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//�ж��Ƿ���
				String user_date =adapter.getCurrentYear(position) + "��"
						+ adapter.getCurrentMonth(position) + "��"
						+ dayNumbers[position] + "��";
				//��һ��ʹ�ø����
				String is_firstTime =DateUtils.StringToString((String) SPUtils.get(LookSport_Activity.this, ConstantValues.First_Time, ""),
						DateStyle.YYYY_MM_DD_CN);
				if(isOver(user_date.trim()) || DateUtils.getIntervalDays(user_date, is_firstTime,false)<0){
					//������޷�Ӧ
					Toast.makeText(LookSport_Activity.this, "���ڲ��Ϸ�", 0).show();
				}else{
					selectPostion = position;
					adapter.setSeclection(position);
					adapter.notifyDataSetChanged();
					tv_date.setText(adapter.getCurrentYear(selectPostion) + "��"
							+ adapter.getCurrentMonth(selectPostion) + "��"
							+ dayNumbers[position] + "��");
					//���ݿ������
					//1 ��ѯ����ָ�����ڵ�����
					items =selectDate(tv_date.getText().toString().trim());
					//�ж�
					if(items!=null){
						LogUtils.i("��������", "���ݸ���"+items.size());
						listview_adapter.notifyDataSetChanged();
					}else {
					}
				}
			}
		});
		gridView.setLayoutParams(params);
	}
	//ģ������ÿ������
	private List<Sport_add> selectDate(String user_date){
		String selectDate =DateUtils.StringToString(user_date, DateStyle.YYYY_MM_DD_CN);
		//���Ҳ���
		String curr_time =DateUtils.DateToString(Calendar.getInstance()
				.getTime(), DateStyle.YYYY_MM_DD_CN);
		if(selectDate.equals(curr_time)){//����  ���sharedȡ
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
		//��ѯ�˶�
		items =DataSupport.where("riqi like ?",selectDate+"%").order("time desc").find(Sport_add.class);
		return items;
	}
	private boolean isOver(String user_date){
		//������ڵ���ȷ�ԣ�
			//��ǰ������
		Date currdate =DateUtils.StringToDate(DateUtils.getYear(Calendar.getInstance().getTime())+"��"+
				(Calendar.getInstance().get(Calendar.MONTH)+1)+"��"+ //�·ݼ�1
				DateUtils.getDay(Calendar.getInstance().getTime())+"��", DateStyle.YYYY_MM_DD_CN);
		Date selectDate =DateUtils.StringToDate(user_date);
		LogUtils.i("��ǰʱ��", user_date);
		LogUtils.i("��ǰʱ��", DateUtils.getYear(Calendar.getInstance().getTime())+"��"+
				(Calendar.getInstance().get(Calendar.MONTH)+1)+"��"+ //�·ݼ�1
				DateUtils.getDay(Calendar.getInstance().getTime())+"��");
		return currdate.getTime()<selectDate.getTime();
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		int gvFlag = 0;
		/**
		 * ���󻬶���ʱ��Ҫ�ж��Ƿ���
		 * 	1 ���ڣ����ܻ���
		 *  2 �����ڣ�
		 * 	   ָʾ���λ�ó���ʱ���ص���ǰ
		 * 	   ָʾ�㲻����ʱ��ָʾ�㲻��
		 * ע�⣬���ж��Ƿ���ʱ��������ָʾ�����������һ��ʱ����Ҫ����һ���ʱ�䡣
		 */
		//ȡ�õ�ǰ���һ��
		String user_date =adapter.getCurrentYear(selectPostion) + "��"
				+ adapter.getCurrentMonth(selectPostion) + "��"
				+ dayNumbers[6] + "��";
		//ȡ�õ�ǰ����һ��
		String user_date_zao =adapter.getCurrentYear(selectPostion) + "��"
				+ adapter.getCurrentMonth(selectPostion) + "��"
				+ dayNumbers[0] + "��";
		user_date =DateUtils.addDay(user_date, 1);
		user_date_zao =DateUtils.addDay(user_date_zao, -1);
		if (e1.getX() - e2.getX() > 75) {//�һ�
			//��
			//��ǰ��ʾ�����һ������ �Ƿ�ʱ
			if(isOver(user_date.trim())){
				Toast.makeText(LookSport_Activity.this, "�ף�û�������ˣ�", 1000).show();
			}else{
				// ����
				addGridView();
				currentWeek++;//��һ��
				getCurrent();//���¼�������
				//���´���������
				adapter = new LookSport_adapter(this, getResources(), currentYear,
						currentMonth, currentWeek, currentNum, selectPostion,
						currentWeek == 1 ? true : false);
				dayNumbers = adapter.getDayNumbers();
				//ȡ�û����� ��һָʾ���λ�õ����ڣ�Ҳ������һ�ܣ���ǰ�ж��Ƿ���--������λ��
				String user_date1 =adapter.getCurrentYear(selectPostion) + "��"
						+ adapter.getCurrentMonth(selectPostion) + "��"
						+ dayNumbers[selectPostion] + "��";
				if(isOver(user_date1.trim())){//����ָʾ���λ��(��ǰ���ڵ�λ��)
					//����Ϊ��ǰʱ��
					selectPostion = adapter.getTodayPosition();//��õ�ǰ���ڵ�λ��
				}
				adapter.setoFirstSelectListener(this);
//				dayNumbers = adapter.getDayNumbers();
				gridView.setAdapter(adapter);
				tv_date.setText(adapter.getCurrentYear(selectPostion) + "��"
						+ adapter.getCurrentMonth(selectPostion) + "��"
						+ dayNumbers[selectPostion] + "��");
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
		} else if (e1.getX() - e2.getX() < -75) {//��
			//�״�ʹ�����֮ǰ ���ڲ���ѡ��
			String is_firstTime =DateUtils.StringToString((String) SPUtils.get(LookSport_Activity.this, ConstantValues.First_Time, ""),
						DateStyle.YYYY_MM_DD_CN);
			
			if(DateUtils.getIntervalDays(user_date_zao, is_firstTime,false)>0){
				addGridView();
				currentWeek--;
				getCurrent();
				adapter = new LookSport_adapter(this, getResources(), currentYear,
						currentMonth, currentWeek, currentNum, selectPostion,
						currentWeek == 1 ? true : false);
				String user_date1 =adapter.getCurrentYear(selectPostion) + "��"
						+ adapter.getCurrentMonth(selectPostion) + "��"
						+ dayNumbers[selectPostion] + "��";
				if(isOver(user_date1.trim())){
					//��ʱ���������Ҫ���¶�λָ��  ������Ϊ��ǰʱ��
					selectPostion = adapter.getTodayPosition();//��õ�ǰ���ڵ�λ��
				}
				adapter.setoFirstSelectListener(this);
				dayNumbers = adapter.getDayNumbers();
				gridView.setAdapter(adapter);
				tv_date.setText(adapter.getCurrentYear(selectPostion) + "��"
						+ adapter.getCurrentMonth(selectPostion) + "��"
						+ dayNumbers[selectPostion] + "��");
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
				Toast.makeText(LookSport_Activity.this, "û�и��������", 0).show();
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
		//ȡ���û��������ʱ��
		String user_date =adapter.getCurrentYear(selectPostion) + "��"
				+ adapter.getCurrentMonth(selectPostion) + "��"
				+ dayNumbers[position] + "��";
		//listview������
		items =selectDate(user_date.trim());
		listview_adapter =new LookSport_item_adapter(LookSport_Activity.this, items);
		listView.setAdapter(listview_adapter);
	}

}
