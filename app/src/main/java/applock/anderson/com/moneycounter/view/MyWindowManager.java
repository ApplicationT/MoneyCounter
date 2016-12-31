package applock.anderson.com.moneycounter.view;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import applock.anderson.com.moneycounter.R;
import applock.anderson.com.moneycounter.services.CountService;

/**
 * Created by Xiamin on 2016/12/11.
 */

public class MyWindowManager {

    /**
     * 小悬浮窗View的实例
     */
    private static FloatWindowSmallView smallWindow;

    /**
     * 大悬浮窗View的实例
     */
    private static FloatWindowBigView bigWindow;

    private static FloatWindowHintView mHintView;
    private static LayoutParams mHintWindowParams;

    private static FloatSecurityView mSecurityView;
    private static LayoutParams mSecutWindowParams;
    /**
     * 小悬浮窗View的参数
     */
    private static LayoutParams smallWindowParams;

    /**
     * 大悬浮窗View的参数
     */
    private static LayoutParams bigWindowParams;

    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private static WindowManager mWindowManager;

    /**
     * 用于获取手机可用内存
     */
    private static ActivityManager mActivityManager;

    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void createSmallWindow(Context context) {
        mContext = context;
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (smallWindow == null) {
            smallWindow = new FloatWindowSmallView(context);
            if (smallWindowParams == null) {
                smallWindowParams = new LayoutParams();
                smallWindowParams.type = LayoutParams.TYPE_TOAST;
                smallWindowParams.format = PixelFormat.RGBA_8888;
                smallWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_NOT_FOCUSABLE;
                smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                smallWindowParams.width = FloatWindowSmallView.viewWidth;
                smallWindowParams.height = FloatWindowSmallView.viewHeight;
                smallWindowParams.x = screenWidth;
                smallWindowParams.y = screenHeight / 2;
            }
            smallWindow.setParams(smallWindowParams);
            windowManager.addView(smallWindow, smallWindowParams);
            Logger.d("将small view添加进桌面");
        }
    }

    /**
     * 将小悬浮窗从屏幕上移除。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void removeSmallWindow(Context context) {
        if (smallWindow != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(smallWindow);
            windowManager.removeViewImmediate(smallWindow);
            smallWindow = null;
            Logger.d("移除small view");
        }
    }

    /**
     * 创建一个大悬浮窗。位置为屏幕正中间。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void createBigWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (bigWindow == null) {
            bigWindow = new FloatWindowBigView(context);
            if (bigWindowParams == null) {
                bigWindowParams = new LayoutParams();
                bigWindowParams.x = screenWidth / 2 - FloatWindowBigView.viewWidth / 2;
                bigWindowParams.y = screenHeight / 8 - FloatWindowBigView.viewHeight / 2;
                bigWindowParams.type = LayoutParams.TYPE_TOAST;
                bigWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_NOT_FOCUSABLE;
                bigWindowParams.format = PixelFormat.RGBA_8888;
                bigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                bigWindowParams.width = FloatWindowBigView.viewWidth;
                bigWindowParams.height = FloatWindowBigView.viewHeight;
            }
            windowManager.addView(bigWindow, bigWindowParams);
            Logger.d("添加big view");
            bigWindow.upDateView(data1, data2, data3);
        }

    }

    /**
     * 将大悬浮窗从屏幕上移除。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void removeBigWindow(Context context) {
        if (bigWindow != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(bigWindow);
            bigWindow = null;
            Logger.d("移除big view");
        }
    }

    /**
     * 更新小悬浮窗的TextView上的数据，显示内存使用的百分比。
     *
     * @param context 可传入应用程序上下文。
     */
    public static void updateUsedPercent(Context context) {
        if (smallWindow != null) {
            TextView percentView = (TextView) smallWindow.findViewById(R.id.percent);
            percentView.setText(getUsedPercentValue(context));
        }
    }

