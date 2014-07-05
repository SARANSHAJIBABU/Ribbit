package com.Saran.ribbit;

import java.util.List;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.Saran.ribbit.constants.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseException;
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
		query.addAscendingOrder(ParseConstants.KEY_CREATED_AT);
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> messages, ParseException e) {
				getActivity().setProgressBarIndeterminateVisibility(false);
				
				if(e==null){
					mMessages = messages;
					MessageAdapter adapter = new MessageAdapter(getListView().getContext(), R.layout.message_item, mMessages);
					setListAdapter(adapter);
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
}
