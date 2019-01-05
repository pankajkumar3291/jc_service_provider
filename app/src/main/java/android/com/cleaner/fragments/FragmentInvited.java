package android.com.cleaner.fragments;

import android.com.cleaner.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import am.appwise.components.ni.NoInternetDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FragmentInvited extends Fragment implements View.OnClickListener {


    private View view;
    private TextView tvReferNow;

    private NoInternetDialog noInternetDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_invited, container, false);

        noInternetDialog = new NoInternetDialog.Builder(getContext()).build();

        findingIdsHere(view);
        eventsListenerHere();

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


    private void eventsListenerHere() {
        tvReferNow.setOnClickListener(this);
    }


    private void findingIdsHere(View view) {
        tvReferNow = view.findViewById(R.id.tvReferNow);
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
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/sto...");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));


    }


}