    /**
     * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
     *
     * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
     */
    public static boolean isWindowShowing() {
        return smallWindow != null || bigWindow != null;
    }

    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
     *
     * @param context 必须为应用程序的Context.
     * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    /**
     * 如果ActivityManager还未创建，则创建一个新的ActivityManager返回。否则返回当前已创建的ActivityManager。
     *
     * @param context 可传入应用程序上下文。
     * @return ActivityManager的实例，用于获取手机可用内存。
     */
    private static ActivityManager getActivityManager(Context context) {
        if (mActivityManager == null) {
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        return mActivityManager;
    }

    /**
     * 计算已使用内存的百分比，并返回。
     *
     * @param context 可传入应用程序上下文。
     * @return 已使用内存的百分比，以字符串形式返回。
     */
    public static String getUsedPercentValue(Context context) {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
            long availableSize = getAvailableMemory(context) / 1024;
            int percent = (int) ((totalMemorySize - availableSize) / (float) totalMemorySize * 100);
            return percent + "%";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "悬浮窗";
    }

    /**
     * 获取当前可用内存，返回数据以字节为单位。
     *
     * @param context 可传入应用程序上下文。
     * @return 当前可用内存。
     */
    private static long getAvailableMemory(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        getActivityManager(context).getMemoryInfo(mi);
        return mi.availMem;
    }

    private void updateBigInfo() {

    }

    public static void showOrnotBigWindow(Context context) {
        if (bigWindow != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(bigWindow);
            bigWindow = null;
        } else {
            createBigWindow(context);
        }
    }

    private static String[] data1;
    private static String data2;
    private static String data3;


    public static void updateBigWindowData(String[] dataLei, String baozi, String shunzi) {
        data1 = dataLei;
        data2 = baozi;
        data3 = shunzi;
        if (bigWindow != null) {
            bigWindow.upDateView(dataLei, baozi, shunzi);
        }
    }

    private static Context mContext;
    private static Timer timer = new Timer();
    private static TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {

        }
    };


