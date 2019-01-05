package android.com.cleaner.fragments;

import android.com.cleaner.R;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdsmdg.tastytoast.TastyToast;
//import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

public class ScheduleFragment extends Fragment {


    private View view;
//    CollapsibleCalendar collapsibleCalendar; // https://github.com/shrikanth7698/Collapsible-Calendar-View-Android
    // https://github.com/shrikanth7698/Collapsible-Calendar-View-Android


    // Alert dialog links
    // https://android-arsenal.com/tag/30


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {


//        view = inflater.inflate(R.layout.fragment_schedule, null);

//
//                findingIdsHere(view);
//                calenderOperationHere();


        return view;

    }

//    private void calenderOperationHere() {
//
//        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
//            @Override
//            public void onDaySelect() {
//                TastyToast.makeText(getActivity(), "OnDaySelect", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
//
//            }
//
//            @Override
//            public void onItemClick(View view) {
//                TastyToast.makeText(getActivity(), String.valueOf(view), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
//
//            }
//
//            @Override
//            public void onDataUpdate() {
//                TastyToast.makeText(getActivity(), "OnDayUpdate", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
//
//            }
//
//            @Override
//            public void onMonthChange() {
//                TastyToast.makeText(getActivity(), "OnMonthChange", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
//
//            }
//
//            @Override
//            public void onWeekChange(int i) {
//                TastyToast.makeText(getActivity(), "OnWeekChange" + i, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
//
//            }
//        });


//    }


    private void findingIdsHere(View view) {
//        collapsibleCalendar = view.findViewById(R.id.calendarView);

    }


}
