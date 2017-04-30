package com.amazon.aws.demo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SimpleDbList extends ListActivity {
	String name="";
	String number="";
	String total="";
	ArrayList<String> al=new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		RegisterOthers ro=new RegisterOthers();
		List<Others> list=ro.getAllValues();
		int len=list.size();
		System.out.println("size @@@@@@@@@@@@@"+len);
		for(int i=0;i<len;i++)
		{
			
			Others oo=list.get(i);
		 name=oo.getName();
			 number=oo.getNumber();
			 total=name+"@"+number;
			 al.add(total);
			 
			System.out.println(oo.getName()+"==========="+oo.getNumber());
		}
		ListView lv=getListView();
		lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,al));
		 lv.setOnItemClickListener(new OnItemClickListener() {
             public void onItemClick(AdapterView<?> parent, View view,
                 int position, long id) {
           	  String ite=((TextView)view).getText().toString();
           	  Toast.makeText(getApplicationContext(), ite,30).show();
           	  checkList(((TextView) view).getText().toString());
             }

			private void checkList(String contact) {
				// TODO Auto-generated method stub
				 RegisterOthers rlist=new RegisterOthers();
				// rlist.createDomain();
				String to=contact;
				String[] names=to.split("@");
				String name=names[0];
				String number=names[1];
				
				Intent in1=new Intent(SimpleDbList.this,EditContact.class);
				in1.putExtra("name", name);
				in1.putExtra("number", number);
				startActivity(in1);
				//Others user=new Others(name,number);
				//rlist.AddToOthers(user);
				
				Toast.makeText(getApplicationContext(), name+"/"+number, 30).show();
				
			}
           });
     }

}

