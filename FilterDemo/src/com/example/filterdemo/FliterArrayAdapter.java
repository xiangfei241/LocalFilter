package com.example.filterdemo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
public class FliterArrayAdapter extends ArrayAdapter<String> {

	private ConversationFilter conversationFilter;
	
	private List<String> objs;
	
	private List<String> copyConversationList;
	
	private boolean notiyfyByFilter;
	
	public FliterArrayAdapter(Context context, int resource,List<String> objs) {
		super(context, resource,objs);
		this.objs = objs;
		copyConversationList = new ArrayList<String>();
		copyConversationList.addAll(objs);
	}
	
	@Override
	public Filter getFilter() {
		if (conversationFilter == null) {
			conversationFilter = new ConversationFilter(objs);
		}
		return conversationFilter;
	}
	
	private class ConversationFilter extends Filter {
		List<String> mOriginalValues = null;

		public ConversationFilter(List<String> mList) {
			mOriginalValues = mList;
		}

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mOriginalValues == null) {
				mOriginalValues = new ArrayList<String>();
			}
			if (prefix == null || prefix.length() == 0) {
				results.values = copyConversationList;
				results.count = copyConversationList.size();
			} else {
				String prefixString = prefix.toString();
				final int count = mOriginalValues.size();
				final ArrayList<String> newValues = new ArrayList<String>();
				for (int i = 0; i < count; i++) {

					String string = mOriginalValues.get(i);
					// First match against the whole ,non-splitted value
					if (string.startsWith(prefixString)) {
						newValues.add(string);
					}else if(string.contains(prefixString)){
						newValues.add(string);
					}else{
						  final String[] words = string.split(" ");
	                        final int wordCount = words.length;
	                        // Start at index 0, in case valueText starts with space(s)
	                        for (int k = 0; k < wordCount; k++) {
	                            if (words[k].startsWith(prefixString)) {
	                                newValues.add(string);
	                                break;
	                            }
	                        }
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			objs.clear();
			objs.addAll((List<String>) results.values);
			if (results.count > 0) {
			    notiyfyByFilter = true;
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}

		}

	}
	
	@Override
	public void notifyDataSetChanged() {
	    super.notifyDataSetChanged();
	    if(!notiyfyByFilter){
            copyConversationList.clear();
            copyConversationList.addAll(objs);
            notiyfyByFilter = false;
        }
	}

}
