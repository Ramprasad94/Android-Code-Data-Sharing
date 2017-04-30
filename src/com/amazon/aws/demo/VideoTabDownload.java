package com.amazon.aws.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import com.amazon.aws.demo.SongsTabDownload.EfficientAdapter;
import com.amazon.aws.demo.SongsTabDownload.EfficientAdapter.ViewHolder;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class VideoTabDownload extends ListActivity {
	ArrayList<String> al=new ArrayList<String>();
	 public static ArrayList<String> videoNameList=new ArrayList<String>();
	 //private EfficientAdapter adap;
	 //private static String[] data = new String[] { "0", "1", "2", "3", "4" };
	 int columnIndex=0;
		String pavan=null;
		Cursor c=null;
		static InputStream is=null;
		 String res="";
		static ProgressDialog dialog,dialog2;
		 private EfficientAdapter adap;
		 SQLiteDatabase sampleDB = null;
		// private static String[] data = new String[] { "0", "1", "2", "3", "4" };
		 static String[] data;
		 //public static String TitleString[];
	    	public void onCreate(Bundle savedInstanceState) {
	            super.onCreate(savedInstanceState);
	          //  videoNameList=GettingFiles.videoNameList;
	            requestWindowFeature(Window.FEATURE_NO_TITLE);
	            setContentView(R.layout.imagedownload);
	          
	         //  setContentView(R.layout.main);
	           if (videoNameList.size()>0) {
	        	   videoNameList.removeAll(videoNameList);
				
			}
 sampleDB =  this.openOrCreateDatabase("AWSDatabase", MODE_PRIVATE, null);
	            
	            Cursor c = sampleDB.rawQuery("SELECT Name FROM AWSVideos", null);
		    	
				if (c != null ) {
		    		if  (c.moveToFirst()) {
		    			do {
		    				String name = c.getString(c.getColumnIndex("Name"));
		    				videoNameList.add(name);
		    				//Toast.makeText(this,name,20).show();
		    				Log.d("flag2", name);
		    			}while (c.moveToNext());
		    		} }
				 data=new String[videoNameList.size()];
		           videoNameList.toArray(data);
		           adap = new EfficientAdapter(this);
		            setListAdapter(adap);
	    	}
	    	

	    	  @Override
	    	  protected void onListItemClick(ListView l, View v, int position, long id) {
	    	    // TODO Auto-generated method stub
	    	    super.onListItemClick(l, v, position, id);
	    	    Toast.makeText(this, "Click-" + String.valueOf(position), Toast.LENGTH_SHORT).show();
	    	  }

	    	  public static class EfficientAdapter extends BaseAdapter implements Filterable {
	    	    private LayoutInflater mInflater;
	    	    private Bitmap mIcon1;
	    	    private Context context;

	    	    public EfficientAdapter(Context context) {
	    	      // Cache the LayoutInflate to avoid asking for a new one each time.
	    	      mInflater = LayoutInflater.from(context);
	    	      this.context = context;
	    	    }

	    	    /**
	    	     * Make a view to hold each row.
	    	     * 
	    	     * @see android.widget.ListAdapter#getView(int, android.view.View,
	    	     *      android.view.ViewGroup)
	    	     */
	    	    public View getView(final int position, View convertView, ViewGroup parent) {
	    	      // A ViewHolder keeps references to children views to avoid
	    	      // unneccessary calls
	    	      // to findViewById() on each row.
	    	      ViewHolder holder;

	    	      // When convertView is not null, we can reuse it directly, there is
	    	      // no need
	    	      // to reinflate it. We only inflate a new View when the convertView
	    	      // supplied
	    	      // by ListView is null.
	    	      if (convertView == null) {
	    	        convertView = mInflater.inflate(R.layout.adaptor_content, null);

	    	        // Creates a ViewHolder and store references to the two children
	    	        // views
	    	        // we want to bind data to.
	    	        holder = new ViewHolder();
	    	        holder.textLine = (TextView) convertView.findViewById(R.id.textLine);
	    	      //  holder.iconLine = (ImageView) convertView.findViewById(R.id.iconLine);
	    	        holder.buttonLine = (Button) convertView.findViewById(R.id.buttonLine);
	    	        holder.buttonline2=(Button)convertView.findViewById(R.id.buttonLine2);
	    	        
	    	        
	    	        convertView.setOnClickListener(new OnClickListener() {
	    	          private int pos = position;

	    	       
	    	          public void onClick(View v) {
	    	            Toast.makeText(context, "Click-" + String.valueOf(pos), Toast.LENGTH_SHORT).show();    
	    	          }
	    	        });

	    	        holder.buttonLine.setOnClickListener(new OnClickListener() {
	    	          private int pos = position;

	    	          
	    	          public void onClick(View v) {
	    	           // Toast.makeText(context, "view-" + videoNameList.get(position).toString(), Toast.LENGTH_SHORT).show();
	    	            viewVideo(  GetbucketName.getFoldername()+"/"+videoNameList.get(position).toString());

	    	          }
	    	        });
	    	        
	    	        holder.buttonline2.setOnClickListener(new OnClickListener(){
	    	        	   private int pos = position;
						public void onClick(View v) {
							dialog = new ProgressDialog(context);
				            dialog.setCancelable(true);
				            dialog.setMessage("Downloading Videos Plz Wait...");
				            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								dialog.show();
								 final Thread newThred=new Thread(){
									 	public void run(){
									        		
									 		 try{
									 		//	S3.getDataForObject2(GetbucketName.bucketName, videoNameList.get(position).trim());
									 			is=S3.getImageForObject(GetbucketName.bucketName, GetbucketName.getFoldername()+"/"+videoNameList.get(position).toString().trim());
											
													dialog.dismiss();
													//finish();
												} catch(Throwable e){
													e.printStackTrace();
													
												}
												
									        	}
									        };
									        newThred.start();
									        Thread t1=new Thread(){
									        	public void run(){
									        		try {
														newThred.join();
													} catch (InterruptedException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
									        	     String filepath="/sdcard/AWSVideo/aws";
												     File imgdir=new File(filepath);
												     File file=new File(filepath+videoNameList.get(position).toString().trim());
												     if(file.exists()==false){
												    	 String path=imgdir.toString().toLowerCase();
												    	 String name=imgdir.getName().toLowerCase();
												    	// File k = new File(path, filename);  
												    	  
												    	 ContentValues values = new ContentValues();  
												    	 values.put(MediaStore.MediaColumns.DATA,filepath+ videoNameList.get(position).toString().trim());  
												    	 values.put(MediaStore.MediaColumns.TITLE, "exampletitle");  
												    	 values.put(MediaStore.MediaColumns.MIME_TYPE, "video/"+videoNameList.get(position).toString().substring(videoNameList.get(position).toString().lastIndexOf("."))+"");  
												    	 values.put(MediaStore.Video.Media.ARTIST, "cssounds ");  
												    	 values.put(MediaStore.Video.Media.DURATION,new Date().getMinutes());
												    	// values.put(MediaStore.Video.Media.IS_RINGTONE, true);  
												    	// values.put(MediaStore.Video.Media.IS_NOTIFICATION, true);  
												    	// values.put(MediaStore.Audio.Media.IS_ALARM, true);  
												    	// values.put(MediaStore.Audio.Media.IS_MUSIC, false);  
												    	   
												    	 //Insert it into the database  
												    	
												    	 ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
												    	   Uri uri = contentResolver.insert(Video.Media.EXTERNAL_CONTENT_URI, values);
												    	   OutputStream outStream = null;
														try {
															outStream = contentResolver.openOutputStream(uri);
														} catch (FileNotFoundException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
												    	   //OutputStream outStream = contentResolver.openOutputStream(uri);
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
									        };t1.start();

						}
	    	        	
	    	        });

	    	        convertView.setTag(holder);
	    	      } else {
	    	        // Get the ViewHolder back to get fast access to the TextView
	    	        // and the ImageView.
	    	        holder = (ViewHolder) convertView.getTag();
	    	      }

	    	      // Get flag name and id
	    	      String filename = "flag_" + String.valueOf(position);
	    	      int id = context.getResources().getIdentifier(filename, "drawable", context.getString(R.string.package_str));

	    	      // Icons bound to the rows.
	    	      if (id != 0x0) {
	    	        mIcon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon);
	    	      }

	    	      // Bind the data efficiently with the holder.
	    	  //    holder.iconLine.setImageBitmap(mIcon1);
	    	      holder.textLine.setText(data[position]);

	    	      return convertView;
	    	    }

	    	    protected void viewVideo(final String string) {
	    	    	dialog = new ProgressDialog(context);
		            dialog.setCancelable(true);
		            dialog.setMessage("Downloading Videos Plz Wait...");
		            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						dialog.show();
	    	    	Thread t=new Thread(){
	    	    		public void run(){
	    	    			Log.d("sdfdsfdf", string);
	    					InputStream is=S3.getImageForObject(GetbucketName.bucketName, string);
	    					Log.d("inputstream", is.toString());
	    					  File temp = new File("/sdcard/abcd.3gp");
	    					   
	      					//  temp.createNewFile();
	      					   
	      					  String tempPath = temp.getAbsolutePath();
	      					   
	      					                                         
	      					   
	      					  FileOutputStream out;
	    					try {
	    						out = new FileOutputStream(temp);
	    						 byte buf[] = new byte[128];
	    	  					   
	    	  					  do{
	    	  					   
	    	  					     int numread = is.read(buf);
	    	  					   
	    	  					     if(numread <= 0) break;
	    	  					   
	    	  					     out.write(buf,0,numread);
	    	  					  //   progressHandler.sendMessage(progressHandler.obtainMessage());
	    	  					     } while(true);
	    	  					 dialog.dismiss();
	 	      					Intent it=new Intent(context,ViewVideo2.class);
	 	      					context.startActivity(it);
	    					} catch (FileNotFoundException e) {
	    						// TODO Auto-generated catch block
	    						e.printStackTrace();
	    					} catch (IOException e) {
	    						// TODO Auto-generated catch block
	    						e.printStackTrace();
	    					}
	      					  
	    	    		}
	    	    	};t.start();
					
  					
				}

				static class ViewHolder {
	    	      TextView textLine;
	    	     
	    	      Button buttonLine,buttonline2;
	    	    }

	    	
	    	    public Filter getFilter() {
	    	      // TODO Auto-generated method stub
	    	      return null;
	    	    }

	    	    public long getItemId(int position) {
	    	      // TODO Auto-generated method stub
	    	      return 0;
	    	    }
	    	    public int getCount() {
	    	      // TODO Auto-generated method stub
	    	      return data.length;
	    	    }
	    	    public Object getItem(int position) {
	    	      // TODO Auto-generated method stub
	    	      return data[position];
	    	    }

	    	  }
	    	  @Override
	    		public void onBackPressed() {
	    			// TODO Auto-generated method stub
	    			super.onBackPressed();
	    			
	    		}
	}