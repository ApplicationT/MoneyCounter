package applock.anderson.com.moneycounter.services;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.orhanobut.logger.Logger;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import applock.anderson.com.moneycounter.Bean.PersonMoneyBean;
import applock.anderson.com.moneycounter.Utils.SettingsContact;

/**
 * Created by Xiamin on 2016/12/11.
 */

public class CountService extends AccessibilityService {
    private final static String FIRST_HINT = "com.tencent.mm:id/be6";
    private final static String NAME_ID = "com.tencent.mm:id/bgc";
    private final static String MONEY_ID = "com.tencent.mm:id/bgg";
    private final static String END_HINT = "com.tencent.mm:id/bgo";
    private final static String ITEM_LAYOUT = "com.tencent.mm:id/j0";

    private AccessibilityNodeInfo rootNodeInfo;  //界面根节点信息
    private static List<PersonMoneyBean> mMoneyBeanList = new ArrayList<>();

    private boolean isOpen = false;
    private boolean isBaozi = false;
    private boolean isShunzi = false;
    private boolean isFloatOpen = false;
    private boolean isGetMoney = false;
    private int mWeishu = 2;


    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("CountService 开启成功");
        initSettings();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Logger.d("界面变化，触发onAccessibilityEvent");
        if(isOpen) {
            /*采集雷值*/
            if(mWeishu == 2) {
                watchChatMoney2(event);
            } else {

            }
            /*采集豹子*/
            if(isBaozi) {

            }
            //采集顺子数据
            if (isShunzi) {

            }

            //抢红包
            if(isGetMoney) {

            }
        }
    }

    @Override
    public void onInterrupt() {

    }

    private boolean isStart = false;
    private boolean isEnd = false;

    /**
     * 获取红包金额
     *
     * @param event
     */
    private void watchChatMoney2(AccessibilityEvent event) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rootNodeInfo = getRootInActiveWindow();
        }
        if (rootNodeInfo == null) return;
        Logger.d(" 开始查找金额");
        List<AccessibilityNodeInfo> moneyInfo = null;
        List<AccessibilityNodeInfo> itemInfo = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            itemInfo = rootNodeInfo.findAccessibilityNodeInfosByViewId(FIRST_HINT);
            itemInfo = rootNodeInfo.findAccessibilityNodeInfosByViewId(FIRST_HINT);
            if (itemInfo != null && itemInfo.size() != 0) {
                Logger.d(" 找到开头 重新统计");
                mMoneyBeanList.clear();
            }

            itemInfo = rootNodeInfo.findAccessibilityNodeInfosByViewId(ITEM_LAYOUT);
            if (itemInfo != null && itemInfo.size() != 0) {
                Logger.d(" 找到item layout查找");
                for (AccessibilityNodeInfo layoutnode : itemInfo) {
                    if ("android.widget.LinearLayout".equals(layoutnode.getClassName())) {
                        Logger.e("找到layout");
                        List<AccessibilityNodeInfo> name = null;
                        List<AccessibilityNodeInfo> money = null;
                        name = layoutnode.findAccessibilityNodeInfosByViewId(NAME_ID);
                        money = layoutnode.findAccessibilityNodeInfosByViewId(MONEY_ID);
                        if (name != null && name.size() != 0 && money != null && money.size() != 0) {
                            PersonMoneyBean personMoneyBean = new PersonMoneyBean();
                            Logger.e("开始查找姓名和金额");
                            for (AccessibilityNodeInfo i : name) {
                                if ("android.widget.TextView".equals(i.getClassName())) {
                                    personMoneyBean.setName(i.getText().toString());
                                }
                            }
                            for (AccessibilityNodeInfo j : money) {
                                if ("android.widget.TextView".equals(j.getClassName())) {
                                    personMoneyBean.setMoneyString(j.getText().toString());
                                    Bundle arguments = new Bundle();
                                    arguments.putCharSequence(
                                            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                                            "your info");
//                                    j.getBoundsInScreen();
//                                    j.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                                }
                            }
                            addToList(personMoneyBean);

                        }
                    }
                }

            }

            itemInfo = rootNodeInfo.findAccessibilityNodeInfosByViewId(END_HINT);
            if (itemInfo != null && itemInfo.size() != 0) {
                Logger.d(" 找到结尾 结束统计");
                Logger.d(mMoneyBeanList);
                startCount();
            }
        }
    }

    private void addToList(PersonMoneyBean moneyBean) {
        for (PersonMoneyBean k : mMoneyBeanList) {
            if (k.getName().toString().equals(moneyBean.getName().toString()) &&
                    k.getMoneyFloat() == moneyBean.getMoneyFloat()) {
                return;
            }
        }
        mMoneyBeanList.add(moneyBean);
    }

    public static List<PersonMoneyBean> getmMoneyBeanList() {
        return mMoneyBeanList;
    }

    String[] lei = new String[10];

    private void startCount() {
        int[] a = new int[10];
        for (PersonMoneyBean i : mMoneyBeanList) {
            a[(i.moneyInt % 10)]++;
        }
        for (int i = 0; i < 10; i++) {
            float result = (float) a[i] / (float) mMoneyBeanList.size();
            Logger.d(a[i]);
            try {
                DecimalFormat fnum = new DecimalFormat("##0.0");
                String dd = fnum.format(result * 100) + "%";
                lei[i] = dd;
            } catch (Exception e) {
                Logger.e("浮点数转换出错 " + result + "  " + e.toString());
            }
        }
        Logger.d(lei);
    }


    private void initSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
        isOpen = sharedPreferences.getBoolean(SettingsContact.OPEN, false);
        isBaozi = sharedPreferences.getBoolean(SettingsContact.BAOZI, false);
        isShunzi = sharedPreferences.getBoolean(SettingsContact.SHUNZI, false);
        isFloatOpen = sharedPreferences.getBoolean(SettingsContact.FLOAT, false);
        isGetMoney = sharedPreferences.getBoolean(SettingsContact.GET_MONEY, false);
        mWeishu = sharedPreferences.getInt(SettingsContact.WEIZHI, 2);
        Logger.d("init open state:" + isOpen + " baozi: " + isBaozi + "shunzi: " + isShunzi + "Weizhi: " + mWeishu);
        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(SettingsContact.OPEN)) {
                    if (sharedPreferences.getBoolean(key, false)) {
                        isOpen = true;
                    } else {
                        isOpen = false;
                    }
                } else if (key.equals(SettingsContact.BAOZI)) {
                    if (sharedPreferences.getBoolean(key, false)) {
                        isBaozi = true;
                    } else {
                        isBaozi = false;
                    }
                } else if (key.equals(SettingsContact.SHUNZI)) {
                    if (sharedPreferences.getBoolean(key, false)) {
                        isShunzi = true;
                    } else {
                        isShunzi = false;
                    }
                } else if (key.equals(SettingsContact.WEIZHI)) {
                    mWeishu = sharedPreferences.getInt(SettingsContact.WEIZHI, 2);
                } else if (key.equals(SettingsContact.FLOAT)) {
                    isFloatOpen = sharedPreferences.getBoolean(SettingsContact.FLOAT, false);
                } else if (key.equals(SettingsContact.GET_MONEY)) {
                    isGetMoney = sharedPreferences.getBoolean(SettingsContact.GET_MONEY, false);
                }
                Logger.d("open state:" + isOpen + " baozi: " + isBaozi + " shunzi: " + isShunzi
                        + "  Weizhi: " + mWeishu + " isFloatOpen " + isFloatOpen + " isGetMoney:" + isGetMoney);
            }
        });
    }
}
