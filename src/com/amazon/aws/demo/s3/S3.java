/*
 * Copyright 2010-2011 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazon.aws.demo.s3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import android.util.Log;


import com.amazon.aws.demo.AWSMainScreen;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3 {

	private static AmazonS3 s3 = null;
	private static ObjectListing objListing = null;
	public static final String BUCKET_NAME = "_bucket_name";
	public static final String OBJECT_NAME = "_object_name";
	
	static {
		System.setProperty("org.xml.sax.driver","org.xmlpull.v1.sax2.Driver");
		try {
			@SuppressWarnings("unused")
			XMLReader reader = XMLReaderFactory.createXMLReader();
		}
		catch ( SAXException e ) {
			Log.e( "SAXException", e.getMessage() );
		}
	}
		
	public static AmazonS3 getInstance() {
        if ( s3 == null ) {
		    s3 = new AmazonS3Client( AWSMainScreen.credentials );
        }

        return s3;
	}

	@SuppressWarnings("unchecked")
	public static List<String> getBucketNames() {
		List buckets = getInstance().listBuckets();
		
		List<String> bucketNames = new ArrayList<String>( buckets.size() );
		Iterator<Bucket> bIter = buckets.iterator();
		while(bIter.hasNext()){
			bucketNames.add((bIter.next().getName()));
		}
		return bucketNames;
	}

	@SuppressWarnings("unchecked")
	public static List<String> getObjectNamesForBucket( String bucketName ) {
		ObjectListing objects = getInstance().listObjects( bucketName );
		objListing = objects;
		List<String> objectNames = new ArrayList<String>( objects.getObjectSummaries().size() );
		Iterator<S3ObjectSummary> oIter = objects.getObjectSummaries().iterator();
		while(oIter.hasNext()){
			objectNames.add(oIter.next().getKey());
		}
		return objectNames;		
	}	
	
	@SuppressWarnings("unchecked")
	public static List<String> getObjectNamesForBucket( String bucketName , int numItems) {
		ListObjectsRequest req= new ListObjectsRequest();
		req.setMaxKeys(new Integer(numItems));
		req.setBucketName(bucketName);
		ObjectListing objects = getInstance().listObjects( req );
		objListing = objects;
		List<String> objectNames = new ArrayList<String>( objects.getObjectSummaries().size());
		Iterator<S3ObjectSummary> oIter = objects.getObjectSummaries().iterator();
		while(oIter.hasNext()){
			objectNames.add(oIter.next().getKey());
		}

		return objectNames;		
	}	
	
	@SuppressWarnings("unchecked")
	public static List<String> getMoreObjectNamesForBucket() {
		try{
			ObjectListing objects = getInstance().listNextBatchOfObjects(objListing);
			objListing = objects;
			List<String> objectNames = new ArrayList<String>( objects.getObjectSummaries().size());
			Iterator<S3ObjectSummary> oIter = objects.getObjectSummaries().iterator();
			while(oIter.hasNext()){
				objectNames.add(oIter.next().getKey());
			}
			return objectNames;
		} catch (NullPointerException e){
			return new ArrayList<String>();
		}

	}	
	/*public static void createBucket( String bucketName ) {
		getInstance().createBucket( bucketName );
	}*/		
	public static void createFolder( String folderName ) {
		
		getInstance().putObject("AndroidAmazon",folderName+"/",  new ByteArrayInputStream(new byte[0]), null);
	}
public static void createFolder2( String folderName ) {
		
		getInstance().putObject("donotdelete",folderName+"/",  new ByteArrayInputStream(new byte[0]), null);
	}
	public static void deleteBucket( String bucketName ) {
		getInstance().deleteBucket(  bucketName );
	}
	public static void getObject(String bucketName, String key) {
		
	}

	public static boolean createObjectForBucket( String bucketName, String objectName, File data ) {
		try {
			System.out.println("object name      "+objectName+"bucketName  "+bucketName+"File   "+data);
			PutObjectRequest putObject = new PutObjectRequest(bucketName, objectName, data);
			ObjectMetadata metaData = new ObjectMetadata();		
			metaData.setContentType("application/octet-stream"); //binary data
			System.out.println("File size " + data.length());
			System.out.println("object name      "+objectName+"bucketName  "+bucketName+"File   "+data);
			putObject.setMetadata(metaData);
			System.out.println("File size1 " + data.length());
			System.out.println("object name      "+objectName+"bucketName  "+bucketName+"File   "+data);
			getInstance().putObject(bucketName, objectName, data);
			
			System.out.println("File size2 " + data.length());
			System.out.println("object name      "+objectName+"bucketName  "+bucketName+"File   "+data);
			
			/*ByteArrayInputStream bais = new ByteArrayInputStream( data.getBytes() );
			ObjectMetadata metaData = new ObjectMetadata();		
			metaData.setContentType("application/octet-stream");
			metaData.setContentLength( data.getBytes().length );
			getInstance().putObject(bucketName, objectName, bais, metaData );*/
			return true;
		}
		catch ( Exception exception ) {
			Log.e( "TODO", "createObjectForBucket"+exception );
			return false;
		}
		
	}
	
	public static void deleteObject( String bucketName, String objectName ) {
		getInstance().deleteObject( bucketName, objectName );
	}

	public static InputStream getDataForObject( String bucketName, String objectName ) {
		return  getInstance().getObject( bucketName, objectName ).getObjectContent() ;
	}
	public static String getDataForObject2( String bucketName, String objectName ) {
		//Log.d("Stringzdsa", objectName);
Log.d("object name", objectName);
		return read(getInstance().getObject( bucketName, objectName ).getObjectContent(),objectName);
	}
	public static InputStream getImageForObject(String bucketName, String objectName ){
		
		return getInstance().getObject( bucketName, objectName ).getObjectContent();
		
	}
	public static String getDataForObject3( String bucketName, String objectName ) {
		//Log.d("Stringzdsa", objectName);
Log.d("object name", objectName);
		return read2(getInstance().getObject( bucketName, objectName ).getObjectContent(),objectName);
	}
	
	protected static String read( InputStream stream, String objectName ) {
		try {
			Log.d("inputstream", stream.toString());
			ByteArrayOutputStream baos = new ByteArrayOutputStream( 8196 );
			File f=new File("/sdcard/AWSOthers"+objectName);
			
			OutputStream out=new FileOutputStream(f);
			byte buf[]=new byte[1024];
			  int len;
			  while((len=stream.read(buf))>0)
			  out.write(buf,0,len);
			  out.close();
			  stream.close();
			  System.out.println("\nFile is created........	...........................");
			byte[] buffer = new byte[1024];
			int length = 0;
			while ( ( length = stream.read( buffer ) ) > 0 ) {
				baos.write( buffer, 0, length );
			}
		return baos.toString();
		}
		catch ( Exception exception ) {
			return exception.getMessage();
		}
	}
	protected static String read2( InputStream stream, String objectName ) {
		try {
			Log.d("inputstream", stream.toString());
			ByteArrayOutputStream baos = new ByteArrayOutputStream( 8196 );
			
			byte[] buffer = new byte[1024];
			int length = 0;
			while ( ( length = stream.read( buffer ) ) > 0 ) {
				baos.write( buffer, 0, length );
			}
		return baos.toString();
		}
		catch ( Exception exception ) {
			return exception.getMessage();
		}
	}
}
