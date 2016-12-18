package applock.anderson.com.moneycounter;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import applock.anderson.com.moneycounter.Utils.PermissionsChecker;
import okhttp3.Call;


public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 111; // 请求码
    // 所需的全部权限

//    <uses-permission android:name="android.permission.INTERNET" />
//    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
//    <uses-permission android:name="android.permission.READ_LOGS" />
//    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
//    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />

    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.READ_PHONE_STATE
    };
    PermissionsChecker permissionsChecker;

    private Button mEnsure;
    private EditText mEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mEditText = (EditText) findViewById(R.id.editText);
        mEnsure = (Button) findViewById(R.id.ensure);
        mEnsure.setOnClickListener(this);

        if (PermissionCheck()) {
            SharedPreferences sp = getSharedPreferences("setting", MODE_PRIVATE);
            String code = sp.getString("code", "");
            if (!TextUtils.isEmpty(code)) {
                checkRegist(code, false);
            }
        }

        //  startActivity(new Intent(WelcomeActivity.this, MainActivity.class));

        //

    }

    private boolean PermissionCheck() {
        permissionsChecker = new PermissionsChecker(this);
        if (permissionsChecker.lacksPermissions(PERMISSIONS)) {
            Log.i("iii", "缺少权限，跳转权限申请界面");
            Intent intent = new Intent(this, PermissionActivity.class);
            intent.putExtra(PermissionActivity.PERMISSION_REQUEST_FLAG, PERMISSIONS);
            startActivityForResult(intent, REQUEST_CODE);
            return false;
        } else {
            Log.i("iii", "拥有所有权限");
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 若权限拒绝
         */
        if (requestCode == REQUEST_CODE && resultCode == PermissionActivity.PERMISSIONS_DENIED) {
            finish();
        } else if (requestCode == REQUEST_CODE && resultCode == PermissionActivity.PERMISSIONS_GRANTED) {
            //startToMain();
            SharedPreferences sp = getSharedPreferences("setting", MODE_PRIVATE);
            String code = sp.getString("code", "");
            if (!TextUtils.isEmpty(code)) {
                checkRegist(code, false);
            }
        }
    }


    private boolean checkRegist(final String yanzhengcode, final boolean isBaocun) {
        String url = "http://107.151.148.153:8080/token/index?method="
                + "index&class=LuaAction&function=pidui&token="
                + yanzhengcode
                + "&id=11";
        OkHttpUtils.get().url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(WelcomeActivity.this, "错误的网络访问请求", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.d("ret  " + response);
                        String[] a = response.split("\\|");
                        Logger.d("string  " + a);
                        if (a != null && a.length >= 2) {
                            if (a[0].equals("true")) {
                                if (a[1].equals("chenggong")) {
                                    if (a[2].equals("11")) {
                                        Toast.makeText(WelcomeActivity.this, "设备验证成功", Toast.LENGTH_SHORT).show();
                                        if (isBaocun) {
                                            SharedPreferences sp = getSharedPreferences("setting", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sp.edit();
                                            editor.putString("code", yanzhengcode);
                                            editor.commit();
                                        }
                                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                                        finish();
                                    }
                                } else if (a[1].equals("jujue")) {
                                    Toast.makeText(WelcomeActivity.this, "软件被停用", Toast.LENGTH_SHORT).show();
                                } else if (a[1].equals("wuxiao")) {
                                    Toast.makeText(WelcomeActivity.this, "无效的授权码", Toast.LENGTH_SHORT).show();
                                } else if (a[1].equals("guoqi")) {
                                    Toast.makeText(WelcomeActivity.this, "授权码已过期", Toast.LENGTH_SHORT).show();
                                } else if (a[1].equals("shibai")) {
                                    Toast.makeText(WelcomeActivity.this, "错误的绑定设备", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(WelcomeActivity.this, "网络访问失败", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(WelcomeActivity.this, "获取服务器数据错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ensure:
                checkRegist(mEditText.getText().toString(), true);
                break;
        }
    }
}
