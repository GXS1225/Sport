package edu.bzu.project.activity.BBS;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.bmob.v3.datatype.BmobFile;
import edu.bzu.project.R;
import edu.bzu.project.domain.bmob.health_domain;
import edu.bzu.project.utils.CommonUtils;
import edu.bzu.project.utils.MyApplication;
import android.R.dimen;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 运动小常识的详情界面
 */
public class Health_InfoActivity extends Activity {
	
	
	private Button healthinfo_back =null;
	private TextView user_content =null;
	private ImageView photo1 =null;
	private health_domain domain =null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_helthinfo);
		initView();
		initDate();
		healthinfo_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyApplication.getInstance().removeActivity(Health_InfoActivity.this);
				finish();
			}
		});
	}
	private void initDate() {
		// TODO Auto-generated method stub
		domain =(health_domain) getIntent().getSerializableExtra("healthinfo");
		if(domain!=null){
			photo1.setVisibility(View.VISIBLE);
			user_content.setText(domain.getContent());
			BmobFile file =domain.getPhoto();
			if(file!=null){
				String uri =file.getFileUrl(Health_InfoActivity.this);
				ImageLoader.getInstance().displayImage(uri, photo1, MyApplication.getInstance()
						.getOptions(R.drawable.bg_pic_loading));
			}
		}
	}
	private void initView() {
		// TODO Auto-generated method stub
		healthinfo_back =(Button)findViewById(R.id.healthinfo_back);
		user_content =(TextView)findViewById(R.id.healthcontent_text);
		photo1 =(ImageView)findViewById(R.id.health_photo);
	}
}
