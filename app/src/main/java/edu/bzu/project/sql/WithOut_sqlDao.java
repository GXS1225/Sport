package edu.bzu.project.sql;

import java.util.ArrayList;
import java.util.List;

import edu.bzu.project.domain.Sport_w;
import edu.bzu.project.utils.LogUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * 数据库 操作类
 *
 */
public class WithOut_sqlDao {

	private SqlDatebase sqlDatebase =null;//外部的数据库
	
	public SQLiteDatabase sqlite =null;

	public WithOut_sqlDao(Context context) {
		super();
		sqlDatebase =new SqlDatebase(context);
		sqlite =sqlDatebase.without;//获取数据库操作对象
	}
	//数据库操作
	
	//分页查询挖外部数据库
	public List<Sport_w> getPageList(int start,int PageSize){
		List<Sport_w> list =null;
	//从 offset行 取 limit 个
//	 String sql= "select * from sportsHeat "+    
//        " Limit "+String.valueOf(PageSize)+ " Offset " +String.valueOf(pageID*PageSize);
	String sql ="select * from SPORTS limit "+String.valueOf(start)+","+String.valueOf(PageSize);
	LogUtils.v("SPORTS分页查询数据sql-------", sql);
	Cursor cursor =null;
	 try {
		 cursor =sqlite.rawQuery(sql, null);
		 list =new ArrayList<Sport_w>();
		 while(cursor.moveToNext()){
			 Sport_w sport_w =new Sport_w();
			 sport_w.setId(cursor.getInt(0));
			 sport_w.setMode(cursor.getString(1));
			 sport_w.setHeat(cursor.getDouble(2));
			 sport_w.setItem(null);
			 list.add(sport_w);
		 }
		
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}finally{
		if(cursor!=null)
		cursor.close();
	}
	 LogUtils.v("SPORTS的一页数据个数：---", list.size()+"");
		return list;
	}
	
	
	
}
