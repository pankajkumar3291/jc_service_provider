package android.com.cleaner.activities;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.com.cleaner.R;
import android.com.cleaner.apiResponses.bookACleaner.BookCleaner;
import android.com.cleaner.apiResponses.getAllZipcode.GetAllZipcodeMain;
import android.com.cleaner.apiResponses.typesOfServices.Payload;
import android.com.cleaner.apiResponses.typesOfServices.TypesOfSerives;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.com.cleaner.makingArrayRequest.MakeArrayRequest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import io.apptik.widget.multiselectspinner.BaseMultiSelectSpinner;
import io.apptik.widget.multiselectspinner.MultiSelectSpinner;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
public class ActivityBookCleaner extends AppCompatActivity {
    private Spinner spinner;
    private TextView tvBookCleanerSubmit, tvPickYourTime, tvPickYourDate;
    private EditText edZipcode, edAddress;
    private PlaceAutocompleteFragment placeAutocompleteFragment;
    private String selectedPlace = "";
    private NoInternetDialog noInternetDialog;
    private int mHour, mMinute;
    private double lat, lng;
    final Calendar myCalendar = Calendar.getInstance();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private List<String> zipcode;
    private MultiSelectSpinner multiselectSpinner;
    private ArrayList<String> foo_array;
    ArrayList<MakeArrayRequest.Servicetype> selectedServices = new ArrayList<>();
    private List<String> idsList;
    private String csv;
    private String dateCheck, timeCheck;
    private ProgressDialog mProgressDialog;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private TextView tvToast, tvToastError, tvClose;
    private TextView tvYes;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_cleaner);
        Hawk.init(this).build();
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityBookCleaner.this, R.color.statusBarColor));
        findingIdsHere();
        getAllZipcodeApiGoesHere();
        typesOfSerivesApi();
