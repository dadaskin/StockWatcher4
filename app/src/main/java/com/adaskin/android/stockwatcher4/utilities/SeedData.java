package com.adaskin.android.stockwatcher4.utilities;

import java.util.Date;
import java.util.GregorianCalendar;

import com.adaskin.android.stockwatcher4.database.DbAdapter;
import com.adaskin.android.stockwatcher4.models.BuyBlock;
import com.adaskin.android.stockwatcher4.models.StockQuote;

@SuppressWarnings("SpellCheckingInspection")
public class SeedData {

	public SeedData(DbAdapter dba) {

		// Last Update date/time
	   	   dba.createLastUpdateRecord("Aug 31, 2012", "12:22 PM PDT");
	   	   
	   	   // OWNED
	       dba.createQuoteRecord(createASeed());
	       dba.createQuoteRecord(createACCLSeed());
	       dba.createQuoteRecord(createACASSeed());
          // dba.createQuoteRecord(createAZNSeed());
	       dba.createQuoteRecord(createBTISeed());
	       //dba.createQuoteRecord(createCMISeed());
	       //dba.createQuoteRecord(createDALSeed());
	       //dba.createQuoteRecord(createDUKSeed());
	      // dba.createQuoteRecord(createECOLSeed());
	       dba.createQuoteRecord(createFSeed());
	       dba.createQuoteRecord(createLSRCXSeed());
	       
	       dba.createQuoteRecord(createGESeed());
	     //  dba.createQuoteRecord(createMOSeed());
	     //  dba.createQuoteRecord(createRGRSeed());
           //dba.createQuoteRecord(createSWHCSeed());
	       dba.createQuoteRecord(createVLCCFSeed());
	      // dba.createQuoteRecord(createVVCSeed());
	      // dba.createQuoteRecord(createUALSeed());
	      // dba.createQuoteRecord(createHISeed());
	      // dba.createQuoteRecord(createMSFTSeed());
	     //  dba.createQuoteRecord(createOSeed());
	      // dba.createQuoteRecord(createPHKSeed());
	     //  dba.createQuoteRecord(createPMSeed());
//	       mDbAdapter.createQuoteRecord(createSESeed());
//	       mDbAdapter.createQuoteRecord(createTSeed());
//	       mDbAdapter.createQuoteRecord(createTEGSeed());
//	       mDbAdapter.createQuoteRecord(createFCXSeed());
//	       mDbAdapter.createQuoteRecord(createDOVSeed());
//	       mDbAdapter.createQuoteRecord(createDDSeed());
//	       mDbAdapter.createQuoteRecord(createFAXSeed());
//	       mDbAdapter.createQuoteRecord(createJNKSeed());
//	       mDbAdapter.createQuoteRecord(createMMMSeed());
//	       mDbAdapter.createQuoteRecord(createOLNSeed());
//	       mDbAdapter.createQuoteRecord(createPBISeed());
//	       mDbAdapter.createQuoteRecord(createPPLSeed());
//	       mDbAdapter.createQuoteRecord(createPSECSeed());
//	       mDbAdapter.createQuoteRecord(createRGCSeed());
//	       mDbAdapter.createQuoteRecord(createRRDSeed());
//	       mDbAdapter.createQuoteRecord(createUTXSeed());
//	       mDbAdapter.createQuoteRecord(createVZSeed());
//	       mDbAdapter.createQuoteRecord(createMRKSeed());
//	       mDbAdapter.createQuoteRecord(createHSCSeed());
//	       mDbAdapter.createQuoteRecord(createWMSeed());
//	       mDbAdapter.createQuoteRecord(createKOSeed());
//	       mDbAdapter.createQuoteRecord(createAEPSeed());
//	       mDbAdapter.createQuoteRecord(createGLOSeed());
//	       mDbAdapter.createQuoteRecord(createJNJSeed());
	       
	       // WATCH
	       //dba.createQuoteRecord(createNLYSeed());
	       //dba.createQuoteRecord(createABTSeed());
	   }
	


	    
	private StockQuote createASeed() {
		float pps = 38.00f;
		float divPS = 0.4f;
		
	    Date date0 = (new GregorianCalendar(2000, 5, 2)).getTime(); // Actually: 2000-06-02

		BuyBlock bb0 = new BuyBlock(date0,  10.0f, 19.80f, 0.07f, divPS, Constants.ACCOUNT_JOINT);
		
		StockQuote q = new StockQuote("A", pps, divPS, 200f);
		q.mFullName = "Agilent Technologies";
		q.mBuyBlockList.add(bb0);
		return q;
	}

	private StockQuote createACASSeed() {
			float pps = 15.0f;
			float divPS = 0.0f;
			
			Date date0 = (new GregorianCalendar(2003, 0, 22)).getTime();  // Actually: 2003,  1, 22
			Date date1 = (new GregorianCalendar(2009, 7, 10)).getTime();  // Actually: 2009,  8, 10
			
			BuyBlock bb0 = new BuyBlock(date0, 100.0f, 23.66f, 0.07f, divPS, Constants.ACCOUNT_JOINT);
			BuyBlock bb1 = new BuyBlock(date1,  99.0f,  3.22f, 0.07f, divPS, Constants.ACCOUNT_JOINT);
			
			StockQuote q = new StockQuote("ACAS", pps, divPS, 20f);
			q.mFullName = "American Capital, Ltd";
			q.mBuyBlockList.add(bb0);
			q.mBuyBlockList.add(bb1);
			return q;
	}

