package com.example.EnglishDictionary;

import java.io.IOException;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class DictionaryActivity extends FragmentActivity
	implements LoaderManager.LoaderCallbacks<Cursor>, TextWatcher, OnItemClickListener {

	private static final String SEARCH_PARAM = "text";
	
	private EditText mFilter;
	
	private static final int DICTIONARY_LOADER = 0;
	
	SimpleCursorAdapter mAdapter;
	ListView mListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_dictionary);
		
		Bundle params = new Bundle();
		params.putString(SEARCH_PARAM, "");
		getSupportLoaderManager().initLoader(DICTIONARY_LOADER, params, this);
		
		//mProgressBar = (ProgressBar)findViewById(R.id.search_progress_bar);
		
		mListView = (ListView)findViewById(R.id.dictionary_entry_list);
		mFilter = (EditText)findViewById(R.id.edit_search);
		mFilter.addTextChangedListener(this);
		
		//Cursor cursor = dbHelper.fetchAll();
		final int[] to = new int[] {R.id.text_word};
		final String[] from = new String[]{DictionaryProvider.Contract.WORD_COLUMN};
		
		mAdapter = new SimpleCursorAdapter(
				this,
				R.layout.custom_dictionary_row,
				null,
				from,
				to);
		
		mListView.setAdapter(mAdapter);
		mListView.setTextFilterEnabled(true);
		mListView.setOnItemClickListener(this);
		//mListView.setEmptyView(emptyView);
		
		setTitle("Dictionary");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dictionary, menu);
		return true;
	}

	/*
    private static final String[] PROJECTION =
    {
    	DictionaryProvider.Contract._ID,
    	DictionaryProvider.Contract.ENTRY_COLUMN,
    	DictionaryProvider.Contract.WORD_COLUMN
    };
	*/
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		Uri uri = DictionaryProvider.Contract.ENTRIES_TABLE_CONTENTURI;
		String text = args.getString(SEARCH_PARAM);
		
		switch (id) {
		case DICTIONARY_LOADER:
			// Returns a new CursorLoader
			setProgressBarIndeterminateVisibility(true);
			return new CursorLoader(
					this,   // Parent activity context
					uri,        // Table to query
					new String[] {
							DictionaryProvider.Contract._ID,
							DictionaryProvider.Contract.WORD_COLUMN},     // Projection to return
					DictionaryProvider.Contract.WORD_COLUMN + " LIKE '"+text+"%'",            // No selection clause
					//"word BEGINS WITH '"+text+"'",
					null,            // No selection arguments
					null//DictionaryProvider.Contract.WORD_COLUMN            // Default sort order
					);
		default:
			// An invalid id was passed in
			return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		setProgressBarIndeterminateVisibility(false);		
		mAdapter.changeCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		setProgressBarIndeterminateVisibility(false);
		mAdapter.changeCursor(null);
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	
		//mAdapter.c
		
		Bundle params = new Bundle();
		params.putString(SEARCH_PARAM, mFilter.getText().toString());
		LoaderManager manager = getSupportLoaderManager();
		manager.restartLoader(DICTIONARY_LOADER, params, this);
		
		//mListView.setFilterText(mFilter.getText().toString());
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		
		//long id = mListView.getItemIdAtPosition(position);
		Cursor cursor = mAdapter.getCursor();
		cursor.moveToPosition(position);
		long id = cursor.getInt(0);
		String word = cursor.getString(1);
		
		Intent intent = new Intent(this, DictionaryDetailActivity.class);
		intent.putExtra(DictionaryDetailActivity.ENTRY_ID, id);
		intent.putExtra(DictionaryDetailActivity.ENTRY_WORD, word);
		startActivity(intent);

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_reload_dictionary:
			
			DictDBHelper db = DictDBHelper.getInstance(null);
			db.close();
			
			try {
				
				db.createDataBase(true);
				mAdapter.notifyDataSetChanged();
				
				Toast.makeText(getApplicationContext(), "Database reloaded", Toast.LENGTH_SHORT).show();
				
	    	} catch (IOException ioe) {
	    		throw new Error("Unable to create database");
	    	}

	    	try {
	    		db.openDataBase();
	    	}catch(SQLException sqle){
	    		throw sqle;
	    	}
		}
		return super.onOptionsItemSelected(item);
	}

}
