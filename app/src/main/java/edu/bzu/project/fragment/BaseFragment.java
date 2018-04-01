package edu.bzu.project.fragment;


import edu.bzu.project.utils.LogUtils;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment基类
 *
 * 
 */
public abstract class BaseFragment extends Fragment {

	//对于Fragment布局的fragment
	/*
	 * 可以使用事物的add hide来切换fragment。能保存状态
	 * 可以使用替换，但是不保存状态，加上栈可以保存
	 * */
	//对于viewpage的fragment
	/*加载1页面 就创建了2页面 打开3页面 1页面被销毁
	 * 使用fragment的适配器，可以进行滑动切换，但是也不保存状态()
	 * 使用viewpage的setcurrentitem指定的fragment，也不保存状态
	 * 使用pager.setOffscreenPageLimit(2);多缓存一个页面。
	 * 还有就是去修改适配器等让他在变化的时候，先隐藏所有，在展示。
	 * 
	 * */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		LogUtils.i(getFragmentName(), " onAttach()");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtils.i(getFragmentName(), " onCreate()");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtils.i(getFragmentName(), " onCreateView()");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		LogUtils.i(getFragmentName(), " onViewCreated()");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogUtils.i(getFragmentName(), " onActivityCreated()");
	}

	@Override
	public void onStart() {
		super.onStart();
		LogUtils.i(getFragmentName(), " onStart()");
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtils.i(getFragmentName(), " onResume()");
	}

	@Override
	public void onPause() {
		super.onPause();
		LogUtils.i(getFragmentName(), " onPause()");
	}

	@Override
	public void onStop() {
		super.onStop();
		LogUtils.i(getFragmentName(), " onStop()");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		LogUtils.i(getFragmentName(), " onDestroyView()");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtils.i(getFragmentName(), " onDestroy()");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		LogUtils.i(getFragmentName(), " onDetach()");
	}

	/**
	 * fragment name
	 */
	public abstract String getFragmentName();

}