		private StockQuote createACCLSeed() {
			float pps = 8.00f;
			float divPS = 0.0f;
			
			Date date0 = (new GregorianCalendar(2007, 2, 1)).getTime();  // Actually: 2007, 03, 1
			Date date1 = (new GregorianCalendar(2007, 3, 30)).getTime();  // Actually: 2007, 04, 30
			Date date2 = (new GregorianCalendar(2007, 9, 31)).getTime();  // Actually: 2007, 10, 31
		
			BuyBlock bb0 = new BuyBlock(date0,  55.3942f, 45.13f, 0.10f, divPS, Constants.ACCOUNT_ESPP);
			BuyBlock bb1 = new BuyBlock(date1, 419.7476f, 12.54f, 0.10f, divPS, Constants.ACCOUNT_ESPP);
			BuyBlock bb2 = new BuyBlock(date2, 528.1954f, 10.034f, 0.10f, divPS, Constants.ACCOUNT_ESPP);
			
			StockQuote q = new StockQuote("ACCL", pps, divPS, 10f);
			q.mFullName = "Accelrys";
			q.mBuyBlockList.add(bb0);
			q.mBuyBlockList.add(bb1);
			q.mBuyBlockList.add(bb2);
			return q;
		}

//		private StockQuote createAEPSeed() {
//			Date date0 = new Date(109,  8, 23);  // Actually: 2009,  9, 23
//			BuyBlock bb0 = new BuyBlock(date0, 100f, 31.53f, 0.07f, 0f ,0f, Constants.ACCOUNT_ROTH);
//			StockQuote q = new StockQuote("AEP", 0f, 0f, 25f);
//			q.mBuyBlockList.add(bb0);
//			return q;
//		}
		
		private StockQuote createAZNSeed() {
			Date date0 = (new GregorianCalendar(2012, 1, 21)).getTime();  // Actually: 2012,  2, 21
			Date date1 = (new GregorianCalendar(2012, 4, 2)).getTime();  // Actually: 2012,  5,  2
			
			BuyBlock bb0 = new BuyBlock(date0, 200f, 45.09f, 0.08f, 0f , Constants.ACCOUNT_IRA);
			BuyBlock bb1 = new BuyBlock(date1, 100f, 43.93f, 0.08f, 0f , Constants.ACCOUNT_IRA);
			
			StockQuote q = new StockQuote("AZN", 0f, 0f, 25f);
			
			// Add a dummy pps, et. al.
			q.mPPS = 46f;
			q.mYrMax = 50f;
			q.mYrMin = 40f;
			q.mDivPerShare = 0.2f;
			bb0.mEffDivYield = (q.mDivPerShare/bb0.mBuyPPS) * 100f;
			bb1.mEffDivYield = (q.mDivPerShare/bb1.mBuyPPS) * 100f;
            //
			
			q.mBuyBlockList.add(bb0);
			q.mBuyBlockList.add(bb1);
			
			// Add a dummy prevClose and compute
			q.compute(44.5f);
			
			return q;
		}
		
		private StockQuote createBTISeed() {
			float pps = 101.5f;
			float divPS = 2.85f;
			
			Date date0 = (new GregorianCalendar(2009, 5, 4)).getTime();  // Actually: 2009, 6, 4
			Date date1 = (new GregorianCalendar(2010, 5, 4)).getTime();  // Actually: 2010, 6, 4
			
			BuyBlock bb0 = new BuyBlock(date0, 100.0f, 55.10f, 0.08f, divPS, Constants.ACCOUNT_IRA);
			BuyBlock bb1 = new BuyBlock(date1, 100.0f, 62.00f, 0.08f, divPS, Constants.ACCOUNT_JOINT);
					
			StockQuote q = new StockQuote("BTI", pps, divPS, 120f);
			q.mFullName = "British-American Tobacco";
			q.mStrikePrice = 55.0f;
			
			// Add a dummy pps, et. al.
			q.mPPS = 52.35f;
			q.mYrMax = 60f;
			q.mYrMin = 50f;
			q.mDivPerShare = 2.05f;
			bb0.mEffDivYield = (q.mDivPerShare/bb0.mBuyPPS) * 100f;
			bb1.mEffDivYield = (q.mDivPerShare/bb1.mBuyPPS) * 100f;
            //
			
			q.mBuyBlockList.add(bb0);
			q.mBuyBlockList.add(bb1);
			
			// Add a dummy prevClose and compute
			q.compute(53.10f);
			
			return q;
		}

		private StockQuote createCMISeed() {
			Date date0 = (new GregorianCalendar(2012, 4, 3)).getTime();  // Actually: 2012,  5,  3
			Date date1 = (new GregorianCalendar(2013, 4, 3)).getTime();  // Actually: 2013,  5,  3
			
			BuyBlock bb0 = new BuyBlock(date0,  50f, 111.27f, 0.07f, 0f , Constants.ACCOUNT_JOINT);
			BuyBlock bb1 = new BuyBlock(date1, 100f, 111.27f, 0.07f, 0f , Constants.ACCOUNT_ROTH);

			StockQuote q = new StockQuote("CMI", 0f, 0f, 25f);
			
			// Add a dummy pps, et. al.
			q.mPPS = 111.34f;
			q.mYrMax = 120f;
			q.mYrMin = 100f;
			q.mDivPerShare = 0.1f;
			bb0.mEffDivYield = (q.mDivPerShare/bb0.mBuyPPS) * 100f;
			bb1.mEffDivYield = (q.mDivPerShare/bb1.mBuyPPS) * 100f;
            //
			
			q.mBuyBlockList.add(bb0);
			q.mBuyBlockList.add(bb1);
			
			// Add a dummy prevClose and compute
			q.compute(111.34f);
			
			return q;
		}
		
