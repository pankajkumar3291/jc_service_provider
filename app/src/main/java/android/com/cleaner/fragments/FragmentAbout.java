package android.com.cleaner.fragments;

import android.com.cleaner.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import am.appwise.components.ni.NoInternetDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FragmentAbout extends Fragment {


    private View view;
    private TextView tvStartTime, tvLastTime, tvBioScrollable;

    private NoInternetDialog noInternetDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_about, container, false);

        noInternetDialog = new NoInternetDialog.Builder(getContext()).build();

        findingAboutIdsHere(view);
        settingValuesHere(view);

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

    private void settingValuesHere(View view) {


        tvBioScrollable.setMovementMethod(new ScrollingMovementMethod());

    }

    private void findingAboutIdsHere(View view) {

        tvStartTime = view.findViewById(R.id.tvStartTime);
        tvLastTime = view.findViewById(R.id.tvLastTime);
        tvBioScrollable = view.findViewById(R.id.tvBioScrollable);


    }


}
