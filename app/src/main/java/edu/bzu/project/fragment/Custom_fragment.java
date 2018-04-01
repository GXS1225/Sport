package edu.bzu.project.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;



import edu.bzu.project.R;
import edu.bzu.project.adapter.SportCustom_adapter;
import edu.bzu.project.adapter.SportsData_adapter;
import edu.bzu.project.domain.Sport_item;
import edu.bzu.project.domain.Sport_w;
import edu.bzu.project.other.OnJiLuClilckListener;
import edu.bzu.project.other.OnSubmitClickListener;
import edu.bzu.project.utils.MyApplication;
import edu.bzu.project.view.SwipeMenu;
import edu.bzu.project.view.SwipeMenuCreator;
import edu.bzu.project.view.SwipeMenuItem;
import edu.bzu.project.view.SwipeMenuListView;
import edu.bzu.project.view.SwipeMenuListView.OnMenuItemClickListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;
/**
 * 自定义运动的fragment
 *
 */
public class Custom_fragment extends BaseFragment implements OnJiLuClilckListener{

	public static String TAG="Custom_fragment";
	
	//布局
	private SwipeMenuListView listView =null;
	private ImageButton create_sport =null;
	private LinearLayout create_sport_lay =null;
	
	private SportCustom_adapter adapter =null;
	
	private List<Sport_w> lists =null;
	//添加的对话框
	private Dialog dialog =null;
	private EditText name =null;
	private EditText qianka =null;
	private Button back =null;
	private Button commit =null;
	
	//对话框选择器
	private LinearLayout layout =null;
	private Button ok =null;
	private Button no =null;
	//选择
	private NumberPicker n1 =null;
	private NumberPicker n2 =null;
	@Override
	public String getFragmentName() {
		// TODO Auto-generated method stub
		return TAG;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		lists =DataSupport.findAll(Sport_w.class);
	}

	private void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootv =inflater.inflate(R.layout.page_sport_custom, container, false);
		
