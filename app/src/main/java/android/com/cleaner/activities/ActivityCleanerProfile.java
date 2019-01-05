package android.com.cleaner.activities;

import android.app.AlertDialog;
import android.com.cleaner.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import am.appwise.components.ni.NoInternetDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityCleanerProfile extends AppCompatActivity implements View.OnClickListener {


    private TextView tvBioScrollable, tvProfileContinue;
    private ImageView backarr;
    private NoInternetDialog noInternetDialog;
    private TextView tvSubmit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaners_details);


        noInternetDialog = new NoInternetDialog.Builder(this).build();

        changingStatusBarColorHere();
        findingIdsHere();
        eventClickLietener();


        tvBioScrollable.setMovementMethod(new ScrollingMovementMethod());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    private void changingStatusBarColorHere() {
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityCleanerProfile.this, R.color.statusBarColor));
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void eventClickLietener() {
        tvProfileContinue.setOnClickListener(this);
        backarr.setOnClickListener(this);


    }

    private void findingIdsHere() {
        tvBioScrollable = findViewById(R.id.tvBioScrollable);
        tvProfileContinue = findViewById(R.id.tvProfileContinue);
        backarr = findViewById(R.id.backarr);

    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {


            case R.id.tvProfileContinue:

                personalInfoOfUserDialog();
                break;


            case R.id.backarr:
                finish();
                break;

        }
    }

    private void personalInfoOfUserDialog() {

        LayoutInflater li = LayoutInflater.from(ActivityCleanerProfile.this);
        View dialogView = li.inflate(R.layout.dialog_personal_info_cleaner, null);


        findingCleanerInfoIdsHere(dialogView);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityCleanerProfile.this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder
                .setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


    }

    private void findingCleanerInfoIdsHere(View dialogView) {

        tvSubmit = dialogView.findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityCleanerProfile.this, "SUBMITTING", Toast.LENGTH_SHORT).show();

            }
        });


    }
}







