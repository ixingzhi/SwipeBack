package com.xingzhi.android.swipeback.swipeback;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.xingzhi.android.swipeback.utils.ScreenUtils;


/**
 * 解决全屏Activity的键盘遮挡输入框，Fullscreen遮挡EditText，全屏输入框bug
 * AndroidBug5497Workaround，沉浸式输入框
 * For more information, see https://issuetracker.google.com/issues/36911528
 * To use this class, simply invoke assistActivity() on an Activity that already has its content view set.
 */
public class AndroidBug5497Workaround {

    private Activity mActivity;
    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;

    protected void addOnGlobalLayoutListener(Activity activity) {
        this.mActivity = activity;
        FrameLayout content = mActivity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(mListener);
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    protected void removeOnGlobalLayoutListener() {
        if (mChildOfContent != null) {
            mChildOfContent.getViewTreeObserver().removeOnGlobalLayoutListener(mListener);
        }
    }

    private final ViewTreeObserver.OnGlobalLayoutListener mListener = this::possiblyResizeChildOfContent;

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight() - ScreenUtils.getNavigationBarHeightIfRoom(mActivity);
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
            } else {
                // keyboard probably just became hidden
                frameLayoutParams.height = usableHeightSansKeyboard;
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        // 因为是沉浸式显示，不需要减去top
        //return (r.bottom - r.top);
        return (r.bottom);
    }

}
