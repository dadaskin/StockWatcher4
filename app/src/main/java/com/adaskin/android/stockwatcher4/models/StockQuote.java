package com.adaskin.android.stockwatcher4.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;

import com.adaskin.android.stockwatcher4.utilities.Constants;
import com.adaskin.android.stockwatcher4.utilities.Status;

public class StockQuote  implements Parcelable  {
     
	 // -------- Fields -------
	 // Raw Fields
	 public String mSymbol;
	 public String mFullName;
	 public Status mStatus;
	 public float mPPS;
	 public List<BuyBlock> mBuyBlockList;
     public float mPctGainTarget;	 
     public float mYrMax;
     public float mYrMin;
     public float mDivPerShare;
     public float mStrikePrice;
     
     // Filled by special web request
     public float mAnalystsOpinion;
	 
	 // Calculated Fields
	 public float mPctChangeSinceLastClose;
	 public float mPctChangeSinceBuy;  // Determined from mBuyBlockList
     public int mOverallAccountColor;  // Determined from mBuyBlockList
	 
     // -------- Constructors -------- 
	 // Constructor for Owned quote, creates empty BuyBlockList
	 public StockQuote(String symbol,
                       float pps, 
                       float divPS,
                       float pctGainTarget
                       ) {
		mSymbol = symbol;
		mFullName = "Foo Bar Incorporated";
		mStatus = Status.OWNED;
		mPPS = pps;
		mDivPerShare = divPS;
		mPctGainTarget = pctGainTarget;
		mYrMax = 0f;
		mYrMin = 0f;
		mBuyBlockList = new ArrayList<BuyBlock>();
		mStrikePrice = 0f;
		mPctChangeSinceBuy = 0f;
		mAnalystsOpinion = 0.0f;
		mOverallAccountColor = Constants.ACCOUNT_UNKNOWN;  // default placeholder
	 }
	 
	 // Constructor for Watch quote
	 public StockQuote(String symbol,
		               float pps, 
		               float strikePrice,
		               float divPS,
		               float yrMax,
		               float yrMin) {
			mSymbol = symbol;
			mFullName = "Foo Bar Incorporated";
			mStatus = Status.WATCH;
			mPPS = pps;
			mStrikePrice = strikePrice;
			mDivPerShare = divPS;
			mPctGainTarget = 0f;
			mYrMax = yrMax;
			mYrMin = yrMin;
			mAnalystsOpinion = 0.0f;	
			mOverallAccountColor = Constants.ACCOUNT_UNKNOWN;  
	 }


	 
	 
	// -------- Methods -------- 
    public void compute(float lastClosePPS) {
    	if (mBuyBlockList != null) {
		    for (BuyBlock block: mBuyBlockList)
		        block.computeChange(mPPS);

		    mPctChangeSinceBuy = findBestChangeSinceBuy();
	    }
	    
    	// Don't use commission to compute change since last close
	    if (lastClosePPS > .0001) {
		    mPctChangeSinceLastClose = ((mPPS-lastClosePPS)/lastClosePPS) * 100.0f;
		} else {
		    mPctChangeSinceLastClose = 0.0f;
		}
	}
    
    public void determineOverallAccountColor() {
    	if ((mBuyBlockList != null) && (mBuyBlockList.size() > 0)) {
    		mOverallAccountColor = Constants.ACCOUNT_UNKNOWN;
    		for (BuyBlock block: mBuyBlockList) {
    			if (mOverallAccountColor == Constants.ACCOUNT_UNKNOWN) {
    				mOverallAccountColor = block.mAccountColor;
    			}
    			else {
    			    if (block.mAccountColor != mOverallAccountColor) {
    				     mOverallAccountColor = Constants.ACCOUNT_MIXED;
    				    return;
    			    }
    			}
    		}
    	}
    }
	
    private float findBestChangeSinceBuy() {
    	float bestChangeSinceBuy = Float.NEGATIVE_INFINITY;
    	for (BuyBlock block: mBuyBlockList) {
    		if (block.mPctChangeSinceBuy > bestChangeSinceBuy) {
    			bestChangeSinceBuy = block.mPctChangeSinceBuy;
    		}
    	}
    	return bestChangeSinceBuy;
    }
    
    public  BuyBlock findBuyBlockByDateString(String dateString) {
    	BuyBlock theBuyBlock = null;
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
    	for(BuyBlock bb : mBuyBlockList) {
    		String aDateString = sdf.format(bb.mBuyDate);
    		if (dateString.contentEquals(aDateString)) {
    			theBuyBlock = bb;
    			break;
    		}
    	}
    	
    	return theBuyBlock;
    }
	 
	// -------- Implementation of Parcelable interface --------- 

    // Constructor for Parcelable
	private StockQuote(Parcel in){
	    readFromParcel(in);
    }
	 
	@Override
	public int describeContents() {
		// May have to do something here
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSymbol);
        dest.writeString(mFullName);
        dest.writeString(mStatus.name());
        dest.writeFloat(mPPS);
        dest.writeTypedList(mBuyBlockList);
        dest.writeFloat(mPctGainTarget);
        dest.writeFloat(mYrMax);
        dest.writeFloat(mYrMin);
        dest.writeFloat(mDivPerShare);
        dest.writeFloat(mStrikePrice);
        dest.writeFloat(mPctChangeSinceLastClose);
        dest.writeFloat(mAnalystsOpinion);
        dest.writeInt(mOverallAccountColor);
	}
	
	private void readFromParcel(Parcel in) {
		mSymbol = in.readString();
		mFullName = in.readString();
		try {
			mStatus = Status.valueOf(in.readString());
		} catch (IllegalArgumentException e) {
			mStatus = Status.OWNED;
		}
		mPPS = in.readFloat();
		mBuyBlockList = new ArrayList<BuyBlock>();
		in.readTypedList(mBuyBlockList, BuyBlock.CREATOR);
		mPctGainTarget = in.readFloat();
		mYrMax = in.readFloat();
		mYrMin = in.readFloat();
		mDivPerShare = in.readFloat();
		mStrikePrice = in.readFloat();
		mPctChangeSinceLastClose = in.readFloat();
		mAnalystsOpinion = in.readFloat();
		mOverallAccountColor = in.readInt();
	}
	
	public static final Parcelable.Creator<StockQuote> CREATOR = new Parcelable.Creator<StockQuote>() {
		public StockQuote createFromParcel(Parcel in) {
			return new StockQuote(in);
		}
		
		public StockQuote[] newArray(int size) {
			return new StockQuote[size];
		}
	};
}

