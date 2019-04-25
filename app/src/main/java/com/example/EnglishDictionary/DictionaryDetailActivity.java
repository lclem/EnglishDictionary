package com.example.EnglishDictionary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class DictionaryDetailActivity extends Activity {
	
	public static final String ENTRY_ID = "entry_id"; 
	public static final String ENTRY_WORD = "entry_word"; 	
	
	public final String ITEM_FORMAT  = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd\" xml:lang=\"en\">	<head>		<title>(Oxford) myDict of English</title>		<link rel=\"stylesheet\" type=\"text/css\" href=\"styles_common.css\"/>		<link rel=\"stylesheet\" type=\"text/css\" href=\"styles_targeted.css\"/>		<script type=\"text/javascript\" src=\"jquery-1.2.6.js\"></script>		<script type=\"text/javascript\" src=\"jquery-color-animation.js\"></script>		<script type=\"text/javascript\" src=\"common.js\"></script>	</head>	<body>		<dictionary xml:space=\"preserve\">			%s			</dictionary>	</body></html>";

	WebView webView;
	String word;
	
	private WebViewClient mClient = new WebViewClient() {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			
			Uri request = Uri.parse(url);
			word = request.getLastPathSegment();
			
			//if(TextUtils.equals(request.getAuthority(), "www.google.com")) {
				//Allow the load
			//	return false; 
			//}
			Toast.makeText(DictionaryDetailActivity.this, "Loading word " + word, Toast.LENGTH_LONG).show();
			
			DictDBHelper helper = DictDBHelper.getInstance(DictionaryDetailActivity.this);
			SQLiteDatabase db = helper.getReadableDatabase();
			
			Cursor cursor = db.query(
					DictionaryProvider.Contract.ENTRIES_TABLE_NAME,
					new String[] {
							DictionaryProvider.Contract._ID,
							//DictionaryProvider.Contract.WORD_COLUMN,
							DictionaryProvider.Contract.ENTRY_COLUMN},     // Projection to return
				    DictionaryProvider.Contract.WORD_COLUMN + "='" + word + "'",            // 
					null, null, null, null);
			
			if (cursor.moveToFirst()) {
				
				String data = new String();
				
				do {

					String entry = cursor.getString(cursor.getColumnIndex(DictionaryProvider.Contract.ENTRY_COLUMN));
					data = data + entry;
				
				} while (cursor.moveToNext());
				
				String html = String.format(ITEM_FORMAT, data);
				
				webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
				
			}
			
			return true;
			
		}
	};

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dicitonary_detail);
		
		webView = (WebView)findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(mClient);
		
		DictDBHelper helper = DictDBHelper.getInstance(this);
		SQLiteDatabase db = helper.getReadableDatabase();
		
		Intent intent = getIntent();
		long id = intent.getLongExtra(ENTRY_ID, -1);
		word = intent.getStringExtra(ENTRY_WORD);
		
		//System.out.println("DictionaryDetailActivity using id = " + id);

		// Does the query against a read-only version of the database
		Cursor cursor = db.query(
				DictionaryProvider.Contract.ENTRIES_TABLE_NAME,
				new String[] {
						DictionaryProvider.Contract._ID,
						DictionaryProvider.Contract.ENTRY_COLUMN},
						//DictionaryProvider.Contract._ID + "='" +id + "'",
						DictionaryProvider.Contract.WORD_COLUMN + "='" + word + "'",
						null, null, null, null);

		/*
		if (cursor.moveToFirst()) {
			
			String entry = cursor.getString(cursor.getColumnIndex(DictionaryProvider.Contract.ENTRY_COLUMN));
			String data = String.format(ITEM_FORMAT, entry);
			
			webView.loadDataWithBaseURL("file:///android_asset/", data, "text/html", "utf-8", null);
			//webView.loadData(entry, "text/html", "utf-8");
			
			System.out.println(entry);
			
		}
		else
			System.out.println("DictionaryDetailActivity entry not found??");
		*/
		
		if (cursor.moveToFirst()) {
			
			String data = new String();
			int n = cursor.getCount();
			int i = 1;
			
			do
			{

				String entry = cursor.getString(cursor.getColumnIndex(DictionaryProvider.Contract.ENTRY_COLUMN));
				System.out.println(entry);
				
				if(n > 1)
					data = data + "<h>" + i + "</h>"; 
					
				data = data + entry;
				
				i++;
			
			} while (cursor.moveToNext());
			
			String html = String.format(ITEM_FORMAT, data);
			webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);

			
		}

		db.close();
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dicitonary_detail, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
