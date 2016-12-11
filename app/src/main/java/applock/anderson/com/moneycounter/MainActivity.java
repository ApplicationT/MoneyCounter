package applock.anderson.com.moneycounter;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.util.List;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initView();

        Logger.d("开始 监听AccessibilityService 变化");
        //监听AccessibilityService 变化
        accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            accessibilityManager.addAccessibilityStateChangeListener(this);
        }
        MyWindowManager.createSmallWindow(getApplicationContext());
    }

    private void initView() {
        mStartButton = (Button) findViewById(R.id.start_plugin);
        mSettingButton = (Button) findViewById(R.id.start_setting);
        mStartButton.setOnClickListener(this);
        mSettingButton.setOnClickListener(this);
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
            case R.id.start_setting:
                break;
        }
    }

    public void openAccessibility() {
        try {
            Toast.makeText(this, "点击「海贼王插件」选择打开或者关闭", Toast.LENGTH_SHORT).show();
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
}
