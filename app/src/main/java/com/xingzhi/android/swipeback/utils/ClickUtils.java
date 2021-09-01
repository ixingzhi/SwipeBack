package com.xingzhi.android.swipeback.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xiedongdong on 2021/08/31
 */
public class ClickUtils {

    private static final int MIN_DELAY_TIME = 800;  // 两次点击间隔不能少于800ms
    private static int CUSTOM_MIN_DELAY_TIME = 2000; // 自定义点击时间
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    public static boolean isFastClick(int delayTime) {
        CUSTOM_MIN_DELAY_TIME = delayTime;
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= CUSTOM_MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    /**
     * @param handler           外界handler(为了减少handler的泛滥使用,最好全局传handler引用,如果没有就直接传 new Handler())
     * @param longClickView     被长按的视图(任意控件)
     * @param delayMillis       长按时间,毫秒
     * @param longClickListener 长按回调的返回事件
     */
    public static void setLongClick(final Handler handler, final View longClickView, final long delayMillis, final View.OnLongClickListener longClickListener) {
        longClickView.setOnTouchListener(new View.OnTouchListener() {
            private int TOUCH_MAX = 50;
            private int mLastMotionX;
            private int mLastMotionY;
            private final Runnable r = () -> {
                if (longClickListener != null) {
                    longClickListener.onLongClick(longClickView);
                }
            };

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacks(r);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs(mLastMotionX - x) > TOUCH_MAX
                                || Math.abs(mLastMotionY - y) > TOUCH_MAX) {
                            handler.removeCallbacks(r);
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                        handler.removeCallbacks(r);
                        mLastMotionX = x;
                        mLastMotionY = y;
                        handler.postDelayed(r, delayMillis);
                        break;
                }
                return true;
            }
        });
    }

}
