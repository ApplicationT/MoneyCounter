package applock.anderson.com.moneycounter.services;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.orhanobut.logger.Logger;

import java.util.List;

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
        float mTmpMoney = 0;
        Logger.d(" 开始查找金额");
        List<AccessibilityNodeInfo> moneyInfo = null;
        List<AccessibilityNodeInfo> itemInfo = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            itemInfo = rootNodeInfo.findAccessibilityNodeInfosByViewId(ITEM_LAYOUT);
            if (itemInfo != null && itemInfo.size() != 0) {
                Logger.d(" 找到item layout查找");
                for (AccessibilityNodeInfo layoutnode : itemInfo) {
                    if("android.widget.LinearLayout".equals(layoutnode.getClassName())){
                        Logger.e("找到layout");
                        List<AccessibilityNodeInfo> name = null;
                        List<AccessibilityNodeInfo> money = null;
                        name = layoutnode.findAccessibilityNodeInfosByViewId(NAME_ID);
                        money = layoutnode.findAccessibilityNodeInfosByViewId(MONEY_ID);
                        StringBuilder stringBuilder = new StringBuilder("");
                        if (name != null && name.size() != 0 && money != null && money.size() != 0) {
                            Logger.e("开始查找姓名和金额");
                            for(AccessibilityNodeInfo i: name) {
                                if ("android.widget.TextView".equals(i.getClassName())) {
                                    stringBuilder.append("姓名：" + i.getText().toString());
                                }
                            }
                            for(AccessibilityNodeInfo j: money) {
                                if ("android.widget.TextView".equals(j.getClassName())) {
                                    stringBuilder.append("金额 " + j.getText().toString());
                                }
                            }
                            Logger.e(stringBuilder.toString());
                        }
                    }
                }
            }

//            moneyInfo = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bgg");
//            if (moneyInfo != null && moneyInfo.size() != 0) {
//                Logger.d(" 找到bb0 金额 查找");
//
//                for (AccessibilityNodeInfo mynode : moneyInfo) {
//                    if ("android.widget.TextView".equals(mynode.getClassName())) {
////                        Bundle arguments = new Bundle();
////                        arguments.putCharSequence(AccessibilityNodeInfo
////                                        .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "100元");
////                        if(mynode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT,null)){
////                            Logger.d("设置金额成功");
////                        } else {
////                            Logger.d("设置金额失败");
////                        }
//
//                        Logger.d(mynode.getText());
//                        //mTmpMoney = Float.parseFloat(mynode.getText().toString());
//                        Logger.d(" 遍历mynode getText(): " + mTmpMoney);
//                    } else {
//                        Logger.d(" 该node失败" + mynode.getText());
//                    }
//                }
//                Toast.makeText(this, "金额" + mTmpMoney, Toast.LENGTH_SHORT).show();
//            }
        }
    }
}
