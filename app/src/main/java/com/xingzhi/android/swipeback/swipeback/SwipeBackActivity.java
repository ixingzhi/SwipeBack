
package com.xingzhi.android.swipeback.swipeback;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.xingzhi.android.swipeback.R;
import com.xingzhi.android.swipeback.utils.KeyBoardUtils;
import com.xingzhi.android.swipeback.utils.OSUtils;
import com.xingzhi.android.swipeback.utils.StatusBarUtils;
import com.xingzhi.android.swipeback.utils.UIUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by xiedongdong on 2019/4/23
 */
public class SwipeBackActivity extends AppCompatActivity implements SwipeBackActivityBase
        , SwipeBackLayout.SwipeListener {
    private static final String TAG = SwipeBackActivity.class.getSimpleName();

    private View mViewStatusBar;
    protected FrameLayout mFrameLayoutContent;
    private SwipeBackActivityHelper mHelper;

    private AndroidBug5497Workaround mAndroidBug5497Workaround;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 透明屏幕只有在Android8.0中有此Bug，"Only fullscreen activities can request orientation"
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            boolean result = fixOrientation();
            Log.i(TAG, "onCreate fixOrientation when Oreo, result = " + result);
        }

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_frame_layout_context);

        // 解决透明屏幕EditText显示问题
        mAndroidBug5497Workaround = new AndroidBug5497Workaround();
        mAndroidBug5497Workaround.addOnGlobalLayoutListener(this);

        mViewStatusBar = findViewById(R.id.view_status_bar);
        mFrameLayoutContent = findViewById(R.id.frame_layout_content);

        ViewGroup.LayoutParams params = mViewStatusBar.getLayoutParams();
        params.height = StatusBarUtils.getStatusBarHeight(this);
        mViewStatusBar.setLayoutParams(params);

        setImmersiveStatusBar(this, true, ContextCompat.getColor(this, R.color.color_base_bg6));

        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        mHelper.getSwipeBackLayout().addSwipeListener(this);
        mHelper.getSwipeBackLayout().setEdgeSize(UIUtils.dp2px(this, 15));
    }

    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            return;
        }
        super.setRequestedOrientation(requestedOrientation);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    @Override
    public void onScrollStateChange(int state, float scrollPercent) {
    }

    @Override
    public void onEdgeTouch(int edgeFlag) {
        try {
            View view = getWindow().peekDecorView();
            if (KeyBoardUtils.isShowKeyboard(this)) {
                KeyBoardUtils.hideSoftKeyboard(this, view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onScrollOverThreshold() {
    }

    /**
     * 设置沉浸式状态栏
     *
     * @param fontIconDark 状态栏字体和图标颜色是否为深色
     */
    public void setImmersiveStatusBar(Activity activity, boolean fontIconDark, int statusBarColor) {
        StatusBarUtils.setTranslucentStatus(activity);
        if (fontIconDark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || OSUtils.isMIUI() || OSUtils.isFlyme()) {
                StatusBarUtils.setStatusBarFontIconDark(activity, true);
            } else {
                if (statusBarColor == Color.WHITE) {
                    statusBarColor = 0xffcccccc;
                }
            }
        }
        setStatueBarColor(statusBarColor);
    }

    /**
     * 设置状态栏是否显示
     */
    public void setStatusBarVisibility(int visibility) {
        mViewStatusBar.setVisibility(visibility);
    }

    private void setStatueBarColor(int statusBarColor) {
        if (mViewStatusBar != null) {
            mViewStatusBar.setBackgroundColor(statusBarColor);
        }
    }

    /**
     * 如果Activity中有OnGlobalLayoutListener，需要取消，不然会冲突
     */
    protected void requestInterceptParentGlobalLayoutListener() {
        if (mAndroidBug5497Workaround != null) {
            mAndroidBug5497Workaround.removeOnGlobalLayoutListener();
        }
    }

    @Override
    protected void onDestroy() {
        mViewStatusBar = null;
        mFrameLayoutContent = null;
        mHelper = null;
        mAndroidBug5497Workaround = null;
        super.onDestroy();
    }

}
