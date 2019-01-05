package android.com.cleaner.fragments;

import android.com.cleaner.R;
import android.com.cleaner.activities.ActivityCompanyInfo;
import android.com.cleaner.adapters.CompanyInfoAdapter;
import android.com.cleaner.adapters.CompletedAppointmentsAdapter;
import android.com.cleaner.models.CompletedAppointments;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FragmentCompletedAppointment extends Fragment {


    private View view;
    private RecyclerView recyclerCmpletedAppointments;

    List<CompletedAppointments> appointmentsList;

    private NoInternetDialog noInternetDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_completed_appointments, container, false);

        noInternetDialog = new NoInternetDialog.Builder(getContext()).build();

        findingIdsHere(view);
        settingUpTheAdapterHere(view);

        return view;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    private void settingUpTheAdapterHere(View view) {

        appointmentsList = new ArrayList<>();

        appointmentsList.add(new CompletedAppointments("Shahzeb", "A", "100", "13-Nov-2018"));
        appointmentsList.add(new CompletedAppointments("Sakshi", "B", "200", "20-Nov-2018"));
        appointmentsList.add(new CompletedAppointments("Manpreet", "C", "300", "29-Nov-2018"));
        appointmentsList.add(new CompletedAppointments("Sameer", "D", "10", "13-Dec-2018"));
        appointmentsList.add(new CompletedAppointments("Anupriya", "E", "15", "20-Dec-2018"));
        appointmentsList.add(new CompletedAppointments("Ajay", "F", "25", "30-Dec-2018"));
        appointmentsList.add(new CompletedAppointments("Gurdeep", "G", "0", "31-Dec-2018"));


        CompletedAppointmentsAdapter appointmentsAdapter = new CompletedAppointmentsAdapter(getActivity(), appointmentsList);
        recyclerCmpletedAppointments.setHasFixedSize(true);
        recyclerCmpletedAppointments.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerCmpletedAppointments.setAdapter(appointmentsAdapter);


    }

    private void findingIdsHere(View view) {


        recyclerCmpletedAppointments = view.findViewById(R.id.recyclerCmpletedAppointments);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(CalligraphyContextWrapper.wrap(context));
    }


}
