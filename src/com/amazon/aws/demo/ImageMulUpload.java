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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Contacts.People;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;



public class ImageMulUpload extends ListActivity {
	String extStorageDirectory;
	private ListView mainListView ;
	ArrayList list=new ArrayList();
	ListView lv;
    ArrayList images=new ArrayList();
    ArrayList images2=new ArrayList();
	ProgressDialog dialog,dialog2;
	SQLiteDatabase sampleDB = null;
	//private final String SAMPLE_DB_NAME = "MyAWS";
	//private final String SAMPLE_TABLE_NAME = "MyImages";
	int columnIndex=0;
	 NetworkInfo info=null;
	ArrayList<String> al=new ArrayList<String>();	
	ArrayList<String> al2=new ArrayList<String>();	
	Cursor c=null;
	ArrayList<Boolean> flagvalues=new ArrayList<Boolean>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(true);
        dialog.setMessage("Uploading Images Plz Wait...");
        dialog2 = new ProgressDialog(this);
        dialog2.setCancelable(true);
        dialog2.setMessage("Downloading Images Plz Wait...");
        dialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        extStorageDirectory=Environment.getExternalStorageState().toString();
        
       
        
        
           if(!GetbucketName.flag){
        	if(al.size()>0){
        		al.removeAll(al);
        		al2.removeAll(al2);
        	}
        	   c = managedQuery( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                       null, // Which columns to return
                       null ,     // Return all rows
                       null,
                       MediaStore.Images.Media._ID);
              int i=c.getColumnIndex(MediaStore.Images.Media.DATA);
              columnIndex = c.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
          //    Toast.makeText(getBaseContext(), c.toString(), 20).show();
              if(c.moveToFirst())
              {do{
           	   al2.add(c.getString(i));
           	   al.add(c.getString(i).substring(c.getString(i).lastIndexOf("/")+1));
           	}while(c.moveToNext());
              c.close();
            //  Toast.makeText(getBaseContext(), c.toString(), 20).show();
              }
        
            setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, al));

            ListView lv = getListView();
            lv.setTextFilterEnabled(true);
            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            lv.setOnItemClickListener(new OnItemClickListener() {
              public void onItemClick(AdapterView<?> parent, View view,
                  int position, long id) {
            	  checkList(al.get(position).toString(),al2.get(position).toString());
            	 // Toast.makeText(getBaseContext(), al.get(position).toString(), 20).show();
              }
            });
        
        }else{
        	if(list.size()>0)
        		list.removeAll(list);
        	 sampleDB =  this.openOrCreateDatabase("AWSDatabase", MODE_PRIVATE, null);
             
             Cursor c = sampleDB.rawQuery("SELECT Name FROM AWSImages", null);
 	    	
 			if (c != null ) {
 	    		if  (c.moveToFirst()) {
 	    			do {
 	    				String name = c.getString(c.getColumnIndex("Name"));
 	    				list.add(name);
 	    				Toast.makeText(this,name,20).show();
 	    				Log.d("flag2", name);
 	    			}while (c.moveToNext());
 	    		} }
        	//list=GettingFiles.imageNameList;
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
		// TODO Auto-generated method stub
		if(images.contains(element))
			images.remove(element);
		
		else{
			Toast.makeText(this, "no", 20).show();
			images.add(element);
		}
		
	}
    protected void checkList(String element,String element2) {
		

		if(images.contains(element)){
			//Toast.makeText(this, "yes", 20).show();
			images.remove(element);
			images2.remove(element2);
		}
		else{
			Toast.makeText(this, "no", 20).show();
			images.add(element);
			images2.add(element2);
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
		Toast.makeText(this, "downloading and deleting", 20).show();
		if(haveInternet()){
		 dialog2.show();
		
		  Thread newThred=new Thread(){
        public void run(){
	 try{
		
		 for(int i=0;i<images.size();i++){
			
		     
		  	InputStream is=S3.getImageForObject(GetbucketName.bucketName, GetbucketName.getFoldername()+"/"+images.get(i).toString().trim());
		  	
		     String filepath="/sdcard/AWS/aws";
		     File imgdir=new File(filepath);
		     File file=new File(filepath+images.get(i).toString().trim());
		     if(file.exists()==false){
		    	 String path=imgdir.toString().toLowerCase();
		    	 String name=imgdir.getName().toLowerCase();
		    	 
		    	 
		    	 ContentValues values = new ContentValues(7);
		    	   values.put(Images.Media.TITLE, images.get(i).toString().trim());
		    	   values.put(Images.Media.DISPLAY_NAME, images.get(i).toString().trim());
		    	   values.put(Images.Media.DATE_TAKEN, new Date().getTime());
		    	   values.put(Images.Media.MIME_TYPE, "image/jpeg");
		    	   values.put(Images.ImageColumns.BUCKET_ID, path.hashCode());
		    	   values.put(Images.ImageColumns.BUCKET_DISPLAY_NAME, name);
		    	   
		    	   
		    	   values.put("_data", filepath + images.get(i).toString().trim());
		    	   
		    	   
		    	   ContentResolver contentResolver = getApplicationContext().getContentResolver();
		    	   Uri uri = contentResolver.insert(Images.Media.EXTERNAL_CONTENT_URI, values);
		    	   OutputStream outStream = contentResolver.openOutputStream(uri);
		    	   
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
		     S3.deleteObject(GetbucketName.bucketName, images.get(i).toString());
		     sampleDB.execSQL("DELETE FROM AWSImages where Name='"+images.get(i).toString()+"'");
		     FileOutputStream fos=openFileOutput("awsincoign2",Context.MODE_PRIVATE);
				File f=new File("/sdcard/AWS/aws"+images.get(i));
			 GettingFiles.updateDownoadSize(f, fos);
				}
		
		 dialog2.dismiss();
		} catch(Throwable e){
			e.printStackTrace();
		}
		finally{
			Intent it=new Intent(ImageMulUpload.this,UploadMulFileTab.class);
			startActivity(it);
		}
		
	}
};
newThred.start();
		}
		else{
    		Toast.makeText(getBaseContext(), "Network Unavailable", Toast.LENGTH_LONG).show();
			
    	}
	}
	private void downoadFiles() {
		if(haveInternet()){
		 dialog2.show();
		  Thread newThred=new Thread(){
          public void run(){
	 try{
		
		 for(int i=0;i<images.size();i++){
				//S3.getDataForObject2(GetbucketName.bucketName, images.get(i).toString().trim());
				InputStream is=S3.getImageForObject(GetbucketName.bucketName, GetbucketName.getFoldername()+"/"+images.get(i).toString().trim());
			     String filepath="/sdcard/AWS/aws";
			     File imgdir=new File(filepath);
			     File file=new File(filepath+images.get(i).toString().trim());
			     if(file.exists()==false){
			    	 String path=imgdir.toString().toLowerCase();
			    	 String name=imgdir.getName().toLowerCase();
			    	 ContentValues values = new ContentValues(7);
			    	   values.put(Images.Media.TITLE, images.get(i).toString().trim());
			    	   values.put(Images.Media.DISPLAY_NAME, images.get(i).toString().trim());
			    	   values.put(Images.Media.DATE_TAKEN, new Date().getTime());
			    	   values.put(Images.Media.MIME_TYPE, "image/jpeg");
			    	   values.put(Images.ImageColumns.BUCKET_ID, path.hashCode());
			    	   values.put(Images.ImageColumns.BUCKET_DISPLAY_NAME, name);
			    	   values.put("_data", filepath + images.get(i).toString().trim());
			    	   ContentResolver contentResolver = getApplicationContext().getContentResolver();
			    	   
			    	   Uri uri = contentResolver.insert(Images.Media.EXTERNAL_CONTENT_URI, values);
			    	   OutputStream outStream = contentResolver.openOutputStream(uri);
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
		else{
    		Toast.makeText(getBaseContext(), "Network Unavailable", Toast.LENGTH_LONG).show();
			
    	}
		
	}
	private void uploadFiles() {
		if(haveInternet()){
		 dialog.show();
		 
	final int remainSize=GettingFiles.updateSize(images2);
	Log.d("size", Integer.toString(remainSize));
	if(remainSize>0){
		  final Thread newThred=new Thread(){
public void run(){
  		
	 try{
		
		 for(int i=0;i<images.size();i++){
		  File f=new File(images2.get(i).toString());
			flagvalues.add(S3.createObjectForBucket("AndroidAmazon",GetbucketName.getFoldername()+"/"+images.get(i).toString() ,f ));
			 int size=GettingFiles.updateSize1(f);
			 FileOutputStream fos=openFileOutput("awsincoign2",Context.MODE_PRIVATE);
			GettingFiles.updateFile(fos,size);
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
			DBAdapterSongs d=new DBAdapterSongs(ImageMulUpload.this,"AWSImages");
			for(int i=0;i<flagvalues.size();i++){
				Log.d("DataBase", Boolean.toString(flagvalues.get(i)));
			if(flagvalues.get(i)==true){
			d.open();
			long i1=d.insertTitle(images.get(i).toString());
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
	else{
		Toast.makeText(getBaseContext(), "No space", Toast.LENGTH_LONG).show();
		
	}
		}
  else{
		Toast.makeText(getBaseContext(), "Network Unavailable", Toast.LENGTH_LONG).show();
		
	}
	
	
	}
    private void uploadDelete() {
    	if(haveInternet()){
   	 dialog.show();
   	final int remainSize=GettingFiles.updateSize(images2);
   	
   	final ArrayList<Boolean> flag2=new ArrayList<Boolean>();
   	Log.d("ssss", Integer.toString(remainSize));
   	
   	Toast.makeText(getBaseContext(),  Integer.toString(remainSize), 20).show();
	if(remainSize>0){
		  final Thread newThred=new Thread(){
public void run(){
		
	 try{
		 
		 for(int i=0;i<images.size();i++){
		  File f=new File(images2.get(i).toString());
		// boolean flag=false;
		//Toast.makeText(getBaseContext(), images2.get(i).toString(), 20).show();
			flagvalues.add(S3.createObjectForBucket(  "AndroidAmazon",GetbucketName.getFoldername()+"/"+images.get(i).toString() ,f ));
			//flag2.add(flag);
			//f.delete();
			//Uri uri = People.CONTENT_URI;
			 int size=GettingFiles.updateSize1(f);
			 FileOutputStream fos=openFileOutput("awsincoign2",Context.MODE_PRIVATE);
			GettingFiles.updateFile(fos,size);
			//finish();
		
		 }
			 
		 dialog.dismiss();
		 
		
		} catch(Throwable e){
			e.printStackTrace();
		}
		finally{
			Intent it=new Intent(ImageMulUpload.this,UploadMulFileTab.class);
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
			DBAdapterSongs d=new DBAdapterSongs(ImageMulUpload.this,"AWSImages");
			for(int i=0;i<flagvalues.size();i++){
				Log.d("DataBase", Boolean.toString(flagvalues.get(i)));
			if(flagvalues.get(i)==true){
			d.open();
			long i1=d.insertTitle(images.get(i).toString());
			if(i1>0){
				 getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,Media.DISPLAY_NAME+"=?",new String[] {images.get(i).toString()});
					
				Log.d("DataBase", "Inserted Sucessfuly in upanddelllllllll");
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
	}
	else {
		Toast.makeText(this, "no Space in your Bucket", 20).show();
	}
	}
    	//--------------------------------------------
    	else{
    		Toast.makeText(getBaseContext(), "Network Unavailable", Toast.LENGTH_LONG).show();
			
    	}
    }
    private boolean haveInternet(){
        info= ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
       if (info==null || !info.isConnected()) {
               return false;
       }
       else
       return true;
}	
    
	}
  