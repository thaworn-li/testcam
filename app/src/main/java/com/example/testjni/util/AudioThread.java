package com.example.testjni.util;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.fos.sdk.FosSdkJNI;
import com.fos.sdk.FrameData;
import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * ���������߳�
 * 
 * @author FuS
 * @date 2014-5-8 ����5:43:40
 */
@SuppressLint("NewApi")
public class AudioThread extends Thread {
	private AudioTrack mAudioTrack;
	/** �ײ��ȡ�����ݣ���������Ƶ����Ƶ��ͼƬ���ݣ� */
	private FrameData mFramedata;

	public boolean isPlayAudio = false;
	/** ��Ƶ�� */
	final int sampleRateInHz = 8000;
	/** Ĭ�ϵ���Ƶ����buff��С */
	final int defalutBufSize = 1486;

	private Handler mHandler ;
	
	public Integer len = 0;
	
	public AudioThread() {
	}

	/** ��ʼ�� */
	public void init() {	
		Global.mHandler = mHandler;
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == Msg.GET_AUDIO_DATA_SUCC){
					mFramedata = (FrameData) msg.obj;
				}
				super.handleMessage(msg);
			}
		};
		mFramedata = new FrameData();
		mFramedata.data = new byte[1024*1024*3];
		int minBufSize = AudioTrack.getMinBufferSize(sampleRateInHz, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

		// "getMinBufferSize(): error querying hardware";
		if ((minBufSize == -1) || (minBufSize == 0)) {
			minBufSize = defalutBufSize;
		}

		mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRateInHz, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufSize * 4, AudioTrack.MODE_STREAM);
		/*
		if (android.os.Build.VERSION.SDK_INT >= 16 && Global.mAudioRecord != null) 
		{ 
			mAudioTrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL, sampleRateInHz, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufSize * 4, AudioTrack.MODE_STREAM, Global.mAudioRecord.getAudioSessionId()); 
		} 
		else 
		{ 
			mAudioTrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL, sampleRateInHz, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufSize * 4, AudioTrack.MODE_STREAM); 
		} 
		*/
		if (mAudioTrack.getState() == AudioTrack.STATE_UNINITIALIZED) {
			this.init();
			mAudioTrack.play();
		} else {
			mAudioTrack.play();
		//	PausePlay();
		}
	}
	
	
	

	@Override
	public void run() {
		while (Global.isAudioOpenOrNot) {
			FosSdkJNI.GetAudioData(Global.mHandlerNo, mFramedata);
			if(mFramedata != null && mFramedata.len > 0){
					// ��������һ��ʱ��󣬻����ӳ٣���ʱ�䳤�̣��ӳ��д�С����������������
					if ( (mAudioTrack.getPlayState() != AudioTrack.PLAYSTATE_PAUSED) && (mFramedata.len>0)) {
						Log.i("jerry", "I  get voice len ============ "+ mFramedata.len);
			   			mAudioTrack.write(mFramedata.data, 0, mFramedata.len);			    
					}	
					/*
					InputStream in = null;
					byte[] tempbytes = new byte[960];  
					int byteread = 0;  
					try {
						in = new FileInputStream("//sdcard//DCIM//out.pcm");
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					try {
						while ((byteread = in.read(tempbytes)) != -1){  
							mAudioTrack.write(tempbytes, 0, byteread);
			    			Log.i("jerry", "wanwan..............======");	
			    			
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
					*/
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		super.run();
	}

	public void PausePlay() {
		if (mAudioTrack.getState() == AudioTrack.STATE_INITIALIZED) {
			mAudioTrack.pause();
		}
	}

	public void ResumePlay() {
		if (mAudioTrack.getState() == AudioTrack.STATE_INITIALIZED) {
			mAudioTrack.play();
		}
	}

	public void StopRun() {
		if (mAudioTrack.getState() == AudioTrack.STATE_INITIALIZED) {
			mAudioTrack.stop();
			mAudioTrack.release();
		}
	}
}
