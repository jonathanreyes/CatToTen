package com.example.catto10;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {

	ImageButton sendBtn;
	EditText txtPhoneNo;
	EditText txtMessage;
	String mMessage;
	String mPhoneNum;
	public static int mNumAngryWords = 0;
	public static ArrayList<OffensivePhrase> mAngryWordsFound = new ArrayList<OffensivePhrase>();
	public static int offensiveRating = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sendBtn = (ImageButton) findViewById(R.id.btnSendSMS);
		//sendBtn.setBackgroundColor(color);
		txtPhoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
		txtMessage = (EditText) findViewById(R.id.editTextSMS);

		sendBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				if (txtPhoneNo.getText().toString().length() < 10){
					Toast.makeText(getApplicationContext(), "Enter a valid number", Toast.LENGTH_SHORT).show();
				} else if (txtMessage.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(), "Enter a message", Toast.LENGTH_SHORT).show();
				} else {

					sendSMSMessage();

					// clear the text fields of the sms app. if done somewhere else I need to 
					// have access to these variables in another class
					txtPhoneNo.setText("");
					txtMessage.setText("");
				}
			}
		});

	}
	protected void sendSMSMessage() {
		Log.i("Send SMS", "");

		mPhoneNum = txtPhoneNo.getText().toString();
		mMessage = txtMessage.getText().toString();


		//check the content of the message for any offensive language
		CheckMessageContent.checkMessageContent(mMessage);

		// Send the message through the native app on the phone if
		// not offensive.
		if (offensiveRating < 3){
			Intent smsIntent = new Intent(Intent.ACTION_VIEW);
			smsIntent.setData(Uri.parse("smsto:"));
			smsIntent.setType("vnd.android-dir/mms-sms");

			smsIntent.putExtra("address", mPhoneNum);
			smsIntent.putExtra("sms_body", mMessage);

			try {
				startActivity(smsIntent);
				finish();
				Log.i("Finished sending SMS...", "");
			} catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(MainActivity.this, 
						"SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
			}
		} 
		// launch dialog box from catToTen with Count down and edit message option
		else {
			WarningDialogBox mWarning= new WarningDialogBox(mMessage, mPhoneNum);
			mWarning.show(getFragmentManager(), WarningDialogBox.CHOOSER_TEXT);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.main_settings) {
			Intent mOpenDictionary = new Intent(MainActivity.this, DictionaryListActivity.class);
			startActivity(mOpenDictionary);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		//reset the static variables 
		mNumAngryWords = 0;
		offensiveRating = 0;
		mAngryWordsFound.clear();
	}
}