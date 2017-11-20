package com.adaskin.android.stockwatcher4;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

import com.adaskin.android.stockwatcher4.javamail.Mail;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import static android.util.Log.DEBUG;


public class MainActivity extends ActionBarActivity
            implements ActionBar.TabListener, ToolbarListener, ListFragmentListener{

	private ActionBar mActionBar;
	private SectionsPagerAdapter mPagerAdapter;
	private NonSwipableViewPager mViewPager;
	private List<String> mInvalidSymbolList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mActionBar = getSupportActionBar(); 
		
		setUpActionBar(mActionBar);
		setUpPager(mActionBar);

		mInvalidSymbolList = new ArrayList<String>();
		Log.d("myTag", "I'm alive.");
	}

	private void setUpActionBar(ActionBar actionBar)
	{
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//actionBar.setDisplayShowTitleEnabled(true);

		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View customTitleView = inflater.inflate(R.layout.custom_main_titlebar, null);
		TextView tv = (TextView) customTitleView.findViewById(R.id.custom_title);
		tv.setText(getString(R.string.app_name));
		actionBar.setCustomView(customTitleView);
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
		Log.d("myTag", "Start refreshQuotes()");
		DbAdapter dbAdapter = new DbAdapter(this);
		dbAdapter.open();

		List<StockQuote> quoteList = dbAdapter.fetchStockQuoteList();
		dbAdapter.close();

		DoNetworkTask task = new DoNetworkTask(this);
		task.execute(quoteList);

		Log.d("myTag", "Finish refreshQuotes()");
	}

	private void handleInvalidSymbols(List<String> invalidSymbolList, List<StockQuote> updatedQuoteList) {

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

		DbAdapter dbAdapter = new DbAdapter(this);
		dbAdapter.open();

		for(String symbol : mInvalidSymbolList) {
			StockQuote badQuote = dbAdapter.fetchQuoteObjectFromSymbol(symbol);
			updatedQuoteList.remove(badQuote);
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

    private void networkTaskCompleted(List<StockQuote> updatedQuoteList)
    {
		Log.d("myTag", "Starting networkTaskCompleted()");
		handleInvalidSymbols(mInvalidSymbolList, updatedQuoteList);

        UpdateQuotesInDB(updatedQuoteList);
		UpdateDateStringsInDB();
		for (int i = 0; i < mPagerAdapter.getCount(); i++) {
			ListFragmentBase fragment = mPagerAdapter.getItem(i);
			fragment.redisplayList();
		}

        ToolbarFragment toolbarFragment = (ToolbarFragment)getSupportFragmentManager().findFragmentById(R.id.footer_fragment);

    	toolbarFragment.endButtonAnimation();
        toolbarFragment.updateLastUpdateStrings();
    }

//  Method used with Yahoo Finance < 01 Nov 2017
//	// Development Note 06Dec13:   At first I tried to put the suffix characters in their own
//	//    variable and then increment the urlString with that variable.  This left out the
//	//    "&" character.
//	private String formURLString(List<String> symbols){
//		String urlString = "http://finance.yahoo.com/d/quotes.csv?s=";
//		for (String symbol: symbols) {
//		    urlString += symbol + "+";
//		}
//
//		// Replace last "+" with "&"
//		int lastIdx = urlString.lastIndexOf("+");
//		urlString = (String)urlString.subSequence(0, lastIdx);
//
//    	// Flags:  s  = symbol
//    	//         l1 = last trade price
//    	//         k  = 52 week high
//    	//         j  = 52 week low
//    	//         d  = Dividend/share
//    	//         p  = Previous close
//    	//         n  = Full name
//    	urlString += "&f=sl1kjdpn";
//		return urlString;
//	}
	private void UpdateQuotesInDB(List<StockQuote> updatedQuoteList) {
		DbAdapter dbAdapter = new DbAdapter(this);
		dbAdapter.open();
		for (StockQuote quote: updatedQuoteList) {
			dbAdapter.changeQuoteRecord(dbAdapter.fetchQuoteIdFromSymbol(quote.mSymbol), quote);
		}
		dbAdapter.close();
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

	@SuppressWarnings("UnusedParameters")
	public void ExportCommand(MenuItem item) {
		DbAdapter dbAdapter = new DbAdapter(this);
		dbAdapter.open();
		boolean isSuccessful = dbAdapter.exportDB();
		dbAdapter.close();
        sendEmail();

		CharSequence msg = "Database exported";
		if (!isSuccessful)
			msg = "Export error.";
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	private void sendEmail()
    {
        String attachmentFileName = Environment.getExternalStorageDirectory() + "/stockwatcher4_backup.db";
		String message = Build.MODEL + "  "  + Build.SERIAL;
        String[] emailParameters = new String[] {attachmentFileName, message};
        new SendEmailTask().execute(emailParameters);
    }

    private class SendEmailTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... inputStrings) {
            String senderUsername = "executive@adaskin.com";
            String senderPassword = "~.^voA^ZVsMD";
            Mail m = new Mail(senderUsername, senderPassword);
            String[] recipientArray = {"dave@adaskin.com"};
            m.sendTo(recipientArray);
            m.sendFrom(senderUsername);
            m.setSubject("Stockwatcher4 DB update from: " + inputStrings[1]);
            m.setBody("<See attachment>");

            String result = "Email: ";
            try {
                 m.addAttachment(inputStrings[0]);
                if (m.send()) {
                    result += "sent";
                } else {
                    result += "not sent";
                }
            } catch (Exception e) {
                result +=  e.toString();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
        }
    }

    @SuppressWarnings("UnusedParameters")
	public void ImportCommand(MenuItem item) {
		DbAdapter dbAdapter = new DbAdapter(this);
		dbAdapter.open();
		boolean isSuccessful = dbAdapter.importDB();
		dbAdapter.close();

		CharSequence msg = "Database imported";
		if (!isSuccessful)
			msg = "Import error.";
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

		if (isSuccessful)
		    quoteAddedOrMoved();
	}

    private class DoNetworkTask extends AsyncTask<List<StockQuote>, Integer, List<StockQuote>> {
		
        private final Context mContext;

		public DoNetworkTask(Context context) {
			mContext = context;
		}
		
		// Potentially long running task here.
//		@Override
//		protected String doInBackground(String... urls) {
//			String response = "";
//			String theURLString = urls[0];
//			DefaultHttpClient client = new DefaultHttpClient();
//			HttpGet httpGet = new HttpGet(theURLString);
//			try {
//				HttpResponse execute = client.execute(httpGet);
//				InputStream content = execute.getEntity().getContent();
//				BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
//				String s;
//				while ((s = buffer.readLine()) != null){
//					parseLine(s);
//				}
//			} catch(Exception e) {
//				e.printStackTrace();
//			}
//			return response;
//		}

		private void writeWebResponseToFile(BufferedReader readBuffer, String fileName) {
			String file = Environment.getExternalStorageDirectory() + "/" + fileName;
			String line;

			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				while ((line = readBuffer.readLine()) != null) {
					writer.write(line);
					writer.write("\nxxx\n");
				}
				writer.flush();
				writer.close();
			}catch(IOException e){
				String msg = "Exception in writeWebResponseToFile(): " + e.getMessage();
				Log.d("myTag", msg);
				e.printStackTrace();
			}
		}

		@Override
		protected List<StockQuote> doInBackground(List<StockQuote>... params) {
			String response = "";
			DefaultHttpClient client = new DefaultHttpClient();
			List<StockQuote> quoteList = params[0];

			for (StockQuote quote : quoteList) {
				String url = "https://finance.yahoo.com/quote/" + quote.mSymbol;
				HttpGet httpGet = new HttpGet(url);
				try {
					HttpResponse execute = client.execute(httpGet);
					Log.d("myTag", "Received response for: " + quote.mSymbol);
					InputStream is = execute.getEntity().getContent();
					BufferedReader buffer = new BufferedReader(new InputStreamReader(is));

//				String htmlFileName = Environment.getExternalStorageDirectory() + "/yahoo-page-source-MO.txt";
//                symbol = "MO";
//				BufferedReader buffer = new BufferedReader(new FileReader(htmlFileName));

					//writeWebResponseToFile(buffer, "response_PRWCX.txt");

					boolean isValidSymbol = parseYAHOOResponse(buffer, quote);
					if (!isValidSymbol)
						mInvalidSymbolList.add(quote.mSymbol);
					buffer.close();
					is.close();
				} catch (Exception e) {
					String msg = "Sending/Receiving web request failed:\n" + e.getMessage();
					Log.d("myTag", msg);
					e.printStackTrace();
				}
			}
			return quoteList;
		}
		private String findItemTrimString(String input, String startPattern, int startOffset, String stopPattern, StockQuote quote, int itemNumber) {
			String remainder = input;
			int startIdx = input.indexOf(startPattern);
			if (startIdx != -1) {
				String sub = input.substring(startIdx + startPattern.length() + startOffset);
				int stopIdx = sub.indexOf(stopPattern);
				if (stopIdx != -1)
				{
					String itemString = sub.substring(0, stopIdx);
					remainder = sub.substring(stopIdx + stopPattern.length());
					switch(itemNumber)  {
						case 0: quote.mFullName = itemString.replace("&amp;","&");
							break;
						case 1: quote.mPPS = parseFloatOrNA(itemString);
							break;
						case 2: // Do stuff involving previous value
							float previousClose = parseFloatOrNA(itemString);
							quote.compute(previousClose);
							break;
						case 3: // Parse range string, add pieces to quote
							String yrMinString = itemString.substring(0, itemString.indexOf(" -"));
					        String yrMaxString = itemString.substring(itemString.indexOf("- ")+2, itemString.length());
					        quote.mYrMin = parseFloatOrNA(yrMinString);
					        quote.mYrMax = parseFloatOrNA(yrMaxString);
							break;
						case 4: quote.mDivPerShare = parseFloatOrNA(itemString);
							break;
					}
				}
			}
			return remainder;
		}

		private boolean parseYAHOOResponse(BufferedReader reader, StockQuote quote){
			String symbol = quote.mSymbol;

			String nameStart = "data-reactid=\"7\">" + symbol + " - ";
			String nameStop = "</h1>";

			String badSymbolPattern = "<title>Symbol Lookup from Yahoo! Finance";

			String ppsStart = "<!-- react-text: 15 -->";
			String ppsStop = "<!--";

			String divStart = "DIVIDEND_AND_YIELD-value";
			int divStartOffset = 20;
			String divStop = " (";

			String rangeStart = "FIFTY_TWO_WK_RANGE-value";
			int rangeStartOffset = 20;
			String rangeStop = "</td>";

			String line;
			String defaultNameString = "barf";
			quote.mFullName = defaultNameString;
			try {
			    while ((line = reader.readLine()) != null) {
					if (line.contains(badSymbolPattern)){
						Log.d("myTag", symbol + " is a bad symbol. *****");
						return false;
					}

					String remainder = findItemTrimString(line, nameStart, 0, nameStop, quote, 0);
					remainder = findItemTrimString(remainder, ppsStart, 0, ppsStop, quote, 1);
					remainder = findItemTrimString(remainder, ppsStart, 0, ppsStop, quote, 2);
					remainder = findItemTrimString(remainder, rangeStart, rangeStartOffset, rangeStop, quote, 3);
					findItemTrimString(remainder, divStart, divStartOffset, divStop, quote, 4);

					if(!(quote.mFullName.equals(defaultNameString)))
						break;
			    }
    			for (BuyBlock block : quote.mBuyBlockList) {
					block.mEffDivYield = quote.mDivPerShare/block.mBuyPPS * 100.0f;
				}
				quote.determineOverallAccountColor();

				String msg = quote.mSymbol + ":\t" + quote.mPPS + "\t" + quote.mPctChangeSinceLastClose + "\t" + quote.mDivPerShare + "\t" + quote.mYrMin + "-" + quote.mYrMax + "\t" + quote.mFullName;
				Log.d("myTag", msg);
			} catch (IOException e) {
				String msg = "Reading line from BufferedReader failed:\n" + e.getMessage();
				Log.d("myTag", msg);
				e.printStackTrace();
			}
			return true;
		}

		@Override
		protected void onPostExecute(List<StockQuote> updatedQuoteList) {
//            handleInvalidSymbols(mInvalidSymbolList);
//    		for (int i = 0; i < mPagerAdapter.getCount(); i++) {
//    		    ListFragmentBase fragment = mPagerAdapter.getItem(i);
//    	        fragment.redisplayList();
//    		}
    		((MainActivity)mContext).networkTaskCompleted(updatedQuoteList);
			Toast.makeText(MainActivity.this, "Refresh Complete", Toast.LENGTH_LONG).show();
		}
		

//		private void parseLine(String s) {
//
//			Log.d("myTag", s);
//
//     		String[] fields = s.split(",");
//			String symbol = (String)fields[0].subSequence(1, fields[0].length()-1);
//			Float pps = parseFloatOrNA(fields[1]);
//			Float yrHi = parseFloatOrNA(fields[2]);
//			Float yrLo = parseFloatOrNA(fields[3]);
//			Float div = parseFloatOrNA(fields[4]);
//			Float prevClose = parseFloatOrNA(fields[5]);
//
//			// FullName may have "," in it and be split
//			String fullName = fields[6];
//			int fieldsRead = 7;
//			while (fields.length > fieldsRead)
//			{
//				fullName += fields[fieldsRead];
//				fieldsRead++;
//			}
//
//			DbAdapter dbAdapter = new DbAdapter(MainActivity.this);
//			dbAdapter.open();
//			long id = dbAdapter.fetchQuoteIdFromSymbol(symbol);
//			StockQuote quote = dbAdapter.fetchQuoteObjectFromId(id);
//			quote.mPPS = pps;
//			quote.mYrMax = yrHi;
//			quote.mYrMin = yrLo;
//			quote.mDivPerShare = div;
//			quote.mFullName = fullName;
//
//			if (isSymbolValid(quote, prevClose)) {
//				for (BuyBlock block : quote.mBuyBlockList) {
//					block.mEffDivYield = div/block.mBuyPPS * 100.0f;
//				}
//
//				quote.compute(prevClose);
//				quote.determineOverallAccountColor();
//				dbAdapter.changeQuoteRecord(id, quote);
//			} else {
//				mInvalidSymbolList.add(quote.mSymbol);
//			}
//
//			dbAdapter.close();
//		}
		
	    private float parseFloatOrNA(String field) {
	    	float parsedFloat = 0.0f;
	    	if (!field.contains("N/A")) {
	    		parsedFloat = Float.valueOf(field);
	    	}
	    	return parsedFloat;
	    }
		
//	    private boolean isSymbolValid(StockQuote quote, float prevClose) {
//			return !((quote.mPPS < Constants.MINIMUM_SIGNIFICANT_VALUE) &&
//					(quote.mDivPerShare < Constants.MINIMUM_SIGNIFICANT_VALUE) &&
//					(quote.mYrMin < Constants.MINIMUM_SIGNIFICANT_VALUE) &&
//					(quote.mYrMax < Constants.MINIMUM_SIGNIFICANT_VALUE) &&
//					(prevClose < Constants.MINIMUM_SIGNIFICANT_VALUE));
//
//		}
	}
	
	


}
