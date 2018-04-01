package edu.bzu.project.activity.setting;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.litepal.crud.DataSupport;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import edu.bzu.project.R;
import edu.bzu.project.activity.LoginActivity;
import edu.bzu.project.activity.MainActivity;
import edu.bzu.project.domain.Userinfo;
import edu.bzu.project.utils.ConstantValues;
import edu.bzu.project.utils.MyApplication;
import edu.bzu.project.utils.CommonUtils;
import edu.bzu.project.utils.SPUtils;
import edu.bzu.project.view.NumberSeekBar;

public class SetAim extends Activity implements OnClickListener{
	private Button btn_next;
    private NumberSeekBar pb2;
    private int max =7000;
	private Userinfo info =null;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    pb2.setProgress(pb2.getProgress() + 10);
                    break;
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
        setContentView(R.layout.setalm_activity);
        pb2 = (NumberSeekBar)findViewById(R.id.bar2);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);
        info =(Userinfo)getIntent().getSerializableExtra("info");
        init();
        start();
    }
    
    
    private void init() {    
        pb2.setTextSize(20);// ���������С
        pb2.setTextColor(Color.WHITE);// ��ɫ
        pb2.setMyPadding(10, 10, 10, 10);// ����padding ����setpadding����Ч
        pb2.setImagePadding(0, 1);// ���Բ�����
        pb2.setTextPadding(0, 0);// ���Բ�����
    }
    
    private void start() {
        TimerTask tt = new TimerTask() {
            public void run() {
                handler.sendEmptyMessage(1);
            }
        };
        Timer timer = new Timer();
        timer.schedule(tt, 1000, 2000);
    }
    
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    	CommonUtils.ExitApp(this);
	        return true;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next:
			SPUtils.put(SetAim.this, "max", pb2.getBushuMax());
			info.setMax( pb2.getBushuMax());
			
			Userinfo old =DataSupport.find(Userinfo.class, 1);
			if(old == null){
				SPUtils.put(SetAim.this, ConstantValues.isFirstIn, false);
				double ibm =info.getWeight()/Math.pow((double)info.getHeight()/100, 2);
				info.setIbm(ibm);
				info.saveThrows();//����
				Intent intent = new Intent(SetAim.this ,LoginActivity.class);
				startActivity(intent);
			}else{
				//�޸�
				info.update(1);
				Toast.makeText(SetAim.this, "�޸ĳɹ�", Toast.LENGTH_SHORT).show();
			}
			finish();
			break;
	}
	}
}
