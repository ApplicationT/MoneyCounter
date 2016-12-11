package applock.anderson.com.moneycounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.Logger;


public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.init("MoneyCounter");
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }
}
