package android.com.cleaner.activities;

import android.app.ProgressDialog;
import android.com.cleaner.R;
import android.com.cleaner.apiResponses.signInResponse.SignIn;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.com.cleaner.sharedPrefrence.SharedPrefsHelper;
import android.com.cleaner.utils.AppConstant;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.List;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spencerstudios.com.bungeelib.Bungee;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ActivitySignIn extends BaseActivity implements View.OnClickListener, Validator.ValidationListener {


    private ImageView backArrowImage;

    @NotEmpty(message = "Please enter the Phone/Email")
    private EditText edEmailPhone;

    @NotEmpty
    @Password(message = "Please enter the valid password", min = 6)
    //,scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS
    private EditText edPassword;

    private CheckBox chKeemMeLoggedIn;
    private TextView btnDontHave;
    private TextView loginBtn;

    private RelativeLayout relativeDontHaveAnAccount;
    private TextView tvForgetPass;

    private Context context;
    private String emailValue, passwordValue, keep_me_logged_in;

    Validator validator;
    private ProgressDialog mProgressDialog;

    private NoInternetDialog noInternetDialog;
    private SharedPrefsHelper preferences;


//    TextInputLayout   usernameTextObj = (TextInputLayout)    findViewById(R.id.input_username_id));
//    font = Typeface.createFromAsset(getAssets(), "fonts/ir.ttf");
//    usernameTextObj .setTypeface(font);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_new);


        context = this;

        noInternetDialog = new NoInternetDialog.Builder(this).build();
        Hawk.init(this).build();


        findingIdsHere();
        initializingValidationHere();
        eventListener();
        changingStatusBarColorHere();


//        editText.setTypeface(Typeface.SERIF);


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

    private void initializingValidationHere() {

        validator = new Validator(this);
        validator.setValidationListener(this);
    }


    private void gettingValuesFromHawk() {

        emailValue = Hawk.get("email");
        passwordValue = Hawk.get("password");


    }

    private void savingValuesInHawk() {

        Hawk.put("email", edEmailPhone.getText().toString());
        Hawk.put("password", edPassword.getText().toString());


    }

    private void setupWindowAnimations() {

        Transition fade = TransitionInflater.from(this).inflateTransition(R.transition.slide_and_changebounds_sequential);
        getWindow().setEnterTransition(fade);

    }

    private void callingActivity() {

        mProgressDialog.dismiss();
        Intent intent1 = new Intent(ActivitySignIn.this, DashboardActivity.class);
//        Bungee.zoom(context);
        intent1.putExtra("Uniqid", "ActivitySignIn");
        startActivity(intent1);
        finish();

    }


    private void changingStatusBarColorHere() {

        getWindow().setStatusBarColor(ContextCompat.getColor(ActivitySignIn.this, R.color.statusBarColor));
    }


    private void eventListener() {

        backArrowImage.setOnClickListener(this);

        tvForgetPass.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        relativeDontHaveAnAccount.setOnClickListener(this);
        btnDontHave.setOnClickListener(this);
    }

    private void findingIdsHere() {

        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        tvForgetPass = findViewById(R.id.tvForgetPass);
        backArrowImage = findViewById(R.id.backArrowImage);
        loginBtn = findViewById(R.id.loginBtn);

        edEmailPhone = findViewById(R.id.edEmailPhone);
        edPassword = findViewById(R.id.edPassword);
        chKeemMeLoggedIn = findViewById(R.id.chKeemMeLoggedIn);
        relativeDontHaveAnAccount = findViewById(R.id.relativeDontHaveAnAccount);
        btnDontHave = findViewById(R.id.btnDontHave);


        preferences = new SharedPrefsHelper(ActivitySignIn.this);


        TextInputLayout usernameTextObj = (TextInputLayout) findViewById(R.id.tilTwo);
        usernameTextObj.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/headingbrew.otf"));


    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Bungee.slideLeft(context); //fire the slide left animation
//    }


    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.backArrowImage:

                animationForImageBackArrowHere();
                finish();
                break;


            case R.id.tvForgetPass:
                callingForgetPasswordActivityHere();
                break;


            case R.id.loginBtn:

                loginProgressing();
                validator.validate();


                break;

            case R.id.btnDontHave:

                Intent intent1 = new Intent(ActivitySignIn.this, ActivitySignUp.class);
                startActivity(intent1);

                break;

        }


    }

    private void loginProgressing() {

        mProgressDialog.setMessage("Login..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

    }

    private void callingForgetPasswordActivityHere() {

        Intent intent = new Intent(ActivitySignIn.this, ActivityForgetPassword.class);
        startActivity(intent);

    }

    private void animationForImageBackArrowHere() {

        final Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        backArrowImage.startAnimation(animTranslate);


    }


    @Override
    public void onValidationSucceeded() {


        savingValuesInHawk();

        gettingValuesFromHawk();

        setupWindowAnimations();

        clearingTheEditTextEnteredValues();

        callingSignInApiHere();


    }

    private void callingSignInApiHere() {


        HttpModule.provideRepositoryService().signInAPI(edEmailPhone.getText().toString(), edPassword.getText().toString()).enqueue(new Callback<SignIn>() {
            @Override
            public void onResponse(@NonNull Call<SignIn> call, @NonNull Response<SignIn> response) {

                mProgressDialog.dismiss();


                if (response.body() != null && response.body().getIsSuccess()) {


                    String userId = String.valueOf(response.body().getPayload().getUserId());
                    Hawk.put("USER_ID", userId);
                    Hawk.put("USER_NAME", response.body().getPayload().getUsername());
                    Hawk.put("EMAIL_ID_SIGNIN", edEmailPhone.getText().toString());


                    if (chKeemMeLoggedIn.isChecked()) {
                        preferences.put(AppConstant.KEEP_ME_LOGGED_IN, true);
                    }

                    showTheDialogMessageForOk(response.body().getMessage());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            callingActivity();
                        }
                    }, 2000);

                } else {
                    showTheDialogMessageForError(Objects.requireNonNull(response.body()).getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<SignIn> call, @NonNull Throwable t) {

                mProgressDialog.dismiss();
                System.out.println("ActivitySignIn.onFailure " + t.toString());

            }
        });


    }

    private void clearingTheEditTextEnteredValues() {

        edEmailPhone.setError(null);
        edPassword.setError(null);

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        mProgressDialog.dismiss();
        for (ValidationError error : errors) {

            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);


            if (view instanceof EditText) {  // Display error messages

                ((EditText) view).setError(message);

            } else {


            }
        }


    }

}
