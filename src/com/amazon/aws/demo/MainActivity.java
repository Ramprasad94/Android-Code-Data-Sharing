package com.amazon.aws.demo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
Button sync,update,share;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sync=(Button)findViewById(R.id.button1);
		update=(Button)findViewById(R.id.button2);
		//share=(Button)findViewById(R.id.button3);
		sync.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in=new Intent(MainActivity.this,DisplayContacts.class);
				startActivity(in);
				
			}
			
		});
		update.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent second=new Intent(MainActivity.this,SimpleDbList.class);
				startActivity(second);
				
			}
			
		});
				
	}
}
