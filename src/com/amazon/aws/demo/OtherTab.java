package com.amazon.aws.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.amazon.aws.demo.database.DBAdapterOthers;
import com.amazon.aws.demo.database.DBAdapterSongs;
import com.amazon.aws.demo.s3.DBAdapter;
import com.amazon.aws.demo.s3.S3;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class OtherTab extends ListActivity{
    /** Called when the activity is first created. */
	String extStorageDirectory;
	ArrayList list=new ArrayList();
	ListView lv;
	ProgressDialog dialog;
	boolean flag=false;
	DBAdapterSongs d=new DBAdapterSongs(this,"AWSOthers");
	String usern;
	PublicPrivate pp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
        usern=getIntent().getStringExtra("u");
        pp=new PublicPrivate();
        dialog.setCancelable(true);
        dialog.setMessage("Uploading File Plz Wait...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        extStorageDirectory=Environment.getExternalStorageState().toString();
        
        getListOfFiles("/sdcard/");

       

    }
    private void getListOfFiles(String path) {

        File files = new File(path);

        FileFilter filter = new FileFilter() {

            private final List<String> exts = Arrays.asList("docx", "xml","doc", "csv", "txt","java","apk","pdf");

            public boolean accept(File pathname) {
                String ext;
                String path = pathname.getPath();
                ext = path.substring(path.lastIndexOf(".") + 1);
                return exts.contains(ext);
            }
        };

        final File [] filesFound = files.listFiles(filter);
        final List<String> list = new ArrayList<String>();
        if (filesFound != null && filesFound.length > 0) {
            for (File file : filesFound) {
            	Toast.makeText(this, file.getName(), 20).show();
               list.add(file.getName());
            }
        }

        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, list));
        lv=getListView();
        lv.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
					String fname=lv.getItemAtPosition(arg2).toString();
				String res="";
				alertbox("Confirmation !","Are You Sure Do You Want TO Upload ?",fname);
				
				 
			}
        	
        });
    }
    protected void alertbox(String title,String mymessage,final String imagePath)
	   {
		 final CharSequence[] items = {"private", "public"};
		 
		 AlertDialog.Builder builder = new AlertDialog.Builder(OtherTab.this);
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

		 
		 

  /*  protected void alertbox(String title, String mymessage,final String imagePath)
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
	protected void uploading(final String file) {
		
		System.err.println("otherrrrrrrrrrrrrrrrrrrrrrrrrrrrr-------------");
		// TODO Auto-generated method stub
		String str="/sdcard/"+file;
		
		final String ssss[]=str.split("/");
    	   dialog.cancel();
			   dialog.show();
			final File f=new File(str);
			
			final int size=GettingFiles.updateSize1(f);
		if(size>0){
			  final Thread newThred=new Thread(){
 	public void run(){
        		
 		 try{
      
				flag=S3.createObjectForBucket( GetbucketName.bucketName,GetbucketName.getFoldername()+"/"+file ,f );
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
				d.open();
				long i=d.insertTitle(file);
				if(i>0){
					Log.d("DataBase", "Inserted Sucessfuly");
				}
				else{
					Log.d("DataBase", "NOt Inserted Sucessfuly");	
				}
				d.close();
        	}
				else{
					Intent it=new Intent(OtherTab.this,AlertActivity.class);
					startActivity(it);
				}
        	}
        };t2.start();

			}else{
				Toast.makeText(getBaseContext(), "No space", Toast.LENGTH_LONG).show();
				
			}
		}
		
	
	protected void sendbucket(String res, String string, File f) {
		// TODO Auto-generated method stub
		 try{
				S3.createObjectForBucket( GetbucketName.bucketName,string ,f );
				finish();
			} catch(Throwable e){
				e.printStackTrace();
				
			
			}
	}
}