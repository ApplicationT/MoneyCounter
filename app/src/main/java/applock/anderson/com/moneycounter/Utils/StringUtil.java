package applock.anderson.com.moneycounter.Utils;

import com.orhanobut.logger.Logger;

/**
 * Created by Xiamin on 2016/12/11.
 */

public class StringUtil {
    public static float stringYuanTofloat(String str) {
        float ret = 0;

        return ret;
    }

    public static boolean isHasIn5to10(char c, int[] a) {
        Logger.d(a.length + "" + a);
        if (a.length != 10) {
            return false;
        }
        for (int i = 3; i < 10; i++) {
            if (c == a[i] + '0') {
                Logger.d("在后5名里面找到该数字");
                return true;

            }
        }
        return false;
    }
}
