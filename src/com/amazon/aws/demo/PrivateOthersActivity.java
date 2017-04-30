package com.amazon.aws.demo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import android.provider.MediaStore.Images;
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

public class PrivateOthersActivity extends ListActivity {
	String u_name, status;
	String imagename = "";
	ArrayList<String> al = new ArrayList<String>();
	Context context;
	String ite;
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

			if(imagename.contains("docx")||imagename.contains("doc")||imagename.contains("txt")||imagename.contains("xls")||imagename.contains("exe")||imagename.contains("pdf")||imagename.contains("html")||imagename.contains("java")||imagename.contains("pdf")||imagename.contains("apk")||imagename.contains("csv"))
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
				
				System.out.println("iteeeeeeeeeeeeeeeee       " + ite
						+ "===========");
				alertbox("Confirmation ! Select The  Level?",ite, position);
				/*
				 * Toast.makeText(getApplicationContext(), ite, 30).show();
				 * Intent i=new
				 * Intent(PrivatealActivity.this,LoadingImageActivity
				 * .class); i.putExtra("im_nam", ite); startActivity(i);
				 */
				
			}		        



});

	}


	protected void alertbox(String title,final String imagePath, final int pos)
	   {
		 final CharSequence[] items = {"delete", "download"};
		 
		 AlertDialog.Builder builder = new AlertDialog.Builder(PrivateOthersActivity.this);
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
              Intent it=new Intent(PrivateOthersActivity.this,TabViewDownload.class);
         	  startActivity(it);
        	   Toast.makeText(getApplicationContext(), "Deleted the file sucessfully", 190).show();
                    break;
           case 1:
         	 
         	  downloadfile(imagePath,pos);
                   break;
             default :
          	   
          	  downloadfile(imagePath,pos);
          	Toast.makeText(getApplicationContext(), "Downloaded the file sucessfully", 190).show();
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
		try{
 			is=S3.getDataForObject(GetbucketName.bucketName,  GetbucketName.getFoldername()+"/"+al.get(posi).trim());
				
				//finish();
			} catch(Throwable e){
				e.printStackTrace();
				
			}
			
        
     
       
        		ByteArrayOutputStream baos = new ByteArrayOutputStream( 8196 );
    			File f=new File("/sdcard/AWS/"+al.get(posi));
    			
    			OutputStream out = null;
				try {
					out = new FileOutputStream(f);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			byte buf[]=new byte[1024];
    			  int len;
    			  try {
					while((len=is.read(buf))>0)
					  out.write(buf,0,len);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			  try {
					out.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    			  try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			  System.out.println("\nFile is created........	...........................");
    			  Toast.makeText(getApplicationContext(), "Downloaded sucessfully", 30).show();
    			byte[] buffer = new byte[1024];
    			int length = 0;
    			try {
					while ( ( length = is.read( buffer ) ) > 0 ) {
						baos.write( buffer, 0, length );
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
	
		
	
	}


