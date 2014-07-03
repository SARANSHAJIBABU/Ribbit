package com.Saran.ribbit;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.parse.ParseUser;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
	
	public static final int CAPTURE_PHOTO_REQ_CODE = 0;
	public static final int CAPTURE_VIDE0_REQ_CODE = 1;
	public static final int CHOOSE_PHOTO_REQ_CODE = 2;
	public static final int CHOOSE_VIDEO_REQ_CODE = 3;


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
			Log.i("Saran", "Current user is "+currentUser.getUsername());
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
