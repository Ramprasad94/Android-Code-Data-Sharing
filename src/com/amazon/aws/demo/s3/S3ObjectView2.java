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


public class S3ObjectView2 extends Activity{
	
	protected Handler mHandler;
	protected TextView loadingText,body;
	protected ImageView bodyText;
	protected String bucketName;
	protected String objectName;
	protected InputStream objectData;
	String str;
	
	private final Runnable postResults = new Runnable() {
		public void run(){
			updateUi();
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_view2);
        Bundle extras = this.getIntent().getExtras();
        bucketName = extras.getString("bucketname");
        objectName = extras.getString("imagename");
        mHandler = new Handler();
        loadingText = (TextView) findViewById(R.id.item_view_loading_text);
        bodyText = (ImageView) findViewById(R.id.ImageView01);
        body=(TextView) findViewById(R.id.item_view_loading_text2);
        startPopulateText();
    }
    
    private void startPopulateText(){
    	Thread t = new Thread() {
    		@Override
    		public void run(){
    			System.out.println("object Name is "+objectName);
    			str = S3.getDataForObject3(bucketName, objectName);
    			Log.d("sfsdfsdafvesddzx", str);
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
    	body.setText(str);
    	loadingText.setTextSize(16);
    }
  		
}
