package com.xingzhi.android.swipeback.demo

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.xingzhi.android.swipeback.R
import com.xingzhi.android.swipeback.base.BaseActivity
import com.xingzhi.android.swipeback.utils.StatusBarUtils
import kotlinx.android.synthetic.main.activity_demo_three.*

/**
 * Created by xiedongdong on 2021/09/01
 */
class DemoThreeActivity : BaseActivity() {

    override val layoutId: Int
        get() = R.layout.activity_demo_three

    override fun initView(savedInstanceState: Bundle?, view: View?) {
        setStatusBarVisibility(View.GONE)

        val statusBarHeight = StatusBarUtils.getStatusBarHeight(this)
        val params = mTitleBar.layoutParams as ConstraintLayout.LayoutParams
        params.setMargins(0, statusBarHeight, 0, 0)
    }

    override fun initEvent() {
    }

    override fun initData() {
    }

}