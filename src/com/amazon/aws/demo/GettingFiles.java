package com.amazon.aws.demo;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.amazon.aws.demo.s3.S3;
import com.amazonaws.javax.xml.stream.events.i;

public class GettingFiles {
	//public static String path="/mnt/sdcard/";
	protected static List<String> objectNameList;
	protected static ArrayList<String> audioNameList=new ArrayList<String>();
	protected static ArrayList<String> videoNameList=new ArrayList<String>();
	protected static ArrayList<String> imageNameList=new ArrayList<String>();
	protected static ArrayList<String> otherNameList=new ArrayList<String>();
	protected static ArrayList<String> audioMulNameList=new ArrayList<String>();
	protected static ArrayList<String> videoMulNameList=new ArrayList<String>();
	protected static ArrayList<String> imageMulNameList=new ArrayList<String>();
	protected static ArrayList<String> otherMulNameList=new ArrayList<String>();
	@SuppressWarnings("unchecked")
	List<String> list=new ArrayList();
	public static void getFiles(String bname){
		System.out.print(bname);
		objectNameList=S3.getObjectNamesForBucket("AndroidAmazon");
		for(int i=0;i<objectNameList.size();i++){
			if(objectNameList.get(i).endsWith("mp3")&& !audioNameList.contains(objectNameList.get(i).toString()) )
				audioNameList.add(objectNameList.get(i).toString());
			else
				if((objectNameList.get(i).endsWith("3gp")||objectNameList.get(i).endsWith("mp4") || objectNameList.get(i).endsWith("flv")|| objectNameList.get(i).endsWith("avi") ) && !videoNameList.contains(objectNameList.get(i).toString()))
					videoNameList.add(objectNameList.get(i).toString());
				else
					if((objectNameList.get(i).endsWith("jpg")||objectNameList.get(i).endsWith("png") || objectNameList.get(i).endsWith("jpeg")|| objectNameList.get(i).endsWith("JPG")) && !imageNameList.contains(objectNameList.get(i).toString()))
						imageNameList.add(objectNameList.get(i).toString());
					else
						if((objectNameList.get(i).endsWith("java")||objectNameList.get(i).endsWith("xml")||objectNameList.get(i).endsWith("doc")||objectNameList.get(i).endsWith("docx")) && !otherNameList.contains(objectNameList.get(i).toString()))
						otherNameList.add(objectNameList.get(i).toString());
			
		}
	
		
	}
	public static void removelist(){
	
			
			imageNameList.removeAll(imageNameList);
			audioNameList.removeAll(audioNameList);
			videoNameList.removeAll(videoNameList);
			otherNameList.removeAll(otherNameList);
			objectNameList.removeAll(objectNameList);
	}
	public static void getMultiImageFiles(){
		
		  File files = new File("/sdcard/");

	        FileFilter filter = new FileFilter() {

	        	   private final List<String> exts = Arrays.asList("jpg", "png","JPG");
	            public boolean accept(File pathname) {
	                String ext;
	                String path = pathname.getPath();
	                ext = path.substring(path.lastIndexOf(".") + 1);
	                return exts.contains(ext);
	            }
	        };

	        final File [] filesFound = files.listFiles(filter);
	        final ArrayList<String> list = new ArrayList<String>();
	        if (filesFound != null && filesFound.length > 0) {
	            for (File file : filesFound) {
	            	Log.d("aesdfsds",file.getName());
	            }
	        }
       
	}
	public static void getMultiAudioFiles(){

        File files = new File("/mnt/sdcard/");

        FileFilter filter = new FileFilter() {

            private final List<String> exts = Arrays.asList("mp3", "mp4");

            public boolean accept(File pathname) {
                String ext;
                String path = pathname.getPath();
                ext = path.substring(path.lastIndexOf(".") + 1);
                return exts.contains(ext);
            }
        };

        final File [] filesFound = files.listFiles(filter);
        if (filesFound != null && filesFound.length > 0) {
            for (File file : filesFound) {
            	Log.d("aesdfsds",file.getName());
               audioMulNameList.add(file.getName());
            }
        }
	}

