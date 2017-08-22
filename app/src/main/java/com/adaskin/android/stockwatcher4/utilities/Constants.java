package com.adaskin.android.stockwatcher4.utilities;

public class Constants {
    public static final float COMMISSION_PER_TRANSACTION = 0.05f;  // Fidelity as of ~ 01 Mar 2017

	public static final String SYMBOL_BUNDLE_KEY = "TheSymbol";
	public static final String WATCH_ADD_SYMBOL_BUNDLE_KEY = "WatchAddSymbolBundleKey";
	public static final String WATCH_ADD_STRIKE_PRICE_BUNDLE_KEY = "WatchAddStrikePriceBundleKey";
	public static final String OWNED_ADD_SYMBOL_BUNDLE_KEY = "OwnedAddSymbolBundleKey";
	public static final String OWNED_ADD_GAIN_TARGET_BUNDLE_KEY = "OwnedAddGainTargetBundleKey";
	public static final String BUY_BLOCK_SYMBOL_KEY = "BuyBlockSymbolKey";
	public static final String BUY_BLOCK_ACCOUNT_COLOR_KEY = "BuyBlockAccountColorKey";
	public static final String BUY_BLOCK_COMMISSION_KEY = "BuyBlockCommissionKey";
	public static final String BUY_BLOCK_DATE_KEY = "BuyBlockDateKey";
	public static final String BUY_BLOCK_PRICE_KEY = "BuyBlockPriceKey";
	public static final String BUY_BLOCK_NUM_KEY = "BuyBlockNumKey";
	public static final String PARAM_NAME_BUNDLE_KEY = "ParameterNameBundleKey";
	public static final String PARAM_NEW_VALUE_BUNDLE_KEY = "ParameterNewValueBundleKey";
	public static final String OLD_VALUE_BUNDLE_KEY = "OldValueBundleKey";
	
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String UPDATE_DATE_FORMAT = "MMM d, yyyy";
	public static final String UPDATE_TIME_FORMAT = "h:mm a z";
	
	public static final String CURRENCY_FORMAT = "$%.2f";   // Specific to USD
	public static final String CURRENCY_FORMAT_INTEGER = "$%.0f";
	public static final String PERCENTAGE_FORMAT = "%1.1f%%";
	public static final String OPINION_FORMAT = "%1.1f";
	public static final String NUM_SHARES_FORMAT = "%1.0f";
	
	public static final int OWNED_ADD_ACTIVITY = 0;
	public static final int WATCH_ADD_ACTIVITY = 1;
	public static final int BUY_BLOCK_ADD_ACTIVITY = 2;
	public static final int PARAMETER_CHANGE_ACTIVITY = 4;
	public static final int OWNED_DETAIL_ACTIVITY=5;
	public static final int WATCH_DETAIL_ACTIVITY=6;
	
	public static final float POSITIVE_ONE_DECIMAL_LIMIT = 0.05f;
	public static final float NEGATIVE_ONE_DECIMAL_LIMIT = -0.049f;
	
	public static final String EMPTY_FIELD_ERR_MSG = "Please enter a value in every field.";
	
	public static final float MINIMUM_SIGNIFICANT_VALUE = 0.005f;
	
	public static final int SYMBOL_VIEW_IN_QUOTE = 1;  // 0-based index of symbol field in quote_row
	public static final int DATE_VIEW_IN_BLOCK = 1;
	public static final int NUM_SHARES_VIEW_IN_BLOCK = 2;
	
	
	public static final int ACCOUNT_UNKNOWN = 0xff777777;  // Light Gray
	public static final int ACCOUNT_MIXED   = 0xffc0c000;  // Yellow
	public static final int ACCOUNT_JOINT   = 0xfff07000;  // Orange
	public static final int ACCOUNT_IRA     = 0xff00c000;  // Light Green
	public static final int ACCOUNT_ROTH    = 0xff007000;  // Dark Green
	public static final int ACCOUNT_ESPP    = 0xffe00000;  // Red
	public static final int ACCOUNT_DRIP    = 0xffe000e0;  // Purple
}