		listView =(SwipeMenuListView)rootv.findViewById(R.id.swipeMenuListView1);
		create_sport =(ImageButton)rootv.findViewById(R.id.create_sport);
		create_sport_lay =(LinearLayout)rootv.findViewById(R.id.create_sport_lay);
		return rootv;
		
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		create_sport_lay.setOnClickListener(onClickListener);
	}

	OnClickListener onClickListener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.swipeMenuListView1:
				
				break;
			case R.id.create_sport_lay:
				View cv =View.inflate(getActivity(), R.layout.dialog_createsport, null);
				name =(EditText)cv.findViewById(R.id.create_name);
				qianka =(EditText)cv.findViewById(R.id.create_qianka);
				back =(Button)cv.findViewById(R.id.dialog_sport_no);
				commit =(Button)cv.findViewById(R.id.dialog_sport_ok);
				dialog =new AlertDialog.Builder(getActivity()).setView(cv).show();
				
				commit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String name_str =name.getText().toString();
						String qianka_str =qianka.getText().toString();
						if(!TextUtils.isEmpty(name_str) && !TextUtils.isEmpty(qianka_str)){
							//添加到数据库.没判定输入非法值
							Sport_w create =new Sport_w();
							create.setMode(name_str);
							create.setHeat(Double.parseDouble(qianka_str));
							create.saveThrows();
							Toast.makeText(getActivity(), "创建了属于自己的运动", 2000);
							dialog.dismiss();
							//刷新界面
							lists.add(create);
							adapter.notifyDataSetChanged();
						}
						Toast.makeText(getActivity(), "你没按要求填写,怪我咯！", 2000);
					}
				});
				back.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				
				break;
			default:
				break;
			}
		}
	};
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//读取数据库的操作，查找所有的自定义运动
		lists =DataSupport.findAll(Sport_w.class);
		adapter =new SportCustom_adapter(getActivity(),lists);
		adapter.setOnJiLuClilckListener(this);
		listView.setAdapter(adapter);
		
			SwipeMenuCreator creator = new SwipeMenuCreator() {

				@Override
				public void create(SwipeMenu menu) {

					SwipeMenuItem deleteItem = new SwipeMenuItem(
							getActivity().getApplicationContext());
					// set item background
					deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
							0x3F, 0x25)));
					// set item width
					deleteItem.setWidth(dp2px(90));
					// set a icon
					deleteItem.setIcon(R.drawable.ic_delete);
					// add to menu
					menu.addMenuItem(deleteItem);
				}
			};
		listView.setMenuCreator(creator);
		listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				// TODO Auto-generated method stub
				lists.remove(position);
				adapter.notifyDataSetChanged();
			}
		});
		initEvent();
		//以前的数据 更新listview
		if(getArguments()!=null){
			ArrayList<Sport_item> items =null;
			items =(ArrayList<Sport_item>)getArguments().getSerializable("customsyet");
			for(Sport_item item:items){
				lists.get(item.getPosition()).setItem(item);
			}
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	/**接口回调*/
	@Override
	public void onJiLuClick(final int position) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		layout =(LinearLayout)View.inflate(getActivity(), R.layout.dialog_weight, null);
		 n1 =(NumberPicker)layout.findViewById(R.id.numberpicker1);
		 n2 =(NumberPicker)layout.findViewById(R.id.numberpicker2);
		//获取按钮
		ok =(Button)layout.findViewById(R.id.dialog_weight_ok);
		no =(Button)layout.findViewById(R.id.dialog_weight_no);
	
		//自定义的数值
		String value1[] =new String[24];
		String value2[] =new String[60];
		for(int i =0;i<24;i++){
			value1[i]=i+"小时";
		}
		for(int i=0;i<60;i++){
			value2[i] =i+"分钟";
		}
		n1.setMinValue(0);
		n1.setMaxValue(value1.length-1);
		n1.setDisplayedValues(value1);
		n2.setMinValue(0);
		n2.setMaxValue(value2.length-1);
		n2.setDisplayedValues(value2);

		//创建对话框
		dialog =new AlertDialog.Builder(getActivity()).setView(layout).show();
		//对话框的按钮监听器
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int position1 =n1.getValue();
				int position2 =n2.getValue();
				//计算总时间
				String position1_str =n1.getDisplayedValues()[position1];
				position1 =Integer.parseInt(position1_str.substring(0, position1_str.lastIndexOf("小时")));
				String position2_str =n2.getDisplayedValues()[position2];
				position2 =Integer.parseInt(position2_str.substring(0, position2_str.lastIndexOf("分钟")));
				int time_sum =position1*60+position2;
				//获取千卡
				double qianka =lists.get(position).getHeat();
				double qianka_sum;
					//设置时间 为0时，需要改变列表项的图标
				if(time_sum == 0){
					//更改数据，设置为记录图标
					//判断是否选择过，选择过则清空之
					if(lists.get(position).getItem()!=null){
						Sport_item item =lists.get(position).getItem();
						lists.get(position).setItem(null);//设置空
						//改变activity的布局
						if(onSubmitClickListener !=null){
							onSubmitClickListener.onSubmitListener(item,false);//回调activity的方法，null则减一
						}
					}
				}else {
					qianka_sum =(double)time_sum/60*qianka;//总千卡
					DecimalFormat   fnum  =   new  DecimalFormat("0.00");
					String qianka_str =fnum.format(qianka_sum);
					qianka_sum =Double.parseDouble(qianka_str);
					//更改数据，设置单项的数值
					Sport_item item =new Sport_item();
					item.setPosition(position);
					item.setQianka_value(qianka);
					item.setName(lists.get(position).getMode());
					item.setHour(position1);
					item.setMinute(position2);
					item.setQianka(qianka_sum);
					item.setType(3);
					lists.get(position).setItem(item);//列表项 展示
					//改变activity的布局
					if(onSubmitClickListener !=null){
						onSubmitClickListener.onSubmitListener(item,true);//回调activity的方法，为其提供数据
					}
				}
				adapter.notifyDataSetChanged();//刷新
				dialog.dismiss();
			}
		});
		no.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}
	public OnSubmitClickListener onSubmitClickListener;
	
	public OnSubmitClickListener getOnSubmitClickListener() {
		return onSubmitClickListener;
	}

	public void setOnSubmitClickListener(OnSubmitClickListener onSubmitClickListener) {
		this.onSubmitClickListener = onSubmitClickListener;
	}
}
