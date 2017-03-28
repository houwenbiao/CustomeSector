package com.gree.customsector.Util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by JackHou on 2017/3/24.
 */

public class Tools
{
    private static final boolean TAG = true;

    /**
     * Log输出
     * @param tag
     * @param msg
     */
    public static void ShowLog(String tag, String msg)
    {
        if(TAG)
        {
            Log.i(tag, msg);
        }
    }

    /**
     * Toast显示
     * @param context
     * @param msg
     * @param isLongTime
     */
    public static void showToast(Context context, String msg, boolean isLongTime)
    {
        if(isLongTime)
        {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * dp转换成px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp)
    {
        //获取屏幕密度
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * px转换成dp
     *
     * @param context
     * @param px
     * @return
     */
    public static int px2dp(Context context, float px)
    {
        //获取屏幕密度
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5);
    }

    /**
     * 获取屏幕尺寸
     *
     * @param context
     * @param which
     * @return
     */
    public static int getScreenWidthOrHeight(Context context, String which)
    {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        ShowLog("zxxx", "screenWidth:" + screenWidth + " screenHeight:" + screenHeight);
        if(which.equals("height"))
        {
            return screenHeight;
        }
        else if(which.equals("width"))
        {
            return screenWidth;
        }
        else
        {
            return Log.e("zxx", "获取失败which需要是height/width");
        }
    }

}
