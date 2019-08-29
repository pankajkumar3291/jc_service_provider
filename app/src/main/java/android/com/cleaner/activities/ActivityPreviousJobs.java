package android.com.cleaner.activities;
import android.com.cleaner.R;
import android.com.cleaner.adapters.PreviousJobsAdapter;
import android.com.cleaner.apiResponses.allPreviousJobs.AllPreviousJobs;
import android.com.cleaner.apiResponses.completedJobs.CustomerCompletedJobs;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.com.cleaner.interfaces.ItemClickListenerTwo;
import android.com.cleaner.models.PreviousJobs;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
public class ActivityPreviousJobs extends AppCompatActivity implements ItemClickListenerTwo {
    private RecyclerView recyclerViewPreviousJobs;
    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TextView tvNoData;
    List<PreviousJobs> jobsList;
    RelativeLayout reMainRelative;
    private ImageView backarr;
    private NoInternetDialog noInternetDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_jobs);
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityPreviousJobs.this, R.color.statusBarColor));
        context = this;
        findingIdsHere();
        callingAdapterHere();
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
    private void callingAdapterHere() {
//        jobsList = new ArrayList<>();
//
//        jobsList.add(new PreviousJobs("Hell cleaning center"));
//        jobsList.add(new PreviousJobs("Shahzeb "));
//        jobsList.add(new PreviousJobs("SmartIT "));
//        jobsList.add(new PreviousJobs("Ropar Punjab"));
//        jobsList.add(new PreviousJobs("Charanveer singh "));
//        jobsList.add(new PreviousJobs("Surinder Singh"));
//        jobsList.add(new PreviousJobs("Cleaner call"));
//        jobsList.add(new PreviousJobs("Enjoy here"));
//
//
//        PreviousJobsAdapter previousJobsAdapter = new PreviousJobsAdapter(context, jobsList);
//        recyclerViewPreviousJobs.setHasFixedSize(true);
//        recyclerViewPreviousJobs.setLayoutManager(new LinearLayoutManager(this));
//        recyclerViewPreviousJobs.setAdapter(previousJobsAdapter);
//        previousJobsAdapter.setClickListener(this);
        compositeDisposable.add(HttpModule.provideRepositoryService().
                allPreviousJobs(Hawk.get("spanish", false) ? "es" : "en", String.valueOf(Hawk.get("savedUserId"))).
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<AllPreviousJobs>() {
                    @Override
                    public void accept(AllPreviousJobs allPreviousJobs) throws Exception {
                        if (allPreviousJobs != null && allPreviousJobs.getIsSuccess()) {
                            PreviousJobsAdapter previousJobsAdapter = new PreviousJobsAdapter(context, allPreviousJobs.getPayload());

                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            mLayoutManager.setReverseLayout(true);
                            recyclerViewPreviousJobs.setHasFixedSize(true);
                            recyclerViewPreviousJobs.setLayoutManager(mLayoutManager);
                            recyclerViewPreviousJobs.setAdapter(previousJobsAdapter);
                            previousJobsAdapter.setClickListener(ActivityPreviousJobs.this);
                        } else {
                            tvNoData.setVisibility(View.VISIBLE);
                            Toast.makeText(ActivityPreviousJobs.this, Objects.requireNonNull(allPreviousJobs).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        tvNoData.setVisibility(View.VISIBLE);
                        Toast.makeText(ActivityPreviousJobs.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
    }
    private void findingIdsHere() {
        recyclerViewPreviousJobs = findViewById(R.id.recyclerViewPreviousJobs);
        reMainRelative = findViewById(R.id.reMainRelative);
        tvNoData = findViewById(R.id.tvNoData);
        backarr = findViewById(R.id.backarr);
        backarr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onClick(View view, int position) {
//        TastyToast.makeText(getApplicationContext(), "Clicking testing ", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
        if (position == 0) {
        } else if (position == 1) {
        } else if (position == 2) {
        }
    }
}
