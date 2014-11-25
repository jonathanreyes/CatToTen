package com.example.catto10;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


public class DictionaryListActivity extends Activity implements EntryDialogFragment.EditDialogListener {
	
	private DictionaryListAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dictionary_main);

		RecyclerView recyclerList = (RecyclerView) findViewById(R.id.list);
		recyclerList.setHasFixedSize(true);

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		linearLayoutManager.scrollToPosition(0);
		recyclerList.setLayoutManager(linearLayoutManager);

		mAdapter = new DictionaryListAdapter(this);
		recyclerList.setAdapter(mAdapter);

		// creates dividers between items
		RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
		recyclerList.addItemDecoration(itemDecoration);

		// add button
		ImageButton addButton = (ImageButton) findViewById(R.id.add_item);
		addButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view) {
				showEditDialog();
			}

		});
		
		createList(30);
	}
	
	// fires Dialog
	private void showEditDialog(){
		EntryDialogFragment newFragment = EntryDialogFragment.newInstance("Add Entry");
		newFragment.setListener(this);
		newFragment.show(getFragmentManager(), "Dialog");
	}
	
	// after Dialog
	public void onFinishEditDialog(String word, int level){
		int status = mAdapter.add(word, level, mAdapter.getItemCount());
		if (status == 1){
			Toast.makeText(this, "Entry successfully added!", Toast.LENGTH_SHORT).show();
		} else if (status == 2){
			Toast.makeText(this, "Entry already exists", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cat_to_ten_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void createList(int size){
		String abc = "abcdefghijklmnopqrstuvwxyz";
		Random r = new Random();

		for(int i = 0; i < size; i++){
			int start = r.nextInt(20);
			int length = r.nextInt(5) + 1;
			int flame = r.nextInt(5) + 1;

			mAdapter.add(abc.substring(start, start+length), flame, mAdapter.getItemCount());
		}
	}
}
