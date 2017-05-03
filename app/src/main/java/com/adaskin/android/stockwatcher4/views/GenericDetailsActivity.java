package com.adaskin.android.stockwatcher4.views;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.adaskin.android.stockwatcher4.R;
import com.adaskin.android.stockwatcher4.database.DbAdapter;
import com.adaskin.android.stockwatcher4.models.StockQuote;
import com.adaskin.android.stockwatcher4.models.StockQuoteExtra;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class GenericDetailsActivity extends ActionBarActivity {

	
	StockQuote mQuote;
	String mSymbol;

	public GenericDetailsActivity() {
		super();
	}

	private void receiveExtraInformation(StockQuoteExtra extras) {

		if (extras.mSymbol.equals(mQuote.mSymbol)) {
			
		    DbAdapter dbAdapter = new DbAdapter(this);
		    dbAdapter.open();

		    // Update the quote from the DB here to obtain any other changed information.
		    // (Resulted from defect where deleted block would come back on subsequent
		    // switches to owned details view.)
		    long id = dbAdapter.fetchQuoteIdFromSymbol(mQuote.mSymbol);
		    StockQuote q = dbAdapter.fetchQuoteObjectFromId(id);
		    
		    // Update the model with the extra information
		    q.mFullName = handleSpecialCharacters(extras.mCompleteFullName);
		    if (!extras.mAnalystOpinion.contains("N/A"))
		    {
		    	try {
					q.mAnalystsOpinion = Float.parseFloat(extras.mAnalystOpinion);
		    	} catch (NumberFormatException e) 	{
		    		q.mAnalystsOpinion = this.getPlaceholderAnalystsOpinion();
		    	}
		    }
		    
		    // Update DB.
		    dbAdapter.removeQuoteRecord(q.mSymbol);
		    dbAdapter.createQuoteRecord(q);
		    dbAdapter.close();
		    
		    mQuote = q;
		    updateDisplayWithExtraInfo();
		}
	}
	
	// For a list of HTML special characters: www.w3schools.com/tags/ref_entities.asp
	private String handleSpecialCharacters(String inputString) {
		
		if (inputString.length() < 1)
			return "";

		return inputString.replaceAll("&amp;","&");
	}
	
	void updateDisplayWithExtraInfo() {
		// Override this in the subclass
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

	@SuppressWarnings("SameReturnValue")
	private float getPlaceholderAnalystsOpinion() {
		return 9.9f;
	}

	
    // Initiates asynchronous task to get complete full name and analysts opinion.
	// When completed successfully an event will be raised that will be handled by the
	// subclasses to put the new information into the quote and update the view.
	void requestExtraQuoteInformation(String symbol) {
	
    	String urlString = "http://finance.yahoo.com/q/ao?s="  + symbol +"+Analyst+Opinion";
  	   
    	GetExtraInformationTask extraInfoTask = new GetExtraInformationTask();
    	extraInfoTask.execute(urlString);
	}
	
	
	private class GetExtraInformationTask extends AsyncTask<String, Integer, StockQuoteExtra> {

		@Override
		protected StockQuoteExtra doInBackground(String... params) {
			StockQuoteExtra qx = new StockQuoteExtra();
			String matchString = "<div class=\"title\"><h2>";
			boolean haveValues1 = false;
			boolean haveValues2 = false;
			
			String theURLString = params[0];
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(theURLString);
			try {
				HttpResponse execute = client.execute(httpGet);
				InputStream content = execute.getEntity().getContent();
				BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
				String s;

				while ((s = buffer.readLine()) != null) {
					int idxA = s.indexOf(matchString);
					if (idxA > 0) {
						String t = s.substring(idxA+matchString.length(), s.length());
						int idxB = t.indexOf("(");
						int idxC = t.indexOf(")");
						if ((idxB > 0)||(idxC > 0)){
							qx.mCompleteFullName = t.substring(0, idxB);
							qx.mSymbol = t.substring(idxB+1, idxC);
							haveValues1 = true;
						}
					}
						
					int idx1 = s.indexOf("Mean Recommendation (this week):");
					if (idx1 > 0) {
						qx.mAnalystOpinion = s.substring(idx1+65, idx1+68);
						haveValues2 = true;
					}
					
					if (haveValues1 && haveValues2)
						break;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			return qx;
		}

		@Override
		protected void onPostExecute(StockQuoteExtra result) {
			if (result.mAnalystOpinion.isEmpty())
			    result.mAnalystOpinion = "N/A";
			
            Toast.makeText(GenericDetailsActivity.this, "", Toast.LENGTH_SHORT).show();
            receiveExtraInformation(result);
            
			super.onPostExecute(result);
		}
		
	}  // End of private class
	
	
}