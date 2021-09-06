package com.xingzhi.android.swipeback.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by xiedongdong on 2021/08/31
 */
public class ScreenUtils {

    private static final String BRAND = Build.BRAND.toLowerCase();

    public static boolean isXiaomi() {
        return Build.MANUFACTURER.toLowerCase().equals("xiaomi");
    }

    public static boolean isVivo() {
        return BRAND.contains("vivo");
    }

    public static boolean isOppo() {
        return BRAND.contains("oppo");
    }

    public static boolean isHuawei() {
        return BRAND.contains("huawei") || BRAND.contains("honor");
    }

    public static boolean isSamsung() {
        return BRAND.contains("samsung");
    }

    public static boolean isSmartisan() {
        return BRAND.contains("smartisan");
    }

    public static boolean isNokia() {
        return BRAND.contains("nokia");
    }

    public static int getRealScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getRealMetrics(dm);
        return dm.heightPixels;
    }

    public static int getRealScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getRealMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 判断设备是否显示NavigationBar
     *
     * @return 1 不显示（手势导航） 0 显示 2 未知
     */
    public static int isNavBarHide(Context context) {
        // 有虚拟键，判断是否显示
        if (isVivo()) {
            return vivoNavigationEnabled(context);
        }
        if (isOppo()) {
            return oppoNavigationEnabled(context);
        }
        if (isXiaomi()) {
            return xiaomiNavigationEnabled(context);
        }
        if (isHuawei()) {
            return huaWeiNavigationEnabled(context);
        }
        if (isSamsung()) {
            return samsungNavigationEnabled(context);
        }
        if (isSmartisan()) {
            return smartisanNavigationEnabled(context);
        }
        if (isNokia()) {
            return nokiaNavigationEnabled(context);
        }
        return 2;
    }

    /**
     * 判断当前系统是使用导航键还是手势导航操作
     *
     * @return 0 表示使用的是虚拟导航键，1 表示使用的是手势导航，默认是0
     */
    public static int vivoNavigationEnabled(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "navigation_gesture_on", 0);
    }

    public static int oppoNavigationEnabled(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "hide_navigationbar_enable", 0);
    }

    public static int xiaomiNavigationEnabled(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "force_fsg_nav_bar", 0);
    }

    private static int huaWeiNavigationEnabled(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "navigationbar_is_min", 0);
    }

    public static int samsungNavigationEnabled(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "navigationbar_hide_bar_enabled", 0);
    }

    public static int smartisanNavigationEnabled(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "navigationbar_trigger_mode", 0);
    }

    public static int nokiaNavigationEnabled(Context context) {
        boolean result = Settings.Secure.getInt(context.getContentResolver(), "swipe_up_to_switch_apps_enabled", 0) != 0
                || Settings.System.getInt(context.getContentResolver(), "navigation_bar_can_hiden", 0) != 0;

        if (result) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 虚拟键背景是否显示（虚拟按键 或者 全面屏手势提示线）
     */
    private static boolean isNavigationBarBackgroundShow(Activity activity) {
        View view = activity.findViewById(android.R.id.navigationBarBackground);
        if (view == null) {
            return false;
        }
        int visible = view.getVisibility();
        return visible != View.GONE && visible != View.INVISIBLE;
    }

    /**
     * 虚拟键高度（部分手机未显示，也可以获取到高度）
     */
    public static int getNavigationBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 获取虚拟按键高度，如果开启。
     */
    public static int getNavigationBarHeightIfRoom(Activity activity) {
        int isNavBarHide = isNavBarHide(activity);

        if (isNavBarHide == 0) {
            return getNavigationBarHeight(activity);
        } else if (isNavBarHide == 1) {
            // 全面屏，如果显示手势提示线，需要加上提示线高度（即 NavigationBar 高度，提示线是在NavigationBar中显示的）
            boolean isNavigationBarBackgroundShow = isNavigationBarBackgroundShow(activity);
            if (isNavigationBarBackgroundShow) {
                return getNavigationBarHeight(activity);
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    private static boolean isAllScreenDevice(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            // 7.0放开限制，7.0以下都不为全面屏
            return false;
        } else {
            int realWidth = getRealScreenWidth(context);
            int realHeight = getRealScreenHeight(context);

            float width;
            float height;
            if (realWidth < realHeight) {
                width = realWidth;
                height = realHeight;
            } else {
                width = realHeight;
                height = realWidth;
            }
            // Android中默认的最大屏幕纵横比为1.86
            return height / width >= 1.86f;
        }
    }

    /**
     * 获取去除导航栏高度的剩余高度（含状态栏）
     */
    public static int getScreenContentHeight(Context context) {
        if (isAllScreenDevice(context)) {

            int result = isNavBarHide(context);

            if (result == 0) {
                return getRealScreenHeight(context) - getNavigationBarHeight(context);
            } else if (result == 1) {
                return getRealScreenHeight(context);
            } else {
                // 未知
                return getScreenHeight(context);
            }
        } else {
            return getScreenHeight(context);
        }
    }

}
