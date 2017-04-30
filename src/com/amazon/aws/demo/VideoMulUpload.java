package com.amazon.aws.demo;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.amazon.aws.demo.database.DBAdapterSongs;
import com.amazon.aws.demo.s3.S3;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Video;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class VideoMulUpload extends ListActivity {
	String extStorageDirectory;
	private ListView mainListView ;
	ArrayList list=new ArrayList();
	ListView lv;
	ArrayList video=new ArrayList();
	ArrayList video2=new ArrayList();
	int columnIndex=0;
	ArrayList<String> al=new ArrayList<String>();	
	ArrayList<String> al2=new ArrayList<String>();	
	Cursor c=null;
	ProgressDialog dialog,dialog2;

	ArrayList<Boolean> flagvalues=new ArrayList<Boolean>();
	SQLiteDatabase sampleDB = null;
    @SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(true);
        dialog.setMessage("Uploading Videos Plz Wait...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog2 = new ProgressDialog(this);
        dialog2.setCancelable(true);
        dialog2.setMessage("Downloading Video Plz Wait...");
        dialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        extStorageDirectory=Environment.getExternalStorageState().toString();
     //   setContentView(R.layout.mulimage);
      //  getListOfFiles("/sdcard/");
      
        if(!GetbucketName.flag){
        	if(al.size()>0){
        		al.removeAll(al);
        		al2.removeAll(al2);
        	}
 c = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
             
        	 c = managedQuery( MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                     null, // Which columns to return
                     null ,     // Return all rows
                     null,
                     MediaStore.Images.Media._ID);
            int i=c.getColumnIndex(MediaStore.Images.Media.DATA);
            columnIndex = c.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            
            if(c.moveToFirst())
            {do{
         	   
            	al2.add(c.getString(i));
          	   al.add(c.getString(i).substring(c.getString(i).lastIndexOf("/")+1));
         	}while(c.moveToNext());
            }
            setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, al));

            ListView lv = getListView();
            lv.setTextFilterEnabled(true);
            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            lv.setOnItemClickListener(new OnItemClickListener() {
              public void onItemClick(AdapterView<?> parent, View view,
                  int position, long id) {
            	  checkList(al.get(position).toString(),al2.get(position).toString());
              }
            });
        
        }else{
        	if(list.size()>0)
        		list.removeAll(list);
        	sampleDB =  this.openOrCreateDatabase("AWSDatabase", MODE_PRIVATE, null);
            
            Cursor c = sampleDB.rawQuery("SELECT Name FROM AWSVideos", null);
	    	
			if (c != null ) {
	    		if  (c.moveToFirst()) {
	    			do {
	    				String name = c.getString(c.getColumnIndex("Name"));
	    				list.add(name);
	    			//	Toast.makeText(this,name,20).show();
	    				Log.d("flag2", name);
	    			}while (c.moveToNext());
	    		} };
        	  setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, list));

              ListView lv = getListView();
              lv.setTextFilterEnabled(true);
              lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
              lv.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
              	  checkList2(((TextView) view).getText().toString());
                }
              });
        }
    
    
    }
    protected void checkList2(String element) {
    	if(video.contains(element))
    		video.remove(element);
		else{
			Toast.makeText(this, "no", 20).show();
			video.add(element);
		}
	}
    protected void checkList(String element,String element2) {
		// TODO Auto-generated method stub
		if(video.contains(element)){
			video.remove(element);
			video2.remove(element2);
		}
		else{
		//	Toast.makeText(this, "no", 20).show();
			video.add(element);
			video2.add(element2);
		}
		
	}
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
       if(GetbucketName.flag)
        inflater.inflate(R.menu.menu2, menu);
       else
    	   inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.new_game:
        	uploadFiles();
            return true;
        case R.id.help:
        	uploadDelete();
            return true;
        case R.id.download:
        	downoadFiles();
            return true;
        case R.id.downloaddelete:
        	downloadDelete();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	private void downloadDelete() {
		 dialog2.show();
		 FileOutputStream fos = null;
			try {
				fos=openFileOutput("coignBucketName2",Context.MODE_PRIVATE);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		  Thread newThred=new Thread(){
        public void run(){
	 try{
		
		 for(int i=0;i<video.size();i++){
			// S3.getDataForObject2(GetbucketName.bucketName, video.get(i).toString().trim());
			
				InputStream is=S3.getImageForObject(GetbucketName.bucketName,  GetbucketName.getFoldername()+"/"+video.get(i).toString().trim());
				
			     String filepath="/sdcard/AWSVideo/aws";
			     File imgdir=new File(filepath);
			     File file=new File(filepath+video.get(i).toString().trim());
			     if(file.exists()==false){
			    	 String path=imgdir.toString().toLowerCase();
			    	 String name=imgdir.getName().toLowerCase();
			    	// File k = new File(path, filename);  
			    	  
			    	 ContentValues values = new ContentValues();  
			    	 values.put(MediaStore.MediaColumns.DATA,filepath+ video.get(i).toString().trim());  
			    	 values.put(MediaStore.MediaColumns.TITLE, "exampletitle");  
			    	 values.put(MediaStore.MediaColumns.MIME_TYPE, "video/"+video.get(i).toString().substring(video.get(i).toString().lastIndexOf("."))+"");  
			    	 values.put(MediaStore.Video.Media.ARTIST, "cssounds ");  
			    	 values.put(MediaStore.Video.Media.DURATION,new Date().getMinutes());
			    	// values.put(MediaStore.Video.Media.IS_RINGTONE, true);  
			    	// values.put(MediaStore.Video.Media.IS_NOTIFICATION, true);  
			    	// values.put(MediaStore.Audio.Media.IS_ALARM, true);  
			    	// values.put(MediaStore.Audio.Media.IS_MUSIC, false);  
			    	   
			    	 //Insert it into the database  
			    	
			    	 ContentResolver contentResolver = getApplicationContext().getContentResolver();
			    	   Uri uri = contentResolver.insert(Video.Media.EXTERNAL_CONTENT_URI, values);
			    	   OutputStream outStream = contentResolver.openOutputStream(uri);
			    	   //OutputStream outStream = contentResolver.openOutputStream(uri);
			    	   byte[] buffer = new byte[1024];
			    	   int count;
			    	   while ((count = is.read(buffer)) != -1) {
			    	    if (Thread.interrupted() == true) {
			    	     String functionName = Thread.currentThread().getStackTrace()[2].getMethodName() + "()";
			    	     throw new InterruptedException("The function " + functionName + " was interrupted.");
			    	    }
			    	    outStream.write(buffer, 0, count);
			    	   }
			    	   S3.deleteObject(GetbucketName.bucketName,  GetbucketName.getFoldername()+"/"+video.get(i).toString());
			    	   sampleDB.execSQL("DELETE FROM AWSSongs where Name='"+video.get(i).toString()+"'");
			    	  }
			//	GettingFiles.objectNameList.remove(video.get(i).toString());
			//	GettingFiles.audioNameList.remove(video.get(i).toString());
			     FileOutputStream fos=openFileOutput("awsincoign2",Context.MODE_PRIVATE);
					File f=new File("/sdcard/AWS/aws"+video.get(i));
				 GettingFiles.updateDownoadSize(f, fos);
				}
		 
		 dialog2.dismiss();
		} catch(Throwable e){
			e.printStackTrace();
		}
		finally{
			Intent it=new Intent(VideoMulUpload.this,UploadMulFileTab.class);
			startActivity(it);
		}
		
	}
};
newThred.start();
			
	}
	private void downoadFiles() {
		 dialog2.show();
		  Thread newThred=new Thread(){
          public void run(){
	 try{
		
		 for(int i=0;i<video.size();i++){
				//S3.getDataForObject2(GetbucketName.bucketName, video.get(i).toString().trim());
				InputStream is=S3.getImageForObject(GetbucketName.bucketName,  GetbucketName.getFoldername()+"/"+video.get(i).toString().trim());
				S3.deleteObject(GetbucketName.bucketName, video.get(i).toString());
			     String filepath="/sdcard/AWSVideo/aws";
			     File imgdir=new File(filepath);
			     File file=new File(filepath+video.get(i).toString().trim());
			     if(file.exists()==false){
			    	 String path=imgdir.toString().toLowerCase();
			    	 String name=imgdir.getName().toLowerCase();
			    	// File k = new File(path, filename);  
			    	  
			    	 ContentValues values = new ContentValues();  
			    	 values.put(MediaStore.MediaColumns.DATA,filepath+ video.get(i).toString().trim());  
			    	 values.put(MediaStore.MediaColumns.TITLE, "exampletitle");  
			    	 values.put(MediaStore.MediaColumns.MIME_TYPE, "video/"+video.get(i).toString().substring(video.get(i).toString().lastIndexOf("."))+"");  
			    	 values.put(MediaStore.Video.Media.ARTIST, "cssounds ");  
			    	 values.put(MediaStore.Video.Media.DURATION,new Date().getMinutes());
			    	// values.put(MediaStore.Video.Media.IS_RINGTONE, true);  
			    	// values.put(MediaStore.Video.Media.IS_NOTIFICATION, true);  
			    	// values.put(MediaStore.Audio.Media.IS_ALARM, true);  
			    	// values.put(MediaStore.Audio.Media.IS_MUSIC, false);  
			    	   
			    	 //Insert it into the database  
			    	
			    	 ContentResolver contentResolver = getApplicationContext().getContentResolver();
			    	   Uri uri = contentResolver.insert(Video.Media.EXTERNAL_CONTENT_URI, values);
			    	   OutputStream outStream = contentResolver.openOutputStream(uri);
			    	   //OutputStream outStream = contentResolver.openOutputStream(uri);
			    	   byte[] buffer = new byte[1024];
			    	   int count;
			    	   while ((count = is.read(buffer)) != -1) {
			    	    if (Thread.interrupted() == true) {
			    	     String functionName = Thread.currentThread().getStackTrace()[2].getMethodName() + "()";
			    	     throw new InterruptedException("The function " + functionName + " was interrupted.");
			    	    }
			    	    outStream.write(buffer, 0, count);
			    	   }
			    	  }
				}
		 dialog2.dismiss();
		} catch(Throwable e){
			e.printStackTrace();
		}
		
		
 	}
 };
 newThred.start();


		
		
	}

    private void uploadFiles() {
		 dialog.show();
		 final int remainSize=GettingFiles.updateSize(video2);
			if(remainSize>0){
		  final Thread newThred=new Thread(){
public void run(){
 		
	 try{
		
		 for(int i=0;i<video.size();i++){
			 final File f=new File(video2.get(i).toString());
				flagvalues.add(S3.createObjectForBucket( "AndroidAmazon", GetbucketName.getFoldername()+"/"+video.get(i).toString() ,f ));
				 int size=GettingFiles.updateSize1(f);
				 FileOutputStream fos=openFileOutput("awsincoign2",Context.MODE_PRIVATE);
				GettingFiles.updateFile(fos,size);
			//finish();
		 }
		
		 dialog.dismiss();
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
				DBAdapterSongs d=new DBAdapterSongs(VideoMulUpload.this,"AWSVideos");
				for(int i=0;i<flagvalues.size();i++){
					Log.d("DataBase", Boolean.toString(flagvalues.get(i)));
				if(flagvalues.get(i)==true){
				d.open();
				long i1=d.insertTitle(video.get(i).toString());
				if(i1>0){
					Log.d("DataBase", "Inserted Sucessfuly");
				}
				else{
					Log.d("DataBase", "NOt Inserted Sucessfuly");	
				}
				d.close();
				}
	  	}
				flagvalues.retainAll(flagvalues);
	  	}
	  };t2.start();		
	
			}
			else {
				Toast.makeText(this, "no Space in your Bucket", 20).show();
			}
			

	}

    private void uploadDelete() {
    	 dialog.show();
    	 final int remainSize=GettingFiles.updateSize(video2);
    		if(remainSize>0){
		  final Thread newThred=new Thread(){
public void run(){
		
	 try{
		
		 for(int i=0;i<video.size();i++){
		 final File f=new File(video2.get(i).toString());
			flagvalues.add(S3.createObjectForBucket( GetbucketName.bucketName, GetbucketName.getFoldername()+"/"+video.get(i).toString() ,f ));
			 int size=GettingFiles.updateSize1(f);
			 FileOutputStream fos=openFileOutput("awsincoign2",Context.MODE_PRIVATE);
			GettingFiles.updateFile(fos,size);
		 }
		
		 
		 dialog.dismiss();
		} catch(Throwable e){
			e.printStackTrace();
		}
		finally{
			Intent it=new Intent(VideoMulUpload.this,UploadMulFileTab.class);
			startActivity(it);
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
			DBAdapterSongs d=new DBAdapterSongs(VideoMulUpload.this,"AWSVideos");
			for(int i=0;i<flagvalues.size();i++){
				Log.d("DataBase", Boolean.toString(flagvalues.get(i)));
			if(flagvalues.get(i)==true){
			d.open();
			long i1=d.insertTitle(video.get(i).toString());
			if(i1>0){
				
				 getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,Media.DISPLAY_NAME+"=?",new String[] {video.get(i).toString()});
					
				Log.d("DataBase", "Inserted Sucessfuly");
			}
			else{
				Log.d("DataBase", "NOt Inserted Sucessfuly");	
			}
			d.close();
			}
			flagvalues.retainAll(flagvalues);
  	}
  	}
  };t2.start();
	}else{
		Toast.makeText(getBaseContext(), "No space", Toast.LENGTH_LONG).show();
		
	}
    }
  }