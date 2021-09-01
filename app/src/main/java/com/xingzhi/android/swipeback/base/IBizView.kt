package com.xingzhi.android.swipeback.base

import android.os.Bundle
import android.view.View

/**
 * Created by xiedongdong on 2018/11/09.
 */
interface IBizView {

    val layoutId: Int

    fun initView(savedInstanceState: Bundle?, view: View?)

    fun initEvent()

    fun initData()

}
