package com.ly.traffic.app.libui.popwindow

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import com.ly.traffic.app.libui.popwindow.FastBlurHelper.doBlur
import com.ly.traffic.app.libui.popwindow.RenderScriptBlurHelper.doBlur

/**
 * Encapsulate the whole behaviour to provide a blur effect on a DialogFragment.
 *
 *
 * All the screen behind the dialog will be blurred except the action bar.
 *
 *
 * Simply linked all methods to the matching lifecycle ones.
 */
class BlurPopupEngine(
    private val mAnimationDuration: Int,//Duration used to animate in and out the blurred image.In milli.
    private var mContext: Context?,
    private val mBlurView: View,
    private val mBlurredBackgroundView: ImageView?//Image view used to display blurred background.
) {
    private val clipRect = Rect()

    /**
     * Layout params used to add blurred background.
     */
    private var mBlurredBackgroundLayoutParams: FrameLayout.LayoutParams? = null

    /**
     * Task used to capture screen and blur it.
     */
    private var mBluringTask: BlurAsyncTask? = null

    /**
     * Used to enable or disable debug mod.
     */
    private var mDebugEnable = false

    /**
     * Factor used to down scale background. High quality isn't necessary
     * since the background will be blurred.
     */
    private var mDownScaleFactor =
        DEFAULT_BLUR_DOWN_SCALE_FACTOR

    /**
     * Radius used for fast blur algorithm.
     */
    private var mBlurRadius = DEFAULT_BLUR_RADIUS

    /**
     * Boolean used to know if RenderScript should be used
     */
    private var mUseRenderScript = false

    /**
     * Resume the engine.
     *
     * @param retainedInstance use getRetainInstance.
     */
    fun onResume(retainedInstance: Boolean) {
        if (mBlurredBackgroundView == null || retainedInstance) {
            mBluringTask = BlurAsyncTask()
            mBluringTask!!.execute()
        }
    }

    /**
     * Must be linked to the original lifecycle.
     */
    @SuppressLint("NewApi")
    fun onDismiss() {
        //remove blurred background and clear memory, could be null if dismissed before blur effect
        //processing ends
        //cancel async task
        if (mBluringTask != null) {
            mBluringTask!!.cancel(true)
        }
        mBlurredBackgroundView?.animate()?.alpha(0f)?.setDuration(mAnimationDuration.toLong())
            ?.setInterpolator(AccelerateInterpolator())?.start()
    }

    /**
     * Must be linked to the original lifecycle.
     */
    fun onDestroy() {
        if (mBluringTask != null) {
            mBluringTask!!.cancel(true)
        }
        mBluringTask = null
        mContext = null
    }

    /**
     * Enable / disable debug mode.
     *
     *
     * LogCat and graphical information directly on blurred screen.
     *
     * @param enable true to display log in LogCat.
     */
    fun debug(enable: Boolean) {
        mDebugEnable = enable
    }

    /**
     * Apply custom down scale factor.
     *
     *
     * By default down scale factor is set to
     * [BlurPopupEngine.DEFAULT_BLUR_DOWN_SCALE_FACTOR]
     *
     *
     * Higher down scale factor will increase blurring speed but reduce final rendering quality.
     *
     * @param factor customized down scale factor, must be at least 1.0 ( no down scale applied )
     */
    fun setDownScaleFactor(factor: Float) {
        mDownScaleFactor = if (factor >= 1.0f) {
            factor
        } else {
            1.0f
        }
    }

    /**
     * Set use of RenderScript
     *
     *
     * By default RenderScript is set to
     * [BlurPopupEngine.DEFAULT_USE_RENDERSCRIPT]
     *
     *
     * Don't forget to add those lines to your build.gradle
     * <pre>
     * defaultConfig {
     * ...
     * renderscriptTargetApi 22
     * renderscriptSupportModeEnabled true
     * ...
     * }
    </pre> *
     *
     * @param useRenderScript use of RenderScript
     */
    fun setUseRenderScript(useRenderScript: Boolean) {
        mUseRenderScript = useRenderScript
    }

    /**
     * Apply custom blur radius.
     *
     *
     * By default blur radius is set to
     * [BlurPopupEngine.DEFAULT_BLUR_RADIUS]
     *
     * @param radius custom radius used to blur.
     */
    fun setBlurRadius(radius: Int) {
        mBlurRadius = if (radius >= 0) {
            radius
        } else {
            0
        }
    }

    fun setBlurRect(left: Int, top: Int, right: Int, bottom: Int) {
        clipRect[left, top, right] = bottom
    }

    /**
     * Blur the given bitmap and add it to the activity.
     *
     * @param bkg  should be a bitmap of the background.
     * @param view background view.
     */
    private fun blur(bkg: Bitmap?, view: View?): Bitmap? {
        val startMs = System.currentTimeMillis()
        //define layout params to the previous imageView in order to match its parent
        mBlurredBackgroundLayoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )

        //overlay used to build scaled preview and blur background
        var overlay: Bitmap? = null

        //add offset to the source boundaries since we don't want to blur actionBar pixels
        val srcRect = Rect(
            0,
            0,
            bkg!!.width,
            bkg.height
        )

        //in order to keep the same ratio as the one which will be used for rendering, also
        //add the offset to the overlay.
        val height =
            Math.ceil(bkg.height / mDownScaleFactor.toDouble())
        val width = Math.ceil(
            bkg.width * height
                    / bkg.height
        )

        // Render script doesn't work with RGB_565
        overlay = if (mUseRenderScript) {
            Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)
        } else {
            Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.RGB_565)
        }
        //scale and draw background view on the canvas overlay
        val canvas = Canvas(overlay)
        val paint = Paint()
        paint.flags = Paint.FILTER_BITMAP_FLAG

        //build drawing destination boundaries
        val destRect = RectF(0f, 0f, overlay.width.toFloat(), overlay.height.toFloat())

        //draw background from source area in source background to the destination area on the overlay
        canvas.drawBitmap(bkg, srcRect, destRect, paint)

        //apply fast blur on overlay
        overlay = if (mUseRenderScript) {
            doBlur(overlay, mBlurRadius, true, mContext)
        } else {
            doBlur(overlay, mBlurRadius, true)
        }
        if (mDebugEnable) {
            val blurTime = (System.currentTimeMillis() - startMs).toString() + " ms"
            Log.d(
                TAG,
                "Blur method : " + if (mUseRenderScript) "RenderScript" else "FastBlur"
            )
            Log.d(TAG, "Radius : $mBlurRadius")
            Log.d(
                TAG,
                "Down Scale Factor : $mDownScaleFactor"
            )
            Log.d(TAG, "Blurred achieved in : $blurTime")
            Log.d(
                TAG,
                "Allocation : " + bkg.rowBytes + "ko (screen capture) + "
                        + overlay!!.rowBytes + "ko (blurred bitmap)"
                        + if (!mUseRenderScript) " + temp buff " + overlay.rowBytes + "ko." else "."
            )
            val bounds = Rect()
            val canvas1 = Canvas(overlay)
            paint.color = Color.BLACK
            paint.isAntiAlias = true
            paint.textSize = 20.0f
            paint.getTextBounds(blurTime, 0, blurTime.length, bounds)
            canvas1.drawText(blurTime, 2f, bounds.height().toFloat(), paint)
        }
        return overlay
    }

    /**
     * Used to check if the status bar is translucent.
     *
     * @return true if the status bar is translucent.
     */
    @get:TargetApi(Build.VERSION_CODES.KITKAT)
    private val isStatusBarTranslucent: Boolean
        get() {
            val typedValue = TypedValue()
            val attribute = intArrayOf(android.R.attr.windowTranslucentStatus)
            val array = mContext!!.obtainStyledAttributes(typedValue.resourceId, attribute)
            val isStatusBarTranslucent = array.getBoolean(0, false)
            array.recycle()
            return isStatusBarTranslucent
        }

    /**
     * Async task used to process blur out of ui thread
     */
    private inner class BlurAsyncTask : AsyncTask<Void?, Void?, Bitmap?>() {

        private var mBackground: Bitmap? = null
        private var mBackgroundView: View? = null

        override fun onPreExecute() {
            super.onPreExecute()
            mBackgroundView = mBlurView

            //retrieve background view, must be achieved on ui thread since
            //only the original thread that created a view hierarchy can touch its views.
            val rect = Rect()
            mBackgroundView!!.getWindowVisibleDisplayFrame(rect)
            mBackgroundView!!.destroyDrawingCache()
            mBackgroundView!!.isDrawingCacheEnabled = true
            mBackgroundView!!.buildDrawingCache(true)
            mBackground = mBackgroundView!!.getDrawingCache(true)
            mBackground = Bitmap.createBitmap(
                mBackground!!, clipRect.left, clipRect.top,
                clipRect.width(), clipRect.height(), null, false
            )
            /**
             * After rotation, the DecorView has no height and no width. Therefore
             * .getDrawingCache() return null. That's why we  have to force measure and layout.
             */
            if (mBackground == null) {
                mBackgroundView!!.measure(
                    View.MeasureSpec.makeMeasureSpec(
                        rect.width(),
                        View.MeasureSpec.EXACTLY
                    ),
                    View.MeasureSpec.makeMeasureSpec(
                        rect.height(),
                        View.MeasureSpec.EXACTLY
                    )
                )
                mBackgroundView!!.layout(
                    0, 0, mBackgroundView!!.measuredWidth,
                    mBackgroundView!!.measuredHeight
                )
                mBackgroundView!!.destroyDrawingCache()
                mBackgroundView!!.isDrawingCacheEnabled = true
                mBackgroundView!!.buildDrawingCache(true)
                mBackground = mBackgroundView!!.getDrawingCache(true)
                mBackground = Bitmap.createBitmap(
                    mBackground!!, clipRect.left, clipRect.top,
                    clipRect.width(), clipRect.height(), null, false
                )
            }
        }

        override fun doInBackground(vararg params: Void?): Bitmap? {
            //process to the blue
            val blur: Bitmap? = if (!isCancelled) {
                blur(mBackground, mBackgroundView)
            } else {
                return null
            }
            //clear memory
            mBackground!!.recycle()
            return blur
        }

        @SuppressLint("NewApi")
        override fun onPostExecute(bitmap: Bitmap?) {
            super.onPostExecute(bitmap)
            if (bitmap != null) {
                mBlurredBackgroundView!!.scaleType = ImageView.ScaleType.CENTER_CROP
                mBlurredBackgroundView.setImageDrawable(
                    BitmapDrawable(
                        mContext!!.resources,
                        bitmap
                    )
                )
            }
            mBackgroundView!!.destroyDrawingCache()
            mBackgroundView!!.isDrawingCacheEnabled = false
            mBlurredBackgroundView!!.alpha = 0f
            mBlurredBackgroundView
                .animate()
                .alpha(1f)
                .setDuration(mAnimationDuration.toLong())
                .setInterpolator(LinearInterpolator())
                .start()
            mBackgroundView = null
            mBackground = null
        }
    }

    companion object {
        /**
         * Since image is going to be blurred, we don't care about resolution.
         * Down scale factor to reduce blurring time and memory allocation.
         */
        const val DEFAULT_BLUR_DOWN_SCALE_FACTOR = 4.0f

        /**
         * Radius used to blur the background
         */
        const val DEFAULT_BLUR_RADIUS = 8

        /**
         * Default dimming policy.
         */
        const val DEFAULT_DIMMING_POLICY = false

        /**
         * Default debug policy.
         */
        const val DEFAULT_DEBUG_POLICY = false

        /**
         * Default action bar blurred policy.
         */
        const val DEFAULT_ACTION_BAR_BLUR = false

        /**
         * Default use of RenderScript.
         */
        const val DEFAULT_USE_RENDERSCRIPT = false

        /**
         * Log cat
         */
        private val TAG = BlurPopupEngine::class.java.simpleName
    }

}