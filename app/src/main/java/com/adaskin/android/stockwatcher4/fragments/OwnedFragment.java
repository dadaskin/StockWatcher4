package com.adaskin.android.stockwatcher4.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.adaskin.android.stockwatcher4.adapters.QuoteCursorAdapter;
import com.adaskin.android.stockwatcher4.database.DbAdapter;
import com.adaskin.android.stockwatcher4.models.BuyBlock;
import com.adaskin.android.stockwatcher4.models.DataModel;
import com.adaskin.android.stockwatcher4.models.StockQuote;
import com.adaskin.android.stockwatcher4.utilities.Constants;
import com.adaskin.android.stockwatcher4.utilities.Status;
import com.adaskin.android.stockwatcher4.views.OwnedDetailsActivity;
import com.adaskin.android.stockwatcher4.views.OwnedAddActivity;
import com.adaskin.android.stockwatcher4.R;

public class OwnedFragment extends ListFragmentBase {

	private ListFragmentListener activityCallback;
	private int mTopVisiblePosition = -1;
	private int mTopPadding = -1;
		
	public OwnedFragment() {
	}

	private String getSymbolFromRow(View v) {
		LinearLayout ll = (LinearLayout)v;
		TextView tv = (TextView)ll.getChildAt(Constants.SYMBOL_VIEW_IN_QUOTE);
		return tv.getText().toString();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			activityCallback = (ListFragmentListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement ListFragmentListener");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerForContextMenu(getListView());

		
        fillData();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.longpress_owned, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_owned_delete) {
		    AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		    String symbol = getSymbolFromRow(info.targetView);
		    createAndShowConfirmDialog(symbol);
		    return true;
		}
		return super.onContextItemSelected(item);
	}

	private void createAndShowConfirmDialog(String symbol) {
		DbAdapter dbAdapter = new DbAdapter(getActivity());
		dbAdapter.open();
		
		DataModel model = new DataModel(dbAdapter);
		
		StockQuote quote = model.findStockQuoteBySymbol(symbol);
		int numBlocks = quote.mBuyBlockList.size();
        dbAdapter.close();
		
        String msg = "Delete symbol: " + symbol + "?\n";
        msg += "(Includes " + numBlocks + " Blocks)";
        
        final String finalSymbol = symbol;
        
		new AlertDialog.Builder(getActivity())
		          .setTitle("Confirm Delete")
		          .setMessage(msg)
		          .setIcon(android.R.drawable.ic_dialog_alert)
		          .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					
					    @Override
					    public void onClick(DialogInterface dialog, int which) {
                            doDelete(finalSymbol);						
					    }
				    })
				    .show();
		dbAdapter.close();
	}
	
	private void doDelete(String symbol) {
		DbAdapter dbAdapter = new DbAdapter(getActivity());
		dbAdapter.open();
		
		DataModel model = new DataModel(dbAdapter);
		model.changeStatusFromOwnedToWatch(symbol);
		
        dbAdapter.close();
        
        activityCallback.quoteAddedOrMoved();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		ListView lv = getListView();
        mTopVisiblePosition = lv.getFirstVisiblePosition();		
        View firstChildView = lv.getChildAt(0);
        mTopPadding = (firstChildView == null)? 0 : firstChildView.getTop() - lv.getPaddingTop();
			
		DbAdapter dbAdapter = new DbAdapter(getActivity());
        dbAdapter.open();
        
        DataModel model = new DataModel(dbAdapter);
        
        String symbol = getSymbolFromRow(v);
        
        StockQuote quote = model.findStockQuoteBySymbol(symbol);
        
        if (quote != null)
        {
        	Bundle bundle = new Bundle();
        	bundle.putString(Constants.SYMBOL_BUNDLE_KEY, quote.mSymbol);
        	Intent intent = new Intent(getActivity(), OwnedDetailsActivity.class);
        	intent.putExtras(bundle);
        	startActivityForResult(intent, Constants.OWNED_DETAIL_ACTIVITY);
        }
        
        dbAdapter.close();

	}
	
	private void fillData() {
		DbAdapter dbAdapter = new DbAdapter(getActivity());
        dbAdapter.open();
        Cursor cursor = dbAdapter.fetchAllQuoteRecordsByStatus(Status.OWNED);
        dbAdapter.close();  
        
        String[] fields = new String[] { DbAdapter.Q_ACCOUNT_COLOR,
        		                         DbAdapter.Q_SYMBOL,
        		                         DbAdapter.Q_PPS,
        		                         DbAdapter.Q_CHANGE_VS_CLOSE,
        		                         DbAdapter.Q_CHANGE_VS_BUY};
        
        int[] ids = new int[] {R.id.overall_account_color_field_id,
        		               R.id.symbol_field_id, 
        		               R.id.pps_field_id,
        		               R.id.chng_close_field_id,
        		               R.id.last_q_field_id};
        
        QuoteCursorAdapter qcs = new QuoteCursorAdapter(getActivity(), 
        		                                        cursor,
        		                                        fields,
        		                                        ids);
        
        setListAdapter(qcs);
        
        }

	@Override
	public void addAQuote() {
	   	Intent intent = new Intent(getActivity(), OwnedAddActivity.class);
    	startActivityForResult(intent, Constants.OWNED_ADD_ACTIVITY);
    }

	@Override
	public void redisplayList() {
		fillData();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == Constants.OWNED_DETAIL_ACTIVITY) {
			fillData();
			if (mTopVisiblePosition > 0) {
			    getListView().setSelectionFromTop(mTopVisiblePosition, mTopPadding);
			    mTopVisiblePosition = -1;
			}
		}
		
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == Constants.OWNED_ADD_ACTIVITY) {
			    grabNewQuoteInfoAndStore(data);
	            activityCallback.quoteAddedOrMoved();
			}
		}
	}
        

	private void grabNewQuoteInfoAndStore(Intent data) {
		Bundle bundle = data.getExtras();
		String symbol = bundle.getString(Constants.OWNED_ADD_SYMBOL_BUNDLE_KEY);
		float gainTargetPct = bundle.getFloat(Constants.OWNED_ADD_GAIN_TARGET_BUNDLE_KEY);
		String buyDateString = bundle.getString(Constants.BUY_BLOCK_DATE_KEY);
		float buyNumShares = bundle.getFloat(Constants.BUY_BLOCK_NUM_KEY);
		float buyPPS = bundle.getFloat(Constants.BUY_BLOCK_PRICE_KEY);
		int accountColor = bundle.getInt(Constants.BUY_BLOCK_ACCOUNT_COLOR_KEY);
		float commission = bundle.getFloat(Constants.BUY_BLOCK_COMMISSION_KEY);
		
    	SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
		Date buyDate = new Date();
     	try {
	  	    buyDate = sdf.parse(buyDateString);
	   	} catch(ParseException e) {
			e.printStackTrace();
	   	}
		
		BuyBlock firstBlock = new BuyBlock(buyDate, buyNumShares, buyPPS, commission, 0.0f, accountColor);
		
		DbAdapter dbAdapter = new DbAdapter(getActivity());
		dbAdapter.open();
		DataModel model = new DataModel(dbAdapter);
		model.addOwned(symbol, gainTargetPct, firstBlock);
		dbAdapter.close();
   }
	
	public void moveToOwned(Intent data) {
		grabNewQuoteInfoAndStore(data);
		activityCallback.quoteAddedOrMoved();
	}
	
}
