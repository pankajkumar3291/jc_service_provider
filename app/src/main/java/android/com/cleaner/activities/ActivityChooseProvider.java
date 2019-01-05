package android.com.cleaner.activities;

import android.com.cleaner.R;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import am.appwise.components.ni.NoInternetDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ActivityChooseProvider extends AppCompatActivity implements View.OnClickListener {


    private ImageView imageCustomer, imageProvider;
    private Context context;
    private NoInternetDialog noInternetDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_provider);

        context = this;

        noInternetDialog = new NoInternetDialog.Builder(this).build();

        findingIdsHere();
        eventListener();
        changingStatusBarColorHere();
        animationHere();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void animationHere() {

        Animation animationCustomer = AnimationUtils.loadAnimation(context, R.anim.swing_up_left);
        Animation animationProvider = AnimationUtils.loadAnimation(context, R.anim.swing_up_right);

        imageCustomer.startAnimation(animationCustomer);
        imageProvider.startAnimation(animationProvider);


    }

    private void changingStatusBarColorHere() {
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityChooseProvider.this, R.color.statusBarColor));


    }

    private void eventListener() {


        imageCustomer.setOnClickListener(this);
        imageProvider.setOnClickListener(this);

    }

    private void findingIdsHere() {

        imageCustomer = findViewById(R.id.imageCustomer);
        imageProvider = findViewById(R.id.imageProvider);


    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.imageCustomer:

                Intent intentCustomer = new Intent(ActivityChooseProvider.this, ActivitySignIn.class);
                startActivity(intentCustomer);
                break;


            case R.id.imageProvider:


                Intent intentProvider = new Intent(ActivityChooseProvider.this, ActivitySignIn.class);
                startActivity(intentProvider);
                break;

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
