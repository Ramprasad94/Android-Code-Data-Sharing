package com.amazon.aws.demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DataView extends Activity {
	Button b1,b2;
	 ProgressDialog dialog;
	 NetworkInfo info=null;
	PublicPrivate pp;
	String u_name;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);
		
		 u_name=getIntent().getStringExtra("un");
		    System.out.println("dataview              "+u_name);
		 pp=new PublicPrivate();
		b1=(Button)findViewById(R.id.downloadprivatefile);
		b2=(Button)findViewById(R.id.dowloadpublicfile);
		
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String b1text=b1.getText().toString();
				Intent i1=new Intent(DataView.this,TabViewDownload.class);
				i1.putExtra("u", u_name);
				i1.putExtra("status", b1text);
				startActivity(i1);
			
				
			}
		});
		b2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String b2text=b2.getText().toString();
				Intent i1=new Intent(DataView.this,PublicTabViewDownload.class);
				i1.putExtra("u", u_name);
				i1.putExtra("status", b2text);
				startActivity(i1);
				
				
			}
		});
	}
	
	/*public void pri(View v)
	{
		System.out.println("username               "+LoginActivity.usern);
	}
	
	public void pub(View v)
	{
		System.out.println("username               "+LoginActivity.usern);
	}*/
}