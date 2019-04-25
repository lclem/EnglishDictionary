package com.example.EnglishDictionary;

import java.io.IOException;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.SparseArray;

public class DictionaryProvider extends ContentProvider {
	
	public static final int DICTIONARY_URL_QUERY = 0;
    public static final int INVALID_URI = -1;

    public static final String LOG_TAG = "DictionaryProvider";

    private DictDBHelper mHelper;
    
    static public final class Contract implements BaseColumns {
    
        private Contract() { }

        public static final String SCHEME = "content";
        public static final String AUTHORITY = "com.example.EnglishDictionary";
        
        public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY);;

        public static final String MIME_TYPE_ROWS =
                "vnd.android.cursor.dir/vnd.com.example.EnglishDictionary";

        public static final String MIME_TYPE_SINGLE_ROW =
                "vnd.android.cursor.item/vnd.com.example.EnglishDictionary";

        /**
         * Picture table primary key column name
         */
        public static final String ROW_ID = BaseColumns._ID;
        
        /**
         * Picture table name
         */
        public static final String ENTRIES_TABLE_NAME = "entries";

        /**
         * Picture table content URI
         */
        public static final Uri ENTRIES_TABLE_CONTENTURI =
                Uri.withAppendedPath(CONTENT_URI, ENTRIES_TABLE_NAME);

        public static final String WORD_COLUMN = "word";
        public static final String ENTRY_COLUMN = "entry";
        
        // The content provider database name
        //public static final String DATABASE_NAME = "PictureDataDB";

        // The starting version of the database
        //public static final int DATABASE_VERSION = 1;
           
    }
    
    private static final UriMatcher sUriMatcher;
    private static final SparseArray<String> sMimeTypes;

    static {
    	
    	System.out.println("DictionaryProvider: static initialization...");
        
        // Creates an object that associates content URIs with numeric codes
        sUriMatcher = new UriMatcher(0);

        /*
         * Sets up an array that maps content URIs to MIME types, via a mapping between the
         * URIs and an integer code. These are custom MIME types that apply to tables and rows
         * in this particular provider.
         */
        sMimeTypes = new SparseArray<String>();

        // Adds a URI "match" entry that maps picture URL content URIs to a numeric code
        sUriMatcher.addURI(
                Contract.AUTHORITY,
                Contract.ENTRIES_TABLE_NAME,
                DICTIONARY_URL_QUERY);
        
    }
    
    @Override
	public String getType(Uri uri) {
		return sMimeTypes.get(sUriMatcher.match(uri));
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
    	throw new UnsupportedOperationException("Delete -- unsupported operation " + uri);
    }	

    @Override
    public Uri insert(Uri uri, ContentValues values) {
    	throw new UnsupportedOperationException("Insert -- unsupported operation " + uri);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    	throw new UnsupportedOperationException("Update -- unsupported operation " + uri);
    }
		

    @Override
    public boolean onCreate() {

    	System.out.println("DictionaryProvider: onCreate");
        
    	mHelper = DictDBHelper.getInstance(getContext());

    	try {

    		mHelper.createDataBase(false);

    	} catch (IOException ioe) {

    		throw new Error("Unable to create database");

    	}

    	try {

    		mHelper.openDataBase();

    	}catch(SQLException sqle){

    		throw sqle;

    	}

    	//this.
    	
    	return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
    	System.out.println("DictionaryProvider: query");

		switch (sUriMatcher.match(uri)) {

		// If the query is for a picture URL
		case DICTIONARY_URL_QUERY:
		{

			SQLiteDatabase db = mHelper.getReadableDatabase();

			// Does the query against a read-only version of the database
			Cursor returnCursor = db.query(
					Contract.ENTRIES_TABLE_NAME,
					projection,
					selection, selectionArgs, null, null, sortOrder);

			// Sets the ContentResolver to watch this content URI for data changes
			returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
			
			return returnCursor;
		}
		case INVALID_URI:
			throw new IllegalArgumentException("Query -- Invalid URI:" + uri);
		}

		return null;
	}

	// Closes the SQLite database helper class, to avoid memory leaks
    public void close() {
        mHelper.close();
    }

}