//      googlePlacesApisGoesHere(tvBookCleanerSubmit);
    }
    private void getAllZipcodeApiGoesHere() {
        compositeDisposable.add(HttpModule.provideRepositoryService().getAllZipcodeAPI().
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<GetAllZipcodeMain>() {
                    @Override
                    public void accept(GetAllZipcodeMain getAllZipcodeMain) throws Exception {
                        if (getAllZipcodeMain != null && getAllZipcodeMain.getIsSuccess()) {
                            checkEdittext(getAllZipcodeMain.getPayload());
                        } else {
                            TastyToast.makeText(ActivityBookCleaner.this, Objects.requireNonNull(getAllZipcodeMain).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        TastyToast.makeText(ActivityBookCleaner.this, throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    }
                }));
    }
    private void checkEdittext(final List<android.com.cleaner.apiResponses.getAllZipcode.Payload> payload) {
        zipcode = new ArrayList<>();
        for (int x = 0; x < payload.size(); x++) {
            zipcode.add(payload.get(x).getZipcode());
        }
        edZipcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (zipcode.contains(edZipcode.getText().toString())) {
                    edZipcode.setError(null);
                    Hawk.put("CHECK_EDITTEXT_VALUES", edZipcode.getText().toString());
                    TastyToast.makeText(ActivityBookCleaner.this, "Matched", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                } else {
                    edZipcode.setError("No matching zipcode found");
                }
            }
            @Override
            public void afterTextChanged(Editable editable){
            }
        });
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        noInternetDialog.onDestroy();
        compositeDisposable.dispose();
    }
    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    // MARK: GOOGLE PLACES SEARCH
    private void googlePlacesApisGoesHere(TextView tvBookCleanerSubmit){
//        http://velmm.com/google-places-autocomplete-android-example/
        placeAutocompleteFragment = (PlaceAutocompleteFragment) this.getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()/*.setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)*/.build();
        placeAutocompleteFragment.setFilter(autocompleteFilter);
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener(){
            @Override
            public void onPlaceSelected(Place place){
                latlngFromAddress(place);
                selectedPlace = place.getName().toString();
            }
            @Override
            public void onError(Status status) {
                System.out.println("PlacesActivity.onError " + status.toString());
                Toast.makeText(ActivityBookCleaner.this, status.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void bookACleanerApiGoesHere(){
        compositeDisposable.add(HttpModule.provideRepositoryService().
                bookCleanerAPI(Hawk.get("savedUserId", ""), edZipcode.getText().toString(), Hawk.get("DATE", ""), Hawk.get("TIME", ""), csv, edAddress.getText().toString()).
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<BookCleaner>(){
                    @Override
                    public void accept(BookCleaner bookCleaner) throws Exception{
                        if (bookCleaner != null && bookCleaner.getIsSuccess()){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run(){
                                    mProgressDialog.dismiss();
//                                    Intent intent = new Intent(ActivityBookCleaner.this, ActivityCleanerProfile.class);
//                                    startActivity(intent);
                                    finish();
                                }
                            }, 2000);
                        } else {
                            mProgressDialog.dismiss();
                            showTheDialogMessageForError(Objects.requireNonNull(bookCleaner).getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mProgressDialog.dismiss();
                        TastyToast.makeText(ActivityBookCleaner.this, throwable.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        System.out.println("ActivityBookCleaner.accept " + throwable.toString());
                    }
                }));
    }
    private void showTheDialogMessageForError(String message) {
        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.dialog_show_for_message_error, null);
        findingIdsHereForError(dialogView, message);
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder
                .setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    private void findingIdsHereForError(View dialogView, String message) {
        tvToastError = dialogView.findViewById(R.id.tvToastError);
        tvClose = dialogView.findViewById(R.id.tvClose);
        tvToastError.setText(message);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
    private void showTheDialogMessageForOkay(String message){
        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.dialog_show_for_message_ok, null);
        findingIdsForOk(dialogView, message);
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder
                .setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    private void findingIdsForOk(View dialogView, String message) {
        tvToast = dialogView.findViewById(R.id.tvToast);
        tvYes = dialogView.findViewById(R.id.tvYes);
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        tvToast.setText(message);
    }
    private void latlngFromAddress(Place place) {
        TastyToast.makeText(ActivityBookCleaner.this, place.getName().toString(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
//      GeocodingLocation locationAddress = new GeocodingLocation();
//        GeocodingLocation.getAddressFromLocation(place.getName().toString(), ActivityBookCleaner.this, new GeocoderHandler());
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        try {
            address = coder.getFromLocationName(place.getName().toString(), 1);
            if (address != null && address.size() > 0) {
                Address location = Objects.requireNonNull(address).get(0);
                lat = location.getLatitude();
                lng = location.getLongitude();
                System.out.println("ActivityBookCleaner.latlngFromAddress " + lat);
                System.out.println("ActivityBookCleaner.latlngFromAddress " + lng);
            }
        } catch (Exception e) {
            System.out.println("ActivityBookCleaner.latlngFromAddress " + e.toString());
        }
    }
    private void typesOfSerivesApi() {
        HttpModule.provideRepositoryService().makingTypesOfServices(Hawk.get("spanish",false)?"es":"en").enqueue(new Callback<TypesOfSerives>() {
            @Override
            public void onResponse(Call<TypesOfSerives> call, Response<TypesOfSerives> response) {
                if (response.body() != null) {
                    if (response.body() != null && response.body().getIsSuccess()) {
                        spinnerSetUpHere(response.body().getPayload());
                    } else {
                        TastyToast.makeText(ActivityBookCleaner.this, response.body().getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    }
                } else {
                    TastyToast.makeText(ActivityBookCleaner.this, "500 Internal server error", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                }
            }
            @Override
            public void onFailure(Call<TypesOfSerives> call, Throwable t) {
                TastyToast.makeText(ActivityBookCleaner.this, t.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
            }
        });
    }
    private void spinnerSetUpHere(final List<Payload> payload) {
        foo_array = new ArrayList<>();
        for (int i = 0; i < payload.size(); i++) {
            foo_array.add(payload.get(i).getName());
        }
        multiselectSpinner = findViewById(R.id.multiselectSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, foo_array);
        multiselectSpinner.setListener(new BaseMultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                idsList = new ArrayList<>();
                for (int temp = 0; temp < selected.length; temp++) {
                    if (selected[temp]) {
                        idsList.add(String.valueOf(payload.get(temp).getId()));
                    }
                    String idList = idsList.toString();
                    csv = idList.substring(1, idList.length() - 1).replace(", ", ",");
                    System.out.println("ActivityBookCleaner.onItemsSelected " + csv);
                }
            }
        });
        multiselectSpinner.setListAdapter(adapter).setSelectAll(false).setAllCheckedText("All types")
                .setAllUncheckedText("none selected")
                .setSelectAll(false);
    }
    private void findingIdsHere() {

        findViewById(R.id.backarrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        spinner = findViewById(R.id.spinner);
        tvBookCleanerSubmit = findViewById(R.id.tvBookCleanerSubmit);
        tvPickYourTime = findViewById(R.id.tvPickYourTime);
        tvPickYourDate = findViewById(R.id.tvPickYourDate);
        edAddress = findViewById(R.id.edAddress);
        edZipcode = findViewById(R.id.edZipcode);
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        tvBookCleanerSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edZipcode.getText().toString().isEmpty()) {
                    TastyToast.makeText(ActivityBookCleaner.this, "All feilds are required", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (edAddress.getText().toString().isEmpty()) {
                    TastyToast.makeText(ActivityBookCleaner.this, "All feilds are required", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (tvPickYourTime.getText().toString().equalsIgnoreCase("Pick your time")) {
                    TastyToast.makeText(ActivityBookCleaner.this, "All feilds are required", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (tvPickYourDate.getText().toString().equalsIgnoreCase("Pick your date")) {
                    TastyToast.makeText(ActivityBookCleaner.this, "All feilds are required", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (TextUtils.isEmpty(csv)) {
                    TastyToast.makeText(ActivityBookCleaner.this, "All feilds are required", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else {
                    loginProgressing();
                    bookACleanerApiGoesHere();
                }
            }
        });
        tvPickYourTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickYourTimeFromHere();
            }
        });
        // MARK: DATE PICKER
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        tvPickYourDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ActivityBookCleaner.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void loginProgressing() {
        mProgressDialog.setMessage("Searching to find you a cleaner , we 'll send you their profile as soon as we match !! Thank you for your patience");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }
    private void pickYourTimeFromHere() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tvPickYourTime.setText(hourOfDay + ":" + minute + ":" + "00");
                        timeCheck = hourOfDay + ":" + minute;
                        Hawk.put("TIME", hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
    private void updateLabel() {
//        yyyy-MM-dd hh:mm:ss a
        String myFormat = "yyyy-MM-dd";     //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateCheck = sdf.format(myCalendar.getTime());
        tvPickYourDate.setText(sdf.format(myCalendar.getTime()));
        Hawk.put("DATE", sdf.format(myCalendar.getTime()));
    }
}
