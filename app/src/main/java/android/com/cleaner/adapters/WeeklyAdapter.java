package android.com.cleaner.adapters;

import android.com.cleaner.R;
import android.com.cleaner.activities.ActivitySchedule;
import android.com.cleaner.models.Time;
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
import android.widget.Toast;

import java.util.List;

public class WeeklyAdapter extends RecyclerView.Adapter<WeeklyAdapter.WeeklyViewHolder> {


    private Context mCtx;
    List<Time> List;
    View view;

    public WeeklyAdapter(ActivitySchedule activitySchedule, List<Time> timeList) {


        this.mCtx = activitySchedule;
        this.List = timeList;

    }

    @NonNull
    @Override
    public WeeklyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        view = inflater.inflate(R.layout.weekly_adapter_row, null);
        return new WeeklyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WeeklyViewHolder holder, int position) {

        Time timeWeekly = List.get(position);
        holder.checkWeekly.setText(timeWeekly.getName());
        holder.tvWeekly.setText(timeWeekly.getTime());


        holder.checkWeekly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (holder.checkWeekly.isChecked()) {


                    Toast.makeText(view.getContext(), "True", Toast.LENGTH_SHORT).show();
                    checkBoxColorCheckedUnchecked(holder);
                    holder.checkWeekly.setTextColor(Color.parseColor("#303F9F"));
                    holder.tvWeekly.setTextColor(Color.parseColor("#303F9F"));

                    // Changing drawable left color
                    setTextViewDrawableColor(holder.tvWeekly, R.color.english_btnColor);


                } else {


                    Toast.makeText(view.getContext(), "False", Toast.LENGTH_SHORT).show();
                    holder.checkWeekly.setTextColor(Color.parseColor("#FFFFFF"));
                    holder.tvWeekly.setTextColor(Color.parseColor("#FFFFFF"));
                    setTextViewDrawableColor(holder.tvWeekly, R.color.white);
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
