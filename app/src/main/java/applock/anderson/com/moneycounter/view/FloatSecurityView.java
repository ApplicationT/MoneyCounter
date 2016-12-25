package applock.anderson.com.moneycounter.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import applock.anderson.com.moneycounter.R;

/**
 * Created by Xiamin on 2016/12/24.
 */

public class FloatSecurityView extends LinearLayout {
    View view;
    public static int viewWidth;    //记录大悬浮窗的宽度
    public static int viewHeight;   //记录大悬浮窗的高度
    public FloatSecurityView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.security_item, this);
        view = findViewById(R.id.security_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
    }
}
