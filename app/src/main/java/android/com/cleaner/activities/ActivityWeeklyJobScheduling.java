package android.com.cleaner.activities;

import android.app.ProgressDialog;
import android.com.cleaner.R;
import android.com.cleaner.adapters.WeeklyAdapter;
import android.com.cleaner.apiResponses.getAllZipcode.GetAllZipcodeMain;
import android.com.cleaner.apiResponses.getAllZipcode.Payload;
import android.com.cleaner.apiResponses.typesOfServices.TypesOfSerives;
import android.com.cleaner.apiResponses.weeklyJobScheduled.AllWorkingDays;
import android.com.cleaner.apiResponses.weeklyJobScheduled.weeklyJobScheduledResponse.WeeklyJobScheduled;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.com.cleaner.interfaces.ClickListnerForWeeklyJobScheduled;
import android.com.cleaner.requestModelsForWeeklyJob.DateTime;
import android.com.cleaner.requestModelsForWeeklyJob.WeeklyJobRequestModel;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import java.util.ArrayList;
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
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityWeeklyJobScheduling extends AppCompatActivity implements View.OnClickListener, ClickListnerForWeeklyJobScheduled {
    private RecyclerView recyclerviewWeeklyJobScheduling;
    private RelativeLayout relativeLayoutForZipcode;
    private EditText edEnter_your_address;
    private MultiSelectSpinner multiselectSpinner;
    private SpinnerDialog spinnerDialog;
    private TextView tvSelect_zipcode, tvSubmit, noDaysAvailable;
    private String selectedZipcode;
    private String finalServicesIds;
    private ArrayList<String> serviceTypes;
    private List<String> idsListServiceTypes;
    private List<String> items = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public ClickListnerForWeeklyJobScheduled clickListnerForWeeklyJobScheduled;
    private ProgressDialog mProgressDialog;
    private WeeklyJobRequestModel weeklyJobRequestModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_job_scheduling);
        clickListnerForWeeklyJobScheduled = this;
        weeklyJobRequestModel = new WeeklyJobRequestModel();
        findingIdsHere();
        loginProgressing();
        callTheAdapterFromHere();
        typesOfSerives();
        selectYourZopcodeFromHere();
        eventListenerGoesHere();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    private void loginProgressing(){
        mProgressDialog.setMessage("Wait..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }
    private void typesOfSerives(){
        HttpModule.provideRepositoryService().makingTypesOfServices(Hawk.get("spanish",false)?"es":"en").enqueue(new Callback<TypesOfSerives>() {
            @Override
            public void onResponse(Call<TypesOfSerives> call, Response<TypesOfSerives> response) {
                if (response.body() != null && response.body().getIsSuccess()){
                    setMultiSpinner(response.body().getPayload()); } else {TastyToast.makeText(ActivityWeeklyJobScheduling.this, Objects.requireNonNull(response.body()).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show(); } }
            @Override
            public void onFailure(Call<TypesOfSerives> call, Throwable t) {
                System.out.println("ActivityDailyJobSchedule.onFailure " + t.toString());
                TastyToast.makeText(ActivityWeeklyJobScheduling.this, "Something went wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
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

                    if (selected[temp]){

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

    private void eventListenerGoesHere(){

        tvSelect_zipcode.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    private void selectYourZopcodeFromHere(){


        compositeDisposable.add(HttpModule.provideRepositoryService().getAllZipcodeAPI().
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                subscribeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<GetAllZipcodeMain>(){

                    @Override
                    public void accept(GetAllZipcodeMain getAllZipcodeMain) throws Exception{

                        if (getAllZipcodeMain != null && getAllZipcodeMain.getIsSuccess()){

                            gettingAllZipcodes(getAllZipcodeMain.getPayload());

                        } else {
                            TastyToast.makeText(ActivityWeeklyJobScheduling.this, Objects.requireNonNull(getAllZipcodeMain).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }
                    }

                }, new Consumer<Throwable>(){

                    @Override
                    public void accept(Throwable throwable) throws Exception{

                        TastyToast.makeText(ActivityWeeklyJobScheduling.this, "Something went wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        System.out.println("ActivityDailyJobSchedule.accept " + throwable.toString());
                    }
                }));
    }

    private void gettingAllZipcodes(List<Payload> payload) {

        for (int x = 0; x < payload.size(); x++) {
            items.add(payload.get(x).getZipcode());
        }
        spinnerDialog = new SpinnerDialog(ActivityWeeklyJobScheduling.this, (ArrayList<String>) items, "Select or Search Zipcode", "Close");// With No Animation
        spinnerDialog = new SpinnerDialog(ActivityWeeklyJobScheduling.this, (ArrayList<String>) items, "Select or Search Zipcode", R.style.DialogAnimations_SmileWindow, "Close");// With 	Animation
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                tvSelect_zipcode.setText(item); // + " Position: " + position
                selectedZipcode = item;

            }
        });
    }


    private void callTheAdapterFromHere(){
        try {
            compositeDisposable.add(HttpModule.provideRepositoryService().allWorkingDays().
                    subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribe(new Consumer<AllWorkingDays>() {
                        @Override
                        public void accept(AllWorkingDays allWorkingDays) throws Exception {
                            if (allWorkingDays != null && allWorkingDays.getIsSuccess()) {
                                mProgressDialog.dismiss();
                                WeeklyAdapter weeklyAdapter = new WeeklyAdapter(ActivityWeeklyJobScheduling.this, allWorkingDays.getPayload(), clickListnerForWeeklyJobScheduled);
                                recyclerviewWeeklyJobScheduling.setHasFixedSize(true);
                                recyclerviewWeeklyJobScheduling.setLayoutManager(new LinearLayoutManager(ActivityWeeklyJobScheduling.this));
                                recyclerviewWeeklyJobScheduling.setAdapter(weeklyAdapter);
                            } else {
                                noDaysAvailable.setVisibility(View.VISIBLE);
                            }
                        }

                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mProgressDialog.dismiss();
                            TastyToast.makeText(ActivityWeeklyJobScheduling.this, "Something went wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }
                    }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void findingIdsHere() {
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        recyclerviewWeeklyJobScheduling = findViewById(R.id.recyclerviewWeeklyJobScheduling);
        relativeLayoutForZipcode = findViewById(R.id.relativeLayoutForZipcode);
        edEnter_your_address = findViewById(R.id.edEnter_your_address);
        multiselectSpinner = findViewById(R.id.multiselectSpinner);
        tvSelect_zipcode = findViewById(R.id.tvSelect_zipcode);
        tvSubmit = findViewById(R.id.tvSubmit);
        noDaysAvailable = findViewById(R.id.noDaysAvailable);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSelect_zipcode:
                spinnerDialog.showSpinerDialog();
                break;
            case R.id.tvSubmit:
                if (TextUtils.isEmpty(selectedZipcode)) {
                    TastyToast.makeText(ActivityWeeklyJobScheduling.this, "All feilds are required", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (edEnter_your_address.getText().toString().isEmpty()) {

                    TastyToast.makeText(ActivityWeeklyJobScheduling.this, "All feilds are required", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                } else if (TextUtils.isEmpty(finalServicesIds)) {

                    TastyToast.makeText(ActivityWeeklyJobScheduling.this, "All feilds are required", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (weeklyJobRequestModel.getDateTime() == null) {

                    TastyToast.makeText(ActivityWeeklyJobScheduling.this, "All feilds are required", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                } else {

                    callTheWeeklyJobApiFromHere();
                }


                break;
        }


    }

    private void callTheWeeklyJobApiFromHere() {


        weeklyJobRequestModel.setCustomerId(String.valueOf(Hawk.get("savedUserId")));
        weeklyJobRequestModel.setZipcode(selectedZipcode);
        weeklyJobRequestModel.setAddress(edEnter_your_address.getText().toString());
        weeklyJobRequestModel.setServiceIds(finalServicesIds);


        compositeDisposable.add(HttpModule.provideRepositoryService().weeklyJobScheduled(weeklyJobRequestModel).
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<WeeklyJobScheduled>() {

                    @Override
                    public void accept(WeeklyJobScheduled weeklyJobScheduled) throws Exception {


                        if (weeklyJobScheduled != null && weeklyJobScheduled.getIsSuccess()) {

                            new AwesomeSuccessDialog(ActivityWeeklyJobScheduling.this)
                                    .setTitle("Awesome")
                                    .setMessage(weeklyJobScheduled.getMessage())
                                    .setColoredCircle(R.color.english_btnColor)
                                    .setDialogIconAndColor(R.drawable.ic_success, R.color.white)
                                    .setCancelable(false)
                                    .setPositiveButtonText("Ok")
                                    .setPositiveButtonbackgroundColor(R.color.english_btnColor)
                                    .setPositiveButtonTextColor(R.color.white)
                                    .setPositiveButtonClick(new Closure() {
                                        @Override
                                        public void exec() {

                                            ActivityWeeklyJobScheduling.this.finish();


                                        }
                                    })
                                    .show();

                        } else {


                            new AwesomeErrorDialog(ActivityWeeklyJobScheduling.this)
                                    .setTitle("Oops")
                                    .setMessage(Objects.requireNonNull(weeklyJobScheduled).getMessage())
                                    .setColoredCircle(R.color.dialogErrorBackgroundColor)
                                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                                    .setCancelable(true).setButtonText(getString(R.string.dialog_ok_button))
                                    .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                                    .setButtonText(getString(R.string.dialog_ok_button))
                                    .setErrorButtonClick(new Closure() {
                                        @Override
                                        public void exec() {

                                            ActivityWeeklyJobScheduling.this.finish();


                                        }
                                    })
                                    .show();

                        }

                    }

                }, new Consumer<Throwable>() {

                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        TastyToast.makeText(ActivityWeeklyJobScheduling.this, "Something went wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                    }

                }));


    }


    @Override
    public void weeklyJobScheduledAdapterItemClicked(List<DateTime> list, int position) {

        System.out.println("ActivityWeeklyJobScheduling.weeklyJobScheduledAdapterItemClicked " + list);

        weeklyJobRequestModel.setDateTime(list);

    }


}
