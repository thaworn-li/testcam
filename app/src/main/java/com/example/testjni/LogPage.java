package com.example.testjni;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.testjni.util.Global;
import com.fos.sdk.*;
import com.mydemo.snapdemo.R;

public class LogPage extends Activity{
	
	private Button btcon;
//	private EditText ip;
	private EditText uid;
	private EditText ssid;
	private EditText psk;
	private EditText port;
	private EditText type;
 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_new);
		super.onCreate(savedInstanceState);
		FosSdkJNI.SetLogLevel(LogLevel.LEVEL_ALL);
		btcon = (Button)findViewById(R.id.bt_con);
		btcon.setOnClickListener(click);
	}
	
	OnClickListener click=new OnClickListener() 
	{
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub  MainActivity
		Intent it=new Intent(LogPage.this, MainActivity.class);
		
		Bundle bundle = new Bundle();  
		/*字符、字符串、布尔、字节数组、浮点数等等，都可以传*/  
	//	ip =(EditText)findViewById(R.id.ed_ip);
		uid =(EditText)findViewById(R.id.ed_uid);
		ssid =(EditText)findViewById(R.id.ed_name);
		psk =(EditText)findViewById(R.id.ed_pwd);
		port =(EditText)findViewById(R.id.ed_port);
		type = (EditText)findViewById(R.id.ed_type);
		
		bundle.putString("uid", uid.getText().toString());  
		bundle.putString("ssid", ssid.getText().toString());  
		bundle.putString("psk", psk.getText().toString());    
		bundle.putShort("port", Short.parseShort(port.getText().toString()));
		bundle.putInt("type", Integer.parseInt(type.getText().toString()));
		/*把bundle对象assign给Intent*/  
		
		it.putExtras(bundle);  
		startActivity(it);
	}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main , menu);
		return true;
	}
	
}
	
