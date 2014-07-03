package com.Saran.ribbit;

import java.util.List;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.Saran.ribbit.constants.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class FriendsListFragment extends ListFragment {
	
	private ParseUser mCurrentUser;
	private ParseRelation<ParseUser> mFriendsRelation;
	private List<ParseUser> mFriends;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friendslist, container,false);
		return view;
	}
	
	@Override
	public void onResume() { 
		super.onResume();
		getActivity().setProgressBarIndeterminateVisibility(true);
		mCurrentUser = ParseUser.getCurrentUser();
		mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
		ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
		query.addAscendingOrder(ParseConstants.KEY_USERNAME);
		query.findInBackground(new FindCallback<ParseUser>() {
			
			@Override
			public void done(List<ParseUser> friends, ParseException e) {
				getActivity().setProgressBarIndeterminateVisibility(false);
				if(e!=null){
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); 
					builder.setMessage(R.string.friends_error_message);
					builder.setTitle(R.string.friends_error_title);
					builder.setPositiveButton(android.R.string.ok, null);
					
					AlertDialog dialog = builder.create();
					dialog.show();
				}else{
					mFriends = friends;
					int i = 0;
					Log.d("Saran","number of friends "+mFriends.size());
					String[] friendsArray = new String[mFriends.size()];
					for(ParseUser friend:mFriends){
						friendsArray[i] = friend.getUsername();
						i++;
					}
					
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, friendsArray);
					setListAdapter(adapter);
				}
			}
		});
		
	}
}
