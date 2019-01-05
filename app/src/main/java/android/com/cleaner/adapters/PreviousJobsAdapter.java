package android.com.cleaner.adapters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.com.cleaner.R;
import android.com.cleaner.interfaces.ItemClickListenerTwo;
import android.com.cleaner.models.PreviousJobs;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.sdsmdg.tastytoast.TastyToast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PreviousJobsAdapter extends RecyclerView.Adapter<PreviousJobsAdapter.PreviousJobsViewHolder> {


    private Context mCtx;
    View view;

    private ItemClickListenerTwo clickListener;


    List<PreviousJobs> preJobsList;

    Calendar myCalendar = Calendar.getInstance();

    public PreviousJobsAdapter(Context context, List<PreviousJobs> jobsList) {


        this.mCtx = context;
        this.preJobsList = jobsList;

    }


    @NonNull
    @Override
    public PreviousJobsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        view = inflater.inflate(R.layout.row_previous_jobs, null);
        return new PreviousJobsViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final PreviousJobsViewHolder holder, int position) {


        PreviousJobs jobs = preJobsList.get(position);
        holder.tvTitle.setText(jobs.getTitle());

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(holder);
            }
        };


        holder.calenderPikImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(mCtx, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });


        holder.rebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rebbokDialog();


            }
        });


//        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
//        Drawable drawable = ratingBar.getProgressDrawable();
//        drawable.setColorFilter(Color.parseColor("#0064A8"),PorterDuff.Mode.SRC_ATOP);


    }

    private void rebbokDialog() {

        LayoutInflater li = LayoutInflater.from(mCtx);
        View dialogView = li.inflate(R.layout.dialog_rebook, null);

        findingDialogRebookIds(dialogView);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder
                .setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


    }

    private void findingDialogRebookIds(View dialogView) {

        TextView tvRebookThisCleaningJobs;
        tvRebookThisCleaningJobs = dialogView.findViewById(R.id.tvRebookThisCleaningJobs);


        tvRebookThisCleaningJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TastyToast.makeText(mCtx, "Rebooking cleaners", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
            }
        });

    }

    private void updateLabel(PreviousJobsViewHolder holder) {


        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        holder.in_date.setVisibility(View.VISIBLE);
        holder.in_date.setText(sdf.format(myCalendar.getTime()));

    }


    @Override
    public int getItemCount() {

        return preJobsList.size();
    }

    public void setClickListener(ItemClickListenerTwo itemClickListener) {
        this.clickListener = itemClickListener;
    }


    public class PreviousJobsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private CircleImageView profileImage;
        private TextView tvTitle, rebookBtn;
        private ImageView calenderPikImage;
        private TextView in_date;
        private RatingBar ratingBar;
        private RelativeLayout reParent;


        public PreviousJobsViewHolder(View itemView) {
            super(itemView);


            profileImage = itemView.findViewById(R.id.profile_image);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            rebookBtn = itemView.findViewById(R.id.rebookBtn);
            calenderPikImage = itemView.findViewById(R.id.calenderPikImage);
            in_date = itemView.findViewById(R.id.in_date);
            ratingBar = itemView.findViewById(R.id.ratingBar);


            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {

            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());

        }


    }


}
