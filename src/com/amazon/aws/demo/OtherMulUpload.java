package com.amazon.aws.demo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.amazon.aws.demo.database.DBAdapterSongs;
import com.amazon.aws.demo.s3.S3;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

public class OtherMulUpload extends ListActivity {
	String extStorageDirectory;
	private ListView mainListView ;
	ArrayList list=new ArrayList();
	ListView lv;
	ArrayList others=new ArrayList();
	ArrayList<Boolean> flagvalues=new ArrayList<Boolean>();
	DBAdapterSongs d=new DBAdapterSongs(this,"AWSOthers");
	ProgressDialog dialog,dialog2;
	SQLiteDatabase sampleDB = null;
	InputStream is;
    @SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(true);
        dialog.setMessage("Uploading Files Plz Wait...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog2 = new ProgressDialog(this);
        dialog2.setCancelable(true);
        dialog2.setMessage("Downloading Files Plz Wait...");
        dialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        extStorageDirectory=Environment.getExternalStorageState().toString();
        if(list.size()>0)
        	list.removeAll(list);
        if(!GetbucketName.flag){
        
        File files = new File("/sdcard/");

        FileFilter filter = new FileFilter() {

        	 private final List<String> exts = Arrays.asList("docx", "xml",
                     "doc", "csv", "txt","java","apk","pdf");
            public boolean accept(File pathname) {
                String ext;
                String path = pathname.getPath();
                ext = path.substring(path.lastIndexOf(".") + 1);
                return exts.contains(ext);
            }
        };

        final File [] filesFound = files.listFiles(filter);
        final ArrayList<String> list = new ArrayList<String>();
        if (filesFound != null && filesFound.length > 0) {
            for (File file : filesFound) {
            	//Toast.makeText(this, file.getName(), 20).show();
               list.add(file.getName());
            }
            setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, list));

            ListView lv = getListView();
            lv.setTextFilterEnabled(true);
            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            lv.setOnItemClickListener(new OnItemClickListener() {
              public void onItemClick(AdapterView<?> parent, View view,
                  int position, long id) {
            	  checkList(((TextView) view).getText().toString());
              }
            });
        }
        }else{
       
sampleDB =  this.openOrCreateDatabase("AWSDatabase", MODE_PRIVATE, null);
            
            Cursor c = sampleDB.rawQuery("SELECT Name FROM AWSOthers", null);
	    	
			if (c != null ) {
	    		if  (c.moveToFirst()) {
	    			do {
	    				String name = c.getString(c.getColumnIndex("Name"));
	    				list.add(name);
	    				//Toast.makeText(this,name,20).show();
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
              	  checkList(((TextView) view).getText().toString());
                }
              });
        }
    
       
    } protected void checkList(String element) {
    	if(others.contains(element))
    		others.remove(element);
		else{
			Toast.makeText(this, "no", 20).show();
			others.add(element);
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
			
			if(GettingFiles.updateSize(others)>0){
		  final Thread newThred=new Thread(){
        public void run(){
	 try{
		
		 for(int i=0;i<others.size();i++){
			 S3.getDataForObject(GetbucketName.bucketName,  GetbucketName.getFoldername()+"/"+others.get(i).toString().trim());
			 ByteArrayOutputStream baos = new ByteArrayOutputStream( 8196 );
				File f=new File("/sdcard/AWS/aws"+others.get(i));
				
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
				S3.deleteObject(GetbucketName.bucketName,  GetbucketName.getFoldername()+"/"+others.get(i).toString());
				 sampleDB.execSQL("DELETE FROM AWSSongs where Name='"+others.get(i).toString()+"'");
				 FileOutputStream fos=openFileOutput("awsincoign2",Context.MODE_PRIVATE);
					File f2=new File("/sdcard/AWS/aws"+others.get(i));
				 GettingFiles.updateDownoadSize(f2, fos);
				}
		
		 dialog2.dismiss();
		} catch(Throwable e){
			e.printStackTrace();
		}
		finally{
			Intent it=new Intent(OtherMulUpload.this,UploadMulFileTab.class);
			startActivity(it);
		}
		
	}
};
newThred.start();
Thread t1=new Thread(){
	public void run(){
		try {
			newThred.join();
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
	}
};t1.start();
			}
			else {
				Toast.makeText(this, "no Space in your Bucket", 20).show();
			}
	}
	private void downoadFiles() {
		 dialog2.show();
		  Thread newThred=new Thread(){
          public void run(){
	 try{
		
		 for(int i=0;i<others.size();i++){
				S3.getDataForObject2(GetbucketName.bucketName,  GetbucketName.getFoldername()+"/"+others.get(i).toString().trim());
				}
		 dialog2.dismiss();
		} catch(Throwable e){
			e.printStackTrace();
		}
		
		
 	}
 };
 newThred.start();


		
		
	}

    private void uploadDelete() {
    	 dialog.show();
    	 final int remainSize=GettingFiles.updateSize(others);
 		if(remainSize>0){
		  final Thread newThred=new Thread(){
public void run(){
		
	 try{
		
		 for(int i=0;i<others.size();i++){
		 final File f=new File("/sdcard/"+others.get(i).toString());
			flagvalues.add(S3.createObjectForBucket(  GetbucketName.bucketName, GetbucketName.getFoldername()+"/"+others.get(i).toString() ,f ));
			
			
			//finish();
		 }
		 FileOutputStream fos=openFileOutput("awsincoign2",Context.MODE_PRIVATE);
			GettingFiles.updateFile(fos,remainSize);
		 dialog.dismiss();
		} catch(Throwable e){
			e.printStackTrace();
		}
		finally{
			Intent it=new Intent(OtherMulUpload.this,UploadMulFileTab.class);
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
			for(int i=0;i<flagvalues.size();i++){
				Log.d("DataBase", Boolean.toString(flagvalues.get(i)));
			if(flagvalues.get(i)==true){
			d.open();
			long i1=d.insertTitle(others.get(i).toString());
			if(i1>0){
				 final File f=new File("/sdcard/"+others.get(i).toString());
				boolean deleted = f.delete();
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
 		}
 		
 		else {
 			Toast.makeText(this, "no Space in your Bucket", 20).show();
 		}

	}
	
	private void uploadFiles() {
		 dialog.show();
		 final int remainSize=GettingFiles.updateSize2(others);
		if(remainSize>0){
		  final Thread newThred=new Thread(){
public void run(){
 		
	 try{
		
		 for(int i=0;i<others.size();i++){
		 final File f=new File("/sdcard/"+others.get(i).toString());
			flagvalues.add(S3.createObjectForBucket(  GetbucketName.bucketName, GetbucketName.getFoldername()+"/"+others.get(i).toString() ,f ));
			
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
				for(int i=0;i<flagvalues.size();i++){
					Log.d("DataBase", Boolean.toString(flagvalues.get(i)));
				if(flagvalues.get(i)==true){
				d.open();
				long i1=d.insertTitle(others.get(i).toString());
				if(i1>0){
				
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