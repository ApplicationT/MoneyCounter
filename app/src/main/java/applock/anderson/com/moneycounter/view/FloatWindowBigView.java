package applock.anderson.com.moneycounter.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import applock.anderson.com.moneycounter.R;

/**
 * Created by Xiamin on 2016/12/11.
 */
public class FloatWindowBigView extends LinearLayout {

    /**
     * 记录大悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录大悬浮窗的高度
     */
    public static int viewHeight;

    public FloatWindowBigView(final Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.float_window_big, this);
        View view = findViewById(R.id.big_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
    }
}