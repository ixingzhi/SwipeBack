package com.xingzhi.android.swipeback.demo

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.xingzhi.android.swipeback.R
import com.xingzhi.android.swipeback.base.BaseActivity

/**
 * Created by xiedongdong on 2021/09/01
 */
class DemoTwoActivity : BaseActivity() {

    override val layoutId: Int
        get() = R.layout.activity_demo_two

    override fun initView(savedInstanceState: Bundle?, view: View?) {
        setImmersiveStatusBar(this, false, ContextCompat.getColor(this, R.color.color_000_black))
    }

    override fun initEvent() {
    }

    override fun initData() {
    }

}