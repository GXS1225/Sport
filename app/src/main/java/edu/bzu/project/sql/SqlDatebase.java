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
 * �����ⲿ���ݿ⵽���أ���sqLiteDatabase_sms�������ڴ�����������sqlite���󣬴�������Ķ�����Ե��ã��������Ϳ��Բ������ݿ���

 */
public class SqlDatebase {

	/*** �����Ķ��� */
	private Context context;

	/** ���ݿ�������� **/
	public SQLiteDatabase without;

	public SqlDatebase(Context context) {
		this.context = context;
		//��ȡ���ݿ�ĵײ����
		without = fromOutCopyDate();
	
	}


	/***
	 * �Ӹ������ݵ�����
	 * 
	 * @return
	 */
	public SQLiteDatabase fromOutCopyDate() {
		String DATEBASE_PATH = "/data/data/edu.bzu.project/databases";
		String DATEBASE_FILENAME = "sports.db";

		/** ���ݵľ���·�� */
		String databasePath = DATEBASE_PATH + '/' + DATEBASE_FILENAME;

		//�����ļ�
		File path = new File(DATEBASE_PATH);
		// �ж��Ƿ���Ŀ¼���ļ��У�
		if (!path.exists()) {
			path.mkdir();
		}
		// ����һ���ļ�
		if (!(new File(databasePath)).exists()) {
			//���assetsĿ¼�µ��ļ�
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
				//���ļ��ֽ������
				FileOutputStream fileOutputStream = new FileOutputStream(
						databasePath);

				byte data[] = new byte[2048];

				int index = 0;
				//�����ݿ�����ݶ�����Ӧ���ļ�
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
		//��path�ļ��µ����ݿ⣬������������ݿ��������
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
