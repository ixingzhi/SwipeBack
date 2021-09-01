package com.xingzhi.android.swipeback.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import kotlin.Deprecated;

/**
 * Created by xiedongdong on 2021/08/31
 */
public class KeyBoardUtils {
    /**
     * 隐藏软键盘
     *
     * @param activity 当前activity
     */
    public static void alwaysHideSoftKeyboard(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * 隐藏键盘
     *
     * @param context 上下文
     * @param view    输入焦点view
     */
    public static void hideSoftKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 弹出键盘
     *
     * @param context 上下文
     * @param view    输入焦点view
     */
    public static void showSoftKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            view.requestFocus();
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * 强制隐藏输入法
     *
     * @param context
     * @param editText
     */
    private void closeInputMethod(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            boolean isOpen = imm.isActive();
            if (isOpen) {
                imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 判断软键盘是否弹出
     */
    @Deprecated(message = "判断不准确，弃用。")
    public static boolean isShowKeyboard(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.hideSoftInputFromWindow(v.getWindowToken(), 0)) {
            imm.showSoftInput(v, 0);
            // 软键盘已弹出
            return true;
        } else {
            // 软键盘未弹出
            return false;
        }
    }

    public static boolean isShowKeyboard(Activity activity) {
        // 获取当前屏幕内容的高度
        int screenHeight = activity.getWindow().getDecorView().getHeight();
        // 获取View可见区域的bottom
        Rect rect = new Rect();
        // DecorView即为activity的顶级view
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        // 考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        // 选取 screenHeight * 2 / 3 进行判断
        int navigationBarHeight = (NavigationBarUtils.getNavigationBarHeightIfRoom(activity));

        return (screenHeight - navigationBarHeight) * 2 / 3 > rect.bottom;
    }

}
