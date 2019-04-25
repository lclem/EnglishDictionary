package com.example.EnglishDictionary;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;
//import com.google.gdata.data.spreadsheet.ListFeed;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.EnglishDictionary.MESSAGE";


	private static final int NOTE_ID = 100;

	private Runnable task = new Runnable() {
		@Override
		public void run() {
			NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			Intent launchIntent = new Intent(getApplicationContext(), MainActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, launchIntent, 0);

			//Create notification with the time it was fired
			Notification note = new Notification(R.drawable.ic_launcher, "Something Happened",	System.currentTimeMillis());

			// TODO: commented out, it didn't compile otherwise
			//Set notification information
			//note.setLatestEventInfo(getApplicationContext(), "We're Finished!", "Click Here!", contentIntent);
			note.defaults |= Notification.DEFAULT_SOUND;
			//note.defaults |= Notification.DEFAULT_VIBRATE;
			note.defaults |= Notification.DEFAULT_LIGHTS;
			note.flags |= Notification.FLAG_AUTO_CANCEL;
			manager.notify(NOTE_ID, note);
		}
	};

	WebView wv;
		
	public final String ITEM_FORMAT  = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd\" xml:lang=\"en\">	<head>		<title>(Oxford) myDict of English</title>		<link rel=\"stylesheet\" type=\"text/css\" href=\"styles_common.css\"/>		<link rel=\"stylesheet\" type=\"text/css\" href=\"styles_targeted.css\"/>		<script type=\"text/javascript\" src=\"jquery-1.2.6.js\"></script>		<script type=\"text/javascript\" src=\"jquery-color-animation.js\"></script>		<script type=\"text/javascript\" src=\"common.js\"></script>	</head>	<body>		<dictionary xml:space=\"preserve\">			%s			</dictionary>	</body></html>";
	/*public final String ITEM_FORMAT  = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd\" xml:lang=\"en\">" +
			"<head>		<title>(Oxford) myDict of English</title>		" +
			"<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/styles_common.css\"/>		" +
			"<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/styles_targeted.css\"/>		" +
			"<script type=\"text/javascript\" src=\"file:///android_asset/jquery-1.2.6.js\"></script>		" +
			"<script type=\"text/javascript\" src=\"file:///android_asset/jquery-color-animation.js\"></script>		" +
			"<script type=\"text/javascript\" src=\"file:///android_asset/common.js\"></script>	</head>	" +
			"<body>		<dictionary xml:space=\"preserve\">			%s			</dictionary>	</body></html>";*/
	public final String ITEM_FORMAT_WITH_COMMENTS = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd\" xml:lang=\"en\">	<head>		<title>(Oxford) myDict of English</title>		<link rel=\"stylesheet\" type=\"text/css\" href=\"styles_common.css\"/>		<link rel=\"stylesheet\" type=\"text/css\" href=\"styles_targeted.css\"/>		<script type=\"text/javascript\" src=\"jquery-1.2.6.js\"></script>		<script type=\"text/javascript\" src=\"jquery-color-animation.js\"></script>		<script type=\"text/javascript\" src=\"common.js\"></script>	</head>	<body>		<dictionary xml:space=\"preserve\">			%s			</dictionary> <ff>COMMENTS</ff> <div> %s </div>	</body></html>";

	public final String entryExample = 
			"<e url=\"x543390\" type=\"subject\">" +
			"<h><hw>music</hw>" +
			"<audio word='music' translit=''/>" +
			"</h>" +
			"<sb url=\"x580287\">" +
			"<pl><ps>noun</ps>" +
			"<gramGrp><sy>mass noun</sy></gramGrp></pl>" +
			"<se url=\"x580288\">" +
			"<d url=\"x580289\">the art or science of combining vocal or instrumental sounds (or both) to produce beauty of form, harmony, and expression of emotion:</d>" +
			"<ex url=\"x580290\">he devoted his life to music.</ex>" +
			"<specUse><MS url=\"x580291\" core=\"no\">" +
			"<d url=\"x580292\">the vocal or instrumental sound produced in this way:</d>" +
			"<ex url=\"x580293\">couples were dancing to the music</ex> | " +
			"<ex url=\"x580294\">baroque music.</ex></MS>" +
			"<MS url=\"x580295\" core=\"no\">" +
			"<d url=\"x580296\">a sound perceived as pleasingly harmonious:</d>" +
			"<ex url=\"x580297\">the background music of softly lapping water.</ex></MS></specUse></se>" +
			"<se url=\"x580298\"><d url=\"x580299\">the written or printed signs representing vocal or instrumental sound:</d>" +
			"<ex url=\"x580300\">Tony learned to <b>read music</b>.</ex><specUse>" +
			"<MS url=\"x580301\" core=\"no\">" +
			"<d url=\"x580302\">the score or scores of a musical composition or compositions:</d>" +
			"<ex url=\"x580303\">the music was open on a stand.</ex></MS></specUse></se></sb>" +
			"<phrBlock><sub><l>music of the spheres</l>" +
			"<xrefGrp>see <xref><x>sphere</x></xref>.</xrefGrp></sub>" +
			"<sub><l>music to one's ears</l>" +
			"<d url=\"x580304\">something that is very pleasant or gratifying to hear or discover:</d>" +
			"<ex url=\"x580305\">the commission's report was music to the ears of the government.</ex></sub></phrBlock>" +
			"<eb><date><lang>Middle English</lang></date>: from <lang>Old French</lang> <ff>musique</ff>, via <lang>Latin</lang> from <lang>Greek</lang> <ff>mousik&#x0113; (tekhn&#x0113;)</ff> <trans>(art) of the Muses</trans>, from <ff>mousa</ff> <trans>muse</trans>.</eb></e>";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		//wv = (WebView)findViewById(R.id.myWebView);
		//String string = String.format(ITEM_FORMAT, entryExample);

		showDictionary(null);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();    	
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public void showDictionary(View v) {

		startActivity(new Intent(MainActivity.this, DictionaryActivity.class));
		
	}

}
