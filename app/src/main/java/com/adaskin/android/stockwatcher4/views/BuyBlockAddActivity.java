package com.adaskin.android.stockwatcher4.views;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.adaskin.android.stockwatcher4.R;
import com.adaskin.android.stockwatcher4.fragments.AccountSelectionFragment;
import com.adaskin.android.stockwatcher4.fragments.DatePickerDialogFragment;
import com.adaskin.android.stockwatcher4.fragments.AccountSelectionFragment.AlertOkListener;
import com.adaskin.android.stockwatcher4.models.AccountModel;
import com.adaskin.android.stockwatcher4.utilities.Constants;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class BuyBlockAddActivity extends ActionBarActivity implements AlertOkListener, DatePickerDialog.OnDateSetListener{

	private Calendar mCalendar;
	private String mSymbol;
	private Button mDateButton;
	private int mAccountColor;
	private final SimpleDateFormat mDateFormatter = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_block_add);

        mCalendar = Calendar.getInstance();
        mSymbol = getIntent().getStringExtra(Constants.BUY_BLOCK_SYMBOL_KEY);
        mDateButton = (Button)findViewById(R.id.buy_block_date);
        mDateButton.setOnClickListener(mDateButtonOnClickListener);
        Button accountChangeButton = (Button)findViewById(R.id.buy_block_account_change_button);
        accountChangeButton.setOnClickListener(mAccountChangeButtonOnClickListener);
        mDateButton.setText(mDateFormatter.format(mCalendar.getTime()));
        mAccountColor = Constants.ACCOUNT_UNKNOWN;
        TextView accountColorBlock = (TextView)findViewById(R.id.buy_block_account_color_field);
        accountColorBlock.setBackgroundColor(mAccountColor);
        TextView accountName = (TextView)findViewById(R.id.buy_block_account_name_text_view);
        accountName.setText("Unassigned");
        
        setTitleString();
	}
	
	private void setTitleString()
	{
		ActionBar actionBar = this.getSupportActionBar();

		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View customTitleView = inflater.inflate(R.layout.custom_titlebar, null);
		TextView tv = (TextView)customTitleView.findViewById(R.id.custom_title);
		tv.setText(getString(R.string.app_name) + getString(R.string.add_buy_block) + mSymbol);
		actionBar.setCustomView(customTitleView);
	}
	
	@Override
	public void onBackPressed() {
        Intent returnIntent = new Intent();
    	setResult(Activity.RESULT_CANCELED, returnIntent);
    	finish();
	}

    @SuppressWarnings("UnusedParameters")
	public void doneButtonClicked(View view) {
    	
    	EditText buyPriceField = (EditText)findViewById(R.id.buy_block_price);
    	EditText numSharesField = (EditText)findViewById(R.id.buy_block_num_shares);

    	String buyDateString = mDateButton.getText().toString();
    	float buyPrice;
    	float numShares;
    	try {
    	    buyPrice = Float.parseFloat(buyPriceField.getText().toString());
    	    numShares = Float.parseFloat(numSharesField.getText().toString());
    	} catch(NumberFormatException e) {
    		e.printStackTrace();
    		Toast.makeText(this, Constants.EMPTY_FIELD_ERR_MSG, Toast.LENGTH_LONG).show();
    		return;
    	}
    	Bundle bundle = new Bundle();
    	bundle.putString(Constants.BUY_BLOCK_SYMBOL_KEY, mSymbol);
        bundle.putString(Constants.BUY_BLOCK_DATE_KEY,buyDateString); 	
        bundle.putFloat(Constants.BUY_BLOCK_PRICE_KEY, buyPrice);
        bundle.putFloat(Constants.BUY_BLOCK_NUM_KEY, numShares);
        bundle.putInt(Constants.BUY_BLOCK_ACCOUNT_COLOR_KEY, mAccountColor);

        Intent returnIntent = new Intent();
    	returnIntent.putExtras(bundle);
    	
    	setResult(Activity.RESULT_OK, returnIntent);
    	finish();
    }    
    
    
    
    // Date Picker methods
	private final OnClickListener mDateButtonOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			showDatePicker();
		}
	};
    
	private void showDatePicker() {
		int year = mCalendar.get(Calendar.YEAR);
		int month = mCalendar.get(Calendar.MONTH);
		int day = mCalendar.get(Calendar.DAY_OF_MONTH);
		
		DialogFragment newFragment = DatePickerDialogFragment.newInstance(year, month, day);
		newFragment.show(getSupportFragmentManager(), "DatePicker");
    }    
	
	   @Override
	   public void onDateSet(DatePicker view, int year, int month, int day) {
		    mCalendar.set(year, month, day);
	        mDateButton.setText(mDateFormatter.format(mCalendar.getTime()));
	   }
	

	// Account change methods
	private final OnClickListener mAccountChangeButtonOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			showAccountSelectionDialog();
		}
	};

	 private void showAccountSelectionDialog(){
    	FragmentManager manager = getSupportFragmentManager();
    	AccountSelectionFragment accountFragment = new AccountSelectionFragment();
    	
    	// Use current AccountColor as default
    	Bundle args = new Bundle();
    	args.putInt("color", mAccountColor);
    	
    	accountFragment.setArguments(args);
    	accountFragment.show(manager, "Account Selection Dialog");
    }

	@Override
	public void onOkClick(int position) {
    	List<Integer> colorList = AccountModel.getBlockAccountColorList();
    	List<CharSequence> nameList = AccountModel.getBlockAccountNameList();
    	
    	mAccountColor = colorList.get(position);
    	CharSequence accountName = nameList.get(position);
    	
    	TextView accountColorView = (TextView)findViewById(R.id.buy_block_account_color_field);
    	TextView accountNameView = (TextView)findViewById(R.id.buy_block_account_name_text_view);
    	
    	accountColorView.setBackgroundColor(mAccountColor);
    	accountNameView.setText(accountName);
	}


//  Do we need to handle these?   Enhancement F2.
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//
//	}
//
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//	}

}
