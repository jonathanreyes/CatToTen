package com.example.catto10;

import com.afollestad.materialdialogs.MaterialDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class PicturePromptFragment extends DialogFragment {

	static final String CHOOSER_TEXT = "Load with:";

	String mMessage;
	String mPhoneNum;

	public PicturePromptFragment (String m, String pNum){
		mMessage = m;
		mPhoneNum = pNum;
	}
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		// Use the Builder class for convenient dialog construction
		MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

		builder.title(R.string.angry_words)
				.content(R.string.send_message_unedited)
				.positiveText(R.string.edit_message)
				.negativeText(R.string.proceed)
				.callback(new MaterialDialog.Callback() {
					@Override
					public void onPositive(MaterialDialog dialog) {
						EditMessageFragment test = new EditMessageFragment(mMessage, mPhoneNum);
						test.show(getFragmentManager(), EditMessageFragment.CHOOSER_TEXT);
					}

					@Override
					public void onNegative(MaterialDialog dialog) {
						CatPictureDialog cats = new CatPictureDialog(mMessage, mPhoneNum);
						cats.show(getFragmentManager(), CatPictureDialog.CHOOSER_TEXT);
					}
				});
		final MaterialDialog dialog = builder.show();

		return dialog;
	}
}
