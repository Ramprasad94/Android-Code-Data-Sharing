package com.amazon.aws.demo;




import java.util.ArrayList;
import java.util.Collection;
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

public class RegisterOthers {
	  
		
	protected AmazonSimpleDBClient sdbClient;
	protected String nextToken;
	private static final String NAME_ATTRIBUTE = "Name";
	private static final String NUMBER_ATTRIBUTE = "Number";
	
	
	protected int count;
	///private static final String LOGIN_VERIFY = "select * from CoignCloudWithOthers where ImageName = '' and Pwd='' ";
	private static final String REG_DOMAIN = "RemoteContactSharing";
	private static final String COUNT_QUERY = "select count(*) from RemoteContactSharing";

	public RegisterOthers() {
		// TODO Auto-generated constructor stub
		
		AWSCredentials credentials = new BasicAWSCredentials( Constants.ACCESS_KEY_ID, Constants.SECRET_KEY );
        this.sdbClient = new AmazonSimpleDBClient( credentials); 
		this.nextToken = null;
		this.count = -1;
	}

	public void createDomain() {
		// TODO Auto-generated method stub
		CreateDomainRequest cdr = new CreateDomainRequest(REG_DOMAIN);
		this.sdbClient.createDomain( cdr ); 
	}

/*	public void createFields() {
		// TODO Auto-generated method stub
		ReplaceableAttribute UserAttribute = new ReplaceableAttribute( USERNAME_ATTRIBUTE, "", Boolean.TRUE );
		ReplaceableAttribute PassAttribute = new ReplaceableAttribute( PASSWORD_ATTRIBUTE, "", Boolean.TRUE );
		ReplaceableAttribute ContactAttribute = new ReplaceableAttribute( CONTACT_ATTRIBUTE, "", Boolean.TRUE );

		List<ReplaceableAttribute> attrs = new ArrayList<ReplaceableAttribute>(2);
		attrs.add( UserAttribute );
		attrs.add( PassAttribute );
		attrs.add( ContactAttribute );
		
		PutAttributesRequest par = new PutAttributesRequest( REG_DOMAIN, "", attrs);	
		try {
			this.sdbClient.putAttributes( par );
		}
		catch ( Exception exception ) {
			System.out.println( "EXCEPTION = " + exception ); 
		}
		
	}*/

	public void AddToOthers(Others u) {  
		// TODO Auto-generated method stub
		ReplaceableAttribute UserAttribute = new ReplaceableAttribute( NAME_ATTRIBUTE, u.getName(), Boolean.TRUE );
		ReplaceableAttribute PassAttribute = new ReplaceableAttribute( NUMBER_ATTRIBUTE, u.getNumber(), Boolean.TRUE );
		//ReplaceableAttribute EmailAttribute = new ReplaceableAttribute( EMAIL_ATTRIBUTE, u.getEmail(), Boolean.TRUE );
		//ReplaceableAttribute PhnoAttribute = new ReplaceableAttribute( PHNO_ATTRIBUTE, u.getPhno(), Boolean.TRUE );
		

		List<ReplaceableAttribute> attrs = new ArrayList<ReplaceableAttribute>(2);
		attrs.add( UserAttribute );
		attrs.add( PassAttribute );
		
		
		PutAttributesRequest par = new PutAttributesRequest( REG_DOMAIN, u.getName(), attrs);	
		try {
			this.sdbClient.putAttributes( par );
		}
		catch ( Exception exception ) {
			System.out.println( "EXCEPTION = " + exception ); 
		}  
	}
	private String delete(String name)
	{
		
		//sdbClient.batchDeleteAttributes(name);
		
		return name;
		
	}

	public int loginVerify(String un, String pwd) {
		// TODO Auto-generated method stub
		int flag=0;
		SelectRequest selectRequest = new SelectRequest("select Check from CoignCloudUploadingAll where FileName ='"+un+"'").withConsistentRead( true );
		selectRequest.setNextToken( this.nextToken );
		
		SelectResult response = this.sdbClient.select( selectRequest );
		
		List<String> ls= response.getItems();
		System.out.println("@@"+ls.size());
		System.out.println("@@"+response.getItems());
		this.nextToken = response.getNextToken();  
		System.out.println("@@"+this.nextToken); 
		
		if(ls.size()>0)
		{
			flag=1;
		}
		else
		{
			flag=0;
		}
		return flag;
		
	}

	public List<Others> getAllValues() {
		// TODO Auto-generated method stub
		int flag=0;
		SelectRequest selectRequest = new SelectRequest("select * from RemoteContactSharing").withConsistentRead( true );
		selectRequest.setNextToken( this.nextToken );
		
		SelectResult response = this.sdbClient.select( selectRequest );
		
		/*List<String> ls= response.getItems();  */
		
		return this.valuesGetting(response.getItems());
		
	}
	public List<Others> getDomainValues(String dname)
	{
		int flag=0;
		SelectRequest selectRequest = new SelectRequest("select * from "+dname).withConsistentRead( true );
		selectRequest.setNextToken( this.nextToken );
		
		SelectResult response = this.sdbClient.select( selectRequest );
		
		/*List<String> ls= response.getItems();  */
		
		return this.valuesGetting(response.getItems());
	}

	private List<Others> valuesGetting(List<Item> items) {
		// TODO Auto-generated method stub
		ArrayList<Others> alldata=new ArrayList<Others>(items.size());
		
		for(Item item:items)
		{
			alldata.add(this.individulaData(item));
		}
		
		
		return alldata;
	}

	private Others individulaData(Item item) {
		// TODO Auto-generated method stub
		return new Others(this.getusernames(item),this.getCheck(item));  
	}
	private String getCheck(Item item) {
		// TODO Auto-generated method stub
		return this.getAllStringAttribute(NUMBER_ATTRIBUTE,item.getAttributes());
	}



	private String getusernames(Item item) {
		// TODO Auto-generated method stub
		return this.getAllStringAttribute(NAME_ATTRIBUTE,item.getAttributes());
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

	public int getCount() {
		// TODO Auto-generated method stub

		if ( count < 0 ) {
			SelectRequest selectRequest = new SelectRequest( COUNT_QUERY ).withConsistentRead( true );
			List<Item> items = this.sdbClient.select( selectRequest ).getItems();	
			
			Item countItem = (Item)items.get(0);
			Attribute countAttribute = (Attribute)(((com.amazonaws.services.simpledb.model.Item) countItem).getAttributes().get(0));
			this.count = Integer.parseInt( countAttribute.getValue() );
		}

		return this.count;
	
	}
	
	  


	
	
}
