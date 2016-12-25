package applock.anderson.com.moneycounter.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import applock.anderson.com.moneycounter.R;

/**
 * Created by Xiamin on 2016/12/24.
 */

public class ToastView extends LinearLayout {
    View view;
    TextView mTextView;
    public static int viewWidth;    //记录大悬浮窗的宽度
    public static int viewHeight;   //记录大悬浮窗的高度
    public ToastView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.toast_item, this);
        view = findViewById(R.id.toast_content);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
    }

    public ToastView(Context context,String string) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.toast_item, this);
        view = findViewById(R.id.toast_content);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;

        mTextView = (TextView) view.findViewById(R.id.message);
        mTextView.setText(string);
    }

}
