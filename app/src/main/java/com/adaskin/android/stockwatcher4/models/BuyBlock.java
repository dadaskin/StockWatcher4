package com.adaskin.android.stockwatcher4.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.adaskin.android.stockwatcher4.utilities.Constants;

import android.os.Parcel;
import android.os.Parcelable;

public class BuyBlock implements Parcelable{
    
	// -------- Fields --------
	// Raw fields
	public Date mBuyDate;
	public float mNumShares;
	public float mBuyPPS;   
	public float mBuyCommissionPerShare;
	public int mAccountColor;
	
	// Calculated fields
	public float mTotalDividend;
	public float mEffDivYield;
	public float mPctChangeSinceBuy;

	// -------- Constructor --------
	public BuyBlock(Date buyDate, 
			        float numShares,
			        float buyPrice,  
			        float buyCommissionPS, 
			        float divPS, 
			        float totalDividend, 
			        int accountColor) {
		mBuyDate = buyDate;
		mNumShares = numShares;
		mBuyPPS = buyPrice;
		mBuyCommissionPerShare = buyCommissionPS;
		mEffDivYield = divPS/mBuyPPS * 100.0f;
		mAccountColor = accountColor;
	}
	
	// -------- Methods -------- 
	// Public Methods
	public void computeChange(float currentPPS){
		// Assuming that that the sell commission per shares is same as buy.
        mPctChangeSinceBuy = ((currentPPS - mBuyPPS - mBuyCommissionPerShare)/mBuyPPS)*100.0f;
	}

	
	// -------- Implementation of Parcelable Interface --------
	// Constructor used when re-contructing object from a parcel
	public BuyBlock(Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, 
			                  int flags) {
    	SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
		String dateString = sdf.format(mBuyDate);
		dest.writeString(dateString);
		dest.writeFloat(mNumShares);
		dest.writeFloat(mBuyPPS);
		dest.writeFloat(mBuyCommissionPerShare);
		dest.writeFloat(mTotalDividend);
		dest.writeFloat(mEffDivYield);
		dest.writeFloat(mPctChangeSinceBuy);
		dest.writeInt(mAccountColor);
	}
	
	private void readFromParcel(Parcel in) {
    	SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
		String dateString = in.readString();
		try {
			mBuyDate= (Date)sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		mNumShares = in.readFloat();
		mBuyPPS = in.readFloat();
		mBuyCommissionPerShare = in.readFloat();
		mTotalDividend = in.readFloat();
		mEffDivYield = in.readFloat();
		mPctChangeSinceBuy = in.readFloat();
		mAccountColor = in.readInt();
	}
	
	public static final Parcelable.Creator<BuyBlock> CREATOR = new Parcelable.Creator<BuyBlock>() {
		public BuyBlock createFromParcel(Parcel in) {
			return new BuyBlock(in);
		}
		
		public BuyBlock[] newArray(int size) {
			return new BuyBlock[size];
		}
	};
		
}
