package android.com.cleaner.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.com.cleaner.R;
import android.com.cleaner.apiResponses.bookACleaner.BookCleaner;
import android.com.cleaner.apiResponses.typesOfServices.Payload;
import android.com.cleaner.apiResponses.typesOfServices.TypesOfSerives;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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

    private PlaceAutocompleteFragment placeAutocompleteFragment;
    private String selectedPlace = "";
    private NoInternetDialog noInternetDialog;
    private int mHour, mMinute;

    private double lat, lng;

    final Calendar myCalendar = Calendar.getInstance();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_cleaner);

        Hawk.init(this).build();
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityBookCleaner.this, R.color.statusBarColor));


        findingIdsHere();
        typesOfSerivesApi();
        googlePlacesApisGoesHere(tvBookCleanerSubmit);


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void googlePlacesApisGoesHere(TextView tvBookCleanerSubmit) {


//        http://velmm.com/google-places-autocomplete-android-example/


        placeAutocompleteFragment = (PlaceAutocompleteFragment) this.getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()/*.setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)*/.build();

        placeAutocompleteFragment.setFilter(autocompleteFilter);

        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                latlngFromAddress(place);
                selectedPlace = place.getName().toString();


            }

            @Override
            public void onError(Status status) {

                System.out.println("PlacesActivity.onError " + status.toString());
                Toast.makeText(ActivityBookCleaner.this, status.toString(), Toast.LENGTH_SHORT).show();

            }
        });


        tvBookCleanerSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (selectedPlace.matches("")) {

                    TastyToast.makeText(ActivityBookCleaner.this, "Address can not be blank", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                } else {
                    bookACleanerApiGoesHere();

                }


            }
        });


    }

    private void bookACleanerApiGoesHere() {


        compositeDisposable.add(HttpModule.provideRepositoryService().
                bookCleanerAPI(String.valueOf(Hawk.get("savedUserId")), String.valueOf(lat), String.valueOf(lng), String.valueOf(Hawk.get("DATE")), String.valueOf(Hawk.get("TIME")), "1").
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<BookCleaner>() {


                    @Override
                    public void accept(BookCleaner bookCleaner) throws Exception {


                        if (bookCleaner != null && bookCleaner.getIsSuccess()) {

                            Intent intent = new Intent(ActivityBookCleaner.this, ActivityCleanerProfile.class);
                            startActivity(intent);
                            finish();

                            TastyToast.makeText(ActivityBookCleaner.this, bookCleaner.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                        } else {

                            TastyToast.makeText(ActivityBookCleaner.this, Objects.requireNonNull(bookCleaner).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }


                    }


                }, new Consumer<Throwable>() {


                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        TastyToast.makeText(ActivityBookCleaner.this, throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                    }


                }));


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


        HttpModule.provideRepositoryService().makingTypesOfServices().enqueue(new Callback<TypesOfSerives>() {
            @Override
            public void onResponse(Call<TypesOfSerives> call, Response<TypesOfSerives> response) {


                if (response.body() != null) {

                    if (response.body() != null && response.body().getIsSuccess()) {


                        int position = 0;

                        spinnerSetUpHere(response.body().getPayload(), position);


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

    private void spinnerSetUpHere(List<Payload> payload, int position) {


        ArrayList<String> foo_array = new ArrayList<>();

        for (int i = 0; i < payload.size(); i++) {
            foo_array.add(payload.get(i).getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ActivityBookCleaner.this, android.R.layout.simple_spinner_item, foo_array);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setDropDownWidth(1000);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedItemText = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                TastyToast.makeText(ActivityBookCleaner.this, parent + "", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

            }
        });


    }

    private void findingIdsHere() {

        spinner = findViewById(R.id.spinner);
        tvBookCleanerSubmit = findViewById(R.id.tvBookCleanerSubmit);
        tvPickYourTime = findViewById(R.id.tvPickYourTime);
        tvPickYourDate = findViewById(R.id.tvPickYourDate);


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

                        tvPickYourTime.setText(hourOfDay + ":" + minute);

                        Hawk.put("TIME", hourOfDay + ":" + minute);

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();


    }

    private void updateLabel() {

        String myFormat = "MM/dd/yy";     //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tvPickYourDate.setText(sdf.format(myCalendar.getTime()));
        Hawk.put("DATE", sdf.format(myCalendar.getTime()));

    }


}