		private StockQuote createDALSeed() {
			Date date0 = (new GregorianCalendar(2012, 1, 22)).getTime();  // Actually: 2012,  2, 22
			Date date1 = (new GregorianCalendar(2013, 1, 22)).getTime();  // Actually: 2013,  2, 22
			Date date2 = (new GregorianCalendar(2013, 1, 1)).getTime();  // Actually: 2013,  5, 01
			Date date3 = (new GregorianCalendar(2013, 9, 1)).getTime();  // Actually: 2013, 10, 01
			
			BuyBlock bb0 = new BuyBlock(date0, 200f, 9.99f, 0.07f, 0f, Constants.ACCOUNT_IRA);
			BuyBlock bb1 = new BuyBlock(date1, 200f, 7.99f, 0.07f, 0f, Constants.ACCOUNT_JOINT);
			BuyBlock bb2 = new BuyBlock(date2, 100f, 13.0f, 0.07f, 0f, Constants.ACCOUNT_ROTH);
			BuyBlock bb3 = new BuyBlock(date3, 500f, 10.18f, 0.07f, 0f, Constants.ACCOUNT_IRA);
			
			StockQuote q = new StockQuote("DAL", 0f, 0f, 25f);
			
			// Add a dummy pps, et. al.
			q.mPPS = 10.25f;
			q.mYrMax = 12f;
			q.mYrMin = 5f;
			q.mDivPerShare = 0.0f;
			bb0.mEffDivYield = (q.mDivPerShare/bb0.mBuyPPS) * 100f;
			bb1.mEffDivYield = (q.mDivPerShare/bb1.mBuyPPS) * 100f;
			bb2.mEffDivYield = (q.mDivPerShare/bb2.mBuyPPS) * 100f;
			bb3.mEffDivYield = (q.mDivPerShare/bb3.mBuyPPS) * 100f;
            //
			
			q.mBuyBlockList.add(bb0);
			q.mBuyBlockList.add(bb1);
			q.mBuyBlockList.add(bb2);
			q.mBuyBlockList.add(bb3);
			
			// Add a dummy prevClose and compute
			q.compute(11.00f);
			
			return q;
		}
		
//		private StockQuote createDDSeed() {
//			Date date0 = new Date(111,   2, 16);  // Actually: 2011,  3, 16
//			BuyBlock bb0 = new BuyBlock(date0, 200f, 51.79f, 0.08f, 0f, Constants.ACCOUNT_IRA);
//			StockQuote q = new StockQuote("DD", 0f, 0f, 25f);
//			q.mBuyBlockList.add(bb0);
//			return q;
//		}
		
//		private StockQuote createDOVSeed() {
//			Date date0 = new Date(112,  5, 26);  // Actuall: 2012,  6, 26
//			BuyBlock bb0 = new BuyBlock(date0, 200f, 51.42f, 0.07f, 0f, 0f);
//			StockQuote q = new StockQuote("DOV", 0f, 0f, 25f);
//			q.mBuyBlockList.add(bb0);
//			return q;
//		}
		
		private StockQuote createDUKSeed() {
			float pps = 62.00f;
			float divPS = 3.06f;
			
			Date date0 = (new GregorianCalendar(2004, 7, 9)).getTime();  // Actually: 2004, 8, 9
		
			BuyBlock bb0 = new BuyBlock(date0, 33.0f, 44.13f, 0.07f, divPS, Constants.ACCOUNT_IRA);
			
			StockQuote q = new StockQuote("DUK", pps, divPS, 100f);
			q.mFullName = "Duke Energy";
			q.mBuyBlockList.add(bb0);
			return q;
		}

		private StockQuote createECOLSeed() {
			float pps = 22.00f;
			float divPS = 0.68f;
			
			Date date0 = (new GregorianCalendar(2009, 11, 8)).getTime();  // Actually 2009, 12, 08
			Date date1 = (new GregorianCalendar(2012, 4, 1)).getTime();  // Actually 2012,  5,  1
			
			BuyBlock bb0 = new BuyBlock(date0, 100f, 16.86f, 0.08f, divPS, Constants.ACCOUNT_IRA);
			BuyBlock bb1 = new BuyBlock(date1, 100f, 17.28f, 0.08f, divPS, Constants.ACCOUNT_IRA);
			
			StockQuote q = new StockQuote("ECOL", pps, divPS, 30f);
		    q.mFullName = "US Ecology";
		    q.mBuyBlockList.add(bb0);
		    q.mBuyBlockList.add(bb1);
		    return q;
		}

		private StockQuote createFSeed() {
	    	float pps = 10.36f;
	    	float divPS = 0.20f;
	    	
	    	Date date0 = (new GregorianCalendar(2012, 5, 27)).getTime();  // Actually 2012, 6, 27
	    	
	    	BuyBlock bb0 = new BuyBlock(date0, 1000.0f, 9.99f, 0.07f, divPS, Constants.ACCOUNT_IRA);
    	
	    	StockQuote q = new StockQuote("F", pps, divPS, 25f);
	    	q.mFullName = "Ford Motor Co.";
	    	q.mAnalystsOpinion = 2.1f;
	    	q.mBuyBlockList.add(bb0);
	    	return q;
	    }
	    
//		private StockQuote createFAXSeed() {
//			Date date0 = new Date(110,  5, 10);  // Actually: 2010,  6, 10
//	    	BuyBlock bb0 = new BuyBlock(date0, 1000.0f, 5.31f, 0.08f, 0f, 0f);
//	    	StockQuote q = new StockQuote("FAX", 0f, 0f, 25f);
//	    	q.mBuyBlockList.add(bb0);
//	    	return q;
//		}
		
//		private StockQuote createFCXSeed() {
//			Date date0 = new Date(112,  2, 14);  // Actually: 2012,  3, 14
//			Date date1 = new Date(112, 11,  5);  // Actually: 2012, 12,  5
//			
//			BuyBlock bb0 = new BuyBlock(date0, 200f, 38.19f, 0.07f, 0f, 0f);
//			BuyBlock bb1 = new BuyBlock(date1, 100f, 32.99f, 0.07f, 0f, 0f);
//			
//			StockQuote q = new StockQuote("FCX", 0f, 0f, 25f);
//			q.mBuyBlockList.add(bb0);
//			q.mBuyBlockList.add(bb1);
//			return q;
//		}
		
		
	    private StockQuote createGESeed() {
			Date date0 = (new GregorianCalendar(2011, 7, 3)).getTime();  // Actually 2011,  8,  3
			
			BuyBlock bb0 = new BuyBlock(date0, 200f, 17.32f, 0.07f, 0f, Constants.ACCOUNT_IRA);
			
			StockQuote q = new StockQuote("GE", 0.0f, 0.0f, 25f);
			q.mBuyBlockList.add(bb0);
			return q;
		}

//	    private StockQuote createGLOSeed() {
//			Date date0 = new Date(108, 1, 14);  // Actually 2008,  2, 14
//			BuyBlock bb0 = new BuyBlock(date0, 200f, 17.81f, 0.07f, 0f, 0f);
//			StockQuote q = new StockQuote("GLO", 0.0f, 0.0f, 25f);
//			q.mBuyBlockList.add(bb0);
//			return q;
//		}
	    
