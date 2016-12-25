package applock.anderson.com.moneycounter.services;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import applock.anderson.com.moneycounter.Bean.PersonMoneyBean;
import applock.anderson.com.moneycounter.Utils.MoneyPackInfo;
import applock.anderson.com.moneycounter.Utils.SettingsContact;
import applock.anderson.com.moneycounter.Utils.StringUtil;
import applock.anderson.com.moneycounter.Utils.WeChatConstant;
import applock.anderson.com.moneycounter.view.MyWindowManager;

/**
 * Created by Xiamin on 2016/12/11.
 */

public class CountService extends AccessibilityService {
    public final static String TAG = "MoneyCounter";
    private final static String PACK_Hint = "com.tencent.mm:id/bai";
    private final static String FIRST_HINT = "com.tencent.mm:id/baq";
    private final static String NAME_ID = "com.tencent.mm:id/bdn";
    private final static String MONEY_ID = "com.tencent.mm:id/bdr";
    private final static String END_HINT = "com.tencent.mm:id/bdx";
    private final static String ITEM_LAYOUT = "com.tencent.mm:id/li";

    private final static String FABAO = "com.tencent.mm:id/bbf";

    private final static String QUN_RENMING = "com.tencent.mm:id/c4h";

    private final static int[] Data_shunzi = new int[]{123, 234, 345, 456, 567, 789, 1234, 2345, 3456, 4567, 6789};
    private final static int[] Data_baozi = new int[]{111, 222, 333, 444, 5555, 666, 777, 888,
            999, 1111, 2222, 3333, 4444, 5555};

    private AccessibilityNodeInfo rootNodeInfo;  //界面根节点信息
    private static List<PersonMoneyBean> mMoneyBeanList = new ArrayList<>();

    private boolean isOpen = false;
    private boolean isBaozi = false;
    private boolean isShunzi = false;
    private boolean isFloatOpen = false;
    private boolean isGetMoney = false;
    private int mWeishu = 2;

