package android.com.cleaner.adapters;

import android.app.AlertDialog;
import android.com.cleaner.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

// https://github.com/chthai64/SwipeRevealLayout/blob/master/demo/src/main/java/com/chauthai/swipereveallayoutdemo/RecyclerAdapter.java


public class UpcomingAppointmentsAdapter extends RecyclerView.Adapter {
    private List<String> mDataSet = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();


    AlertDialog alertDialog;
    AlertDialog.Builder alertDialogBuilder;


    private TextView tvCancel, tvDone;


    public UpcomingAppointmentsAdapter(Context context, List<String> dataSet) {
        mContext = context;
        mDataSet = dataSet;
        mInflater = LayoutInflater.from(context);

        // uncomment if you want to open only one row at a time
        // binderHelper.setOpenOnlyOne(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_list_swap, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        final ViewHolder holder = (ViewHolder) h;

        if (mDataSet != null && 0 <= position && position < mDataSet.size()) {
            final String data = mDataSet.get(position);


            // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
            // put an unique string id as value, can be any string which uniquely define the data
            binderHelper.bind(holder.swipeLayout, data);

            // Bind your data here
            holder.bind(data);
        }
    }

    @Override
    public int getItemCount() {
        if (mDataSet == null)
            return 0;
        return mDataSet.size();
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onSaveInstanceState(Bundle)}
     */
    public void saveStates(Bundle outState) {
        binderHelper.saveStates(outState);
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onRestoreInstanceState(Bundle)}
     */
    public void restoreStates(Bundle inState) {
        binderHelper.restoreStates(inState);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private SwipeRevealLayout swipeLayout;
        private View frontLayout;
        private View deleteLayout;
        private TextView textView;
        private ImageView imageDots;
        private RelativeLayout imagesLayout;
        private ImageView imageCancel;

        public ViewHolder(View itemView) {
            super(itemView);

            swipeLayout = itemView.findViewById(R.id.swipe_layout);
            frontLayout = itemView.findViewById(R.id.front_layout);
            deleteLayout = itemView.findViewById(R.id.delete_layout);
//            textView = itemView.findViewById(R.id.text);

            imageDots = itemView.findViewById(R.id.imageDots);
            imagesLayout = itemView.findViewById(R.id.imagesLayout);
            imageCancel = itemView.findViewById(R.id.imageCancel);

        }

        public void bind(final String data) {
            deleteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    mDataSet.remove(getAdapterPosition());
//                    notifyItemRemoved(getAdapterPosition());


                    interiorPaintingDialog(data);


                }
            });

//            textView.setText(data);

            frontLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String displayText = "" + data + " clicked";
                    Toast.makeText(mContext, displayText, Toast.LENGTH_SHORT).show();
                    Log.d("RecyclerAdapter", displayText);

                }
            });


            imageDots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(mContext, "ImageClicked", Toast.LENGTH_SHORT).show();
                    imagesLayout.setVisibility(View.VISIBLE);


                }
            });


            imagesLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


            imageCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    imagesLayout.setVisibility(View.GONE);

                }
            });


        }
    }

    private void interiorPaintingDialog(String data) {


        LayoutInflater li = LayoutInflater.from(mContext);
        View dialogView = li.inflate(R.layout.dialog_interior_painting, null);


        findingLogoutDialodIdsHere(dialogView, data);


        alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder
                .setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


    }

    private void findingLogoutDialodIdsHere(View dialogView, String data) {

        tvCancel = dialogView.findViewById(R.id.tvCancel);
        tvDone = dialogView.findViewById(R.id.tvDone);


        takingClicks(data);


    }

    private void takingClicks(String data) {


        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDataSet.remove(0);
                notifyItemRemoved(0);


                alertDialog.dismiss();


            }
        });

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertDialog.dismiss();
                TastyToast.makeText(mContext, "Thanks", TastyToast.LENGTH_SHORT, TastyToast.WARNING).show();


            }
        });


    }
}