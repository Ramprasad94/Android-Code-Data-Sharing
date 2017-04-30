package com.amazon.aws.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;

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
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SongsTab extends ListActivity {
	private static final int UPDATE_FREQUENCY = 500;
	private static final int STEP_VALUE = 4000;
	
	private MediaCursorAdapter mediaAdapter = null;
	private TextView selelctedFile = null;
	
	private MediaPlayer player = null;
	
	private boolean isStarted = false;
	boolean dbFlag=false;
	private String currentFile = "";
	private boolean isMoveingSeekBar = false;
	Cursor cursor;
	ArrayList listitems=new ArrayList();
	PublicPrivate pp;
	String usern;
	

	private final Handler handler = new Handler();
	ProgressDialog dialog;
	private final Runnable updatePositionRunnable = new Runnable() {
		public void run() {
			updatePosition();
		}
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio);
        usern=getIntent().getStringExtra("u");
        System.out.println("userrrrrrrrrrrr nameeeeeeeeeeeeeeee songs tab  "+usern);
        pp=new PublicPrivate();
        dialog = new ProgressDialog(this);
        dialog.setCancelable(true);
        dialog.setMessage("Uploading Song Plz Wait...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        
        selelctedFile = (TextView)findViewById(R.id.selectedfile);
    
        
        player = new MediaPlayer();
        
       
        cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        
        if(null != cursor)
        {
        	cursor.moveToFirst();
        	
      	
        	mediaAdapter = new MediaCursorAdapter(this, R.layout.audiolistitem, cursor);
        	
        	setListAdapter(mediaAdapter);
        	
        	
        }
    }
    
    @Override
	protected void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);
		
			String file=(String) view.getTag();
		alertbox("Confirmation !","Are You Sure Do You Want TO Upload ?",file);
		
	}
    protected void alertbox(String title,String mymessage,final String imagePath)
	   {
		 final CharSequence[] items = {"private", "public"};
		 
		 AlertDialog.Builder builder = new AlertDialog.Builder(SongsTab.this);
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
             	  Toast.makeText(getApplicationContext(), "Uploaded sucessfully", 190).show();
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
	protected void uploading(final String file) {
		// TODO Auto-generated method stub
		
		final File f=new File(file);
		final String ssss[]=file.split("/");
		String res="";
		 int index=file.lastIndexOf("/");
			final String str=file.substring(index+1);
			
		 dialog.cancel();
		   dialog.show();
			final int size=GettingFiles.updateSize1(f);
			if(size>0){
		   final Thread newThred=new Thread(){
			 	public void run(){
			        		
			 		 try{
			      
							dbFlag=S3.createObjectForBucket( "AndroidAmazon",GetbucketName.getFoldername()+"/"+str ,f );
							FileOutputStream fos=openFileOutput("awsincoign2",Context.MODE_PRIVATE);
							GettingFiles.updateFile(fos,size);
                              
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
							if(dbFlag==true){
								DBAdapterSongs d=new DBAdapterSongs(SongsTab.this,"AWSSongs");
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
			        		Intent it=new Intent(SongsTab.this,AlertActivity.class);
			        		startActivity(it);
			        	}
			        	}
			        };t2.start();
			}
			else{
				Toast.makeText(getBaseContext(), "No space", Toast.LENGTH_LONG).show();
				
			}
	
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		handler.removeCallbacks(updatePositionRunnable);
		player.stop();
		player.reset();
		player.release();

		player = null;
	}

	private void startPlay(String file) {
		Log.i("Selected: ", file);
		
		selelctedFile.setText(file);
		
				
		player.stop();
		player.reset();
		
		try {
			player.setDataSource(file);
			player.prepare();
			player.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		

		
		updatePosition();
		
		isStarted = true;
	}
	
	public void stopPlay() {
		player.stop();
		player.reset();
		
		handler.removeCallbacks(updatePositionRunnable);
		
		
		isStarted = false;
	}
	
	private void updatePosition(){
		handler.removeCallbacks(updatePositionRunnable);
		
		
		handler.postDelayed(updatePositionRunnable, UPDATE_FREQUENCY);
	}

	public class MediaCursorAdapter extends SimpleCursorAdapter{

		public MediaCursorAdapter(Context context, int layout, Cursor c) {
			super(context, layout, c, 
					new String[] { MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.TITLE, MediaStore.Audio.AudioColumns.DURATION},
					new int[] { R.id.displayname, R.id.title, R.id.duration });
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView title = (TextView)view.findViewById(R.id.title);
			TextView name = (TextView)view.findViewById(R.id.displayname);
			TextView duration = (TextView)view.findViewById(R.id.duration);
			
			name.setText(cursor.getString(
					cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)));
			
			title.setText(cursor.getString(
					cursor.getColumnIndex(MediaStore.MediaColumns.TITLE)));

			long durationInMs = Long.parseLong(cursor.getString(
					cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION)));
			
			double durationInMin = ((double)durationInMs/1000.0)/60.0;
			
			durationInMin = new BigDecimal(Double.toString(durationInMin)).setScale(2, BigDecimal.ROUND_UP).doubleValue(); 

			duration.setText("" + durationInMin);
			
			view.setTag(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)));
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.audiolistitem, parent, false);
			
			bindView(v, context, cursor);
			
			return v;
		}
    }
	
	private View.OnClickListener onButtonClick = new View.OnClickListener() {
		
		public void onClick(View v) {
		}
	};
	
	private MediaPlayer.OnCompletionListener onCompletion = new MediaPlayer.OnCompletionListener() {
		
		public void onCompletion(MediaPlayer mp) {
			stopPlay();
		}
	};
	
	private MediaPlayer.OnErrorListener onError = new MediaPlayer.OnErrorListener() {
		
		public boolean onError(MediaPlayer mp, int what, int extra) {
			// returning false will call the OnCompletionListener
			return false;
		}
	};
	
	private SeekBar.OnSeekBarChangeListener seekBarChanged = new SeekBar.OnSeekBarChangeListener() {
		public void onStopTrackingTouch(SeekBar seekBar) {
			isMoveingSeekBar = false;
		}
		
		public void onStartTrackingTouch(SeekBar seekBar) {
			isMoveingSeekBar = true;
		}
		
		public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
			if(isMoveingSeekBar)
			{
				player.seekTo(progress);
			
				Log.i("OnSeekBarChangeListener","onProgressChanged");
			}
		}
	};
	protected void onPause() {
	    super.onPause();
	   stopPlay();
	}
}