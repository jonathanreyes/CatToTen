package com.example.catto10;

import com.afollestad.materialdialogs.MaterialDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class EntryDialogFragment extends DialogFragment {

	public static interface EditDialogListener {
		public abstract void onFinishEditDialog(String phrase, int offensiveness);
	}
	
	private EditDialogListener mListener;
	private EditText mEditText;
	private RatingBar mRatingBar;

	public static EntryDialogFragment newInstance(String title) {
		EntryDialogFragment newFragment = new EntryDialogFragment();
		
		Bundle args = new Bundle();
		args.putString("title", title);
		newFragment.setArguments(args);
		
		return newFragment; 
	}
	
	public static EntryDialogFragment newInstance(String phrase, int offensiveness, String title) {
		EntryDialogFragment newFragment = new EntryDialogFragment();
		
		Bundle args = new Bundle();
		args.putString("phrase", phrase);
		args.putString("title", title);
		args.putInt("offensiveness", offensiveness);
		newFragment.setArguments(args);
		
		return newFragment; 
	}

	public void setListener(EditDialogListener activity){
		mListener = activity;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
		
		builder.title(getArguments().getString("title"))
				.customView(R.layout.dialog_layout)
				.positiveText(R.string.save)
				.negativeText(R.string.discard)
				.callback(new MaterialDialog.SimpleCallback() {
					@Override
					public void onPositive(MaterialDialog dialog) {	
						mListener.onFinishEditDialog(mEditText.getText().toString(), (int) mRatingBar.getProgress());
					}
				});
		
		MaterialDialog dialog = builder.show();
		View view = dialog.getCustomView();
		
		mRatingBar = (RatingBar) view.findViewById(R.id.anger_bar);
		mEditText = (EditText) view.findViewById(R.id.entry_text);
		
		if(getArguments().size() > 1){
			mEditText.setText(getArguments().getString("phrase"));
			mRatingBar.setProgress(getArguments().getInt("offensiveness"));
		}		
		
		return dialog;
	}

}
