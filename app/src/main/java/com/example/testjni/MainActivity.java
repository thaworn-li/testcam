package com.example.testjni;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


import com.example.testjni.playview.VideoSurfaceView;
import com.example.testjni.util.AudioThread;
import com.example.testjni.util.Global;
import com.example.testjni.util.TalkThread;
import com.fos.sdk.FosDiscovery_Node;
import com.fos.sdk.FosSdkJNI;


import com.fos.sdk.*;
import com.mydemo.snapdemo.R;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("ShowToast")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class MainActivity extends Activity implements View.OnClickListener,
		View.OnTouchListener {
	public static  short PORT = 88;
	public static  String IP_OR_DNS = "172.16.0.41";
	public static  String UID = "UID";
	public static  String LOGNAME = "admin";
	public static  String LOGPD = "";
	
	public static  String SSID = "";
	public static  String PSK = "";
	
	public static  int STREAM_TYPE = 0;
	public static  int CONNET_TYPE = 1;
	public static  int con_type = 1; 
	public static  int vvport = 1; 
	public byte[]  snapData = new byte[512*1024];
	public static  Integer dataLen = 512*1024;

	public static final int OPNE_VIDEO_SUC = 10;
	public static final int OPNE_VIDEO_FAILED = 11;
	
	public static final int LOGIN_FAILED = 15;
	
	private VideoSurfaceView mVideoSurfaceView;

	public boolean isRun = true;
	public int OPEN_VIDEO_STATE = -1;
	public int LOGIN_STATE = -3;

	private Integer mPermissionFlag = new Integer(-1);
	private Integer mGetDataLength = new Integer(-1);

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case OPNE_VIDEO_FAILED:
					Toast.makeText(getApplicationContext(), 
							"打开视频失败！", 500).show();
			case LOGIN_FAILED:
				Toast.makeText(getApplicationContext(), 
						"login失败,错误码："+ msg.arg1, 500).show();	
			default:
				break;
			}
		};
	};
	
	public static WifiInfo getCurrConnWifi(Context context) { 
	      WifiInfo currWifi = null; 
	      try { 
	         WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); 
	         currWifi = wifiManager.getConnectionInfo(); 
	      } catch (Exception ex) { 
	         currWifi = null; 
	      } 
	      return currWifi; 
	   }

	/*** 显示正在连接界面 **/
	/*
	private void showConnectingView() {
		if (mVideoSurfaceView.hasReceivedData == false) {
			if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
				mVideoSurfaceView.setBackground(null);
			} else {
				mVideoSurfaceView.setBackgroundDrawable(null);
			}
		}
	}
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		   /*获取Intent中的Bundle对象*/  
        Bundle bundle = this.getIntent().getExtras();  
        /*获取Bundle中的数据，注意类型和key*/  
 //       IP_OR_DNS = bundle.getString("ip");
        UID = bundle.getString("uid");
        SSID = bundle.getString("ssid");
        PSK = bundle.getString("psk");
        PORT = bundle.getShort("port");
        con_type = bundle.getInt("type");
        Log.i("jerry", IP_OR_DNS+ " " +UID+ " "+LOGNAME+" "+LOGPD+"  "+PORT);
		mVideoSurfaceView = (VideoSurfaceView) findViewById(R.id.live_surface_view);
	//	showConnectingView();
		initView();
		new Thread(new Runnable() {
			@Override
			public void run() {
				if(con_type > 1)
				{
					con_type = 1;
				}	
				FosDiscovery_Node getNode = new FosDiscovery_Node();
				WifiInfo info = getCurrConnWifi(MainActivity.this); 
		        int ip = info.getIpAddress();
				//int ret = FosSdkJNI.StartEZlink(UID, SSID, PSK, getNode, 12000);  //2min
				int ret = FosSdkJNI.StartEZlink2(UID, SSID, PSK, ip, getNode, 12000, 0);  //2min
				
				//(String uid, String ssid, String password, int phoneIP, FosDiscovery_Node getNode ,int timeout, int Ezlinktype);
				if(ret != 0)                  
				{
					Log.i("jerry", "EZ link failed");
				}
				FosSdkJNI.StopEZlink();	
				//String mm = getNode.ip;
				Global.mHandlerNo = FosSdkJNI.Create( getNode.ip, UID,
						"admin", "", PORT, PORT, IPCType.FOSIPC_H264, ConnectType.FOSCNTYPE_IP);
				if (Global.mHandlerNo > 0) {
					
					LOGIN_STATE = FosSdkJNI.Login(Global.mHandlerNo,
						mPermissionFlag, 3000);
					Log.i("jerry", "Login State == "+ LOGIN_STATE +"privileg == " + mPermissionFlag);
					
					switch (LOGIN_STATE) {
					case 0:
						StrData dname = new StrData();
						FosSdkJNI.GetDevName(Global.mHandlerNo, 2000, dname);
						Log.i("jerry", "dname = "+ dname.str);
						
						Global.OPEN_VIDEO_STATE = FosSdkJNI.OpenVideo(
								Global.mHandlerNo, StreamType.FOSSTREAM_SUB, 1000);
						if(Global.OPEN_VIDEO_STATE == 0)
						{
							Global.isReceiveData = true;
							mVideoSurfaceView.startDraw(); 
						}
						else{
							Message msg = mHandler.obtainMessage(OPNE_VIDEO_FAILED);
	                        mHandler.sendMessage(msg); 
						} 
						Log.i("jerry", "OPEN_VIDEO_STATE------->"
								+ Global.OPEN_VIDEO_STATE);
						break;
					default:
						Message msg = mHandler.obtainMessage(LOGIN_FAILED);
						msg.arg1 = LOGIN_STATE;
                        mHandler.sendMessage(msg); 
						break;
					}
				} else {
					Log.i("sdk","create failed!");
				}
		}}).start();
	//	 获取事件的线程
		
		/*
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true)
				{
					if(LOGIN_STATE == 0){
						FosEvet_Data  envData= new FosEvet_Data();
						FosSdkJNI.GetEvent(Global.mHandlerNo, envData);
						String evt = new String("");
						if(envData.data != null)
						{
						try {
							evt=new String(envData.data ,"UTF-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						Log.i("jerry", "evt--:"+ evt);
						}
					}
				}
			}
		}).start();
		*/
	}
	
	private Button video;
	private Button audio;
	private Button talk;
	private Button recode;
	private Button capture;
	private ImageView jpgView;
	private Button bt_pre;
	private Button bt_next;
	private TalkThread mTalkThread;
	public AudioThread mAudioThread;

	private void initView() {

		video = (Button) findViewById(R.id.video_btn);
		audio = (Button) findViewById(R.id.audio_btn);
		talk = (Button) findViewById(R.id.talk_btn);
		recode = (Button) findViewById(R.id.record_btn);
		capture = (Button) findViewById(R.id.capture_pic_btn);
		bt_pre = (Button) findViewById(R.id.bt_pre);
		bt_next = (Button) findViewById(R.id.bt_next);
		jpgView = (ImageView)findViewById(R.id.jpgview);
		
		video.setOnClickListener(this);
		audio.setOnClickListener(this);
		talk.setOnClickListener(this);
		recode.setOnClickListener(this);
		capture.setOnClickListener(this);
		bt_pre.setOnClickListener(this);
		bt_next.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		Log.i("jerry", "onDestroy()");
		isRun = false;
		if(mAudioThread != null)
		{
			mAudioThread.StopRun();
		}
		if(mTalkThread != null)
		{
			mTalkThread.StopTalk();
		}
		mVideoSurfaceView.stopDraw();
		FosSdkJNI.Logout(Global.mHandlerNo, 1000);
		FosSdkJNI.Release(Global.mHandlerNo);
		FosSdkJNI.DeInit();
		super.onDestroy();
		FosSdkJNI.Init();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Global.isIPCconect = false;
		MainActivity.this.finish();
		mVideoSurfaceView.stopDraw();
		Log.i("jerry", "onBackPressed");
		if(mAudioThread != null)
		{
			mAudioThread.StopRun();
		}
		if(mTalkThread != null)
		{
			mTalkThread.StopTalk();
		}
		FosSdkJNI.Logout(Global.mHandlerNo, 1000);
		FosSdkJNI.Release(Global.mHandlerNo);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		/*
		switch (v.getId()) {
		case R.id.bt_pre:
			if(Global.isReceiveData){
				new Thread(new Runnable() {
					@Override
					public void run() {
						FosSdkJNI.PtzCmd(Global.mHandlerNo, 0, 1000);
					}
				}).start();
			}
			break;
		case R.id.bt_next:
			if(Global.isReceiveData){
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						FosSdkJNI.PtzCmd(Global.mHandlerNo, 6, 1000);
					}
				}).start();
			}
			break;
		default:
			break;
		}
		*/
		return false;
	}

	private boolean isTalkOpen = false;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.video_btn:
			if(Global.OPEN_VIDEO_STATE == 0){
				new Thread(new Runnable() {
					@Override
					public void run() {
						Global.OPEN_VIDEO_STATE = FosSdkJNI.CloseVideo(Global.mHandlerNo, 1000);
						Global.OPEN_VIDEO_STATE = -1;
					}
				}).start();	
				video.setText("开启视频");
			}else{
				new Thread(new Runnable() {
					@Override
					public void run() {
						Global.OPEN_VIDEO_STATE = FosSdkJNI.OpenVideo(Global.mHandlerNo,StreamType.FOSSTREAM_MAIN, 1000);
					}
				}).start();	
				video.setText("关闭视频");
			}
			break;
		case R.id.audio_btn:
			if(Global.isReceiveData){
				if(!Global.isAudioOpenOrNot){
						audio.setText("停止音频");
						int openRet = FosSdkJNI.OpenAudio
						(Global.mHandlerNo, StreamType.FOSSTREAM_MAIN, 2000);   //0 main 1 sub
						if(openRet == 0)
						{
							Log.i("jerry", "open audio  success");
							Toast.makeText(getApplicationContext(), 
									"已开启音频", 500).show();
							if (mAudioThread == null) {
								mAudioThread = new AudioThread();	
								mAudioThread.init();
								mAudioThread.start();
								Global.isAudioOpenOrNot = true;
								Log.i("jerry", "mAudioThread.start()");
								}
							
		
						}
				}else{
						audio.setText("开始音频");
						int audioRet = FosSdkJNI.CloseAudio(Global.mHandlerNo, 2000);
						if(audioRet == 0)
						{
							Toast.makeText(getApplicationContext(), 
									"已关闭音频", 500).show();
								
							if (mAudioThread != null) {
								mAudioThread.StopRun();
								mAudioThread = null;
								
								Log.i("jerry", "mAudioThread.StopRun()");
								}
							Global.isAudioOpenOrNot = false;
							
						}
				}
			}

			break;
		case R.id.talk_btn:
			if(Global.isReceiveData){
				if(!isTalkOpen){
					int show = FosSdkJNI.OpenTalk(Global.mHandlerNo, 2000);
						if (mTalkThread == null && show == 0) {
							mTalkThread = new TalkThread();
							mTalkThread.isRunTalk = true;
							mTalkThread.init();
							mTalkThread.start();
							Log.i("jerry", "mTalkThread.start()");
							}
						mTalkThread.startTalk();
						talk.setText("停止对讲");
					// 如果这一句在talkThrea创建前调用，会把程序的按键声传递过去
						isTalkOpen = true;
					}else{
						int ret = FosSdkJNI.CloseTalk(Global.mHandlerNo, 2000);

						if(mTalkThread != null && ret == 0){
							
							mTalkThread.isRunTalk = false;
							mTalkThread.StopTalk();
							Log.i("jerry", "mTalkThread.StopTalk()");
							mTalkThread = null;
							isTalkOpen = false;
							talk.setText("开始对讲");
						}
					}
			}
			break;
		case R.id.record_btn:  //录像模块
			if(Global.isReceiveData){
				if(!Global.isRecordOrNot){
					new Thread(new Runnable() {
						@Override
						public void run() {
							int recordRet = FosSdkJNI.StartRecord(Global.mHandlerNo, 1, "//sdcard//DCIM//record.mp4");
							Log.i("jerry", "recordRet==="+recordRet);
						}
					}).start();
					recode.setText("停止录像");
					Global.isRecordOrNot = true;
				}else{
					new Thread(new Runnable() {
						@Override
						public void run() {
						FosSdkJNI.StopRecord(Global.mHandlerNo);
					}
					}).start();
					recode.setText("开始录像");
					Global.isRecordOrNot = false;
				}
			}
			break;
		case R.id.capture_pic_btn:
			if(Global.isReceiveData){
			//	int ret = FosSdkJNI.NetSnapPicture(Global.mHandlerNo, 3000, "//sdcard//DCIM//Camera//mpeg.jpeg");
				int ret = FosSdkJNI.NetSnap(Global.mHandlerNo, 3000, snapData, dataLen);
				if(ret == 0)
				{
					Toast.makeText(getApplicationContext(), 
							"截取图片成功", 500).show();
					FileOutputStream out = null;
					try {
						out = new FileOutputStream(new File("//sdcard//DCIM//Camera//mpeg.jpeg"));
						try {
							out.write(snapData);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally
					{
						try {
							out.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}  
					}	
				}
				
			}
			break;
		case R.id.bt_pre:
			if(Global.isReceiveData){
				new Thread(new Runnable() {
					@Override
					public void run() {
					}
				}).start();
			}
			break;
		case R.id.bt_next:
			if(Global.isReceiveData){
				
				new Thread(new Runnable() {
					@Override
					public void run() {
					}
				}).start();
			}
			break;
		}
	}

}
