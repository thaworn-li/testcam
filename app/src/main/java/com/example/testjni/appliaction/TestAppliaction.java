package com.example.testjni.appliaction;

import com.fos.sdk.FosSdkJNI;

import android.app.Application;

public class TestAppliaction extends Application{

	@Override
	public void onCreate() {
		super.onCreate();
		 FosSdkJNI.Init(); 
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		FosSdkJNI.DeInit();
		super.onTerminate();
	}
	
}