	    private StockQuote createHISeed() {
	    	Date date0 = (new GregorianCalendar(2010, 7, 10)).getTime();  // Actually 2010,  8, 10
	    	BuyBlock bb0 = new BuyBlock(date0, 200f, 20.73f, 0.07f, 0f, Constants.ACCOUNT_IRA);
	    	StockQuote q = new StockQuote("HI", 0f, 0f, 25f);
	    	q.mBuyBlockList.add(bb0);
	    	return q;
	    }
	    
//	    private StockQuote createHSCSeed() {
//	    	Date date0 = new Date(112,  5, 28);  // Actually 2012,  6, 28
//	    	BuyBlock bb0 = new BuyBlock(date0, 300f, 18.83f, 0.08f, 0f, 0f);
//	    	StockQuote q = new StockQuote("HSC", 0f, 0f, 25f);
//	    	q.mBuyBlockList.add(bb0);
//	    	return q;
//	    }
	    
//	    private StockQuote createJNJSeed() {
//			Date date0 = new Date(107,  10, 30);  // Actually: 2007, 11, 30
//	    	BuyBlock bb0 = new BuyBlock(date0, 100f, 69.11f, 0.07f, 0f, 0f);
//	    	StockQuote q = new StockQuote("JNJ", 0f, 0f, 25f);
//	    	q.mBuyBlockList.add(bb0);
//	    	return q;
//	    }
	    
//	    private StockQuote createJNKSeed() {
//			Date date0 = new Date(111,  5, 13);  // Actually: 2011,  6, 13
//	    	BuyBlock bb0 = new BuyBlock(date0, 200f, 39.84f, 0.08f, 0f, 0f);
//	    	StockQuote q = new StockQuote("JNK", 0f, 0f, 25f);
//	    	q.mBuyBlockList.add(bb0);
//	    	return q;
//	    }
	    
//	    private StockQuote createKOSeed() {
//			Date date0 = new Date(112,  9, 28);  // Actually: 2012, 10, 28
//	    	BuyBlock bb0 = new BuyBlock(date0, 200f, 36.74f, 0.08f, 0f, 0f);
//	    	StockQuote q = new StockQuote("KO", 0f, 0f, 25f);
//	    	q.mBuyBlockList.add(bb0);
//	    	return q;
//	    }
	    
		private StockQuote createLSRCXSeed() {
		    	Date date0 = (new GregorianCalendar(2001, 0, 9)).getTime(); // Actually: 2001,  1,  9
		    	Date date1 = (new GregorianCalendar(2001, 11, 13)).getTime(); // Actually: 2001, 12, 13
		    	Date date2 = (new GregorianCalendar(2002, 11, 13)).getTime(); // Actually: 2002, 12, 13
		    	Date date3 = (new GregorianCalendar(2003, 11, 12)).getTime(); // Actually: 2003, 12, 12
		    	Date date4 = (new GregorianCalendar(2004, 11, 14)).getTime(); // Actually: 2004, 12, 14
		    	Date date5 = (new GregorianCalendar(2005, 11, 14)).getTime(); // Actually: 2005, 12, 14
		    	Date date6 = (new GregorianCalendar(2006, 11, 15)).getTime(); // Actually: 2006, 12, 15
		    	Date date7 = (new GregorianCalendar(2007, 11, 16)).getTime(); // Actually: 2007, 12, 16
		    	
		    	BuyBlock bb0 = new BuyBlock(date0, 98.8630f, 20.23f, 0.07f, 10.5f, Constants.ACCOUNT_UNKNOWN);
		    	BuyBlock bb1 = new BuyBlock(date1, 5.7552f,  20.27f, 0.07f, 10.5f, Constants.ACCOUNT_MIXED);
		    	BuyBlock bb2 = new BuyBlock(date2, 7.8787f,  17.82f, 0.07f, 10.5f, Constants.ACCOUNT_JOINT);
		    	BuyBlock bb3 = new BuyBlock(date3, 8.0194f,  23.02f, 0.07f, 11.0f, Constants.ACCOUNT_IRA);
		    	BuyBlock bb4 = new BuyBlock(date4, 12.6590f, 25.20f, 0.07f, 11.5f, Constants.ACCOUNT_ROTH);
		    	BuyBlock bb5 = new BuyBlock(date5, 14.3030f, 26.75f, 0.07f, 12.0f, Constants.ACCOUNT_ESPP);
		    	BuyBlock bb6 = new BuyBlock(date6, 24.4850f, 24.80f, 0.07f, 12.0f, Constants.ACCOUNT_DRIP);
		    	BuyBlock bb7 = new BuyBlock(date7, 33.8120f, 24.46f, 0.07f, 13.0f, Constants.ACCOUNT_UNKNOWN);
		    	
		    	StockQuote q = new StockQuote("LSRCX", 0.0f, 0.0f, 25f);
		    	q.mBuyBlockList.add(bb0);
		    	q.mBuyBlockList.add(bb1);
		    	q.mBuyBlockList.add(bb2);
		    	q.mBuyBlockList.add(bb3);
		    	q.mBuyBlockList.add(bb4);
		    	q.mBuyBlockList.add(bb5);
		    	q.mBuyBlockList.add(bb6);
		    	q.mBuyBlockList.add(bb7);
		    	return q;
		}

//		private StockQuote createMMMSeed() {
//			Date date0 = new Date(111,  2, 17);  // Actually: 2011,  3, 17
//	    	BuyBlock bb0 = new BuyBlock(date0, 100f, 88.64f, 0.08f, 0f, 0f);
//	    	StockQuote q = new StockQuote("MMM", 0f, 0f, 25f);
//	    	q.mBuyBlockList.add(bb0);
//	    	return q;
//		}
		
