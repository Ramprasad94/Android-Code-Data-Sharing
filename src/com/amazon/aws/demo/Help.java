package com.amazon.aws.demo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Help extends Activity{

	Button gre,callhistory;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		gre=(Button) findViewById(R.id.gre);
		callhistory=(Button) findViewById(R.id.callhistory);
		gre.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse("https://market.android.com/details?id=com.gini.coign&feature=search_result") );

	    	    startActivity( browse );
				
			}
			
		});
		callhistory.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse("https://market.android.com/details?id=com.coign.calltracker&feature=search_result") );

	    	    startActivity( browse );
			}
			
		});
	}

}
