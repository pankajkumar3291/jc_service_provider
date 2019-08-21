package android.com.cleaner.fragments;

import android.com.cleaner.R;
import android.com.cleaner.adapters.UpcomingAppointmentsAdapter;
import android.com.cleaner.apiResponses.customerCurrentJobs.CustomerCurrentJobs;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FragmentUpcomingAppointments extends Fragment {


    private View view;
    private UpcomingAppointmentsAdapter adapter;
    private RecyclerView recyclerView;
    private NoInternetDialog noInternetDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TextView tvNoData;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upcoming_appointments, container, false);

        noInternetDialog = new NoInternetDialog.Builder(getContext()).build();
        findingIdsHere(view);

        callTheAdapterFromHere();

//        setupActionBar();
//        setupList();
        return view;
    }

    private void callTheAdapterFromHere() {
        compositeDisposable.add(HttpModule.provideRepositoryService().customerCurrentJobsApi(Hawk.get("spanish",false)?"es":"en",String.valueOf(Hawk.get("savedUserId"))).
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<CustomerCurrentJobs>() {

                    @Override
                    public void accept(CustomerCurrentJobs customerCurrentJobs) throws Exception {

                        if (customerCurrentJobs != null && customerCurrentJobs.getIsSuccess()) {

                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            adapter = new UpcomingAppointmentsAdapter(getActivity(), customerCurrentJobs.getPayload());
                            recyclerView.setAdapter(adapter);
                            recyclerView.setVisibility(View.VISIBLE);
                            tvNoData.setVisibility(View.GONE);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            tvNoData.setVisibility(View.VISIBLE);
                            TastyToast.makeText(getActivity(), customerCurrentJobs.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }

                    }


                }, new Consumer<Throwable>() {

                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        recyclerView.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
                        System.out.println("FragmentUpcomingAppointments.accept " + throwable.toString());

                    }
                }));


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }


    private void setupActionBar() {

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }
    }


    private void findingIdsHere(View view) {
tvNoData=view.findViewById(R.id.no_data);

        recyclerView = view.findViewById(R.id.recyclerView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(CalligraphyContextWrapper.wrap(context));

    }
}
