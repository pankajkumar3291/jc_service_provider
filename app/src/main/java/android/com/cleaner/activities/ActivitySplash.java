package android.com.cleaner.activities;

import android.com.cleaner.R;
import android.com.cleaner.sharedPrefrence.SharedPrefsHelper;
import android.com.cleaner.utils.AppConstant;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;

import am.appwise.components.ni.NoInternetDialog;


public class ActivitySplash extends AppCompatActivity {

    public boolean isChecked = false;
    private NoInternetDialog noInternetDialog;
    private SharedPrefsHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        noInternetDialog = new NoInternetDialog.Builder(this).build();
        Hawk.init(this).build();
        helper = new SharedPrefsHelper(ActivitySplash.this);

        changingStatusBarColorHere();
        splashTiming();
    }


    private void splashTiming() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if (helper.get(AppConstant.KEEP_ME_LOGGED_IN, false)) {
//
//                    Toast.makeText(ActivitySplash.this, "True", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ActivitySplash.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();

                } else {
//                    Toast.makeText(ActivitySplash.this, "False", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ActivitySplash.this, ActivitySelectLanguages.class);
                    startActivity(intent);
                    finish();

                }


            }
        }, 2000);
    }


    private void changingStatusBarColorHere() {
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivitySplash.this, R.color.statusBarColor));

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }


}
