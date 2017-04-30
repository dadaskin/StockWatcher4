package com.adaskin.android.stockwatcher4.fragments;

import android.content.Intent;
import android.support.v4.app.ListFragment;

abstract public class ListFragmentBase extends ListFragment {

	public interface ListFragmentListener {
		void quoteAddedOrMoved();
		void moveToOwned(Intent data);
	}
	
	public abstract void addAQuote();
	
	public abstract void redisplayList();
}
