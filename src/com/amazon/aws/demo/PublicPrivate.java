package com.amazon.aws.demo;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;


public class PublicPrivate {
	AmazonSimpleDBClient sdbClient;
	String nextToken;
	protected int count;
	private static final String REG_DOMAIN = "ImageView";
	private static final String NAME_ATTRIBUTE = "IMAGENAME";
	private static final String NUMBER_ATTRIBUTE = "STATUS";
	

	public PublicPrivate() {
		// TODO Auto-generated constructor stub

		AWSCredentials credentials = new BasicAWSCredentials(
				Constants.ACCESS_KEY_ID, Constants.SECRET_KEY);
		sdbClient = new AmazonSimpleDBClient(credentials);
		this.nextToken = null;
		this.count = -1;
	}
	public void createDomain() {
		// TODO Auto-generated method stub
		CreateDomainRequest cr = new CreateDomainRequest(REG_DOMAIN);
		sdbClient.createDomain(cr);

	}

	public void AddToTable(String imagename, String username, String status) {
		// TODO Auto-generated method stub
		
		ReplaceableAttribute iname = new ReplaceableAttribute(
				"IMAGENAME", imagename, Boolean.TRUE);
		ReplaceableAttribute uname = new ReplaceableAttribute("USERNAME",
				username, Boolean.TRUE);
		ReplaceableAttribute sta = new ReplaceableAttribute("STATUS",
				status, Boolean.TRUE);

		List<ReplaceableAttribute> attrs = new ArrayList<ReplaceableAttribute>(
				2);
		attrs.add(iname);
		attrs.add(uname);
		attrs.add(sta);
		PutAttributesRequest par = new PutAttributesRequest(REG_DOMAIN,
				imagename, attrs);
		try {
			sdbClient.putAttributes(par);
		} catch (Exception exception) {
			System.out.println("EXCEPTION = " + exception);
		}
	}

	
	public List<Others> getAllValues(String usern, String status) {
		// TODO Auto-generated method stub
		int flag=0;
		SelectRequest selectRequest = new SelectRequest(
				"select IMAGENAME from ImageView where USERNAME='" + usern
				+ "' and STATUS='" + status + "'").withConsistentRead(true);
		selectRequest.setNextToken( this.nextToken );
		
		SelectResult response = this.sdbClient.select( selectRequest );
		
		/*List<String> ls= response.getItems();  */
		
		/*return this.valuesGetting(response.getItems());*/
		//System.out.println("image names            "+valuesGetting(response.getItems()));
		System.out.println("hello          "+  response.getItems().toString());
		return valuesGetting(response.getItems());
		
	}
	
	
	public List<Others> getAllPublicValues(String usern) {
		// TODO Auto-generated method stub
		int flag=0;
		SelectRequest selectRequest = new SelectRequest(
				"select IMAGENAME from ImageView where STATUS='public' ").withConsistentRead(true);
		selectRequest.setNextToken( this.nextToken );
		
		SelectResult response = this.sdbClient.select( selectRequest );
		
		/*List<String> ls= response.getItems();  */
		
		/*return this.valuesGetting(response.getItems());*/
		//System.out.println("image names            "+valuesGetting(response.getItems()));
		System.out.println("hello          "+  response.getItems().toString());
		return valuesGetting(response.getItems());
		
	}
	private List<Others> valuesGetting(List<Item> items) {
		// TODO Auto-generated method stub
		ArrayList<Others> alldata=new ArrayList<Others>(items.size());
		
		for(Item item:items)
		{
			alldata.add(this.individulaData(item));
		}
		
		System.out.println("all data size        "+alldata.size());
		for(int i=0;i<alldata.size();i++)
		{
			System.out.println(" name  "+alldata.get(i));
		}
		return alldata;
	}
	private Others individulaData(Item item) {
		// TODO Auto-generated method stub
		return new Others(this.getimagenames(item));  
	}
	



	private String getimagenames(Item item) {
		// TODO Auto-generated method stub
		return this.getAllStringAttribute("IMAGENAME",item.getAttributes());
	}
	
	private String getAllStringAttribute(String usernameAttribute, List<Attribute> list) {
		// TODO Auto-generated method stub
		
		for(Attribute attrib:list)
		{
			if(attrib.getName().equals(usernameAttribute))
			{
				return attrib.getValue();
			}
		} 
		
		return "";  
	}
}
