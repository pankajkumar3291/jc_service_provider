package android.com.cleaner.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.com.cleaner.R;
import android.com.cleaner.activities.ActivityDailyJobSchedule;
import android.com.cleaner.activities.ActivitySelectLanguages;
import android.com.cleaner.activities.ActivitySplash;
import android.com.cleaner.activities.DashboardActivity;
import android.com.cleaner.apiResponses.completedJobs.Payload;
import android.com.cleaner.apiResponses.reviewsAddedByProvider.ReviewsAdded;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.com.cleaner.utils.AppConstant;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class CompletedAppointmentsAdapter extends RecyclerView.Adapter<CompletedAppointmentsAdapter.ViewHolder> {


    Context mCtx;
    View view;

    List<Payload> appointmentsList;
    private String setRatingReviews;
    private EditText edWriteReview;
    private ProgressDialog mProgressDialog;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;


    public CompletedAppointmentsAdapter(FragmentActivity activity, List<Payload> appointmentsList) {

        this.mCtx = activity;
        this.appointmentsList = appointmentsList;

        mProgressDialog = new ProgressDialog(mCtx, R.style.AppTheme_Dark_Dialog); //, R.style.AppTheme_Dark_Dialog

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_appointments_adapter_row, parent, false);
        return new CompletedAppointmentsAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position){
        final Payload appointments = appointmentsList.get(position);

        holder.tvTitle.setText(appointments.getProviderName());
        holder.tvTypesOfJobs.setText(appointments.getServicesNames());
        holder.tvJobs.setText(appointments.getNoOfJobsCompleted().toString() + " " + "Job completed");
        holder.tvDates.setText(appointments.getDate());
        Picasso.get().load(appointments.getProviderProfile()).error(R.drawable.noimage).into(holder.profileImage);


        Drawable drawable = holder.averageRatingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#FFFDD433"), PorterDuff.Mode.SRC_ATOP);


//        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//
//                Toast.makeText(mCtx, "Rating changed, current rating " + ratingBar.getRating(),
//                        Toast.LENGTH_SHORT).show();

//        DecimalFormat form = new DecimalFormat("0.00");
//        holder.averageRatingBar.setRating(Float.parseFloat(form.format(appointments.getAverageRating())));
//        holder.averageRatingBar.setEnabled(false);
//        holder.averageRatingBar.invalidate();

//        holder.averageRatingBar.setEnabled(false);
        holder.averageRatingBar.setMax(5);
        holder.averageRatingBar.setStepSize(0.01f);
        holder.averageRatingBar.setRating(appointments.getAverageRating());
        holder.averageRatingBar.invalidate();

//
//            }
//        });


        holder.tvWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                writeAReviewDialog(appointments.getProviderId().toString(), position, appointments.getProviderName());


            }
        });


    }

    private void writeAReviewDialog(String providerId, int position, String providerName) {

        LayoutInflater li = LayoutInflater.from(mCtx);
        View dialogView = li.inflate(R.layout.dialog_write_a_review, null);

        findingBookACleanerIdsHere(dialogView, providerId, position, providerName);


        alertDialogBuilder = new AlertDialog.Builder(mCtx);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


    }

    private void findingBookACleanerIdsHere(View dialogView, final String providerId, final int position, final String providerName) {

        final RatingBar writeReviewRatingBar = dialogView.findViewById(R.id.writeReviewRatingBar);
        final TextView tvSubmitReview = dialogView.findViewById(R.id.tvSubmitReview);
        edWriteReview = dialogView.findViewById(R.id.edWriteReview);
        TextView tvNameReview = dialogView.findViewById(R.id.tvNameReview);
        tvNameReview.setText(providerName);

//        Drawable drawableReview = writeReviewRatingBar.getProgressDrawable();
//        drawableReview.setColorFilter(Color.parseColor("#FFFDD433"), PorterDuff.Mode.SRC_ATOP);


        writeReviewRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

//                Toast.makeText(mCtx, "Rating changed, current rating " + ratingBar.getRating(), Toast.LENGTH_SHORT).show();

                writeReviewRatingBar.setRating(ratingBar.getRating());
                setRatingReviews = String.valueOf(ratingBar.getRating());


            }
        });


        tvSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (writeReviewRatingBar.getRating() == 0.0) {

                    TastyToast.makeText(mCtx, "No review has given", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                } else if (TextUtils.isEmpty(edWriteReview.getText().toString())) {

                    TastyToast.makeText(mCtx, "Write some comments", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                } else {

                    loginProgressing();
                    callReviewsApiFromHere(providerId, position, setRatingReviews, providerName);
                }


            }
        });


    }

    private void loginProgressing() {

        mProgressDialog.setMessage("Waiting..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();


    }

    private void callReviewsApiFromHere(String providerId, int position, String setRatingReviews, String providerName) {


        compositeDisposable.add(HttpModule.provideRepositoryService().reviewsAdded((String) Hawk.get("savedUserId"), "43",
                providerId, setRatingReviews, edWriteReview.getText().toString()).subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ReviewsAdded>() {

            @Override
            public void accept(final ReviewsAdded reviewsAdded) throws Exception {


                if (reviewsAdded != null && reviewsAdded.getIsSuccess()) {

                    edWriteReview.getText().clear();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            mProgressDialog.dismiss();
                            alertDialog.dismiss();

                            new AwesomeSuccessDialog(mCtx)
                                    .setTitle("Great")
                                    .setMessage(reviewsAdded.getMessage())
                                    .setColoredCircle(R.color.english_btnColor)
                                    .setDialogIconAndColor(R.drawable.ic_success, R.color.white)
                                    .setCancelable(false)
                                    .setPositiveButtonText("Ok")
                                    .setPositiveButtonbackgroundColor(R.color.english_btnColor)
                                    .setPositiveButtonTextColor(R.color.white)
                                    .setPositiveButtonClick(new Closure() {
                                        @Override
                                        public void exec() {


                                        }
                                    })
                                    .show();


                        }
                    }, 3000);


                } else {

                    mProgressDialog.dismiss();
                    TastyToast.makeText(mCtx, Objects.requireNonNull(reviewsAdded).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                }

            }

        }, new Consumer<Throwable>() {

            @Override
            public void accept(Throwable throwable) throws Exception {

                mProgressDialog.dismiss();
                TastyToast.makeText(mCtx, throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
            }


        }));


    }


    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private RatingBar ratingBar, averageRatingBar;
        private TextView tvTitle, tvTypesOfJobs, tvJobs, tvDates, tvWriteReview;
        private CircleImageView profileImage;


        public ViewHolder(View itemView) {
            super(itemView);

            ratingBar = itemView.findViewById(R.id.ratingBar);
            averageRatingBar = itemView.findViewById(R.id.averageRatingBar);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTypesOfJobs = itemView.findViewById(R.id.tvTypesOfJobs);
            tvJobs = itemView.findViewById(R.id.tvJobs);
            tvDates = itemView.findViewById(R.id.tvDates);
            tvWriteReview = itemView.findViewById(R.id.tvWriteReview);
            profileImage = itemView.findViewById(R.id.profileImage);


        }


    }


}
