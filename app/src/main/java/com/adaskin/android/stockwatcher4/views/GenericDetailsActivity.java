package com.adaskin.android.stockwatcher4.views;

import com.adaskin.android.stockwatcher4.R;
import com.adaskin.android.stockwatcher4.models.StockQuote;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class GenericDetailsActivity extends ActionBarActivity {

	
	StockQuote mQuote;
	String mSymbol;

	public GenericDetailsActivity() {
		super();
	}

//	private void receiveExtraInformation(StockQuoteExtra extras) {
//
//		if (extras.mSymbol.equals(mQuote.mSymbol)) {
//
//		    DbAdapter dbAdapter = new DbAdapter(this);
//		    dbAdapter.open();
//
//		    // Update the quote from the DB here to obtain any other changed information.
//		    // (Resulted from defect where deleted block would come back on subsequent
//		    // switches to owned details view.)
//		    long id = dbAdapter.fetchQuoteIdFromSymbol(mQuote.mSymbol);
//		    StockQuote q = dbAdapter.fetchQuoteObjectFromId(id);
//
//		    // Update the model with the extra information
//		//  q.mFullName = handleSpecialCharacters(extras.mCompleteFullName);
//		    if (!extras.mAnalystOpinion.contains("N/A"))
//		    {
//		    	try {
//					q.mAnalystsOpinion = Float.parseFloat(extras.mAnalystOpinion);
//		    	} catch (NumberFormatException e) 	{
//		    		q.mAnalystsOpinion = this.getPlaceholderAnalystsOpinion();
//		    	}
//		    }
//
//		    // Update DB.
//		    dbAdapter.removeQuoteRecord(q.mSymbol);
//		    dbAdapter.createQuoteRecord(q);
//		    dbAdapter.close();
//
//		    mQuote = q;
//		    updateDisplayWithExtraInfo();
//		}
//	}
	
//	// For a list of HTML special characters: www.w3schools.com/tags/ref_entities.asp
//	private String handleSpecialCharacters(String inputString) {
//
//		if (inputString.length() < 1)
//			return "";
//
//		return inputString.replaceAll("&amp;","&");
//	}
	
//	 void updateDisplayWithExtraInfo() {
//		// Override this in the subclass
//	}
	
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

//	@SuppressWarnings("SameReturnValue")
//	private float getPlaceholderAnalystsOpinion() {
//		return 9.9f;
//	}

	
//    // Initiates asynchronous task to get analysts opinion.
//	// When completed successfully an event will be raised that will be handled by the
//	// subclasses to put the new information into the quote and update the view.
//	void requestExtraQuoteInformation(String symbol) {
//
//    	//String urlString = "http://finance.yahoo.com/q/ao?s="  + symbol +"+Analyst+Opinion";
//
//    	GetExtraInformationTask extraInfoTask = new GetExtraInformationTask();
//    	extraInfoTask.execute(symbol);
//	}
	
	
//	private class GetExtraInformationTask extends AsyncTask<String, Integer, StockQuoteExtra> {
//
//		private void writeExtraWebResponseToFile(BufferedReader readBuffer, String fileName) {
//			Log.d("myTag", "Writing extra info web response to file.");
//			String file = Environment.getExternalStorageDirectory() + "/" + fileName;
//			String line;
//
//			try {
//				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
//				while ((line = readBuffer.readLine()) != null) {
//					writer.write(line);
//					writer.write("\nxxx\n");
//				}
//				writer.flush();
//				writer.close();
//			} catch (IOException e){
//				String msg = "Exception in writeExtraWebResponseToFile(): " + e.getMessage();
//				Log.d("myTag", msg);
//				e.printStackTrace();
//			}
//		}
//
//		@Override
//		protected StockQuoteExtra doInBackground(String... params) {
//			String symbol = params[0];
//			String theURLString = "http://finance.yahoo.com/q/ao?s="  + symbol +"+Analyst+Opinion";
//			StockQuoteExtra qx = new StockQuoteExtra();
//			qx.mSymbol = symbol;
//
//			String opinionStart = "\"recommendationMean\":{\"raw\":";
//			String opinionStop = ",\"fmt\"";
//
//			DefaultHttpClient client = new DefaultHttpClient();
//			HttpGet httpGet = new HttpGet(theURLString);
//			try {
//				HttpResponse execute = client.execute(httpGet);
//				InputStream content = execute.getEntity().getContent();
//				BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
//
//                //writeExtraWebResponseToFile(buffer, "responseExtra_LSRCX.txt");
//
//				String line;
//				while ((line = buffer.readLine()) != null) {
//					int idxStart = line.indexOf(opinionStart);
//					if (idxStart != -1) {
//						String sub = line.substring(idxStart+opinionStart.length());
//						int idxStop = sub.indexOf(opinionStop);
//						if (idxStop != -1) {
//							qx.mAnalystOpinion = sub.substring(0, idxStop);
//						}
//					}
//				}
//			} catch(Exception e) {
//				e.printStackTrace();
//			}
//
//			return qx;
//		}
//
//		@Override
//		protected void onPostExecute(StockQuoteExtra result) {
//			if (result.mAnalystOpinion.isEmpty())
//			    result.mAnalystOpinion = "N/A";
//
//            Toast.makeText(GenericDetailsActivity.this, "", Toast.LENGTH_SHORT).show();
//            receiveExtraInformation(result);
//
//			super.onPostExecute(result);
//		}
//
//	}  // End of private class
//
	
}