package com.Saran.ribbit;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.Saran.ribbit.constants.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class EditFriendsActivity extends ListActivity {
	
	private List<ParseUser> mUsers;
	private ParseRelation<ParseUser> mFriendsRelation;
	private ParseUser mCurrentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_edit_friends);
		
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mCurrentUser = ParseUser.getCurrentUser();
		mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
		
		setProgressBarIndeterminateVisibility(true);
		
		//Query to get all users from parse
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.orderByAscending(ParseConstants.KEY_USERNAME);
		query.setLimit(1000);
		query.findInBackground(new FindCallback<ParseUser>() {
			
			@Override
			public void done(List<ParseUser> list, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				if(e!=null){
					AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this); 
					builder.setMessage(R.string.edit_friends_error_message);
					builder.setTitle(R.string.edit_friends_error_title);
					builder.setPositiveButton(android.R.string.ok, null);
					
					AlertDialog dialog = builder.create();
					dialog.show();
				}else{
					mUsers = list;
					String[] users = new String[mUsers.size()];
					int i=0;
					
					for(ParseUser user: list){
						users[i] =  user.getUsername();
						i++;
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditFriendsActivity.this, android.R.layout.simple_list_item_checked,users);
					setListAdapter(adapter);
					
					mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {

						@Override
						public void done(List<ParseUser> friends,ParseException e) {
							for(int i =0; i<mUsers.size();i++){
								ParseUser user = mUsers.get(i);
								
								for(ParseUser friend: friends){
									if(friend.getObjectId().equals(user.getObjectId())){
										getListView().setItemChecked(i, true);
									}
								}
							}
						}
					});
				}
			}
		});
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
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		if(getListView().isItemChecked(position)){
			mFriendsRelation.add(mUsers.get(position));
		}else{
			mFriendsRelation.remove(mUsers.get(position));
		}
		
		mCurrentUser.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				if(e!=null){
					//error
				}
				
			}
		});

	}
}
