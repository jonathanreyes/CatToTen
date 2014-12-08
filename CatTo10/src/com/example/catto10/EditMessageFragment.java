package com.example.catto10;

import com.afollestad.materialdialogs.MaterialDialog;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditMessageFragment extends DialogFragment {

	public static final double ANGER_THRESHOLD = 5; 
	static final String CHOOSER_TEXT = "Load with:";

	String mMessage;
	String mPhoneNum;

	public EditMessageFragment(String m, String pNum){
		mMessage = m;
		mPhoneNum = pNum;
	}
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		// Use the Builder class for convenient dialog construction
		MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
		
		builder.title(R.string.words_detected)
				.customView(R.layout.edit_message)
				.positiveText(R.string.discard)
				.negativeText(R.string.send)
				.callback(new MaterialDialog.Callback() {
					@Override
					public void onPositive(MaterialDialog dialog) {}

					@Override
					public void onNegative(MaterialDialog dialog) {
						//TODO have to do another check to see if the new message has 
						// any offensive content.  (method currently exists in Main Activity)

						EditText mEditMessageView = (EditText) ((Dialog)dialog).findViewById(R.id.user_message_edit);
						String mEditMessage = mEditMessageView.getText().toString();
						
						
						CheckMessageContent temp = new CheckMessageContent(dialog.getContext());
						temp.checkMessageContent(mEditMessage);
						
						if (MainActivity.offensiveRating < ANGER_THRESHOLD){
							sendMessage(mEditMessage);
							
						} else {
							WarningDialogBox mWarning= new WarningDialogBox(mEditMessage, mPhoneNum);
							mWarning.show(getFragmentManager(), WarningDialogBox.CHOOSER_TEXT);
						}
					}
				});
		
		final MaterialDialog dialog = builder.show();

		// Set text message to send to user 
		
		TextView mPhrasesFound = (TextView) dialog.findViewById(R.id.words_found);
		mPhrasesFound.setText(getOffensiveWords());

		TextView messageContent = (TextView) dialog.findViewById(R.id.user_message_edit);
		messageContent.setText(mMessage);

		//reset the static variables 
		MainActivity.mNumAngryWords = 0;
		MainActivity.offensiveRating = 0;
		MainActivity.mAngryWordsFound.clear();

		return dialog;
	}
	
	public void sendMessage(String mEditMessage){
		
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
	}
	
	public String getOffensiveWords(){
		
		String offensiveWords = "";
		for(OffensivePhrase phrase: MainActivity.mAngryWordsFound){
			offensiveWords += phrase.getPhrase() + " (Level " +phrase.getOffensiveness() + ")\n";
		}
		return offensiveWords;
	}
}
