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

import java.util.Locale;

public class BuyBlockCursorAdapter extends SimpleCursorAdapter{

	static class ViewHolder {
		TextView colorView;
		TextView dateView;
		TextView numSharesView;
		TextView ppsBuyView;
		TextView pctChangeSinceBuyView;
		TextView effYieldView;
	}


	private final LayoutInflater mInflater;
	private final int mAccountIdx;
	private final int mDateIdx;
	private final int mNumSharesIdx;
	private final int mPPSBuyIdx;
	private final int mPctChangeSinceBuyIdx;
	private final int mEffYieldIdx;
	
	private final float mGainTarget;

	
	public BuyBlockCursorAdapter(Context context, 
			                     Cursor cursor,
			                     float gainTarget,
			                     String[] fields,
			                     int[] ids) {	                    
		super(context, R.layout.buy_block_row, cursor, fields, ids, 0);
        mInflater = LayoutInflater.from(context);
        
        mAccountIdx = cursor.getColumnIndex(DbAdapter.B_ACCOUNT);
        mDateIdx = cursor.getColumnIndex(DbAdapter.B_DATE);
        mNumSharesIdx = cursor.getColumnIndex(DbAdapter.B_NUM_SHARES);
        mPPSBuyIdx = cursor.getColumnIndex(DbAdapter.B_PPS);
        mPctChangeSinceBuyIdx = cursor.getColumnIndex(DbAdapter.B_CHANGE_VS_BUY);
        mEffYieldIdx = cursor.getColumnIndex(DbAdapter.B_EFF_YIELD);
        
        mGainTarget = gainTarget;
         
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = mInflater.inflate(R.layout.buy_block_row, parent, false);
		ViewHolder  holder = new ViewHolder();
		holder.colorView = (TextView)view.findViewById(R.id.account_color_field_id);
		holder.dateView = (TextView)view.findViewById(R.id.date_field_id);
		holder.numSharesView = (TextView)view.findViewById(R.id.num_shares_field_id);
		holder.ppsBuyView = (TextView)view.findViewById(R.id.buy_pps_field_id);
		holder.pctChangeSinceBuyView = (TextView)view.findViewById(R.id.chng_buy_field_id);
		holder.effYieldView = (TextView)view.findViewById(R.id.eff_div_field_id);
		view.setTag(holder);
		return view;
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		ViewHolder holder = (ViewHolder)view.getTag();

		// Color
		int colorValue = cursor.getInt(mAccountIdx);
		holder.colorView.setBackgroundColor(colorValue);
		
		// Date
	    String dateStr = cursor.getString(mDateIdx);
		holder.dateView.setText(dateStr);
			
		// Number of Shares
		float numShares = cursor.getFloat(mNumSharesIdx);
		holder.numSharesView.setText(String.format(Locale.US,Constants.NUM_SHARES_FORMAT, numShares));
			
		// Buy prices per share
		float ppsBuy = cursor.getFloat(mPPSBuyIdx);
		holder.ppsBuyView.setText(String.format(Locale.US,Constants.CURRENCY_FORMAT, ppsBuy));
			
		// % change since buy
	    float chngVsBuy = cursor.getFloat(mPctChangeSinceBuyIdx);
	    showZeroWithoutSign(holder.pctChangeSinceBuyView, chngVsBuy);
	    adjustTextColor(context, holder.pctChangeSinceBuyView, chngVsBuy);
	    
	    // Effective Yield
	    float effYield = cursor.getFloat(mEffYieldIdx);
	    if (effYield > Constants.POSITIVE_ONE_DECIMAL_LIMIT) {
   		    holder.effYieldView.setText(String.format(Locale.US,Constants.PERCENTAGE_FORMAT, effYield));
	    } else {
	    	holder.effYieldView.setText("--");
	    }
	}

	private void showZeroWithoutSign(TextView view, float value) {
		if ((value < Constants.POSITIVE_ONE_DECIMAL_LIMIT) &&
        	(value > Constants.NEGATIVE_ONE_DECIMAL_LIMIT))	{
			view.setText(String.format(Locale.US,Constants.PERCENTAGE_FORMAT, 0.0f));
        } else {
        	view.setText(String.format(Locale.US,Constants.PERCENTAGE_FORMAT, value));
        }
	}
	
	private void adjustTextColor(Context context, TextView view, float value) {
        Resources resources = context.getResources();
        int normalBackgroundColor = resources.getColor(R.color.list_background_color);
        int highlightBackgroundColor = resources.getColor(R.color.over_gain_target_color);
        
        int positiveTextColor = resources.getColor(R.color.positive_text_color);
        int neutralTextColor = resources.getColor(R.color.neutral_text_color);
        int negativeTextColor = resources.getColor(R.color.negative_text_color);
		
		if (value > Constants.POSITIVE_ONE_DECIMAL_LIMIT) {
	    	view.setTextColor(positiveTextColor);
	    } else if (value < Constants.NEGATIVE_ONE_DECIMAL_LIMIT) {
	    	view.setTextColor(negativeTextColor);
	    } else {
	    	view.setTextColor(neutralTextColor);
	    }
		
	    if (value > mGainTarget) {
	    	view.setBackgroundColor(highlightBackgroundColor);
 	       view.setTextColor(neutralTextColor);	        
	    } else {
	    	view.setBackgroundColor(normalBackgroundColor);
	    }

	}
}
