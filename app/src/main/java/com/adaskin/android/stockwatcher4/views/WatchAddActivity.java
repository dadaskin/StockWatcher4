package com.adaskin.android.stockwatcher4.views;

import com.adaskin.android.stockwatcher4.R;
import com.adaskin.android.stockwatcher4.database.DbAdapter;
import com.adaskin.android.stockwatcher4.utilities.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WatchAddActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_add);

        setTitleString();
	}
	
	private void setTitleString()
	{
		ActionBar actionBar = this.getSupportActionBar();

		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View customTitleView = inflator.inflate(R.layout.custom_titlebar, null);
		TextView tv = (TextView)customTitleView.findViewById(R.id.custom_title);
		tv.setText(getString(R.string.app_name) + getString(R.string.add_watch));
		actionBar.setCustomView(customTitleView);
	}
	
	@Override
	public void onBackPressed() {
        Intent returnIntent = new Intent();
    	setResult(Activity.RESULT_CANCELED, returnIntent);
    	finish();
	}
	
    public void doneButtonClicked(View v) {
    	
    	EditText symbolField = (EditText)findViewById(R.id.watch_add_symbol);
    	EditText strikePriceField = (EditText)findViewById(R.id.watch_add_strike_price);
    	
    	// Check for empty fields
    	String symbol = symbolField.getText().toString();
    	if (symbol.isEmpty()) {
    		Toast.makeText(this, Constants.EMPTY_FIELD_ERR_MSG, Toast.LENGTH_LONG).show();
    		return;
    	}
    	
    	float strikePrice;
    	try {
    		strikePrice = Float.parseFloat(strikePriceField.getText().toString());
    	} catch(NumberFormatException e) {
    		e.printStackTrace();
    		Toast.makeText(this, Constants.EMPTY_FIELD_ERR_MSG, Toast.LENGTH_LONG).show();
    		return;
    	}
    	
    	// Check for Duplicate symbol. 
    	DbAdapter dbAdapter = new DbAdapter(this);
    	dbAdapter.open();
    	long existingId = dbAdapter.fetchQuoteIdFromSymbol(symbol);
    	if (existingId != -1) {
    		createAlertDuplicateDialog(symbol);
    		return;
    	}

    	// If it is a new symbol bundle up the information and finish the activity
    	Bundle bundle = new Bundle();
        bundle.putString(Constants.WATCH_ADD_SYMBOL_BUNDLE_KEY, symbol);
    	bundle.putFloat(Constants.WATCH_ADD_STRIKE_PRICE_BUNDLE_KEY, strikePrice);
    	
    	Intent returnIntent = new Intent();
    	returnIntent.putExtras(bundle);
    	setResult(Activity.RESULT_OK, returnIntent);
    	finish();
    }
    
	private void createAlertDuplicateDialog(String symbol) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_alert)
		       .setTitle("Duplicate Symbol")
		       .setMessage(createAlertDuplicateMessage(symbol))
		       .setPositiveButton("OK", mAlertConfirmListener)
		       .setCancelable(false)
		       .show();
	}

    private String createAlertDuplicateMessage(String symbol) {
		return "Symbol: " + symbol + " is a Duplicate.\nIgnoring this input.";
    }
 
    private final DialogInterface.OnClickListener mAlertConfirmListener =
    		new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
		        	Intent returnIntent = new Intent();
		        	setResult(Activity.RESULT_CANCELED, returnIntent);
		        	finish();
				}
			};
	
    
}
