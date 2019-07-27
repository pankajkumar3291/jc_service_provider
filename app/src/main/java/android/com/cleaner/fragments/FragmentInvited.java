package android.com.cleaner.fragments;

import android.app.ProgressDialog;
import android.com.cleaner.R;
import android.com.cleaner.apiResponses.refferalCode.RefferalCode;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FragmentInvited extends Fragment implements View.OnClickListener {


    private View view;
    private TextView tvReferNow, tvRefferalCode;

    private NoInternetDialog noInternetDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_invited, container, false);

        noInternetDialog = new NoInternetDialog.Builder(getContext()).build();

        findingIdsHere(view);
        eventsListenerHere();

        loginProgressing();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                referralCodeApi();
            }
        }, 3000);


        return view;
    }

    private void loginProgressing() {

        mProgressDialog.setMessage("Loading..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

    }

    private void referralCodeApi() {
        compositeDisposable.add(HttpModule.provideRepositoryService().refferalCode(String.valueOf(Hawk.get("savedUserId"))).
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<RefferalCode>() {

                    @Override
                    public void accept(RefferalCode refferalCode) throws Exception {


                        if (refferalCode != null && refferalCode.getIsSuccess()) {

                            mProgressDialog.dismiss();
                            tvRefferalCode.setText(refferalCode.getPayload());

                        } else {

                            mProgressDialog.dismiss();
                            TastyToast.makeText(getActivity(), Objects.requireNonNull(refferalCode).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                        }

                    }

                }, new Consumer<Throwable>() {

                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        mProgressDialog.dismiss();
                        TastyToast.makeText(getActivity(), throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                    }

                }));

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


    private void eventsListenerHere() {
        tvReferNow.setOnClickListener(this);
    }


    private void findingIdsHere(View view) {

        tvReferNow = view.findViewById(R.id.tvReferNow);
        tvRefferalCode = view.findViewById(R.id.tvRefferalCode);


        mProgressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tvReferNow:

                shareWithSocial();


                break;

        }

    }

    private void shareWithSocial() {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "The app");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.whatsapp&hl=en"); //https://play.google.com/sto...
        startActivity(Intent.createChooser(sharingIntent, "Share via"));


    }


}
