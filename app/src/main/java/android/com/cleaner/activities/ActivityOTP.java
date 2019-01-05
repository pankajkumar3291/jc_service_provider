package android.com.cleaner.activities;

import android.app.ProgressDialog;
import android.com.cleaner.R;
import android.com.cleaner.apiResponses.forgetPasswordResponse.ForgetPassword;
import android.com.cleaner.apiResponses.matchOtp.MatchOTP;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityOTP extends AppCompatActivity implements View.OnClickListener {


    private EditText ed1, ed2, ed3, ed4;
    private TextView tvSend, tvResendOtp;

    private ImageView backarr;

    ProgressDialog mProgressDialog;
    private NoInternetDialog noInternetDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        noInternetDialog = new NoInternetDialog.Builder(this).build();

        Hawk.init(this).build();
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityOTP.this, R.color.statusBarColor));

        findingIdsHere();
        eventListenerHere();
        changingFocusOfEdittextFromHere();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    private void changingFocusOfEdittextFromHere() {


        ed1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {


                if (s.length() == 1) {
                    ed2.requestFocus();

                } else {
                    ed1.requestFocus();
                }
            }

        });


        ed2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    ed3.requestFocus();

                } else {
                    ed1.requestFocus();
                }

            }
        });


        ed3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    ed4.requestFocus();

                } else {
                    ed2.requestFocus();
                }

            }
        });


        ed4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    ed4.requestFocus();

                } else {
                    ed3.requestFocus();
                }


            }
        });


    }

    private void eventListenerHere() {

        tvSend.setOnClickListener(this);
        tvResendOtp.setOnClickListener(this);

    }

    private void findingIdsHere() {

        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        ed1 = findViewById(R.id.ed1);
        ed2 = findViewById(R.id.ed2);
        ed3 = findViewById(R.id.ed3);
        ed4 = findViewById(R.id.ed4);


        tvSend = findViewById(R.id.tvSend);
        tvResendOtp = findViewById(R.id.tvResendOtp);

        backarr = findViewById(R.id.backarr);

        tvResendOtp.setPaintFlags(tvResendOtp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {


            case R.id.tvSend:

                callingMatchOtpApiHere();


                break;


            case R.id.tvResendOtp:

                loginProgressing();
                resendOtpCode();


                break;


            case R.id.backarr:

                finish();
                break;


        }

    }

    private void loginProgressing() {

        mProgressDialog.setMessage("Waiting..");
//        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

    }

    private void resendOtpCode() {

        HttpModule.provideRepositoryService().forgetPasswordAPI(String.valueOf(Hawk.get("ED_FORGET_PASSWORD"))).enqueue(new Callback<ForgetPassword>() {
            @Override
            public void onResponse(Call<ForgetPassword> call, Response<ForgetPassword> response) {
                mProgressDialog.dismiss();

                if (response.body() != null) {


                    if (response.body() != null && response.body().getIsSuccess()) {


                        TastyToast.makeText(ActivityOTP.this, "Sending you the code again", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                    } else {
                        TastyToast.makeText(ActivityOTP.this, response.body().getMessage(), TastyToast.LENGTH_SHORT, TastyToast.WARNING).show();
                    }


                } else {

                    TastyToast.makeText(ActivityOTP.this, "Null Body", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();


                }


            }

            @Override
            public void onFailure(Call<ForgetPassword> call, Throwable t) {


                mProgressDialog.dismiss();

            }
        });


    }

    private void callingMatchOtpApiHere() {


        HttpModule.provideRepositoryService().matchOtpAPI(String.valueOf(Hawk.get("savedUserId")), ed1.getText().toString() + ed2.getText().toString() + ed3.getText().toString() + ed4.getText().toString()).enqueue(new Callback<MatchOTP>() {
            @Override
            public void onResponse(Call<MatchOTP> call, Response<MatchOTP> response) {


                if (response.body() != null) {

                    if (response.body() != null && response.body().getIsSuccess()) {

                        TastyToast.makeText(ActivityOTP.this, response.body().getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                        finish();


                        callResetPasswordActivityFromHere();


                    } else {

                        TastyToast.makeText(ActivityOTP.this, response.body().getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                    }

                } else {

                    TastyToast.makeText(ActivityOTP.this, "Null Body", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                }


            }

            @Override
            public void onFailure(Call<MatchOTP> call, Throwable t) {


            }
        });


    }

    private void callResetPasswordActivityFromHere() {

        Intent intent = new Intent(ActivityOTP.this, ActivityResetPassword.class);
        startActivity(intent);


    }
}
