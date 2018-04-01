package edu.bzu.project.view;

import edu.bzu.project.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class LoadMoreListView extends ListView implements OnScrollListener{
	//�ײ��ļ�����ͼ������ˢ�¹ٷ��Ѿ�д��
	private View footer;
	
	private int totalItem;
	private int lastItem;
	
	private boolean isLoading;
	
	private OnLoadMore onLoadMore;
	
	private LayoutInflater inflater;
	
	public LoadMoreListView(Context context) {
		super(context);
		init(context);
	}

	public LoadMoreListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	@SuppressLint("InflateParams")
	private void init(Context context) {
		inflater = LayoutInflater.from(context);
		footer = inflater.inflate(R.layout.load_more_footer,null ,false);
		footer.setVisibility(View.GONE);
		this.addFooterView(footer);
		this.setOnScrollListener(this);
	}

	//��������ʱ �õ�
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		this.lastItem = firstVisibleItem+visibleItemCount;//���һ��
		this.totalItem = totalItemCount;//��ǰ��ʾ��item����
	}
//
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		//���һ��  ֹͣ����  ����
		if(this.totalItem == lastItem&&scrollState == SCROLL_STATE_IDLE){
			if(!isLoading){
				isLoading=true;
				footer.setVisibility(View.VISIBLE);
				if(onLoadMore!=null){
					onLoadMore.loadMore();//�ص�����������
				}
			
			}
		}
	}
	
	public OnLoadMore getOnLoadMore() {
		return onLoadMore;
	}

	public void setOnLoadMore(OnLoadMore onLoadMore) {
		this.onLoadMore = onLoadMore;
	}

	/**
	 * �������ʱ
	 */
	public void onLoadComplete(){
		footer.setVisibility(View.GONE);
		isLoading = false;
		
	}
	
	public interface OnLoadMore{
		public void loadMore();
	}

}