		private StockQuote createMOSeed() {
			float pps = 20.00f;
		 	float divPS = 1.50f;
			
			Date date0 = (new GregorianCalendar(2001, 10, 19)).getTime();  // Actually: 2001, 11, 19
			Date date1 = (new GregorianCalendar(2009, 1, 16)).getTime();   // Actually: 2009,  2, 16
			Date date2 = (new GregorianCalendar(2009, 10, 19)).getTime();  // Actually: 2009, 11, 19
			Date date3 = (new GregorianCalendar(2011, 0, 27)).getTime();   // Actually: 2011,  1, 27
			Date date4 = (new GregorianCalendar(2011, 0, 31)).getTime();   // Actually: 2011,  1, 31
			
			BuyBlock bb0 = new BuyBlock(date0, 100.0f,  8.61f, 0.07f, divPS, Constants.ACCOUNT_ROTH);
			BuyBlock bb1 = new BuyBlock(date1, 100.0f, 15.69f, 0.08f, divPS, Constants.ACCOUNT_ROTH);
			BuyBlock bb2 = new BuyBlock(date2, 400.0f, 19.25f, 0.08f, divPS, Constants.ACCOUNT_ROTH);
			BuyBlock bb3 = new BuyBlock(date3, 100.0f, 24.06f, 0.08f, divPS, Constants.ACCOUNT_ROTH);
			BuyBlock bb4 = new BuyBlock(date4, 100.0f, 23.53f, 0.08f, divPS, Constants.ACCOUNT_ROTH);
			
			StockQuote q = new StockQuote("MO", pps, divPS, 300f);
			q.mFullName = "Altria";
			q.mBuyBlockList.add(bb0);
			q.mBuyBlockList.add(bb1);
			q.mBuyBlockList.add(bb2);
			q.mBuyBlockList.add(bb3);
			q.mBuyBlockList.add(bb4);
			return q;
		}

//		private StockQuote createMRKSeed() {
//			Date date0 = new Date(112,  4,  1);  // Actually: 2012,  5,  1
//			BuyBlock bb0 = new BuyBlock(date0, 100f, 38.00f, 0.08f, 0f, 0f);
//			StockQuote q = new StockQuote("MRK", 0f, 0f, 25f);
//			q.mBuyBlockList.add(bb0);
//			return q;
//		}
		
		private StockQuote createMSFTSeed() {
			Date date0 = (new GregorianCalendar(1999, 3, 27)).getTime();  // Actually: 1999,  4, 27
			BuyBlock bb0 = new BuyBlock(date0, 50f, 42.48f, 0.07f, 0f, Constants.ACCOUNT_ROTH);
			StockQuote q = new StockQuote("MSFT", 0f, 0f, 10f);
			q.mBuyBlockList.add(bb0);
			return q;
		}

		private StockQuote createOSeed() {
			Date date0 = (new GregorianCalendar(2007, 4, 15)).getTime();  // Actually: 2007,  5, 15
			Date date1 = (new GregorianCalendar(2009, 9, 7)).getTime();  // Actually: 2009, 10,  7
			Date date2 = (new GregorianCalendar(2012, 10, 1)).getTime();  // Actually: 2012, 11,  1
			
			BuyBlock bb0 = new BuyBlock(date0, 200f, 28.04f, 0.07f, 0f, Constants.ACCOUNT_JOINT);
			BuyBlock bb1 = new BuyBlock(date1, 500f, 23.52f, 0.08f, 0f, Constants.ACCOUNT_JOINT);
			BuyBlock bb2 = new BuyBlock(date2, 100f, 39.64f, 0.07f, 0f, Constants.ACCOUNT_JOINT);
			
			StockQuote q = new StockQuote("O", 0f, 0f, 100f);
			q.mBuyBlockList.add(bb0);
			q.mBuyBlockList.add(bb1);
			q.mBuyBlockList.add(bb2);
			return q;
		}
		
//		private StockQuote createOLNSeed(){
//			Date date0 = new Date(111,  0, 19);  // Actually: 2011,  1, 19
//			Date date1 = new Date(111,  7, 10);  // Actually: 2011,  8, 10
//			
//			BuyBlock bb0 = new BuyBlock(date0, 200f, 19.77f, 0.08f, 0f, 0f);
//			BuyBlock bb1 = new BuyBlock(date1, 200f, 18.24f, 0.08f, 0f, 0f);
//	    	
//	    	StockQuote q = new StockQuote("OLN", 0f, 0f, 25f);
//	    	q.mBuyBlockList.add(bb0);
//	    	q.mBuyBlockList.add(bb1);
//	    	return q;
//		}
		
//		private StockQuote createPBISeed() {
//			Date date0 = new Date(111,  5, 13);  // Actually: 2011,  6, 13
//			Date date1 = new Date(112,  4,  8);  // Actually: 2011,  5,  8
//			
//			BuyBlock bb0 = new BuyBlock(date0, 300f, 22.58f, 0.08f, 0f, 0f);
//			BuyBlock bb1 = new BuyBlock(date1, 100f, 15.53f, 0.08f, 0f, 0f);
//	    	
//	    	StockQuote q = new StockQuote("PBI", 0f, 0f, 25f);
//	    	q.mBuyBlockList.add(bb0);
//	    	q.mBuyBlockList.add(bb1);
//	    	return q;
//		}
		
		private StockQuote createPHKSeed() {
			Date date0 = (new GregorianCalendar(2004, 6, 27)).getTime();  // Actually: 2004,  7, 27
			Date date1 = (new GregorianCalendar(2010, 3, 16)).getTime();  // Actually: 2010,  4, 16)
			Date date2 = (new GregorianCalendar(2012, 9, 25)).getTime();  // Actually: 2012, 10, 25)
			
			BuyBlock bb0 =  new BuyBlock(date0, 200f, 13.96f, 0.07f, 0f, Constants.ACCOUNT_JOINT);
			BuyBlock bb1 =  new BuyBlock(date1, 500f, 12.04f, 0.07f, 0f, Constants.ACCOUNT_JOINT);
			BuyBlock bb2 =  new BuyBlock(date2, 500f, 11.84f, 0.08f, 0f, Constants.ACCOUNT_JOINT);
			
			StockQuote q = new StockQuote("PHK", 0f, 0f, 50f);
			q.mBuyBlockList.add(bb0);
			q.mBuyBlockList.add(bb1);
			q.mBuyBlockList.add(bb2);
			return q;
		}
		
