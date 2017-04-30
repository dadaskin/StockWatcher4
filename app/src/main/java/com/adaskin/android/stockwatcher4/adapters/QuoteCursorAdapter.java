package com.adaskin.android.stockwatcher4.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adaskin.android.stockwatcher4.R;
import com.adaskin.android.stockwatcher4.database.DbAdapter;
import com.adaskin.android.stockwatcher4.utilities.Constants;
import com.adaskin.android.stockwatcher4.utilities.Status;

public class QuoteCursorAdapter extends SimpleCursorAdapter {

	private static class ViewHolder {
		 TextView accountView;
		 TextView symbolView;
		 TextView ppsView;
		 TextView pctChangeSinceLastCloseView;
		 TextView lastQView;
	}
	
	private final int mLayoutId;
	private final LayoutInflater mInflater;
	private final int mOverallAccountIdx;
	private final int mSymbolIdx;
	private final int mPPSIdx;
	private final int mChngVsCloseIdx;
	private final int mStrikeIdx;
	private final int mGainTargetIdx;
	
	public final int mNormalBackgroundColor;
	public final int mBuyBackgroundColor;
    public final int mSellBackgroundColor;
    
    public final int mNeutralTextColor;
    public final int mPositiveDayChangeTextColor;
    public final int mNegativeTextColor;
		
	public QuoteCursorAdapter(Context context, 
			                  int layoutId, 
			                  Cursor cursor,
			                  String[] fields, 
			                  int[] ids) {
		super(context, layoutId, cursor, fields, ids, 0);
		mLayoutId = layoutId;
		mInflater = LayoutInflater.from(context);

		mOverallAccountIdx = cursor.getColumnIndex(DbAdapter.Q_ACCOUNT_COLOR);
		mSymbolIdx = cursor.getColumnIndex(DbAdapter.Q_SYMBOL);
		mPPSIdx = cursor.getColumnIndex(DbAdapter.Q_PPS);
		mChngVsCloseIdx = cursor.getColumnIndex(DbAdapter.Q_CHANGE_VS_CLOSE);
		mStrikeIdx = cursor.getColumnIndex(DbAdapter.Q_STRIKE);
		mGainTargetIdx = cursor.getColumnIndex(DbAdapter.Q_GAIN_TARGET);
		
        Resources resources = context.getResources();
        mNormalBackgroundColor = resources.getColor(R.color.list_background_color);
        mSellBackgroundColor = resources.getColor(R.color.over_gain_target_color);
        mBuyBackgroundColor = resources.getColor(R.color.under_strike_price_color);
        
        mNeutralTextColor = resources.getColor(R.color.neutral_text_color);
        mPositiveDayChangeTextColor = resources.getColor(R.color.positive_text_color);
        mNegativeTextColor = resources.getColor(R.color.negative_text_color);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = mInflater.inflate(mLayoutId, parent, false);
		ViewHolder holder = new ViewHolder();
		holder.accountView = (TextView)view.findViewById(R.id.overall_account_color_field_id);
		holder.symbolView = (TextView)view.findViewById(R.id.symbol_field_id);
		holder.ppsView = (TextView)view.findViewById(R.id.pps_field_id);
		holder.pctChangeSinceLastCloseView = (TextView)view.findViewById(R.id.chng_close_field_id);
		holder.lastQView = (TextView)view.findViewById(R.id.last_q_field_id);
		view.setTag(holder);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder)view.getTag();
        
        DbAdapter dbAdapter = new DbAdapter(context);
        dbAdapter.open();
        
        //Status status = mDbAdapter.getStatus(cursor);
        Status status = dbAdapter.getStatus(cursor);
        
        // Account
        int colorValue = cursor.getInt(mOverallAccountIdx);
        holder.accountView.setBackgroundColor(colorValue);

        // Symbol
        String symbol = cursor.getString(mSymbolIdx);
    	holder.symbolView.setText(symbol);
        
    	// PPS
        float pps = cursor.getFloat(mPPSIdx);
        holder.ppsView.setText(String.format(Constants.CURRENCY_FORMAT, pps));
       	float strikePrice = cursor.getFloat(mStrikeIdx);
    	adjustPPSFieldColor(holder.ppsView, strikePrice, pps);
        
        // Change since last close
        float chngVsClose = cursor.getFloat(mChngVsCloseIdx);
        showZeroWithoutSign(holder.pctChangeSinceLastCloseView, chngVsClose);
        adjustChangeSinceLastCloseFieldColor(holder.pctChangeSinceLastCloseView, chngVsClose);
        
        // Change since buy (Owned) or Strike Price (Watch)
        if (status == Status.WATCH) {
        	float strike = cursor.getFloat(mStrikeIdx);
        	holder.lastQView.setText(String.format(Constants.CURRENCY_FORMAT, strike));
        	holder.lastQView.setBackgroundColor(mNormalBackgroundColor);
        } else {
//        	mDbAdapter.open();
//        	float changeSinceBuy = mDbAdapter.getBestChangeSinceBuy(symbol);
//        	mDbAdapter.close();
        	dbAdapter.open();
        	float changeSinceBuy = dbAdapter.getBestChangeSinceBuy(symbol);
        	dbAdapter.close();
        	showZeroWithoutSign(holder.lastQView, changeSinceBuy);
        	float gainTarget = cursor.getFloat(mGainTargetIdx);
        	adjustChangeSinceBuyFieldColor(holder.lastQView, changeSinceBuy, gainTarget);
        }
	}

	private void showZeroWithoutSign(TextView view, float value) {
		if ((value < Constants.POSITVE_ONE_DECIMAL_LIMIT) &&
        	(value > Constants.NEGATIVE_ONE_DECIMAL_LIMIT))	{
			view.setText(String.format(Constants.PERCENTAGE_FORMAT, 0.0f));
        } else {
        	view.setText(String.format(Constants.PERCENTAGE_FORMAT, value));
        }
	}

	private void adjustChangeSinceBuyFieldColor(TextView view, float changeSinceBuy, float gainTarget) {
		if (changeSinceBuy > Constants.POSITVE_ONE_DECIMAL_LIMIT) {
			view.setTextColor(mPositiveDayChangeTextColor);
		} else if (changeSinceBuy < Constants.NEGATIVE_ONE_DECIMAL_LIMIT){
			view.setTextColor(mNegativeTextColor);
		} else {
			view.setTextColor(mNeutralTextColor);
		}

		if (changeSinceBuy > gainTarget) {
			view.setBackgroundColor(mSellBackgroundColor);
		    view.setTextColor(mNeutralTextColor);
		} else {
			view.setBackgroundColor(mNormalBackgroundColor);
		}
	}

	private void adjustChangeSinceLastCloseFieldColor(TextView view,
			float chngVsClose) {
		if (chngVsClose > Constants.POSITVE_ONE_DECIMAL_LIMIT) {
        	view.setTextColor(mPositiveDayChangeTextColor);
        } else if (chngVsClose  < Constants.NEGATIVE_ONE_DECIMAL_LIMIT){
        	view.setTextColor(mNegativeTextColor);
        } else {
        	view.setTextColor(mNeutralTextColor);
        }
	}

	private void adjustPPSFieldColor(TextView view, float strikePrice, float value) {
      	if (strikePrice > value) {
      		view.setBackgroundColor(mBuyBackgroundColor);
       	} else {
       		view.setBackgroundColor(mNormalBackgroundColor);
       	}
	}
}
