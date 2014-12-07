package com.example.catto10;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditMessageFragment extends DialogFragment {

	static private final int GET_TEXT_REQUEST_CODE = 1;

	static final String CHOOSER_TEXT = "Load with:";

	private Button mDiscard;
	private Button mSend;
	String mMessage;
	String mPhoneNum;

	public EditMessageFragment(String m, String pNum){
		mMessage = m;
		mPhoneNum = pNum;
	}
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.edit_message, null);

		// Set and change the size of the view
		builder.setTitle(R.string.words_detected).setView(dialogView)
		.setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.setNegativeButton(R.string.send, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//TODO have to do another check to see if the new message has 
				// any offensive content.  (method currently exists in Main Activity)

				EditText mEditMessageView = (EditText) ((Dialog)dialog).findViewById(R.id.user_message_edit);
				String mEditMessage = mEditMessageView.getText().toString();

				CheckMessageContent.checkMessageContent(mEditMessage);
				if (MainActivity.offensiveRating < 2){

					Intent smsIntent = new Intent(Intent.ACTION_VIEW);
					smsIntent.setData(Uri.parse("smsto:"));
					smsIntent.setType("vnd.android-dir/mms-sms");

					smsIntent.putExtra("address", mPhoneNum);
					smsIntent.putExtra("sms_body", mEditMessage);

					try {
						startActivity(smsIntent);
						Log.i("Finished sending SMS...", "");
					} catch (android.content.ActivityNotFoundException ex) {
						Toast.makeText(null, 
								"SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
					}
				} else {
					WarningDialogBox mWarning= new WarningDialogBox(mEditMessage, mPhoneNum);
					mWarning.show(getFragmentManager(), WarningDialogBox.CHOOSER_TEXT);
				}
				dialog.cancel();
			}
		});
		
		final AlertDialog dialog = builder.show();

		// Set text message to send to user 
		TextView mPhrasesFound = (TextView) dialog.findViewById(R.id.words_found);
		String offensiveWords= ""; 
		for(OffensivePhrase phrase: MainActivity.mAngryWordsFound){
			offensiveWords += phrase.getPhrase() + " (Level " +phrase.getOffensiveness() + ")\n";
		}
		mPhrasesFound.setText(offensiveWords);

		TextView messageContent = (TextView) dialog.findViewById(R.id.user_message_edit);
		messageContent.setText(mMessage);

		//reset the static variables 
		MainActivity.mNumAngryWords = 0;
		MainActivity.offensiveRating = 0;
		MainActivity.mAngryWordsFound.clear();

	
		return dialog;
	}
}
