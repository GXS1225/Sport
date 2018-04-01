package edu.bzu.project.sql;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

/***
 * 
 * 加载外部数据库到本地，用sqLiteDatabase_sms（他是在此类中声明的sqlite对象，创建本类的对象可以调用）这个对象就可以操作数据库了

 */
public class SqlDatebase {

	/*** 上下文对象 */
	private Context context;

	/** 数据库操作对象 **/
	public SQLiteDatabase without;

	public SqlDatebase(Context context) {
		this.context = context;
		//获取数据库的底层对象
		without = fromOutCopyDate();
	
	}


	/***
	 * 从复制数据到本地
	 * 
	 * @return
	 */
	public SQLiteDatabase fromOutCopyDate() {
		String DATEBASE_PATH = "/data/data/edu.bzu.project/databases";
		String DATEBASE_FILENAME = "sports.db";

		/** 数据的绝对路径 */
		String databasePath = DATEBASE_PATH + '/' + DATEBASE_FILENAME;

		//创建文件
		File path = new File(DATEBASE_PATH);
		// 判断是否是目录（文件夹）
		if (!path.exists()) {
			path.mkdir();
		}
		// 建立一个文件
		if (!(new File(databasePath)).exists()) {
			//获得assets目录下的文件
			AssetManager as =context.getAssets();
			InputStream inputStream =null;;
			try {
				inputStream = as.open(
						"sports.db");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				//打开文件字节输出流
				FileOutputStream fileOutputStream = new FileOutputStream(
						databasePath);

				byte data[] = new byte[2048];

				int index = 0;
				//把数据库的内容读到对应的文件
				while ((index = inputStream.read(data)) != -1) {
					fileOutputStream.write(data, 0, index);
				}
				inputStream.close();
				fileOutputStream.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//打开path文件下的数据库，并返回这个数据库操作对象
		SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(
				databasePath, null);
		
		return sqLiteDatabase;
	}

	public SQLiteDatabase getSqLiteDatabase() {
		return without;
	}

	public void setSqLiteDatabase(SQLiteDatabase sqLiteDatabase) {
		this.without = sqLiteDatabase;
	}

	public void closeSQl() {
		without.close();
	}

}
