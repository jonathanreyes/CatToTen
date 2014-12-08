package com.example.catto10;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import com.afollestad.materialdialogs.MaterialDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CatPictureDialog extends DialogFragment {

	private final String URL = "https://cattotenmaster.files.wordpress.com/2014/12/1-";
	static final String CHOOSER_TEXT = "Load with:";

	String mMessage;
	String mPhoneNum;
	private int mPicCounter = 1;
	private int mTempRandNum;
	private Random randomGenerator = new Random();
	private ArrayList<Integer> catSeen = new ArrayList<Integer>();


	public CatPictureDialog (String m, String pNum){
		mMessage = m;
		mPhoneNum = pNum;
	}
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		// Use the Builder class for convenient dialog construction
		MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

		builder.title("Cat Picture " + mPicCounter  + " of 10")
				.autoDismiss(false)
				.customView(R.layout.cat_pictures)
				.positiveText(R.string.edit_message)
				.negativeText(R.string.next)
				.callback(new MaterialDialog.Callback() {

					@Override
					public void onPositive(MaterialDialog dialog) {
						EditMessageFragment test = new EditMessageFragment(mMessage, mPhoneNum);
						test.show(getFragmentManager(), EditMessageFragment.CHOOSER_TEXT);
						dialog.dismiss();
					}

					@Override
					public void onNegative(MaterialDialog dialog) {
						sendCatPics(dialog);
					}
				});

	
		final MaterialDialog dialog = builder.show();
		// Set picture for Cat from website
		generateCatPicture(dialog);
		

		return dialog;
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}
	
	public void generateCatPicture(MaterialDialog dialog){
		
		mTempRandNum = randomGenerator.nextInt(51) + 1;
		while (catSeen.contains(mTempRandNum)){
			mTempRandNum = randomGenerator.nextInt(51) + 1;
		}
		catSeen.add(mTempRandNum);
		new DownloadImageTask((ImageView) ((Dialog) dialog).findViewById(R.id.cat_image))
		.execute(URL + mTempRandNum + ".jpg");
	}

	public void sendCatPics(MaterialDialog dialog){
		if (mPicCounter < 10){
			mPicCounter++;
			((Dialog) dialog).setTitle("Cat Picture " + mPicCounter  + " of 10");
			generateCatPicture(dialog);
		} else {
			sendMessage();
			dialog.dismiss();
		}
	}

	public void sendMessage(){
		
		MainActivity.mNumAngryWords = 0;
		MainActivity.offensiveRating = 0;
		MainActivity.mAngryWordsFound.clear();

		Intent smsIntent = new Intent(Intent.ACTION_VIEW);
		smsIntent.setData(Uri.parse("smsto:"));
		smsIntent.setType("vnd.android-dir/mms-sms");

		smsIntent.putExtra("address", mPhoneNum);
		smsIntent.putExtra("sms_body", mMessage);

		try {
			startActivity(smsIntent);
			Log.i("Finished sending SMS...", "");
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(null, 
					"SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
		}
	}
}
