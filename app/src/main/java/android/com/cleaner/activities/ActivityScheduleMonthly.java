package android.com.cleaner.activities;

import android.com.cleaner.R;
import android.com.cleaner.adapters.MonthlyAdapter;
import android.com.cleaner.adapters.WeeklyAdapter;
import android.com.cleaner.models.Time;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityScheduleMonthly extends AppCompatActivity implements View.OnClickListener {


    private ImageView backArrowImage;
    private TextView tvContinueBtn;
    private RecyclerView recylerViewMonthly;


    private List<Time> list;

    private NoInternetDialog noInternetDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_monthly);

        noInternetDialog = new NoInternetDialog.Builder(this).build();

        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityScheduleMonthly.this, R.color.statusBarColor));

        findingIdsHere();
        eventsListenerHere();
        callMonthlyAdapterHere();


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


    private void callMonthlyAdapterHere() {


        list = new ArrayList<>();

        list.add(new Time("1", "10:00AM"));
        list.add(new Time("2", "10:00AM"));
        list.add(new Time("3", "10:00AM"));
        list.add(new Time("4", "10:00AM"));
        list.add(new Time("5", "10:00AM"));
        list.add(new Time("6", "10:00AM"));
        list.add(new Time("7", "10:00AM"));
        list.add(new Time("8", "10:00AM"));
        list.add(new Time("9", "10:00AM"));
        list.add(new Time("10", "10:00AM"));
        list.add(new Time("11", "10:00AM"));
        list.add(new Time("12", "10:00AM"));
        list.add(new Time("13", "10:00AM"));
        list.add(new Time("14", "10:00AM"));
        list.add(new Time("15", "10:00AM"));
        list.add(new Time("16", "10:00AM"));
        list.add(new Time("17", "10:00AM"));
        list.add(new Time("18", "10:00AM"));
        list.add(new Time("19", "10:00AM"));
        list.add(new Time("20", "10:00AM"));
        list.add(new Time("21", "10:00AM"));
        list.add(new Time("22", "10:00AM"));
        list.add(new Time("23", "10:00AM"));
        list.add(new Time("24", "10:00AM"));
        list.add(new Time("25", "10:00AM"));
        list.add(new Time("26", "10:00AM"));
        list.add(new Time("27", "10:00AM"));
        list.add(new Time("28", "10:00AM"));
        list.add(new Time("29", "10:00AM"));
        list.add(new Time("30", "10:00AM"));


        MonthlyAdapter monthlyAdapter = new MonthlyAdapter(ActivityScheduleMonthly.this, list);
        recylerViewMonthly.setHasFixedSize(true);
        recylerViewMonthly.setLayoutManager(new LinearLayoutManager(this));
        recylerViewMonthly.setAdapter(monthlyAdapter);


    }

    private void eventsListenerHere() {

        backArrowImage.setOnClickListener(this);
        tvContinueBtn.setOnClickListener(this);

    }

    private void findingIdsHere() {


        backArrowImage = findViewById(R.id.backArrowImage);
        tvContinueBtn = findViewById(R.id.tvContinueBtn);
        recylerViewMonthly = findViewById(R.id.recylerViewMonthly);


    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.backArrowImage:
                finish();
                break;


            case R.id.tvContinueBtn:

                Toast.makeText(ActivityScheduleMonthly.this, "Continues", Toast.LENGTH_SHORT).show();

                break;

        }

    }
}
