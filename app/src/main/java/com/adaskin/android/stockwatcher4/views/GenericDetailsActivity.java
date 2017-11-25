package com.adaskin.android.stockwatcher4.views;

import com.adaskin.android.stockwatcher4.R;
import com.adaskin.android.stockwatcher4.database.DbAdapter;
import com.adaskin.android.stockwatcher4.models.StockQuote;
import com.adaskin.android.stockwatcher4.utilities.Parsers;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
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

public abstract class GenericDetailsActivity extends ActionBarActivity {

	View mRefreshButtonView;
	StockQuote mQuote;

	public GenericDetailsActivity() {
		super();
	}

	void setTitleString(String symbol) {
		ActionBar actionBar = this.getSupportActionBar();
	
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View customTitleView = inflater.inflate(R.layout.custom_titlebar, null);
		TextView tv = (TextView)customTitleView.findViewById(R.id.custom_title);
		tv.setText(getString(R.string.app_name) + ": " + symbol + getString(R.string.detail) );
		actionBar.setCustomView(customTitleView);
	}

	public void detailRefreshButtonClicked(View v) {
		Toast.makeText(this, "Refresh this Watch item!", Toast.LENGTH_LONG).show();

		mRefreshButtonView = v;
		String msg = mQuote.mPPS + "\t" + mQuote.mDivPerShare + "\t" + mQuote.mAnalystsOpinion;
		Log.d("myTag", msg);

		Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_1sec_center);
		animation.setRepeatCount(Animation.INFINITE);
		mRefreshButtonView.startAnimation(animation);
		SingleSymbolUpdateTask updateTask = new SingleSymbolUpdateTask();
		updateTask.execute(mQuote);
	}

	protected void updateQuoteInDB(StockQuote updatedQuote) {
		DbAdapter dbAdapter = new DbAdapter(this);
		dbAdapter.open();
		dbAdapter.changeQuoteRecord(dbAdapter.fetchQuoteIdFromSymbol(updatedQuote.mSymbol), updatedQuote);
		dbAdapter.close();
	}

	protected abstract void singleSymbolUpdateCompleted(StockQuote updatedQuote);

	private class SingleSymbolUpdateTask extends AsyncTask<StockQuote, Integer, StockQuote> {

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
			mRefreshButtonView.clearAnimation();
			singleSymbolUpdateCompleted(updatedQuote);
		}
	}
}