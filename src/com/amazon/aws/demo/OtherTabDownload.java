package com.amazon.aws.demo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.amazon.aws.demo.VideoTabDownload.EfficientAdapter;
import com.amazon.aws.demo.VideoTabDownload.EfficientAdapter.ViewHolder;
import com.amazon.aws.demo.s3.S3;
import com.amazon.aws.demo.s3.S3ObjectView;
import com.amazon.aws.demo.s3.S3ObjectView2;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
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

public class OtherTabDownload extends ListActivity {
	ArrayList<String> al=new ArrayList<String>();
	 public static ArrayList<String> otherNameList=new ArrayList<String>();
	  int columnIndex=0;
		String pavan=null;
		Cursor c=null;
		 String res="";
		static ProgressDialog dialog;
		 private EfficientAdapter adap;
	static InputStream is;
		 static String[] data;
		 SQLiteDatabase sampleDB = null;;
	    	public void onCreate(Bundle savedInstanceState) {
	            super.onCreate(savedInstanceState);
	          //  otherNameList=GettingFiles.otherNameList;
	          
	            requestWindowFeature(Window.FEATURE_NO_TITLE);
	            setContentView(R.layout.imagedownload);
	          if(otherNameList.size()>0){
	        	  otherNameList.removeAll(otherNameList);
	          }
sampleDB =  this.openOrCreateDatabase("AWSDatabase", MODE_PRIVATE, null);
	            
	            Cursor c = sampleDB.rawQuery("SELECT Name FROM AWSOthers", null);
		    	
				if (c != null ) {
		    		if  (c.moveToFirst()) {
		    			do {
		    				String name = c.getString(c.getColumnIndex("Name"));
		    				otherNameList.add(name);
		    				//Toast.makeText(this,name,20).show();
		    				Log.d("flag2", name);
		    			}while (c.moveToNext());
		    		} }
				 data=new String[otherNameList.size()];
		           otherNameList.toArray(data);
		         //  setContentView(R.layout.main);
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
	    	          //  Toast.makeText(context, "view-" +, Toast.LENGTH_SHORT).show();
	    	        	Intent it=new Intent(context,S3ObjectView2.class);
						it.putExtra("imagename",   GetbucketName.getFoldername()+"/"+otherNameList.get(position).toString());
						it.putExtra("bucketname", GetbucketName.bucketName);
					    context.startActivity(it);
				
	    	          }
	    	        });
	    	        
	    	        holder.buttonline2.setOnClickListener(new OnClickListener(){
	    	        	   private int pos = position;
						public void onClick(View v) {
							dialog = new ProgressDialog(context);
				            dialog.setCancelable(true);
				            dialog.setMessage("Downloading File Plz Wait...");
				            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								dialog.show();
								 final Thread newThred=new Thread(){
									 	public void run(){
									        		
									 		 try{
									 			is=S3.getDataForObject(GetbucketName.bucketName,  GetbucketName.getFoldername()+"/"+otherNameList.get(position).trim());
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
													} catch (InterruptedException e2) {
														// TODO Auto-generated catch block
														e2.printStackTrace();
													}
									        		ByteArrayOutputStream baos = new ByteArrayOutputStream( 8196 );
									    			File f=new File("/sdcard/AWS/"+otherNameList.get(position));
									    			
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

	    		@TargetApi(Build.VERSION_CODES.ECLAIR) @Override
	    		public void onBackPressed() {
	    			// TODO Auto-generated method stub
	    			//otherNameList.removeAll(otherNameList);
	    			super.onBackPressed();
	    			
	    		}
	}