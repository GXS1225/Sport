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
	//底部的加载视图，上拉刷新官方已经写了
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

	//分批加载时 用到
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		this.lastItem = firstVisibleItem+visibleItemCount;//最后一个
		this.totalItem = totalItemCount;//当前显示的item总数
	}
//
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		//最后一个  停止下拉  触发
		if(this.totalItem == lastItem&&scrollState == SCROLL_STATE_IDLE){
			if(!isLoading){
				isLoading=true;
				footer.setVisibility(View.VISIBLE);
				if(onLoadMore!=null){
					onLoadMore.loadMore();//回调方法，加载
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
	 * 加载完成时
	 */
	public void onLoadComplete(){
		footer.setVisibility(View.GONE);
		isLoading = false;
		
	}
	
	public interface OnLoadMore{
		public void loadMore();
	}

}