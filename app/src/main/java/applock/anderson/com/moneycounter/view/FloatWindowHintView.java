package applock.anderson.com.moneycounter.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import applock.anderson.com.moneycounter.R;
import applock.anderson.com.moneycounter.services.CountService;

/**
 * Created by Xiamin on 2016/12/17.
 */

public class FloatWindowHintView extends LinearLayout {
    View view;

    TextView jianyizhi;
    TextView tishi;
    public static int viewWidth;    //记录大悬浮窗的宽度
    public static int viewHeight;   //记录大悬浮窗的高度

    public FloatWindowHintView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.hint_layout, this);
        view = findViewById(R.id.hint_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        jianyizhi = (TextView) view.findViewById(R.id.jianyi);
        tishi = (TextView) view.findViewById(R.id.tishi);
    }

    public FloatWindowHintView(Context context, String jianyi, String ts) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.hint_layout, this);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        jianyizhi = (TextView) view.findViewById(R.id.jianyi);
        tishi = (TextView) view.findViewById(R.id.tishi);
        jianyizhi.setText(jianyi);
        tishi.setText(ts);
    }

    public void upDateUI(String jianyi, String ts) {
        Log.d(CountService.TAG, "" + jianyi + ts);
        jianyizhi.setText(jianyi);
        tishi.setText(ts);
    }
}
