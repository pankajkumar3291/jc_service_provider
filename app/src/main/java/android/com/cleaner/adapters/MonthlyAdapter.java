package android.com.cleaner.adapters;

import android.com.cleaner.R;
import android.com.cleaner.activities.ActivityScheduleMonthly;
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

public class MonthlyAdapter extends RecyclerView.Adapter<MonthlyAdapter.MonthlyViewHolder> {


    private Context mCtx;
    private List<Time> timeList;

    public MonthlyAdapter(ActivityScheduleMonthly activityScheduleMonthly, List<Time> list) {

        this.mCtx = activityScheduleMonthly;
        this.timeList = list;


    }

    @NonNull
    @Override
    public MonthlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.monthly_adapter_row, null);
        return new MonthlyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MonthlyViewHolder holder, int position) {
        holder.tvMonthly.setTextColor(Color.parseColor("#FFFFFF")); // extra doze

        Time time = timeList.get(position);
        holder.checkMonthly.setText(time.getName());
        holder.tvMonthly.setText(time.getTime());


        holder.checkMonthly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (holder.checkMonthly.isChecked()) {


                    Toast.makeText(mCtx, "True", Toast.LENGTH_SHORT).show();
                    checkBoxColorCheckedUnchecked(holder);
                    holder.checkMonthly.setTextColor(Color.parseColor("#303F9F"));
                    holder.tvMonthly.setTextColor(Color.parseColor("#303F9F"));

                    // Changing drawable left color
                    setTextViewDrawableColor(holder.tvMonthly, R.color.english_btnColor);


                } else {


                    Toast.makeText(mCtx, "False", Toast.LENGTH_SHORT).show();
                    holder.checkMonthly.setTextColor(Color.parseColor("#FFFFFF"));
                    holder.tvMonthly.setTextColor(Color.parseColor("#FFFFFF"));

                    // Changing drawable left color
                    setTextViewDrawableColor(holder.tvMonthly, R.color.white);
                }
            }
        });


    }

    private void setTextViewDrawableColor(TextView tvMonthly, int english_btnColor) {

        for (Drawable drawable : tvMonthly.getCompoundDrawables()) {
            if (drawable != null) {

                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(tvMonthly.getContext(), english_btnColor), PorterDuff.Mode.SRC_IN));
            }
        }


    }

    private void checkBoxColorCheckedUnchecked(MonthlyViewHolder holder) {


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
//        CompoundButtonCompat.setButtonTintList(holder.checkWeekly, colorStateList);
        CompoundButtonCompat.setButtonTintList(holder.checkMonthly, colorStateList);


    }


    @Override
    public int getItemCount() {
        return timeList.size();
    }


    public class MonthlyViewHolder extends RecyclerView.ViewHolder {

        public CheckBox checkMonthly;
        public TextView tvMonthly;


        public MonthlyViewHolder(View itemView) {
            super(itemView);


            checkMonthly = itemView.findViewById(R.id.checkMonthly);
            tvMonthly = itemView.findViewById(R.id.tvMonthly);

        }
    }
}
