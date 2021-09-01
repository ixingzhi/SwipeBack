package com.xingzhi.android.swipeback.base

import android.os.Bundle
import android.view.LayoutInflater
import com.xingzhi.android.swipeback.swipeback.SwipeBackActivity

/**
 * Created by xiedongdong on 2021/08/31
 */
abstract class BaseActivity : SwipeBackActivity(), IBizView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mContentView = LayoutInflater.from(this).inflate(layoutId, null)
        mFrameLayoutContent.addView(mContentView)
        initView(savedInstanceState, mContentView)
        initEvent()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
