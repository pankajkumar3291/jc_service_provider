package android.com.cleaner.fragments;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.com.cleaner.R;
import android.com.cleaner.activities.ActivitySignIn;
import android.com.cleaner.apiResponses.changePassword.ChangePassword;
import android.com.cleaner.apiResponses.customerFullDetailsApis.CustomerFullDetails;
import android.com.cleaner.apiResponses.customerFullDetailsApis.PayLoad;
import android.com.cleaner.apiResponses.getCityList.CityList;
import android.com.cleaner.apiResponses.getStateList.GetStateList;
import android.com.cleaner.apiResponses.getStateList.Payload;
import android.com.cleaner.apiResponses.getZipcodeList.ZipCodeList;
import android.com.cleaner.apiResponses.updateCustomerProfile.UpdateCustomerProfile;
import android.com.cleaner.apiResponses.updateProfile.GetProfile;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
public class FragmentAddress extends BaseFragment implements View.OnClickListener {
    private View view;
    private ImageView chooserImage;
    private CircleImageView profileImage;
    private EditText edName, edEmail, edPassword, edAddress, edPhoneNumber, edCity, edZipCode;
    private Spinner spinnerStates, spinnerCity, spinnerZipcode;
    private TextView tvChangePass;
    private Button SignUpBtn;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private NoInternetDialog noInternetDialog;
    private EditText edOldPass, edNewPassword, edPasswordConfirm;
    private TextView tvSave;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ProgressDialog mProgressDialog;
    private ArrayList<String> arrayStateId = new ArrayList<>();
    private ArrayList<String> arrayCityId = new ArrayList<>();
    private ArrayList<String> arraZipcodeId = new ArrayList<>();
    private TextView tvStayHere, tvLoginAgain, tvMessage;
    private boolean isStateSelected, isCitySelected, isZipCodeSelected;
    private String state, city, zipcode;
    private String stateId, cityId, zipcodeId;
    private ArrayAdapter<String> dataAdapterForCities;
    private ArrayAdapter<String> dataAdapterForZipcodes;
    List<String> arrayCitiesName;
    ArrayList<String> arrayZipcodeNames;
    String nameCheck, addressCheck, phoneNumberCheck;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_address, container, false);
        noInternetDialog = new NoInternetDialog.Builder(getContext()).build();
        mProgressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
        findingIdsHere(view);
        // MARK: CUSTOMER FULL DETAIL API
        customerFullDetails();
        eventsListenerHere();
        return view;
    }
    private void choosingStateFromHere() {
        HttpModule.provideRepositoryService().getStateListAPI().enqueue(new Callback<GetStateList>() {
            @Override
            public void onResponse(Call<GetStateList> call, Response<GetStateList> response) {
                if (response.body() != null && Objects.requireNonNull(response.body()).getIsSuccess()) {
                    setupStateHere(response.body().getPayload());
                } else {
                    System.out.println("FragmentAddress.onResponse SUCCESS FALSE " + Objects.requireNonNull(response.body()).getMessage());
                }
            }
            @Override
            public void onFailure(Call<GetStateList> call, Throwable t) {
                System.out.println("FragmentAddress.onFailure ONFAILURE " + t.toString());
            }
        });
    }
    private void setupStateHere(List<Payload> payload) {
        ArrayList<String> arrayStateNames = new ArrayList<>();
        arrayStateId.clear();
        for (int i = 0; i < payload.size(); i++) {
            arrayStateNames.add(payload.get(i).getName());
            arrayStateId.add(String.valueOf(payload.get(i).getId()));
        }
        System.out.println("FragmentAddress.setupStateHere " + arrayStateNames);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),
                android.R.layout.simple_spinner_item, arrayStateNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStates.setDropDownWidth(1000);
        spinnerStates.setAdapter(dataAdapter);
        int statePosition = dataAdapter.getPosition(state);
        spinnerStates.setSelection(statePosition);
        isStateSelected = true;
        itemSelectionForState();
    }
    // MARK: ITEM SELECTION FOR STATE
    private void itemSelectionForState() {
        spinnerStates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!stateId.equals(arrayStateId.get(i))) {
                    isCitySelected = false;
                }
                stateId = arrayStateId.get(i);
                // MARK: CHOOSING CITIES
                choosingCitiesFromHere(stateId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    private void choosingCitiesFromHere(String selectedStateIds) {
        compositeDisposable.add(HttpModule.provideRepositoryService().getCitiesAPI(selectedStateIds).
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<CityList>() {
                    @Override
                    public void accept(CityList cityList) throws Exception {
                        if (cityList != null && cityList.getIsSuccess()) {
                            System.out.println("FragmentAddress.accept SUCCESS TRUE" + cityList.getMessage());
                            setupCitiesFromHere(cityList);
                        } else {
                            arrayCityId.clear();
                            arrayCitiesName.clear();
                            dataAdapterForCities.notifyDataSetChanged();
                            // MAKR: CLEARING AND REFRESHING THE ADAPTER FOR ZIPCODES AS WELL
                            arraZipcodeId.clear();
                            arrayZipcodeNames.clear();
                            dataAdapterForZipcodes.notifyDataSetChanged();
                            System.out.println("FragmentAddress.accept SUCCESS FALSE" + Objects.requireNonNull(cityList).getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("FragmentAddress.accept SUCCESS TRUE" + throwable.toString());
                    }
                }));
    }
    private void setupCitiesFromHere(CityList cityList) {
        arrayCitiesName = new ArrayList<>();
        arrayCityId.clear();
        arrayCitiesName.clear();
        for (int j = 0; j < cityList.getPayload().size(); j++) {
            arrayCitiesName.add(cityList.getPayload().get(j).getName());
            arrayCityId.add(String.valueOf(cityList.getPayload().get(j).getId()));
        }
        dataAdapterForCities = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, arrayCitiesName);
        dataAdapterForCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setDropDownWidth(1000);
        spinnerCity.setAdapter(dataAdapterForCities);
        int cityPosition = dataAdapterForCities.getPosition(city);
        spinnerCity.setSelection(cityPosition);
        isCitySelected = true;
        itemSelectionForCities();
    }
    private void itemSelectionForCities() {
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!cityId.equals(arrayCityId.get(i))) {
                    isCitySelected = false;
                }
                cityId = arrayCityId.get(i);
                choosingZipcodeFromHere(cityId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    private void choosingZipcodeFromHere(String selectedCityIds) {
        compositeDisposable.add(HttpModule.provideRepositoryService().getZipCodeListAPI(selectedCityIds).
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<ZipCodeList>() {
                    @Override
                    public void accept(ZipCodeList zipCodeList) throws Exception {
                        if (zipCodeList != null && zipCodeList.getIsSuccess()) {
                            setupZipcodeFromHere(zipCodeList);
                        } else {
                            arraZipcodeId.clear();
                            arrayZipcodeNames.clear();
                            dataAdapterForZipcodes.notifyDataSetChanged();
                            System.out.println("FragmentAddress.accept  SUCCESS FALSE " + Objects.requireNonNull(zipCodeList).getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("FragmentAddress.accept ONFAILURE " + throwable.toString());
                    }
                }));
    }
    private void setupZipcodeFromHere(ZipCodeList zipCodeList) {
        arrayZipcodeNames = new ArrayList<>();
        arraZipcodeId.clear();
        arrayZipcodeNames.clear();
        for (int k = 0; k < zipCodeList.getPayload().size(); k++) {
            arrayZipcodeNames.add(zipCodeList.getPayload().get(k).getZipcode());
            arraZipcodeId.add(String.valueOf(zipCodeList.getPayload().get(k).getCityId()));
        }
        dataAdapterForZipcodes = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, arrayZipcodeNames);
        dataAdapterForZipcodes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerZipcode.setDropDownWidth(1000);
        spinnerZipcode.setAdapter(dataAdapterForZipcodes);
        int zipcodePosition = dataAdapterForZipcodes.getPosition(zipcode);
        spinnerZipcode.setSelection(zipcodePosition);
        isZipCodeSelected = true;
        itemSelectionForZipcodes();
    }
    private void itemSelectionForZipcodes() {
        spinnerZipcode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!zipcodeId.equals(arraZipcodeId.get(i))) {
                    isZipCodeSelected = false;
                }
                zipcodeId = arraZipcodeId.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    private void customerFullDetails(){
        compositeDisposable.add(HttpModule.provideRepositoryService().customerFullDetailsApi(String.valueOf(Hawk.get("savedUserId"))).
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<CustomerFullDetails>() {
                    @Override
                    public void accept(CustomerFullDetails customerFullDetails) throws Exception {
                        if (customerFullDetails != null && customerFullDetails.getIsSuccess()) {
                            System.out.println("FragmentAddress.accept ONSUCCESS IS CALLING HERE " + customerFullDetails.getMessage());
                            nameCheck = customerFullDetails.getPayLoad().getUserDetails().getFirstName();
                            addressCheck = customerFullDetails.getPayLoad().getAddress();
                            phoneNumberCheck = customerFullDetails.getPayLoad().getUserDetails().getMobile();
                            edName.setText(customerFullDetails.getPayLoad().getUserDetails().getFirstName());
                            edEmail.setText(customerFullDetails.getPayLoad().getUserDetails().getEmail());
                            edAddress.setText(customerFullDetails.getPayLoad().getAddress());
                            edPhoneNumber.setText(customerFullDetails.getPayLoad().getUserDetails().getMobile());
                            edCity.setText(customerFullDetails.getPayLoad().getCityname());
                            edPassword.setText((CharSequence) Hawk.get("PASSWORD"));
                            state = customerFullDetails.getPayLoad().getState();
                            city = customerFullDetails.getPayLoad().getCityname();
                            zipcode = customerFullDetails.getPayLoad().getZipcode();
                            stateId = String.valueOf(customerFullDetails.getPayLoad().getStateId());
                            cityId = customerFullDetails.getPayLoad().getCityId();
                            zipcodeId = customerFullDetails.getPayLoad().getZipcodeId();




                            Picasso.get().load(customerFullDetails.getPayLoad().getUserDetails().getImage()).resize(100, 100).error(R.drawable.ic_user).into(profileImage);
                            Hawk.put("cEmail", customerFullDetails.getPayLoad().getUserDetails().getEmail());
                            Hawk.put("cName", customerFullDetails.getPayLoad().getUserDetails().getFirstName());
                            Hawk.put("cImg", customerFullDetails.getPayLoad().getUserDetails().getImage());
                            // MARK: CHOOSING STATE API
                            choosingStateFromHere();
                        } else {
                            System.out.println("FragmentAddress.accept ONSUCCESS BUT GETTING FALSE RESPONSE -" + Objects.requireNonNull(customerFullDetails).getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("FragmentAddress.accept ONFAILURE IS CALLING HERE " + throwable.toString());
                    }
                }));
    }
    @Override
    public void onResume() {
        super.onResume();
        String updatedImage = Hawk.get("TEMP");
//        Bitmap bitmap = null;
//
//        try {
//            byte[] encodeByte = Base64.decode(updatedImage, Base64.DEFAULT);
//            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//        } catch (Exception e) {
//            e.getMessage();
//        }
        Picasso.get().load(updatedImage).resize(100, 100).error(R.drawable.ic_user).into(profileImage);
    }
    private void allSpinnerSetup(PayLoad payLoad) {
        // MARK: STATE SPINNER
        ArrayList<String> state = new ArrayList<>();
        state.add(String.valueOf(Hawk.get("STATE_VALUE")));
        state.add(payLoad.getState());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, state);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStates.setDropDownWidth(1000);
        spinnerStates.setAdapter(dataAdapter);
        // MARK: CITY SPINNER
        ArrayList<String> city = new ArrayList<>();
        city.add(String.valueOf(Hawk.get("CITY_VALUE")));
        city.add(payLoad.getCityname());
        ArrayAdapter<String> dataAdapterCity = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, city);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setDropDownWidth(1000);
        spinnerCity.setAdapter(dataAdapterCity);
        // MARK: ZIPCODE SPINNER
        ArrayList<String> zipcode = new ArrayList<>();
        zipcode.add(String.valueOf(Hawk.get("ZIPCODE_VALUE")));
        zipcode.add(payLoad.getZipcode());
        ArrayAdapter<String> dataAdapterZipcode = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, zipcode);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerZipcode.setDropDownWidth(1000);
        spinnerZipcode.setAdapter(dataAdapterZipcode);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
        compositeDisposable.dispose();
    }
    private void eventsListenerHere() {
        chooserImage.setOnClickListener(this);
    }
    private void findingIdsHere(View view) {
        chooserImage = view.findViewById(R.id.chooserImage);
        profileImage = view.findViewById(R.id.profileImage);
        spinnerStates = view.findViewById(R.id.spinnerStates);
        spinnerCity = view.findViewById(R.id.spinnerCity);
        spinnerZipcode = view.findViewById(R.id.spinnerZipcode);
        tvChangePass = view.findViewById(R.id.tvChangePass);
        SignUpBtn = view.findViewById(R.id.SignUpBtn);
        edName = view.findViewById(R.id.edName);
        edEmail = view.findViewById(R.id.edEmail);
        edPassword = view.findViewById(R.id.edPassword);
        edAddress = view.findViewById(R.id.edAddress);
        edPhoneNumber = view.findViewById(R.id.edPhoneNumber);
        edCity = view.findViewById(R.id.edCity);
        edZipCode = view.findViewById(R.id.edZipCode);
        tvChangePass.setPaintFlags(tvChangePass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswordDialog();
            }
        });
        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingProgress();
                updateCustomerProfile();
            }
        });
    }
    private void updateCustomerProfile() {
        if (edName.getText().toString().equalsIgnoreCase(nameCheck) && edPhoneNumber.getText().toString().
                equalsIgnoreCase(phoneNumberCheck) && edAddress.getText().toString().equalsIgnoreCase(addressCheck) &&
                spinnerStates.getSelectedItem().toString().equalsIgnoreCase(state)
                && spinnerCity.getSelectedItem().toString().equalsIgnoreCase(city) &&
                spinnerZipcode.getSelectedItem().toString().equalsIgnoreCase(zipcode)) {
            mProgressDialog.dismiss();
            TastyToast.makeText(getActivity(), "You have not updated anything new", TastyToast.LENGTH_SHORT, TastyToast.WARNING).show();
        } else {
            compositeDisposable.add(HttpModule.provideRepositoryService().
                    updateCustomerProfileApi(String.valueOf(Hawk.get("savedUserId")), edName.getText().toString(),
                            "JC", edPhoneNumber.getText().toString(),
                            edAddress.getText().toString(),
                            stateId,
                            cityId,
                            zipcodeId).
                    subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribe(new Consumer<UpdateCustomerProfile>() {
                        @Override
                        public void accept(UpdateCustomerProfile updateCustomerProfile) throws Exception {
                            if (updateCustomerProfile != null && updateCustomerProfile.getIsSuccess()) {
                                mProgressDialog.dismiss();
                                updatingProfileDialog(updateCustomerProfile.getMessage());
                            } else {
                                mProgressDialog.dismiss();
                                TastyToast.makeText(getActivity(), Objects.requireNonNull(updateCustomerProfile).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mProgressDialog.dismiss();
                            System.out.println("FragmentAddress.accept " + throwable.toString());
                            TastyToast.makeText(getActivity(), "Internal server error", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }
                    }));
        }
    }
    private void updatingProfileDialog(String message) {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View dialogView = li.inflate(R.layout.dialog_show_for_update_profile, null);
        findingDialogIdsForUpdateProfile(dialogView, message);
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder
                .setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    private void findingDialogIdsForUpdateProfile(View dialogView, String message) {
        tvMessage = dialogView.findViewById(R.id.tvMessage);
        tvMessage.setText(message);
        tvStayHere = dialogView.findViewById(R.id.tvStayHere);
        tvLoginAgain = dialogView.findViewById(R.id.tvLoginAgain);
        tvStayHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerFullDetails();
                alertDialog.dismiss();
            }
        });
        tvLoginAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivitySignIn.class);
                Objects.requireNonNull(getActivity()).startActivity(intent);
            }
        });
    }
    private void loadingProgress() {
        mProgressDialog.setMessage("Updating your profile..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }
    private void settingTheValuesHere(PayLoad payLoad) {
        Hawk.put("FirstName", payLoad.getUserDetails().getFirstName());
        Hawk.put("LastName", payLoad.getUserDetails().getLastName());
        Hawk.put("Address", payLoad.getAddress());
        Hawk.put("PhoneNumber", payLoad.getUserDetails().getMobile());
        Hawk.put("City", payLoad.getCityname());
        edName.setEnabled(true);
        edEmail.setEnabled(false);
        edPhoneNumber.setEnabled(true);
        edPassword.setEnabled(false);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(CalligraphyContextWrapper.wrap(context));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chooserImage:
                choosingTheImageFromHere();
                break;
        }
    }
    private void choosingTheImageFromHere() {
        PickSetup setup = new PickSetup()
                .setTitle("Choose")
                .setTitleColor(Color.WHITE)
                .setBackgroundColor(Color.WHITE)
                .setCancelText("Close")
                .setCancelTextColor(Color.WHITE)
                .setButtonTextColor(Color.WHITE)
                .setMaxSize(500)
                .setPickTypes(EPickType.GALLERY, EPickType.CAMERA)
                .setCameraButtonText("Camera")
                .setGalleryButtonText("Gallery")
                .setIconGravity(Gravity.LEFT)
                .setButtonOrientation(LinearLayout.VERTICAL)// LinearLayoutCompat.VERTICAL
                .setSystemDialog(false)
                .setGalleryIcon(R.drawable.gallery_picker)
                .setCameraIcon(R.drawable.camera_picker)
                .setSystemDialog(false).setBackgroundColor(Color.parseColor("#FF146BAC"));
        PickImageDialog.build(setup).setOnPickResult(new IPickResult() {
            @Override
            public void onPickResult(PickResult pickResult) {
                mProgressDialog.setMessage("Loding..");
                mProgressDialog.setCancelable(false);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.show();
                userProfilePicApi(pickResult);
            }
        }).setOnPickCancel(new IPickCancel() {
            @Override
            public void onCancelClick() {
            }
        }).show(Objects.requireNonNull(getActivity()));
    }
    private void userProfilePicApi(final PickResult pickResult) {
        File file = new File(pickResult.getPath());
        final RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(Hawk.get("USER_ID")));
        HttpModule.provideRepositoryService().postImage(id, body).enqueue(new Callback<GetProfile>() {
            @Override
            public void onResponse(Call<GetProfile> call, Response<GetProfile> response) {
                if (response.body() != null && Objects.requireNonNull(response.body()).getIsSuccess()) {
                    mProgressDialog.dismiss();
                    profileImage.setImageBitmap(pickResult.getBitmap());
//                    MARK: CONVERTING BITMAP INTO STRING
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    pickResult.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] b = baos.toByteArray();
                    String temp = Base64.encodeToString(b, Base64.DEFAULT);
                    Hawk.put("TEMP", temp);
                    showTheDialogMessageForOk(Objects.requireNonNull(response.body()).getMessage());
                } else {
                    mProgressDialog.dismiss();
                    try {
                        showTheDialogMessageForError(Objects.requireNonNull(response.body()).getMessage());
                    } catch (Exception e) {
                        System.out.println("FragmentAddress.onResponse " + e);
                    }
                }
            }
            @Override
            public void onFailure(Call<GetProfile> call, Throwable t) {
                mProgressDialog.dismiss();
                TastyToast.makeText(getActivity(), t.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
            }
        });
    }
    private void changePasswordDialog() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View dialogView = li.inflate(R.layout.dialog_change_password, null);
        findingDialodIdsHere(dialogView);
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    private void findingDialodIdsHere(View dialogView) {
        edOldPass = dialogView.findViewById(R.id.edOldPass);
        edNewPassword = dialogView.findViewById(R.id.edNewPassword);
        edPasswordConfirm = dialogView.findViewById(R.id.edPasswordConfirm);
        tvSave = dialogView.findViewById(R.id.tvSave);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswordApiGoesHere();
            }
        });
    }
    private void changePasswordApiGoesHere() {
        compositeDisposable.add(HttpModule.provideRepositoryService().changePasswordAPI(String.valueOf(Hawk.get("savedUserId")), edOldPass.getText().toString(), edNewPassword.getText().toString()).
                subscribeOn(io.reactivex.schedulers.Schedulers.computation()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<ChangePassword>() {
                    @Override
                    public void accept(ChangePassword changePassword) throws Exception {
                        if (changePassword != null && changePassword.getIsSuccess()) {
                            TastyToast.makeText(getActivity(), changePassword.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                            alertDialog.dismiss();
                        } else {
                            TastyToast.makeText(getActivity(), changePassword.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        TastyToast.makeText(getActivity(), throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    }
                }));
    }
}
