package com.amazon.aws.demo;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class ViewVideo2 extends Activity{
	 ProgressDialog dialog;
	 VideoView vv;
	
     private String filename;
     public void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
          setContentView(R.layout.video);
           System.gc();
             filename = "/sdcard/abcd.3gp";
          	 vv = (VideoView)findViewById(R.id.myvideoview);
           vv.setVideoPath(filename);
           vv.setMediaController(new MediaController(this));
           vv.requestFocus();
           vv.start();
          
           
     }
}
