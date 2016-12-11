package applock.anderson.com.moneycounter.services;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.os.Bundle;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import applock.anderson.com.moneycounter.Bean.PersonMoneyBean;

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
    private List<PersonMoneyBean> mMoneyBeanList = new ArrayList<>();


    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("CountService 开启成功");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Logger.d("界面变化，触发onAccessibilityEvent");
        watchChatMoney(event);
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
    private void watchChatMoney(AccessibilityEvent event) {
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
            itemInfo = rootNodeInfo.findAccessibilityNodeInfosByViewId(END_HINT);
            if (itemInfo != null && itemInfo.size() != 0) {
                Logger.d(" 找到结尾 结束统计");
                Logger.d(mMoneyBeanList);
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
}
