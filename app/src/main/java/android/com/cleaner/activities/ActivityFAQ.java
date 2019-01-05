package android.com.cleaner.activities;

import android.com.cleaner.R;
import android.com.cleaner.adapters.FaqAdapter;
import android.com.cleaner.apiResponses.getFAQ.FAQ;
import android.com.cleaner.httpRetrofit.HttpModule;
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

import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityFAQ extends AppCompatActivity {


    private RecyclerView recyclerViewList;

    private ImageView backarr;
    private NoInternetDialog noInternetDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        noInternetDialog = new NoInternetDialog.Builder(this).build();

        changingStatusBarColorHere();

        backarr = findViewById(R.id.backarr);
        recyclerViewList = findViewById(R.id.recyclerViewList);

        callingTheFAQApisHere();


        backarr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void callingTheFAQApisHere() {


        compositeDisposable.add(HttpModule.provideRepositoryService().getFAQAPI().
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<FAQ>() {


                    @Override
                    public void accept(FAQ faq) throws Exception {

                        if (faq != null && faq.getIsSuccess()) {


                            TastyToast.makeText(ActivityFAQ.this, "You can now able to see your Questins and Answers", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                            FaqAdapter expandableListAdapter = new FaqAdapter(ActivityFAQ.this, faq.getPayload());
                            recyclerViewList.setHasFixedSize(true);
                            recyclerViewList.setLayoutManager(new LinearLayoutManager(ActivityFAQ.this));
                            recyclerViewList.setAdapter(expandableListAdapter);

                        } else {

                            TastyToast.makeText(ActivityFAQ.this, "Something went wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }


                    }


                }, new Consumer<Throwable>() {


                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        TastyToast.makeText(ActivityFAQ.this, throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                    }


                }));


    }

    private void changingStatusBarColorHere() {
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityFAQ.this, R.color.statusBarColor));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }


}
