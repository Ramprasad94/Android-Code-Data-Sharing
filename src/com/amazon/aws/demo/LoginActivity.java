package com.amazon.aws.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	EditText un, pwd;
	public  static String usern;
	
	String passw, role, name;
	Bundle extras;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main1);
		 

		
		extras = getIntent().getExtras();
		Button button = (Button) findViewById(R.id.login);
		un = (EditText) findViewById(R.id.uname);
		pwd = (EditText) findViewById(R.id.pwdd);
		
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				usern = un.getText().toString().trim();
				passw = pwd.getText().toString().trim();
				pwd.setText("");
				if (!usern.equals("") && !passw.equals("")) {
					RegisterList rlist = new RegisterList();

					int ver = rlist.loginVerify(usern, passw);

					if (ver == 0) {
						Toast.makeText(getBaseContext(),
								"In Valid Credentials", 30).show();
					} else {
						Intent it = new Intent(LoginActivity.this,
								AWSMainScreen.class);
						it.putExtra("cont", usern);
						startActivity(it);
						//finish();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"please enter all fields", 90).show();
				}
			}
		});

		Button regview = (Button) findViewById(R.id.register);
		regview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this,
						RegistrationActivity.class);
				startActivity(intent);	
				
				
			}
		});

	}

}