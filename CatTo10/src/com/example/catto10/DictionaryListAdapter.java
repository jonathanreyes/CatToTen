package com.example.catto10;

import java.util.ArrayList;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DictionaryListAdapter extends RecyclerView.Adapter<DictionaryListAdapter.ListViewHolder> {
	private List<WordEntry> mList = new ArrayList<WordEntry>();
	private Context mContext;
	
	
	public DictionaryListAdapter(Context context){
		this.mContext = context;
	}
	
	public int add(String word, int level, int position){
		if(!mList.contains(word)){
			Bitmap flame = getAngerBitmap(level);
			
			WordEntry item = new WordEntry(word, level, flame);
			mList.add(item);
			
			notifyItemInserted(position);
			return 1;	// Success
		} else if(mList.contains(word)) {
			return 2;	// Duplicate
		}
		
		return 0;
	}
	
	public void delete(int position){
		mList.remove(position);
		notifyItemRemoved(position);
	}
	
	public void clear(){
		mList.clear();
		notifyDataSetChanged();
	}
	
	public void edit(String word, int level, int position){
		WordEntry curr = mList.get(position);
		
		if(!curr.getWord().equals(word))
			curr.setWord(word);
		
		if(curr.getLevel() != level){
			curr.setLevel(level);
			curr.setFlame(getAngerBitmap(level));
		}
		
		notifyItemChanged(position);
	}
	
	// returns imagebitmap depicting "offensive level"
	public Bitmap getAngerBitmap(int level){
		Bitmap flame = null;
		
		switch(level) {
		case 1:
			flame = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.level_flame_one);
			break;
		case 2:
			flame = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.level_flame_two);
			break;
		case 3:
			flame = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.level_flame_three);
			break;
		case 4:
			flame = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.level_flame_four);
			break;
		case 5:
			flame = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.level_flame_five);
		}
		
		return flame;
	}
	
	@Override
	public void onBindViewHolder(ListViewHolder listViewHolder, int position) {
		WordEntry w = mList.get(position);
		listViewHolder.vWord.setText(w.getWord());
		listViewHolder.vAnger.setImageBitmap(w.getFlame());
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	@Override
	public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
		
		return new ListViewHolder(itemView, parent);
	}
	
	public class ListViewHolder extends RecyclerView.ViewHolder implements OnClickListener, EntryDialogFragment.EditDialogListener{
		protected TextView vWord;
		protected ImageView vAnger, vDelete, vEdit;
		protected boolean toggleFlag = false;
		
		public ListViewHolder(View v, ViewGroup parent) {
			super(v);
			vWord = (TextView) v.findViewById(R.id.list_word);
			vAnger = (ImageView) v.findViewById(R.id.list_anger);
			vDelete = (ImageView) v.findViewById(R.id.list_delete);
			vEdit = (ImageView) v.findViewById(R.id.list_edit);
			
			v.setOnClickListener(this);
			
			vDelete.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					delete(getPosition());
				}
			});
			
			
			// fires Dialog
			vEdit.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					WordEntry entry = mList.get(getPosition());
					
					EntryDialogFragment newFragment = EntryDialogFragment.newInstance(entry.getWord(), entry.getLevel(), "Edit Entry");
					newFragment.setListener(ListViewHolder.this);
					newFragment.show(((Activity)mContext).getFragmentManager(), "Dialog");
				}
			});
		}

		@Override
		public void onClick(View view) {
			if(!toggleFlag){
				vDelete.setVisibility(View.VISIBLE);
				vEdit.setVisibility(View.VISIBLE);
				toggleFlag = true;
			} else {
				vDelete.setVisibility(View.INVISIBLE);
				vEdit.setVisibility(View.INVISIBLE);
				toggleFlag = false;
			}
		}
		
		// retrieves edited info from dialog and changes the WordEntry object
		public void onFinishEditDialog(String word, int level){
			edit(word, level, getPosition());
		}
		
	}
}
