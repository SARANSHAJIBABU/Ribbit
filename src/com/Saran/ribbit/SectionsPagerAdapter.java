package com.Saran.ribbit;

import java.util.Locale;


import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
	
	private Context mContext;

	public SectionsPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		mContext = context;
	}

	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a PlaceholderFragment (defined as a static inner class
		// below)
		
		if(position == 0){
			return new InboxFragment();
		}
		else if(position == 1){
			return new FriendsListFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		// Show 2 total pages.
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return mContext.getString(R.string.tab_section1).toUpperCase(l);
		case 1:
			return mContext.getString(R.string.tab_section2).toUpperCase(l);
		}
		return null;
	}
}