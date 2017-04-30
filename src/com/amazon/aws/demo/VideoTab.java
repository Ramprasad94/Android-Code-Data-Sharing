package com.amazon.aws.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.amazon.aws.demo.database.DBAdapterSongs;
import com.amazon.aws.demo.database.DBAdapterVideo;
import com.amazon.aws.demo.s3.DBAdapter;
import com.amazon.aws.demo.s3.S3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class VideoTab extends ListActivity{
	
	ArrayList<String>l1=new ArrayList<String>();
	ArrayList<String>l2=new ArrayList<String>();
	ArrayList<String>l3=new ArrayList<String>();
	ArrayList<String>l4=new ArrayList<String>();
	
	String sec=null,min=null;

	ProgressDialog dialog;
	boolean flag=false;
	String usern;
	PublicPrivate pp;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        usern=getIntent().getStringExtra("u");
        System.out.println("userrrrrrrrrrrr nameeeeeeeeeeeeeeee songs tab  "+usern);
        pp=new PublicPrivate();
        dialog = new ProgressDialog(this);
        dialog.setCancelable(true);
        dialog.setMessage("Uploading Video Plz Wait...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
       
        String[] proj = { MediaStore.Video.Media._ID,
        		MediaStore.Video.Media.DATA,
        		MediaStore.Video.Media.DISPLAY_NAME,
        		MediaStore.Video.Media.SIZE,MediaStore.Video.Media.DURATION};
        		Cursor c = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        		proj, null, null, null);
        		           int na=c.getColumnIndex(MediaStore.Video.Media.DATA);
        		           int nam=c.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);
        		           int si=c.getColumnIndex(MediaStore.Video.Media.SIZE);
        		           int du=c.getColumnIndex(MediaStore.Video.Media.DURATION);
        		           if(c.moveToFirst())
        		           {
        		        	   do
        		        	   {
        		        		   String name=c.getString(na);
        		        		   String name2=c.getString(nam);
        		        		   String siz=Integer.toString(c.getInt(si)/1024);
        		        		   int time1=c.getInt(du);
        		        		   int seconds = (int) ((time1 / 1000) % 60);
        		        		   int minutes = (int) ((time1 / 1000) / 60);
        		        		   int hours   = (int) ((time1 / 1000) / 3600);
        		        		   l1.add(name);
        		        		   l2.add(name2);
        		        		   l3.add(siz);
        		        		   if(hours<=0)
        		        		   {
        		        		   l4.add(minutes+":"+seconds);
        		        		   }else
        		        		   {
        		        			   l4.add(hours+":"+minutes+":"+seconds);
        		        		   }
        		        		   
        		        		   
        		        	   }while(c.moveToNext());
        		           }
        
        		           ListView lv=getListView();
        		           lv.setAdapter(new MyThumbnaildapter());
        		           lv.setOnItemClickListener(new OnItemClickListener()
        		           {
        		        	  

							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
							//	Toast.makeText(getBaseContext(), "sszfcsa", 20).show();
								String filename = l1.get(arg2); 
								alertbox("Confirmation !","Are You Sure Do You Want TO Upload ?",filename);
								
								
							}
        		        	   
        		           });
	}
	
	  protected void alertbox(String title,String mymessage,final String imagePath)
	   {
		 final CharSequence[] items = {"private", "public"};
		 
		 AlertDialog.Builder builder = new AlertDialog.Builder(VideoTab.this);
     builder.setTitle(title);
     
     builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
     public void onClick(DialogInterface dialog, int item) {
    	 
    	 final String ssss1[]=imagePath.split("/");
    	 int index1=imagePath.lastIndexOf("/");
			final String str1=imagePath.substring(index1+1);
			

         switch(item)
         {
             case 0:
            	 
                 pp.AddToTable(/*GetbucketName.bucketName+imagePath*/str1,usern, items[item].toString());
                 Toast.makeText(getApplicationContext(), "Uploaded sucessfully", 190).show();
            	 
                      break;
             case 1:
                     // Your code when 2nd  option seletced
            	 //uploading(imagePath);
            	 //Toast.makeText(ImageTab.this, items[item], 90).show();
            	 
            	// uploading(imagePath);
            	 
            	 pp.AddToTable(/*GetbucketName.bucketName+imagePath*/str1,usern, items[item].toString());
            	 Toast.makeText(getApplicationContext(), "Uploaded sucessfully", 190).show();   
            	 break;
               default :
            	   
            	   pp.AddToTable(str1,usern, "public");
            	   break;
             
         }
    
         }
     });
    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
   public void onClick(DialogInterface dialog, int id) {
	
			System.out.println("hellooooooooooooooooooooooooooo "+imagePath);
			
			uploading(imagePath);

   }
});
builder.show();  
	   }

		 
		 

	/* protected void alertbox(String title, String mymessage,final String imagePath)
	   {
	   new AlertDialog.Builder(this)
	      .setMessage(mymessage)
	      .setTitle(title)
	      .setCancelable(true)
	      .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
  public void onClick(DialogInterface dialog, int id) {
       uploading(imagePath);
  }
})
.setNegativeButton("No", new DialogInterface.OnClickListener() {
  public void onClick(DialogInterface dialog, int id) {
       dialog.cancel();
  }
})
	      .show();
	   }*/
							protected void uploading(final String filename) {
							// TODO Auto-generated method stub
		final String ssss[]=filename.split("/");
		String res="";
		 int index=filename.lastIndexOf("/");
			final String str=filename.substring(index+1);
			
		final File f=new File(filename);
		
		 dialog.cancel();
		   dialog.show();
		   final int size=GettingFiles.updateSize1(f);
		  if(size>0){
		   final Thread newThred=new Thread(){
			 	public void run(){    		
			 		 try{
							flag=S3.createObjectForBucket( "AndroidAmazon",GetbucketName.getFoldername()+"/"+str ,f );
                              
                              FileOutputStream fos=openFileOutput("awsincoign2",Context.MODE_PRIVATE);
              				GettingFiles.updateFile(fos,size);
							dialog.dismiss();
							
							//finish();
						} catch(Throwable e){
							e.printStackTrace();
							
						}
						
			        	}
			        };
			        newThred.start();
			        Thread t2=new Thread(){
			        	public void run(){
			        		try {
								newThred.join();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								
							}
							if(flag==true){
								DBAdapterSongs d=new DBAdapterSongs(VideoTab.this,"AWSVideos");
							d.open();
							long i=d.insertTitle(str);
							if(i>0){
								Log.d("DataBase", "Inserted Sucessfuly");
							}
							else{
								Log.d("DataBase", "NOt Inserted Sucessfuly");	
							}
							d.close();
			        	}
							else{
								Intent it=new Intent(VideoTab.this,AlertActivity.class);
								startActivity(it);
							}
			        	}
			        };t2.start();
	
						}else{
							Toast.makeText(getBaseContext(), "No space", Toast.LENGTH_LONG).show();
							
						}
							}
							
	public class MyThumbnaildapter extends BaseAdapter{
		public int getCount() {
			
			return l1.size();
		}

		
		public Object getItem(int position) {
			
			return position;
		}

		
		public long getItemId(int position) {
			
			return position;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if(row==null){
				LayoutInflater inflater=getLayoutInflater();
				row=inflater.inflate(R.layout.row, parent, false);
			}
			TextView textfilePath = (TextView)row.findViewById(R.id.FilePath);
			textfilePath.setText(l2.get(position)+"\n");
			
			TextView size1=(TextView)row.findViewById(R.id.FileSize);
			size1.setText(l3.get(position)+"kb");
			TextView dur=(TextView)row.findViewById(R.id.FileDuration);
			dur.setText(l4.get(position));
			ImageView imageThumbnail = (ImageView)row.findViewById(R.id.Thumbnail);
			imageThumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageThumbnail.setPadding(8, 8, 8, 8);
			
			Bitmap bmThumbnail;
	      /*  bmThumbnail = ThumbnailUtils.createVideoThumbnail(l1.get(position), Thumbnails.MICRO_KIND);
	        imageThumbnail.setImageBitmap(bmThumbnail); */
	        
			return row;
		}

		
	}

	}
	
    
