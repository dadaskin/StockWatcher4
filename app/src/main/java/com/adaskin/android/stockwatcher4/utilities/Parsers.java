package com.adaskin.android.stockwatcher4.utilities;

import android.util.Log;

import com.adaskin.android.stockwatcher4.models.BuyBlock;
import com.adaskin.android.stockwatcher4.models.StockQuote;

import java.io.BufferedReader;
import java.io.IOException;


public class Parsers {

    private static final String nameStartFormat = "data-reactid=\"7\">%s - ";
    private static final String nameStop = "</h1>";
    private static final String badSymbolPattern = "<title>Symbol Lookup from Yahoo! Finance";
    private static final String ppsStart = "<!-- react-text: 15 -->";
    private static final String ppsStop = "<!--";
    private static final String divStart = "DIVIDEND_AND_YIELD-value";
    private static final String divStop = " (";
    private static final String rangeStart = "FIFTY_TWO_WK_RANGE-value";
    private static final String rangeStop = "</td>";
    private static final String opinionStart = "\"recommendationMean\":{\"raw\":";
    private static final String opinionStop = ",\"fmt\"";

    private static final int divStartOffset = 20;
    private static final int rangeStartOffset = 20;

    public static boolean parseYAHOOResponse(BufferedReader reader, StockQuote quote){
        String symbol = quote.mSymbol;

        String nameStart = String.format(nameStartFormat, symbol);

        boolean isMainInformationFound = false;
        boolean isOpinionFound = false;
        String line;
        quote.mFullName = "barf";
        try {
            while ((line = reader.readLine()) != null) {
                if (line.contains(badSymbolPattern)){
                    //Log.d("myTag", symbol + " is a bad symbol. *****");
                    return false;
                }

                if (line.contains(nameStart)) {
                    String remainder = findItemTrimString(line, nameStart, 0, nameStop, quote, 0);
                    remainder = findItemTrimString(remainder, ppsStart, 0, ppsStop, quote, 1);
                    remainder = findItemTrimString(remainder, ppsStart, 0, ppsStop, quote, 2);
                    remainder = findItemTrimString(remainder, rangeStart, rangeStartOffset, rangeStop, quote, 3);
                    findItemTrimString(remainder, divStart, divStartOffset, divStop, quote, 4);
                    isMainInformationFound = true;
                }

                if (line.contains(opinionStart)) {
                    int opinionStartIdx = line.indexOf(opinionStart);
                    if (opinionStartIdx != -1){
                        String sub = line.substring(opinionStartIdx + opinionStart.length());
                        int opinionStopIdx = sub.indexOf(opinionStop);
                        if (opinionStopIdx != -1) {
                            String opinionString = sub.substring(0, opinionStopIdx);
                            quote.mAnalystsOpinion = parseFloatOrNA(opinionString);
                            isOpinionFound = true;
                        }
                    }
                }

                if(isMainInformationFound && isOpinionFound)
                    break;
            }
            for (BuyBlock block : quote.mBuyBlockList) {
                block.mEffDivYield = quote.mDivPerShare/block.mBuyPPS * 100.0f;
            }
            quote.determineOverallAccountColor();
        } catch (IOException e) {
//            String exceptionMsg = "Reading line from BufferedReader failed:\n" + e.getMessage();
//            Log.d("myTag", exceptionMsg);
            e.printStackTrace();
        }
        return true;
    }

    private static String findItemTrimString(String input, String startPattern, int startOffset, String stopPattern, StockQuote quote, int itemNumber) {
        String remainder = input;
        int startIdx = input.indexOf(startPattern);
        if (startIdx != -1) {
            String sub = input.substring(startIdx + startPattern.length() + startOffset);
            int stopIdx = sub.indexOf(stopPattern);
            if (stopIdx != -1)
            {
                String itemString = sub.substring(0, stopIdx);
                remainder = sub.substring(stopIdx + stopPattern.length());
                switch(itemNumber)  {
                    case 0: quote.mFullName = itemString.replace("&amp;","&");
                        break;
                    case 1: quote.mPPS = parseFloatOrNA(itemString);
                        break;
                    case 2: // Do stuff involving previous value
                        float previousClose = parseFloatOrNA(itemString);
                        quote.compute(previousClose);
                        break;
                    case 3: // Parse range string, add pieces to quote
                        String yrMinString = itemString.substring(0, itemString.indexOf(" -"));
                        String yrMaxString = itemString.substring(itemString.indexOf("- ")+2, itemString.length());
                        quote.mYrMin = parseFloatOrNA(yrMinString);
                        quote.mYrMax = parseFloatOrNA(yrMaxString);
                        break;
                    case 4: quote.mDivPerShare = parseFloatOrNA(itemString);
                        break;
                }
            }
        }
        return remainder;
    }

    private static float parseFloatOrNA(String field) {
        float parsedFloat = 0.0f;
        if (!field.contains("N/A")) {
            parsedFloat = Float.valueOf(field.replace(",",""));
        }
        return parsedFloat;
    }
}
