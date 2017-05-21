package com.adaskin.android.stockwatcher4.models;

import java.util.ArrayList;
import java.util.List;

import com.adaskin.android.stockwatcher4.utilities.Constants;

final public class AccountModel {

	@SuppressWarnings("unused")
	public static String getNameFromColor(int color) {
		switch(color) {
		case Constants.ACCOUNT_UNKNOWN: return "Unassigned"; 
		case Constants.ACCOUNT_MIXED:   return "Mixed";     
		case Constants.ACCOUNT_ESPP:    return "ESPP"; 
		case Constants.ACCOUNT_IRA:     return "IRA"; 
		case Constants.ACCOUNT_JOINT:   return "Joint"; 
		case Constants.ACCOUNT_ROTH:    return "Roth IRA"; 
		}
		return "Other";
	}
	
	
	// Returns a list of Account names for a BuyBlock (i.e. no "Mixed")
	public static List<CharSequence> getBlockAccountNameList() {
		List<CharSequence> nameList = new ArrayList<CharSequence>();
		nameList.add("Unassigned");
		nameList.add("ESPP");
		nameList.add("IRA");
		nameList.add("Joint");
		nameList.add("RothIRA");
		nameList.add("Other");
		return nameList;
	}
	
	public static List<Integer> getBlockAccountColorList() {
		List<Integer> colorList = new ArrayList<Integer>();
		colorList.add(Constants.ACCOUNT_UNKNOWN);
		colorList.add(Constants.ACCOUNT_ESPP);
		colorList.add(Constants.ACCOUNT_IRA);
		colorList.add(Constants.ACCOUNT_JOINT);
		colorList.add(Constants.ACCOUNT_ROTH);
		colorList.add(Constants.ACCOUNT_DRIP);
		return colorList;
		
	}
	
	// Returns index of a color in the above list.
	// This must stay in sync with the above list.
    public static int getBlockColorIndex(int color) {
    	switch(color)
    	{
    	case Constants.ACCOUNT_UNKNOWN: return 0;
    	case Constants.ACCOUNT_ESPP:    return 1;
    	case Constants.ACCOUNT_IRA:     return 2;
    	case Constants.ACCOUNT_JOINT:   return 3;
    	case Constants.ACCOUNT_ROTH:    return 4;
    	}
    	return 5;
    }
    
    private final String mName;
    private final int mColor;
    
	public AccountModel(String name, int color)  {
		mName = name;
		mColor = color;
	}

	public String getName() { 
		return mName;
	}
	
	public int getColor() {
		return mColor;
	}
}
