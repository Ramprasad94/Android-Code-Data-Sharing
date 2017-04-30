package com.amazon.aws.demo;

import java.io.FileOutputStream;
import java.util.ArrayList;



import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DisplayContacts extends ListActivity{
	ArrayList<String>list1=new ArrayList<String>();
	 String name="";
	 String number="";
	
	private static final Uri URI = ContactsContract.Contacts.CONTENT_URI;
	 private static final String ID = ContactsContract.Contacts._ID;
	 private static final Uri PURI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
	 private static final String CID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
	   private static final String DNAME = ContactsContract.Contacts.DISPLAY_NAME;
	   private static final String HPN = ContactsContract.Contacts.HAS_PHONE_NUMBER;
	    private static final String PNUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		 GetData();
	        
	        ListView lv=getListView();
	        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice,list1));
	        lv.setTextFilterEnabled(true);
            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
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
				 rlist.createDomain();
				String to=contact;
				String[] names=to.split("@");
				String name=names[0];
				String number=names[1];
				Others user=new Others(name,number);
				rlist.AddToOthers(user);
				
				Toast.makeText(getApplicationContext(), name+"/"+number, 30).show();
				
			}
            });
      }
	    

	    public void GetData(){
	    	 Cursor cu=getContentResolver().query(URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC ");
	       
	    	
	       while (cu.moveToNext()) { 	
	           
	           
	          String id = cu.getString(cu.getColumnIndex(ID));
	           
	           name = cu.getString(cu.getColumnIndex(DNAME));   
	          // row.createCell((short) 0).setCellValue(name);
		    	
	                       
	           int phcounter = 0;
	           if (Integer.parseInt(cu.getString(cu.getColumnIndex(HPN))) > 0) {	        	
	              Cursor pCur = getContentResolver().query(PURI,  null, CID + " = ?",  new String[]{id}, null);
	            
	              while (pCur.moveToNext()) {
	                 number = pCur.getString(pCur.getColumnIndex(PNUM));
	              //  row.createCell((short) 1).setCellValue(number);
	                String data=name+"@ "+number;
	               list1.add(data);
	               
	              
	                 phcounter ++;
	              } 
	              pCur.close();  
	             // FileOutputStream fileOut;
	  			//try {
	  				//fileOut = new FileOutputStream(filename);
	  			
	  	    	//hwb.write(fileOut);
	  	    	//fileOut.close();
	  	    //	System.out.println("Your excel file has been generated!");
	  		//	} catch (Exception e) {
	  				// TODO Auto-generated catch block
	  			//	e.printStackTrace();
	  			//}
	           }  
	       }
	    }
	    
	
		


}
