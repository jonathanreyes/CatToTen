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
import android.widget.TextView;
import android.widget.Toast;

public class WarningDialogBox extends DialogFragment {

	static private final int GET_TEXT_REQUEST_CODE = 1;

	static final String CHOOSER_TEXT = "Load with:";

	String mMessage;
	String mPhoneNo;

	public WarningDialogBox(String m, String pNum) {
		mMessage = m;
		mPhoneNo = pNum;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// message depicting words found
		String mInstance = MainActivity.mNumAngryWords < 2 ? "instance"
				: "instances";
		String mWarningMessage = "We found "
				+ MainActivity.mNumAngryWords
				+ " "
				+ mInstance
				+ " of angry words in your message with an average offensive level of "
				+ MainActivity.offensiveRating
				+ ".  Please edit your message to acceptable level before sending.\n\n"
				+ mMessage;
		
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// Set and change the size of the view
		builder.setTitle(R.string.angry_words)
		.setMessage(mWarningMessage)
		.setPositiveButton(R.string.edit_message, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				EditMessageFragment test = new EditMessageFragment(mMessage, mPhoneNo);
				test.show(getFragmentManager(),
						EditMessageFragment.CHOOSER_TEXT);

				dialog.cancel();
			}
		})
		.setNegativeButton(R.string.send_anyways, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Send pictures of cats one at a time
				PicturePromptFragment test = new PicturePromptFragment(mMessage, mPhoneNo);
				test.show(getFragmentManager(),
						PicturePromptFragment.CHOOSER_TEXT);

				dialog.cancel();
			}
		});
		final AlertDialog dialog = builder.show();

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
