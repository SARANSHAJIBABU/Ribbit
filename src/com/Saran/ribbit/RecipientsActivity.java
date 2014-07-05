package com.Saran.ribbit;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.Saran.ribbit.constants.ParseConstants;
import com.Saran.ribbit.helpers.FileHelper;
import com.Saran.ribbit.helpers.ImageResizer;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class RecipientsActivity extends ListActivity {
	
	private ParseUser mCurrentUser;
	private ParseRelation<ParseUser> mFriendsRelation;
	private List<ParseUser> mFriends;
	private MenuItem mSendButton;
	private String mFileType;
	private Uri mFileUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_recipients);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		mFileType = getIntent().getStringExtra(ParseConstants.KEY_FILE_TYPE);
		mFileUri = getIntent().getData();
		
		Log.d("Saran", mFileType+"   "+mFileUri);
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

		int id = item.getItemId();
		if (id == R.id.action_send) {
			
			ParseObject message = createMessage();
			if(message == null){
				AlertDialog.Builder builder = new AlertDialog.Builder(RecipientsActivity.this);
				builder.setMessage(R.string.file_select_error);
				builder.setTitle(R.string.inbox_error_title);
				
				AlertDialog dialog = builder.create();
				dialog.show();
			}else{
				sendMessage(message);
				finish();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void sendMessage(ParseObject message) { 
		message.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				if(e==null){
					Toast.makeText(RecipientsActivity.this,R.string.message_send, Toast.LENGTH_LONG).show();
				}else{
					AlertDialog.Builder builder = new AlertDialog.Builder(RecipientsActivity.this);
					builder.setMessage(R.string.general_error);
					builder.setTitle(R.string.inbox_error_title);
					
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			}
		});
	}

	private ParseObject createMessage() {
		//A parse file can be created from a bytearray. So we need to convert media to byte array
		ParseFile file;
		byte[] fileBytes = FileHelper.getByteArrayFromFile(RecipientsActivity.this, mFileUri);
		
		if(fileBytes == null){
				return null;
		}else{		
			//if it is an image, reduce size
			if(mFileType.equals(ParseConstants.FILE_TYPE_IMAGE)){
				fileBytes = FileHelper.reduceImageForUpload(fileBytes);
			}
			String filename = FileHelper.getFileName(RecipientsActivity.this,mFileUri, mFileType);
			file = new ParseFile(filename, fileBytes);
		}
		
		
		ParseObject message = new ParseObject(ParseConstants.CLASS_MESSAGES);
		message.put(ParseConstants.KEY_SENDER_ID, mCurrentUser.getObjectId());
		message.put(ParseConstants.KEY_SENDER_NAME, mCurrentUser.getUsername());
		message.put(ParseConstants.KEY_RECIPIENT_IDS,getRecipientIds());
		message.put(ParseConstants.KEY_FILE_TYPE,mFileType);
		message.put(ParseConstants.KEY_FILE, file);
		return message;
	}

	private ArrayList<String> getRecipientIds() {
		ArrayList<String> recipientIds = new ArrayList<String>();
		
		//Loop through each friend in list and check whether it is checked.
		for(int i=0; i<getListView().getCount(); i++){
			if(getListView().isItemChecked(i)){
				recipientIds.add(mFriends.get(i).getObjectId());
			}
		}
		return recipientIds;
	}

}
