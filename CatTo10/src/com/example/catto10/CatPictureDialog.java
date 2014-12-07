package com.example.catto10;

import java.io.InputStream;
import java.util.Random;

import android.app.Activity;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CatPictureDialog extends DialogFragment {

	static private final int GET_TEXT_REQUEST_CODE = 1;
	private final String URL = "https://cattoten.files.wordpress.com/2014/12/1-";
	static final String CHOOSER_TEXT = "Load with:";

	private Button mNext;
	private Button mEdit;
	String mMessage;
	String mPhoneNum;
	private int mPicCounter = 1;
	private int mTempRandNum;
	private Random randomGenerator = new Random();
	

	public CatPictureDialog (String m, String pNum){
		mMessage = m;
		mPhoneNum = pNum;
	}
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		// Use the Builder class for convenient dialog construction
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.cat_pictures, null);

		// Set and change the size of the view
		builder.setView(dialogView)
		.setTitle("Cat Picture " + mPicCounter  + " of 10")
		.setPositiveButton(R.string.edit_message, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				EditMessageFragment test = new EditMessageFragment(mMessage, mPhoneNum);
				test.show(getFragmentManager(), EditMessageFragment.CHOOSER_TEXT);

				dialog.cancel();
			}
		}).setNegativeButton(R.string.next, null);
		final AlertDialog dialog = builder.show();

		mTempRandNum = randomGenerator.nextInt(51) + 1;
		new DownloadImageTask((ImageView) dialog.findViewById(R.id.cat_image))
        .execute(URL + mTempRandNum + ".jpg");

		dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				if (mPicCounter < 10){
					mPicCounter++;
					((Dialog) dialog).setTitle("Cat Picture " + mPicCounter  + " of 10");

					mTempRandNum = randomGenerator.nextInt(51) + 1;
					new DownloadImageTask((ImageView) ((Dialog) dialog).findViewById(R.id.cat_image))
			        .execute(URL + mTempRandNum + ".jpg");

				} else {
					//make proceed button appear on screen?  Or create a new dialog box altogether?
					//Toast.makeText(dialog.getContext(),"DONE", Toast.LENGTH_SHORT).show();
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
					dialog.cancel();
				}
			}
			
		});
		
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
}
