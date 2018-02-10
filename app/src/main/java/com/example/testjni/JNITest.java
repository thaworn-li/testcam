package com.example.testjni;

import junit.framework.Assert;
import android.test.AndroidTestCase;
import android.util.Log;


import com.fos.sdk.*;

public class JNITest extends AndroidTestCase{
	String url = "172.16.0.88";
//	String url = "192.168.10.109";
	String uid = "DFJTA1RY8WUF8G6PSZYS";
	String usr = "a";
	String pwd = "a";
	short port = 88;
	int ret = -1;
	int handle = -1;
	int flag = -1;
	Integer retInt = -1;
	int timeout = 3000;
	
	
	public void testBase()
	{
		FosSdkJNI.Init();
		handle = FosSdkJNI.Create(url, uid, usr, pwd, port, port, IPCType.FOSIPC_H264, ConnectType.FOSCNTYPE_IP);
		Assert.assertTrue(handle > 0);
		{
			ret = FosSdkJNI.Login(handle, retInt, timeout);
		}
		
		ret = FosSdkJNI.CheckHandle(handle, retInt);
		Log.i("jerry", "CheckHandle usrright = "+ retInt);
		
	//	ret = FosSdkJNI.RetainHandle(handle, retInt);
	//	Log.i("jerry", "retain handle ret = "+ ret + "usrright = "+retInt);
		
		FosDiscovery_Node[] node;
		node = FosSdkJNI.Discovery(retInt, 500);
		Assert.assertTrue(retInt >= 0);
		
		ret = FosSdkJNI.OpenVideo(handle, StreamType.FOSSTREAM_SUB, timeout);
		Assert.assertEquals(0, ret);
		
		/*
		FrameData mFramedata = new FrameData();
		mFramedata.data = new byte[1024*1024*3];
		ret = FosSdkJNI.GetVideoData(handle, mFramedata,FosDecFmt.FOSDECTYPE_RGBA32);
		Log.i("jerry", "GetVideoData ret = "+ret);
		Assert.assertEquals(0, ret);
		*/
		ret = FosSdkJNI.OpenAudio(handle, StreamType.FOSSTREAM_SUB, timeout);
		Assert.assertEquals(0, ret);
		
		/*
		ret = FosSdkJNI.GetAudioData(handle, mFramedata);
		Log.i("jerry", "GetAudioData ret = "+ret);
		Assert.assertEquals(0, ret);
		*/
		
		ret = FosSdkJNI.OpenTalk(handle, timeout);
		Assert.assertEquals(0, ret);
		
		byte[] bdata = new byte[960];
		ret = FosSdkJNI.SendTalkData(handle, bdata, 960);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.CloseAudio(handle, timeout);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.CloseVideo(handle, timeout);
		Assert.assertEquals(0, ret);
		
		FosSdkJNI.CancelAllNetCmd(handle);
		
		FosSdkJNI.Logout(handle, timeout);
		FosSdkJNI.Release(handle);
		FosSdkJNI.DeInit();
	}
	
	
	
