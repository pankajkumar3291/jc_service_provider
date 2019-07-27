package android.com.cleaner.applicationUtils;

import android.app.Application;
import android.com.cleaner.R;
import android.com.cleaner.rxBus.RxBus;

import com.orhanobut.hawk.Hawk;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends Application {


    private RxBus rxBus;

    public RxBus getRxBus() {

        return rxBus;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Hawk.init(this).build();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/headingbrew.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        rxBus = new RxBus();

    }
}
