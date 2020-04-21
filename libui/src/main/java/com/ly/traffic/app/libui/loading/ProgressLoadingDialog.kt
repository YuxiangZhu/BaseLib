package com.ly.traffic.app.libui.loading

import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.fragment.app.DialogFragment
import com.ly.traffic.app.libui.databinding.FragmentProgressLoadingBinding

/**
 * 作者： Alex
 * 日期： 2020-04-13
 * 签名： 摊牌了，我是沙雕
 *                    .::::.
 *                  .::::::::.
 *                 :::::::::::
 *             ..:::::::::::'
 *           '::::::::::::'
 *             .::::::::::
 *        '::::::::::::::..
 *             ..::::::::::::.
 *           ``::::::::::::::::
 *            ::::``:::::::::'        .:::.
 *           ::::'   ':::::'       .::::::::.
 *         .::::'      ::::     .:::::::'::::.
 *        .:::'       :::::  .:::::::::' ':::::.
 *       .::'        :::::.:::::::::'      ':::::.
 *      .::'         ::::::::::::::'         ``::::.
 *  ...:::           ::::::::::::'              ``::.
 * ```` ':.          ':::::::::'                  ::::..
 *                    '.:::::'                    ':'````..
 * ----------------------------------------------------------------
 *带进度条的dialog,适用于升级，下载等
 */
class ProgressLoadingDialog : DialogFragment() {

    val obMsg = ObservableField<String>("正在加载...")
    val obProgress = ObservableInt(0)
    private lateinit var binding: FragmentProgressLoadingBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        obMsg.set(arguments?.getString(KEY_MSG))
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    /**
     * 设置dialog宽度为屏幕的的百分比
     */
    override fun onStart() {
        super.onStart()

        dialog?.let {
            val displayMetrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            it.window?.setLayout(
                (displayMetrics.widthPixels * 0.85).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProgressLoadingBinding.inflate(inflater)
        binding.msg = obMsg
        binding.progress = obProgress

        dialog?.setCanceledOnTouchOutside(false)//点击屏幕其他地方不消失
        dialog?.setOnKeyListener(object : DialogInterface.OnKeyListener {//点击返回键不消失
            override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {

                if(keyCode==KeyEvent.KEYCODE_BACK){

                    return true
                }
                return false

            }


        })

        return binding.root
    }


    /**
     * 更新进度条
     */
    fun setProgress(p: Int) {
        obProgress.set(p)
    }

    companion object {

        internal const val KEY_MSG = "msg"//用于显示的提示信息
    }

}