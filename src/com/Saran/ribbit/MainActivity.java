package com.Saran.ribbit;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.parse.ParseUser;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
	
	public static final int CAPTURE_PHOTO_REQ_CODE = 0;
	public static final int CAPTURE_VIDE0_REQ_CODE = 1;
	public static final int CHOOSE_PHOTO_REQ_CODE = 2;
	public static final int CHOOSE_VIDEO_REQ_CODE = 3;
	
	public static final int MEDIA_TYPE_IMAGE = 4;
	public static final int MEDIA_TYPE_VIDEO = 5;
	
	private Uri mMediaUri;


	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	
	private DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch(which){
				case 0:
					//Take picture
					Intent photoTakingIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					
					//uri to store the image
					mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
					if(mMediaUri==null){
						Toast.makeText(MainActivity.this,R.string.external_storage_error_message, Toast.LENGTH_SHORT).show();
					}
					photoTakingIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
					startActivityForResult(photoTakingIntent, CAPTURE_PHOTO_REQ_CODE);
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
			}
		}
		
		private Uri getOutputMediaFileUri(int mediaType){
	
			if(isExternalStorageAvailable()){
				//1. Get external storage directory
				//2. Create a subdirectory
				//3. Create a file name
				//4. Create a file
				//5. Return file's Uri
				
				//1. Get external storage directory 
						//-> get external storage dir of type pictures
						//-> create a new file using the above details and appname
				String appName = getString(R.string.app_name);
				File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),appName);
//				Log.d("Saran", "External Storage Dir path for images "+ Uri.fromFile(mediaStorageDir));

				
				//2. Create a subdirectory
						//Check if a directory exists at the above file path, if no, create one
				if(!mediaStorageDir.exists()){
					if(!mediaStorageDir.mkdirs()){
						//error occured
						return null;
					}
				}
				
				//3. Create a file name
				//4. Create a file
				File mediaFile = null;
				Date now = new Date();
				
				//create timestamp to uniguely name an image
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now);
				String path = mediaStorageDir.getPath() + File.separator;
				
				//if it is an image add path+_IMG+timestamp+jpg
				if(mediaType == MEDIA_TYPE_IMAGE){
					mediaFile = new File(path +"_IMG"+timeStamp+".jpg");
					
				//if it is an video add path+_VID+timestamp+mp4
				}else if(mediaType == MEDIA_TYPE_VIDEO){
					mediaFile = new File(path+"_VID"+timeStamp+".mp4");
				}else{
					return null;
				}
//				Log.d("Saran", "Captured file dir"+ Uri.fromFile(mediaFile));
				
				//create Uri from the file and return
				return Uri.fromFile(mediaFile);
			}else{
				return null;
			}
		}
		
		private boolean isExternalStorageAvailable(){
		String externalStorageState = Environment.getExternalStorageState();
			
			if(externalStorageState.equals(Environment.MEDIA_MOUNTED)){
				return true;
			}
			return false;
		}
	};
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_main);   
		
		//If the user is not logged in, route to login activity
		ParseUser currentUser = ParseUser.getCurrentUser();
		
		//check the user object is cached in or not
		if (currentUser==null) {
			doLogin();
		}else{
			//Log.i("Saran", "Current user is "+currentUser.getUsername());
		}
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the two
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),this);

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}
	
	@Override
	protected void onActivityResult(int reqCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(reqCode, resultCode, data);
		if(resultCode == RESULT_OK){
			//Add it to gallery
			Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			sendBroadcast(mediaScanIntent);
		}else if(resultCode!=RESULT_CANCELED){
			Toast.makeText(this, R.string.general_error, Toast.LENGTH_SHORT).show();
		}
	}


	private void doLogin() {
		Intent loginIntent = new Intent(this, LoginActivity.class);
		//clear the task stack
		loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(loginIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
		}else if(id == R.id.action_logout){
			ParseUser.logOut();
			this.doLogin();
		}else if(id == R.id.action_edit_friends){
			Intent editFriendsIntent = new Intent(this,EditFriendsActivity.class);
			startActivity(editFriendsIntent);
		}else if(id == R.id.action_camera){
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); 
			builder.setItems(R.array.camera_items, mDialogListener);		
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

}
