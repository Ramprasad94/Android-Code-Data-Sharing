package com.amazon.aws.demo;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.DeleteAttributesRequest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditContact extends Activity{
	AWSCredentials credentials = new BasicAWSCredentials( Constants.ACCESS_KEY_ID, Constants.SECRET_KEY );
	protected AmazonSimpleDBClient sdbClient=new AmazonSimpleDBClient( credentials); 
	private static final String REG_DOMAIN = "RemoteContactSharing";
	EditText e1,e2;
	String upname="";
	String upnum="";
	Button b1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);
		Bundle b=getIntent().getExtras();
		final String name=b.getString("name");
		String number=b.getString("number");
		e1=(EditText)findViewById(R.id.editText1);
		e2=(EditText)findViewById(R.id.editText2);
		e1.setText(name);
		e2.setText(number);
		b1=(Button)findViewById(R.id.editbtn);
		b1.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "success", 30).show();
				// TODO Auto-generated method stub
				upname=e1.getText().toString();
				upnum=e2.getText().toString();
				
				sdbClient.deleteAttributes(new DeleteAttributesRequest(REG_DOMAIN,name));
				Others ot=new Others(upname,upnum);
				RegisterOthers ro=new RegisterOthers();
				ro.AddToOthers(ot);
				Toast.makeText(getApplicationContext(), "successfully upadated", 30).show();
				
			}
			
		});
		
}
}
