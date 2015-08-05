package com.example.twitterflickrdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author optimus158
 * 
 *         Adapter Class for setting the fragments to the activity
 */
public class FragmentAdapter extends FragmentPagerAdapter {

	public FragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	public Fragment getItem(int arg0) {
		Fragment fragment = null;
		if (arg0 == 0) {
			fragment = new TweetsBySearch();
		}
		if (arg0 == 1) {
			fragment = new FlickrPhotosBySearch();
		}
		return fragment;
	}

	public int getCount() {
		return 2;
	}

}