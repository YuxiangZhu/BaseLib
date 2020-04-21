package com.ly.traffic.app.libui.popwindow

import android.app.Activity
import android.content.Context
import android.view.WindowManager

/**
 * @author BakerJ
 */
object Utils {

    @JvmStatic
    fun isFullScreen(activity: Activity): Boolean {
        return (WindowManager.LayoutParams.FLAG_FULLSCREEN and activity.window
            .attributes.flags == WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    @JvmStatic
    fun isTranslucentStatusBar(activity: Activity): Boolean {
        return WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS and activity.window
            .attributes.flags == WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
    }

    @JvmStatic
    fun getStatusBarHeight(context: Context): Float {
        val heightResId =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return (if (heightResId > 0) context.resources.getDimensionPixelSize(heightResId) else 0).toFloat()
    }
}