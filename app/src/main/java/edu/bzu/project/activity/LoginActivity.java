package edu.bzu.project.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.mob.MobSDK;

import java.util.HashMap;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import edu.bzu.project.R;
import edu.bzu.project.domain.bmob.User;
import edu.bzu.project.utils.MyApplication;
/**
 * @ClassName: LoginActivity
 * @Description: TODO
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	private ImageView ivQQ, ivWeChat, ivSinaWeibo;
	private String TAG = "MainActivityTag";
	private ImageView ivPortrait;
	private TextView tvName;
    private CustomVideoView videoview;
	EditText edt_user, edt_pass;
	Button btn_login;
	TextView btn_register;
	private TextView probation =null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
		setContentView(R.layout.activity_login);
		MyApplication.getInstance().addActivity(this);
		init();
		MobSDK.init(this,"1f62018a91df9","6a9962d3f732900ad79527cdefa9b41a");
		ivSinaWeibo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//�����������¼��ť������Ȩ��¼
				authPlatform_XinLang();
			}
		});

		ivQQ.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				authPlatform_QQ();
			}
		});



		ivWeChat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				authPlatform_Weixin();
			}
		});


	}
	private void init() {
		ivQQ = (ImageView) findViewById(R.id.ivQQ);
		ivWeChat = (ImageView) findViewById(R.id.ivWeChat);
		ivSinaWeibo = (ImageView) findViewById(R.id.ivSinaWeibo);
		ivPortrait = (ImageView) findViewById(R.id.iv_icon);
		tvName = (TextView) findViewById(R.id.tv_name);
		videoview = (CustomVideoView) findViewById(R.id.videoview);
		edt_user = (EditText) findViewById(R.id.edt_user);
		edt_pass = (EditText) findViewById(R.id.edt_pass);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_register = (TextView) findViewById(R.id.btn_register);
		probation =(TextView)findViewById(R.id.probation);

		//�����û���ʱ�� ����ֱ�ӵ�¼
		User user =BmobUser.getCurrentUser(LoginActivity.this, User.class);
		if(user != null){
			probation.setVisibility(View.GONE);
		}
		probation.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		//���ò��ż���·��
		videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));
		//����
		videoview.start();
		//ѭ������
		videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mediaPlayer) {
				videoview.start();
			}
		});
	}


	//������������
	@Override
	protected void onRestart() {
		init();
		super.onRestart();
	}

	//��ֹ���������г���ʱ�������ڲ���
	@Override
	protected void onStop() {
		videoview.stopPlayback();
		super.onStop();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			String username=edt_user.getText().toString();
			String pass=edt_pass.getText().toString();
			//���˺���������пղ�ѯ
			if(TextUtils.isEmpty(username)||TextUtils.isEmpty(pass)){
				Toast.makeText(LoginActivity.this, "�˺Ż�����δ��д�����������˺ź�����",Toast.LENGTH_SHORT).show();
			}else{
				User user=new User();
				user.setUsername(username);
				user.setPassword(pass);
				user.login(LoginActivity.this, new SaveListener() {
					@Override
					public void onSuccess() {
						Toast.makeText(LoginActivity.this, "��½�ɹ�", Toast.LENGTH_SHORT).show();
						Intent i=new Intent(LoginActivity.this,MainActivity.class);
						startActivity(i);
						MyApplication.getInstance().removeActivity(LoginActivity.this);
						finish();
					}
					@Override
					public void onFailure(int i, String s) {
						Toast.makeText(LoginActivity.this, "��½ʧ��"+",�˺Ż���������!", Toast.LENGTH_SHORT).show();
					}
				});
			}
			break;
			
		case R.id.btn_register:
			// ��ת��ע�����
			Intent i=new Intent(LoginActivity.this,RegisterActivity.class);
			startActivity(i);
			break;
		case R.id.probation:
			Intent ii=new Intent(LoginActivity.this,MainActivity.class);
			startActivity(ii);
			MyApplication.getInstance().removeActivity(LoginActivity.this);
			finish();
		default:
			break;
		}
	}


	/**
	 * QQ ��Ȩ
	 */
	private void authPlatform_QQ() {
		//��ȡQQƽ̨�ֶ���Ȩ
		final Platform qq = ShareSDK.getPlatform(QQ.NAME);
		//���ûص�����
		qq.setPlatformActionListener(new PlatformActionListener() {
			@Override
			public void onComplete(Platform platform, int i, final HashMap<String, Object> hashMap) {
				Log.d(TAG, " _QQ: -->> onComplete: Platform:" + platform.toString());
				Log.d(TAG, " _QQ: -->> onComplete: hashMap:" + hashMap);

				//��ǰ�̲߳���ִ��UI��������Ҫ�ŵ����߳���ȥ
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showUser_QQ(hashMap);
					}
				});
			}

			@Override
			public void onError(Platform platform, int i, Throwable throwable) {
				Log.d(TAG, " _QQ: -->> onError:  " + throwable.toString());
				throwable.printStackTrace();
			}

			@Override
			public void onCancel(Platform platform, int i) {
				qq.removeAccount(true);
			}
		});
		//������Ȩ,���������û������������
        /*qq.authorize();*/
		//��Ȩ������û���Ϣ
		qq.showUser(null);
	}



	/**
	 * ������Ȩ
	 */
	private void authPlatform_XinLang() {
		//��ȡ�����ƽ̨�ֶ���Ȩ
		final Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
		//���ûص�����
		weibo.setPlatformActionListener(new PlatformActionListener() {
			@Override
			public void onComplete(Platform platform, int i, final HashMap<String, Object> hashMap) {
				Log.d(TAG, " _XinLang: -->> onComplete: Platform:" + platform.toString());
				Log.d(TAG, " _XinLang: -->> onComplete: hashMap:" + hashMap);

				//��ǰ�̲߳���ִ��UI��������Ҫ�ŵ����߳���ȥ
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showUser_XinLang(hashMap);
					}
				});
			}

			@Override
			public void onError(Platform platform, int i, Throwable throwable) {
				Log.d(TAG, " _XinLang: -->> onError:  " + throwable.toString());
				throwable.printStackTrace();
			}

			@Override
			public void onCancel(Platform platform, int i) {
				weibo.removeAccount(true);
			}
		});
		//authorize��showUser��������һ�����ɣ�
		//������Ȩ,���������û������������
        /*weibo.authorize();*/
		//��Ȩ����ȡ�û���Ϣ
		weibo.showUser(null);
		//�Ƴ���Ȩ
        /*weibo.removeAccount(true);*/
	}

	/**
	 * ΢����Ȩ
	 */
	private void authPlatform_Weixin() {
		//����ƽ̨�ֶ���Ȩ
		final Platform weixin = ShareSDK.getPlatform(Wechat.NAME);
		//���ü����ص�
		weixin.setPlatformActionListener(new PlatformActionListener() {
			@Override
			public void onComplete(Platform platform, int i, final HashMap<String, Object> hashMap) {
				Log.d(TAG, " _Weixin: -->> onComplete: Platform:" + platform.toString());
				Log.d(TAG, " _Weixin: -->> onComplete: hashMap:" + hashMap);

				//��ǰ�̲߳���ִ��UI��������Ҫ�ŵ����߳���ȥ
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showUser_WeiXin(hashMap);
					}
				});
			}

			@Override
			public void onError(Platform platform, int i, Throwable throwable) {
				Log.d(TAG, " _Weixin: -->> onError:  " + throwable.toString());
				throwable.printStackTrace();
				weixin.removeAccount(true);
			}

			@Override
			public void onCancel(Platform platform, int i) {
				Toast.makeText(LoginActivity.this, "Hahahah", Toast.LENGTH_SHORT).show();
			}
		});
		//������Ȩ,���������û������������
        /*weixin.authorize();*/
		//��Ȩ����ȡ�û���Ϣ
		weixin.showUser(null);

		OnekeyShare onekeyShare = new OnekeyShare();
		onekeyShare.setCallback(new PlatformActionListener() {
			@Override
			public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

			}

			@Override
			public void onError(Platform platform, int i, Throwable throwable) {

			}

			@Override
			public void onCancel(Platform platform, int i) {

			}
		});

	}


	/**
	 * ���˻ص���Ϣ
	 *
	 * @param params ��ʾ�û�������
	 */
	public void showUser_XinLang(HashMap<String, Object> params) {
		String name = (String) params.get("name");
		tvName.setText(name);

		String url = (String) params.get("profile_image_url");
		Glide.with(LoginActivity.this)
				.load(url)
				.placeholder(R.drawable.ic_launcher)
				.error(R.drawable.ic_launcher)
				.into(ivPortrait);

	}

	/**
	 * qq�ص���Ϣ
	 *
	 * @param hashMap
	 */
	private void showUser_QQ(HashMap<String, Object> hashMap) {
		String name = (String) hashMap.get("nickname");
		tvName.setText(name);

		String url = (String) hashMap.get("figureurl_qq_1");
		Glide.with(LoginActivity.this)
				.load(url)
				.placeholder(R.drawable.ic_launcher)
				.error(R.drawable.ic_launcher)
				.into(ivPortrait);
	}

	/**
	 * ΢�Żص���Ϣ
	 *
	 * @param hashMap
	 */
	private void showUser_WeiXin(HashMap<String, Object> hashMap) {
		String name = (String) hashMap.get("nickname");
		tvName.setText(name);

		String url = (String) hashMap.get("headimgurl");
		Glide.with(LoginActivity.this)
				.load(url)
				.placeholder(R.drawable.ic_launcher)
				.error(R.drawable.ic_launcher)
				.into(ivPortrait);
	}
}
