package com.adaskin.android.stockwatcher4.views;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.adaskin.android.stockwatcher4.R;
import com.adaskin.android.stockwatcher4.database.DbAdapter;
import com.adaskin.android.stockwatcher4.models.BuyBlock;
import com.adaskin.android.stockwatcher4.utilities.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OwnedAddActivity extends ActionBarActivity {

	private BuyBlock mFirstBuyBlock;
	private EditText mSymbolField;
	private EditText mGainTargetField;
	private boolean mFromOwned = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.owned_add);

		setTitleString();

		mGainTargetField = (EditText) findViewById(R.id.owned_add_gain_target);
		mSymbolField = (EditText) findViewById(R.id.owned_add_symbol);
		String symbol = getIntent().getStringExtra(Constants.BUY_BLOCK_SYMBOL_KEY);
		if (symbol != null) {
			mFromOwned = false;
			mSymbolField.setText(symbol);
		}
	}

	private void setTitleString() {
		ActionBar actionBar = this.getSupportActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);

		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View customTitleView = inflater.inflate(R.layout.custom_titlebar, null);
		TextView tv = (TextView) customTitleView.findViewById(R.id.custom_title);
		tv.setText(String.format(Locale.US, "%s + %s", getString(R.string.app_name), ": Add to Owned List"));
		actionBar.setCustomView(customTitleView);
	}

	@Override
	public void onBackPressed() {
		Intent returnIntent = new Intent();
		setResult(Activity.RESULT_CANCELED, returnIntent);
		finish();
	}

	@SuppressWarnings("UnusedParameters")
	public void addBlockButtonClicked(View v) {
		String symbol = mSymbolField.getText().toString();

		if (mFromOwned) {
			DbAdapter dbAdapter = new DbAdapter(this);
			dbAdapter.open();
			long existingId = dbAdapter.fetchQuoteIdFromSymbol(symbol);
			dbAdapter.close();
			if (existingId != -1) {
				createAlertDuplicateDialog(symbol);
				return;
			}
		}

		if (inputsAreValid()) {
			Intent intent = new Intent(this, BuyBlockAddActivity.class);
			intent.putExtra(Constants.BUY_BLOCK_SYMBOL_KEY, symbol);
			startActivityForResult(intent, Constants.BUY_BLOCK_ADD_ACTIVITY);
		} else {
			Toast.makeText(this, Constants.EMPTY_FIELD_ERR_MSG, Toast.LENGTH_LONG).show();
		}
	}

	private boolean inputsAreValid() {
		String symbol = mSymbolField.getText().toString();
		if (symbol.isEmpty()) {
			return false;
		}

		try {
			// Here we just want to know if the text is a double, we don't really care what
			// the actual value is.   C# would do this with a Double.tryParse() method, but
			// Java doesn't have anything like that.
			//noinspection ResultOfMethodCallIgnored
			Float.parseFloat(mGainTargetField.getText().toString());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private void createAlertDuplicateDialog(String symbol) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Duplicate Symbol")
				.setMessage(createAlertDuplicateMessage(symbol))
				.setPositiveButton("OK", mAlertConfirmListener)
				.setCancelable(false)
				.show();
	}

	private String createAlertDuplicateMessage(String symbol) {
		return "Symbol: " + symbol + " is a Duplicate.\nIgnoring this input.";
	}

	private final DialogInterface.OnClickListener mAlertConfirmListener =
			new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent returnIntent = new Intent();
					setResult(Activity.RESULT_CANCELED, returnIntent);
					finish();
				}
			};

	// Handle return from BuyBlockAdd Activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == Activity.RESULT_OK) {
			Bundle bundle = intent.getExtras();
			mFirstBuyBlock = grabBuyBlockInfo(bundle);
			grabInfoAndReturn();
		}

	}

	private BuyBlock grabBuyBlockInfo(Bundle bundle) {
		String buyDateString = bundle.getString(Constants.BUY_BLOCK_DATE_KEY);
		float buyPrice = bundle.getFloat(Constants.BUY_BLOCK_PRICE_KEY);
		float numShares = bundle.getFloat(Constants.BUY_BLOCK_NUM_KEY);
		int accountColor = bundle.getInt(Constants.BUY_BLOCK_ACCOUNT_COLOR_KEY);
		float commissionPS = Constants.COMMISSION_PER_TRANSACTION;

		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
		Date buyDate = new Date();
		try {
			buyDate = sdf.parse(buyDateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return new BuyBlock(buyDate, numShares, buyPrice, commissionPS, 0.0f, accountColor);
	}

	private void grabInfoAndReturn() {
		EditText symbolField = (EditText) findViewById(R.id.owned_add_symbol);
		EditText GainTargetField = (EditText) findViewById(R.id.owned_add_gain_target);

		String symbol = symbolField.getText().toString();
		float gainTarget = Float.parseFloat(GainTargetField.getText().toString());

		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
		String buyDateString = sdf.format(mFirstBuyBlock.mBuyDate);

		Bundle bundle = new Bundle();
		bundle.putString(Constants.OWNED_ADD_SYMBOL_BUNDLE_KEY, symbol);
		bundle.putFloat(Constants.OWNED_ADD_GAIN_TARGET_BUNDLE_KEY, gainTarget);
		bundle.putString(Constants.BUY_BLOCK_DATE_KEY, buyDateString);
		bundle.putFloat(Constants.BUY_BLOCK_NUM_KEY, mFirstBuyBlock.mNumShares);
		bundle.putFloat(Constants.BUY_BLOCK_PRICE_KEY, mFirstBuyBlock.mBuyPPS);
		bundle.putInt(Constants.BUY_BLOCK_ACCOUNT_COLOR_KEY, mFirstBuyBlock.mAccountColor);
		bundle.putFloat(Constants.BUY_BLOCK_COMMISSION_KEY, mFirstBuyBlock.mBuyCommissionPerShare);

		Intent returnIntent = new Intent();
		returnIntent.putExtras(bundle);
		setResult(Activity.RESULT_OK, returnIntent);
		finish();
	}
}

