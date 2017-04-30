package com.adaskin.android.stockwatcher4.adapters;

import com.adaskin.android.stockwatcher4.fragments.ListFragmentBase;
import com.adaskin.android.stockwatcher4.fragments.OwnedFragment;
import com.adaskin.android.stockwatcher4.fragments.WatchFragment;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

	private OwnedFragment mOwnedFragment = null;
	private WatchFragment mWatchFragment = null;
	
	public SectionsPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	@Override
	public ListFragmentBase getItem(int position) {
		
		ListFragmentBase fragment = null;
		
		switch(position) {
		case 0:
		    if (mOwnedFragment == null) {
		    	mOwnedFragment =  new OwnedFragment();
		    }
		    fragment = mOwnedFragment;	
			break;
		case 1: 
			if (mWatchFragment == null) {
				mWatchFragment = new WatchFragment();
			}
			fragment = mWatchFragment;
			break;
		}
		
		return fragment;
	}

	@Override
	public int getCount() {
		return 2;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case 0: 
			return "Owned";
		case 1:
			return "Watch";
		}
		return null;
	}	
	
	public void moveToOwned(Intent data) {
		mOwnedFragment.moveToOwned(data);
	}

}
