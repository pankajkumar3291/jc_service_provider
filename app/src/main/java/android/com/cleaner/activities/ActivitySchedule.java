package android.com.cleaner.activities;

import android.app.AlertDialog;
import android.com.cleaner.R;
import android.com.cleaner.adapters.DailyAdapter;
import android.com.cleaner.adapters.WeeklyAdapter;
import android.com.cleaner.apiResponses.weeklyJobScheduled.AllWorkingDays;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.com.cleaner.interfaces.ClickListnerForWeeklyJobScheduled;
import android.com.cleaner.models.Time;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ActivitySchedule extends AppCompatActivity implements View.OnClickListener { // , ClickListnerForWeeklyJobScheduled


    public RecyclerView recyclerViewDaily, recyclerViewWeekly, recyclerViewMonthly;
    private CheckBox checkDaily, checkWeekly, checkMonthly;
    private TextView tvContinueBtn;
    private ImageView backArrowImage;


    private List<Time> timeList;
    private Context mCtx;

    private NoInternetDialog noInternetDialog;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;


    private TextView select_zipcode;
    ArrayList<String> items = new ArrayList<>();
    SpinnerDialog spinnerDialog;


    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public ClickListnerForWeeklyJobScheduled clickListnerForWeeklyJobScheduled;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        noInternetDialog = new NoInternetDialog.Builder(this).build();
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivitySchedule.this, R.color.statusBarColor));

//        clickListnerForWeeklyJobScheduled = this;

        findingIdsHere();
        eventsListenerHere();
        checkBoxColorCheckedUnchecked();


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


    @Override
    protected void onResume(){
        super.onResume();
        performOperationsHere();
    }

    private void checkBoxColorCheckedUnchecked(){

        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked}, // unchecked
                        new int[]{android.R.attr.state_checked}, // checked
                },
                new int[]{
                        Color.parseColor("#FFFFFF"),
                        Color.parseColor("#FF182391"),
                }
        );

        CompoundButtonCompat.setButtonTintList(checkDaily, colorStateList);
        CompoundButtonCompat.setButtonTintList(checkWeekly, colorStateList);
        CompoundButtonCompat.setButtonTintList(checkMonthly, colorStateList);


    }

    private void eventsListenerHere() {
        tvContinueBtn.setOnClickListener(this);
        backArrowImage.setOnClickListener(this);
    }

    private void performOperationsHere() {

        checkDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                checkBoxColorCheckedUnchecked();

                if (checkDaily.isChecked()) {

                    selectYourDailyScheduling();


//                    checkDaily.setTextColor(Color.parseColor("#FF182391"));
//                    checkMonthly.setTextColor(Color.parseColor("#FFFFFF"));
//                    checkWeekly.setTextColor(Color.parseColor("#FFFFFF"));
//                    recyclerViewDaily.setVisibility(View.VISIBLE);
//                    checkWeekly.setChecked(false);
//                    checkMonthly.setChecked(false);
//
//                    callDailyAdapterHere();

                } else {
                    recyclerViewDaily.setVisibility(View.GONE);
                    checkDaily.setTextColor(Color.parseColor("#FFFFFF"));

                }

            }
        });

        checkWeekly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBoxColorCheckedUnchecked();
                if (checkWeekly.isChecked()) {
                    selectYourWeeklyJobScheduling();
                    checkWeekly.setTextColor(Color.parseColor("#FF182391"));
                    checkDaily.setTextColor(Color.parseColor("#FFFFFF"));
                    recyclerViewWeekly.setVisibility(View.VISIBLE);
                    recyclerViewDaily.setVisibility(View.GONE);
                    TastyToast.makeText(ActivitySchedule.this, "checkWeekly.isChecked", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                    checkDaily.setChecked(false);
                    checkMonthly.setChecked(false);
//                    callWeeklyAdapterHere();
                } else {
                    recyclerViewWeekly.setVisibility(View.GONE);
                    recyclerViewDaily.setVisibility(View.GONE);
                    checkWeekly.setTextColor(Color.parseColor("#FFFFFF"));

                }

            }
        });


        checkMonthly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                checkBoxColorCheckedUnchecked();

                if (checkMonthly.isChecked()) {
//                    awesomeDialogForInfo();

                    checkMonthly.setTextColor(Color.parseColor("#FF182391"));
                    checkWeekly.setTextColor(Color.parseColor("#FFFFFF"));
                    checkDaily.setTextColor(Color.parseColor("#FFFFFF"));

                    recyclerViewMonthly.setVisibility(View.VISIBLE);
                    recyclerViewDaily.setVisibility(View.GONE);
                    recyclerViewWeekly.setVisibility(View.GONE);
//                    TastyToast.makeText(ActivitySchedule.this, "checkMonthly.isChecked", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                    checkDaily.setChecked(false);
                    checkWeekly.setChecked(false);


                } else {
                    recyclerViewMonthly.setVisibility(View.GONE);
                    recyclerViewDaily.setVisibility(View.GONE);
                    recyclerViewWeekly.setVisibility(View.GONE);
                    checkMonthly.setTextColor(Color.parseColor("#FFFFFF"));


                }

            }
        });


    }

    private void awesomeDialogForInfo() {


        new AwesomeInfoDialog(this)
                .setTitle("Great")
                .setMessage("Click continue to schedule your monthly job")
                .setColoredCircle(R.color.dialogInfoBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                .setCancelable(true)
                .setPositiveButtonText("Ok")
                .setPositiveButtonbackgroundColor(R.color.dialogInfoBackgroundColor)
                .setPositiveButtonTextColor(R.color.white)
//                .setNeutralButtonText(getString(R.string.dialog_neutral_button))
//                .setNeutralButtonbackgroundColor(R.color.dialogInfoBackgroundColor)
//                .setNeutralButtonTextColor(R.color.white)
//                .setNegativeButtonText(getString(R.string.dialog_no_button))
//                .setNegativeButtonbackgroundColor(R.color.dialogInfoBackgroundColor)
//                .setNegativeButtonTextColor(R.color.white)
                .setPositiveButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        //click
                    }
                })
