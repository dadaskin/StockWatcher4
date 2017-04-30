package com.adaskin.android.stockwatcher4.database;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.adaskin.android.stockwatcher4.utilities.Constants;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DbTable {

	private static final String LAST_UPDATE_TABLE_CREATE =
		"create table " + DbAdapter.LAST_UPDATE_TABLE + " (" +	
	    DbAdapter.U_ROW_ID   + " integer primary key autoincrement, " +
		DbAdapter.U_DATE     + " text not null, " +
	    DbAdapter.U_TIME     + " text not null);";
		
	private static final String QUOTE_TABLE_CONTENTS =
	    " (" +
		DbAdapter.Q_ROW_ID            + " integer primary key autoincrement, " +
		DbAdapter.Q_SYMBOL            + " text not null, " +
		DbAdapter.Q_CHANGE_VS_CLOSE   + " real, " +
		DbAdapter.Q_DIV_SHARE         + " real, " +
		DbAdapter.Q_FULL_NAME         + " text, " +
		DbAdapter.Q_GAIN_TARGET       + " real, " +
		DbAdapter.Q_OPINION           + " real, " +
		DbAdapter.Q_PPS               + " real, " +
		DbAdapter.Q_STATUS            + " text, " +
		DbAdapter.Q_STRIKE            + " real, " +
		DbAdapter.Q_CHANGE_VS_BUY     + " real, " +
		DbAdapter.Q_ACCOUNT_COLOR     + " integer  default " +  Constants.ACCOUNT_UNKNOWN + ", " +     
		DbAdapter.Q_YR_MAX            + " real, " +
		DbAdapter.Q_YR_MIN            + " real"   +
		");"; 

	private static final String QUOTE_TABLE_CREATE = "create table " + DbAdapter.QUOTE_TABLE + QUOTE_TABLE_CONTENTS;

	private static final String TEMP_QUOTE_TABLE_CREATE = "CREATE TABLE temp_quote" + QUOTE_TABLE_CONTENTS;
			
	private static final String BUY_BLOCK_TABLE_CONTENTS =
		" (" +
		DbAdapter.B_ROW_ID          + " integer primary key autoincrement, " +
		DbAdapter.B_CHANGE_VS_BUY   + " real, " +
		DbAdapter.B_COMM_SHARE      + " real, " +
		DbAdapter.B_DATE            + " text, " +
		DbAdapter.B_EFF_YIELD       + " real, " +
		DbAdapter.B_NUM_SHARES      + " real, " +
		DbAdapter.B_PPS             + " real, " +
		DbAdapter.B_ACCOUNT         + " integer default " +  Constants.ACCOUNT_UNKNOWN + ", " +  
		DbAdapter.B_PARENT          + " integer not null, " +
		" foreign key (" + DbAdapter.B_PARENT +  ") references " + 
		DbAdapter.QUOTE_TABLE +   "(" + DbAdapter.Q_ROW_ID + ")" +
		");";		
	
	private static final String BUY_BLOCK_TABLE_CREATE = "create table " + DbAdapter.BUY_BLOCK_TABLE + BUY_BLOCK_TABLE_CONTENTS;
	
    private static final String  TEMP_BLOCK_TABLE_CREATE = "CREATE TABLE temp_blocks" + BUY_BLOCK_TABLE_CONTENTS;
	
	// Before we insert something into the BuyBlock table check that the BuyBlock's parent
	// field has been set to one of the primary key of one of the quotes.  
	private static final String DB_TRIGGER = 
		"create trigger " + DbAdapter.DB_TRIGGER_NAME +
		" before insert on " + DbAdapter.BUY_BLOCK_TABLE +
		" for each row begin" +
		" select case when ((select " + DbAdapter.Q_ROW_ID +
		" from " + DbAdapter.QUOTE_TABLE +
		" where " + DbAdapter.Q_ROW_ID +
		"= new." + DbAdapter.B_PARENT + " ) is null)" +
		" then raise (abort, 'Foreign Key Violation') end;" +
		" end;";
	
	private static final String DB_BLOCK_VIEW_CREATE =
		"create view " + DbAdapter.DB_VIEW_NAME +
		" as select " + DbAdapter.BUY_BLOCK_TABLE + "." + DbAdapter.B_ROW_ID + " as _id," +
		" " + DbAdapter.BUY_BLOCK_TABLE + "." + DbAdapter.B_DATE + "," +
		" " + DbAdapter.BUY_BLOCK_TABLE + "." + DbAdapter.B_NUM_SHARES + "," +
		" " + DbAdapter.BUY_BLOCK_TABLE + "." + DbAdapter.B_CHANGE_VS_BUY + "," +
		" " + DbAdapter.BUY_BLOCK_TABLE + "." + DbAdapter.B_COMM_SHARE + "," +
		" " + DbAdapter.BUY_BLOCK_TABLE + "." + DbAdapter.B_EFF_YIELD + "," +
		" " + DbAdapter.BUY_BLOCK_TABLE + "." + DbAdapter.B_PPS + "," +
		" " + DbAdapter.BUY_BLOCK_TABLE + "." + DbAdapter.B_ACCOUNT + "," +   // New
		" " + DbAdapter.QUOTE_TABLE + "." + DbAdapter.Q_SYMBOL + 
        " from " + DbAdapter.BUY_BLOCK_TABLE +
        " join " + DbAdapter.QUOTE_TABLE +
            " on " + DbAdapter.BUY_BLOCK_TABLE + "." + DbAdapter.B_PARENT +
            " =" + DbAdapter.QUOTE_TABLE + "." + DbAdapter.Q_ROW_ID ;
 	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(LAST_UPDATE_TABLE_CREATE);
		database.execSQL(QUOTE_TABLE_CREATE);
		database.execSQL(BUY_BLOCK_TABLE_CREATE);
		database.execSQL(DB_TRIGGER);
		database.execSQL(DB_BLOCK_VIEW_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldRev, int newRev)
	{
		if (oldRev < newRev) {
           database.beginTransaction();

           String sql1 = "DROP VIEW IF EXISTS " + DbAdapter.DB_VIEW_NAME;
  		   database.execSQL(sql1);
    		
    	   String sql2 = "DROP TRIGGER IF EXISTS " + DbAdapter.DB_TRIGGER_NAME;
   		   database.execSQL(sql2);
    		
   		   database.execSQL(TEMP_QUOTE_TABLE_CREATE);
   		   database.execSQL(TEMP_BLOCK_TABLE_CREATE);
    		
   		   List<String> newQuoteColumns = getColumns(database, "temp_quote");
    	   List<String> newBlockColumns = getColumns(database, "temp_blocks");
    		
           newQuoteColumns.retainAll(getColumns(database, DbAdapter.QUOTE_TABLE));
           newBlockColumns.retainAll(getColumns(database, DbAdapter.BUY_BLOCK_TABLE));
            
           String colStringQ = join(newQuoteColumns, ",");
           String colStringB = join(newBlockColumns, ",");
            
           String sql3 = "INSERT INTO temp_quote (" + colStringQ + ") SELECT " + colStringQ + " FROM " + DbAdapter.QUOTE_TABLE;
   		   database.execSQL(sql3);
     		
            String sql4 = "INSERT INTO temp_blocks (" + colStringB + ") SELECT " + colStringB + " FROM " + DbAdapter.BUY_BLOCK_TABLE;
    		database.execSQL(sql4);
     		
            String sql5a = "DROP TABLE IF EXISTS "  + DbAdapter.QUOTE_TABLE;
    		database.execSQL(sql5a);
      		
            String sql5b = "DROP TABLE IF EXISTS "  + DbAdapter.BUY_BLOCK_TABLE;
    		database.execSQL(sql5b);
            
            String sql6 = "ALTER TABLE temp_quote RENAME TO " + DbAdapter.QUOTE_TABLE;
    		database.execSQL(sql6);
            
            String sql7 = "ALTER TABLE temp_blocks RENAME TO " + DbAdapter.BUY_BLOCK_TABLE;
      		database.execSQL(sql7);
    		
    		database.execSQL(DB_TRIGGER);
    		database.execSQL(DB_BLOCK_VIEW_CREATE);
    		
            database.setTransactionSuccessful();
            database.endTransaction();
 		}
	}

	public static List<String> getColumns(SQLiteDatabase db, String tableName) {
		List<String> ar = null;
		Cursor c = null;
		try {
			c = db.rawQuery("select * from " + tableName + " limit 1", null);
			if (c != null) {
				ar = new ArrayList<String>(Arrays.asList(c.getColumnNames()));
			}
		} catch (Exception e) {
			Log.v(tableName, e.getMessage(), e);
			e.printStackTrace();
		} finally {
			if (c != null)
				c.close();
		}
		return ar;
	}
	
	public static String join(List<String> list, String delimiter) {
		StringBuilder buf = new StringBuilder();
		int num = list.size();
		for (int i = 0; i < num; i++) {
			if (i != 0)
				buf.append(delimiter);
			buf.append((String)list.get(i));
		}
		return buf.toString();
	}
	
}
