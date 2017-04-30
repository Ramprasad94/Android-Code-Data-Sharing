package com.amazon.aws.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import com.amazon.aws.demo.s3.S3;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class DiaplayActivity extends Activity{

	private static final int UPDATE_FREQUENCY = 500;
	private static final int STEP_VALUE = 4000;
	int increment=10;
	private TextView selelctedFile = null;
	private SeekBar seekbar = null;
	private MediaPlayer player = null;
	private ImageButton playButton = null;
	private ImageButton prevButton = null;
	private ImageButton nextButton = null;
	private boolean isStarted = true;
	private String currentFile = "";
	private boolean isMoveingSeekBar = false;
	ProgressDialog dialog;
	private final Handler handler = new Handler();
	
	private final Runnable updatePositionRunnable = new Runnable() {
		public void run() {
			updatePosition();
		}
	};
	  Handler progressHandler = new Handler() {
	        public void handleMessage(Message msg) {
	            dialog.incrementProgressBy(increment);
	        }
	    };
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio);
		dialog = new ProgressDialog(this);
        dialog.setCancelable(true);
        dialog.setMessage("Loading Song Plz Wait...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
		   
        selelctedFile = (TextView)findViewById(R.id.selectedfile);
        seekbar = (SeekBar)findViewById(R.id.seekbar);
        playButton = (ImageButton)findViewById(R.id.play);
        prevButton = (ImageButton)findViewById(R.id.prev);
        nextButton = (ImageButton)findViewById(R.id.next);
        player = new MediaPlayer();
        
        player.setOnCompletionListener(onCompletion);
        player.setOnErrorListener(onError);
        seekbar.setOnSeekBarChangeListener(seekBarChanged);
    	playButton.setOnClickListener(onButtonClick);
    	nextButton.setOnClickListener(onButtonClick);
    	prevButton.setOnClickListener(onButtonClick);
    	
		//ImageView iv=(ImageView) findViewById(R.id.image);
	    Bundle b=getIntent().getExtras();
	    final String name=b.getString("imagename");
	    final String bucketname=b.getString("bucketname");
	    Thread newThred=new Thread(){
        	public void run(){
        		
        		try{
        			
        			InputStream is=S3.getDataForObject(bucketname, name);
					  
					  MediaPlayer mp = new MediaPlayer();
					   
					  File temp = new File("/sdcard/abcd.mp3");
					   
					//  temp.createNewFile();
					   
					  String tempPath = temp.getAbsolutePath();
					   
					                                         
					   
					  FileOutputStream out = new FileOutputStream(temp);
					   
					  byte buf[] = new byte[128];
					   
					  do{
					   
					     int numread = is.read(buf);
					   
					     if(numread <= 0) break;
					   
					     out.write(buf,0,numread);
					  //   progressHandler.sendMessage(progressHandler.obtainMessage());
					     } while(true);
					  dialog.dismiss();
					  startPlay("/sdcard/abcd.mp3");
				}
				catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				finally{
					
				}
        	}
        };
        newThred.start();

	   
	    seekbar.setVisibility(0);
		playButton.setVisibility(0);
		prevButton.setVisibility(0);
		nextButton.setVisibility(0);
		
		
	
	}

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
		seekbar.setProgress(0);
				
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
		
		seekbar.setMax(player.getDuration());
		playButton.setImageResource(android.R.drawable.ic_media_pause);
		
		updatePosition();
		
		isStarted = true;
	}
	
	public void stopPlay() {
		player.stop();
		player.reset();
		playButton.setImageResource(android.R.drawable.ic_media_play);
		handler.removeCallbacks(updatePositionRunnable);
		seekbar.setProgress(0);
		
		isStarted = false;
	}
	
	private void updatePosition(){
		handler.removeCallbacks(updatePositionRunnable);
		
		seekbar.setProgress(player.getCurrentPosition());
		
		handler.postDelayed(updatePositionRunnable, UPDATE_FREQUENCY);
	}

	private class MediaCursorAdapter extends SimpleCursorAdapter{

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
			switch(v.getId())
			{
				case R.id.play:
				{
					if(player.isPlaying())
					{
						handler.removeCallbacks(updatePositionRunnable);
						player.pause();
						playButton.setImageResource(android.R.drawable.ic_media_play);
					}
					else
					{
						if(isStarted)
						{
							player.start();
							playButton.setImageResource(android.R.drawable.ic_media_pause);
							
							updatePosition();
						}
						else
						{
							startPlay(currentFile);
						}
					}
					
					break;
				}
				case R.id.next:
				{
					int seekto = player.getCurrentPosition() + STEP_VALUE;
					
					if(seekto > player.getDuration())
						seekto = player.getDuration();
					
					player.pause();
					player.seekTo(seekto);
					player.start();
					
					break;
				}
				case R.id.prev:
				{
					int seekto = player.getCurrentPosition() - STEP_VALUE;
					
					if(seekto < 0)
						seekto = 0;
					
					player.pause();
					player.seekTo(seekto);
					player.start();
					
					break;
				}
			}
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
	
};
}
