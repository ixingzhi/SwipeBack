package com.xingzhi.android.swipeback.demo

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.xingzhi.android.swipeback.R
import com.xingzhi.android.swipeback.base.BaseActivity

/**
 * Created by xiedongdong on 2021/09/01
 */
class DemoOneActivity : BaseActivity() {

    override val layoutId: Int
        get() = R.layout.activity_demo_one

    override fun initView(savedInstanceState: Bundle?, view: View?) {
        setImmersiveStatusBar(this, true, ContextCompat.getColor(this, R.color.color_base_bg3))
    }

    override fun initEvent() {
    }

    override fun initData() {
    }

}