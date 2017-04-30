package com.amazon.aws.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends Activity {
	EditText name, password, phone;
	String name1, password1, phoneno;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		 
		name = (EditText) findViewById(R.id.editText1);
		password = (EditText) findViewById(R.id.editText2);
		phone = (EditText) findViewById(R.id.editText3);
		Button bt = (Button) findViewById(R.id.button1);
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				name1 = name.getText().toString().trim();
				password1 = password.getText().toString().trim();
				phoneno = phone.getText().toString().trim();
				if (!name.equals("") && !password1.equals("")
						&& !phoneno.equals("")) {
					if(phoneno.length()==10)
					{
					if(password1.length()>=14)
					{
						char c;
						int count=0,s=0;
						for(int i=0;i<password1.length();i++)
						{
							int l=0,b=0;
							c=password1.charAt(i);
							if(c==' ')
							    b++;
							if(Character.isDigit(c))
								count++;
							else if(Character.isLetter(c))
								l++;
							else
								s++;
							
						}
						if(count>=2 && s>=4)
						{
					RegisterList rlist = new RegisterList();
					rlist.createDomain();
					rlist.AddToTable(name1, password1, phoneno);
					Toast.makeText(getBaseContext(), "Registration completed",
							Toast.LENGTH_LONG).show();
					/*Intent it = new Intent(RegistrationActivity.this,
							LoginActivity.class);
					startActivity(it);*/
					finish();
					}
						else {
							Toast.makeText(getApplicationContext(), "please password should contain minimum 4 special charecters and 2 digits",300).show();
						    password.setText("");
						}
					}
					else {
						Toast.makeText(getApplicationContext(), "please enter minimum 14 character password", 300).show();
					}
					} 
				} else {
					Toast.makeText(getApplicationContext(),
							"Please Enter All Fields", 300).show();
				}
			}
		});
	}

	public void register(View v) {
	}
}
