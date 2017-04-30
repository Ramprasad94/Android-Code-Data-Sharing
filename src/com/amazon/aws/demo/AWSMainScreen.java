package com.amazon.aws.demo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.aws.demo.s3.S3BucketView;
import com.amazonaws.auth.BasicAWSCredentials;

public class AWSMainScreen extends Activity{
	
	Button viewmyfiles,createfolder,upload1file,uploadmultifiles,download1file,downloadmultifiles,help,statics,Contacts;
	InputStream in=null;
	NetworkInfo info=null;
    ProgressDialog dialog;
	String res="";
	boolean ff=true;
	 List<String> list=new ArrayList();	
	 
	 private Handler mHandler;
		private static final String fail = "Load Failed. Please Try Restarting the Application.";
		
		
		public static BasicAWSCredentials credentials = null;
		
		protected Button s3Button;
		
		protected TextView welcomeText;
		
		private boolean credentials_found;
		String username;
		
		
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.awsmainscreen);
		username=getIntent().getStringExtra("cont");
		
		  dialog = new ProgressDialog(this);
	        dialog.setCancelable(true);
	        dialog.setMessage("Getting Files Plz Wait...");
	        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        startGetCredentials();
		
		upload1file=(Button) findViewById(R.id.uploadsinglefile);
		//uploadmultifiles=(Button) findViewById(R.id.uploadmultfiles);
		download1file=(Button) findViewById(R.id.dowloadsiglefile);
		//downloadmultifiles=(Button) findViewById(R.id.downloadmultifiles);
		//help=(Button) findViewById(R.id.Help);
		//statics=(Button) findViewById(R.id.Statics);
		//Contacts=(Button)findViewById(R.id.button1);
		
		
		//ff=verify();
		/*if(ff){*/
		/*help.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent it=new Intent(AWSMainScreen.this,Help.class);
				startActivity(it);	
			}
			
		});
		statics.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
			Intent it=new Intent(AWSMainScreen.this,MainChart.class);
			startActivity(it);
			}
			
		});*/
		upload1file.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				UploadFile();
			}
			
		});
		/*uploadmultifiles.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				uploadmultifiles();
			}
			
		});*/
		
		download1file.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent it=new Intent(AWSMainScreen.this,DataView.class);
				it.putExtra("un", username);
				
	 			startActivity(it);
				
				//downloadsinglefile();
			}
			
		});
		/*Contacts.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it=new Intent(AWSMainScreen.this,MainActivity.class);
				startActivity(it);
			}
		});

		downloadmultifiles.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				downloadMultiFiles();
				
			}
			
		});
		}else{
			Intent it=new Intent(this,ContacUs.class);
		}
*/	}
	

	private boolean verify() {
		Date d=new Date();
		//String mydate="14-Sep-11";
		 DateFormat formatter ; 
		String ss=GetbucketName.getDate();
		
	      formatter = new SimpleDateFormat("dd-MM-yy");
	      try {
			
			Date mydate2=formatter.parse(ss);
			int dd=mydate2.getDate();
			int mm=mydate2.getMonth();
			int yy=mydate2.getYear();
			String ssdf=GettingFiles.verifyDate(dd, mm, yy);
			Log.d("age ad", ssdf);
			
			System.out.print(dd+mm+yy);
		      if(d.compareTo(mydate2)<0){
		      
		         Toast.makeText(getBaseContext(), "Today Date is Lesser than my Date", 20).show();
		         return true;
		      }
		      else if(d.compareTo(mydate2)>0){
			    
			         Toast.makeText(getBaseContext(), "Today Date is Gretter than my Date", 20).show();
			         return false;
			      }
		      else{
		    	 
			         Toast.makeText(getBaseContext(), "Both all equal", 20).show();
			         return true;
			      }
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	
	
	private void startGetCredentials() {
    	Thread t = new Thread() {
    		
    		public void run(){
    	        try {            
    	            Properties properties = new Properties();
    	            properties.load( getClass().getResourceAsStream( "AwsCredentials.properties" ) );
    	            
    	            String accessKeyId = properties.getProperty( "accessKey" );
    	            String secretKey = properties.getProperty( "secretKey" );
    	            
    	            if ( ( accessKeyId == null ) || ( accessKeyId.equals( "" ) ) ||
    	            	 ( accessKeyId.equals( "CHANGEME" ) ) ||( secretKey == null )   || 
    	                 ( secretKey.equals( "" ) ) || ( secretKey.equals( "CHANGEME" ) ) ) {
    	                Log.e( "AWS", "Aws Credentials not configured correctly." );                                    
        	            credentials_found = false;
    	            } else {
    	            credentials = new BasicAWSCredentials( properties.getProperty( "accessKey" ), properties.getProperty( "secretKey" ) );
        	        credentials_found = true;
    	            }

    	        }
    	        catch ( Exception exception ) {
    	            Log.e( "Loading AWS Credentials", exception.getMessage() );
    	            credentials_found = false;
    	        }
    			
    		}
    	};
    	t.start();
    }
    



	protected void downloadMultiFiles() {
		if(haveInternet()){
		dialog.show();
		
		
		  Thread newThred=new Thread(){
			 	public void run(){
			        		
			 		 try{
			 			String bname=GetbucketName.bucketName;
			 			GetbucketName.flag=true;
			 			
			 		//	GettingFiles.getFiles(bname);
			 			
			 			Intent it=new Intent(AWSMainScreen.this,UploadMulFileTab.class);
			 			startActivity(it);
			 			
			 			dialog.dismiss();
						} catch(Throwable e){
							e.printStackTrace();
							
						}
						
			        	}
			        };
			        newThred.start();
	}
		else{
    		Toast.makeText(getBaseContext(), "Network Unavailable", Toast.LENGTH_LONG).show();
			
    	}
	}
	protected void uploadmultifiles() {
		// TODO Auto-generated method stub
		dialog.show();

		  Thread newThred=new Thread(){
			 	public void run(){
			        		
			 		 try{
			 			GetbucketName.flag=false;
			 			//GettingFiles.getMultiImageFiles();
			 			dialog.dismiss();
			 			Intent it=new Intent(AWSMainScreen.this,UploadMulFileTab.class);
			 			startActivity(it);
			 			
						} catch(Throwable e){
							e.printStackTrace();
							
						}
						
			        	}
			        };
			        newThred.start();
	}


	protected void downloadsinglefile() {
		if(haveInternet()){
	dialog.show();
		  Thread newThred=new Thread(){
			 	public void run(){
			        		
			 		 try{
			 			//String bname=GetbucketName.bucketName;
			 		
				 			//GettingFiles.getFiles("shivashiva");
				 			
			 			Intent it=new Intent(AWSMainScreen.this,TabViewDownload.class);
			 			//Intent it=new Intent(AWSMainScreen.this,View.class);
			 			 
			 			startActivity(it);
			 			dialog.dismiss();
						} catch(Throwable e){
							e.printStackTrace();
							
						}
						
			        	}
			        };
			        newThred.start();
	}
		else{
    		Toast.makeText(getBaseContext(), "Network Unavailable", Toast.LENGTH_LONG).show();
			
    	}
	}
	protected void UploadFile() {
		Intent it=new Intent(this,TabMenu.class);
		it.putExtra("un", username);
		startActivity(it);
		
	}

	protected void viewFiles(String res2) {
	
		Intent bucketViewIntent = new Intent(AWSMainScreen.this, S3BucketView.class);
		bucketViewIntent.putExtra( "bucketname", "mynameisshiva" );
		startActivity(bucketViewIntent);
		
		
	}

	
	protected void onDestroy() {
	
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// 
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// 
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// 
		super.onResume();
	}

	@Override
	protected void onStart() {
		// 
		super.onStart();
	}

	@Override
	protected void onStop() {
		// 
		super.onStop();
	}


	@TargetApi(Build.VERSION_CODES.ECLAIR)
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	finish();
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