//                .setNeutralButtonClick(new Closure() {
//                    @Override
//                    public void exec() {
//                        //click
//                    }
//                })
//                .setNegativeButtonClick(new Closure() {
//                    @Override
//                    public void exec() {
//                        //click
//                    }
//                })
                .show();

    }

    private void selectYourWeeklyJobScheduling() {

        Intent intent = new Intent(ActivitySchedule.this, ActivityWeeklyJobScheduling.class);
        startActivity(intent);
        finish();


    }

    private void selectYourDailyScheduling() {
        Intent intent = new Intent(ActivitySchedule.this, ActivityDailyJobSchedule.class);
        startActivity(intent);
        finish();
    }


    private void callWeeklyAdapterHere() {


        compositeDisposable.add(HttpModule.provideRepositoryService().allWorkingDays().
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<AllWorkingDays>() {

                    @Override
                    public void accept(AllWorkingDays allWorkingDays) throws Exception {


                        if (allWorkingDays != null && allWorkingDays.getIsSuccess()) {

                            TastyToast.makeText(ActivitySchedule.this, allWorkingDays.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

//                            WeeklyAdapter weeklyAdapter = new WeeklyAdapter(ActivitySchedule.this, allWorkingDays.getPayload(), clickListnerForWeeklyJobScheduled);
                            recyclerViewWeekly.setHasFixedSize(true);
                            recyclerViewWeekly.setLayoutManager(new LinearLayoutManager(ActivitySchedule.this));
//                            recyclerViewWeekly.setAdapter(weeklyAdapter);


                        } else {
                            TastyToast.makeText(ActivitySchedule.this, Objects.requireNonNull(allWorkingDays).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }

                    }

                }, new Consumer<Throwable>() {

                    @Override
                    public void accept(Throwable throwable) throws Exception {


                        TastyToast.makeText(ActivitySchedule.this, "Something went wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                    }


                }));


    }

    private void findingIdsHere() {

        checkDaily = findViewById(R.id.checkDaily);
        checkWeekly = findViewById(R.id.checkWeekly);
        checkMonthly = findViewById(R.id.checkMonthly);


        recyclerViewDaily = findViewById(R.id.recyclerViewDaily);
        recyclerViewWeekly = findViewById(R.id.recyclerViewWeekly);
        recyclerViewMonthly = findViewById(R.id.recyclerViewMonthly);


        tvContinueBtn = findViewById(R.id.tvContinueBtn);
        backArrowImage = findViewById(R.id.backArrowImage);


    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.tvContinueBtn:

                if (checkMonthly.isChecked() && !checkDaily.isChecked() && !checkWeekly.isChecked()) {

//                    Intent intent = new Intent(ActivitySchedule.this, ActivityScheduleMonthly.class);
//                    startActivity(intent);
                }
//                else {
//
//                    TastyToast.makeText(this, "Select monthly to continue", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
//                }

                break;


            case R.id.backArrowImage:
                finish();
                break;


        }


    }


//    @Override
//    public void weeklyJobScheduledAdapterItemClicked(List<String> stringslist) {
//
//        System.out.println("ActivitySchedule.weeklyJobScheduledAdapterItemClicked " + stringslist);
//
//
//    }


}
