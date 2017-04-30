package com.amazon.aws.demo.database;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class DBAdapterSongs 
{
 	
	
    public static final String KEY_ROWID = "Name";
    public static final String KEY_ISBN = "isbn";
    public static final String KEY_TITLE = "title";
    public static final String KEY_PUBLISHER = "publisher";    
    private static final String TAG = "DBAdapter";
    public ArrayList<String> list=new ArrayList<String>();
    private static final String DATABASE_NAME = "AWSDatabase";
    private static String DATABASE_TABLE = null;
    private static final int DATABASE_VERSION = 1;
 
    private static final String DATABASE_CREATE =
        "create table AWSImages (_id integer primary key autoincrement, "
        + "Name text);";
    private static final String DATABASE_CREATE2 =
        "create table AWSSongs (_id integer primary key autoincrement, "
        + "Name text);";
    private static final String DATABASE_CREATE3 =
        "create table AWSVideos (_id integer primary key autoincrement, "
        + "Name text);";
    private static final String DATABASE_CREATE4 =
        "create table AWSOthers (_id integer primary key autoincrement, "
        + "Name text);";
 
    private final Context context; 
 
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
 
    public DBAdapterSongs(Context ctx,String tableName) 
    { 
    	DBAdapterSongs.DATABASE_TABLE=tableName;
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
   
 
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
 
        @Override
        public void onCreate(SQLiteDatabase db) 
        {
        	System.out.println("FOR main----------------------------");
            db.execSQL(DATABASE_CREATE);
          	System.out.println("table createddd---------------------------");
            db.execSQL(DATABASE_CREATE2);
            db.execSQL(DATABASE_CREATE3);
            db.execSQL(DATABASE_CREATE4);
        }
 
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion 
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS titles");
            onCreate(db);
        }
    }    
 
    //---opens the database---
    public DBAdapterSongs open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }
 
    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
 
    //---insert a title into the database---
    public long insertTitle(String imageName) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROWID, imageName);
       
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
 
    //---deletes a particular title---
    public boolean deleteTitle(long rowId) 
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + 
        		"=" + rowId, null) > 0;
    }
 
    //---retrieves all the titles---
    public ArrayList<String> getAllTitles() 
    {
    	 Cursor c = db.rawQuery("SELECT * FROM " +DATABASE_TABLE +"", null);
    	 if (c != null ) {
	    		if  (c.moveToFirst()) {
	    			do {
	    				String name = c.getString(c.getColumnIndex("Name"));
	    				list.add(name);
	    				
	    			}while (c.moveToNext());
	    		} }
        return list;
    }
 
    //---retrieves a particular title---
    public Cursor getTitle(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                		KEY_ROWID,
                		KEY_ISBN, 
                		KEY_TITLE,
                		KEY_PUBLISHER
                	}, 
                		KEY_ROWID + "=" + rowId, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
 
    //---updates a title---
    public boolean updateTitle(long rowId, String isbn, 
    String title, String publisher) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_ISBN, isbn);
        args.put(KEY_TITLE, title);
        args.put(KEY_PUBLISHER, publisher);
        return db.update(DATABASE_TABLE, args, 
                         KEY_ROWID + "=" + rowId, null) > 0;
    }
}