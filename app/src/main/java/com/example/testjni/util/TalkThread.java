package com.example.testjni.util;


import com.fos.sdk.FosSdkJNI;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.os.Build.VERSION_CODES;
import android.os.SystemClock;
import android.util.Log;

/**
 * �Խ��߳�<br>
 * �����ǵ��������������⣬��v4.1��ʹ��AcousticEchoCanceler�ࣩ
 * 
 * @author FuS
 * @date 2014-5-8 ����7:07:31
 */
@SuppressLint("NewApi")
public class TalkThread extends Thread {
	public boolean isRunTalk = false;
//	AudioRecord mAudioRecord;
	byte[] buffer = new byte[960];
	/** �������� */
	public AcousticEchoCanceler canceler; 
	/** ����Ƶ�� */
	final int sampleRateInHz = 8000;

	public TalkThread() {
		
	}

	@SuppressLint("NewApi")
	public void init() {
		int minsize = AudioRecord.getMinBufferSize(sampleRateInHz, AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
		/* �������鲻��̫С,̫С�ӳ��� */
		
		if (android.os.Build.VERSION.SDK_INT >= 16) 
		{ 
			Global.mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION, sampleRateInHz, AudioFormat.CHANNEL_IN_DEFAULT,  AudioFormat.ENCODING_PCM_16BIT, minsize * 15); 

		}else 
		{ 
			Global.mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRateInHz, AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT, minsize * 15); 
		} 

		
	//	mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRateInHz, AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT, minsize * 15);
		
		if (android.os.Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
			boolean isa = AcousticEchoCanceler.isAvailable();
			if (isa) {
				int aa = Global.mAudioRecord.getAudioSessionId();
				canceler =
						AcousticEchoCanceler.create(aa);
				Log.i("jerry", "canceler = "+ canceler);
				canceler.setEnabled(true);
			}
		}
	}

	public void startTalk() {
		if (Global.mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
			Global.mAudioRecord.startRecording();
			isRunTalk = true;
		}
	}

	public void PauseTalk() {
		if (Global.mAudioRecord.getState() == AudioRecord.RECORDSTATE_RECORDING) {
			Global.mAudioRecord.stop();
		}
	}

	public void StopTalk() {
		isRunTalk = false;
		canceler.setEnabled(false);
		canceler.release();
		Global.mAudioRecord.stop();
		Global.mAudioRecord.release();
	}

	@Override
	public void run() {
		while(isRunTalk){
			int length = 960;
//			int type = Global.currentPlayCamera.getType();
//			if(type == 0) {
//				length = 640;
//			}
			if(Global.isReceiveData){
				int ReadSize = Global.mAudioRecord.read(buffer, 0, length);
				if (ReadSize > 0) {
					Log.i("jerry", "send-------------");
					int state = FosSdkJNI.SendTalkData(Global.mHandlerNo, buffer, 960);
				}
			}
	//		SystemClock.sleep(1);
		}
		super.run();
	}

}
