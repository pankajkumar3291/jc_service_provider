package android.com.cleaner.adapters;

import android.app.TimePickerDialog;
import android.com.cleaner.R;
import android.com.cleaner.activities.ActivityWeeklyJobScheduling;
import android.com.cleaner.apiResponses.weeklyJobScheduled.Payload;
import android.com.cleaner.interfaces.ClickListnerForWeeklyJobScheduled;
import android.com.cleaner.requestModelsForWeeklyJob.DateTime;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WeeklyAdapter extends RecyclerView.Adapter<WeeklyAdapter.WeeklyViewHolder> {


    private Context mCtx;
    List<Payload> List;
    View view;
    private int mHour, mMinute;

    private ClickListnerForWeeklyJobScheduled clickListnerForWeeklyJobScheduled;
    private ArrayList<DateTime> integerArrayListForIds = new ArrayList<>();
    private DateTime dateTime;
    private String selectedTime;


    public WeeklyAdapter(ActivityWeeklyJobScheduling activitySchedule, java.util.List<Payload> timeList, ClickListnerForWeeklyJobScheduled clickListnerForWeeklyJobScheduled) {

        this.mCtx = activitySchedule;
        this.List = timeList;
        this.clickListnerForWeeklyJobScheduled = clickListnerForWeeklyJobScheduled;


    }

    @NonNull
    @Override
    public WeeklyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        view = inflater.inflate(R.layout.weekly_adapter_row, null);
        return new WeeklyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WeeklyViewHolder holder, final int position) {

        final Payload timeWeekly = List.get(position);
        holder.checkWeekly.setText(timeWeekly.getDay());


        holder.tvWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(mCtx,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                holder.tvWeekly.setText(hourOfDay + ":" + minute);
                                selectedTime = hourOfDay + ":" + minute;

                                dateTime = new DateTime();
                                dateTime.setDay(timeWeekly.getId());
                                dateTime.setTime(selectedTime);

                                integerArrayListForIds.add(dateTime);
                                clickListnerForWeeklyJobScheduled.weeklyJobScheduledAdapterItemClicked(integerArrayListForIds, position);
                                Hawk.put("TIME", hourOfDay + ":" + minute);


                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();


            }
        });


        holder.checkWeekly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                if (holder.checkWeekly.isChecked()){
                    checkBoxColorCheckedUnchecked(holder);
                    holder.tvWeekly.setVisibility(View.VISIBLE);

                } else {

                    integerArrayListForIds.remove(dateTime);
                    clickListnerForWeeklyJobScheduled.weeklyJobScheduledAdapterItemClicked(integerArrayListForIds, position);
                    holder.tvWeekly.setVisibility(View.GONE);
                }


            }
        });


    }

    private void setTextViewDrawableColor(TextView tvWeekly, int colorAccent) {

        for (Drawable drawable : tvWeekly.getCompoundDrawables()) {
            if (drawable != null) {

                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(tvWeekly.getContext(), colorAccent), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    private void checkBoxColorCheckedUnchecked(WeeklyViewHolder holder) {

        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked}, // unchecked
                        new int[]{android.R.attr.state_checked}, // checked
                },
                new int[]{
                        Color.parseColor("#FFFFFF"),
                        Color.parseColor("#FF182391"),
                }
        );

//        CompoundButtonCompat.setButtonTintList(checkDaily, colorStateList);
        CompoundButtonCompat.setButtonTintList(holder.checkWeekly, colorStateList);
//        CompoundButtonCompat.setButtonTintList(checkMonthly, colorStateList);


    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    public class WeeklyViewHolder extends RecyclerView.ViewHolder {

        public CheckBox checkWeekly;
        public TextView tvWeekly;

        public WeeklyViewHolder(View itemView) {
            super(itemView);


            checkWeekly = itemView.findViewById(R.id.checkWeekly);
            tvWeekly = itemView.findViewById(R.id.tvWeekly);


        }


    }
}
