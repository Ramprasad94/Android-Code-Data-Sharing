package com.amazon.aws.demo;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class UploadMulFileTab extends TabActivity {
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tabxml);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, ImageMulUpload.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("image").setIndicator("Images",
	                      res.getDrawable(R.drawable.ic_tab_artists))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, songMulUpload.class);
	    spec = tabHost.newTabSpec("songs").setIndicator("Songs",
	                      res.getDrawable(R.drawable.ic_tab_example))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, VideoMulUpload.class);
	    spec = tabHost.newTabSpec("video").setIndicator("Videos",
	                      res.getDrawable(R.drawable.ic_tab_video))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, OtherMulUpload.class);
	    spec = tabHost.newTabSpec("other").setIndicator("Others",
	                      res.getDrawable(R.drawable.ic_tab_other))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);
	}
}