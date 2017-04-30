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

import java.io.File;

import android.os.Bundle;
import android.os.Handler;

import com.amazon.aws.demo.AlertActivity;

public class S3Upload extends AlertActivity {
	
	
	protected Handler mHandler;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
           
        wireSubmitButton();
    }
	
	public void wireSubmitButton(){
		
				try{
					
					File f2=new File("/mnt/sdcard/hi.txt");
					S3.createObjectForBucket( "MyBucket007", "hi.txt",f2 );
					
					finish();
				} catch(Throwable e){
    				setStackAndPost(e);
				}
			}
		
	}
	
	


