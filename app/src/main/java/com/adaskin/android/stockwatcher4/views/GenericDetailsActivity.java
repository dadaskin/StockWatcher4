package com.adaskin.android.stockwatcher4.views;

import com.adaskin.android.stockwatcher4.R;
import com.adaskin.android.stockwatcher4.models.StockQuote;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class GenericDetailsActivity extends ActionBarActivity {

	StockQuote mQuote;
	String mSymbol;

	public GenericDetailsActivity() {
		super();
	}

	void setTitleString() {
		ActionBar actionBar = this.getSupportActionBar();
	
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View customTitleView = inflater.inflate(R.layout.custom_titlebar, null);
		TextView tv = (TextView)customTitleView.findViewById(R.id.custom_title);
		tv.setText(getString(R.string.app_name) + ": " + mSymbol + getString(R.string.detail) );
		actionBar.setCustomView(customTitleView);
	}

}