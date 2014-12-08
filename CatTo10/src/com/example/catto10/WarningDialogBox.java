package com.example.catto10;

import com.afollestad.materialdialogs.MaterialDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class WarningDialogBox extends DialogFragment {

	static final String CHOOSER_TEXT = "Load with:";

	String mMessage;
	String mPhoneNo;

	public WarningDialogBox(String m, String pNum) {
		mMessage = m;
		mPhoneNo = pNum;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		// Warning message to user including original message
		String mInstance = MainActivity.mNumAngryWords < 2 ? "instance"
				: "instances";
		String mWarningMessage = "We found "
				+ MainActivity.mNumAngryWords
				+ " "
				+ mInstance
				+ " of angry words in your message with a cumulative offensive level of "
				+ MainActivity.offensiveRating
				+ ".  Please edit your message to acceptable level before sending.\n\n"
				+ mMessage;
		
		// Use the Builder class for convenient dialog construction
		MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

		builder.title(R.string.angry_words).content(mWarningMessage)
				.positiveText(R.string.edit_message)
				.negativeText(R.string.send_anyways)
				.callback(new MaterialDialog.Callback() {
					@Override
					public void onPositive(MaterialDialog dialog) {
						EditMessageFragment test = new EditMessageFragment(mMessage, mPhoneNo);
						test.show(getFragmentManager(), EditMessageFragment.CHOOSER_TEXT);
					}
					
					@Override
					public void onNegative(MaterialDialog dialog) {
					// Open dialogbox to send pictures of cats to user
						PicturePromptFragment test = new PicturePromptFragment(mMessage, mPhoneNo);
						test.show(getFragmentManager(), PicturePromptFragment.CHOOSER_TEXT);
					}
				});
		final MaterialDialog dialog = builder.show();

		return dialog;
	}
	
	public void onBackButton() {
		super.onDetach();
		MainActivity.mNumAngryWords = 0;
		MainActivity.offensiveRating = 0;
		MainActivity.mAngryWordsFound.clear();
	}
}