    private static String[] dataForlei = new String[5];
    private static int[] dataPaiMing = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private static int baoziCount = 0;
    private static int shunziCount = 0;
    private static String dataForBaozi = "未出现";
    private static String dataForShunzi = "未出现";
    public static int no1;
    public static int no2;
    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("CountService 开启成功");
        initSettings();
        mHandler = new Handler();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //Log.d(TAG, "界面变化，触发onAccessibilityEvent");
        if (isOpen) {
            watchChatMoney(event);
            /*采集雷值*/
            if (mWeishu == 2) {
                startCount2();
            } else {
                startCount1();
            }
            /*采集豹子*/
            if (isBaozi) {
                checkBaozi();
            }
            //采集顺子数据
            if (isShunzi) {
                checkShunzi();
            }

            if (mDataChangedListener != null) {
                mDataChangedListener.onChanged();
            }

            MyWindowManager.updateBigWindowData(dataForlei, dataForBaozi, dataForBaozi);
            //抢红包
            if (isGetMoney) {
                watchNotifications(event);
                getPackageMoney(event);
            }
            //    watchNotifications(event);
            //    getPackageMoney(event);
        }
    }

    @Override
    public void onInterrupt() {

    }

    private boolean isStart = false;
    private boolean isEnd = false;

    private static String oldName = "";
    private static String newName = "";


    /**
     * 获取红包金额
     *
     * @param event
     */
    private void watchChatMoney(AccessibilityEvent event) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rootNodeInfo = getRootInActiveWindow();
        }
        if (rootNodeInfo == null) return;
        //Log.i(TAG, "开始查找金额");
        List<AccessibilityNodeInfo> moneyInfo = null;
        List<AccessibilityNodeInfo> itemInfo = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

            /**
             * 寻找红包名
             */
            itemInfo = rootNodeInfo.findAccessibilityNodeInfosByViewId(PACK_Hint);
            if (itemInfo != null && itemInfo.size() != 0) {
                for (AccessibilityNodeInfo layoutnode : itemInfo) {
                    if ("android.widget.TextView".equals(layoutnode.getClassName())) {
                        newName = layoutnode.getText().toString();
                        if (newName.equals(oldName)) {
                            break;
                        }
                        oldName = newName;
                        Log.d(TAG, " 找到红包名 重新统计");
                        isEnd = false;
                        mMoneyBeanList.clear();
                        dataForBaozi = "未出现";
                        dataForBaozi = "未出现";
                        for (String s : dataForlei) {
                            s = "";
                        }
                        shunziCount++;
                        baoziCount++;
                    }
                }
            }

            /**
             * 寻找开头
             */
            itemInfo = rootNodeInfo.findAccessibilityNodeInfosByViewId(FIRST_HINT);
            if (itemInfo != null && itemInfo.size() != 0) {
//                Log.d(TAG, " 找到开头 重新统计");
//                isEnd = false;
//                mMoneyBeanList.clear();
//                dataForBaozi = "未出现";
//                dataForBaozi = "未出现";
//                for (String s : dataForlei) {
//                    s = "";
//                }
            }

            /**
             * 寻找发包界面
             */
            itemInfo = rootNodeInfo.findAccessibilityNodeInfosByViewId(FABAO);
            if (itemInfo != null && itemInfo.size() != 0) {
                for (AccessibilityNodeInfo layoutnode : itemInfo) {
                    if ("android.widget.EditText".equals(layoutnode.getClassName())) {
                        MyWindowManager.createHintWindow(getApplicationContext());
                        Log.d(TAG, "进入发包界面，创建发包窗口");
                    }
                }
            }

            /**
             * 寻找群信息界面
             */
            itemInfo = rootNodeInfo.findAccessibilityNodeInfosByViewId(QUN_RENMING);
            if (itemInfo != null && itemInfo.size() > 1) {
                for (AccessibilityNodeInfo layoutnode : itemInfo) {
                    if ("android.widget.TextView".equals(layoutnode.getClassName())) {
                    }
                }
                Log.d(TAG, "进入群信息界面");
                Toast.makeText(getApplicationContext(), "雷中雷：正在连接当前群成员和您统计的红包数据综合分析中…………", Toast.LENGTH_SHORT).show();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "雷中雷：已为您综合分析完毕请打开你的红包进行埋雷！！！", Toast.LENGTH_SHORT).show();
                    }
                }, 3000);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MyWindowManager.createToast(getApplicationContext(),"加载群成员红包信息完成\n");
                    }
                }, 6000);
            }

            /**
             * 寻找姓名和金额
             */
            itemInfo = rootNodeInfo.findAccessibilityNodeInfosByViewId(ITEM_LAYOUT);
            if (itemInfo != null && itemInfo.size() != 0) {
                Log.d(TAG, " 找到item layout查找");
                for (AccessibilityNodeInfo layoutnode : itemInfo) {
                    if ("android.widget.LinearLayout".equals(layoutnode.getClassName())) {
                        Logger.e("找到layout");
                        List<AccessibilityNodeInfo> name = null;
                        List<AccessibilityNodeInfo> money = null;
                        name = layoutnode.findAccessibilityNodeInfosByViewId(NAME_ID);
                        money = layoutnode.findAccessibilityNodeInfosByViewId(MONEY_ID);
                        if (name != null && name.size() != 0 && money != null && money.size() != 0) {
                            PersonMoneyBean personMoneyBean = new PersonMoneyBean();
                            Log.e(TAG, "开始查找姓名和金额");
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
                                }
                            }
                            Log.e(TAG, personMoneyBean.toString());
                            addToList(personMoneyBean);
                        }
                    }
                }

            }

            /**
             * 寻找结尾提示
             */
            itemInfo = rootNodeInfo.findAccessibilityNodeInfosByViewId(END_HINT);
            if (itemInfo != null && itemInfo.size() != 0 && isEnd == false) {
                Logger.d(" 找到结尾 结束统计" + mMoneyBeanList);

                isEnd = true;
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

    int[] a = new int[10];

    private void startCount2() {
        // Log.i(TAG, "开始 startCount1");
        //Logger.d(mMoneyBeanList);
        ArrayList<Info> infos = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            a[i] = 0;
        }
        /*统计角位*/
        for (PersonMoneyBean i : mMoneyBeanList) {
            a[(i.moneyInt % 10)]++;
        }
        for (int i = 0; i < 10; i++) {
            float result = (float) a[i] / (float) mMoneyBeanList.size();
            Info info = new Info();
            info.index = i;
            info.num = a[i];
            info.result = result;
            try {
                DecimalFormat fnum = new DecimalFormat("##0.0");
                String dd = fnum.format(result * 100) + "%";
                info.str = dd;
            } catch (Exception e) {
                Logger.e("浮点数转换出错 " + result + "  " + e.toString());
            }
            infos.add(info);
        }
        Collections.sort(infos);
        //Logger.d(infos);

        dataForlei[0] = infos.get(0).index + " " + infos.get(0).str;
        dataForlei[1] = infos.get(1).index + " " + infos.get(1).str;
        dataForlei[2] = infos.get(2).index + " " + infos.get(2).str;
        dataForlei[3] = infos.get(3).index + " " + infos.get(3).str;
        dataForlei[4] = infos.get(4).index + " " + infos.get(4).str;
        no1 = infos.get(0).index;
        no2 = infos.get(1).index;
        for (int i = 0; i < infos.size(); i++) {
            dataPaiMing[i] = infos.get(i).index;
        }
    }

    class Info implements Comparable<Info> {
        int index;
        int num;
        float result;
        String str;

        @Override
        public int compareTo(Info o) {
            return o.num - num;
        }

        @Override
        public String toString() {
            return "idex: " + index + " ,num= " + num + ",str= " + str;
        }
    }

    private void startCount1() {
        Log.i(TAG, "开始 startCount1");
        Logger.d(mMoneyBeanList);
        ArrayList<Info> infos = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            a[i] = 0;
        }
        /*统计角位*/
        for (PersonMoneyBean i : mMoneyBeanList) {
            a[((i.moneyInt / 10) % 10)]++;
        }
        for (int i = 0; i < 10; i++) {
            float result = (float) a[i] / (float) mMoneyBeanList.size();
            Info info = new Info();
            info.index = i;
            info.num = a[i];
            info.result = result;
            try {
                DecimalFormat fnum = new DecimalFormat("##0.0");
                String dd = fnum.format(result * 100) + "%";
                info.str = dd;
            } catch (Exception e) {
                Logger.e("浮点数转换出错 " + result + "  " + e.toString());
            }
            infos.add(info);
        }
        Collections.sort(infos);
        //Logger.d(infos);

        dataForlei[0] = infos.get(0).index + " " + infos.get(0).str;
        dataForlei[1] = infos.get(1).index + " " + infos.get(1).str;
        dataForlei[2] = infos.get(2).index + " " + infos.get(2).str;
        dataForlei[3] = infos.get(3).index + " " + infos.get(3).str;
        dataForlei[4] = infos.get(4).index + " " + infos.get(4).str;
        no1 = infos.get(0).index;
        no2 = infos.get(1).index;
        for (int i = 0; i < infos.size(); i++) {
            dataPaiMing[i] = infos.get(i).index;
        }
    }


    private void checkShunzi() {
        for (PersonMoneyBean bean : mMoneyBeanList) {
            if (Arrays.binarySearch(Data_shunzi, bean.moneyInt) > 0) {
                dataForShunzi = "" + bean.moneyFloat + " ！";
                shunziCount = 0;
            } else {
                dataForShunzi = "" + shunziCount + "把未出";
            }
        }
    }

    private void checkBaozi() {
        for (PersonMoneyBean bean : mMoneyBeanList) {
            if (Arrays.binarySearch(Data_baozi, bean.moneyInt) > 0) {
                dataForBaozi = "" + bean.moneyFloat + " ！";
                baoziCount = 0;
            } else {
                dataForBaozi = "" + baoziCount + "把未出";
            }
        }
    }

    private void initSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
        isOpen = sharedPreferences.getBoolean(SettingsContact.OPEN, false);
        isBaozi = sharedPreferences.getBoolean(SettingsContact.BAOZI, false);
        isShunzi = sharedPreferences.getBoolean(SettingsContact.SHUNZI, false);
        isFloatOpen = sharedPreferences.getBoolean(SettingsContact.FLOAT, false);
        isGetMoney = sharedPreferences.getBoolean(SettingsContact.GET_MONEY, false);
        if (isFloatOpen) {
            Log.d(TAG, "开启悬浮窗");
            MyWindowManager.createSmallWindow(getApplicationContext());
        }
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
                    if (isFloatOpen) {
                        MyWindowManager.createSmallWindow(getApplicationContext());
                    } else {
                        MyWindowManager.removeSmallWindow(getApplicationContext());
                        MyWindowManager.removeBigWindow(getApplicationContext());
                    }
                } else if (key.equals(SettingsContact.GET_MONEY)) {
//                    if (sharedPreferences.getBoolean(SettingsContact.GET_MONEY, false)) {
//                        Toast.makeText(CountService.this,"打开雷中类避雷开关",Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(CountService.this,"关闭雷中类避雷开关",Toast.LENGTH_SHORT).show();
//                    }
                    isGetMoney = sharedPreferences.getBoolean(SettingsContact.GET_MONEY, false);
                }
                Log.d(TAG, "open state:" + isOpen + " baozi: " + isBaozi + " shunzi: " + isShunzi
                        + "  Weizhi: " + mWeishu + " isFloatOpen " + isFloatOpen + " isGetMoney:" + isGetMoney);
            }
        });
    }

    private DataChangedListener mDataChangedListener;

    public interface DataChangedListener {
        public void onChanged();
    }

    public void setOnDataChangedListener(DataChangedListener dataChangedListener) {
        mDataChangedListener = dataChangedListener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyWindowManager.removeBigWindow(getApplicationContext());
        MyWindowManager.removeSmallWindow(getApplicationContext());
    }

    Boolean mOpenPack = false;

    private void getPackageMoney(AccessibilityEvent event) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rootNodeInfo = getRootInActiveWindow();
        }
        if (rootNodeInfo == null) return;
        /**
         * 寻找红包名
         */
        List<AccessibilityNodeInfo> itemInfo = null;
        Log.d(TAG, "寻找红包名");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

            itemInfo = rootNodeInfo.findAccessibilityNodeInfosByViewId(WeChatConstant.RED_PACK_LAYOUT);
            if (itemInfo != null && itemInfo.size() != 0) {
                AccessibilityNodeInfo layoutnode = itemInfo.get(itemInfo.size() - 1);
                Log.d(TAG, "找到红包layout " + itemInfo.size() + "个");
                if ("android.widget.LinearLayout".equals(layoutnode.getClassName())) {
                    Log.d(TAG, "找到红包item");
                    if (layoutnode.findAccessibilityNodeInfosByText(WeChatConstant.WECHAT_PACK_TIP) != null
                            && layoutnode.findAccessibilityNodeInfosByText(WeChatConstant.WECHAT_VIEW_OTHERS_CH) != null) {
                        Log.d(TAG, "找到微信红包 ");
                        if (layoutnode.getParent() != null) {
                            List<AccessibilityNodeInfo> nameInfo = null;
                            String name = "";
                            nameInfo = layoutnode.getParent().findAccessibilityNodeInfosByViewId(WeChatConstant.RED_PACK_NAME);
                            if (nameInfo != null && nameInfo.size() != 0) {
                                name = nameInfo.get(0).getText().toString();
                            }
                            List<AccessibilityNodeInfo> textInfo = null;
                            String text = "";
                            textInfo = layoutnode.getParent().findAccessibilityNodeInfosByViewId(WeChatConstant.RED_PACK_TEXT);
                            if (textInfo != null && textInfo.size() != 0) {
                                text = textInfo.get(0).getText().toString();
                            }
                            Log.i(TAG, "该红包 name " + name + " text " + text);

                            if (MoneyPackInfo.checkPack(name, text)) {
                                if (!TextUtils.isEmpty(name) && text != null && !TextUtils.isEmpty(text) && text.length() != 0) {
                                    char c = text.charAt(text.length() - 1);
                                    Log.i(TAG, "该红包未在点击过列表中，点击红包  结尾字符" + c);
                                    Toast.makeText(getApplicationContext(), "雷中雷：百分百避雷中…", Toast.LENGTH_SHORT).show();
                                    if (StringUtil.isHasIn5to10(c, dataPaiMing)) {
                                        MyWindowManager.createSecurityWindow(getApplicationContext());
                                        layoutnode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                        mOpenPack = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            /**
             * 寻找点击按钮
             */

            itemInfo = rootNodeInfo.findAccessibilityNodeInfosByViewId(WeChatConstant.WECHAT_Click_button);
            if (itemInfo != null && itemInfo.size() != 0) {
                AccessibilityNodeInfo layoutnode = itemInfo.get(0);
                if (layoutnode != null) {
                    Log.i(TAG, "点击打开红包");
                    layoutnode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    mOpenPack = true;
                }
            }

            itemInfo = rootNodeInfo.findAccessibilityNodeInfosByViewId(WeChatConstant.WECHAT_BETTER_LUCK_CH);
            if (itemInfo != null && itemInfo.size() != 0) {
                itemInfo = rootNodeInfo.findAccessibilityNodeInfosByViewId(WeChatConstant.WECHAT_cancel_button);
                if (itemInfo != null && itemInfo.size() != 0) {
                    AccessibilityNodeInfo layoutnode = itemInfo.get(0);
                    if (layoutnode != null) {

                        if (mOpenPack) {
                            Log.i(TAG, "点击关闭红包");
                            layoutnode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            mOpenPack = false;
                        }

                    }
                }
            }


            itemInfo = rootNodeInfo.findAccessibilityNodeInfosByViewId(WeChatConstant.WECHAT_BACK);
            if (itemInfo != null && itemInfo.size() != 0) {
                final AccessibilityNodeInfo layoutnode = itemInfo.get(0);
                if (layoutnode != null) {
                    Log.i(TAG, "找到详情页 返回按钮 mOpenPack = " + mOpenPack);
                    if (mOpenPack) {
                        Log.i(TAG, "点击关闭详情");
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                layoutnode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                mOpenPack = false;
                            }
                        }, 1000);

                    }
                }
            }

        }
    }

    private boolean watchNotifications(AccessibilityEvent event) {
        // 判断是不是一个通知
        if (event.getEventType() != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED)
            return false;

        // 判断是不是红包
        String tip = event.getText().toString();
        if (!tip.contains(WeChatConstant.WECHAT_NOTIFICATION_TIP)) {
            Log.d(TAG, "不是微信红包");
            return true;
        }
        Log.d(TAG, "来了一个微信红包");
        Parcelable parcelable = event.getParcelableData();
        if (parcelable instanceof Notification) {
            Notification notification = (Notification) parcelable;
            try {
                /* 清除signature,避免进入会话后误判 */
                // signature.cleanSignature();
                Log.d(TAG, "点击进入");
                notification.contentIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}

