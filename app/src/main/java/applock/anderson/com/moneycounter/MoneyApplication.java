package applock.anderson.com.moneycounter;

import android.app.Application;

import com.orhanobut.logger.Logger;

/**
 * Created by Xiamin on 2016/12/17.
 */

public class MoneyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("MoneyCounter");
    }
}
