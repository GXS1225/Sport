package edu.bzu.project.receiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.R.bool;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import edu.bzu.project.domain.pk_class;
import edu.bzu.project.domain.bmob.Map_domain;
import edu.bzu.project.domain.bmob.Pedometer_Server;
import edu.bzu.project.domain.bmob.Pk_domain;
import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.utils.DateUtils;
import edu.bzu.project.utils.LogUtils;
/**
 * 运动时一些规则或计算
 * 代码复用重构
 * 		还有几个没有进行代码的复用
 *
 */
public class Pk_Utils {
	public  double sumqianka =0;
	private Message message =null;
	
	private boolean flag =false;
	private int sumScore;
	private int successScore;
	/**pk的规则 及 结果*/
	public void getResult( final User user,final User user_,final Handler handler,final int position,final List<pk_class> is_pks,final Context context){
		message =Message.obtain();
		Random random =new Random();
		int suiji =random.nextInt(10);
		if(suiji<7){//地图的比较
			int suiji1 =random.nextInt(10);
			BmobQuery<Map_domain> info =new BmobQuery<Map_domain>();
			final BmobQuery<Map_domain> info1 =new BmobQuery<Map_domain>();
			if(suiji1<7){//比较 总距离
				//查找自己的数据
				info.addWhereEqualTo("user", user);
				info.addWhereEqualTo("user", user_);
				//查找pk人的数据
			}else{//比较 近一个周的距离
				Date qi =DateUtils.addDay(Calendar.getInstance().getTime(), -7);
				BmobDate date =new BmobDate(qi);
				info.addWhereEqualTo("user",user);
				info.addWhereGreaterThan("createAt", date);
				info1.addWhereEqualTo("user", user_);
				info1.addWhereGreaterThan("createAt", date);
			}
			info.findObjects(context, new FindListener<Map_domain>() {
				
				@Override
				public void onSuccess(List<Map_domain> maps) {
					// TODO Auto-generated method stub
					//
					if(maps.size()!=0){
						for(Map_domain domain:maps){
							sumqianka+=domain.getQianka();
						}
					}
					info1.findObjects(context, new FindListener<Map_domain>() {
						
						@Override
						public void onSuccess(List<Map_domain> maps1) {
							// TODO Auto-generated method stub
							//比较
							double sum1 =0;
							if(maps1.size()!=0){
								for(Map_domain domain:maps1){
									sum1+=domain.getQianka();
								}
							}
							if(sumqianka>=sum1){
								message.what= 1;//胜利
							}else{
								message.what =0;//失败
							}
							message.obj =user_;
							if(is_pks!=null){
								is_pks.get(position).setIs_pk(false);
							}else{
								message.arg1 =position;
							}
							handler.sendMessage(message);
						}
						
						@Override
						public void onError(int arg0, String arg1) {
							// TODO Auto-generated method stub
							
						}
					});
				}
				
				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}
			});

		}else{//计步的比较
			BmobQuery<Pedometer_Server> servers =new BmobQuery<Pedometer_Server>();
			final BmobQuery<Pedometer_Server> servers1 =new BmobQuery<Pedometer_Server>();
			servers.addWhereEqualTo("user", user);
			servers1.addWhereEqualTo("user", user_);
			servers.findObjects(context, new FindListener<Pedometer_Server>() {
				
				@Override
				public void onSuccess(List<Pedometer_Server> servers) {
					// TODO Auto-generated method stub
					for(Pedometer_Server server:servers){
						sumqianka +=server.getQianka();
					}
					servers1.findObjects(context, new FindListener<Pedometer_Server>() {
						
						@Override
						public void onSuccess(List<Pedometer_Server> servers1) {
							// TODO Auto-generated method stub
							double sum =0;
							for(Pedometer_Server server:servers1){
								sum +=server.getQianka();
							}
							if(sumqianka>=sum){
								message.what =1;
								//胜利
							}else{
								message.what =0;//失败
							}
							message.obj =user_;
							
							if(is_pks !=null){
								is_pks.get(position).setIs_pk(false);
							}else{
								message.arg1 =position;
							}
							handler.sendMessage(message);
						}
						
						@Override
						public void onError(int arg0, String arg1) {
							// TODO Auto-generated method stub
							
						}
					});
				}
				
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}
	
	/**查看总战绩*/
	public void getSumScore(final User user,final Context context,final TextView textView){
		//查找战绩
		BmobQuery<Pk_domain> dQuery =new BmobQuery<Pk_domain>();
		dQuery.addWhereEqualTo("user", user);
		dQuery.count(context, Pk_domain.class, new CountListener() {
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				LogUtils.i("失败", "");
			}
			
			@Override
			public void onSuccess(int count) {
				// TODO Auto-generated method stub
				sumScore =count;
				BmobQuery<Pk_domain> eq1 =new BmobQuery<Pk_domain>();
				eq1.addWhereEqualTo("user", user);
				BmobQuery<Pk_domain> eq2 =new BmobQuery<Pk_domain>();
				eq2.addWhereEqualTo("result", 1);//胜利的
				//组合条件
				List<BmobQuery<Pk_domain>> queries =new ArrayList<BmobQuery<Pk_domain>>();
				queries.add(eq1);
				queries.add(eq2);
				//查找
				BmobQuery<Pk_domain> query =new BmobQuery<Pk_domain>();
				query.and(queries);
				query.count(context, Pk_domain.class, new CountListener() {
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						LogUtils.i("失败", "");
					}
					@Override
					public void onSuccess(int count) {
						// TODO Auto-generated method stub
						String text =null;
						if(count == 0){
							text ="你还没有战绩哦！";
						}else{
							successScore =count;
							text ="你的战绩是"+successScore+"/"+sumScore;
						}
						textView.setText(text);	
					}
				});
				
			}
		});
	}
}