        public static void getVideoAudioFiles(){

            File files = new File("/mnt/sdcard/");

            FileFilter filter = new FileFilter() {

                private final List<String> exts = Arrays.asList("mp4", "flv","3gp","avi");

                public boolean accept(File pathname) {
                    String ext;
                    String path = pathname.getPath();
                    ext = path.substring(path.lastIndexOf(".") + 1);
                    return exts.contains(ext);
                }
            };

            final File [] filesFound = files.listFiles(filter);
            if (filesFound != null && filesFound.length > 0) {
                for (File file : filesFound) {
                	Log.d("aesdfsds",file.getName());
                   videoMulNameList.add(file.getName());
                }
            }

           
	}
        public static void getOtherAudioFiles(){

            File files = new File("/mnt/sdcard/");

            FileFilter filter = new FileFilter() {

                private final List<String> exts = Arrays.asList("docx", "xml","doc", "csv", "txt");

                public boolean accept(File pathname) {
                    String ext;
                    String path = pathname.getPath();
                    ext = path.substring(path.lastIndexOf(".") + 1);
                    return exts.contains(ext);
                }
            };

            final File [] filesFound = files.listFiles(filter);
            if (filesFound != null && filesFound.length > 0) {
                for (File file : filesFound) {
                	Log.d("aesdfsds",file.getName());
                   videoMulNameList.add(file.getName());
                }
                
            }

           
	}
        public static int updateSize1(File file){
        	int size=0,remain=0;
           	size= (int)file.length();
           
           	System.out.println("file sizeeeeeeeeee "+size);
           
          
        	int size2=Integer.parseInt(GetbucketName.size);
           
        	
        	System.out.println("bucket sizeeeeeeeeee"+size2);
        	remain=size2-size;
        	 
     		System.out.println("remain      "+remain);
     			return remain;

     		
			        	
        }        public static void updateFile(FileOutputStream fos, int size2){
        	
        	Date d=new Date();
			/*int day=d.getDate();
			int mnth=d.getMonth()+1;
			
			int yy=1900+d.getYear()+1;
			String sstr=Integer.toString(day)+"-"+Integer.toString(mnth)+"-"+Integer.toString(yy);*/
        	final String date=GetbucketName.date;
    		final String bname=GetbucketName.getFoldername();
    		GetbucketName.setSize(Integer.toString(size2));
    		String str=bname+"~"+date+"~"+size2;
    		System.out.println("stringgggggggggggg "+str);
    		try {
				fos.write(str.getBytes());
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        public static int updateSize(ArrayList list){
        	
        	 int size=0;
        	 for(int i=0;i<list.size();i++){
        		 final File f=new File(list.get(i).toString());
        		 size+=f.length();
        			 }
        	
        	   int size2=Integer.parseInt(GetbucketName.size);
        		final int remain=size2-size;
        		
			return remain;
        }
       
        public static void updateDownoadSize(File file,FileOutputStream fos){
        	 int size=0;
        	 
        		// final File f=new File("/sdcard/AWS/aws"+);
        		
        		int size2=Integer.parseInt(GetbucketName.size);
           		final String date=GetbucketName.date;
           		final String bname=GetbucketName.getFoldername();
           		final int remain=size2+size;
           		GetbucketName.setSize(Integer.toString(remain));
           		if(remain>0){
           			String str=bname+"~"+date+"~"+remain;
           			try {
   						fos.write(str.getBytes());
   						fos.close();
   					} catch (IOException e) {
   						// TODO Auto-generated catch block
   						e.printStackTrace();
   					}
           		}
        	 
        }
        public static boolean verifyFile(String file){
            File files = new File("/sdcard/");
            String[] ss=files.list();
            boolean flag=true;
         for(int i=0;i<ss.length;i++){
        	 if(file.equals(ss[i])){
        		 flag=false;
        		 break;
        	 }
        	
        		 
         }
         return flag;
        	
        }
        public static String verifyDate(int day,int month,int year){
        	int  ageYears = 0, ageMonths=0, ageDays=0;
        	Date d=new Date();
    		String mydate="14-Sep-11";
    		 DateFormat formatter ; 
    	      formatter = new SimpleDateFormat("dd-MMM-yy");
    	      Calendar cd = Calendar.getInstance();
    			try {
					Date mydate2=formatter.parse(mydate);
					
					
					Calendar bd = new GregorianCalendar(year, month, day);
					ageYears = cd.get(Calendar.YEAR) - bd.get(Calendar.YEAR);
					if(cd.before(new GregorianCalendar(cd.get(Calendar.YEAR), month, day))){
						ageYears--;
						ageMonths = (12 - (bd.get(Calendar.MONTH) + 1)) + (bd.get(Calendar.MONTH));
						if(day > cd.get(Calendar.DAY_OF_MONTH))
							ageDays = day - cd.get(Calendar.DAY_OF_MONTH);
						else if(day < cd.get(Calendar.DAY_OF_MONTH))
							ageDays = cd.get(Calendar.DAY_OF_MONTH) - day;
						else
							ageDays = 0;
					}
					else if(cd.after(bd)){
						ageMonths = (12 - (bd.get(Calendar.MONTH) + 1)) - 1-2;
						if(day > cd.get(Calendar.DAY_OF_MONTH))
							ageDays = day - cd.get(Calendar.DAY_OF_MONTH) - day;
						else if(day < cd.get(Calendar.DAY_OF_MONTH))
							ageDays = cd.get(Calendar.DAY_OF_MONTH) - day;
						else
							ageDays = 0;
					}
					else{
						ageYears = cd.get(Calendar.YEAR) - bd.get(Calendar.YEAR);
						ageMonths = 0;
						ageDays = 0;
					}
					System.out.print("Age of the person : " + ageYears + " year, " + ageMonths + " months and " + ageDays + " days.");
				
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return "Age of the person : " + ageYears + " year, " + ageMonths + " months and " + ageDays + " days.";
        	
        }
		public static int updateSize2(ArrayList list) {
			int size=0;
       	 for(int i=0;i<list.size();i++){
       		 final File f=new File("/sdcard/"+list.get(i).toString());
       		 size+=f.length();
       			 }
       	
       	   int size2=Integer.parseInt(GetbucketName.size);
       		final int remain=size2-size;
       		
			return remain;
		}
       
}
