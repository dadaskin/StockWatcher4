package com.adaskin.android.stockwatcher4.views;

import com.adaskin.android.stockwatcher4.R;
import com.adaskin.android.stockwatcher4.database.DbAdapter;
import com.adaskin.android.stockwatcher4.fragments.ListFragmentBase;
import com.adaskin.android.stockwatcher4.models.StockQuote;
import com.adaskin.android.stockwatcher4.utilities.Constants;
import com.adaskin.android.stockwatcher4.utilities.Parsers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;


public class WatchDetailsActivity extends GenericDetailsActivity {

	//private boolean mIsButtonRotating;
	private View mRefreshButtonView;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_details);

		//mIsButtonRotating = false;
        mSymbol = getIntent().getExtras().getString(Constants.SYMBOL_BUNDLE_KEY);
        setTitleString();
       
        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        
        long id = dbAdapter.fetchQuoteIdFromSymbol(mSymbol);
        mQuote = dbAdapter.fetchQuoteObjectFromId(id);
        dbAdapter.close();
        
        fillData();
    }
    
	private void fillData() {
    	TextView nameField = (TextView)findViewById(R.id.watch_full_name_field);
    	TextView ppsField = (TextView)findViewById(R.id.watch_pps_field);
    	TextView divPSField = (TextView)findViewById(R.id.watch_divps_field);
    	TextView analOpField = (TextView)findViewById(R.id.watch_anal_op_field);
    	TextView yrMinField = (TextView)findViewById(R.id.watch_yr_min_field);
    	TextView yrMaxField = (TextView)findViewById(R.id.watch_yr_max_field);
    	TextView strikeField = (TextView)findViewById(R.id.watch_strike_price_field);
    	
    	nameField.setText(mQuote.mFullName);
		ppsField.setText(String.format(Locale.US,Constants.CURRENCY_FORMAT, mQuote.mPPS));
		String divPSAndYieldMsg = String.format(Locale.US,Constants.CURRENCY_FORMAT, mQuote.mDivPerShare) +
                "  (" +
                String.format(Locale.US,Constants.PERCENTAGE_FORMAT, mQuote.mDivPerShare*100f/mQuote.mPPS) +
                ")";
        divPSField.setText(divPSAndYieldMsg);
        
        analOpField.setText(String.format(Locale.US,Constants.OPINION_FORMAT, mQuote.mAnalystsOpinion));
        yrMinField.setText(String.format(Locale.US,Constants.CURRENCY_FORMAT, mQuote.mYrMin));
        yrMaxField.setText(String.format(Locale.US,Constants.CURRENCY_FORMAT, mQuote.mYrMax));
		strikeField.setText(String.format(Locale.US,Constants.CURRENCY_FORMAT, mQuote.mStrikePrice));
    }

    @SuppressWarnings("UnusedParameters")
    public void changeButtonClicked(View v) {
    	Intent intent = new Intent(this, ChangeParameterActivity.class);
    	intent.putExtra(Constants.SYMBOL_BUNDLE_KEY, mQuote.mSymbol);
    	intent.putExtra(Constants.PARAM_NAME_BUNDLE_KEY, "Strike Price");
    	intent.putExtra(Constants.OLD_VALUE_BUNDLE_KEY, mQuote.mStrikePrice);
    	startActivityForResult(intent, Constants.PARAMETER_CHANGE_ACTIVITY);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == Activity.RESULT_OK) {
			DbAdapter dbAdapter = new DbAdapter(this);
			dbAdapter.open();
			dbAdapter.removeQuoteRecord(mQuote.mSymbol);
			mQuote.mStrikePrice = intent.getFloatExtra(Constants.PARAM_NEW_VALUE_BUNDLE_KEY, 0.0f);
			dbAdapter.createQuoteRecord(mQuote);
			dbAdapter.close();
			fillData();
		}
	}

	public void watchRefreshButtonClicked(View v) {
		Toast.makeText(this, "Refresh this Watch item!", Toast.LENGTH_LONG).show();

		// Replace this with call to refresh the single quote
//		if (mIsButtonRotating) {
//			v.clearAnimation();
//			mIsButtonRotating = false;
//		} else {
//			Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_1sec_center);
//			animation.setRepeatCount(Animation.INFINITE);
//			v.startAnimation(animation);
//			mIsButtonRotating = true;
//		}

		mRefreshButtonView = v;
		String msg = mQuote.mPPS + "\t" + mQuote.mDivPerShare + "\t" + mQuote.mAnalystsOpinion;
		Log.d("myTag", msg);

		Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_1sec_center);
		animation.setRepeatCount(Animation.INFINITE);
		mRefreshButtonView.startAnimation(animation);
		SingleSymbolUpdateTask updateTask = new SingleSymbolUpdateTask(this);
		updateTask.execute(mQuote);
	}

	private void singleSymbolUpdateCompleted(StockQuote updatedQuote) {
		String msg = mQuote.mPPS + "\t" + mQuote.mDivPerShare + "\t" + mQuote.mAnalystsOpinion;
		Log.d("myTag", msg);

		updateQuoteInDB(updatedQuote);
		fillData();
		mRefreshButtonView.clearAnimation();
	}

	private void updateQuoteInDB(StockQuote updatedQuote) {
		DbAdapter dbAdapter = new DbAdapter(this);
		dbAdapter.open();
		dbAdapter.changeQuoteRecord(dbAdapter.fetchQuoteIdFromSymbol(updatedQuote.mSymbol), updatedQuote);
		dbAdapter.close();
	}

	private class SingleSymbolUpdateTask extends AsyncTask<StockQuote, Integer, StockQuote>  {

		private final Context mContext;

		SingleSymbolUpdateTask(Context context) {
			mContext = context;
		}

		@Override
		protected StockQuote doInBackground(StockQuote... params) {
			DefaultHttpClient client = new DefaultHttpClient();
			StockQuote quote = params[0];

			String url = "https://finance.yahoo.com/quote/" + quote.mSymbol;
			HttpGet httpGet = new HttpGet(url);
			try {
				HttpResponse response = client.execute(httpGet);
				Log.d("myTag", "Received response for: "+ quote.mSymbol);
				InputStream is = response.getEntity().getContent();
				BufferedReader buffer = new BufferedReader(new InputStreamReader(is));

				boolean isValidSymbol = Parsers.parseYAHOOResponse(buffer, quote);
				if (!isValidSymbol) {
					String msg = "Symbol: " + quote.mSymbol + " is not valid.";
					Log.d("myTag", msg);
				}
				buffer.close();
				is.close();
			} catch (Exception e) {
				String msg = "Sending/Receiving web request failed:\n" + e.getMessage();
				Log.d("myTag", msg);
				e.printStackTrace();
			}

			return quote;
		}

		@Override
		protected void onPostExecute(StockQuote updatedQuote) {
			((WatchDetailsActivity)mContext).singleSymbolUpdateCompleted(updatedQuote);
		}
	}
}
