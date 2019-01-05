package android.com.cleaner.activities;

import android.com.cleaner.R;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;

import am.appwise.components.ni.NoInternetDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ActivityOrderConfirmation extends AppCompatActivity implements View.OnClickListener {


    private TextView tvCongrates, tvAddressReach, tvViewOrderDetail;
    private ImageView imageCallToCleaner, imageMessageToCleaner, imageCallSupport, imageEmailSupport;


    private NoInternetDialog noInternetDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);


        noInternetDialog = new NoInternetDialog.Builder(this).build();

        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityOrderConfirmation.this, R.color.statusBarColor));

        findingIdsHere();
        eventListener();


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


    private void eventListener() {

        tvViewOrderDetail.setOnClickListener(this);
        imageCallToCleaner.setOnClickListener(this);
        imageMessageToCleaner.setOnClickListener(this);
        imageCallSupport.setOnClickListener(this);
        imageEmailSupport.setOnClickListener(this);

    }

    private void findingIdsHere() {

        tvCongrates = findViewById(R.id.tvCongrates);
        tvAddressReach = findViewById(R.id.tvAddressReach);
        tvViewOrderDetail = findViewById(R.id.tvViewOrderDetail);


        imageCallToCleaner = findViewById(R.id.imageCallToCleaner);
        imageMessageToCleaner = findViewById(R.id.imageMessageToCleaner);
        imageCallSupport = findViewById(R.id.imageCallSupport);
        imageEmailSupport = findViewById(R.id.imageEmailSupport);


    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {


            case R.id.tvViewOrderDetail:
                TastyToast.makeText(ActivityOrderConfirmation.this, "See your order details", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                break;

            case R.id.imageCallToCleaner:
                TastyToast.makeText(ActivityOrderConfirmation.this, "Call the cleaner", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                break;

            case R.id.imageMessageToCleaner:
                TastyToast.makeText(ActivityOrderConfirmation.this, "Message to cleaner", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                break;

            case R.id.imageCallSupport:
                TastyToast.makeText(ActivityOrderConfirmation.this, "Call support", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                break;


            case R.id.imageEmailSupport:
                TastyToast.makeText(ActivityOrderConfirmation.this, "Email support", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                break;


        }


    }
}
