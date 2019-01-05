package android.com.cleaner.activities;

import android.com.cleaner.R;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;

import am.appwise.components.ni.NoInternetDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityAboutUs extends AppCompatActivity implements View.OnClickListener {


    private TextView tvCompanyInfo, tvOffredServices, tvFAQ, tvTermsAndConditions, tvBack;

    private NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);


        noInternetDialog = new NoInternetDialog.Builder(this).build();


        changingStatusBarColorHere();
        findingIdsHere();
        eventsListner();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    private void changingStatusBarColorHere() {
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityAboutUs.this, R.color.statusBarColor));
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void eventsListner() {

        tvCompanyInfo.setOnClickListener(this);
        tvOffredServices.setOnClickListener(this);
        tvFAQ.setOnClickListener(this);
        tvTermsAndConditions.setOnClickListener(this);
        tvBack.setOnClickListener(this);


    }

    private void findingIdsHere() {

        tvCompanyInfo = findViewById(R.id.tvCompanyInfo);
        tvOffredServices = findViewById(R.id.tvOffredServices);
        tvFAQ = findViewById(R.id.tvFAQ);
        tvTermsAndConditions = findViewById(R.id.tvTermsAndConditions);
        tvBack = findViewById(R.id.tvBack);

    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {


            case R.id.tvCompanyInfo:


                TastyToast.makeText(ActivityAboutUs.this, "Company info", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                break;


            case R.id.tvOffredServices:

                TastyToast.makeText(ActivityAboutUs.this, "Offred Services", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                break;


            case R.id.tvFAQ:

                TastyToast.makeText(ActivityAboutUs.this, "FAQ", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                break;


            case R.id.tvTermsAndConditions:
                TastyToast.makeText(ActivityAboutUs.this, "Terms and Conditions", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                break;


            case R.id.tvBack:


                TastyToast.makeText(ActivityAboutUs.this, "Back", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                break;

        }


    }


}
