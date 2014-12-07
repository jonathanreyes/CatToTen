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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PicturePromptFragment extends DialogFragment {

	static private final int GET_TEXT_REQUEST_CODE = 1;

	static final String CHOOSER_TEXT = "Load with:";

	private Button mEdit;
	private Button mSend;
	String mMessage;
	String mPhoneNum;

	public PicturePromptFragment (String m, String pNum){
		mMessage = m;
		mPhoneNum = pNum;
	}
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.picture_prompt, null);

		// Set and change the size of the view
		builder.setView(dialogView);
		final AlertDialog dialog = builder.show();

		//reset the static variables 
		MainActivity.mNumAngryWords = 0;
		MainActivity.offensiveRating = 0;
		MainActivity.mAngryWordsFound.clear();

		// Define actions for buttons
		mSend = (Button) dialogView.findViewById(R.id.send_cats);
		mSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				//Create the dialog boxes with cat images
				CatPictureDialog cats = new CatPictureDialog(mMessage, mPhoneNum);
				cats.show(getFragmentManager(), CatPictureDialog.CHOOSER_TEXT);
				
				dialog.cancel();
			}
		});

		mEdit = (Button) dialogView.findViewById(R.id.edit_message_cats);
		mEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditMessageFragment test = new EditMessageFragment(mMessage, mPhoneNum);
				test.show(getFragmentManager(), EditMessageFragment.CHOOSER_TEXT);

				dialog.cancel();
			}
		});
		return dialog;
	}
}