	public void testUserAccount()
	{
		FosSdkJNI.Init();
		handle = FosSdkJNI.Create(url, uid, usr, pwd, port, port, IPCType.FOSIPC_H264, ConnectType.FOSCNTYPE_IP);
		Assert.assertTrue(handle > 0);
		ret = FosSdkJNI.Login(handle, retInt, timeout);
		Assert.assertEquals(0, ret);
		
		UserList ulist = new UserList();
		ret = FosSdkJNI.GetUserList(handle, 2000, ulist);
		Log.i("jerry", "user count = ------>"+ ulist.usrCnt);
		Log.i("jerry", "privilege = ------>"+ ulist.privilege[0]);
		Log.i("jerry", "name = ------>"+ ulist.usrName[0]);
		Assert.assertEquals(0, ret);
		
		SessionList slist = new SessionList();
		ret = FosSdkJNI.GetSessionList(handle, 2000, slist);
		Log.i("jerry", "session count = ------>"+ slist.usrCnt);
		Log.i("jerry", "session ip = ------>"+ slist.sessionIP[0]);
		Assert.assertEquals(0, ret);
		
		LoginResult lg = new LoginResult();
		
		ret = FosSdkJNI.LogInCGI(handle, 1000,  lg);
		Log.i("jerry", "LogInCGI------->" + ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.LogOutCGI(handle, 1000);
		Log.i("jerry", "LogoutCGI------->" + ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.AddAccount(handle, 1000, "sd", "sd", 2);
		Log.i("jerry", "AddAccount------->" + ret);
		Assert.assertEquals(0, ret);
		if(ret == 0)
		{
			ret = FosSdkJNI.ChangeUsrName(handle, 1000, "sd", "ss");
			Log.i("jerry", "ChangeUsrName------->" + ret);
			Assert.assertEquals(0, ret);
			
			ret = FosSdkJNI.ChangePassword(handle, 1000, "ss", "sd", "ss");
			Log.i("jerry", "ChangePassword------->" + ret);
			Assert.assertEquals(0, ret);
			
			/*
			ret = FosSdkJNI.ChangeUserNameAndPwdTogether(handle, 1000, "ss", "sd", "ss", "sd");
			Log.i("jerry", "ChangeUserNameAndPwdTogether------->" + ret);
			Assert.assertEquals(0, ret);
			*/
			ret = FosSdkJNI.DelAccount(handle, 1000, "ss");
			Log.i("jerry", "DelAccount------->" + ret);
			Assert.assertEquals(0, ret);
			
		}
		
		FosSdkJNI.Logout(handle, timeout);
		FosSdkJNI.Release(handle);
		FosSdkJNI.DeInit();
		
	}
	
	/*
	public void testPTZ()
	{
		FosSdkJNI.Init();
		handle = FosSdkJNI.Create(url, uid, usr, pwd, port, port, IPCType.FOSIPC_H264, ConnectType.FOSCNTYPE_IP);
		Assert.assertTrue(handle > 0);
		ret = FosSdkJNI.Login(handle, retInt, timeout);
		Assert.assertEquals(0, ret);
		//ÔÆÌ¨·½Ïò
		
		ret = FosSdkJNI.PtzCmd(handle, PtzCmd.FOSPTZ_UP, timeout);
		Assert.assertEquals(0, ret);
		ret = FosSdkJNI.PtzCmd(handle, PtzCmd.FOSPTZ_DOWN, timeout);
		Assert.assertEquals(0, ret);
		ret = FosSdkJNI.PtzCmd(handle, PtzCmd.FOSPTZ_LEFT, timeout);
		Assert.assertEquals(0, ret);
		ret = FosSdkJNI.PtzCmd(handle, PtzCmd.FOSPTZ_RIGHT, timeout);
		Assert.assertEquals(0, ret);
		ret = FosSdkJNI.PtzCmd(handle, PtzCmd.FOSPTZ_LEFT_DOWN, timeout);
		Assert.assertEquals(0, ret);
		ret = FosSdkJNI.PtzCmd(handle, PtzCmd.FOSPTZ_LEFT_UP, timeout);
		Assert.assertEquals(0, ret);
		ret = FosSdkJNI.PtzCmd(handle, PtzCmd.FOSPTZ_RIGHT_DOWN, timeout);
		Assert.assertEquals(0, ret);
		ret = FosSdkJNI.PtzCmd(handle, PtzCmd.FOSPTZ_RIGHT_UP, timeout);
		Assert.assertEquals(0, ret);
		ret = FosSdkJNI.PtzCmd(handle, PtzCmd.FOSPTZ_CENTER, timeout);
		Assert.assertEquals(0, ret);
		ret = FosSdkJNI.PtzCmd(handle, PtzCmd.FOSPTZ_STOP, timeout);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.PTZGetSpeed(handle, timeout, retInt);
		Log.i("jerry", "PTZGetSpeed  ret = :" + ret);
		Assert.assertEquals(0, ret);
		ret = FosSdkJNI.PTZSetSpeed(handle, PtzSpeed.FOSPTZ_FAST, timeout);
		Log.i("jerry", "PTZSetSpeed  ret = :" + ret);
		Assert.assertEquals(0, ret);
	
		CruiseMapInfo cruiseMapInfo = new CruiseMapInfo();
		cruiseMapInfo.cruiseMapName = new String("aa");
		cruiseMapInfo.pointName = new String[8];
		
		cruiseMapInfo.pointName[0] = "aa";
		cruiseMapInfo.pointName[1] = "bb";
		cruiseMapInfo.pointName[2] = "aa";
		cruiseMapInfo.pointName[3] = "bb";
		cruiseMapInfo.pointName[4] = "aa";
		cruiseMapInfo.pointName[5] = "bb";
		cruiseMapInfo.pointName[6] = "aa";
		cruiseMapInfo.pointName[7] = "bb";

		ret = FosSdkJNI.PTZSetCruiseMap(handle, cruiseMapInfo, timeout);
		Log.i("jerry", "PTZSetCruiseMap  ret = :" + ret);
		Assert.assertEquals(0, ret);
	
		ret = FosSdkJNI.PTZGetCruiseMapInfo(handle, "aa", timeout, cruiseMapInfo);
		Log.i("jerry", "PTZGetCruiseMapInfo  ret = :" + ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.PTZDelCruiseMap(handle, "aa", timeout);
		Log.i("jerry", "PTZDelCruiseMap  ret = :" + ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.PTZSetCruiseTime(handle, 2, timeout);
		Log.i("jerry", "PTZSetCruiseTime  ret = :" + ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.PTZGetCruiseTime(handle, retInt, timeout);
		Log.i("jerry", "PTZGetCruiseTime  ret = :" + ret);
		Assert.assertEquals(0, ret);
		
		
		CruiseTimeCustomed timeCustomed = new CruiseTimeCustomed();
		
		timeCustomed.time = 2;
		timeCustomed.customed = 1;
		ret = FosSdkJNI.PTZSetCruiseTimeCustomed(handle, timeCustomed, timeout);
		Log.i("jerry", "PTZSetCruiseTimeCustomed  ret = :" + ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.PTZGetCruiseTimeCustomed(handle, timeCustomed, timeout);
		Log.i("jerry", "PTZGetCruiseTimeCustomed  ret = :" + ret);
		Assert.assertEquals(0, ret);
		
		
		ret = FosSdkJNI.PTZZoom(handle, PtzZoomCmd.FOSPTZ_ZOOMIN, timeout);
		Assert.assertEquals(0, ret);
		ret = FosSdkJNI.PTZSetZoomSpeed(handle, PtzZoomSpeed.FOSPTZZOOM_SLOW, timeout);
		Assert.assertEquals(0, ret);
		ret = FosSdkJNI.PTZGetZoomSpeed(handle, timeout, retInt);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.PTZSetCruiseLoopCnt(handle, 1, timeout);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.PTZGetCruiseLoopCnt(handle, timeout, retInt);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.PTZSetCruiseCtrlMode(handle, CruiseCtrlMode.CRUISECTRLMODE_LOOPCOUNT, timeout);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.PTZGetCruiseCtrlMode(handle, timeout, retInt);
		Assert.assertEquals(0, ret);
		
		CruisePrePointLingerTime ptime = new CruisePrePointLingerTime();
		
		ptime.cruiseMapName = new String("bb");
		ptime.time = new int[8];
		for(int i=0;i < 8;i++)
		{
			ptime.time[i] = 0;
		}
		
		ret= FosSdkJNI.PTZSetCruisePrePointLingerTime(handle, ptime, timeout);
		Log.i("jerry", "PTZSetCruisePrePointLingerTime  ret = :" + ret);
		Assert.assertEquals(0, ret);
		
		ret= FosSdkJNI.PTZGetCruisePrePointLingerTime(handle, "bb", timeout, ptime);
		Log.i("jerry", "PTZGetCruisePrePointLingerTime  ret = :" + ret);
		Assert.assertEquals(0, ret);
		
		
		ret = FosSdkJNI.PTZSetSelfTestMode(handle, PtzSelfTestMode.FOSPTZSELFTESTMODE_GO_PREPOINT, timeout);
		Log.i("jerry", "PTZSetSelfTestMode  ret = :" + ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.PTZGetSelfTestMode(handle, timeout, retInt);
		Log.i("jerry", "PTZGetSelfTestMode  ret = :" + ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.PTZSetPrePointForSelfTest(handle, "cc", timeout);
		Log.i("jerry", "PTZSetPrePointForSelfTest  ret = :" + ret);
		Assert.assertEquals(0, ret);
		
		StrData retStr = new StrData();
		ret = FosSdkJNI.PTZGetPrePointForSelfTest(handle, timeout, retStr);
		Log.i("jerry", "PTZGetPrePointForSelfTest  ret = :" + ret);
		Assert.assertEquals(0, ret);
		
		
		Fos485Info info = new Fos485Info();
		ret = FosSdkJNI.PTZGet85Info(handle, timeout, info);
		Log.i("jerry", "PTZGet85Info  ret = :" + ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.PTZSet85Info(handle, info, timeout);
		Log.i("jerry", "PTZSet85Info  ret = :" + ret);
		Assert.assertEquals(0, ret);
		
		
		ResetPointList lst = new ResetPointList();
		
		ret = FosSdkJNI.PTZAddPresetPoint(handle, "88", timeout, lst);
		Log.i("jerry", "PTZAddPresetPoint  ret = :" + ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.PTZGoToPresetPoint(handle, "gg", timeout);
		Log.i("jerry", "PTZGoToPresetPoint  ret = :" + ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.PTZDelPresetPoint(handle, "gg", timeout, lst);
		Log.i("jerry", "PTZDelPresetPoint  ret = :" + ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.PTZGetPresetPointList(handle, timeout, lst);
		Log.i("jerry", "PTZGetPresetPointList  ret = :" + ret);
		Assert.assertEquals(0, ret);
		
	
		CruiseMapList clist = new CruiseMapList();
		ret = FosSdkJNI.PTZGetCruiseMapList(handle, timeout, clist);
		Log.i("jerry", "PTZGetCruiseMapList  ret = :" + ret);
		Assert.assertEquals(0, ret);
	
		
		ret = FosSdkJNI.PTZStartCruise(handle, "aa", timeout);
		Log.i("jerry", "PTZStartCruise  ret = :" + ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.PTZStopCruise(handle, timeout);
		Log.i("jerry", "PTZStopCruise  ret = :" + ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.PTZFocus(handle, PtzFocusCmd.FOSPTZ_FOCUSNEAR, timeout);
		Log.i("jerry", "PTZFocus  ret = :" + ret);
	//	Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.PTZSetGuardPosition(handle, "ww", timeout);
		Log.i("jerry", "PTZSetGuardPosition  ret = :" + ret);
	//	Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.PTZGetGuardPosition(handle, retStr, timeout);
		Log.i("jerry", "PTZGetGuardPosition  ret = :" + ret);
	//	Assert.assertEquals(0, ret);
				
		ret = FosSdkJNI.PTZSetGuardPositionBackTime(handle, 2, timeout);
		Log.i("jerry", "PTZSetGuardPositionBackTime  ret = :" + ret);
	//	Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.PTZGetGuardPositionBackTime(handle, retInt, timeout);
		Log.i("jerry", "PTZGetGuardPositionBackTime  ret = :" + ret);
	//	Assert.assertEquals(0, ret);
		
		FosSdkJNI.Logout(handle, timeout);
		FosSdkJNI.Release(handle);
		FosSdkJNI.DeInit();
		
	}
	*/
	
	
	public void testDevManage()
	{
		FosSdkJNI.Init();
		handle = FosSdkJNI.Create(url, uid, usr, pwd, port, port, IPCType.FOSIPC_H264, ConnectType.FOSCNTYPE_IP);
		Assert.assertTrue(handle > 0);
		ret = FosSdkJNI.Login(handle, retInt, timeout);
		Assert.assertEquals(0, ret);
		
		DevInfo devinfo = new DevInfo();
		DevState state = new DevState();
		
		ProductAllInfo allinfo = new ProductAllInfo();
		
		ret = FosSdkJNI.GetDevInfo(handle, 1000 , devinfo);
		Log.i("jerry", "GetDevInfo ret = ------>"+ ret);
		Log.i("jerry", "GetDevInfo  devName= ------>"+ devinfo.devName);
		Log.i("jerry", "GetDevInfo hardwareVer = ------>"+ devinfo.hardwareVer);
		Log.i("jerry", "GetDevInfo productName = ------>"+ devinfo.productName);
		Log.i("jerry", "GetDevInfo serialNo = ------>"+ devinfo.serialNo);
		Log.i("jerry", "GetDevInfo year = ------>"+ devinfo.year);
		Assert.assertEquals(0, ret);
		ret = FosSdkJNI.GetDevState(handle, 1000 , state);
		Log.i("jerry", "GetDevState sdTotalSpace = ------>"+ state.sdTotalSpace);
		Log.i("jerry", "GetDevState url = ------>"+ state.url);
		Log.i("jerry", "GetDevState wifiConnectedAP = ------>"+ state.wifiConnectedAP);
		Log.i("jerry", "GetDevState ddnsState = ------>"+ state.ddnsState);
		Assert.assertEquals(0, ret);
		ret = FosSdkJNI.GetProductAllInfo(handle, 1000 , allinfo);
		Log.i("jerry", "GetProductAllInfo ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		StrData dname = new StrData();
		ret = FosSdkJNI.GetDevName(handle, timeout, dname);
		Log.i("jerry", "GetDevName ret = ------>"+ ret+ "devname: "+ dname.str);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetDevName(handle, 1000, "FosBaby");
		Log.i("jerry", "SetDevName ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		DevSystemTime time1 = new DevSystemTime();
		ret = FosSdkJNI.GetSystemTime(handle, 1000, time1);
		Log.i("jerry", "GetSystemTime ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetSystemTime(handle, 1000, time1);
		Log.i("jerry", "SetSystemTime ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetInfraLedConfig(handle, 1000, retInt);
		Log.i("jerry", "GetInfraLedConfig ret = ------>"+ ret);
	//	Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetInfraLedConfig(handle, 1000, 1);
		Log.i("jerry", "SetInfraLedConfig ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.OpenInfraLed(handle, 1000, retInt);
		Log.i("jerry", "OpenInfraLed ret = ------>"+ ret + "result£½:" + retInt);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.CloseInfraLed(handle, 1000, retInt);
		Log.i("jerry", "CloseInfraLed ret = ------>"+ ret + "result£½:" + retInt);
		Assert.assertEquals(0, ret);
		
		ScheduleInfraledConfig config = new  ScheduleInfraledConfig();
		ret = FosSdkJNI.GetScheduleInfraLedConfig(handle, 1000, config);
		Log.i("jerry", "GetScheduleInfraLedConfig ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetScheduleInfraLedConfig(handle, 1000, config);
		Log.i("jerry", "SetScheduleInfraLedConfig ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetProductModel(handle, 1000, retInt);
		Log.i("jerry", "GetProductModel ret = ------>"+ ret+ "retInt:" + retInt);
		Assert.assertEquals(0, ret);
		
		StrData name = new StrData();
		ret = FosSdkJNI.GetProductModelName(handle, 1000, name);
		Log.i("jerry", "GetProductModelName ret = ------>"+ ret+ "name:" + name.str);
		Assert.assertEquals(0, ret);
		
		
		ret = FosSdkJNI.GetProductLanguage(handle, 1000, retInt);
		Log.i("jerry", "GetProductLanguage ret = ------>"+ ret+ "retInt:" + retInt);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetProductSensorType(handle, 1000, retInt);
		Log.i("jerry", "GetProductSensorType ret = ------>"+ ret+ "retInt:" + retInt);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetProductWifiType(handle, 1000, retInt);
		Log.i("jerry", "GetProductWifiType ret = ------>"+ ret+ "retInt:" + retInt);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetProductSdFlag(handle, 1000, retInt);
		Log.i("jerry", "GetProductSdFlag ret = ------>"+ ret+ "retInt:" + retInt);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetProductOutdoorFlag(handle, 1000, retInt);
		Log.i("jerry", "GetProductOutdoorFlag ret = ------>"+ ret+ "retInt:" + retInt);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetProductZoomFlag(handle, 1000, retInt);
		Log.i("jerry", "GetProductZoomFlag ret = ------>"+ ret+ "retInt:" + retInt);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetProductRs485Flag(handle, 1000, retInt);
		Log.i("jerry", "GetProductRs485Flag ret = ------>"+ ret+ "retInt:" + retInt);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetProductIoAlarmFlag(handle, 1000, retInt);
		Log.i("jerry", "GetProductIoAlarmFlag ret = ------>"+ ret+ "retInt:" + retInt);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetProductOnvifFlag(handle, 1000, retInt);
		Log.i("jerry", "GetProductOnvifFlag ret = ------>"+ ret+ "retInt:" + retInt);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetProductP2pFlag(handle, 1000, retInt);
		Log.i("jerry", "GetProductP2pFlag ret = ------>"+ ret+ "retInt:" + retInt);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetProductWpsFlag(handle, 1000, retInt);
		Log.i("jerry", "GetProductWpsFlag ret = ------>"+ ret+ "retInt:" + retInt);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetProductAudioFlag(handle, 1000, retInt);
		Log.i("jerry", "GetProductAudioFlag ret = ------>"+ ret+ "retInt:" + retInt);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetProductTalkFlag(handle, 1000, retInt);
		Log.i("jerry", "GetProductTalkFlag ret = ------>"+ ret+ "retInt:" + retInt);
		Assert.assertEquals(0, ret);
		
		StrData ss = new StrData();
		ret = FosSdkJNI.GetProductAppVer(handle, 1000, ss);
		Log.i("jerry", "GetProductAppVer ver = ------>"+ ret+ "ver:" + ss.str);
		Assert.assertEquals(0, ret);
		
		GeneratePubkey pubkey = new  GeneratePubkey();
		ret = FosSdkJNI.GetGeneratePubKey(handle, 1000, pubkey);
		Log.i("jerry", "GetProductAppVer ver = ------>"+ ret+ "value:" + pubkey.pubKey);
		Assert.assertEquals(0, ret);
		/*
		TempAlarmConfig  tempAlarmConfig = new TempAlarmConfig();
		ret = FosSdkJNI.GetTemperatureAlarmConfig(handle, timeout, tempAlarmConfig);
		Log.i("jerry", "GetTemperatureAlarmConfig ret = ------>"+ ret+ " tempAlarmConfig  linkage:" + tempAlarmConfig.linkage);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetTemperatureAlarmConfig(handle, timeout, tempAlarmConfig);
		Log.i("jerry", "SetTemperatureAlarmConfig ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetTemperatureState(handle, timeout, retInt);
		Log.i("jerry", "GetTemperatureState ret = ------>"+ ret + "degreee = :"+ retInt);
		Assert.assertEquals(0, ret);
		
		HumidityAlarmConfig humidityAlarmConfig = new HumidityAlarmConfig();
		ret = FosSdkJNI.GetHumidityAlarmConfig(handle, timeout, humidityAlarmConfig);
		Log.i("jerry", "GetHumidityAlarmConfig ret = ------>"+ ret+ " humidityAlarmConfig  isenable:" + humidityAlarmConfig.isEnable);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetHumidityAlarmConfig(handle, timeout, humidityAlarmConfig);
		Log.i("jerry", "SetHumidityAlarmConfig ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetHumidityState(handle, timeout, retInt);
		Log.i("jerry", "GetHumidityState ret = ------>"+ ret + "hum = :"+ retInt);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetNightLightState(handle, timeout, 1);
		Log.i("jerry", "SetNightLightState ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		*/
		
		ret = FosSdkJNI.SetLedEnableState(handle, timeout, 1);
		Log.i("jerry", "SetLedEnableState ret = ------>"+ ret);
	//	Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetLedEnableState(handle, timeout, retInt);
		Log.i("jerry", "GetLedEnableState ret = ------>"+ ret + "result = :"+ retInt);
	//	Assert.assertEquals(0, ret);
		
		OneKeyAlarmConfig oneKeyAlarmconfig = new OneKeyAlarmConfig();
		ret = FosSdkJNI.GetOneKeyAlarmConfig(handle, timeout, oneKeyAlarmconfig);
		Log.i("jerry", "GetOneKeyAlarmConfig ret = ------>"+ ret + "result = :"+ oneKeyAlarmconfig.isEnable);
	//	Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetOneKeyAlarmConfig(handle, timeout, oneKeyAlarmconfig);
		Log.i("jerry", "SetOneKeyAlarmConfig ret = ------>"+ ret);
	//	Assert.assertEquals(0, ret);
		
		TimingRebootConfig timingRebootConfig= new TimingRebootConfig();
		ret = FosSdkJNI.GetTimingRebootConfig(handle, timeout, timingRebootConfig);
		Log.i("jerry", "GetTimingRebootConfig ret = ------>"+ ret + "retsult = :"+ timingRebootConfig.isEnable);
	//	Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetTimingRebootConfig(handle, timeout, timingRebootConfig);
		Log.i("jerry", "SetTimingRebootConfig ret = ------>"+ ret);
	//	Assert.assertEquals(0, ret);
		
		FosSdkJNI.Logout(handle, timeout);
		FosSdkJNI.Release(handle);
		FosSdkJNI.DeInit();
		
	}
	
	
	public void testAVFunction()
	{
		FosSdkJNI.Init();
		handle = FosSdkJNI.Create(url, uid, usr, pwd, port, port, IPCType.FOSIPC_H264, ConnectType.FOSCNTYPE_IP);
		Assert.assertTrue(handle > 0);
		ret = FosSdkJNI.Login(handle, retInt, timeout);
		Assert.assertEquals(0, ret);
		
		Integer a = -1;
		Integer b = -1;
		FosImge img = new FosImge(); 
		ret = FosSdkJNI.GetImageSetting(handle, img, timeout);
		Log.i("jerry", "GetImageSetting ret = ------>"+ ret + "img: = "+img.sharpness);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.ImageCmd(handle, ImgCmd.FOSIMAGE_SHARPNESS, 50, timeout);
		Log.i("jerry", "ImageCmd ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetMirrorState(handle, 1000, a);
		Log.i("jerry", "GetMirrorState ret = ------>"+ ret + "mirror: = "+ a);
	//	Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.MirrorVideo(handle, 0, 1000);
		Log.i("jerry", "MirrorVideo ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.FlipVideo(handle, 0 , 1000);
		Log.i("jerry", "FlipVideo ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		/*
		ret = FosSdkJNI.GetRatio(handle, 1000 , a);
		Log.i("jerry", "GetRatio ret = ------>"+ ret + "ratio=:"+ a);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetRatio(handle, ret , 1000);
		Log.i("jerry", "SetRatio ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		*/
	
		ret = FosSdkJNI.GetH264FrmRefMode(handle, 1000, a);
		Log.i("jerry", "GetH264FrmRefMode ret = ------>"+ ret + "mode = "+ a);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetH264FrmRefMode(handle, 1, 1000);
		Log.i("jerry", "SetH264FrmRefMode ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetScheduleRecordStreamChn(handle, 1000, a);
		Log.i("jerry", "GetScheduleRecordStreamChn ret = ------>"+ ret + "chanel =" + a);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetScheduleRecordStreamChn(handle, StreamType.FOSSTREAM_MAIN, 1000);
		Log.i("jerry", "SetScheduleRecordStreamChn ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetPwrFreq(handle, 0, 1000);
		Log.i("jerry", "SetPwrFreq ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		VideoStreamListParam videoparam = new VideoStreamListParam();
		
		ret = FosSdkJNI.GetVideoStreamParam(handle, 1000, videoparam);
		Log.i("jerry", "GetVideoStreamParam ret = ------>"+ ret + "ssss"+ videoparam.bitRate[0]);
		Assert.assertEquals(0, ret);
		
		VideoStreamParam  param = new VideoStreamParam();
		param.streamType=1;
		param.bitRate=2097152;
		param.frameRate=20;
		param.GOP=10;
		param.isVBR=0;
		param.resolution=0;
		ret = FosSdkJNI.SetVideoStreamParam(handle, param, 1000);
		Log.i("jerry", "SetVideoStreamParam ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetSubVideoStreamParam(handle, 1000, videoparam);
		Log.i("jerry", "GetSubVideoStreamParam ret = ------>"+ ret + "ssss"+ videoparam.bitRate[0]);
		Assert.assertEquals(0, ret);
		
		VideoStreamParam  param2 = new VideoStreamParam();
		param2.streamType=1;
		param2.bitRate=524288;
		param2.frameRate=15;
		param2.GOP=45;
		param2.isVBR=0;
		param2.resolution=1;
		ret = FosSdkJNI.SetSubVideoStreamParam(handle, param2, 1000);
		Log.i("jerry", "SetSubVideoStreamParam ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		Integer stype= 0;
		ret = FosSdkJNI.GetMainVideoStreamType(handle, 1000, stype);
		Log.i("jerry", "GetMainVideoStreamType ret = ------>"+ ret+ "type= "+ stype);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetSubVideoStreamType(handle, 1000, stype);
		Log.i("jerry", "GetSubVideoStreamType ret = ------>"+ ret+ "type= "+ stype);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetMainVideoStreamType(handle, StreamType.FOSSTREAM_MAIN, 1000);
		Log.i("jerry", "SetMainVideoStreamType ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
	
		ret = FosSdkJNI.SetSubStreamFormat(handle, StreamFmt.STREAMFORMAT_H264 , 1000);
		Log.i("jerry", "SetSubStreamFormat ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		OSDSetting osdSetting = new OSDSetting();
		ret = FosSdkJNI.GetOSDSetting(handle, 1000, osdSetting);
		Log.i("jerry", "GetOSDSetting ret = ------>"+ ret+ " able==: "+ osdSetting.isEnableOSDMask);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetOSDSetting(handle, osdSetting, 1000);
		Log.i("jerry", "SetOSDSetting ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		OSDMaskArea osdMaskArea = new OSDMaskArea();
		ret = FosSdkJNI.GetOsdMaskArea(handle, 1000, osdMaskArea);
		Log.i("jerry", "GetOsdMaskArea ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetOsdMaskArea(handle, osdMaskArea, 1000);
		Log.i("jerry", "SetOsdMaskArea ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		Integer able = -1;
		ret = FosSdkJNI.GetOSDMask(handle, 1000, able);
		Log.i("jerry", "GetOSDMask ret = ------>"+ ret+ " able = :"+ able);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetOSDMask(handle, 1 , 1000);
		Log.i("jerry", "SetOSDMask ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		LocalAlarmRecordConfig almcfg = new LocalAlarmRecordConfig();
		ret = FosSdkJNI.GetLocalAlarmRecordConfig(handle, timeout, almcfg);
		Log.i("jerry", "GetLocalAlarmRecordConfig ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetLocalAlarmRecordConfig(handle, almcfg, timeout);
		Log.i("jerry", "SetLocalAlarmRecordConfig ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		SnapConfig snapConfig = new SnapConfig();
		ret = FosSdkJNI.GetSnapConfig(handle, timeout, snapConfig);
		Log.i("jerry", "GetSnapConfig ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetSnapConfig(handle, snapConfig, timeout);
		Log.i("jerry", "SetSnapConfig ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ScheduleSnapConfig  scheduleSnapConfig = new ScheduleSnapConfig();
		ret = FosSdkJNI.GetScheduleSnapConfig(handle, timeout, scheduleSnapConfig);
		Log.i("jerry", "GetScheduleSnapConfig ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
	
		ret = FosSdkJNI.SetScheduleSnapConfig(handle, scheduleSnapConfig, timeout);
		Log.i("jerry", "SetScheduleSnapConfig ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		
		RecordList recordList = new RecordList();
		SearchRecordPara searchRecordPara = new SearchRecordPara();
		searchRecordPara.recordPath = new String("");

		ret = FosSdkJNI.GetRecordList(handle, searchRecordPara, timeout, recordList);
		Log.i("jerry", "GetRecordList ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetRecordList2(handle, searchRecordPara, timeout, recordList);
		Log.i("jerry", "GetRecordList2 ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.ReloadRecordindex(handle, timeout);
		Log.i("jerry", "ReloadRecordindex ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		AlarmRecordConfig alarmRecordConfig = new AlarmRecordConfig();
		ret = FosSdkJNI.GetAlarmRecordConfig(handle, timeout, alarmRecordConfig);
		Log.i("jerry", "GetAlarmRecordConfig ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetAlarmRecordConfig(handle, alarmRecordConfig, timeout);
		Log.i("jerry", "SetAlarmRecordConfig ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		RecordPath recordPath = new RecordPath();
		ret = FosSdkJNI.GetRecordPath(handle, timeout, recordPath);
		Log.i("jerry", "GetRecordPath ret = ------>"+ ret+ "K= :"+ recordPath.totalCapacity);
		Assert.assertEquals(0, ret);
		
		ScheduleRecordConfig scheduleRecordCofig = new ScheduleRecordConfig();
		ret = FosSdkJNI.GetScheduleRecordConfig(handle, timeout, scheduleRecordCofig);
		Log.i("jerry", "GetScheduleRecordConfig ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetScheduleRecordConfig(handle, scheduleRecordCofig, timeout);
		Log.i("jerry", "SetScheduleRecordConfig ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		
		IOAlarmConfig iOalarmConfig = new IOAlarmConfig();
		ret = FosSdkJNI.GetIOAlarmConfig(handle, timeout, iOalarmConfig);
		Log.i("jerry", "GetIOAlarmConfig ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetIOAlarmConfig(handle, iOalarmConfig, timeout);
		Log.i("jerry", "SetIOAlarmConfig ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.ClearIOAlarmOutput(handle, timeout);
		Log.i("jerry", "ClearIOAlarmOutput ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetPCAudioAlarmCfg(handle, timeout, retInt);
		Log.i("jerry", "GetPCAudioAlarmCfg ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetPCAudioAlarmCfg(handle, retInt, timeout);
		Log.i("jerry", "SetPCAudioAlarmCfg ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetAudioVolume(handle, timeout, 60);
		Log.i("jerry", "SetAudioVolume ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetAudioVolume(handle, timeout, retInt);
		Log.i("jerry", "GetAudioVolume ret = ------>"+ ret +"valum = :"+ retInt);
	//	Assert.assertEquals(0, ret);
		
		DeviceList deviceList = new DeviceList();
		ret = FosSdkJNI.GetMultiDevList(handle, timeout, deviceList);
		Log.i("jerry", "GetMultiDevList ret = ------>"+ ret +"deviceList = :"+ deviceList.devName[0]);
		Assert.assertEquals(0, ret);
		
		MultiDevice multiDeviceInfo = new MultiDevice();
		ret = FosSdkJNI.GetMultiDevDetailInfo(handle, timeout, 0, multiDeviceInfo);
		Log.i("jerry", "GetMultiDevDetailInfo ret = ------>"+ ret +"multiDeviceInfo = :"+ multiDeviceInfo.devName);
		Assert.assertEquals(0, ret);
		
		
		MultiDevice dev = new MultiDevice();
		dev.chnnl = 1;
		dev.devName = "xx";
		dev.ip = "192.168.10.108";
		dev.passwd  = "a";
		dev.username = "a";
		ret = FosSdkJNI.AddMultiDev(handle, timeout, dev);
		Log.i("jerry", "AddMultiDev ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.DelMultiDev(handle, timeout, 1);
		Log.i("jerry", "DelMultiDev ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetDeFrameLevel(handle, timeout, retInt);
		Log.i("jerry", "GetDeFrameLevel ret = ------>"+ ret + "result=:"+ retInt);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetDeFrameLevel(handle, timeout, 1);
		Log.i("jerry", "SetDeFrameLevel ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		MotionDetectConfig mtcfg = new MotionDetectConfig();
		ret = FosSdkJNI.GetMotionDetectConfig(handle, timeout, mtcfg);
		Log.i("jerry", "GetMotionDetectConfig ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetMotionDetectConfig(handle, timeout, mtcfg);
		Log.i("jerry", "SetMotionDetectConfig ret = ------>"+ ret);
		Assert.assertEquals(0, ret);
		
		FosSdkJNI.Logout(handle, timeout);
		FosSdkJNI.Release(handle);
		FosSdkJNI.DeInit();
		
	}
	
	/*
	public void testNetwork()
	{
		FosSdkJNI.Init();
		handle = FosSdkJNI.Create(url, uid, usr, pwd, port, port, IPCType.FOSIPC_H264, ConnectType.FOSCNTYPE_IP);
		Assert.assertTrue(handle > 0);
		ret = FosSdkJNI.Login(handle, retInt, timeout);
		Assert.assertEquals(0, ret);
		
		
		ret = FosSdkJNI.RefreshWifiList(handle, 3000);
		Log.i("jerry", "RefreshWifiList  ret = "+ret);
		Assert.assertEquals(0, ret);
		
		PortInfo  portinfo = new PortInfo();
		ret = FosSdkJNI.GetPortInfo(handle, timeout, portinfo);
		Log.i("jerry", "GetPortInfo  ret = "+ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetPortInfo(handle, timeout, portinfo);
		Log.i("jerry", "SetPortInfo  ret = "+ret);
		Assert.assertEquals(0, ret);
		
		IPInfo  ipinfo = new IPInfo();
		ret = FosSdkJNI.GetIpInfo(handle, timeout, ipinfo);
		Log.i("jerry", "GetIpInfo  ret = "+ ret);
		Assert.assertEquals(0, ret);
		
		
		ret = FosSdkJNI.SetIpInfo(handle, timeout, ipinfo);
		Log.i("jerry", "SetIpInfo  ret = "+ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetUPnPConfig(handle, timeout, retInt);
		Log.i("jerry", "GetUPnPConfig  ret = "+ ret + "result= :"+ retInt);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetUPnPConfig(handle, timeout, retInt);
		Log.i("jerry", "SetUPnPConfig  ret = "+ret );
		Assert.assertEquals(0, ret);
		
		DDNSConfig ddnscfg  = new DDNSConfig();
		ret = FosSdkJNI.GetDDNSConfig(handle, timeout, ddnscfg);
		Log.i("jerry", "GetDDNSConfig  ret = "+ ret );
		Assert.assertEquals(0, ret);
		
		
		ret = FosSdkJNI.SetDDNSConfig(handle, timeout, ddnscfg);
		Log.i("jerry", "SetDDNSConfig  ret = "+ ret );
		Assert.assertEquals(0, ret);
		
		FTPConfig ftpcfg = new FTPConfig();
		ret = FosSdkJNI.GetFtpConfig(handle, timeout, ftpcfg);
		Log.i("jerry", "GetFtpConfig  ret = "+ ret );
		Assert.assertEquals(0, ret);
		
		
		ret = FosSdkJNI.SetFtpConfig(handle, timeout, ftpcfg);
		Log.i("jerry", "SetFtpConfig  ret = "+ ret );
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.TestFtpServer(handle, timeout, ftpcfg, retInt);
		Log.i("jerry", "TestFtpServer  ret = "+ ret + "result =:"+ retInt);
		Assert.assertEquals(0, ret);
		
		SMTPConfig smtpcfg = new SMTPConfig();
		ret = FosSdkJNI.GetSMTPConfig(handle, timeout, smtpcfg);
		Log.i("jerry", "GetSMTPConfig  ret = "+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetSMTPConfig(handle, timeout, smtpcfg);
		Log.i("jerry", "SetSMTPConfig  ret = "+ ret);
		Assert.assertEquals(0, ret);
		
		
		ret = FosSdkJNI.GetP2PEnable(handle, timeout,retInt);
		Log.i("jerry", "GetP2PEnable  ret = "+ ret+ "enable =:"+ retInt);
	//	Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetP2PEnable(handle, timeout, retInt);
		Log.i("jerry", "SetP2PEnable  ret = "+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetP2PPort(handle, timeout, retInt);
		Log.i("jerry", "GetP2PPort  ret = "+ ret+ "port =:"+ retInt);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetP2PPort(handle, timeout, retInt);
		Log.i("jerry", "SetP2PPort  ret = "+ ret);
		Assert.assertEquals(0, ret);
		
		
		PPPOEConfig ppoecfg = new PPPOEConfig();
		ppoecfg.isEnable = 1;
		ppoecfg.userName = "aa";
		ppoecfg.password = "aa";
		ret = FosSdkJNI.SetPPPoEConfig(handle, 3000, ppoecfg);
		Log.i("jerry", "SetPPPoEConfig  ret = "+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetPPPoEConfig(handle, 3000, ppoecfg);
		Log.i("jerry", "GetPPPoEConfig  name "+ ppoecfg.userName);
		Log.i("jerry", "GetPPPoEConfig  ret = "+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetWifiMode(handle, timeout, retInt);
		Log.i("jerry", "GetWifiMode  ret = "+ ret+ "mode =:"+ retInt);
		Assert.assertEquals(0, ret);
		
	
		SoftAPConfig softapconfig = new SoftAPConfig();
		ret = FosSdkJNI.GetSoftApConfig(handle, 3000, softapconfig);
		Log.i("jerry", "GetSoftApConfig  ret = "+ ret+ "softapconfig =:"+ softapconfig.ssid);
		Assert.assertEquals(0, ret);
		
		
		ret = FosSdkJNI.SetSoftApConfig(handle, 3000, softapconfig);
		Log.i("jerry", "SetSoftApConfig  ret = "+ ret);
	//	Assert.assertEquals(0, ret);
		
		
		WifiList wlist = new WifiList();
		ret = FosSdkJNI.GetWifiList(handle, timeout, 0, wlist);
		Log.i("jerry", "GetWifiList  ret = "+ ret+ "wifilist totalCnt=:"+ wlist.totalCnt);
		Assert.assertEquals(0, ret);
		
		WifiSetting wset = new WifiSetting();
		wset.authMode = 2;
		wset.defaultKey = 1;
		wset.isUseWifi = 1;
		wset.ssid = "100001";
		wset.psk = "10001";
		wset.key1 = "b";
		wset.key2 = "b";
		wset.key3 = "b";
		wset.key4 = "b";
		
		ret = FosSdkJNI.SetWifiSetting(handle, timeout, wset);
		Log.i("jerry", "SetWifiSetting  ret = "+ ret);
		Assert.assertEquals(0, ret);

		P2PInfo p2pinfo = new P2PInfo();
		ret = FosSdkJNI.GetP2PInfo(handle, timeout, p2pinfo);
		Log.i("jerry", "GetP2PInfo  ret = "+ ret);
		Assert.assertEquals(0, ret);
		
		WifiConfig wfcfg = new WifiConfig();
		ret = FosSdkJNI.GetWifiConfig(handle, timeout, wfcfg);
		Log.i("jerry", "GetWifiConfig  ret = "+ ret + "wificfg isUseWifi = :"+ wfcfg.isUseWifi);
		Assert.assertEquals(0, ret);
		
	
	//	ret = FosSdkJNI.ChangeNetMode(handle, timeout, 1);
	//	Log.i("jerry", "ChangeNetMode  ret = "+ ret);
	//	Assert.assertEquals(0, ret);
		 
		
		FosSdkJNI.Logout(handle, timeout);
		FosSdkJNI.Release(handle);
		FosSdkJNI.DeInit();
		
	}
*/
	
	/*
	public void testMusic()
	{
		FosSdkJNI.Init();
		handle = FosSdkJNI.Create(url, uid, usr, pwd, port, port, IPCType.FOSIPC_H264, ConnectType.FOSCNTYPE_IP);
		Assert.assertTrue(handle > 0);
		ret = FosSdkJNI.Login(handle, retInt, timeout);
		Assert.assertEquals(0, ret);
		
		
		ret = FosSdkJNI.SetMusicDefaultListRefresh(handle, timeout);
		Log.i("jerry", "SetMusicDefaultListRefresh  ret = " + ret);
		Assert.assertEquals(0, ret);
		
		MusicListNameList musicListList = new MusicListNameList();
		ret = FosSdkJNI.GetMusicListsName(handle, timeout, musicListList);
		Log.i("jerry", "GetMusicListsName  ret = " + ret);
		Assert.assertEquals(0, ret);
		
		MusicList mlist = new MusicList();
		ret = FosSdkJNI.GetMusicsNameOfList(handle, timeout, musicListList.musicListName[0], mlist);
		Log.i("jerry", "GetMusicsNameOfList  ret = " + ret);
		Assert.assertEquals(0, ret);
		

		MusicList mlist2 = new MusicList();
		mlist2.musicCnt = 2;
		mlist2.musicListName = "default2";
		mlist2.musicName = new String[2];
		mlist2.musicName[0] = "Sample2.mp3";
		mlist2.musicName[1] = "Sample1.mp3";
		CurListInfo curListInfo= new CurListInfo();
		ret = FosSdkJNI.AddMusicList(handle, timeout, mlist2, curListInfo);
		Log.i("jerry", "AddMusicList  ret = " + ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.DelMusicList(handle, timeout, mlist2.musicListName, curListInfo);
		Log.i("jerry", "DelMusicList  ret = " + ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetMusicPlayMode(handle, timeout, retInt);
		Log.i("jerry", "GetMusicPlayMode  ret = " + ret + "result: "+ retInt);
	//	Assert.assertEquals(0, ret);
		
		
		ret = FosSdkJNI.GetMusicPlayPath(handle, timeout, retInt);
		Log.i("jerry", "GetMusicPlayPath  ret = " + ret + "result: "+ retInt);
		Assert.assertEquals(0, ret);
	
		ret = FosSdkJNI.SetMusicPlayPath(handle, timeout, MusicPath.PATH_SDCARD);
		Log.i("jerry", "SetMusicPlayPath  ret = " + ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetMusicPlayPath(handle, timeout, retInt);
		Log.i("jerry", "GetMusicPlayPath  ret = " + ret + "result: "+ retInt);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetMusicDormantTime(handle, timeout, 2);
		Log.i("jerry", "SetMusicDormantTime  ret = " + ret );
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetMusicDormantTime(handle, timeout, retInt);
		Log.i("jerry", "GetMusicDormantTime  ret = " + ret + "result: "+ retInt);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetMusicPlayNext(handle, timeout);
		Log.i("jerry", "SetMusicPlayNext  ret = " + ret );
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetMusicPlayPre(handle, timeout);
		Log.i("jerry", "SetMusicPlayPre  ret = " + ret );
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetMusicPlayStop(handle, timeout);
		Log.i("jerry", "SetMusicPlayStop  ret = " + ret );
		Assert.assertEquals(0, ret);
		
		MusicPlayStateInfo statinfo = new MusicPlayStateInfo();
		ret = FosSdkJNI.GetMusicPlayState(handle, timeout, statinfo);
		Log.i("jerry", "GetMusicPlayState  ret = " + ret );
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetMusicPlayStart(handle, timeout, statinfo);
		Log.i("jerry", "SetMusicPlayStart  ret = " + ret );
		Assert.assertEquals(0, ret);
		
		FosSdkJNI.Logout(handle, timeout);
		FosSdkJNI.Release(handle);
		FosSdkJNI.DeInit();
		
	}
	*/
	/*
	public void testCloud()
	{
		
		FosSdkJNI.Init();
		handle = FosSdkJNI.Create(url, uid, usr, pwd, port, port, IPCType.FOSIPC_H264, ConnectType.FOSCNTYPE_IP);
		Assert.assertTrue(handle > 0);
		ret = FosSdkJNI.Login(handle, retInt, timeout);
		Assert.assertEquals(0, ret);
		
		CloudConfig cloudConfig = new CloudConfig();
		ret = FosSdkJNI.GetCloudConfig(handle, timeout, cloudConfig);
		Log.i("jerry", "GetCloudConfig ret = :"+ ret);
		Assert.assertEquals(0, ret);
		
		
		CloudConfig cloudcfg = new CloudConfig();
		cloudcfg.isEnable= 1;
		cloudcfg.cloudServer= 2;
		cloudcfg.authorizationCode = new String("sdsd");
		ret = FosSdkJNI.TestCloudServer(handle, timeout, cloudcfg);
		Log.i("jerry", "TestCloudServer ret = :"+ ret);
		Assert.assertEquals(0, ret);
		
	
		ret = FosSdkJNI.SetCloudConfig(handle, timeout, cloudConfig);
		Log.i("jerry", "SetCloudConfig ret = :"+ ret);
		Assert.assertEquals(0, ret);
		
		CloudServerInfo cloudServerInfo = new CloudServerInfo();
		cloudServerInfo.isEnable = 1;
		cloudServerInfo.cloudServer = 2;
		ret = FosSdkJNI.SelectCloudServer(handle, timeout, cloudServerInfo);
		Log.i("jerry", "SelectCloudServer ret = :"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.GetCloudToken(handle, timeout, cloudConfig);
		Log.i("jerry", "GetCloudToken ret = :"+ ret);
		Assert.assertEquals(0, ret);

		CloudConfig cloudConfig2 = new CloudConfig();
		cloudConfig2.isEnable = 1;
		cloudConfig2.cloudServer = 2;
		cloudcfg.authorizationCode = new String("sdsd");
		ret = FosSdkJNI.GetCloudQuota(handle, timeout, cloudConfig);
		Log.i("jerry", "GetCloudQuota ret = :"+ ret);
		Assert.assertEquals(0, ret);
		
		PushConfig push = new PushConfig();
		ret = FosSdkJNI.GetPushConfig(handle, timeout, push);
		Log.i("jerry", "GetPushConfig ret = :"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.TestPushServer(handle, timeout, push);
		Log.i("jerry", "TestPushServer ret = :"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.SetPushConfig(handle, timeout, push);
		Log.i("jerry", "SetPushConfig ret = :"+ ret);
		Assert.assertEquals(0, ret);
		
		PushOperateInfo pushOperateInfo = new PushOperateInfo();
		pushOperateInfo.channel_id=1;
		pushOperateInfo.cmd_oper=1;
		pushOperateInfo.device_type=1;
		pushOperateInfo.user_id=1;

		ret = FosSdkJNI.PushOperate(handle, timeout, pushOperateInfo);
		Log.i("jerry", "PushOperate ret = :"+ ret);
		Assert.assertEquals(0, ret);
		
		FosSdkJNI.Logout(handle, timeout);
		FosSdkJNI.Release(handle);
		FosSdkJNI.DeInit();
		
	}
	*/
	
	public void testSystem()
	{
		
		FosSdkJNI.Init();
		handle = FosSdkJNI.Create(url, uid, usr, pwd, port, port, IPCType.FOSIPC_H264, ConnectType.FOSCNTYPE_IP);
		Assert.assertTrue(handle > 0);
		ret = FosSdkJNI.Login(handle, retInt, timeout);
		Assert.assertEquals(0, ret);
		/*
		ret = FosSdkJNI.RebootSystem(handle, timeout);
		Log.i("jerry", "RebootSystem ret = :"+ ret);
		Assert.assertEquals(0, ret);
		
		ret = FosSdkJNI.RestoreToFactorySetting(handle, timeout);
		Log.i("jerry", "RestoreToFactorySetting ret = :"+ ret);
		Assert.assertEquals(0, ret);
		*/
		StrData fname = new StrData();
		ret = FosSdkJNI.ExportConfig(handle, timeout, fname);
		Log.i("jerry", "ExportConfig ret = :"+ ret+ "file name = :"+ fname.str);
		Assert.assertEquals(0, ret);
		/*
		ret = FosSdkJNI.ImportConfig(handle, timeout);
		Log.i("jerry", "ImportConfig ret = :"+ ret+ "file name = :"+ fname.str);
		Assert.assertEquals(0, ret);
		*/
		/*
		String filePath= new String("C:\\Users\\Administrator\\Desktop\\FosBabyIPC_B_app_ver2.x.1.212.bin");
		ret = FosSdkJNI.FwUpgrade(handle, timeout, filePath, retInt);
		Log.i("jerry", "FwUpgrade ret = :"+ ret+ "upgradeResult = :"+ retInt);
		Assert.assertEquals(0, ret);
		*/
		FosSdkJNI.Logout(handle, timeout);
		FosSdkJNI.Release(handle);
		FosSdkJNI.DeInit();
		
	}
	
}
