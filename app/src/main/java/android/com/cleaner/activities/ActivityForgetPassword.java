package android.com.cleaner.activities;

import android.app.ProgressDialog;
import android.com.cleaner.R;
import android.com.cleaner.apiResponses.forgetPasswordResponse.ForgetPassword;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityForgetPassword extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {


    @NotEmpty
    @Email(message = "Please enter the valid email")
    private EditText edForgetPassword;

    private TextView btnSend;
    private ImageView backArrowImage;
    Validator validator;
    ProgressDialog mProgressDialog;
    private NoInternetDialog noInternetDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        noInternetDialog = new NoInternetDialog.Builder(this).build();

        findingIdsHere();
        eventsListener();
        changingStatusBarColorHere();

        validator = new Validator(this);
        validator.setValidationListener(this);

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


    private void eventsListener() {
        btnSend.setOnClickListener(this);
        backArrowImage.setOnClickListener(this);

    }


    @Override
    protected void onResume() {
        super.onResume();

        performOperationsHere();
    }

    private void performOperationsHere() {


    }

    private void findingIdsHere() {

        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);


        edForgetPassword = findViewById(R.id.edForgetPassword);
        btnSend = findViewById(R.id.btnSend);
        backArrowImage = findViewById(R.id.backArrowImage);


    }

    private void changingStatusBarColorHere() {
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityForgetPassword.this, R.color.statusBarColor));

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.btnSend:
                loginProgressing();
                validator.validate();

                break;

            case R.id.backArrowImage:
                finish();
                break;

        }

    }

    private void loginProgressing() {

        mProgressDialog.setMessage("Waiting..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

    }

    @Override
    public void onValidationSucceeded() {

        clearingTheEdittextHere();

        gettingValuesFromHawk();


        callingForgetPasswordApiHere();


    }

    private void callingForgetPasswordApiHere() {


        HttpModule.provideRepositoryService().forgetPasswordAPI(edForgetPassword.getText().toString()).enqueue(new Callback<ForgetPassword>() {

            @Override
            public void onResponse(Call<ForgetPassword> call, Response<ForgetPassword> response) {
                mProgressDialog.dismiss();

                if (response.body() != null) {

                    if (response.body() != null && response.body().getIsSuccess()) {

                        savingValuesInHawk();
                        TastyToast.makeText(ActivityForgetPassword.this, response.body().getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                        finish();

                        callOtpActivityFromHere();


                    } else {
                        TastyToast.makeText(ActivityForgetPassword.this, response.body().getMessage(), TastyToast.LENGTH_SHORT, TastyToast.WARNING).show();
                    }

                } else {
                    TastyToast.makeText(ActivityForgetPassword.this, "Null", TastyToast.LENGTH_SHORT, TastyToast.WARNING).show();
                }

            }

            @Override
            public void onFailure(Call<ForgetPassword> call, Throwable t) {

                mProgressDialog.dismiss();
            }

        });


    }

    private void callOtpActivityFromHere() {

        Intent intent = new Intent(this, ActivityOTP.class);
        startActivity(intent);


    }

    private void clearingTheEdittextHere() {

        edForgetPassword.setError(null);

    }

    private void gettingValuesFromHawk() {
        String edPassword = Hawk.get("ED_FORGET_PASSWORD");
    }

    private void savingValuesInHawk() {
        Hawk.put("ED_FORGET_PASSWORD", edForgetPassword.getText().toString());
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
//                Toast.makeText(ActivityForgetPassword.this, message, Toast.LENGTH_LONG).show();
            }
        }

    }
}
