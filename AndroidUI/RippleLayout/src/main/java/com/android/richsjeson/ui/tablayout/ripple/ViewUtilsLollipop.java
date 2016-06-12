package com.android.richsjeson.ui.tablayout.ripple;

import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * Created by richsjeson on 2016/6/2.
 */
public class ViewUtilsLollipop {
    static void setBoundsViewOutlineProvider(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setOutlineProvider(ViewOutlineProvider.BOUNDS);
        }
    }
}
