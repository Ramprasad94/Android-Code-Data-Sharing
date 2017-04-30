/*
 * Copyright 2010-2011 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazon.aws.demo.s3;


import java.io.InputStream;
import java.io.UnsupportedEncodingException;



import com.amazon.aws.demo.R;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


public class S3ObjectView extends Activity{
	
	protected Handler mHandler;
	protected TextView loadingText;
	protected ImageView bodyText;
	protected String bucketName;
	protected String objectName;
	protected InputStream objectData;
	
	private final Runnable postResults = new Runnable() {
		public void run(){
			updateUi();
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_view);
        Bundle extras = this.getIntent().getExtras();
        bucketName = extras.getString("bucketname");
        objectName = extras.getString("imagename");
        mHandler = new Handler();
        loadingText = (TextView) findViewById(R.id.item_view_loading_text);
        bodyText = (ImageView) findViewById(R.id.ImageView01);
        startPopulateText();
    }
    
    private void startPopulateText(){
    	Thread t = new Thread() {
    		@Override
    		public void run(){
    			System.out.println("object Name is "+objectName);
    			objectData = S3.getDataForObject(bucketName, objectName);
    	        mHandler.post(postResults);
    		}
    	};
    	t.start();
    }
    
    private void updateUi(){
    	System.out.println("hai pavan");
    	try {
			byte[] byteArray = objectName.getBytes("UTF-16LE");
			System.out.println(byteArray.length);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	loadingText.setText(objectName);
    	Bitmap  myBitmap = BitmapFactory.decodeStream(objectData);
    	
   /* BufferedInputStream  bis=new BufferedInputStream(objectData);
     ByteArrayBuffer baf=new ByteArrayBuffer(50);
     int current=0;
     try {
		while((current=bis.read())!=-1){
			 baf.append((byte)current);
		 }
		  long startTime = System.currentTimeMillis();
		FileOutputStream fout=new FileOutputStream(objectName);
		fout.write(baf.toByteArray());
		fout.close();
		   Log.d("ImageManager", "download ready in"
                   + ((System.currentTimeMillis() - startTime) / 1000)
                   + " sec");

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
     
    	bodyText.setImageBitmap(myBitmap);
    	loadingText.setTextSize(16);
    }
  		
}
