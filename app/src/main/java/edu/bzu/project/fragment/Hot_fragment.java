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
 * �˶�ѡ����������fragment
 *
 */
public class Hot_fragment extends BaseFragment implements OnRefreshListener,OnLoadMore,OnJiLuClilckListener{

	private static String TAG ="Hot_fragment";
	
	//�ٷ�ˢ�µĿؼ�
	private SwipeRefreshLayout swipeRefreshLayout =null;
	private LoadMoreListView listView =null;//�Զ����list��view
	
	private SportsData_adapter adapter =null;
	
	//��������
	private SearchView searchView =null;

	//�Ի���ѡ����
	private LinearLayout layout =null;
	private AlertDialog dialog =null;
	private Button ok =null;
	private Button no =null;
	//ѡ��
	private NumberPicker n1 =null;
	private NumberPicker n2 =null;
	//���ݿ��ֶ�
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
		
		//�ٷ�ˢ�µĲ���
		swipeRefreshLayout =(SwipeRefreshLayout)rootv.findViewById(R.id.swip_lay);
		listView =(LoadMoreListView)rootv.findViewById(R.id.sports_listView);
		
		listView.setOnLoadMore(this);
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
				android.R.color.holo_green_light);
		
		searchView =(SearchView)rootv.findViewById(R.id.search_sports);
	//	searchView.setSubmitButtonEnabled(true);//��ʾ����
		//��ʼ��listview
		start_item =10;
		initData(start_item,PageSize);
		adapter =new SportsData_adapter(getActivity(),list);
		adapter.setOnJiLuClilckListener(this);//�Զ��ļ�����
		listView.setAdapter(adapter);
		listView.setTextFilterEnabled(true);//����
		
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
	//��ʼ�����ݷ������������ݿ�
	private void initData(int start_item2, int pageSize2) {
		// TODO Auto-generated method stub
		dao =new WithOut_sqlDao(getActivity());
		list =dao.getPageList(start_item2, pageSize2);	
		start_item =start_item2+pageSize2;//���¸�ֵ����ѯλ��
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//dao =new WithOut_sqlDao(getActivity());
		//��ȡ��ǰ������
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

	//�ٷ���ͷ��ˢ��
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}
	//�ײ�ˢ��
	@Override
	public void loadMore() {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//��ҳ��ѯ���ݿ⣬��Ҫ�������������
				LogUtils.v("kaishi -------", start_item+"");
				List<Sport_w> list_add =dao.getPageList(start_item, PageSize);
				if(list ==null){
					list =list_add;
				}else {
					for(Sport_w s:list_add){
						list.add(s);//������µ�����
					}
				}
				start_item =start_item+PageSize;
				//����listview
				listView.onLoadComplete();
				//��������
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
		dao.sqlite.close();//����ʱ�ر����ݿ�
	}

	//ʵ���������еĽӿ�
	@Override
	public void onJiLuClick(final int position) {
		// TODO Auto-generated method stub
		layout =(LinearLayout)View.inflate(getActivity(), R.layout.dialog_weight, null);
		 n1 =(NumberPicker)layout.findViewById(R.id.numberpicker1);
		 n2 =(NumberPicker)layout.findViewById(R.id.numberpicker2);
		//��ȡ��ť
		ok =(Button)layout.findViewById(R.id.dialog_weight_ok);
		no =(Button)layout.findViewById(R.id.dialog_weight_no);
	
		//�Զ������ֵ
		String value1[] =new String[24];
		String value2[] =new String[60];
		for(int i =0;i<24;i++){
			value1[i]=i+"Сʱ";
		}
		for(int i=0;i<60;i++){
			value2[i] =i+"����";
		}
		n1.setMinValue(0);
		n1.setMaxValue(value1.length-1);
		n1.setDisplayedValues(value1);
		n2.setMinValue(0);
		n2.setMaxValue(value2.length-1);
		n2.setDisplayedValues(value2);

		//�����Ի���
		dialog =new AlertDialog.Builder(getActivity()).setView(layout).show();
		//�Ի���İ�ť������
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int position1 =n1.getValue();
				int position2 =n2.getValue();
				//������ʱ��
				String position1_str =n1.getDisplayedValues()[position1];
				position1 =Integer.parseInt(position1_str.substring(0, position1_str.lastIndexOf("Сʱ")));
				String position2_str =n2.getDisplayedValues()[position2];
				position2 =Integer.parseInt(position2_str.substring(0, position2_str.lastIndexOf("����")));
				int time_sum =position1*60+position2;
				//��ȡǧ��
				double qianka =list.get(position).getHeat();
				double qianka_sum;
					//����ʱ�� Ϊ0ʱ����Ҫ�ı��б����ͼ��
				if(time_sum == 0){
					//�������ݣ�����Ϊ��¼ͼ��
					//�ж��Ƿ�ѡ�����ѡ��������֮
					if(list.get(position).getItem()!=null){
						Sport_item item =list.get(position).getItem();
						list.get(position).setItem(null);//���ÿ�
						//�ı�activity�Ĳ���
						if(onSubmitClickListener !=null){
							onSubmitClickListener.onSubmitListener(item,false);//�ص�activity�ķ�����null���һ
						}
					}
				}else {
					qianka_sum =(double)time_sum/60*qianka;//��ǧ��
					DecimalFormat   fnum  =   new  DecimalFormat("0.00");
					String qianka_str =fnum.format(qianka_sum);
					qianka_sum =Double.parseDouble(qianka_str);
					//�������ݣ����õ������ֵ
					Sport_item item =new Sport_item();
					item.setPosition(position);
					item.setQianka_value(qianka);
					item.setName(list.get(position).getMode());
					item.setQianka_value(qianka);
					item.setHour(position1);
					item.setMinute(position2);
					item.setQianka(qianka_sum);
					item.setType(1);
					list.get(position).setItem(item);//�б��� չʾ
					//�ı�activity�Ĳ���
					if(onSubmitClickListener !=null){
						onSubmitClickListener.onSubmitListener(item,true);//�ص�activity�ķ�����Ϊ���ṩ����
					}
				}
				adapter.notifyDataSetChanged();//ˢ��
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