		private StockQuote createPMSeed() {
			Date date0 = (new GregorianCalendar(2008, 2, 28)).getTime();  // Actually: 2008,  3, 28
			BuyBlock bb0 = new BuyBlock(date0, 100f, 19.61f, 0.07f, 0f, Constants.ACCOUNT_JOINT);
			StockQuote q = new StockQuote("PM", 0f, 0f, 100f);
			q.mBuyBlockList.add(bb0);
			return q;
		}
		
//		private StockQuote createPPLSeed() {
//			Date date0 = new Date(111,  8, 22);  // Actually: 2011,  9, 22
//			BuyBlock bb0 = new BuyBlock(date0, 300f, 28.33f, 0.08f, 0f, 0f);
//			StockQuote q = new StockQuote("PPL", 0f, 0f, 100f);
//			q.mBuyBlockList.add(bb0);
//			return q;
//		}
		
//		private StockQuote createPSECSeed() {
//			Date date0 = new Date(110,  4,  7);  // Actually: 2010,  5, 10
//			Date date1 = new Date(111,  6,  5);  // Actually: 2011,  7,  5)
//			Date date2 = new Date(111,  7,  1);  // Actually: 2011,  8,  1)
//			Date date4 = new Date(112, 10,  2);  // Actually: 2011, 11,  2)
//			Date date3 = new Date(112, 10, 14);  // Actually: 2012, 11, 12)
//			
//			BuyBlock bb0 =  new BuyBlock(date0, 500f, 10.51f, 0.08f, 0f, 0f);
//			BuyBlock bb1 =  new BuyBlock(date1, 500f, 10.04f, 0.08f, 0f, 0f);
//			BuyBlock bb2 =  new BuyBlock(date2, 500f,  9.42f, 0.08f, 0f, 0f);
//			BuyBlock bb3 =  new BuyBlock(date3, 100f, 11.00f, 0.07f, 0f, 0f);
//			BuyBlock bb4 =  new BuyBlock(date4, 100f,  9.99f, 0.07f, 0f, 0f);
//			
//			StockQuote q = new StockQuote("PSEC", 0f, 0f, 30f);
//			q.mBuyBlockList.add(bb0);
//			q.mBuyBlockList.add(bb1);
//			q.mBuyBlockList.add(bb2);
//			q.mBuyBlockList.add(bb3);
//			q.mBuyBlockList.add(bb4);
//			return q;
//		}
		
//		private StockQuote createRGCSeed() {
//			Date date0 = new Date(111,  4, 27);  // Actually: 2011,  5, 27
//			BuyBlock bb0 = new BuyBlock(date0, 400f, 13.82f, 0.08f, 0f, 0f);
//			StockQuote q = new StockQuote("RGC", 0f, 0f, 25f);
//			q.mBuyBlockList.add(bb0);
//			return q;
//		}
		
		private StockQuote createRGRSeed() {
			float pps = 43.0f;
			float divPS = 1.53f;
			
			Date date0 = (new GregorianCalendar(2012, 11, 21)).getTime();  // Actually: 2012, 12, 21
			
			BuyBlock bb0 = new BuyBlock(date0, 100.0f, 43.27f, 0.07f, divPS, Constants.ACCOUNT_JOINT);
			
			StockQuote q = new StockQuote("RGR", pps, divPS, 10f);
			q.mFullName = "Sturm, Ruger & Co.";
			q.mBuyBlockList.add(bb0);
			return q;
		}

//		private StockQuote createRRDSeed() {
//			Date date0 = new Date(111,  7,  3);  // Actually: 2011,  8, 11
//			BuyBlock bb0 = new BuyBlock(date0, 300f, 15.96f, 0.08f, 0f, 0f);
//			StockQuote q = new StockQuote("RRD", 0f, 0f, 25f);
//			q.mBuyBlockList.add(bb0);
//			return q;
//		}
		
//		private StockQuote createSESeed() {
//			Date date0 = new Date(107,  0,  2);  // Actually: 2007,  1,  2
//			BuyBlock bb0 = new BuyBlock(date0, 50f, 14.72f, 0.07f, 0f, 0f);
//			StockQuote q = new StockQuote("SE", 0f, 0f, 100f);
//            q.mBuyBlockList.add(bb0);
//            return q;
//		}
		
		private StockQuote createSWHCSeed() {
			float pps = 8.0f;
			float divPS = 0.0f;
			
			Date date0 = (new GregorianCalendar(2012, 11, 21)).getTime();  // Actually: 2012, 12, 21
			
			BuyBlock bb0 = new BuyBlock(date0, 100.0f, 8.12f, 0.07f, divPS, Constants.ACCOUNT_JOINT);
			
			StockQuote q = new StockQuote("SWHC", pps, divPS, 10f);
			q.mFullName = "Smith & Wesson Holding Company";
			q.mBuyBlockList.add(bb0);
			return q;
		}
		
//		private StockQuote createTSeed() {
//			Date date0 = new Date(108,  1, 20);  // Actually: 2008,  2, 20
//			Date date1 = new Date(110,  3, 16);  // Actually: 2010,  4, 16
//			
//			BuyBlock bb0 = new BuyBlock(date0, 100f, 34.39f, 0.07f, 0f, 0f);
//			BuyBlock bb1 = new BuyBlock(date1, 500f, 25.99f, 0.07f, 0f, 0f);
//			
//			StockQuote q = new StockQuote("T", 0f, 0f, 30f);
//			q.mBuyBlockList.add(bb0);
//			q.mBuyBlockList.add(bb1);
//			return q;
//		}
		
//		private StockQuote createTEGSeed() {
//			Date date0 = new Date(108,  5, 24);  // Actually: 2008,  6, 24
//			BuyBlock bb0 = new BuyBlock(date0, 100f, 49.17f, 0.07f, 0f, 0f);
//			StockQuote q = new StockQuote("TEG", 0f, 0f, 25f);
//			q.mBuyBlockList.add(bb0);
//			return q;
//		}
		
