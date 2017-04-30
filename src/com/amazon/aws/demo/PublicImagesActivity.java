package com.amazon.aws.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.amazon.aws.demo.s3.S3;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PublicImagesActivity extends ListActivity {
	String u_name,status,imagename="";
	ArrayList<String> al = new ArrayList<String>();
	InputStream is;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 u_name=getIntent().getStringExtra("u");
		 status=getIntent().getStringExtra("status");
		 System.out.println("publicimages               "+u_name  +"  status   "+status);
		 PublicPrivate pp = new PublicPrivate();
			List<Others> list = pp.getAllPublicValues(u_name);
			int len = list.size();
			System.out.println("size @@@@@@@@@@@@@" + len);
			for (int i = 0; i < len; i++) {

				Others oo = list.get(i);
				imagename = oo.getName();
				if(imagename.contains("jpg")||imagename.contains("png"))
				{
				al.add(imagename);
				}

				System.out.println("image names       "+oo.getName() + "===========" );
			}
			
			
			ListView lv = getListView();
			lv.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, al));
			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String ite = ((TextView) view).getText().toString();
					/*
					 * Toast.makeText(getApplicationContext(), ite, 30).show();
					 * Intent i=new
					 * Intent(PrivatealActivity.this,LoadingImageActivity
					 * .class); i.putExtra("im_nam", ite); startActivity(i);
					 */
					is = S3.getImageForObject("AndroidAmazon",
							"lizy" + "/"
									+ al.get(position).toString().trim());

					String filepath = "/sdcard/AWS/aws";
					File imgdir = new File(filepath);
					File file = new File(filepath
							+ al.get(position).toString().trim());
					 
					System.out.println("@@@@@@@@@@@@@@@@@@@@"+is);
					
					 if(file.exists()==false){
				    	 String path=imgdir.toString().toLowerCase();
				    	 String name=imgdir.getName().toLowerCase();
				    	 ContentValues values = new ContentValues(7);
				    	   values.put(Images.Media.TITLE, al.get(position).toString().trim());
				    	   values.put(Images.Media.DISPLAY_NAME, al.get(position).toString().trim());
	                        values.put(Images.Media.DATE_TAKEN, new Date().getTime());
				    	   values.put(Images.Media.MIME_TYPE, "image/jpg/png");
				    	   values.put(Images.ImageColumns.BUCKET_ID, path.hashCode());
				    	   values.put(Images.ImageColumns.BUCKET_DISPLAY_NAME, name);
				    	   values.put("_data", filepath + al.get(position).toString().trim());
				    	   ContentResolver contentResolver = getApplicationContext().getContentResolver();
				    	   Uri uri = contentResolver.insert(Images.Media.EXTERNAL_CONTENT_URI, values);
				    	   
				    	   OutputStream outStream = null;
						try {
							outStream = contentResolver.openOutputStream(uri);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Toast.makeText(getApplicationContext(), "Downloaded sucessfully", 30).show();
				    	   byte[] buffer = new byte[1024];
				    	   int count;
				    	   try {
							while ((count = is.read(buffer)) != -1) {
							    if (Thread.interrupted() == true) {
							     String functionName = Thread.currentThread().getStackTrace()[2].getMethodName() + "()";
							     throw new InterruptedException("The function " + functionName + " was interrupted.");
							    }
							    outStream.write(buffer, 0, count);
							   }
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    	  }
	      	}       	
	      			
						
				

			});

		}

	}

	
	
	


