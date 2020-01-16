package android.com.cleaner.activities;

import android.app.ProgressDialog;
import android.com.cleaner.R;
import android.com.cleaner.apiResponses.forgetPasswordResponse.ForgetPassword;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityResetPassword extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty(message = "Please enter the Email")
    public EditText edEmailID;

    @NotEmpty
    @Password
    public EditText edPassword;

    @NotEmpty
    @ConfirmPassword
    public EditText edPasswordConfirm;

    private TextView tvSubmit;

    Validator validator;
    ProgressDialog mProgressDialog;

    private NoInternetDialog noInternetDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityResetPassword.this, R.color.statusBarColor));
        findingIdsHere();
        initializingValidationHere();
        findViewById(R.id.backarr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    private void initializingValidationHere() {


        validator = new Validator(this);
        validator.setValidationListener(this);
    }


    private void findingIdsHere() {

        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        edEmailID = findViewById(R.id.edEmailID);
        edPassword = findViewById(R.id.edPassword);
        edPasswordConfirm = findViewById(R.id.edPasswordConfirm);

        tvSubmit = findViewById(R.id.tvSubmit);


        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginProgressing();
                validator.validate();

            }
        });


    }

    private void loginProgressing() {

        mProgressDialog.setMessage("Waiting..");
//        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();


    }

    private void callingResetPasswordApiFromHere() {


        HttpModule.provideRepositoryService().resetPasswordAPI(edEmailID.getText().toString(), edPassword.getText().toString()).enqueue(new Callback<ForgetPassword>() {
            @Override
            public void onResponse(Call<ForgetPassword> call, Response<ForgetPassword> response) {
                mProgressDialog.dismiss();

                if (response.body() != null) {


                    if (response.body() != null && response.body().getIsSuccess()) {

                        TastyToast.makeText(ActivityResetPassword.this, response.body().getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                        finish();


                        callSignInActivityFromHereToLoginAgain();


                    } else {
                        TastyToast.makeText(ActivityResetPassword.this, response.body().getMessage(), TastyToast.LENGTH_SHORT, TastyToast.WARNING).show();

                    }

                } else {
                    TastyToast.makeText(ActivityResetPassword.this, "Null Body", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                }
            }

            @Override
            public void onFailure(Call<ForgetPassword> call, Throwable t) {

                mProgressDialog.dismiss();
            }
        });
    }

    private void callSignInActivityFromHereToLoginAgain() {

        Intent intent = new Intent(ActivityResetPassword.this, ActivitySignIn.class);
        startActivity(intent);

    }
    @Override
    public void onValidationSucceeded() {
        callingResetPasswordApiFromHere();
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
