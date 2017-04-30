package com.amazon.aws.demo;

import java.util.ArrayList;
import java.util.List;

import android.widget.EditText;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;

public class RegisterList {

	AmazonSimpleDBClient sdbClient;
	String nextToken;
	protected int count;
	private static final String REG_DOMAIN = "ContactsReg";

	public RegisterList() {
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

	public void AddToTable(String name1, String password1, String phoneno) {
		// TODO Auto-generated method stub
		ReplaceableAttribute UserAttribute = new ReplaceableAttribute(
				"USERNAME", name1, Boolean.TRUE);
		ReplaceableAttribute Passworda = new ReplaceableAttribute("PASSWORD",
				password1, Boolean.TRUE);
		ReplaceableAttribute phonenn = new ReplaceableAttribute("PHONE_NO",
				phoneno, Boolean.TRUE);

		List<ReplaceableAttribute> attrs = new ArrayList<ReplaceableAttribute>(
				2);
		attrs.add(UserAttribute);
		attrs.add(Passworda);
		attrs.add(phonenn);
		PutAttributesRequest par = new PutAttributesRequest(REG_DOMAIN,
				phoneno, attrs);
		try {
			sdbClient.putAttributes(par);
		} catch (Exception exception) {
			System.out.println("EXCEPTION = " + exception);
		}
	}

	public int loginVerify(String usern, String passw) {
		// TODO Auto-generated method stub
		int flag = 0;
		SelectRequest selectRequest = new SelectRequest(
				"select * from ContactsReg where PHONE_NO='" + usern
						+ "' and PASSWORD='" + passw + "'")
				.withConsistentRead(true);
		selectRequest.setNextToken(this.nextToken);

		SelectResult response = this.sdbClient.select(selectRequest);
		List<String> ls = response.getItems();
		System.out.println("@@" + ls.size());
		System.out.println("@@" + response.getItems());
		this.nextToken = response.getNextToken();
		System.out.println("@@" + this.nextToken);

		if (ls.size() > 0) {
			flag = 1;
		} else {
			flag = 0;
		}
		return flag;

	}
	
	
}