		private StockQuote createUALSeed() {
			float pps = 22.22f;
			float divPS = 0.0f;
			
			Date date0 = (new GregorianCalendar(2012, 1, 22)).getTime();  // Actually: 2012,  2, 22
			
			BuyBlock bb0 = new BuyBlock(date0, 100.0f, 21.27f, 0.07f, divPS, Constants.ACCOUNT_JOINT);
			
			StockQuote q = new StockQuote("UAL", pps, divPS, 25f);
			q.mFullName = "United Continental";
			q.mBuyBlockList.add(bb0);
			return q;
		}
		
//		private StockQuote createUTXSeed() {
//			Date date0 = new Date(111, 11, 16);  // Actually: 2011, 12, 16
//			BuyBlock bb0 = new BuyBlock(date0, 100f, 72.72f, 0.08f, 0f, 0f);
//			StockQuote q = new StockQuote("UTX", 0f, 0f, 25f);
//			q.mBuyBlockList.add(bb0);
//			return q;
//		}
		
		private StockQuote createVLCCFSeed() {
			float pps = 5.0f;
			float divPS = 0.7f;
			
			Date date0 = (new GregorianCalendar(2011, 4, 26)).getTime();  // Actually 2011, 5, 26
			Date date1 = (new GregorianCalendar(2012, 1, 21)).getTime();  // Actually 2012, 2, 21
			Date date2 = (new GregorianCalendar(2012, 4, 4)).getTime();  // Actually 2012, 5,  4
			
			BuyBlock bb0 = new BuyBlock(date0, 300f, 18.93f, 0.08f, divPS, Constants.ACCOUNT_JOINT);
			BuyBlock bb1 = new BuyBlock(date1, 100f, 14.89f, 0.08f, divPS, Constants.ACCOUNT_JOINT);
			BuyBlock bb2 = new BuyBlock(date2, 100f, 11.98f, 0.08f, divPS, Constants.ACCOUNT_JOINT);
			
			StockQuote q = new StockQuote("VLCCF", pps, divPS, 25f);
			q.mFullName = "Knightsbridge Tankers, Ltd";
			q.mBuyBlockList.add(bb0);
			q.mBuyBlockList.add(bb1);
			q.mBuyBlockList.add(bb2);
			return q;
		}

		private StockQuote createVVCSeed() {
	    	float pps = 29.28f;
	    	float divPS = 1.40f;
	    	
	    	Date date0 = (new GregorianCalendar(2008, 1, 28)).getTime();  // Actually: 2008, 2, 28
	    	Date date1 = (new GregorianCalendar(2009, 6, 3)).getTime();  // Actually: 2009, 7, 3
	    	Date date2 = (new GregorianCalendar(2010, 11, 7)).getTime();  // Actually: 2010, 12, 7
	    	
	    	BuyBlock bb0 = new BuyBlock(date0, 100.0f, 26.607f, 0.07f, divPS, Constants.ACCOUNT_JOINT);
	    	BuyBlock bb1 = new BuyBlock(date1, 100.0f, 23.848f, 0.07f, divPS, Constants.ACCOUNT_JOINT);
	    	BuyBlock bb2 = new BuyBlock(date2, 100.0f, 25.089f, 0.07f, divPS, Constants.ACCOUNT_JOINT);
	        
	    	StockQuote q = new StockQuote("VVC", pps, divPS, 25f);
	    	q.mFullName = "Vectren Corporation";
	    	q.mBuyBlockList.add(bb0);
	    	q.mBuyBlockList.add(bb1);
	    	q.mBuyBlockList.add(bb2);
	    	q.mAnalystsOpinion = 2.7f;
	    	return q;
	    }

//		private StockQuote createVZSeed() {
//			Date date0 = new Date(111,  2, 30);  // Actually: 2011,  3, 30
//			BuyBlock bb0 = new BuyBlock(date0, 700f, 38.82f, 0.08f, 0f, 0f);
//			StockQuote q = new StockQuote("VZ", 0f, 0f, 25f);
//			q.mBuyBlockList.add(bb0);
//			return q;
//		}
		
//		private StockQuote createWMSeed() {
//			Date date0 = new Date(112,  6, 11);  // Actually: 2012,  7, 11
//			BuyBlock bb0 = new BuyBlock(date0, 300f, 31.61f, 0.08f, 0f, 0f);
//			StockQuote q = new StockQuote("WM", 0f, 0f, 25f);
//			q.mBuyBlockList.add(bb0);
//			return q;
//		}
		
		
	    // Not a real symbol, but handy in some test situations.
//	    private StockQuote createFOOSeed() {
//	    	StockQuote q = new StockQuote("FOO", 19.47f, 20.00f, 0.01f, 30f, 18f);
//	    	return q;
//	    }
	    
  	    private StockQuote createNLYSeed() {
			return new StockQuote("NLY", 15.93f, 14.00f, 2.00f, 17.75f, 15.27f);
	    }
	    
