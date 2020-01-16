package android.com.cleaner.httpRetrofit;

import android.com.cleaner.BuildConfig;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class HttpModule {
    private static OkHttpClient getOkkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
    }


    //http://103.66.73.21/cleaningService/JC2019clientRepo/public/api/customer/
    public static Retrofit getRetroFitClient(){
        return new Retrofit.Builder()
                .baseUrl("http://103.66.73.21/cleaningService/JC2019clientRepo/public/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .client(getOkkHttpClient())
                .build();
        // MARK: LIVE URL
        //http://192.168.0.3/CleanerUp/cleaning_service/public/api/
        // http://smartit.ventures/CS/cleaning_service/public/api/
        // MARK: TESTING URL
//       http://192.168.0.8/CleanerUp/cleaning_service/public/api/customer/
        //192.168.0.79/jc2019/cleaning_service/public/jc2019/cleaning_service/public/api
    }
    public static RemoteRepositoryService provideRepositoryService() {
        return getRetroFitClient().create(RemoteRepositoryService.class);
    }
}

