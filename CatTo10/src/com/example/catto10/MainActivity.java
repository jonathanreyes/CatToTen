package com.example.catto10;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ActionBar;
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

	public static int mNumAngryWords = 0;
	public static ArrayList<OffensivePhrase> mAngryWordsFound = new ArrayList<OffensivePhrase>();
	public static double offensiveRating = 0;
	public static final double ANGER_THRESHOLD = 5; 
	
	private ImageButton sendBtn;
	private EditText txtPhoneNum;
	private EditText txtMessage;
	private String mMessage;
	private String mPhoneNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sendBtn = (ImageButton) findViewById(R.id.btnSendSMS);
		txtPhoneNum = (EditText) findViewById(R.id.editTextPhoneNo);
		txtMessage = (EditText) findViewById(R.id.editTextSMS);

		sendBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				if (txtPhoneNum.getText().toString().length() < 10){

					showToast("Enter a valid number");
				} else if (txtMessage.getText().toString().equals("")){

					showToast("Enter a message");
				} else {

					checkSMSMessage();
					// Clear the text fields of the sms app.
					txtPhoneNum.setText("");
					txtMessage.setText("");
				}
			}
		});
	}

	protected void checkSMSMessage() {

		mPhoneNum = txtPhoneNum.getText().toString();
		mMessage = txtMessage.getText().toString();

		// Reset the variables in the case that a new SMS is being created
		mNumAngryWords = 0;
		mAngryWordsFound.clear();
		offensiveRating = 0;

		// Check the content of the message for any offensive language
		CheckMessageContent temp = new CheckMessageContent(this);
		temp.checkMessageContent(mMessage);

		// Send the message through the native app on the phone if not offensive.
		if (offensiveRating < ANGER_THRESHOLD){
			sendMessage();
		} 
		// Launch dialog box warning the user of offensive material in message
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

	public void sendMessage(){

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

	public void showToast(String m){
		Toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();
	}
}