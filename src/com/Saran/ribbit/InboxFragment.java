package com.Saran.ribbit;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.Saran.ribbit.constants.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class InboxFragment extends ListFragment {
	
	private List<ParseObject> mMessages;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_inbox, container,false);
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		getActivity().setProgressBarIndeterminateVisibility(true);
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_MESSAGES);
		query.whereEqualTo(ParseConstants.KEY_RECIPIENT_IDS, ParseUser.getCurrentUser().getObjectId());
		query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> messages, ParseException e) {
				getActivity().setProgressBarIndeterminateVisibility(false);
				
				if(e==null){
					mMessages = messages;
					if(getListView().getAdapter()==null){
						//if no adapter, create a new adapter
						MessageAdapter adapter = new MessageAdapter(getListView().getContext(), R.layout.message_item, mMessages);
						setListAdapter(adapter);
					}else{
						//If there is an adapter, just refill its datastructure
						((MessageAdapter)getListView().getAdapter()).refill(mMessages);
					}
				}else{
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); 
					builder.setMessage(R.string.inbox_error_message);
					builder.setTitle(R.string.inbox_error_title);
					builder.setPositiveButton(android.R.string.ok, null);
					
					AlertDialog dialog = builder.create();
					dialog.show();
				}

			}
		}); 
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		ParseObject message = mMessages.get(position);
		ParseFile messageFile = message.getParseFile(ParseConstants.KEY_FILE);
		Uri uri = Uri.parse(messageFile.getUrl());
		
		if(message.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.FILE_TYPE_IMAGE)){
			Intent intent = new Intent(getActivity(),ImageViewActivity.class);
			intent.setData(uri);
			startActivity(intent);
		}else{
			Intent intent = new Intent(Intent.ACTION_VIEW,uri);
			intent.setData(uri);
			intent.setType("video/*");
			startActivity(intent);
		}
		
		List<String> recipientList = message.getList(ParseConstants.KEY_RECIPIENT_IDS);
		if(recipientList.size()==1){
			//if only one recipient, directly delete from background
			//else delete the user's id from recipients list
			message.deleteInBackground();
		}else{
			//create list and put recipient to remove. removeall() requires a collection as arguement
			List<String> toRemove = new ArrayList<String>();
			toRemove.add(message.getString(ParseConstants.KEY_SENDER_ID));
			
			//remove id from message locally
			message.removeAll(ParseConstants.KEY_RECIPIENT_IDS, toRemove);
			
			//save the change made in ParseObject in backend
			message.saveInBackground();
		}
	}
}