	    private StockQuote createABTSeed() {
			return new StockQuote("ABT", 64.96f, 50.0f, 2.04f, 72.47f, 52.05f);
	    }
	    
//	    public  void testSeedData() {
//    	int numQuotes = mDbAdapter.getQuoteCount();            // 4
//    	int numBlocks = mDbAdapter.getBuyBlockCount();         // 5
//    	long row0 = mDbAdapter.fetchQuoteIdFromSymbol("VVC");  // 1
//    	long row1 = mDbAdapter.fetchQuoteIdFromSymbol("F");    // 2
//    	long row2 = mDbAdapter.fetchQuoteIdFromSymbol("AAA");  // -1 
//    	
//    	Cursor cursor0 = mDbAdapter.fetchAllQuoteRecords();       
//    	int idx = cursor0.getColumnIndex(DbAdapter.Q_FULL_NAME);  // 5
//    	String name0 = cursor0.getString(idx);                    // "Vectren Corporation"
//    	long vvcId = cursor0.getLong(0);                          // 1
//    	cursor0.moveToNext();                               
//    	String name1 = cursor0.getString(idx);                    // "Ford Motor Co."
//    	long fId = cursor0.getLong(0);                            // 2
//    	cursor0.close();
//    	
//    	Cursor cursor1 = mDbAdapter.fetchBuyBlockRecordsForThisSymbol("VVC");
//    	int num = cursor1.getCount();                                  // 3
//        int idx1 = cursor1.getColumnIndex(DbAdapter.B_DATE);           // 1
//        int idx2 = cursor1.getColumnIndex(DbAdapter.B_CHANGE_VS_BUY);  // 3
//        String dateString1 = cursor1.getString(idx1);                  // 2008-02-28
//        int chng1 = cursor1.getInt(idx2);                              // 100
//        cursor1.moveToNext();
//        String dateString2 = cursor1.getString(idx1);                  // 2009-08-03
//        int chng2 = cursor1.getInt(idx2);                              // 227
//        cursor1.moveToNext();
//        String dateString3 = cursor1.getString(idx1);                  // 2010-12-07
//        int chng3 = cursor1.getInt(idx2);                              // 167
//        boolean isNotDone = cursor1.moveToNext();                      // false
//    	cursor1.close();
//    	
//    	String sym1 = mDbAdapter.fetchQuoteSymbolFromId(vvcId);        // "VVC"
//    	String sym2 = mDbAdapter.fetchQuoteSymbolFromId(fId);          // "F"
//    	
//        long id = mDbAdapter.fetchQuoteIdFromSymbol(sym1);             // 1
//        Cursor cursor2 = mDbAdapter.fetchQuoteRecordFromId(id);        
//        String name3 = cursor2.getString(cursor2.getColumnIndex(DbAdapter.Q_FULL_NAME));  // "Vectren Corporation"
//    	Status status = mDbAdapter.getStatus(cursor2);                 // Status.OWNED
//        cursor2.close();
//        
//    	Date date1 = new Date(109, 7, 3);
//    	mDbAdapter.removeBuyBlockRecord("VVC", date1);
//    	int countQ = mDbAdapter.getQuoteCount();                      // 4
//    	int countB = mDbAdapter.getBuyBlockCount();                   // 4
//        
//    	long id3 = mDbAdapter.fetchQuoteIdFromSymbol("F");            // 2 
//    	Cursor cursor3 = mDbAdapter.fetchQuoteRecordFromId(id3);      // !null
//    	int idx3 = cursor3.getColumnIndex(DbAdapter.Q_CHANGE_VS_CLOSE);  // 2
//    	int chngVsCloseInt = cursor3.getInt(idx3);                     // 103
//    	cursor3.close();
//    	
//    	mDbAdapter.removeQuoteRecord("F");  // Removes the record for F. Only VVC remains.
//    	
//    	int countQ1 = mDbAdapter.getQuoteCount();                     // 3
//    	int countB1 = mDbAdapter.getBuyBlockCount();                  // 2
//    	
//    	float chng = mDbAdapter.getBestChangeSinceBuy("VVC");         // 16.7
//    	
//    	StockQuote diffQuote = createVVCSeed();                       // !null
//    	diffQuote.mBuyBlockList.remove(1); // Perform the same operations on this record as were performed on the old record.
//    	float lastClosePPS = diffQuote.mPPS;                          // 29.28
//    	diffQuote.mPPS = 89.27f;                                   
//    	diffQuote.compute(lastClosePPS);
//    	long id1 = mDbAdapter.fetchQuoteIdFromSymbol("VVC");          // 1
//    	mDbAdapter.changeQuoteRecord(id1, diffQuote);
//    	
//    	chng = mDbAdapter.getBestChangeSinceBuy("VVC");               // 255.8
//
//    	// Check fetchAllQuoteRecordsByStatus()
//    	Cursor cursor4 = mDbAdapter.fetchAllQuoteRecordsByStatus(Status.OWNED);  // !null
//    	int numOwnedQuotes = cursor4.getCount();                     // 1 
//    	cursor4.close();
//    	
//    	Cursor cursor5 = mDbAdapter.fetchAllQuoteRecordsByStatus(Status.WATCH);  // !null
//		int numWatchQuotes = cursor5.getCount();                    // 2
//		int idxSym = cursor5.getColumnIndex(DbAdapter.Q_SYMBOL);            // 2
//		int idxChng = cursor5.getColumnIndex(DbAdapter.Q_CHANGE_VS_CLOSE);  // 3
//		int idxStrike = cursor5.getColumnIndex(DbAdapter.Q_STRIKE);         // 11 
//			String sym3 = cursor5.getString(idxSym);                   // "NLY"
//			Status status3 = mDbAdapter.getStatus(cursor4);            // WATCH  
//			int sinceClose3 = cursor5.getInt(idxChng);                 //  1
//			int strike3 = cursor5.getInt(idxStrike);                   // 1400
//			cursor5.moveToNext();
//			String sym5 = cursor5.getString(idxSym);                   // "ABT"
//			Status status5 = mDbAdapter.getStatus(cursor4);            // WATCH
//			int sinceClose5 = cursor5.getInt(idxChng);                 //  -2
//			int strike5 = cursor5.getInt(idxStrike);                   // 5000
//		cursor5.close();
//
//	    	mDbAdapter.removeQuoteRecord("VVC");
//        Cursor cursor6 = mDbAdapter.fetchAllQuoteRecordsByStatus(Status.OWNED);  // !null
//        numOwnedQuotes = cursor6.getCount();                       // 0 
//    	cursor6.close();
//    	
//    	countQ1 = 0;
//    }
	    
}
