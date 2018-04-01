package edu.bzu.project.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import edu.bzu.project.R;
import edu.bzu.project.activity.setting.AddExercise_Acivity;
import edu.bzu.project.adapter.SportsData_adapter;

import edu.bzu.project.domain.Sport_item;
import edu.bzu.project.domain.Sport_w;
import edu.bzu.project.other.OnJiLuClilckListener;
import edu.bzu.project.other.OnSubmitClickListener;
import edu.bzu.project.sql.WithOut_sqlDao;
import edu.bzu.project.utils.MyApplication;
import edu.bzu.project.utils.LogUtils;
import edu.bzu.project.view.LoadMoreListView;
import edu.bzu.project.view.LoadMoreListView.OnLoadMore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
/**
 * 运动选择界面的热门fragment
 *
 */
public class Hot_fragment extends BaseFragment implements OnRefreshListener,OnLoadMore,OnJiLuClilckListener{

	private static String TAG ="Hot_fragment";
	
	//官方刷新的控件
	private SwipeRefreshLayout swipeRefreshLayout =null;
	private LoadMoreListView listView =null;//自定义得list的view
	
	private SportsData_adapter adapter =null;
	
	//快速搜索
	private SearchView searchView =null;

	//对话框选择器
	private LinearLayout layout =null;
	private AlertDialog dialog =null;
	private Button ok =null;
	private Button no =null;
	//选择
	private NumberPicker n1 =null;
	private NumberPicker n2 =null;
	//数据库字段
	private int start_item;
	private int PageSize=10;
	private List<Sport_w> list =null;
	private WithOut_sqlDao dao =null;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LogUtils.i(TAG, "onCreateView()");
		View rootv =inflater.inflate(R.layout.page_sports_hot, container, false);
		
		//官方刷新的布局
		swipeRefreshLayout =(SwipeRefreshLayout)rootv.findViewById(R.id.swip_lay);
		listView =(LoadMoreListView)rootv.findViewById(R.id.sports_listView);
		
		listView.setOnLoadMore(this);
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
				android.R.color.holo_green_light);
		
		searchView =(SearchView)rootv.findViewById(R.id.search_sports);
	//	searchView.setSubmitButtonEnabled(true);//显示搜索
		//初始化listview
		start_item =10;
		initData(start_item,PageSize);
		adapter =new SportsData_adapter(getActivity(),list);
		adapter.setOnJiLuClilckListener(this);//自定的监听器
		listView.setAdapter(adapter);
		listView.setTextFilterEnabled(true);//过滤
		
		initEvent();
		return rootv;
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				return false;
			}	
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(newText)){
					listView.clearTextFilter();
				}else {
					listView.setFilterText(newText);
				}
				return true;
			}
		});
		
	
	}
	//初始化数据方法，调用数据库
	private void initData(int start_item2, int pageSize2) {
		// TODO Auto-generated method stub
		dao =new WithOut_sqlDao(getActivity());
		list =dao.getPageList(start_item2, pageSize2);	
		start_item =start_item2+pageSize2;//重新赋值起点查询位置
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//dao =new WithOut_sqlDao(getActivity());
		//获取以前的数据
		if(getArguments()!=null){
			ArrayList<Sport_item> items =null;
			items =(ArrayList<Sport_item>)getArguments().getSerializable("sportsyet");
			for(Sport_item item:items){
				list.get(item.getPosition()).setItem(item);
			}
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	//官方的头部刷新
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}
	//底部刷新
	@Override
	public void loadMore() {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//分页查询数据库，需要保存上面的数据
				LogUtils.v("kaishi -------", start_item+"");
				List<Sport_w> list_add =dao.getPageList(start_item, PageSize);
				if(list ==null){
					list =list_add;
				}else {
					for(Sport_w s:list_add){
						list.add(s);//添加上新的数据
					}
				}
				start_item =start_item+PageSize;
				//更新listview
				listView.onLoadComplete();
				//更新数据
				adapter.notifyDataSetChanged();
			}
		}, 1000);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		dao.sqlite.close();//销毁时关闭数据库
	}

	//实现适配器中的接口
	@Override
	public void onJiLuClick(final int position) {
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
				double qianka =list.get(position).getHeat();
				double qianka_sum;
					//设置时间 为0时，需要改变列表项的图标
				if(time_sum == 0){
					//更改数据，设置为记录图标
					//判断是否选择过，选择过则清空之
					if(list.get(position).getItem()!=null){
						Sport_item item =list.get(position).getItem();
						list.get(position).setItem(null);//设置空
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
					item.setName(list.get(position).getMode());
					item.setQianka_value(qianka);
					item.setHour(position1);
					item.setMinute(position2);
					item.setQianka(qianka_sum);
					item.setType(1);
					list.get(position).setItem(item);//列表项 展示
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
	OnClickListener clickListener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int position1,position2;
			switch (v.getId()) {
			case R.id.dialog_weight_ok:
				
			case R.id.dialog_weight_no:
				break;
			default:
				break;
			}
		}
	};
	
	public OnSubmitClickListener onSubmitClickListener;
	
	public OnSubmitClickListener getOnSubmitClickListener() {
		return onSubmitClickListener;
	}

	public void setOnSubmitClickListener(OnSubmitClickListener onSubmitClickListener) {
		this.onSubmitClickListener = onSubmitClickListener;
	}
	
}
