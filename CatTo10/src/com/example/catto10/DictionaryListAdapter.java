package com.example.catto10;


import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DictionaryListAdapter extends RecyclerView.Adapter<DictionaryListAdapter.ListViewHolder> {
	private List<OffensivePhrase> mList;
	private Context mContext;
	private PhrasesDAO db;
	
	
	public DictionaryListAdapter(Context context, List<OffensivePhrase> list){
		this.mList = list;
		this.mContext = context;

		db = new PhrasesDAO(mContext);
	}
	
	public int add(String phrase, int offensiveness){
		if(phrase.length() > 0){
			db.open();
			if(db.contains(phrase)){
				OffensivePhrase newItem = db.addPhrase(phrase, offensiveness, getIcon(offensiveness));
				mList.add(newItem);
				db.close();
				notifyDataSetChanged();
				return 1;
			} else {
				db.close();
				return 2;
			}
		}
		
		return 0;
	}

	
	public void delete(int position){
		db.open();
		db.deletePhrase(mList.get(position));
		db.close();

		mList.remove(position);
		notifyItemRemoved(position);
	}

	public void clear(){
		mList.clear();
		db.open();
		db.deleteAll();
		db.close();
		notifyDataSetChanged();
	}
	
	public void edit(String phrase, int offensiveness, int position){
		OffensivePhrase curr = mList.get(position);
		db.open();
		db.editPhrase(phrase, offensiveness, getIcon(offensiveness), curr.getId());
		db.close();
		if(!curr.getPhrase().equals(phrase))
			curr.setPhrase(phrase);

		if(curr.getOffensiveness() != offensiveness){
			curr.setOffensiveness(offensiveness);
			curr.setIcon(getIcon(offensiveness));
		}

		notifyItemChanged(position);
	}

	// returns image bitmap depicting "offensive offensiveness"
	public static String getIcon(int offensiveness) {

		switch (offensiveness) {
		case 1:
			return "level_flame_one";
		case 2:
			return "level_flame_two";
		case 4:
			return "level_flame_four";
		case 5:
			return "level_flame_five";
		default:
			return "level_flame_three";
		}

	}
	
	@Override
	public void onBindViewHolder(ListViewHolder listViewHolder, int position) {
		OffensivePhrase w = mList.get(position);
		listViewHolder.vPhrase.setText(w.getPhrase());


		int resID = mContext.getResources().getIdentifier(w.getIcon(), "drawable", mContext.getPackageName());
		Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), resID);
		listViewHolder.vAnger.setImageBitmap(bitmap);

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
		protected TextView vPhrase;
		protected ImageView vAnger, vDelete, vEdit;
		protected boolean toggleFlag = false;

		public ListViewHolder(View v, ViewGroup parent) {
			super(v);
			vPhrase = (TextView) v.findViewById(R.id.list_word);
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
					OffensivePhrase entry = mList.get(getPosition());

					EntryDialogFragment newFragment = EntryDialogFragment.newInstance(entry.getPhrase(), entry.getOffensiveness(), "Edit Entry");
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

		// retrieves edited info from dialog and changes the phraseEntry object
		public void onFinishEditDialog(String phrase, int offensiveness){
			edit(phrase, offensiveness, getPosition());
		}

	}
}
