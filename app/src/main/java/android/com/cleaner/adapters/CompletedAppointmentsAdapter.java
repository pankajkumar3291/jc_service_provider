package android.com.cleaner.adapters;

import android.app.AlertDialog;
import android.com.cleaner.R;
import android.com.cleaner.models.CompletedAppointments;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.List;

public class CompletedAppointmentsAdapter extends RecyclerView.Adapter<CompletedAppointmentsAdapter.ViewHolder> {


    Context mCtx;
    View view;

    List<CompletedAppointments> appointmentsList;

    public CompletedAppointmentsAdapter(FragmentActivity activity, List<CompletedAppointments> appointmentsList) {

        this.mCtx = activity;
        this.appointmentsList = appointmentsList;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_appointments_adapter_row, parent, false);
        return new CompletedAppointmentsAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


        CompletedAppointments appointments = appointmentsList.get(position);

        holder.tvTitle.setText(appointments.getTitle());
        holder.tvTypesOfJobs.setText(appointments.getTypesofJobs());
        holder.tvJobs.setText(appointments.getCompletedJobs());
        holder.tvDates.setText(appointments.getDate());


        Drawable drawable = holder.ratingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#FFFDD433"), PorterDuff.Mode.SRC_ATOP);


        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                Toast.makeText(mCtx, "Rating changed, current rating " + ratingBar.getRating(),
                        Toast.LENGTH_SHORT).show();


                holder.ratingBar.setRating(ratingBar.getRating());


            }
        });


        holder.tvWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                TastyToast.makeText(mCtx, "Write a Review", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();


                writeAReviewDialog();


            }
        });


    }

    private void writeAReviewDialog() {

        LayoutInflater li = LayoutInflater.from(mCtx);
        View dialogView = li.inflate(R.layout.dialog_write_a_review, null);

        findingBookACleanerIdsHere(dialogView);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder
                .setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


    }

    private void findingBookACleanerIdsHere(View dialogView) {

        final RatingBar writeReviewRatingBar = dialogView.findViewById(R.id.writeReviewRatingBar);

        Drawable drawableReview = writeReviewRatingBar.getProgressDrawable();
        drawableReview.setColorFilter(Color.parseColor("#FFFDD433"), PorterDuff.Mode.SRC_ATOP);


//        writeReviewRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//
//                Toast.makeText(mCtx, "Rating changed, current rating " + ratingBar.getRating(),
//                        Toast.LENGTH_SHORT).show();
//
//
//                writeReviewRatingBar.setRating(ratingBar.getRating());
//
//
//            }
//        });


    }


    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        RatingBar ratingBar;
        TextView tvTitle, tvTypesOfJobs, tvJobs, tvDates, tvWriteReview;


        public ViewHolder(View itemView) {
            super(itemView);

            ratingBar = itemView.findViewById(R.id.ratingBar);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTypesOfJobs = itemView.findViewById(R.id.tvTypesOfJobs);
            tvJobs = itemView.findViewById(R.id.tvJobs);
            tvDates = itemView.findViewById(R.id.tvDates);
            tvWriteReview = itemView.findViewById(R.id.tvWriteReview);
        }


    }


}
