package applock.anderson.com.moneycounter.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import applock.anderson.com.moneycounter.R;

/**
 * Created by Xiamin on 2016/12/31.
 */

public class FloatFourierView extends LinearLayout{
    View view;
    TextView textView;
    public static int viewWidth;    //记录大悬浮窗的宽度
    public static int viewHeight;   //记录大悬浮窗的高度
    public FloatFourierView(Context context,int data) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.fourier_layout, this);
        view = findViewById(R.id.fourier_layout_c);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        textView = (TextView) view.findViewById(R.id.fouier_data);
        textView.setText("" + data);
    }
}

