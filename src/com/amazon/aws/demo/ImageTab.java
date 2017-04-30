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
import com.amazon.aws.demo.s3.DBAdapter;
import com.amazon.aws.demo.s3.S3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ImageTab extends Activity {
	
	ArrayList<String> al=new ArrayList<String>();
	int columnIndex=0;
	String pavan=null;
	Cursor c=null;
	 String res="";
	 NetworkInfo info=null;
	ProgressDialog dialog;
	boolean flag=false;
	AlertDialog levelDialog;
	String imagePath,usern;
	PublicPrivate pp;


	
    	public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.cameratab);
            usern=getIntent().getStringExtra("u");
            System.out.println("userrrrrrrrrrrr nameeeeeeeeeeeeeeee  "+usern);
            pp=new PublicPrivate();
            pp.createDomain();
            dialog = new ProgressDialog(this);
            dialog.setCancelable(true);
            dialog.setMessage("Uploading Image Plz Wait...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    
            c = managedQuery( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, // Which columns to return
                    null ,     // Return all rows
                    null,
                    MediaStore.Images.Media._ID);
           int i=c.getColumnIndex(MediaStore.Images.Media.DATA);
           columnIndex = c.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
           
           if(c.moveToFirst())
           {do{
        	   
        	   al.add(c.getString(i));
        	}while(c.moveToNext());
           }
            GridView gridview = (GridView) findViewById(R.id.sdcard);
            gridview.setAdapter(new MyAdapter(this));
            gridview.setOnItemClickListener(new OnItemClickListener()
            {

    			public void onItemClick(AdapterView<?> parent, View v, int position,
    					long id) {
    				
    				String[] projection = {MediaStore.Images.Media.DATA};
    				Cursor cursor = managedQuery( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
    				projection, // Which columns to return
    				null,       // Return all rows
    			
    				null,
    				
    				null);
    				
    				columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    				
    				cursor.moveToPosition(position);
    				
    			
    				 imagePath = cursor.getString(columnIndex);
    			
    			//	uploading(imagePath);
    				
                    
          			
    				alertbox("Confirmation ! Select The  Level?",imagePath);
    			}
            	
            });
            

        }
    	protected void alertbox(String title,final String imagePath)
    	   {
    		 final CharSequence[] items = {"private", "public"};
    		 
    		 AlertDialog.Builder builder = new AlertDialog.Builder(ImageTab.this);
             builder.setTitle(title);
             
             builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int item) {
            	 
            	 final String ssss1[]=imagePath.split("/");
            	 int index1=imagePath.lastIndexOf("/");
      			final String str1=imagePath.substring(index1+1);
      			

                 switch(item)
                 {
                     case 0:
                    	 
                         pp.AddToTable(str1,usern, items[item].toString());

                         Toast.makeText(getApplicationContext(), "Uploaded sucessfully", 190).show();
                              break;
                     case 1:
                             // Your code when 2nd  option seletced
                    	 //uploading(imagePath);
                    	 //Toast.makeText(ImageTab.this, items[item], 90).show();
                    	 
                    	// uploading(imagePath);
                    	 
                    	 pp.AddToTable(str1,usern, items[item].toString());
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

    		 
    		 
    	  /* new AlertDialog.Builder(this)
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
*/    	   }
    	   
        protected void uploading(final String imagePath) {
        	if(haveInternet()){
        	final String ssss[]=imagePath.split("/");
        	 int index=imagePath.lastIndexOf("/");
  			final String str=imagePath.substring(index+1);
  			
  			System.out.println("strrrrrrrrrrrrrrrrrrrrrrrrrrrrrr "+str);
  			
  			// Toast.makeText(ImageTab.this,str, 20).show();
		//	Toast.makeText(getBaseContext(), imagePath, 20).show();
  			 c.close();
			   dialog.cancel();
			   dialog.show();
			   System.out.println("hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii "+imagePath);
			  
			final File f=new File(imagePath);
			Log.d("file", GetbucketName.bucketName+imagePath);
			System.out.println("fileeeeeeeeeeeeeeeee  "+f);

			final int size=GettingFiles.updateSize1(f);
			Log.d("size", Integer.toString(size));
		if(size>0){
			  final Thread newThred=new Thread(){
 	public void run(){
        		
 		 try{
 			
				flag=S3.createObjectForBucket( "AndroidAmazon",GetbucketName.getFoldername()+"/"+str ,f );
			//	insertDB(str);
				System.out.println("str "+str);
				System.out.println("flag for image is-----------"+flag);
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
					DBAdapterSongs d=new DBAdapterSongs(ImageTab.this,"AWSImages");
				d.open();
				long i=d.insertTitle(str);
				if(i>0){
					Log.d("DataBase", "Inserted Sucessfuly");
				}
				else{
					Log.d("DataBase", "NOt Inserted Sucessfuly");	
				}
				d.close();
				}else{
					Intent it=new Intent(ImageTab.this,AlertActivity.class);
					startActivity(it);
				}
        	}
        };t2.start();

		}else{
			Toast.makeText(getBaseContext(), "No space", Toast.LENGTH_LONG).show();
			
		}
        	}
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
		public class MyAdapter extends BaseAdapter {
        	
        	private Context mContext;

    		public MyAdapter(Context c) {
    			// TODO Auto-generated constructor stub
    			mContext = c;
    		}

    		public int getCount() {
    			// TODO Auto-generated method stub
    			return al.size();
    		}

    		public Object getItem(int arg0) {
    			// TODO Auto-generated method stub
    			return al.get(arg0);
    		}

    		public long getItemId(int arg0) {
    			// TODO Auto-generated method stub
    			return arg0;
    		}

    		public View getView(int position, View convertView, ViewGroup parent) {
    			// TODO Auto-generated method stub
    			
    			View grid;
    			 
    			if(convertView==null){
    				grid = new View(mContext);
    				LayoutInflater inflater=getLayoutInflater();
    				grid=inflater.inflate(R.layout.mygrid, parent, false);
    			}else{
    				grid = (View)convertView;
    			}
    			
    			ImageView imageView = (ImageView)grid.findViewById(R.id.imagepart);
    			//TextView textView = (TextView)grid.findViewById(R.id.textpart);
    			Bitmap bm = ShrinkBitmap(al.get(position), 300, 300);
                
    			imageView.setImageBitmap(bm);
    			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setPadding(8, 8, 8, 8);
                

    			return grid;
    		}

    	}
        Bitmap ShrinkBitmap(String file, int width, int height){
      	   
            BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
               bmpFactoryOptions.inJustDecodeBounds = true;
               Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
                
               int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
               int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);
                
               if (heightRatio > 1 || widthRatio > 1)
               {
                if (heightRatio > widthRatio)
                {
                 bmpFactoryOptions.inSampleSize = heightRatio;
                } else {
                 bmpFactoryOptions.inSampleSize = widthRatio; 
                }
               }
                
               bmpFactoryOptions.inJustDecodeBounds = false;
               bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
            return bitmap;
           }

}