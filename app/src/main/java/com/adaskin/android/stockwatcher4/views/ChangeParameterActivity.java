package com.adaskin.android.stockwatcher4.views;

import com.adaskin.android.stockwatcher4.R;
import com.adaskin.android.stockwatcher4.utilities.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeParameterActivity extends ActionBarActivity {

	private String mParamName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.parameter_change);
        
        setTitleString();
        
        Bundle bundle = getIntent().getExtras();
        String symbol = bundle.getString(Constants.SYMBOL_BUNDLE_KEY);
        mParamName = bundle.getString(Constants.PARAM_NAME_BUNDLE_KEY);
        float oldValue = bundle.getFloat(Constants.OLD_VALUE_BUNDLE_KEY);

        setNameAndOriginalValue(symbol, mParamName, oldValue);
        
	}
	
	
	private void setTitleString()
	{
		ActionBar actionBar = this.getSupportActionBar();
  	    //actionBar.setDisplayShowTitleEnabled(true);
		
        //actionBar.setDisplayShowTitleEnabled(true);
		
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View customTitleView = inflator.inflate(R.layout.custom_titlebar, null);
		TextView tv = (TextView)customTitleView.findViewById(R.id.custom_title);
		tv.setText(getString(R.string.app_name) + getString(R.string.change_parameter));
		actionBar.setCustomView(customTitleView);
	}
	
	@Override
	public void onBackPressed() {
		Intent returnIntent = new Intent();
		setResult(Activity.RESULT_CANCELED, returnIntent);
		finish();
	}
    
    private void setNameAndOriginalValue(String symbol, String paramName, float oldValue) {
    	String format = Constants.NUM_SHARES_FORMAT;
    	if (paramName.contains("Gain Target"))
    	{	format = Constants.PERCENTAGE_FORMAT;
    	} else if (paramName.contains("Strike Price")) {
    		format = Constants.CURRENCY_FORMAT;
    	} 
    	
    	TextView nameView = (TextView)findViewById(R.id.param_change_name);
    	nameView.setText("Change \"" + paramName +"\" parameter on " + symbol);
    	
    	TextView oldValueView = (TextView)findViewById(R.id.param_change_old_value);
    	oldValueView.setText("Old Value: " + String.format(format, oldValue));
    }
    
    public void doneButtonClicked(View v) {
    	EditText newValueField = (EditText)findViewById(R.id.param_change_new_value);
    	float newValue;
    	try {
    		newValue = Float.parseFloat(newValueField.getText().toString());
    	} catch(NumberFormatException e) {
    		e.printStackTrace();
    		Toast.makeText(this, Constants.EMPTY_FIELD_ERR_MSG, Toast.LENGTH_LONG).show();
    		return;
    	}
    	Intent returnIntent = new Intent();
    	returnIntent.putExtra(Constants.PARAM_NAME_BUNDLE_KEY, mParamName);
    	returnIntent.putExtra(Constants.PARAM_NEW_VALUE_BUNDLE_KEY, newValue);
    	setResult(Activity.RESULT_OK, returnIntent);
    	finish();
    }



	
}
