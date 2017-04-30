package com.amazon.aws.demo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ReadingFile extends Activity {
String res="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {	
			
			
			 InputStream in = openFileInput("awsincoign");

			   if (in != null) {
			    // prepare the file for reading
			     InputStreamReader input = new InputStreamReader(in);
			     BufferedReader buffreader = new BufferedReader(input);

			      String line;
				while (( line = buffreader.readLine()) != null) {
			      	res += line;
			      }
			      in.close();
			      Log.d("sdsafdsafsadfsa", res);
			      }
         
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Intent it=getIntent();
		it.putExtra("filename", res);
		setResult(RESULT_OK, it);
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	

}
