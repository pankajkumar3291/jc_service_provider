package android.com.cleaner.adapters;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.com.cleaner.R;
import android.com.cleaner.apiResponses.allPreviousJobs.Payload;
import android.com.cleaner.apiResponses.previousJobsDetails.PreviousJobDetails;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.com.cleaner.interfaces.ItemClickListenerTwo;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
public class PreviousJobsAdapter extends RecyclerView.Adapter<PreviousJobsAdapter.PreviousJobsViewHolder> {
    private Context mCtx;
    private View view;
    private ItemClickListenerTwo clickListener;
    private List<Payload> preJobsList;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TextView tvCleanerName, tvLastAppointment, tvClearingType, tvRebookThisCleaningJobs;
    private RatingBar ratingBarRebook;
    private   AlertDialog alertDialog;

    public PreviousJobsAdapter(Context context, List<Payload> jobsList) {
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

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

//        edittext.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    public void onBindViewHolder(@NonNull final PreviousJobsViewHolder holder, int position) {
        final Payload jobs = preJobsList.get(position);
        holder.tvTitle.setText(jobs.getServicesNames());
        holder.in_date.setText(jobs.getDate());
        Picasso.get().load(jobs.getProviderProfile()).into(holder.profileImage);
        holder.ratingBarReview.setMax(5);
        holder.ratingBarReview.setStepSize(0.01f);
        holder.ratingBarReview.setRating(jobs.getAverageRating());
        holder.ratingBarReview.invalidate();
        String[] mColors = {"#DA676F","#01D1BB","#2C66AD","#333C73"};
//        holder.reParent.setBackgroundColor(Color.parseColor(mColors[position % 40]));
        holder.card_view.setCardBackgroundColor(Color.parseColor(mColors[position % 4]));
        holder.rebookBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                callingTheRebookApi(jobs.getProviderId(), jobs.getJobId());
                rebbokDialog(jobs.getProviderId(), jobs.getJobId());
            }
        });
    }
    private void rebbokDialog(Integer providerId, Integer jobId){
        LayoutInflater li = LayoutInflater.from(mCtx);
        View dialogView = li.inflate(R.layout.dialog_rebook, null);
        findingDialogRebookIds(dialogView, providerId, jobId);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
       alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }


    private void findingDialogRebookIds(final View dialogView, final Integer providerId, final Integer jobId) {






        tvRebookThisCleaningJobs = dialogView.findViewById(R.id.tvRebookThisCleaningJobs);
        tvCleanerName = dialogView.findViewById(R.id.tvCleanerName);
        tvLastAppointment = dialogView.findViewById(R.id.tvLastAppointment);
        tvClearingType = dialogView.findViewById(R.id.tvClearingType);
        ratingBarRebook = dialogView.findViewById(R.id.ratingBarRebook);
        final TextView tvdate=dialogView.findViewById(R.id.tv_date);
        final TextView tvTime=dialogView.findViewById(R.id.tv_time);



        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                tvdate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        tvdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                DatePickerDialog dialog =   new DatePickerDialog(v.getContext(),  android.R.style.Theme_Holo_Light_Dialog_NoActionBar, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(v.getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tvTime.setText( selectedHour + ":" + selectedMinute+":00");
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mTimePicker.show();

            }
        });
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        tvdate.setText(date);
        tvTime.setText(time);
        tvRebookThisCleaningJobs.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                final ProgressDialog dialog = new ProgressDialog(v.getContext());

                dialog.setMessage("wait...");
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(700,300);

                compositeDisposable.add(HttpModule.provideRepositoryService().rebookCleaner(String.valueOf(jobId),String.valueOf( Hawk.get("savedUserId")),tvdate.getText().toString(), tvTime.getText().toString()).
                        subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                        observeOn(AndroidSchedulers.mainThread()).
                        subscribe(new Consumer<PreviousJobDetails>(){
                            @Override
                            public void accept(PreviousJobDetails previousJobDetails) throws Exception {
                                dialog.dismiss();
                                alertDialog.dismiss();
                                if (previousJobDetails != null && previousJobDetails.getIsSuccess()) {
                                    Toast.makeText(mCtx,previousJobDetails.getMessage(),Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(mCtx,previousJobDetails.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                dialog.dismiss();
                                alertDialog.dismiss();
//                                Toast.makeText(mCtx,throwable.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }));

            }
        });
    }
    private void findingDialogRebookIds(View dialogView){
    }
    private void callingTheRebookApi(Integer providerId, Integer jobId) {
        compositeDisposable.add(HttpModule.provideRepositoryService().previousJobsDetails(Hawk.get("spanish",false)?"es":"en",String.valueOf(providerId), String.valueOf(jobId),String.valueOf( Hawk.get("savedUserId"))).
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<PreviousJobDetails>(){
                    @Override
                    public void accept(PreviousJobDetails previousJobDetails) throws Exception {
                        if (previousJobDetails != null && previousJobDetails.getIsSuccess()) {
                            Drawable drawable = ratingBarRebook.getProgressDrawable();
                            drawable.setColorFilter(Color.parseColor("#FFFDD433"), PorterDuff.Mode.SRC_ATOP);
                            tvCleanerName.setText(previousJobDetails.getPayload().getProviderName());
                            tvLastAppointment.setText(previousJobDetails.getPayload().getDate());
                            tvClearingType.setText(previousJobDetails.getPayload().getServicesNames());
                            ratingBarRebook.setRating(previousJobDetails.getPayload().getAverageRating());
                        } else {
                            TastyToast.makeText(mCtx, Objects.requireNonNull(previousJobDetails).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        TastyToast.makeText(mCtx, throwable.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    }
                }));
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
        private RatingBar ratingBarReview;
        private RelativeLayout reParent;
        private CardView card_view;
        public PreviousJobsViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            rebookBtn = itemView.findViewById(R.id.rebookBtn);
            calenderPikImage = itemView.findViewById(R.id.calenderPikImage);
            in_date = itemView.findViewById(R.id.in_date);
            ratingBarReview = itemView.findViewById(R.id.ratingBarReview);
            reParent = itemView.findViewById(R.id.reParent);
            card_view = itemView.findViewById(R.id.card_view);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}
