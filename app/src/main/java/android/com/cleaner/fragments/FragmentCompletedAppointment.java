package android.com.cleaner.fragments;
import android.com.cleaner.R;
import android.com.cleaner.adapters.CompletedAppointmentsAdapter;
import android.com.cleaner.apiResponses.completedJobs.CustomerCompletedJobs;
import android.com.cleaner.httpRetrofit.HttpModule;
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
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
public class FragmentCompletedAppointment extends Fragment {
    private View view;
    private RecyclerView recyclerCmpletedAppointments;
    private TextView tvNoData;
    private NoInternetDialog noInternetDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
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
        try {
            compositeDisposable.add(HttpModule.provideRepositoryService().customerCompletedJobsApi(Hawk.get("spanish", false) ? "es" : "en", String.valueOf(Hawk.get("savedUserId"))).
                    subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribe(new Consumer<CustomerCompletedJobs>() {
                        @Override
                        public void accept(CustomerCompletedJobs customerCompletedJobs) throws Exception {
                            if (customerCompletedJobs != null && customerCompletedJobs.getIsSuccess()) {
                                CompletedAppointmentsAdapter appointmentsAdapter = new CompletedAppointmentsAdapter(getActivity(), customerCompletedJobs.getPayload());
                                recyclerCmpletedAppointments.setHasFixedSize(true);
                                recyclerCmpletedAppointments.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerCmpletedAppointments.setAdapter(appointmentsAdapter);
                                recyclerCmpletedAppointments.setVisibility(View.VISIBLE);
                                tvNoData.setVisibility(View.GONE);
                            } else {
                                recyclerCmpletedAppointments.setVisibility(View.GONE);
                                tvNoData.setVisibility(View.VISIBLE);
                                TastyToast.makeText(getActivity(), Objects.requireNonNull(customerCompletedJobs).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            recyclerCmpletedAppointments.setVisibility(View.GONE);
                            tvNoData.setVisibility(View.VISIBLE);
                        }
                    }));
        } catch (Exception e) {
            e.printStackTrace();
            recyclerCmpletedAppointments.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }
    }
    private void findingIdsHere(View view) {
        tvNoData = view.findViewById(R.id.no_data);
        recyclerCmpletedAppointments = view.findViewById(R.id.recyclerCmpletedAppointments);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(CalligraphyContextWrapper.wrap(context));
    }
}
