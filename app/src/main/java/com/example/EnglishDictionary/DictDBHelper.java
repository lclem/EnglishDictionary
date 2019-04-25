
package com.example.EnglishDictionary;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DictDBHelper extends SQLiteOpenHelper {
	
	private static DictDBHelper theInstance;

	   //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.example.EnglishDictionary/databases/";
 
    private static String DB_NAME = "en_dict.sqlite";

    private static int DB_VERSION = 2;
 
    private SQLiteDatabase myDataBase;
    
    private Context myContext;
 
    public static DictDBHelper getInstance(Context context) {
    	
    	if(theInstance == null)
    		theInstance = new DictDBHelper(context);
    	
    	return theInstance;
    	
    }

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    private DictDBHelper(Context context) {
 
    	super(context, DB_NAME, null, DB_VERSION);
		// DB_VERSION is an int,update it every new build
        myContext = context;
        
    }	
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    protected void createDataBase(boolean force) throws IOException {
 
    	boolean dbExist = !force && checkDataBase();
 
    	if(dbExist){
    		//do nothing - database already exist
			System.out.println("do nothing - database already exist");
    	}else{

        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
    			System.out.println("Some copy error occurred when copying dict db: " + e.toString());
        		throw new Error("Error copying database: " + e.toString());
 
        	}
    	}
 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	} catch(SQLiteException e){
 
    		//database does't exist yet.
			System.out.println("When trying to open dict db: " + e.toString());
 
    	}
 
    	if(checkDB != null)
    		checkDB.close();
 
    	return checkDB != null;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transferring bytestream.
     * */
    private void copyDataBase() throws IOException{

		System.out.print("copying database... ");

		//By calling this method and empty database will be created into the default system path
		//of your application so we are gonna be able to overwrite that database with our database.
		SQLiteDatabase db = this.getReadableDatabase();
		db.close(); // must close it otherwise it won't work: https://issuetracker.google.com/issues/80268903

		//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();

		System.out.println("done!");
 
    }
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }
 
    @Override
	public synchronized void close() {
 
    	if(myDataBase != null)
    		myDataBase.close();
 
    	super.close();
 
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		//db.execSQL(DATABASE_CREATE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(newVersion>oldVersion) {

			System.out.println("Upgrading dict db from version " + oldVersion + " to version " + newVersion);

			try {

				copyDataBase();

			} catch (IOException e) {

				System.out.println("Some copy error occurred when copying dict db withing onUpgrade: " + e.toString());
				throw new Error("Error copying database: " + e.toString());

			}

		}
	}
 
        // Add your public helper methods to access and get content from the database.
       // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
       // to you to create adapters for your views.
	
	/*
	public List<String> search(String text) {

		//"SELECT id, entry, word FROM entries WHERE word == ? ORDER BY id"

		List<String> results = new ArrayList<String>();

		try
		{
			//open database to query
			openDataBase();

			Cursor cursor = myDataBase.query("entries",
					new String[] { "_id", "entry", "word"},
					"word" + " like '%"+text+"%'",
					null ,
					null,
					null,
					"_id");

			//mapped all rows to data object
			if (cursor.moveToFirst())
			{
				do
				{

					String str = cursor.getString(cursor.getColumnIndex("entry"));
					results.add(str);

				} while (cursor.moveToNext());
			}
			//close cursor
			cursor.close();
		}
		catch(Exception ex)
		{
			System.out.println("DatabaseHelper.search()- : ex " + ex.getClass() +", "+ ex.getMessage());
		}
		//
		return results;

	}

	
	public Cursor fetchAll() {

		try
		{
			//open database to query
			openDataBase();

			Cursor cursor = myDataBase.query("entries",
					new String[] { "_id", "entry", "word"},
					"word" + " like '%'",
					null ,
					null,
					null,
					"_id");

			return cursor;

		}
		catch(Exception ex)
		{
			System.out.println("DatabaseHelper.fetchAll()- : ex " + ex.getClass() +", "+ ex.getMessage());
		}
		
		return null;

	}
	*/
	
}
