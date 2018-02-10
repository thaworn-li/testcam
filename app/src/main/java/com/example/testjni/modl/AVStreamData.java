package com.example.testjni.modl;

public class AVStreamData {
	public int frameType; // 0=video 1=audio 3=snap
	public int videoFormat; // 0=H264 1=MJPEG
	public byte[] data;
	public int dataLen;
	/** 1�ؼ�֡��0�ǹؼ�֡��ÿ��һ��ʱ�����һ���ؼ�֡����ʱ���������ã�ÿ���ؼ�֡���涼�����ɸ��ǹؼ�֡��H264���йؼ�֡��ǹؼ�֡�� */
	public int isKeyFrame;
	public int videoWidth;
	public int videoHeight;
	public int reserve1;
	public int reserve2;
	public int reserve3;
	public int reserve4;
}
