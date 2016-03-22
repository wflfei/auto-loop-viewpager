package com.wfl.autolooppager;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by wfl on 16/3/22.
 */
public class Utils {

    public static int dp2px(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (dp * displayMetrics.density + 0.5f);
    }
}
