package com.amazon.aws.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.amazon.aws.demo.s3.S3;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class ViewVideo extends Activity {
	VideoView vv;
      private String filename;
      ProgressDialog dialog;
      public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
           setContentView(R.layout.video);
         //   System.gc();
            dialog = new ProgressDialog(this);
            dialog.setCancelable(true);
            dialog.setMessage("Loading Song Plz Wait...");
            // set the progress to be horizontal
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            // reset the bar to the default value of 0
          //  dialog.setProgress(0);
          //  dialog.show();
            Bundle b=getIntent().getExtras();
    	    final String name=b.getString("filename");
    	    final String bucketname=b.getString("bucketname");
    	   vv = (VideoView)findViewById(R.id.myvideoview);
      
             setContentView(vv);
            vv.setVideoPath("/sdcard/abcd.3gp");
            vv.setMediaController(new MediaController(this));
            vv.requestFocus();
     //       vv.start();
    	  /*  Thread newThred=new Thread(){
            	public void run(){
            		
            		try{
            			
            			InputStream is=S3.getDataForObject(bucketname, name);
    					  
    					  MediaPlayer mp = new MediaPlayer();
    					   
    					  File temp = new File("/sdcard/abcd.3gp");
    					   
    					//  temp.createNewFile();
    					   
    					  String tempPath = temp.getAbsolutePath();
    					   
    					                                         
    					   
    					  FileOutputStream out = new FileOutputStream(temp);
    					   
    					  byte buf[] = new byte[128];
    					   
    					  do{
    					   
    					     int numread = is.read(buf);
    					   
    					     if(numread <= 0) break;
    					   
    					     out.write(buf,0,numread);
    					  //   progressHandler.sendMessage(progressHandler.obtainMessage());
    					     } while(true);
    					  dialog.dismiss();
    					  vv.start();
    					 	 
    				}
    				catch (Exception e) {
    					// TODO: handle exception
    					e.printStackTrace();
    				}
    				finally{
    					
    				}
            	}
            };
            newThred.start();

          */
           
            
      }
}