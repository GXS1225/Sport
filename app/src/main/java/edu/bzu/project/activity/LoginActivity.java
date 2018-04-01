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
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.activity_login);
		MyApplication.getInstance().addActivity(this);
		init();
		MobSDK.init(this,"1f62018a91df9","6a9962d3f732900ad79527cdefa9b41a");
		ivSinaWeibo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//点击第三方登录按钮进行授权登录
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

		//存在用户的时候 隐藏直接登录
		User user =BmobUser.getCurrentUser(LoginActivity.this, User.class);
		if(user != null){
			probation.setVisibility(View.GONE);
		}
		probation.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		//设置播放加载路径
		videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));
		//播放
		videoview.start();
		//循环播放
		videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mediaPlayer) {
				videoview.start();
			}
		});
	}


	//返回重启加载
	@Override
	protected void onRestart() {
		init();
		super.onRestart();
	}

	//防止锁屏或者切出的时候，音乐在播放
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
			//对账号密码进行判空查询
			if(TextUtils.isEmpty(username)||TextUtils.isEmpty(pass)){
				Toast.makeText(LoginActivity.this, "账号或密码未填写，请检查您的账号和密码",Toast.LENGTH_SHORT).show();
			}else{
				User user=new User();
				user.setUsername(username);
				user.setPassword(pass);
				user.login(LoginActivity.this, new SaveListener() {
					@Override
					public void onSuccess() {
						Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
						Intent i=new Intent(LoginActivity.this,MainActivity.class);
						startActivity(i);
						MyApplication.getInstance().removeActivity(LoginActivity.this);
						finish();
					}
					@Override
					public void onFailure(int i, String s) {
						Toast.makeText(LoginActivity.this, "登陆失败"+",账号或密码有误!", Toast.LENGTH_SHORT).show();
					}
				});
			}
			break;
			
		case R.id.btn_register:
			// 跳转到注册界面
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
	 * QQ 授权
	 */
	private void authPlatform_QQ() {
		//获取QQ平台手动授权
		final Platform qq = ShareSDK.getPlatform(QQ.NAME);
		//设置回调监听
		qq.setPlatformActionListener(new PlatformActionListener() {
			@Override
			public void onComplete(Platform platform, int i, final HashMap<String, Object> hashMap) {
				Log.d(TAG, " _QQ: -->> onComplete: Platform:" + platform.toString());
				Log.d(TAG, " _QQ: -->> onComplete: hashMap:" + hashMap);

				//当前线程不能执行UI操作，需要放到主线程中去
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
		//单独授权,进入输入用户名和密码界面
        /*qq.authorize();*/
		//授权并获得用户信息
		qq.showUser(null);
	}



	/**
	 * 新浪授权
	 */
	private void authPlatform_XinLang() {
		//获取具体的平台手动授权
		final Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
		//设置回调监听
		weibo.setPlatformActionListener(new PlatformActionListener() {
			@Override
			public void onComplete(Platform platform, int i, final HashMap<String, Object> hashMap) {
				Log.d(TAG, " _XinLang: -->> onComplete: Platform:" + platform.toString());
				Log.d(TAG, " _XinLang: -->> onComplete: hashMap:" + hashMap);

				//当前线程不能执行UI操作，需要放到主线程中去
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
		//authorize与showUser单独调用一个即可，
		//单独授权,进入输入用户名和密码界面
        /*weibo.authorize();*/
		//授权并获取用户信息
		weibo.showUser(null);
		//移除授权
        /*weibo.removeAccount(true);*/
	}

	/**
	 * 微信授权
	 */
	private void authPlatform_Weixin() {
		//设置平台手动授权
		final Platform weixin = ShareSDK.getPlatform(Wechat.NAME);
		//设置监听回调
		weixin.setPlatformActionListener(new PlatformActionListener() {
			@Override
			public void onComplete(Platform platform, int i, final HashMap<String, Object> hashMap) {
				Log.d(TAG, " _Weixin: -->> onComplete: Platform:" + platform.toString());
				Log.d(TAG, " _Weixin: -->> onComplete: hashMap:" + hashMap);

				//当前线程不能执行UI操作，需要放到主线程中去
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
		//单独授权,进入输入用户名和密码界面
        /*weixin.authorize();*/
		//授权并获取用户信息
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
	 * 新浪回调信息
	 *
	 * @param params 显示用户的数据
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
	 * qq回调信息
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
	 * 微信回调信息
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
