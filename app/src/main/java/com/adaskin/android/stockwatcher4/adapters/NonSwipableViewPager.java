package com.adaskin.android.stockwatcher4.adapters;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NonSwipableViewPager extends ViewPager {
	
	public NonSwipableViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NonSwipableViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// Never allow swiping to switch between pages
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// Never allow swiping to switch between pages
		return false;
	}
}
