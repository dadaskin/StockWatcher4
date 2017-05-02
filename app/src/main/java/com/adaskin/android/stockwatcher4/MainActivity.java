package com.adaskin.android.stockwatcher4;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.adaskin.android.stockwatcher4.adapters.NonSwipableViewPager;
import com.adaskin.android.stockwatcher4.adapters.SectionsPagerAdapter;
import com.adaskin.android.stockwatcher4.database.DbAdapter;
import com.adaskin.android.stockwatcher4.fragments.ListFragmentBase;
import com.adaskin.android.stockwatcher4.fragments.ListFragmentBase.ListFragmentListener;
import com.adaskin.android.stockwatcher4.fragments.ToolbarFragment;
import com.adaskin.android.stockwatcher4.fragments.ToolbarFragment.ToolbarListener;
import com.adaskin.android.stockwatcher4.models.BuyBlock;
import com.adaskin.android.stockwatcher4.models.StockQuote;
import com.adaskin.android.stockwatcher4.utilities.Constants;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity
            implements ActionBar.TabListener, ToolbarListener, ListFragmentListener{

	private ActionBar mActionBar;
	private SectionsPagerAdapter mPagerAdapter;
	private NonSwipableViewPager mViewPager;
	public View mCustomTitleView;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mActionBar = getSupportActionBar(); 
		
		setUpActionBar(mActionBar);
		setUpPager(mActionBar);
	}

	private void setUpActionBar(ActionBar actionBar)
	{
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//actionBar.setDisplayShowTitleEnabled(true);

		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mCustomTitleView = inflator.inflate(R.layout.custom_main_titlebar, null);
		TextView tv = (TextView)mCustomTitleView.findViewById(R.id.custom_title);
		tv.setText(getString(R.string.app_name));
		actionBar.setCustomView(mCustomTitleView);
	}
	
	// Handle Pager and Fragments
	private void setUpPager(ActionBar actionBar) {

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		
		// Set up the ViewPager with the sections adapter.
		mViewPager = (NonSwipableViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);
		
		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						MainActivity.this.mActionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab().setText(mPagerAdapter.getPageTitle(i)).setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction arg1) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction arg1) {
	}

	
	// Handle Toolbar buttons
	@Override
	public void addButtonClicked() {
		int currentItem = mViewPager.getCurrentItem();
		ListFragmentBase fragment = mPagerAdapter.getItem(currentItem);
		fragment.addAQuote();
	}

	@Override
	public void refreshButtonClicked() {
	    ToolbarFragment toolbarFragment = (ToolbarFragment)getSupportFragmentManager().findFragmentById(R.id.footer_fragment);
	          
		Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_1sec_center);
        toolbarFragment.beginButtonAnimation(rotation);
        refreshQuotes();
	}

	private void refreshQuotes() {
		DbAdapter dbAdapter = new DbAdapter(this);
		dbAdapter.open();
		
		List<StockQuote> quoteList = dbAdapter.fetchStockQuoteList();
		List<String> symbolList = new ArrayList<String>();
		for (StockQuote q : quoteList) {
			symbolList.add(q.mSymbol);
		}
		dbAdapter.close();
		
		String urlString = formURLString(symbolList);
		DoNetworkTask task = new DoNetworkTask(this); 
		task.execute(urlString);
		
	}

    public void networkTaskCompleted()
    {
		UpdateDateStringsInDB();
        ToolbarFragment toolbarFragment = (ToolbarFragment)getSupportFragmentManager().findFragmentById(R.id.footer_fragment);
        
    	toolbarFragment.endButtonAnimation();
        toolbarFragment.updateLastUpdateStrings();
    }


	// Development Note 06Dec13:   At first I tried to put the suffix characters in their own 
	//    variable and then increment the urlString with that variable.  This left out the 
	//    "&" character.   
	private String formURLString(List<String> symbols){
		String urlString = "http://finance.yahoo.com/d/quotes.csv?s=";
		for (String symbol: symbols) {
		    urlString += symbol + "+";	
		}
		
		// Replace last "+" with "&"
		int lastIdx = urlString.lastIndexOf("+");
		urlString = (String)urlString.subSequence(0, lastIdx);

    	// Flags:  s  = symbol
    	//         l1 = last trade price
    	//         k  = 52 week high
    	//         j  = 52 week low
    	//         d  = Dividend/share
    	//         p  = Previous close
    	//         n  = Full name
    	urlString += "&f=sl1kjdpn";
		return urlString;
	}
	
	

	private void UpdateDateStringsInDB() {
		
		Date now = new Date();

		SimpleDateFormat sdf = new SimpleDateFormat(Constants.UPDATE_DATE_FORMAT, Locale.US);
		SimpleDateFormat stf = new SimpleDateFormat(Constants.UPDATE_TIME_FORMAT, Locale.US);
		
		String dateString = sdf.format(now);
		String timeString = stf.format(now);		
		
		DbAdapter dbAdapter = new DbAdapter(this);
		dbAdapter.open();
		dbAdapter.removeLastUpdateRecord();
		dbAdapter.createLastUpdateRecord(dateString, timeString);
		dbAdapter.close();
		
	}


	@Override
	public void quoteAddedOrMoved() {
		for (int i = 0; i < mPagerAdapter.getCount(); i++) {
		    ListFragmentBase fragment = mPagerAdapter.getItem(i);
	        fragment.redisplayList();
		}
		
	}


	@Override
	public void moveToOwned(Intent data) {
		mPagerAdapter.moveToOwned(data);
		mViewPager.setCurrentItem(0);
	}
	
	public  List<StockQuote> mQuoteList = new ArrayList<StockQuote>();

	public void refreshButtonClicked(View view) {
	}

	public void addButtonClicked(View view) {
	}


	private class DoNetworkTask extends AsyncTask<String, Integer, String> {
		
		private List<String> mInvalidSymbolList;
        private Context mContext;
		
		
		public DoNetworkTask(Context context) {
			mContext = context;
			mInvalidSymbolList = new ArrayList<String>();
		}
		
		// Potentially long running task here.
		@Override
		protected String doInBackground(String... urls) {
			
			//android.os.Debug.waitForDebugger();
			
			String response = "";
			String theURLString = urls[0];
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(theURLString);
			try {
				HttpResponse execute = client.execute(httpGet);
				InputStream content = execute.getEntity().getContent();
				BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
				String s;
				StockQuote q;
				while ((s = buffer.readLine()) != null){
					q = parseLine(s);
					mQuoteList.add(q);
					//parseLine(s);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
            handleInvalidSymbols(mInvalidSymbolList);
    		for (int i = 0; i < mPagerAdapter.getCount(); i++) {
    		    ListFragmentBase fragment = mPagerAdapter.getItem(i);
    	        fragment.redisplayList();
    		}
    		((MainActivity)mContext).networkTaskCompleted();
			Toast.makeText(MainActivity.this, "Refresh Complete", Toast.LENGTH_LONG).show();
		}
		
	    private void handleInvalidSymbols(List<String> invalidSymbolList) {

	    	 int length = invalidSymbolList.size();
	    	 if (length == 0)  return;
	    	 
	    	 String msg;
	    	 String lastSymbol = invalidSymbolList.get(length-1);
	    	 if (invalidSymbolList.size() == 1)	 {
	    		 msg = "The symbol: " + invalidSymbolList.get(0) + " is invalid.\nDeleting."; 
	    	 } else {
	    		 msg = "The symbols: ";
	    		 for (String s : invalidSymbolList) {
	    			 msg += s;
	    			 if (!s.equals(lastSymbol)) {
	    				 msg += ",";
	    			 }
	    		 }
	    		 msg += " are invalid.\n Deleting.";
	    	 }
	    	 
			 DbAdapter dbAdapter = new DbAdapter(MainActivity.this);
			 dbAdapter.open();

			 for(String symbol : mInvalidSymbolList) {
                 dbAdapter.removeQuoteRecord(symbol);
			 }

			 dbAdapter.close();
	    	 
	 		 AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
	 		 builder.setIcon(android.R.drawable.ic_dialog_alert)
	 		        .setTitle("Invalid Symbol(s)")
	 		        .setMessage(msg)
	 		        .setPositiveButton("OK",null)
	 		        .setCancelable(false)
	 		        .show();
		}
		
		private StockQuote parseLine(String s) {

			String[] fields = s.split(",");
			String symbol = (String)fields[0].subSequence(1, fields[0].length()-1);
			Float pps = parseFloatOrNA(fields[1]);
			Float yrHi = parseFloatOrNA(fields[2]);
			Float yrLo = parseFloatOrNA(fields[3]);
			Float div = parseFloatOrNA(fields[4]);
			Float prevClose = parseFloatOrNA(fields[5]);
			
			// FullName may have "," in it and be split
			String fullName = fields[6];
			int fieldsRead = 7;
			while (fields.length > fieldsRead)
			{
				fullName += fields[fieldsRead];
				fieldsRead++;
			}
			
			DbAdapter dbAdapter = new DbAdapter(MainActivity.this);
			dbAdapter.open();
			long id = dbAdapter.fetchQuoteIdFromSymbol(symbol);
			StockQuote quote = dbAdapter.fetchQuoteObjectFromId(id);
			quote.mPPS = pps;
			quote.mYrMax = yrHi;
			quote.mYrMin = yrLo;
			quote.mDivPerShare = div;
			quote.mFullName = fullName;
			
			if (isSymbolValid(quote, prevClose)) {
				for (BuyBlock block : quote.mBuyBlockList) {
					block.mEffDivYield = div/block.mBuyPPS * 100.0f;
				}
				
				quote.compute(prevClose);
				quote.determineOverallAccountColor();
				dbAdapter.changeQuoteRecord(id, quote);
			} else {
				mInvalidSymbolList.add(quote.mSymbol);
			}
			
			dbAdapter.close();
			return quote;
		}
		
	    private float parseFloatOrNA(String field) {
	    	float parsedFloat = 0.0f;
	    	if (!field.contains("N/A")) {
	    		parsedFloat = Float.valueOf(field);
	    	}
	    	return parsedFloat;
	    }
		
	    private boolean isSymbolValid(StockQuote quote, float prevClose) {
			return !((quote.mPPS < Constants.MINIMUM_SIGNIFICANT_VALUE) &&
					(quote.mDivPerShare < Constants.MINIMUM_SIGNIFICANT_VALUE) &&
					(quote.mYrMin < Constants.MINIMUM_SIGNIFICANT_VALUE) &&
					(quote.mYrMax < Constants.MINIMUM_SIGNIFICANT_VALUE) &&
					(prevClose < Constants.MINIMUM_SIGNIFICANT_VALUE));

		}
	}
	
	


}
