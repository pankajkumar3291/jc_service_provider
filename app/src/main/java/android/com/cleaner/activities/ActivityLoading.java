package android.com.cleaner.activities;

import android.com.cleaner.R;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityLoading extends AppCompatActivity {


    private ImageView imageClock, backarr;
    private TextView tvSearchingToFindCleaner, tvSendThierProfile, tvThanku;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        changingStatusBarColorHere();
        findingIdsHere();


    }

    private void findingIdsHere() {

        imageClock = findViewById(R.id.imageClock);

        tvSearchingToFindCleaner = findViewById(R.id.tvSearchingToFindCleaner);
        tvSendThierProfile = findViewById(R.id.tvSendThierProfile);
        tvThanku = findViewById(R.id.tvThanku);

        backarr = findViewById(R.id.backarr);
        backarr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

    }

    private void changingStatusBarColorHere() {
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityLoading.this, R.color.statusBarColor));
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }
}
