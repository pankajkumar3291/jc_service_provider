package android.com.cleaner.activities;

import android.com.cleaner.R;
import android.com.cleaner.adapters.MonthlyAdapter;
import android.com.cleaner.apiResponses.getAllZipcode.GetAllZipcodeMain;
import android.com.cleaner.apiResponses.getAllZipcode.Payload;
import android.com.cleaner.apiResponses.typesOfServices.TypesOfSerives;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.com.cleaner.models.Time;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import io.apptik.widget.multiselectspinner.BaseMultiSelectSpinner;
import io.apptik.widget.multiselectspinner.MultiSelectSpinner;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityScheduleMonthly extends AppCompatActivity implements View.OnClickListener {


    private ImageView backArrowImage;
    private TextView tvContinueBtn, tvSelect_zipcode;
    private RecyclerView recylerViewMonthly;


    private List<Time> list;

    private NoInternetDialog noInternetDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private List<String> items = new ArrayList<>();
    private SpinnerDialog spinnerDialog;
    private String selectedZipcode;
    private ArrayList<String> serviceTypes;
    private MultiSelectSpinner multiselectSpinner;
    private List<String> idsListServiceTypes;
    private String finalServicesIds;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_monthly);

        noInternetDialog = new NoInternetDialog.Builder(this).build();

        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityScheduleMonthly.this, R.color.statusBarColor));

        findingIdsHere();
        eventsListenerHere();
        selectYourZopcodeOpener();
        typesOfSerives();
        callMonthlyAdapterHere();


    }

    private void typesOfSerives() {

        HttpModule.provideRepositoryService().makingTypesOfServices(Hawk.get("spanish",false)?"es":"en").enqueue(new Callback<TypesOfSerives>() {
            @Override
            public void onResponse(Call<TypesOfSerives> call, Response<TypesOfSerives> response) {


                if (response.body() != null && response.body().getIsSuccess()) {

                    setMultiSpinner(response.body().getPayload());


                } else {
                    TastyToast.makeText(ActivityScheduleMonthly.this, Objects.requireNonNull(response.body()).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                }

            }

            @Override
            public void onFailure(Call<TypesOfSerives> call, Throwable t) {

                System.out.println("ActivityDailyJobSchedule.onFailure " + t.toString());
                TastyToast.makeText(ActivityScheduleMonthly.this, "Something went wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();


            }
        });


    }

    private void setMultiSpinner(final List<android.com.cleaner.apiResponses.typesOfServices.Payload> payload) {


        serviceTypes = new ArrayList<>();

        for (int i = 0; i < payload.size(); i++) {

            serviceTypes.add(payload.get(i).getName());
        }

        multiselectSpinner = findViewById(R.id.multiselectSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, serviceTypes);

        multiselectSpinner.setListener(new BaseMultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {

                idsListServiceTypes = new ArrayList<>();

                for (int temp = 0; temp < selected.length; temp++) {

                    if (selected[temp]) {

                        idsListServiceTypes.add(String.valueOf(payload.get(temp).getId()));

                    }

                    String idList = idsListServiceTypes.toString();
                    finalServicesIds = idList.substring(1, idList.length() - 1).replace(", ", ",");
                    System.out.println("ActivityBookCleaner.onItemsSelected " + finalServicesIds);


                }


            }
        });
        multiselectSpinner.setListAdapter(adapter).setSelectAll(false).setAllCheckedText("All types")
                .setAllUncheckedText("none selected")
                .setSelectAll(false);


    }

    private void selectYourZopcodeOpener() {

        compositeDisposable.add(HttpModule.provideRepositoryService().getAllZipcodeAPI().
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<GetAllZipcodeMain>() {

                    @Override
                    public void accept(GetAllZipcodeMain getAllZipcodeMain) throws Exception {


                        if (getAllZipcodeMain != null && getAllZipcodeMain.getIsSuccess()) {

                            gettingAllZipcodes(getAllZipcodeMain.getPayload());

                        } else {

                            TastyToast.makeText(ActivityScheduleMonthly.this, Objects.requireNonNull(getAllZipcodeMain).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }

                    }

                }, new Consumer<Throwable>() {

                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        TastyToast.makeText(ActivityScheduleMonthly.this, throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                    }

                }));

    }

    private void gettingAllZipcodes(List<Payload> payload) {


        for (int x = 0; x < payload.size(); x++) {

            items.add(payload.get(x).getZipcode());
        }

        spinnerDialog = new SpinnerDialog(ActivityScheduleMonthly.this, (ArrayList<String>) items, "Select or Search Zipcode", "Close");// With No Animation
        spinnerDialog = new SpinnerDialog(ActivityScheduleMonthly.this, (ArrayList<String>) items, "Select or Search Zipcode", R.style.DialogAnimations_SmileWindow, "Close");// With 	Animation


        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                tvSelect_zipcode.setText(item); // + " Position: " + position
                selectedZipcode = item;

            }
        });


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
        list.add(new Time("31", "10:00AM"));


        MonthlyAdapter monthlyAdapter = new MonthlyAdapter(ActivityScheduleMonthly.this, list);
        recylerViewMonthly.setHasFixedSize(true);
        recylerViewMonthly.setLayoutManager(new LinearLayoutManager(this));
        recylerViewMonthly.setAdapter(monthlyAdapter);


    }

    private void eventsListenerHere() {

        backArrowImage.setOnClickListener(this);
        tvContinueBtn.setOnClickListener(this);
        tvSelect_zipcode.setOnClickListener(this);

    }

    private void findingIdsHere() {

        backArrowImage = findViewById(R.id.backArrowImage);
        tvContinueBtn = findViewById(R.id.tvContinueBtn);
        recylerViewMonthly = findViewById(R.id.recylerViewMonthly);
        tvSelect_zipcode = findViewById(R.id.tvSelect_zipcode);


    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.backArrowImage:
                finish();
                break;


            case R.id.tvContinueBtn:

                Toast.makeText(ActivityScheduleMonthly.this, "Continues", Toast.LENGTH_SHORT).show();

                callTheMonthlyApiFromHere();

                break;


            case R.id.tvSelect_zipcode:
                spinnerDialog.showSpinerDialog();

                break;

        }

    }

    private void callTheMonthlyApiFromHere() {


    }


}
