package applock.anderson.com.moneycounter.Utils;

import android.content.Context;

import com.orhanobut.logger.Logger;

/**
 * Created by Xiamin on 2016/12/22.
 */

public class WeChatConstant {
    public static String RED_PACK_LAYOUT = "com.tencent.mm:id/a4a";  //红包layout 发红包的layout android.widget.LinearLayout
    public static String RED_PACK_NAME = "com.tencent.mm:id/i9";   //红包发包人
    public static String RED_PACK_TEXT = "com.tencent.mm:id/a4z";  //红包祝福语 恭喜发财 大吉大利 android.widget.TextView
    public static String RED_PACK_HINT = "com.tencent.mm:id/a51";  //微信红包提示 微信红包    android.widget.TextView

    public static String WECHAT_VIEW_SELF_CH = "查看红包";
    public static String WECHAT_VIEW_OTHERS_CH = "领取红包";
    public static String WECHAT_NOTIFICATION_TIP = "[微信红包]";
    public static String WECHAT_PACK_TIP = "微信红包";

    public static String WECHAT_DETAILS_EN = "Details";
    public static String WECHAT_DETAILS_CH = "红包详情";
    public static String WECHAT_BETTER_LUCK_EN = "Better luck next time!";
    public static String WECHAT_BETTER_LUCK_CH = "手慢了";

    public static String WECHAT_cancel_button = "com.tencent.mm:id/bdl";  //点开红包手慢了，点击取消
    public static String WECHAT_Click_button = "com.tencent.mm:id/bdh";  //点开红包按钮
    public static String WECHAT_Click_hint = "com.tencent.mm:id/bdg";  //点开红包界面提示

    public static String WECHAT_BACK = "com.tencent.mm:id/gp";  //返回键  红包详情左上角 android.widget.LinearLayout


    public static String PACK_Hint = "com.tencent.mm:id/bai";   //Anderson大码渣的红包 android.widget.TextView
    public static String FIRST_HINT = "com.tencent.mm:id/baq"; //已存入零钱，可用于发红包  android.widget.TextView
    public static String NAME_ID = "com.tencent.mm:id/bdn";     //人名 android.widget.TextView
    public static String MONEY_ID = "com.tencent.mm:id/bdr";    //0.01元 android.widget.TextView
    public static String END_HINT = "com.tencent.mm:id/bdx";    //收到的钱可直接消费 android.widget.TextView
    public static String ITEM_LAYOUT = "com.tencent.mm:id/li";  //每个红包item的layout  android.widget.LinearLayout

    public static String FABAO = "com.tencent.mm:id/bbf";    //单个金额输入框 android.widget.EditText

    public static String QUN_RENMING = "com.tencent.mm:id/c4h";  //群聊天信息 人名

    public static void upDataId(Context context) {
        String version = VersionUtils.getVersionName(context);
        Logger.d("  微信版本：" + version);
        if (version.equals("6.3.32")) {

        } else if (version.equals("6.5.3")) {
            RED_PACK_LAYOUT = "com.tencent.mm:id/a48";
            RED_PACK_NAME = "com.tencent.mm:id/ia";
            RED_PACK_TEXT = "com.tencent.mm:id/a55";

            WECHAT_cancel_button = "com.tencent.mm:id/bed";
            WECHAT_Click_button = "com.tencent.mm:id/be_";
            WECHAT_BACK = "com.tencent.mm:id/gr";

            PACK_Hint = "com.tencent.mm:id/bba";
            FIRST_HINT = "com.tencent.mm:id/bbi";
            NAME_ID = "com.tencent.mm:id/bef";
            MONEY_ID = "com.tencent.mm:id/bej";
            END_HINT = "com.tencent.mm:id/bep";
            ITEM_LAYOUT = "com.tencent.mm:id/lb";

            FABAO = "com.tencent.mm:id/bc8";
            QUN_RENMING = "com.tencent.mm:id/c5q";

        }
    }
}
