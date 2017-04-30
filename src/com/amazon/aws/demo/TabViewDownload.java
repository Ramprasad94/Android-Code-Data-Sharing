package com.amazon.aws.demo;

import android.annotation.TargetApi;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

public class TabViewDownload extends TabActivity {
	
	String u_name, status;
	String imagename = "";
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tabxml);
	    
	    u_name = getIntent().getStringExtra("u");
		status = getIntent().getStringExtra("status");
		System.out.println("privateal               " + u_name
				+ "  status   " + status);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, PrivateImagesActivity.class).putExtra("u", u_name).putExtra("status",status);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("image").setIndicator("Images",
	                      res.getDrawable(R.drawable.ic_tab_artists))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, PrivateSongsActivity.class).putExtra("u", u_name).putExtra("status",status);
	    spec = tabHost.newTabSpec("songs").setIndicator("Songs",
	                      res.getDrawable(R.drawable.ic_tab_example))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, PrivateVideoActivity.class).putExtra("u", u_name).putExtra("status",status);
	    spec = tabHost.newTabSpec("video").setIndicator("Videos",
	                      res.getDrawable(R.drawable.ic_tab_video))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	  intent = new Intent().setClass(this, PrivateOthersActivity.class).putExtra("u", u_name).putExtra("status",status);
	    spec = tabHost.newTabSpec("other").setIndicator("Others",
	                      res.getDrawable(R.drawable.ic_tab_other))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(2);
	}

	@TargetApi(Build.VERSION_CODES.ECLAIR)
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Toast.makeText(this, "dzsfvds", 20).show();
	}
	
}