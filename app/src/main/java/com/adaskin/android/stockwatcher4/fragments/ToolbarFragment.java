package com.adaskin.android.stockwatcher4.fragments;

import com.adaskin.android.stockwatcher4.R;
import com.adaskin.android.stockwatcher4.database.DbAdapter;
import com.adaskin.android.stockwatcher4.models.DataModel;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;

public class ToolbarFragment extends Fragment {

	private Activity mActivity;
	private TextView mDateTextView;
	private TextView mTimeTextView;
	
	private View mRefreshButtonView;
	
	private ToolbarListener activityCallback;
	
	public interface ToolbarListener {
		void addButtonClicked();
		void refreshButtonClicked();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			activityCallback = (ToolbarListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " nust implement ToolbarListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.toolbar_fragment, container, false);
        
        final ImageButton refreshButton = (ImageButton)view.findViewById(R.id.refresh_button_moving);
        final ImageButton addButton = (ImageButton)view.findViewById(R.id.add_button);
        mDateTextView = (TextView)view.findViewById(R.id.last_update_date_text);
        mTimeTextView = (TextView)view.findViewById(R.id.last_update_time_text);
        
        addButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addButtonClicked(v);				
			}
		});
        
        refreshButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				refreshButtonClicked(v);
			}
		});
		return view;
	}
	
		
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mActivity = getActivity();
		DbAdapter dbAdapter = new DbAdapter(mActivity);
		dbAdapter.open();
		
		new DataModel(dbAdapter);
		
    	Cursor cursor = dbAdapter.fetchLastUpdateRecord();
    	String dateString = cursor.getString(cursor.getColumnIndex(DbAdapter.U_DATE));
    	String timeString = cursor.getString(cursor.getColumnIndex(DbAdapter.U_TIME));
    	mDateTextView.setText(dateString);
    	mTimeTextView.setText(timeString);
    	dbAdapter.close();
	}

	
	@SuppressWarnings("UnusedParameters")
	private void addButtonClicked(View v) {
		activityCallback.addButtonClicked();
	}

	
	private void refreshButtonClicked(View v) {
		mRefreshButtonView = v;
		activityCallback.refreshButtonClicked();
	}

	
	public void updateLastUpdateStrings() {
		DbAdapter dbAdapter = new DbAdapter(mActivity);
		dbAdapter.open();

    	Cursor cursor = dbAdapter.fetchLastUpdateRecord();
    	String dateString = cursor.getString(cursor.getColumnIndex(DbAdapter.U_DATE));
    	String timeString = cursor.getString(cursor.getColumnIndex(DbAdapter.U_TIME));

    	mDateTextView.setText(dateString);
    	mTimeTextView.setText(timeString);
    	dbAdapter.close();
	}
	
	public void beginButtonAnimation(Animation animation)
	{
		animation.setRepeatCount(Animation.INFINITE);
		mRefreshButtonView.startAnimation(animation);
	}
	
	public void endButtonAnimation()
	{
       mRefreshButtonView.clearAnimation();		
	}

}
