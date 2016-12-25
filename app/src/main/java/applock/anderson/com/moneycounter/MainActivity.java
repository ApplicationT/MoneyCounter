package applock.anderson.com.moneycounter;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kyleduo.switchbutton.SwitchButton;
import com.orhanobut.logger.Logger;

import java.util.List;

import applock.anderson.com.moneycounter.Utils.SettingsContact;
import applock.anderson.com.moneycounter.services.CountService;
import applock.anderson.com.moneycounter.view.MyWindowManager;

/**
 * Created by Xiamin on 2016/12/11.
 */

public class MainActivity extends AppCompatActivity implements
        AccessibilityManager.AccessibilityStateChangeListener,
        View.OnClickListener {
    //AccessibilityService 管理
    private AccessibilityManager accessibilityManager;
    private Button mStartButton;
    private Button mSettingButton;
    private SwitchButton mOpenButton;
    private SwitchButton mShunziButton;
    private SwitchButton mBaoziButton;
    private SwitchButton mFloatButton;
    private SwitchButton mGetMoney;
    private SwitchButton mStartMusic;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton1;
    private RadioButton mRadioButton2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initView();
        handleMaterialStatusBar();
        Logger.d("开始 监听AccessibilityService 变化");
        //监听AccessibilityService 变化
        accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            accessibilityManager.addAccessibilityStateChangeListener(this);
        }
//        LayoutInflater inflater = getLayoutInflater();
//        View layout = inflater.inflate(R.layout.toast_item, (ViewGroup) findViewById(R.id.toast_content));
//        Toast toast =
//                Toast.makeText(getApplicationContext(), "请认准正版雷中雷10.0", Toast.LENGTH_LONG);
//        toast.setView(layout);
//        toast.show();
       MyWindowManager.createToast(getApplicationContext(),"请认准正版雷中雷");
    }

    private void initView() {
        mStartButton = (Button) findViewById(R.id.start_plugin);
        mStartButton.setOnClickListener(this);
        mOpenButton = (SwitchButton) findViewById(R.id.btn_1);
        mShunziButton = (SwitchButton) findViewById(R.id.btn_2);
        mBaoziButton = (SwitchButton) findViewById(R.id.btn_3);
        mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        mGetMoney = (SwitchButton) findViewById(R.id.but_open_getmoney);
        mFloatButton = (SwitchButton) findViewById(R.id.btn_5);
        mStartMusic = (SwitchButton) findViewById(R.id.btn_6);
        mRadioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        mRadioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        mRadioGroup.setOnCheckedChangeListener(mRadioListener);
        SharedPreferences sp = getSharedPreferences("setting", Context.MODE_PRIVATE);
        if (sp != null) {
            mOpenButton.setChecked(sp.getBoolean(SettingsContact.OPEN, false));
            mShunziButton.setChecked(sp.getBoolean(SettingsContact.SHUNZI, false));
            mBaoziButton.setChecked(sp.getBoolean(SettingsContact.BAOZI, false));
            mFloatButton.setChecked(sp.getBoolean(SettingsContact.FLOAT, false));
            mGetMoney.setChecked(sp.getBoolean(SettingsContact.GET_MONEY, false));
            mStartMusic.setChecked(sp.getBoolean(SettingsContact.MUSIC, true));
            if (sp.getInt(SettingsContact.WEIZHI, 2) == 2) {
                mRadioButton2.setChecked(true);
                mRadioButton1.setChecked(false);
            } else {
                mRadioButton1.setChecked(true);
                mRadioButton2.setChecked(false);
            }
        }
        mFloatButton.setOnCheckedChangeListener(mfloatListener);
        mOpenButton.setOnCheckedChangeListener(mOpenListener);
        mShunziButton.setOnCheckedChangeListener(mShunziListener);
        mBaoziButton.setOnCheckedChangeListener(mBaoziListener);
        mGetMoney.setOnCheckedChangeListener(mOpenMoneyListener);
        mStartMusic.setOnCheckedChangeListener(mOpenMusicListener);
    }

    /**
     * 获取 HongbaoService 是否启用状态
     *
     * @return
     */
    private boolean isServiceEnabled() {
        List<AccessibilityServiceInfo> accessibilityServices =
                null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            accessibilityServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        }
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                if (info.getId().equals(getPackageName() + "/.services.CounteService")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onAccessibilityStateChanged(boolean enabled) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_plugin:
                openAccessibility();
                break;
        }
    }

    public void openAccessibility() {
        try {
            Toast.makeText(this, "点击「雷中雷插件」选择打开或者关闭", Toast.LENGTH_SHORT).show();
            Intent accessibleIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(accessibleIntent);
        } catch (Exception e) {
            Toast.makeText(this, "遇到一些问题,请手动打开系统设置>无障碍服务>海贼王插件", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /**
     * 适配MIUI沉浸状态栏
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void handleMaterialStatusBar() {
        // Not supported in APK level lower than 21
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return;
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(0xffE46C62);
    }

    private SwitchButton.OnCheckedChangeListener mOpenListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SettingsContact.OPEN, true);
                editor.commit();
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SettingsContact.OPEN, false);
                editor.commit();
            }
        }
    };

    private SwitchButton.OnCheckedChangeListener mShunziListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SettingsContact.SHUNZI, true);
                editor.commit();
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SettingsContact.SHUNZI, false);
                editor.commit();
            }
        }
    };

    private SwitchButton.OnCheckedChangeListener mBaoziListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SettingsContact.BAOZI, true);
                editor.commit();
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SettingsContact.BAOZI, false);
                editor.commit();
            }
        }
    };

    private SwitchButton.OnCheckedChangeListener mfloatListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SettingsContact.FLOAT, true);
                editor.commit();
                //   MyWindowManager.createSmallWindow(getApplicationContext());

            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SettingsContact.FLOAT, false);
                editor.commit();
                //     MyWindowManager.removeBigWindow(getApplicationContext());
                //     MyWindowManager.removeSmallWindow(getApplicationContext());
            }
        }
    };

    private SwitchButton.OnCheckedChangeListener mOpenMoneyListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SettingsContact.GET_MONEY, true);
                Log.i(CountService.TAG, "打开避雷开关");
                editor.commit();
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SettingsContact.GET_MONEY, false);
                Log.i(CountService.TAG, "关闭避雷开关");
                editor.commit();
            }
        }
    };

    private SwitchButton.OnCheckedChangeListener mOpenMusicListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SettingsContact.MUSIC, true);
                editor.commit();
                if (mp != null) {
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                }
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SettingsContact.MUSIC, false);
                editor.commit();
                if (mp != null) mp.pause();
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener mRadioListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.radioButton1) {
                SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(SettingsContact.WEIZHI, 1);
                editor.commit();
            } else if (checkedId == R.id.radioButton2) {
                SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(SettingsContact.WEIZHI, 2);
                editor.commit();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyWindowManager.removeBigWindow(getApplicationContext());
        MyWindowManager.removeSmallWindow(getApplicationContext());
    }

    /**
     * 播放背景音乐部分
     */
    private MediaPlayer mp = new MediaPlayer();

    @Override
    protected void onResume() {
        super.onResume();
        mp = MediaPlayer.create(this, R.raw.music);
        SharedPreferences sp = getSharedPreferences("setting", MODE_PRIVATE);
        if (mp != null && sp.getBoolean(SettingsContact.MUSIC, true)) {
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.start();
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mp != null && mp.isPlaying()) {
            mp.pause();
        }
    }
}
