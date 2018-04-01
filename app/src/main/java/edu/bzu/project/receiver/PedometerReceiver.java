package edu.bzu.project.receiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.litepal.crud.DataSupport;

import edu.bzu.project.domain.Pedometer;
import edu.bzu.project.domain.Userinfo;
import edu.bzu.project.utils.CommonUtils;
import edu.bzu.project.utils.ConstantValues;
import edu.bzu.project.utils.DateStyle;
import edu.bzu.project.utils.DateUtils;
import edu.bzu.project.utils.SPUtils;
import edu.bzu.project.utils.SportType;
import edu.bzu.project.utils.SportUtils;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
/**
 * 广播 保存数据
 *
 *
 */
public class PedometerReceiver extends BroadcastReceiver {

	//保存记步的数据操作，一天，
	//从shard保存到数据库
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//更新数据
		int bushu =(Integer) SPUtils.get(context, ConstantValues.BU_SHU, 0);
		float shijian =(Float)SPUtils.get(context, ConstantValues.HUO_YUE, 0f);
		String linchen =(String)SPUtils.get(context, ConstantValues.LIN_CHEN, "");
	
		Pedometer p =new Pedometer();
		p.setBushul(bushu);
		p.setMeter(SportUtils.getDistanceByPace(bushu));
		p.setQianka(SportUtils.getCalorie(shijian/60, SportType.BU_XING));
		p.setShijian(SportUtils.getMinuteBySecond(shijian));
		
		p.setRiqi(DateUtils.DateToString(Calendar.getInstance()
				.getTime(), DateStyle.YYYY_MM_DD_CN));
		if(DataSupport.find(Userinfo.class, 1) == null){
			p.setMax(7000);
		}else{
			p.setMax((int)DataSupport.find(Userinfo.class, 1).getMax());
		}
		p.setTime(DateUtils.StringToDate(DateUtils.DateToString(Calendar.getInstance().getTime(), DateStyle.YYYY_MM_DD_CN)));
		p.save();
		//清空数据
		SPUtils.put(context, ConstantValues.BU_SHU, 0);
		SPUtils.put(context, ConstantValues.HUO_YUE, 0f);	
	}

}
