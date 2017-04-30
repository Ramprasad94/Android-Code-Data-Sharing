package com.amazon.aws.demo;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazon.aws.demo.s3.S3;

public class AWSMain extends Activity {
	EditText folderName;
	Button createFolder;
  List<String> list=new ArrayList();	
  NetworkInfo info=null;
ProgressDialog dialog;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.awsmain);
		 dialog = new ProgressDialog(this);
	        dialog.setCancelable(true);
	        dialog.setMessage("Creating Bucket Plz Wait...");
	        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		folderName=(EditText) findViewById(R.id.folder);
		createFolder=(Button) findViewById(R.id.createbutton);
		
		
		createFolder.setOnClickListener(new OnClickListener(){

			@SuppressWarnings("static-access")
			public void onClick(View v) {
				
						dialog.show();
				
				if(haveInternet()){
				 list=S3.getObjectNamesForBucket("donotdelete");
				 for(int i=0;i<list.size();i++){
					 Log.d("asadsadsa", list.get(i).toString());
				 }
				 if(list.contains(folderName.getText().toString()+"/")){
					 dialog.dismiss();
					 Toast.makeText(getBaseContext(), "Bucket Name Alreay Exist", 20).show();
				 }else{
				Thread t=new Thread(){
					public void run(){
						 try{
							
							
								//Toast.makeText(getBaseContext(), folderName.getText().toString(), 20).show();
								 String bucktname=folderName.getText().toString();
								   S3.createFolder(bucktname);
								   //S3.createFolder2(bucktname);
									GetbucketName.setFoldername(bucktname);
									
									//String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
									
									Date d=new Date();
									int day=d.getDate();
									int mnth=d.getMonth()+1;
									
									int yy=1900+d.getYear()+1;
									String sstr=Integer.toString(day)+"-"+Integer.toString(mnth)+"-"+Integer.toString(yy);
									Log.d("date", sstr);
									String str=bucktname+"~"+sstr+"~"+"10820480";
									FileOutputStream fos=openFileOutput("awsincoign2",Context.MODE_PRIVATE);
									fos.write(str.getBytes());
									fos.close();
									Intent it=new Intent(AWSMain.this,AWSMainScreen.class);
									startActivity(it);
									finish();
							
								 
								} catch(Throwable e){
									e.printStackTrace();
								}
								
								
					}
				};t.start();
				
				 }
				 
				}
				else{
					Toast.makeText(getBaseContext(), "Network Unavailable", Toast.LENGTH_LONG).show();
				}
				dialog.dismiss();
			}

		
			
		});
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	 private boolean haveInternet(){
         info= ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info==null || !info.isConnected()) {
                return false;
        }
        else
        return true;
}	
}
