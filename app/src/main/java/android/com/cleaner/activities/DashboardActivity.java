package android.com.cleaner.activities;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.com.cleaner.R;
import android.com.cleaner.apiResponses.customerFullDetailsApis.CustomerFullDetails;
import android.com.cleaner.apiResponses.updateProfile.GetProfile;
import android.com.cleaner.fragments.BookCleanerFragment;
import android.com.cleaner.fragments.FragmentAddress;
import android.com.cleaner.fragments.FragmentCancelAppointments;
import android.com.cleaner.fragments.FragmentCompletedAppointment;
import android.com.cleaner.fragments.FragmentContactSupport;
import android.com.cleaner.fragments.FragmentInvited;
import android.com.cleaner.fragments.FragmentUpcomingAppointments;
import android.com.cleaner.fragments.HomeFragment;
import android.com.cleaner.fragments.ScheduleFragment;
import android.com.cleaner.fragments.SettingsFragment;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.com.cleaner.sharedPrefrence.SharedPrefsHelper;
import android.com.cleaner.utils.AppConstant;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.util.Locale;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    // navigation links   https://android-arsenal.com/tag/90
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private NavigationView navigationView;
    private FrameLayout frameLayout;
    private TextView logout, tvNo, tvYes;
    private CircleImageView cleanearsProfile;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private TextView name, email;
    private NoInternetDialog noInternetDialog;
    private SharedPrefsHelper sharedPrefsHelper;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        sharedPrefsHelper = new SharedPrefsHelper(DashboardActivity.this);
        getWindow().setStatusBarColor(ContextCompat.getColor(DashboardActivity.this, R.color.statusBarColor));
        fragmentManager = getSupportFragmentManager();
        setupView();
        if (Hawk.get("spanish", false))
            makingAppLanguagesLocalizeForSpanish();
        else
            makingAppLanguagesLocalizeForEnglish();
        customerFullDetailsApi();
        if (savedInstanceState == null) showHome();
    }
    private void makingAppLanguagesLocalizeForEnglish(){
        Locale locale = new Locale("en");
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
    private void makingAppLanguagesLocalizeForSpanish(){
        Locale locale = new Locale("es");
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
    private void customerFullDetailsApi() {
        compositeDisposable.add(HttpModule.provideRepositoryService().customerFullDetailsApi(String.valueOf(Hawk.get("savedUserId"))).
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<CustomerFullDetails>() {
                    @Override
                    public void accept(CustomerFullDetails customerFullDetails) throws Exception {
                        if (customerFullDetails != null && customerFullDetails.getIsSuccess()) {
                            System.out.println("DashboardActivity.accept" + customerFullDetails.getMessage());
                            name.setText(customerFullDetails.getPayLoad().getUserDetails().getFirstName());
                            email.setText(customerFullDetails.getPayLoad().getUserDetails().getEmail());
                            Picasso.get().load(customerFullDetails.getPayLoad().getUserDetails().getImage()).resize(100, 100).error(R.drawable.ic_user).into(cleanearsProfile);
                            Hawk.put("cEmail", customerFullDetails.getPayLoad().getUserDetails().getEmail());
                            Hawk.put("cName", customerFullDetails.getPayLoad().getUserDetails().getFirstName());
                            Hawk.put("cImg", customerFullDetails.getPayLoad().getUserDetails().getImage());
                        } else {
                            System.out.println("DashboardActivity.accept " + Objects.requireNonNull(customerFullDetails).getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("DashboardActivity.accept " + throwable.toString());
                    }
                }));
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
    private void showHome() {
        selectDrawerItem(navigationView.getMenu().getItem(0));
//        drawerLayout.openDrawer(GravityCompat.START); // its bydefault opening the drawer
    }
    private void selectDrawerItem(MenuItem menuItem) {
        boolean specialToolbarBehaviour = false;
        Class fragmentClass;
        String tag = "";
        switch (menuItem.getItemId()) {
            case R.id.drawer_home:
                tag = "dashboard";
                fragmentClass = BookCleanerFragment.class;
                break;
            case R.id.invite_friend:
                tag = "invite";
                fragmentClass = FragmentInvited.class;
                break;
            case R.id.address:
                tag = "address";
                fragmentClass = FragmentAddress.class;
                break;
            case R.id.upcoming_appointments:
                fragmentClass = FragmentUpcomingAppointments.class;
                break;
            case R.id.contact_support:
                tag = "contact";
                fragmentClass = FragmentContactSupport.class;
                break;
            case R.id.completed_appointments:
                tag = "completedAppointments";
                fragmentClass = FragmentCompletedAppointment.class;
                break;
            case R.id.cancel_appointments:
                tag = "canceAppointments";
                fragmentClass = FragmentCancelAppointments.class;
                break;
            default:
                tag = "dashboard";
                fragmentClass = BookCleanerFragment.class;
                break;
        }
        try {
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, tag).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        setToolbarElevation(specialToolbarBehaviour);
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setToolbarElevation(boolean specialToolbarBehaviour) {
        if (specialToolbarBehaviour) {
            toolbar.setElevation(0.0f);
            frameLayout.setElevation(getResources().getDimension(R.dimen.elevation_toolbar));
        } else {
            toolbar.setElevation(getResources().getDimension(R.dimen.elevation_toolbar));
            frameLayout.setElevation(0.0f);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        drawerToggle.syncState();
        super.onPostCreate(savedInstanceState);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
    private void setupView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        frameLayout = findViewById(R.id.content_frame);
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
//        drawerToggle.setDrawerIndicatorEnabled(false);
//        drawerToggle.setHomeAsUpIndicator(R.drawable.toolbar_icon);
        drawerLayout.addDrawerListener(drawerToggle);
        navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        name = headerView.findViewById(R.id.name);
        email = headerView.findViewById(R.id.email);
        cleanearsProfile = headerView.findViewById(R.id.cleanearsProfile);
        String updatedImage = Hawk.get("TEMP");
        Bitmap bitmap = null;
//                    if (bitmap == null) {
//
//                        Drawable myDrawable = getResources().getDrawable(R.drawable.cleaner_default);
//                        cleanearsProfile.setImageDrawable(myDrawable);
//                    } else {
        try {
            byte[] encodeByte = Base64.decode(updatedImage, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
        }
        cleanearsProfile.setImageBitmap(bitmap);
//                    }
        if (drawerLayout != null && drawerLayout instanceof DrawerLayout) {
            drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(@NonNull View view, float v) {
                }
                @Override
                public void onDrawerOpened(@NonNull View view) {
                    name.setText(Hawk.get("cName").toString());
                    email.setText(Hawk.get("cEmail").toString());
                    Picasso.get().load(Hawk.get("cImg").toString()).resize(100, 100).error(R.drawable.ic_user).into(cleanearsProfile);
                }
                @Override
                public void onDrawerClosed(@NonNull View view) {
                }
                @Override
                public void onDrawerStateChanged(int i) {
//                    String updatedImage = Hawk.get("TEMP");
//                    Bitmap bitmap = null;
////                    if (bitmap == null) {
////
////                        Drawable myDrawable = getResources().getDrawable(R.drawable.cleaner_default);
////                        cleanearsProfile.setImageDrawable(myDrawable);
////                    } else {
//
//                    try {
//                        byte[] encodeByte = Base64.decode(updatedImage, Base64.DEFAULT);
//                        bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//                    } catch (Exception e) {
//                        e.getMessage();
//                    }
//                    cleanearsProfile.setImageBitmap(bitmap);
////                    }
                }
            });
        }
//        //obtain  Intent Object send  from SenderActivity
//        Intent intent = this.getIntent();
//
//        /* Obtain String from Intent  */
//        if (intent != null) {
//            String strdata = intent.getExtras().getString("Uniqid");
//
//            if (strdata.equals("ActivitySignUp")) {
//                name.setText((CharSequence) Hawk.get("FIRST_NAME"));
//                email.setText((CharSequence) Hawk.get("EMAIL_ID"));
//            }
//            if (strdata.equals("ActivitySignIn")) {
//                name.setText((CharSequence) Hawk.get("USER_NAME"));
//                email.setText((CharSequence) Hawk.get("EMAIL_ID_SIGNIN"));
//            }
//
//        } else {
//            TastyToast.makeText(getApplicationContext(), "Nothing Matched", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
//        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                logoutAlertDialog();
                break;
        }
    }
    private void logoutAlertDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.dialog_logout, null);
        findingLogoutDialodIdsHere(dialogView);
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    private void findingLogoutDialodIdsHere(View dialogView) {
        tvNo = dialogView.findViewById(R.id.tvNo);
        tvYes = dialogView.findViewById(R.id.tvYes);
        takingClickOfLogout();
    }
    private void takingClickOfLogout() {
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefsHelper.deleteSavedData(AppConstant.KEEP_ME_LOGGED_IN);
                Hawk.delete("keepMeLoggedIn");
                Hawk.delete("FIRST_NAME");
                Hawk.delete("EMAIL_ID");
                Hawk.delete("USER_NAME");
                Hawk.delete("EMAIL_ID_SIGNIN");
                Intent intent = new Intent(DashboardActivity.this, ActivityMain.class);
                startActivity(intent);
                finish();
            }
        });
    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (doubleBackToExitPressedOnce) {
            System.exit(0);
            super.onBackPressed();
            return;
        }
        if (checkfragment()) {
            this.doubleBackToExitPressedOnce = true;
            Toast toast = Toast.makeText(DashboardActivity.this, "Please click BACK again to exit", Toast.LENGTH_SHORT);
            View view = toast.getView();
            view.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
            TextView text = view.findViewById(android.R.id.message);
            text.setTextColor(getResources().getColor(R.color.colorWhite));
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new BookCleanerFragment(), "dashboard").commit();
        }
    }
    private boolean checkfragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("dashboard");
        if (fragment != null && fragment.isVisible()) {
            return true;
        } else {
            return false;
        }
    }
}
