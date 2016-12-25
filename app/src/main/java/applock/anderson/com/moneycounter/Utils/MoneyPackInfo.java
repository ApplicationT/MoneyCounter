package applock.anderson.com.moneycounter.Utils;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xiamin on 2016/12/23.
 */

public class MoneyPackInfo {
    private static List<PackgeBean> mInfoList = new ArrayList<PackgeBean>();

    /**
     * 检查近期红包表里面是否点过该红包
     *
     * @param name
     * @param hint
     * @return
     */
    public static boolean checkPack(String name, String hint) {
        PackgeBean bean = new PackgeBean(name, hint);
        if (mInfoList.size() > 10) {
            mInfoList.remove(0);
        }

        if (mInfoList.contains(bean)) {
            return false;
        }
        mInfoList.add(bean);
        Logger.d("点过的红包： " + mInfoList);
        return true;
    }

    static class PackgeBean {
        public String name;
        public String hint;

        public PackgeBean(String name, String hint) {
            this.name = name;
            this.hint = hint;
        }

        @Override
        public boolean equals(Object obj) {
            return name.equals(((PackgeBean) obj).name) && hint.equals(((PackgeBean) obj).hint);
        }

        @Override
        public String toString() {
            return "name = " + name + " " + hint;
        }
    }
}
