package com.example.catto10;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WarningDialogBox extends DialogFragment {

	static private final int GET_TEXT_REQUEST_CODE = 1;

	static final String CHOOSER_TEXT = "Load with:";

	private Button mProceed;
	private Button mEditMessage;
	String mMessage;
	String mPhoneNo;

	public WarningDialogBox(String m, String pNum){
		mMessage = m;
		mPhoneNo = pNum;
	}
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.message_dialog, null);

		// Set and change the size of the view
		builder.setView(dialogView);
		final AlertDialog dialog = builder.show();
		dialog.getWindow().setLayout(550, 650);

		// Set text message to send to user 
		TextView mUserWarningMessage = (TextView) dialog.findViewById(R.id.warning_message);
		String mInstance = MainActivity.mNumAngryWords < 2 ? "instance" : "instances";
		String mWarningMessage = "We found " + MainActivity.mNumAngryWords + " " 
				+ mInstance + " of angry words in your message with an average offensive level of "  +
				MainActivity.offensiveRating + ".  Please edit your message to acceptable level before sending.\n\n"
				+ mMessage;
		mUserWarningMessage.setText(mWarningMessage);

		// Define actions for buttons
		mProceed = (Button) dialogView.findViewById(R.id.send_message);
		mProceed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				//TODO Send pictures of cats one at a time
				PicturePromptFragment test = new PicturePromptFragment(mMessage, mPhoneNo);
				test.show(getFragmentManager(), PicturePromptFragment.CHOOSER_TEXT);

				dialog.cancel();
			
			}
		});

		mEditMessage = (Button) dialogView.findViewById(R.id.edit_message);
		mEditMessage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				EditMessageFragment test = new EditMessageFragment(mMessage, mPhoneNo);
				test.show(getFragmentManager(), EditMessageFragment.CHOOSER_TEXT);

				dialog.cancel();
			}
		});
		return dialog;
	}
	
	@Override
    public void onDetach() {
        super.onDetach();
        MainActivity.mNumAngryWords = 0;
        MainActivity.offensiveRating = 0;
        MainActivity.mAngryWordsFound.clear();
    }
}
