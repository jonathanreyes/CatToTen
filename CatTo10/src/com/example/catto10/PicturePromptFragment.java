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

public class PicturePromptFragment extends DialogFragment {

	static private final int GET_TEXT_REQUEST_CODE = 1;

	static final String CHOOSER_TEXT = "Load with:";

	String mMessage;
	String mPhoneNum;

	public PicturePromptFragment (String m, String pNum){
		mMessage = m;
		mPhoneNum = pNum;
	}
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// Set and change the size of the view
		builder.setTitle(R.string.angry_words)
		.setMessage(R.string.send_message_unedited)
		.setPositiveButton(R.string.edit_message, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				EditMessageFragment test = new EditMessageFragment(mMessage, mPhoneNum);
				test.show(getFragmentManager(), EditMessageFragment.CHOOSER_TEXT);

				dialog.cancel();
			}
		})
		.setNegativeButton(R.string.proceed, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Create the dialog boxes with cat images
				CatPictureDialog cats = new CatPictureDialog(mMessage, mPhoneNum);
				cats.show(getFragmentManager(), CatPictureDialog.CHOOSER_TEXT);
				
				dialog.cancel();
			}
		});
		final AlertDialog dialog = builder.show();

		//reset the static variables 
		MainActivity.mNumAngryWords = 0;
		MainActivity.offensiveRating = 0;
		MainActivity.mAngryWordsFound.clear();

		return dialog;
	}
}
