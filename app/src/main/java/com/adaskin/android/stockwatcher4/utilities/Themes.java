package com.adaskin.android.stockwatcher4.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.widget.TextView;

import com.adaskin.android.stockwatcher4.R;

public class Themes {

    public static void adjustBuyBlockTextColor(Context context, TextView view, float value, float gainThreshold) {
        Resources resources = context.getResources();
        int normalBackgroundColor = resources.getColor(R.color.list_background_color);
        int highlightBackgroundColor = resources.getColor(R.color.over_gain_target_color);

        int positiveTextColor = resources.getColor(R.color.positive_text_color);
        int neutralTextColor = resources.getColor(R.color.neutral_text_color);
        int negativeTextColor = resources.getColor(R.color.negative_text_color);

        if (value > Constants.POSITIVE_ONE_DECIMAL_LIMIT) {
            view.setTextColor(positiveTextColor);
        } else if (value < Constants.NEGATIVE_ONE_DECIMAL_LIMIT) {
            view.setTextColor(negativeTextColor);
        } else {
            view.setTextColor(neutralTextColor);
        }

        view.setBackgroundColor(normalBackgroundColor);
        if (value > gainThreshold) {
            view.setBackgroundColor(highlightBackgroundColor);
            view.setTextColor(neutralTextColor);
        }
    }

    public static void adjustOverallTextColor(Context context, TextView view, float value) {
        Resources resources = context.getResources();

        int backgroundColor = resources.getColor(R.color.view_background_color);
        view.setBackgroundColor(backgroundColor);

        int positiveTextColor = resources.getColor(R.color.positive_text_color);
        int neutralTextColor = resources.getColor(R.color.neutral_text_color);
        int negativeTextColor = resources.getColor(R.color.negative_text_color);

        if (value > Constants.POSITIVE_ONE_DECIMAL_LIMIT) {
            view.setTextColor(positiveTextColor);
        } else if (value < Constants.NEGATIVE_ONE_DECIMAL_LIMIT) {
            view.setTextColor(negativeTextColor);
        } else {
            view.setTextColor(neutralTextColor);
        }
    }
}
