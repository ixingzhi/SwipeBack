package com.xingzhi.android.swipeback

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.xingzhi.android.swipeback.base.BaseActivity
import com.xingzhi.android.swipeback.demo.DemoOneActivity
import com.xingzhi.android.swipeback.demo.DemoThreeActivity
import com.xingzhi.android.swipeback.demo.DemoTwoActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by xiedongdong on 2021/08/31
 */
class MainActivity : BaseActivity() {

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?, view: View?) {
        setSwipeBackEnable(false)
    }

    override fun initEvent() {
        mTvDemoOne.setOnClickListener {
            startActivity(Intent(this, DemoOneActivity::class.java))
        }

        mTvDemoTwo.setOnClickListener {
            startActivity(Intent(this, DemoTwoActivity::class.java))
        }

        mTvDemoThree.setOnClickListener {
            startActivity(Intent(this, DemoThreeActivity::class.java))
        }
    }

    override fun initData() {
    }

}