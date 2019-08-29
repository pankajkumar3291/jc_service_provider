package android.com.cleaner.activities;

import android.app.TimePickerDialog;
import android.com.cleaner.R;
import android.com.cleaner.apiResponses.dailyJobScheduled.DailyJobScheduled;
import android.com.cleaner.apiResponses.getAllZipcode.GetAllZipcodeMain;
import android.com.cleaner.apiResponses.getAllZipcode.Payload;
import android.com.cleaner.apiResponses.typesOfServices.TypesOfSerives;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
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
public class ActivityDailyJobSchedule extends AppCompatActivity implements View.OnClickListener {

    private TextView tvSelect_your_time, tvSelect_zipcode, tvSelectetTime, tvSubmit;
    private EditText edEnter_your_address;
    private MultiSelectSpinner multiselectSpinner;
    private int mHour, mMinute;
    private RelativeLayout relativeLayoutForZipcode;
    private List<String> items = new ArrayList<>();
    private SpinnerDialog spinnerDialog;
    private ArrayList<String> serviceTypes;
    private List<String> idsListServiceTypes;
    private String finalServicesIds;
    private String selectedTime;
    private String selectedZipcode;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_your_daily_scheduling);
        findingIdsHere();
        eventListnerGoesHere();
        selectYourZopcodeOpener();
        typesOfSerives();
    }

    private void typesOfSerives() {

        HttpModule.provideRepositoryService().makingTypesOfServices(Hawk.get("spanish",false)?"es":"en").enqueue(new Callback<TypesOfSerives>() {
            @Override
            public void onResponse(Call<TypesOfSerives> call, Response<TypesOfSerives> response) {

                if (response.body() != null && response.body().getIsSuccess()) {
                    setMultiSpinner(response.body().getPayload());
                } else {
                    TastyToast.makeText(ActivityDailyJobSchedule.this, Objects.requireNonNull(response.body()).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                }

            }

            @Override
            public void onFailure(Call<TypesOfSerives> call, Throwable t) {

                System.out.println("ActivityDailyJobSchedule.onFailure " + t.toString());
                TastyToast.makeText(ActivityDailyJobSchedule.this, "Something went wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    private void eventListnerGoesHere() {
        tvSelect_your_time.setOnClickListener(this);
        relativeLayoutForZipcode.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }
    private void findingIdsHere() {
        tvSelect_your_time = findViewById(R.id.tvSelect_your_time);
        tvSelect_zipcode = findViewById(R.id.tvSelect_zipcode);
        tvSelectetTime = findViewById(R.id.tvSelectetTime);
        edEnter_your_address = findViewById(R.id.edEnter_your_address);
        multiselectSpinner = findViewById(R.id.multiselectSpinner);
        relativeLayoutForZipcode = findViewById(R.id.relativeLayoutForZipcode);
        tvSubmit = findViewById(R.id.tvSubmit);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSelect_your_time:
                selectYourTimeOpener();
                break;
            case R.id.relativeLayoutForZipcode:
                spinnerDialog.showSpinerDialog();
                break;
            case R.id.tvSubmit:
                if (tvSelectetTime.getText().toString().matches("")) {
                    TastyToast.makeText(ActivityDailyJobSchedule.this, "All feilds are required", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (tvSelect_zipcode.getText().toString().matches("")) {
                    TastyToast.makeText(ActivityDailyJobSchedule.this, "All feilds are required", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (edEnter_your_address.getText().toString().isEmpty()) {
                    TastyToast.makeText(ActivityDailyJobSchedule.this, "All feilds are required", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (TextUtils.isEmpty(finalServicesIds)) {
                    TastyToast.makeText(ActivityDailyJobSchedule.this, "All feilds are required", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else {
                    dailyJobScheduledApiGoesHere();
                }
                break;
        }
    }

    private void dailyJobScheduledApiGoesHere() {
        compositeDisposable.add(HttpModule.provideRepositoryService().
                dailyJobScheduled(String.valueOf(Hawk.get("savedUserId")), selectedTime, selectedZipcode, edEnter_your_address.getText().toString(), finalServicesIds).
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<DailyJobScheduled>() {

                    @Override
                    public void accept(DailyJobScheduled dailyJobScheduled) throws Exception {
                        if (dailyJobScheduled != null && dailyJobScheduled.getIsSuccess()) {
                            new AwesomeSuccessDialog(ActivityDailyJobSchedule.this)
                                    .setTitle("Awesome")
                                    .setMessage(dailyJobScheduled.getMessage())
                                    .setColoredCircle(R.color.english_btnColor)
                                    .setDialogIconAndColor(R.drawable.ic_success, R.color.white)
                                    .setCancelable(false)
                                    .setPositiveButtonText("Ok")
                                    .setPositiveButtonbackgroundColor(R.color.english_btnColor)
                                    .setPositiveButtonTextColor(R.color.white)
                                    .setPositiveButtonClick(new Closure() {
                                        @Override
                                        public void exec() {

                                            ActivityDailyJobSchedule.this.finish();
                                        }
                                    })
                                    .show();
                        } else {
                            new AwesomeErrorDialog(ActivityDailyJobSchedule.this)
                                    .setTitle("Oops")
                                    .setMessage(Objects.requireNonNull(dailyJobScheduled).getMessage())
                                    .setColoredCircle(R.color.dialogErrorBackgroundColor)
                                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                                    .setCancelable(true).setButtonText(getString(R.string.dialog_ok_button))
                                    .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                                    .setButtonText(getString(R.string.dialog_ok_button))
                                    .setErrorButtonClick(new Closure() {
                                        @Override
                                        public void exec() {
                                        }
                                    })
                                    .show();
                        }
                    }

                }, new Consumer<Throwable>() {

                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        TastyToast.makeText(ActivityDailyJobSchedule.this, "Something went wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                    }

                }));


    }


    private void selectYourZopcodeOpener() {


        compositeDisposable.add(HttpModule.provideRepositoryService().getAllZipcodeAPI().
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                subscribeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<GetAllZipcodeMain>() {

                    @Override
                    public void accept(GetAllZipcodeMain getAllZipcodeMain) throws Exception {

                        if (getAllZipcodeMain != null && getAllZipcodeMain.getIsSuccess()) {

                            gettingAllZipcodes(getAllZipcodeMain.getPayload());

                        } else {
                            TastyToast.makeText(ActivityDailyJobSchedule.this, Objects.requireNonNull(getAllZipcodeMain).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }


                    }

                }, new Consumer<Throwable>() {

                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        TastyToast.makeText(ActivityDailyJobSchedule.this, "Something went wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        System.out.println("ActivityDailyJobSchedule.accept " + throwable.toString());

                    }

                }));


    }

    private void gettingAllZipcodes(List<Payload> payload) {

        for (int x = 0; x < payload.size(); x++) {

            items.add(payload.get(x).getZipcode());
        }

        spinnerDialog = new SpinnerDialog(ActivityDailyJobSchedule.this, (ArrayList<String>) items, "Select or Search Zipcode", "Close");// With No Animation
        spinnerDialog = new SpinnerDialog(ActivityDailyJobSchedule.this, (ArrayList<String>) items, "Select or Search Zipcode", R.style.DialogAnimations_SmileWindow, "Close");// With 	Animation


        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                tvSelect_zipcode.setText(item); // + " Position: " + position
                selectedZipcode = item;

            }
        });


    }

    private void selectYourTimeOpener() {

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        tvSelectetTime.setText(hourOfDay + ":" + minute);

                        selectedTime = hourOfDay + ":" + minute;
                        Hawk.put("TIME", hourOfDay + ":" + minute);

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();


    }
}
