package android.com.cleaner.activities;

import android.app.ProgressDialog;
import android.com.cleaner.R;
import android.com.cleaner.apiResponses.getCityList.CityList;
import android.com.cleaner.apiResponses.getStateList.GetStateList;
import android.com.cleaner.apiResponses.getStateList.Payload;
import android.com.cleaner.apiResponses.getZipcodeList.ZipCodeList;
import android.com.cleaner.apiResponses.signUpResponse.SignUp;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ActivitySignUp extends BaseActivity implements View.OnClickListener, Validator.ValidationListener {


    private ImageView backArrowImage;


    @NotEmpty(message = "Please enter the name")
    public EditText edName;

    @NotEmpty(message = "Please enter the address")
    public EditText edAddress;

    @NotEmpty
    public EditText edPhoneNumber;

    @NotEmpty
    @Email(message = "Please enter the valid email")
    public EditText edEmail;

    @NotEmpty
    @Password(message = "Please enter the valid password")
    // , min = 6, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS
    public EditText edPassword;

    @NotEmpty(message = "Please enter the city")
    public EditText edCity;

    @NotEmpty(message = "Please enter the zip code")
    public EditText edZipCode;

    private Spinner spinnerStates, spinnerCities, spinnerZipCode;

    private TextView tvAlreadyMember, tvStates;
    private TextView SignUpBtn;
    private Context context;

    Validator validator;
    ProgressDialog mProgressDialog;

    private NoInternetDialog noInternetDialog;
    private String android_DeviceID;


    private CompositeDisposable disposable = new CompositeDisposable();


    private ArrayList<String> statelistIdPos = new ArrayList<>();
    private ArrayList<String> citylistIdPos = new ArrayList<>();
    private ArrayList<String> zipCodelistIdPos = new ArrayList<>();


    private String stateIdHolder, cityIdHolder, zipCodeIdHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_new);


        context = this;
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        Hawk.init(this).build();


        // MARK: METHODS

        findingIdsHere();
        initializingValidationHere();
        eventListener();
        changingStatusBarColorHere();


        spinnerForStateOperationGoesHere();

    }

    private void spinnerForStateOperationGoesHere() {

        HttpModule.provideRepositoryService().getStateListAPI().enqueue(new Callback<GetStateList>() {
            @Override
            public void onResponse(Call<GetStateList> call, Response<GetStateList> response) {

                if (response.body() != null) {

                    if (response.body() != null && response.body().getIsSuccess()) {

                        spinnerSetupForState(response.body().getPayload());

                    } else {
                        TastyToast.makeText(ActivitySignUp.this, response.body().getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    }

                } else {
                    TastyToast.makeText(ActivitySignUp.this, "Null body", TastyToast.LENGTH_SHORT, TastyToast.WARNING).show();

                }

            }

            @Override
            public void onFailure(Call<GetStateList> call, Throwable t) {

                System.out.println("ActivitySignUp.onFailure " + t);
            }
        });


    }

    private void spinnerSetupForState(List<Payload> payload) {


        ArrayList<String> stateArrList = new ArrayList<>();
        stateArrList.add("Choose states");
        statelistIdPos.add("0");


        for (int i = 0; i < payload.size(); i++) {
            stateArrList.add(payload.get(i).getName());
            statelistIdPos.add(String.valueOf(payload.get(i).getId()));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stateArrList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStates.setDropDownWidth(700);
        spinnerStates.setAdapter(dataAdapter);


        spinnerItemSelectionForState();


    }

    private void spinnerItemSelectionForState() {


        spinnerStates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                stateIdHolder = statelistIdPos.get(position);
                spinnerForCitiesOperationGoesHere(stateIdHolder);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                TastyToast.makeText(ActivitySignUp.this, parent + "", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

            }
        });


    }

    private void spinnerForCitiesOperationGoesHere(String stateIdHolder) {


        disposable.add(HttpModule.provideRepositoryService().getCitiesAPI(stateIdHolder).
                subscribeOn(io.reactivex.schedulers.Schedulers.computation()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<CityList>() {

                    @Override
                    public void accept(CityList cityList) throws Exception {


                        if (cityList != null && cityList.getIsSuccess()) {

                            spinnerSetupForCityGoesHere(cityList);

                        } else {

//                            TastyToast.makeText(ActivitySignUp.this, cityList.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }


                    }
                }, new Consumer<Throwable>() {

                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        System.out.println("ActivitySignUp.accept " + throwable.toString());

                        TastyToast.makeText(ActivitySignUp.this, throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                    }
                }));


    }

    private void spinnerSetupForCityGoesHere(CityList cityList) {


        ArrayList<String> cityArrayList = new ArrayList<>();
        cityArrayList.add("Choose cities");
        citylistIdPos.add("0");


        for (int j = 0; j < cityList.getPayload().size(); j++) {


            cityArrayList.add(cityList.getPayload().get(j).getName());
            citylistIdPos.add(String.valueOf(cityList.getPayload().get(j).getId()));

        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cityArrayList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCities.setDropDownWidth(700);
        spinnerCities.setAdapter(dataAdapter);


        spinnerItemSelectionForCity();


    }

    private void spinnerItemSelectionForCity() {


        spinnerCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                cityIdHolder = citylistIdPos.get(position);
                spinnerForZipCodeOperationGoesHere(cityIdHolder);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                TastyToast.makeText(ActivitySignUp.this, parent + "", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

            }
        });


    }

    private void spinnerForZipCodeOperationGoesHere(String cityIdHolder) {


        disposable.add(HttpModule.provideRepositoryService().getZipCodeListAPI(cityIdHolder).
                subscribeOn(io.reactivex.schedulers.Schedulers.computation()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<ZipCodeList>() {

                    @Override
                    public void accept(ZipCodeList zipCodeList) throws Exception {


                        if (zipCodeList != null && zipCodeList.getIsSuccess()) {


                            spinnerSetupForZipCodeGoesHere(zipCodeList);


                        } else {
//                            TastyToast.makeText(ActivitySignUp.this, Objects.requireNonNull(zipCodeList).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }

                    }

                }, new Consumer<Throwable>() {

                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        TastyToast.makeText(ActivitySignUp.this, throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                    }
                }));


    }

    private void spinnerSetupForZipCodeGoesHere(ZipCodeList zipCodeList) {


        ArrayList<String> zipCodeArrayList = new ArrayList<>();

        zipCodeArrayList.add("Choose zipcode");
        zipCodelistIdPos.add("0");

        for (int j = 0; j < zipCodeList.getPayload().size(); j++) {


            zipCodeArrayList.add(zipCodeList.getPayload().get(j).getZipcode());
            zipCodelistIdPos.add(String.valueOf(zipCodeList.getPayload().get(j).getId()));

        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, zipCodeArrayList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerZipCode.setDropDownWidth(700);
        spinnerZipCode.setAdapter(dataAdapter);

        spinnerItemSelectionForZipCode();


    }

    private void spinnerItemSelectionForZipCode() {


        spinnerZipCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                zipCodeIdHolder = zipCodelistIdPos.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                TastyToast.makeText(ActivitySignUp.this, parent + "", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
        disposable.dispose();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void initializingValidationHere() {
        validator = new Validator(this);
        validator.setValidationListener(this);

    }


    private void changingStatusBarColorHere() {
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivitySignUp.this, R.color.statusBarColor));

    }

    private void eventListener() {

        backArrowImage.setOnClickListener(this);
        SignUpBtn.setOnClickListener(this);
        tvAlreadyMember.setOnClickListener(this);


    }

    private void findingIdsHere() {


        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        backArrowImage = findViewById(R.id.backArrowImage);

        edName = findViewById(R.id.edName);
        edAddress = findViewById(R.id.edAddress);
        edPhoneNumber = findViewById(R.id.edPhoneNumber);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        edCity = findViewById(R.id.edCity);
        edZipCode = findViewById(R.id.edZipCode);

        spinnerStates = findViewById(R.id.spinnerStates);

        SignUpBtn = findViewById(R.id.SignUpBtn);
        tvAlreadyMember = findViewById(R.id.tvAlreadyMember);

        tvStates = findViewById(R.id.tvStates);

        spinnerCities = findViewById(R.id.spinnerCities);
        spinnerZipCode = findViewById(R.id.spinnerZipCode);


        TextInputLayout usernameTextObj = (TextInputLayout) findViewById(R.id.tilPass);
        usernameTextObj.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/headingbrew.otf"));


    }


    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.backArrowImage:

                finish();

                break;

            case R.id.tvAlreadyMember:

                Intent intent = new Intent(ActivitySignUp.this, ActivitySignIn.class);
                startActivity(intent);
                finish();

                break;

            case R.id.SignUpBtn:

                loginProgressing();
                validator.validate();

                break;
        }

    }

    private void loginProgressing() {
        mProgressDialog.setMessage("Signing up..");
//        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

    }


    private void callingDashboardActivity() {

        Intent intent1 = new Intent(ActivitySignUp.this, ActivitySignIn.class);
//        intent1.putExtra("Uniqid", "ActivitySignUp");
        startActivity(intent1);
        finish();

    }


    @Override
    public void onValidationSucceeded() {

        clearingTheEdittextHere();
        savingEnteredValues();
        gettingEnteredValues();


        if (spinnerStates.getSelectedItem().toString().equalsIgnoreCase("Select State")) {

            mProgressDialog.dismiss();
            TastyToast.makeText(ActivitySignUp.this, "Please select state", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
        } else {

            callingSignUpApiHere();
        }


    }

    private void callingSignUpApiHere() {

        android_DeviceID = Settings.Secure.getString(ActivitySignUp.this.getContentResolver(), Settings.Secure.ANDROID_ID);

        HttpModule.provideRepositoryService().signUpAPI(edName.getText().toString(), "SmartItVentures", edEmail.getText().toString(), edPassword.getText().toString(), edPhoneNumber.getText().toString(), edAddress.getText().toString(), android_DeviceID, "A", "cu", "12", "newCity", "40021").enqueue(new Callback<SignUp>() {
            @Override
            public void onResponse(Call<SignUp> call, Response<SignUp> response) {

                mProgressDialog.dismiss();

                if (response.body() != null) {

                    if (response.body().getIsSuccess()) {


                        String savedUserId = String.valueOf(response.body().getPayload().getUserId());

                        Hawk.put("savedUserId", savedUserId);

                        savingTheValuesinHawkHere();

                        showTheDialogMessageForOk(response.body().getMessage());

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                callingDashboardActivity();
                            }
                        }, 2000);

                    } else {
                        showTheDialogMessageForError(response.body().getMessage());

                    }


                } else {

                    TastyToast.makeText(ActivitySignUp.this, "500 Internal server error", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                }


            }

            @Override
            public void onFailure(Call<SignUp> call, Throwable t) {

                mProgressDialog.dismiss();
                TastyToast.makeText(ActivitySignUp.this, t.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

            }
        });


    }

    private void savingTheValuesinHawkHere() {

        Hawk.put("FIRST_NAME", edName.getText().toString());
        Hawk.put("EMAIL_ID", edEmail.getText().toString());
        Hawk.put("PASSWORD", edPassword.getText().toString());
        Hawk.put("PHONE_NUMBER", edPhoneNumber.getText().toString());
        Hawk.put("ADDRESS", edAddress.getText().toString());
        Hawk.put("STATE_VALUE", spinnerStates.getSelectedItem().toString());
        Hawk.put("CITY_VALUE", spinnerCities.getSelectedItem().toString());
        Hawk.put("ZIPCODE_VALUE", spinnerZipCode.getSelectedItem().toString());


    }

    private void clearingTheEdittextHere() {

        edName.setError(null);
        edAddress.setError(null);
        edPhoneNumber.setError(null);
        edEmail.setError(null);
        edPassword.setError(null);
        edCity.setError(null);
        edZipCode.setError(null);

    }

    public static void gettingEnteredValues() {


        String name = Hawk.get("NAME");
        String address = Hawk.get("ADDRESS");
        String phoneNumber = Hawk.get("PHONE NUMBER");
        String password = Hawk.get("PASSWORD");
        String city = Hawk.get("CITY");
        String zipcode = Hawk.get("ZIPCODE");
        String spinnerStates = Hawk.get("SPINNER STATES");

    }

    private void savingEnteredValues() {

        Hawk.put("NAME", edName.getText().toString());
        Hawk.put("EMAIL", edEmail.getText().toString());
        Hawk.put("ADDRESS", edAddress.getText().toString());
        Hawk.put("PHONE NUMBER", edPhoneNumber.getText().toString());
        Hawk.put("PASSWORD", edPassword.getText().toString());
        Hawk.put("CITY", edCity.getText().toString());
        Hawk.put("ZIPCODE", edZipCode.getText().toString());
        Hawk.put("SPINNER STATES", spinnerStates.getSelectedItem().toString());

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        mProgressDialog.dismiss();

//        Toast.makeText(this, errors + "", Toast.LENGTH_LONG).show();

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);


            if (view instanceof EditText) {  // Display error messages

                ((EditText) view).setError(message);

            } else {
//                Toast.makeText(ActivitySignUp.this, message, Toast.LENGTH_LONG).show();
            }
        }


    }


}
