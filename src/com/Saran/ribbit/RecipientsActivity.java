package com.Saran.ribbit;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
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

public class RecipientsActivity extends ListActivity {
	
	private ParseUser mCurrentUser;
	private ParseRelation<ParseUser> mFriendsRelation;
	private List<ParseUser> mFriends;
	private MenuItem mSendButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_recipients);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}
	
	@Override
	public void onResume() { 
		super.onResume();
		setProgressBarIndeterminateVisibility(true);
		mCurrentUser = ParseUser.getCurrentUser();
		mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
		ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
		query.addAscendingOrder(ParseConstants.KEY_USERNAME);
		query.findInBackground(new FindCallback<ParseUser>() {
			
			@Override
			public void done(List<ParseUser> friends, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				if(e!=null){
					AlertDialog.Builder builder = new AlertDialog.Builder(RecipientsActivity.this); 
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
					
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(RecipientsActivity.this, android.R.layout.simple_list_item_checked, friendsArray);
					setListAdapter(adapter);
				}
			}
		});
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		//Make send button visible when a friend from listview is selected
		if(l.getCheckedItemCount()>0){
			mSendButton.setVisible(true);
		}else{
			mSendButton.setVisible(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recipients, menu);
		
		//store to set visibility in onListItemClickedß
		mSendButton = menu.getItem(0);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_send) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
