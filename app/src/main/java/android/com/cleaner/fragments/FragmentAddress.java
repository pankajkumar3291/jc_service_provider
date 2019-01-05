package android.com.cleaner.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.com.cleaner.R;
import android.com.cleaner.activities.ActivitySignIn;
import android.com.cleaner.apiResponses.changePassword.ChangePassword;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_address, container, false);


        noInternetDialog = new NoInternetDialog.Builder(getContext()).build();
        mProgressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
        findingIdsHere(view);
        eventsListenerHere();


        allSpinnerSetup();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        String updatedImage = Hawk.get("TEMP");
        Bitmap bitmap = null;

        try {
            byte[] encodeByte = Base64.decode(updatedImage, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
        }
        profileImage.setImageBitmap(bitmap);

    }

    private void allSpinnerSetup() {


        // MARK: STATE SPINNER
        ArrayList<String> state = new ArrayList<>();
        state.add(String.valueOf(Hawk.get("STATE_VALUE")));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, state);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStates.setDropDownWidth(1000);
        spinnerStates.setAdapter(dataAdapter);


        // MARK: CITY SPINNER
        ArrayList<String> city = new ArrayList<>();
        city.add(String.valueOf(Hawk.get("CITY_VALUE")));

        ArrayAdapter<String> dataAdapterCity = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, city);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setDropDownWidth(1000);
        spinnerCity.setAdapter(dataAdapterCity);


        // MARK: ZIPCODE SPINNER
        ArrayList<String> zipcode = new ArrayList<>();
        zipcode.add(String.valueOf(Hawk.get("ZIPCODE_VALUE")));

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

                Intent intent = new Intent(getActivity(), ActivitySignIn.class);
                startActivity(intent);

            }
        });

        settingTheValuesHere();


    }

    private void settingTheValuesHere() {

        edName.setText((CharSequence) Hawk.get("FIRST_NAME"));
        edEmail.setText((CharSequence) Hawk.get("EMAIL_ID"));
        edAddress.setText((CharSequence) Hawk.get("FIRST_NAME"));
        edPhoneNumber.setText((CharSequence) Hawk.get("PHONE_NUMBER"));
        edCity.setText((CharSequence) Hawk.get("CITY"));
        edZipCode.setText((CharSequence) Hawk.get("ZIP_CODE"));
        edPassword.setText((CharSequence) Hawk.get("PASSWORD"));
        edAddress.setText((CharSequence) Hawk.get("ADDRESS"));


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
                .setButtonOrientation(LinearLayoutCompat.VERTICAL)
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
