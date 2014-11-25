package com.example.catto10;

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
		public abstract void onFinishEditDialog(String word, int level);
	}
	
	private EditDialogListener mListener;
	private EditText mEditText;
	private TextView mEntryText, mAngerText;
	private RatingBar mRatingBar;

	public static EntryDialogFragment newInstance(String title) {
		EntryDialogFragment newFragment = new EntryDialogFragment();
		
		Bundle args = new Bundle();
		args.putString("title", title);
		newFragment.setArguments(args);
		
		return newFragment; 
	}
	
	public static EntryDialogFragment newInstance(String word, int level, String title) {
		EntryDialogFragment newFragment = new EntryDialogFragment();
		
		Bundle args = new Bundle();
		args.putString("word", word);
		args.putString("title", title);
		args.putInt("level", level);
		newFragment.setArguments(args);
		
		return newFragment; 
	}

	public void setListener(EditDialogListener activity){
		mListener = activity;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_layout, null);
		
		mRatingBar = (RatingBar) view.findViewById(R.id.anger_bar);
		mEditText = (EditText) view.findViewById(R.id.entry_text);
		mEntryText = (TextView) view.findViewById(R.id.entry_title);
		mAngerText = (TextView) view.findViewById(R.id.anger_title);
		
		if(getArguments().size() > 1){
			mEditText.setText(getArguments().getString("word"));
			mRatingBar.setProgress(getArguments().getInt("level"));
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setView(view)
		.setTitle(getArguments().getString("title"))
		.setPositiveButton(R.string.save, new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int id) {
				mListener.onFinishEditDialog(mEditText.getText().toString(), (int) mRatingBar.getProgress());
			}
		})
		.setNegativeButton(R.string.discard, new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		
		return builder.create();
	}

}
