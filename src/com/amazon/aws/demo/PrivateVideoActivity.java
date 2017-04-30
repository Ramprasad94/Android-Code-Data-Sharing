package com.amazon.aws.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.amazon.aws.demo.s3.S3;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.DeleteAttributesRequest;

public class PrivateVideoActivity extends ListActivity {
	String u_name, status;
	String imagename = "",ite;
	ArrayList<String> al = new ArrayList<String>();
	Context context;

	InputStream is;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		u_name = getIntent().getStringExtra("u");
		status = getIntent().getStringExtra("status");
		System.out.println("privateal               " + u_name
				+ "  status   " + status);

		PublicPrivate pp = new PublicPrivate();
		List<Others> list = pp.getAllValues(u_name, status);
		int len = list.size();
		System.out.println("size @@@@@@@@@@@@@" + len);
		for (int i = 0; i < len; i++) {

			Others oo = list.get(i);
			imagename = oo.getName();

			if(imagename.contains("mp4"))
			{
			al.add(imagename);
			}
			System.out.println("image names       " + oo.getName()
					+ "===========");
		}

		ListView lv = getListView();
		lv.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, al));
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			 ite = ((TextView) view).getText().toString();
				alertbox("Confirmation ! Select The  Level?",ite, position);
				
      	}       	
      			
					
			

		});

	}
	protected void alertbox(String title,final String imagePath, final int pos)
	   {
		 final CharSequence[] items = {"delete", "download"};
		 
		 AlertDialog.Builder builder = new AlertDialog.Builder(PrivateVideoActivity.this);
   builder.setTitle(title);
   
   builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
   public void onClick(DialogInterface dialog, int item) {
  	 
  	 final String ssss1[]=imagePath.split("/");
  	 int index1=imagePath.lastIndexOf("/");
			final String str1=imagePath.substring(index1+1);
			

       switch(item)
       {
           case 0:
          	 
              downloadfile1(imagePath,ite);
              Intent it=new Intent(PrivateVideoActivity.this,TabViewDownload.class);
         	  startActivity(it);
        	   Toast.makeText(getApplicationContext(), "Deleted the file sucessfully", 190).show();
          	 
                    break;
           case 1:
         	 
         	  downloadfile(imagePath,pos);
         	  
         	 Toast.makeText(getApplicationContext(), "Downloaded the file sucessfully", 190).show();
                   break;
             default :
          	   
          	  downloadfile(imagePath,pos);
          	   break;
           
       }
  
       }

	private void downloadfile1(String imagePath, String ite) {
		// TODO Auto-generated method stub
		AWSCredentials credentials = new BasicAWSCredentials( "AKIAIVP2GAQDZR5IFIFQ","jSiXRQo9kEdeqxVqrGQfV+TmPuEciQqTs4dz7/4y" );
		AmazonSimpleDBClient sdbClient = new AmazonSimpleDBClient( credentials);
		DeleteAttributesRequest dar = new DeleteAttributesRequest("ImageView", ite);
		sdbClient.deleteAttributes( dar );
	}
   });
  builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
 public void onClick(DialogInterface dialog, int id) {
	
			System.out.println("hellooooooooooooooooooooooooooo "+imagePath);
			
			//downloadfile(imagePath,pos);

 }
});
builder.show();      
	   }

	
	protected void downloadfile(String images, int posi){
		is = S3.getImageForObject("AndroidAmazon",
				"lizy" + "/"
						+ al.get(posi).toString().trim());

		String filepath = "/sdcard/AWSVideo/aws";
		File imgdir = new File(filepath);
		File file = new File(filepath
				+ al.get(posi).toString().trim());
		 
		System.out.println("@@@@@@@@@@@@@@@@@@@@"+file);
		
		 if(file.exists()==false){
	    	 String path=imgdir.toString().toLowerCase();
	    	 String name=imgdir.getName().toLowerCase();
	    	 ContentValues values = new ContentValues();  
	    	 values.put(MediaStore.MediaColumns.DATA,filepath+ al.get(posi).toString().trim());  
	    	 values.put(MediaStore.MediaColumns.TITLE, "exampletitle");  
	    	 values.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");  
	    	 values.put(MediaStore.Video.Media.ARTIST, "cssounds ");  
	    	 values.put(MediaStore.Video.Media.DURATION,new Date().getMinutes());
	    	// values.put(MediaStore.Video.Media.IS_RINGTONE, true);  
	    	// values.put(MediaStore.Video.Media.IS_NOTIFICATION, true);  
	    	// values.put(MediaStore.Audio.Media.IS_ALARM, true);  
	    	// values.put(MediaStore.Audio.Media.IS_MUSIC, false);  
	    	   
	    	 //Insert it into the database  
	    	
	    	 ContentResolver contentResolver = getApplicationContext().getContentResolver();
	    	   Uri uri = contentResolver.insert(Video.Media.EXTERNAL_CONTENT_URI, values);
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
}
