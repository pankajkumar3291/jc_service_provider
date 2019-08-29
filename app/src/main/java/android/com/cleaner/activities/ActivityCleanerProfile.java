package android.com.cleaner.activities;
import android.app.AlertDialog;
import android.com.cleaner.R;
import android.com.cleaner.apiResponses.filteredCleaner.FilteredCleaner;
import android.com.cleaner.apiResponses.filteredCleaner.Payload;
import android.com.cleaner.apiResponses.specialRequestToCleaner.SpecialRequestToCleaner;
import android.com.cleaner.applicationUtils.App;
import android.com.cleaner.firebaseNotification.notificationModels.ProfileInformation;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
public class ActivityCleanerProfile extends AppCompatActivity implements View.OnClickListener {
    private TextView tvBioScrollable, tvProfileContinue, tvStartTime, tvLastTime, cleanerName;
    private ImageView backarr;
    private NoInternetDialog noInternetDialog;
    private TextView tvSubmit;
    private CircleImageView cleanearsProfile;
    private RatingBar ratingBar;
    private String providerId;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private EditText edPackingInstructions, edSpecialRequestCleaner;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaners_details);
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        Hawk.init(this).build();
        if (getIntent().hasExtra("provider_id")) {
            providerId = getIntent().getStringExtra("provider_id");
        }
        changingStatusBarColorHere();
        findingIdsHere();
        if (providerId == null) {
            ((App) getApplicationContext()).getRxBus().toObservable().observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ProfileInformation>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            System.out.println("ActivityCleanerProfile.onSubscribe " + d);
                        }
                        @Override
                        public void onNext(ProfileInformation profileInformation) {
                            System.out.println("ActivityCleanerProfile.onNext " + profileInformation);
                            Picasso.get().load(profileInformation.getProviderProfileImage()).error(R.drawable.noimage).into(cleanearsProfile);
                            tvStartTime.setText(profileInformation.getProviderStartTime());
                            tvLastTime.setText(profileInformation.getProviderEndTime());
                            tvBioScrollable.setText(profileInformation.getProviderBio());
                            cleanerName.setText(profileInformation.getProviderName());
                        }
                        @Override
                        public void onError(Throwable e) {
                            System.out.println("ActivityCleanerProfile.onError " + e);
                        }
                        @Override
                        public void onComplete() {
                        }
                    });
        } else {
            filteredCleanerApi();
        }
        eventClickLietener();
    }
    private void filteredCleanerApi(){
        compositeDisposable.add(HttpModule.provideRepositoryService().filteredCleanerApi(providerId).
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<FilteredCleaner>() {
                    @Override
                    public void accept(FilteredCleaner filteredCleaner) throws Exception {
                        if (filteredCleaner != null && filteredCleaner.getIsSuccess()) {
                            TastyToast.makeText(ActivityCleanerProfile.this, filteredCleaner.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                            setTheValuesOfCleaner(filteredCleaner.getPayload());
                        } else {
                            Toast.makeText(getApplicationContext(),Objects.requireNonNull(filteredCleaner).getMessage(),Toast.LENGTH_SHORT).show();

                            TastyToast.makeText(ActivityCleanerProfile.this, Objects.requireNonNull(filteredCleaner).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getApplicationContext(),throwable.toString(),Toast.LENGTH_SHORT).show();
                    }
                }));
    }
    private void setTheValuesOfCleaner(Payload payLoad) {
        Picasso.get().load(payLoad.getProfilePic()).error(R.drawable.noimage).into(cleanearsProfile);
        tvStartTime.setText(payLoad.getStartTime());
        tvLastTime.setText(payLoad.getEndTime());
        tvBioScrollable.setText(Html.fromHtml(payLoad.getBio()));
        cleanerName.setText(payLoad.getProviderName());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
        compositeDisposable.dispose();
    }
    private void changingStatusBarColorHere() {
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityCleanerProfile.this, R.color.statusBarColor));
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    private void eventClickLietener() {
        tvProfileContinue.setOnClickListener(this);
        backarr.setOnClickListener(this);
    }
    private void findingIdsHere() {
        tvBioScrollable = findViewById(R.id.tvBioScrollable);
        tvProfileContinue = findViewById(R.id.tvProfileContinue);
        backarr = findViewById(R.id.backarr);
        cleanearsProfile = findViewById(R.id.cleanearsProfile);
        tvStartTime = findViewById(R.id.tvStartTime);
        tvLastTime = findViewById(R.id.tvLastTime);
        ratingBar = findViewById(R.id.ratingBar);
        cleanerName = findViewById(R.id.cleanerName);
        tvBioScrollable.setMovementMethod(new ScrollingMovementMethod());
//      CHANGING RATINGBAR COLOR
        Drawable progressDrawable = ratingBar.getProgressDrawable();
        if (progressDrawable != null) {
            DrawableCompat.setTint(progressDrawable, ContextCompat.getColor(ActivityCleanerProfile.this, R.color.colorPrimary));
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvProfileContinue:
//                personalInfoOfUserDialog();
                break;
            case R.id.backarr:
                finish();
                break;
        }
    }
    private void personalInfoOfUserDialog() {
        LayoutInflater li = LayoutInflater.from(ActivityCleanerProfile.this);
        View dialogView = li.inflate(R.layout.dialog_personal_info_cleaner, null);
        findingCleanerInfoIdsHere(dialogView);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityCleanerProfile.this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder
                .setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    private void findingCleanerInfoIdsHere(View dialogView) {
        tvSubmit = dialogView.findViewById(R.id.tvSubmit);
        edPackingInstructions = dialogView.findViewById(R.id.edPackingInstructions);
        edSpecialRequestCleaner = dialogView.findViewById(R.id.edSpecialRequestCleaner);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edPackingInstructions.getText().toString())) {
                    TastyToast.makeText(ActivityCleanerProfile.this, "All feilds are required", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (TextUtils.isEmpty(edSpecialRequestCleaner.getText().toString())) {
                    TastyToast.makeText(ActivityCleanerProfile.this, "All feilds are required", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else {
                    callTheSpecialRequestToCleanerApi();
                }
            }
        });
    }
    private void callTheSpecialRequestToCleanerApi() {
        compositeDisposable.add(HttpModule.provideRepositoryService().specialRequestToCleaner("40",
                (String) Hawk.get("savedUserId"), providerId, edPackingInstructions.getText().toString(), edSpecialRequestCleaner.getText().toString()).
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<SpecialRequestToCleaner>() {
                    @Override
                    public void accept(SpecialRequestToCleaner specialRequestToCleaner) throws Exception {
                        if (specialRequestToCleaner != null && specialRequestToCleaner.getIsSuccess()) {
                            TastyToast.makeText(ActivityCleanerProfile.this, specialRequestToCleaner.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                        } else {
                            TastyToast.makeText(ActivityCleanerProfile.this, Objects.requireNonNull(specialRequestToCleaner).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        TastyToast.makeText(ActivityCleanerProfile.this, throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    }
                }));
    }
}







