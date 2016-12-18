package applock.anderson.com.moneycounter.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import applock.anderson.com.moneycounter.R;
import applock.anderson.com.moneycounter.Utils.SettingsContact;

/**
 * Created by Xiamin on 2016/12/11.
 */
public class FloatWindowBigView extends LinearLayout {


    public static int viewWidth;    //记录大悬浮窗的宽度
    public static int viewHeight;   //记录大悬浮窗的高度

    View view;
    TextView mNo1_text;
    TextView mNo2_text;
    TextView mNo3_text;
    TextView mNo4_text;
    TextView mNo5_text;
    ImageView mStarImageView;
    View mBaoziLayout;
    View mShunziLayout;
    TextView mBaozi_text;
    TextView mShunzi_text;

    public FloatWindowBigView(final Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.float_window_big, this);
        view = findViewById(R.id.big_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        initView();
    }

    private void initView() {
        mNo1_text = (TextView) view.findViewById(R.id.no1_lei_text);
        mNo2_text = (TextView) view.findViewById(R.id.no2_lei_text);
        mNo3_text = (TextView) view.findViewById(R.id.no3_lei_text);
        mNo4_text = (TextView) view.findViewById(R.id.no4_lei_text);
        mNo5_text = (TextView) view.findViewById(R.id.no5_lei_text);
        mBaozi_text = (TextView) view.findViewById(R.id.tv_baozi);
        mShunzi_text = (TextView) view.findViewById(R.id.tv_shunzi);
        mStarImageView = (ImageView) view.findViewById(R.id.star_imageView);
        mBaoziLayout = view.findViewById(R.id.baozi_layout);
        mShunziLayout = view.findViewById(R.id.shunzi_layout);

        RotateAnimation rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        rotate.setDuration(1500);//设置动画持续时间
        rotate.setRepeatCount(-1);//设置重复次数
        rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        mStarImageView.setAnimation(rotate);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("setting", Context.MODE_PRIVATE);
        mBaoziLayout.setVisibility(sharedPreferences.getBoolean(SettingsContact.BAOZI, false) ? VISIBLE : GONE);
        mShunziLayout.setVisibility(sharedPreferences.getBoolean(SettingsContact.SHUNZI, false) ? VISIBLE : GONE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(SettingsContact.OPEN)) {
                    if (sharedPreferences.getBoolean(key, false)) {

                    } else {

                    }
                } else if (key.equals(SettingsContact.BAOZI)) {
                    if (sharedPreferences.getBoolean(key, false)) {
                        mBaoziLayout.setVisibility(VISIBLE);
                    } else {
                        mBaoziLayout.setVisibility(GONE);
                    }
                } else if (key.equals(SettingsContact.SHUNZI)) {
                    if (sharedPreferences.getBoolean(key, false)) {
                        mShunziLayout.setVisibility(VISIBLE);
                    } else {
                        mShunziLayout.setVisibility(GONE);
                    }
                } else if (key.equals(SettingsContact.WEIZHI)) {

                } else if (key.equals(SettingsContact.FLOAT)) {

                } else if (key.equals(SettingsContact.GET_MONEY)) {

                }

            }
        });

    }

    public void upDateView(String[] dataLei,String baozi,String shunzi){
        if(dataLei == null || baozi == null || shunzi == null) {
            return;
        }
        for(int i = 0; i < dataLei.length ; i++){
            if(!TextUtils.isEmpty(dataLei[i])); {
                switch (i) {
                    case 0:
                        mNo1_text.setText(dataLei[i]);
                        break;
                    case 1:
                        mNo2_text.setText(dataLei[i]);
                        break;
                    case 2:
                        mNo3_text.setText(dataLei[i]);
                        break;
                    case 3:
                        mNo4_text.setText(dataLei[i]);
                        break;
                    case 4:
                        mNo5_text.setText(dataLei[i]);
                        break;
                }


            }
        }
        mBaozi_text.setText(baozi);
        mShunzi_text.setText(shunzi);
    }

}