    public static void createHintWindow(final Context context) {
        Handler handler = new Handler();
        mContext = context;
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (mHintView == null) {
            mHintView = new FloatWindowHintView(mContext);
            String jianyi = "";
            if (data1 != null && data1[0] != null) {
                jianyi = "雷中雷建议雷值：" + CountService.no1 + ", " + CountService.no2;
            }
            String tisi = "雷中雷提示：豹子" + data2 + " 顺子" + data3;
            Logger.d("添加hint viewcanshu" + jianyi + tisi);
            mHintView.upDateUI(jianyi, tisi);
            if (mHintWindowParams == null) {
                mHintWindowParams = new LayoutParams();
                mHintWindowParams.x = screenWidth / 2 - FloatWindowHintView.viewWidth / 2;
                mHintWindowParams.y = screenHeight / 7 - FloatWindowHintView.viewHeight / 2;
                mHintWindowParams.type = LayoutParams.TYPE_TOAST;
                mHintWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_NOT_FOCUSABLE;
                mHintWindowParams.format = PixelFormat.RGBA_8888;
                mHintWindowParams.gravity = Gravity.TOP | Gravity.LEFT;
                mHintWindowParams.width = FloatWindowHintView.viewWidth;
                mHintWindowParams.height = FloatWindowHintView.viewHeight;
            }
            if (mHintView != null) {
                windowManager.addView(mHintView, mHintWindowParams);
                Logger.d("添加hint view");
            } else {
                Logger.d("添加hint view失败  为空");
            }
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    WindowManager windowManager = getWindowManager(context);
//                    if (mHintView != null) {
//                        windowManager.addView(mHintView, mHintWindowParams);
//                        Logger.d("添加hint view");
//                    } else {
//                        Logger.d("添加hint view失败  为空");
//                    }
//                }
//            }, 2500);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mHintView != null) {
                        WindowManager windowManager = getWindowManager(context);
                        windowManager.removeViewImmediate(mHintView);
                        mHintView = null;
                        Logger.d("移除hint view");
                    }
                }
            }, 6000);
        }
    }

    public static void createSecurityWindow(final Context context) {
        Handler handler = new Handler();
        mContext = context;
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (mSecurityView == null) {
            mSecurityView = new FloatSecurityView(mContext);

            Logger.d("添加安全界面");
            if (mSecutWindowParams == null) {
                mSecutWindowParams = new LayoutParams();
                mSecutWindowParams.x = screenWidth / 2 - FloatSecurityView.viewWidth / 2;
                mSecutWindowParams.y = screenHeight / 2 - FloatSecurityView.viewHeight / 2;
                mSecutWindowParams.type = LayoutParams.TYPE_TOAST;
                mSecutWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
                mSecutWindowParams.format = PixelFormat.RGBA_8888;
                mSecutWindowParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
                mSecutWindowParams.width = FloatSecurityView.viewWidth;
                mSecutWindowParams.height = FloatSecurityView.viewHeight;
            }
            windowManager.addView(mSecurityView, mSecutWindowParams);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mSecurityView != null) {
                        WindowManager windowManager = getWindowManager(context);
                        windowManager.removeViewImmediate(mSecurityView);
                        mSecurityView = null;
                        Logger.d("移除安全界面");
                    }
                }
            }, 3000);
        }
    }

    private static ToastView mToastView;
    private static LayoutParams mToastWindowParams;

    public static void createToast(final Context context, String text) {
        Handler handler = new Handler();
        mContext = context;
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (mToastView == null) {
            mToastView = new ToastView(mContext, text);

            Logger.d("添加Toast界面");
            if (mToastWindowParams == null) {
                mToastWindowParams = new LayoutParams();
                mToastWindowParams.x = screenWidth / 2 - ToastView.viewWidth / 2;
                mToastWindowParams.y = screenHeight / 2 - ToastView.viewHeight / 2;
                mToastWindowParams.type = LayoutParams.TYPE_TOAST;
                mToastWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
                mToastWindowParams.format = PixelFormat.RGBA_8888;
                mToastWindowParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
                mToastWindowParams.width = ToastView.viewWidth;
                mToastWindowParams.height = ToastView.viewHeight;
            }
            windowManager.addView(mToastView, mToastWindowParams);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mToastView != null) {
                        WindowManager windowManager = getWindowManager(context);
                        windowManager.removeViewImmediate(mToastView);
                        mToastView = null;
                        Logger.d("移除Toast界面");
                    }
                }
            }, 3000);
        }
    }

    public static void createToast(final Context context, String text, int delay, int timelong) {
        Handler handler = new Handler();
        mContext = context;
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (mToastView == null) {
            mToastView = new ToastView(mContext, text);


            if (mToastWindowParams == null) {
                mToastWindowParams = new LayoutParams();
                mToastWindowParams.x = screenWidth / 2 - ToastView.viewWidth / 2;
                mToastWindowParams.y = screenHeight / 2 - ToastView.viewHeight / 2;
                mToastWindowParams.type = LayoutParams.TYPE_TOAST;
                mToastWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
                mToastWindowParams.format = PixelFormat.RGBA_8888;
                mToastWindowParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
                mToastWindowParams.width = ToastView.viewWidth;
                mToastWindowParams.height = ToastView.viewHeight;
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Logger.d("添加Toast界面");
                    WindowManager windowManager = getWindowManager(context);
                    windowManager.addView(mToastView, mToastWindowParams);
                }
            }, delay);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mToastView != null) {
                        WindowManager windowManager = getWindowManager(context);
                        windowManager.removeViewImmediate(mToastView);
                        mToastView = null;
                        Logger.d("移除Toast界面");
                    }
                }
            }, timelong + delay);
        }
    }

    private static FloatFourierView mFourierView;
    private static LayoutParams mFourierWindowParams;
    public static void createFourierWindow(final Context context, int text, int timelong) {
        Handler handler = new Handler();
        mContext = context;
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (mFourierView == null) {
            mFourierView = new FloatFourierView(mContext, text);

            Logger.d("添加fuliye界面");
            if (mFourierWindowParams == null) {
                mFourierWindowParams = new LayoutParams();
                mFourierWindowParams.x = screenWidth / 2 - FloatFourierView.viewWidth / 2;
                mFourierWindowParams.y = screenHeight / 2 - FloatFourierView.viewHeight / 2;
                mFourierWindowParams.type = LayoutParams.TYPE_TOAST;
                mFourierWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
                mFourierWindowParams.format = PixelFormat.RGBA_8888;
                mFourierWindowParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
                mFourierWindowParams.width = FloatFourierView.viewWidth;
                mFourierWindowParams.height = FloatFourierView.viewHeight;
            }
            windowManager.addView(mFourierView, mFourierWindowParams);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mFourierView != null) {
                        WindowManager windowManager = getWindowManager(context);
                        windowManager.removeViewImmediate(mFourierView);
                        mFourierView = null;
                        Logger.d("移除fuliye界面");
                    }
                }
            }, timelong);
        }
    }


}


