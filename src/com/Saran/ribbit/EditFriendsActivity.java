package com.Saran.ribbit;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;

import com.Saran.ribbit.constants.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class EditFriendsActivity extends ListActivity {
	
	private String[] mUsers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_edit_friends);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
					int i=0;
					mUsers = new String[list.size()];
					for(ParseUser user: list){
						mUsers[i] = user.getUsername();
						i++;
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditFriendsActivity.this, android.R.layout.simple_list_item_checked,mUsers);
					setListAdapter(adapter);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_friends, menu);
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
}
