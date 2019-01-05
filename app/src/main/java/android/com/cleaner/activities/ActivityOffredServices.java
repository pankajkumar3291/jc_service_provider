package android.com.cleaner.activities;

import android.app.ProgressDialog;
import android.com.cleaner.R;
import android.com.cleaner.adapters.OffredServicesAdapter;
import android.com.cleaner.apiResponses.offerredServices.OfferredServices;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.com.cleaner.models.OfferedServices;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityOffredServices extends AppCompatActivity {


    private ImageView backarr;
    private RecyclerView recyclerViewOffredServices;

//    List<OfferedServices> jobsList;


    private NoInternetDialog noInternetDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offred_services);
        noInternetDialog = new NoInternetDialog.Builder(this).build();


        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityOffredServices.this, R.color.statusBarColor));


        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        mProgressDialog.setMessage("Waiting..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();


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


        compositeDisposable.add(HttpModule.provideRepositoryService().getOffrredServicesAPI().
                subscribeOn(io.reactivex.schedulers.Schedulers.computation()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<OfferredServices>() {

                    @Override
                    public void accept(OfferredServices offerredServices) throws Exception {


                        if (offerredServices != null && offerredServices.getIsSuccess()) {

                            mProgressDialog.dismiss();

                            TastyToast.makeText(ActivityOffredServices.this, offerredServices.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                            OffredServicesAdapter offredServicesAdapter = new OffredServicesAdapter(ActivityOffredServices.this, offerredServices.getPayload());
                            recyclerViewOffredServices.setHasFixedSize(true);
                            recyclerViewOffredServices.setLayoutManager(new LinearLayoutManager(ActivityOffredServices.this));
                            recyclerViewOffredServices.setAdapter(offredServicesAdapter);

                        } else {
                            TastyToast.makeText(ActivityOffredServices.this, offerredServices.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }


                    }

                }, new Consumer<Throwable>() {

                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        mProgressDialog.dismiss();
                        TastyToast.makeText(ActivityOffredServices.this, throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                    }

                }));
    }


    private void findingIdsHere() {

        backarr = findViewById(R.id.backarr);
        recyclerViewOffredServices = findViewById(R.id.recyclerViewOffredServices);

        backarr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }


}
