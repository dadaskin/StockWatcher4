package com.adaskin.android.stockwatcher4.views;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.adaskin.android.stockwatcher4.R;
import com.adaskin.android.stockwatcher4.adapters.BuyBlockCursorAdapter;
import com.adaskin.android.stockwatcher4.database.DbAdapter;
import com.adaskin.android.stockwatcher4.fragments.AccountSelectionFragment;
import com.adaskin.android.stockwatcher4.fragments.AccountSelectionFragment.AlertOkListener;
import com.adaskin.android.stockwatcher4.models.AccountModel;
import com.adaskin.android.stockwatcher4.models.BuyBlock;
import com.adaskin.android.stockwatcher4.utilities.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class OwnedDetailsActivity extends GenericDetailsActivity implements AlertOkListener {
 
	private BuyBlock mChangingBlock; 
	private long mParentId;
	private AccountSelectionFragment mAccountsFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.owned_details);

		Bundle bundle = getIntent().getExtras();
		mSymbol = bundle.getString(Constants.SYMBOL_BUNDLE_KEY);
        setTitleString();

        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        
		mParentId = dbAdapter.fetchQuoteIdFromSymbol(mSymbol);
        mQuote = dbAdapter.fetchQuoteObjectFromId(mParentId);
        dbAdapter.close();
        
        registerForContextMenu(findViewById(android.R.id.list));
        
        fillData();
	}

	private void fillData() {
    	TextView nameField = (TextView)findViewById(R.id.owned_full_name_field);
    	TextView ppsField = (TextView)findViewById(R.id.owned_pps_field);
    	TextView divPSField = (TextView)findViewById(R.id.owned_divps_field);
    	TextView analOpField = (TextView)findViewById(R.id.owned_anal_op_field);
    	TextView yrMinField = (TextView)findViewById(R.id.owned_yr_min_field);
    	TextView yrMaxField = (TextView)findViewById(R.id.owned_yr_max_field);
    	TextView strikeField = (TextView)findViewById(R.id.owned_strike_price_field);
    	TextView gainField = (TextView)findViewById(R.id.owned_gain_target_field);
    	
    	nameField.setText(mQuote.mFullName);
		ppsField.setText(String.format(Constants.CURRENCY_FORMAT, mQuote.mPPS));
		String divPSAndYieldMsg = String.format(Constants.CURRENCY_FORMAT, mQuote.mDivPerShare) +
				                  "  (" +
				                  String.format(Constants.PERCENTAGE_FORMAT, mQuote.mDivPerShare*100f/mQuote.mPPS) +
				                  ")";
     	divPSField.setText(divPSAndYieldMsg);
		
        analOpField.setText(String.format(Constants.OPINION_FORMAT, mQuote.mAnalystsOpinion)); 
        yrMinField.setText(String.format(Constants.CURRENCY_FORMAT, mQuote.mYrMin));
        yrMaxField.setText(String.format(Constants.CURRENCY_FORMAT, mQuote.mYrMax));
        strikeField.setText(String.format(Constants.CURRENCY_FORMAT, mQuote.mStrikePrice));
        gainField.setText(String.format(Constants.PERCENTAGE_FORMAT, mQuote.mPctGainTarget));
        
        ListView blockListView = (ListView)findViewById(android.R.id.list);
        
        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        Cursor cursor = dbAdapter.fetchBuyBlockRecordsForThisSymbol(mQuote.mSymbol);
        dbAdapter.close();

        String[] fields = new String[] {DbAdapter.B_ACCOUNT,
        		                        DbAdapter.B_DATE, 
        		                        DbAdapter.B_NUM_SHARES,
        		                        DbAdapter.B_PPS,
        		                        DbAdapter.B_CHANGE_VS_BUY,
        		                        DbAdapter.B_EFF_YIELD};
         
        int[] ids = new int[] { R.id.account_color_field_id,
        		                R.id.date_field_id, 
        		                R.id.num_shares_field_id,
        		                R.id.buy_pps_field_id,
        		                R.id.chng_buy_field_id,
        		                R.id.eff_div_field_id};
        
        BuyBlockCursorAdapter bbca
               = new BuyBlockCursorAdapter(this, R.layout.buy_block_row, cursor, mQuote.mPctGainTarget, fields, ids);
        
        blockListView.setAdapter(bbca);
        
        requestExtraQuoteInformation(mQuote.mSymbol);
    }
	
    @Override
	protected void updateDisplayWithExtraInfo() {
		TextView nameField = (TextView)findViewById(R.id.owned_full_name_field);
		TextView analOpField = (TextView)findViewById(R.id.owned_anal_op_field);
		
		nameField.setText(mQuote.mFullName);
		if (mQuote.mAnalystsOpinion == 0.0f) {
			analOpField.setText("--");
		} else {
		    analOpField.setText(String.format(Constants.OPINION_FORMAT, mQuote.mAnalystsOpinion));
		}
		
	}

    
    
	// Create context menu and dispatch selections
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.longpress_buyblock, menu);
		return;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        String dateString = getDateStringFromRow(info.targetView);        
        
        switch(item.getItemId()) {
        case R.id.menu_block_change:
            changeNumShares(info.targetView);
            return true;
        case R.id.menu_account_change:
        	changeAccount(info.targetView);
        	return true;
        case R.id.menu_block_delete:
        	createAndShowConfirmDialog(dateString);
            return true;
        }
		return super.onContextItemSelected(item);
	}
	
	private String getDateStringFromRow(View v) {
		LinearLayout ll = (LinearLayout)v;
		TextView tv = (TextView)ll.getChildAt(Constants.DATE_VIEW_IN_BLOCK);
		return tv.getText().toString();
	}
	
	private void createAndShowConfirmDialog(String dateString) {
		String msg = "Delete block acquired on:\n" + dateString + "?";
		final String finalDateString = dateString;
		
		new AlertDialog.Builder(this)
		       .setTitle("Confirm Delete")
		       .setMessage(msg)
		       .setIcon(android.R.drawable.ic_dialog_alert)
		       .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
                    doDelete(finalDateString);
				}
			
   		    })
   		    .show();
	}
	
	private void doDelete(String dateString) {
		DbAdapter dbAdapter = new DbAdapter(this);
		dbAdapter.open();
		
		dbAdapter.removeBuyBlockRecord(mSymbol, dateString);
		dbAdapter.close();
		
		fillData();
	}

	
	// Handle Change Number of Shares
    private void changeNumShares(View v) {
    	String dateString = getDateStringFromRow(v);
    	
    	mChangingBlock = mQuote.findBuyBlockByDateString(dateString);
    	
    	TextView numSharesView = (TextView)((LinearLayout)v).getChildAt(Constants.NUM_SHARES_VIEW_IN_BLOCK);
    	String numSharesString = numSharesView.getText().toString();
    	float oldValue = 1543f;
    	try {
    		oldValue = Float.parseFloat(numSharesString);
    	} catch (NumberFormatException e) {
    		e.printStackTrace();
    	}
    	    	
    	Intent intent = new Intent(this, ChangeParameterActivity.class);
    	intent.putExtra(Constants.SYMBOL_BUNDLE_KEY, mQuote.mSymbol);
    	intent.putExtra(Constants.PARAM_NAME_BUNDLE_KEY, "Number of Shares");
    	intent.putExtra(Constants.OLD_VALUE_BUNDLE_KEY, oldValue);
    	startActivityForResult(intent, Constants.PARAMETER_CHANGE_ACTIVITY);
    }
	  
	
    // Handle changeAccount
    private void changeAccount(View v) {
    	FragmentManager manager = getSupportFragmentManager();
    	mAccountsFragment = new AccountSelectionFragment();
     	
    	// Use current AccountColor as default
  	    String dateString = getDateStringFromRow(v);
    	mChangingBlock = mQuote.findBuyBlockByDateString(dateString);
        int color = mChangingBlock.mAccountColor;
    	
    	Bundle args = new Bundle();
    	args.putInt("color", color);
    	
    	mAccountsFragment.setArguments(args);
    	mAccountsFragment.show(manager, "Account Selection Dialog");
    }
    

    
	@Override
	public void onOkClick(int position) {
    	List<Integer> colorList = AccountModel.getBlockAccountColorList();
    	int accountColor = colorList.get(position);
		mAccountsFragment.dismiss();
    	
        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        dbAdapter.removeQuoteRecord(mQuote.mSymbol);
        mQuote.mBuyBlockList.remove(mChangingBlock);
    	mChangingBlock.mAccountColor = accountColor;
        mQuote.mBuyBlockList.add(mChangingBlock);      
		dbAdapter.createQuoteRecord(mQuote);
		dbAdapter.close();
		fillData();
	}
    
    
    
    // Handle Add Another Block button
	public void addAnotherBlockButtonClicked(View v) {
		Intent intent = new Intent(this, BuyBlockAddActivity.class);
		intent.putExtra(Constants.BUY_BLOCK_SYMBOL_KEY, mQuote.mSymbol);
		startActivityForResult(intent, Constants.BUY_BLOCK_ADD_ACTIVITY);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		
		if (resultCode == Activity.RESULT_OK) {
			DbAdapter dbAdapter = new DbAdapter(this);
			dbAdapter.open();
			dbAdapter.removeQuoteRecord(mQuote.mSymbol);
			switch(requestCode) {
			case Constants.BUY_BLOCK_ADD_ACTIVITY:
				BuyBlock newBB = grabBuyBlockInfo(intent.getExtras());
			    mQuote.mBuyBlockList.add(newBB);
			    break;
			case Constants.PARAMETER_CHANGE_ACTIVITY:
				String paramName = intent.getStringExtra(Constants.PARAM_NAME_BUNDLE_KEY);
				float newValue =  intent.getFloatExtra(Constants.PARAM_NEW_VALUE_BUNDLE_KEY, 0.0f);
				if (paramName.contains("Strike Price")) {
					mQuote.mStrikePrice = newValue;
			    } else if (paramName.contains("Gain Target")) {
				    mQuote.mPctGainTarget = newValue;
			    } else {
			    	mQuote.mBuyBlockList.remove(mChangingBlock);
			    	mChangingBlock.mNumShares = newValue;
			    	mQuote.mBuyBlockList.add(mChangingBlock);
			    }
				break;
			}
			dbAdapter.createQuoteRecord(mQuote);
			dbAdapter.close();
			fillData();
		}
	}

    private BuyBlock grabBuyBlockInfo(Bundle bundle) {
    	String buyDateString = bundle.getString(Constants.BUY_BLOCK_DATE_KEY);
    	float buyPrice = bundle.getFloat(Constants.BUY_BLOCK_PRICE_KEY);
    	float numShares = bundle.getFloat(Constants.BUY_BLOCK_NUM_KEY);
    	float commissionPS = bundle.getFloat(Constants.BUY_BLOCK_COMMISSION_KEY);
    	int accountColor = bundle.getInt(Constants.BUY_BLOCK_ACCOUNT_COLOR_KEY);
    	
    	SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
    	Date buyDate = new Date();
    	try {
    	    buyDate = sdf.parse(buyDateString);
    	} catch(ParseException e) {
			e.printStackTrace();
    	}
    	
    	BuyBlock newBuyBlock = new BuyBlock(buyDate, numShares, buyPrice, commissionPS, 0.0f, 0.0f, accountColor); 
    
    	return newBuyBlock;
    }	
	

    
    // Handle parameter change buttons
    public void changeButtonClicked_StrikePrice(View v) {
    	Intent intent = new Intent(this, ChangeParameterActivity.class);
    	intent.putExtra(Constants.SYMBOL_BUNDLE_KEY, mQuote.mSymbol);
    	intent.putExtra(Constants.PARAM_NAME_BUNDLE_KEY, "Strike Price");
    	intent.putExtra(Constants.OLD_VALUE_BUNDLE_KEY, mQuote.mStrikePrice);
    	startActivityForResult(intent, Constants.PARAMETER_CHANGE_ACTIVITY);
    }
	
    public void changeButtonClicked_GainTarget(View v) {
    	Intent intent = new Intent(this, ChangeParameterActivity.class);
    	intent.putExtra(Constants.SYMBOL_BUNDLE_KEY, mQuote.mSymbol);
    	intent.putExtra(Constants.PARAM_NAME_BUNDLE_KEY, "Gain Target");
    	intent.putExtra(Constants.OLD_VALUE_BUNDLE_KEY, mQuote.mPctGainTarget);
    	startActivityForResult(intent, Constants.PARAMETER_CHANGE_ACTIVITY);
    }






}    	