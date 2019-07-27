package android.com.cleaner.interfaces;

import android.com.cleaner.requestModelsForWeeklyJob.DateTime;

import java.util.List;

public interface ClickListnerForWeeklyJobScheduled {

    void weeklyJobScheduledAdapterItemClicked(List<DateTime> stringslist, int position);

}
