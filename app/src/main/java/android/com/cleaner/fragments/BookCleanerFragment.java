package android.com.cleaner.fragments;

import android.app.AlertDialog;
import android.com.cleaner.R;
import android.com.cleaner.activities.ActivityBookCleaner;
import android.com.cleaner.activities.ActivityCleanerProfile;
import android.com.cleaner.activities.ActivityPreviousJobs;
import android.com.cleaner.activities.ActivitySchedule;
import android.com.cleaner.apiResponses.typesOfServices.Payload;
import android.com.cleaner.apiResponses.typesOfServices.TypesOfSerives;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.com.cleaner.latlngFromAddressUtils.GeocoderHandler;
import android.com.cleaner.latlngFromAddressUtils.GeocodingLocation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BookCleanerFragment extends Fragment implements View.OnClickListener {


    private LinearLayout linearBookCleanerbtn, linearRebook, linearCreateAutomaticCleaning;
    private View view;


    private TextView tvBookCleanerSubmit, tvSelectServices;
    private Spinner spinner;


    private NoInternetDialog noInternetDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private PlaceAutocompleteFragment placeAutocompleteFragment;
    private String selectedPlace = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_book_cleaner, container, false);

        noInternetDialog = new NoInternetDialog.Builder(getActivity()).build();

        findingIdsHere(view);
        eventListener();


        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(CalligraphyContextWrapper.wrap(context));

    }


    private void eventListener() {

        linearBookCleanerbtn.setOnClickListener(this);
        linearRebook.setOnClickListener(this);
        linearCreateAutomaticCleaning.setOnClickListener(this);

    }


    private void findingIdsHere(View view) {

        linearBookCleanerbtn = view.findViewById(R.id.linearBookCleanerbtn);
        linearRebook = view.findViewById(R.id.linearRebook);
        linearCreateAutomaticCleaning = view.findViewById(R.id.linearCreateAutomaticCleaning);
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.linearBookCleanerbtn:

                bookACleanerDialog();

                break;

            case R.id.linearRebook:

                openingPreviousJobsHere();

                break;

            case R.id.linearCreateAutomaticCleaning:


                Intent intent = new Intent(getActivity(), ActivitySchedule.class);
                startActivity(intent);

                break;


        }


    }

    private void openingPreviousJobsHere() {

        Intent intent = new Intent(getActivity(), ActivityPreviousJobs.class);
        startActivity(intent);
    }


    private void bookACleanerDialog() {


        Intent intent = new Intent(getActivity(), ActivityBookCleaner.class);
        startActivity(intent);


//        LayoutInflater li = LayoutInflater.from(getActivity());
//        View dialogView = li.inflate(R.layout.dialog_book_cleaner, null);
//
//        findingBookACleanerIdsHere(dialogView);
//
//        alertDialogBuilder = new AlertDialog.Builder(getActivity());
//        alertDialogBuilder.setView(dialogView);
//        alertDialogBuilder.setCancelable(true);
//        alertDialog = alertDialogBuilder.create();
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        alertDialog.show();

    }

    private void findingBookACleanerIdsHere(View dialogView) {

//        spinner = dialogView.findViewById(R.id.spinner);
//        tvBookCleanerSubmit = dialogView.findViewById(R.id.tvBookCleanerSubmit);
//        tvSelectServices = dialogView.findViewById(R.id.tvSelectServices);
//
//
//        googlePlacesApisGoesHere(tvBookCleanerSubmit);
//        typesOfSerivesApi();


    }

    private void googlePlacesApisGoesHere(TextView tvBookCleanerSubmit) {


//        http://velmm.com/google-places-autocomplete-android-example/


        placeAutocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()/*.setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)*/.build();

        placeAutocompleteFragment.setFilter(autocompleteFilter);

        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                Toast.makeText(getActivity(), place.getName().toString(), Toast.LENGTH_SHORT).show();

                latlngFromAddress(place);

                selectedPlace = place.getName().toString();


            }

            @Override
            public void onError(Status status) {

                System.out.println("PlacesActivity.onError " + status.toString());
                Toast.makeText(getActivity(), status.toString(), Toast.LENGTH_SHORT).show();

            }
        });


        tvBookCleanerSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (selectedPlace.matches("")) {

                    TastyToast.makeText(getActivity(), "Address can not be blank", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                } else {

                    Intent intent = new Intent(getActivity(), ActivityCleanerProfile.class);
                    startActivity(intent);
                    alertDialog.dismiss();
                }


            }
        });


    }

    private void latlngFromAddress(Place place) {

        TastyToast.makeText(getActivity(), place.getName().toString(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
//      GeocodingLocation locationAddress = new GeocodingLocation();
        GeocodingLocation.getAddressFromLocation(place.getName().toString(), getActivity(), new GeocoderHandler());


    }


    private void typesOfSerivesApi() {

        HttpModule.provideRepositoryService().makingTypesOfServices().enqueue(new Callback<TypesOfSerives>() {
            @Override
            public void onResponse(Call<TypesOfSerives> call, Response<TypesOfSerives> response) {


                if (response.body() != null) {

                    if (response.body() != null && response.body().getIsSuccess()) {

                        tvSelectServices.setVisibility(View.VISIBLE);

                        int position = 0;

                        spinnerSetUpHere(response.body().getPayload(), position);


                    } else {

                        TastyToast.makeText(getActivity(), response.body().getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                    }

                } else {

                    TastyToast.makeText(getActivity(), "Null Body", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                }
            }

            @Override
            public void onFailure(Call<TypesOfSerives> call, Throwable t) {

            }
        });


    }

    private void spinnerSetUpHere(List<Payload> payload, int position) {

        ArrayList<String> foo_array = new ArrayList<>();

        for (int i = 0; i < payload.size(); i++) {
            foo_array.add(payload.get(i).getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, foo_array);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setDropDownWidth(800);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedItemText = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                TastyToast.makeText(getActivity(), parent + "", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

            }
        });


    }


}
