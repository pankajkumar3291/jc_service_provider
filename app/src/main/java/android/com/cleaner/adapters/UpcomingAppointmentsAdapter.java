package android.com.cleaner.adapters;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.com.cleaner.R;
import android.com.cleaner.activities.ActivityChat;
import android.com.cleaner.activities.DashboardActivity;
import android.com.cleaner.apiResponses.customerCurrentJobs.Payload;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.com.cleaner.models.CancelAppointmentByCustomerModel;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
// https://github.com/chthai64/SwipeRevealLayout/blob/master/demo/src/main/java/com/chauthai/swipereveallayoutdemo/RecyclerAdapter.java
public class UpcomingAppointmentsAdapter extends RecyclerView.Adapter<UpcomingAppointmentsAdapter.ViewHolder> {
    private Context mcontext;
    private List<Payload> cuurentPayloadList;
    private View mview;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private TextView tvCancel, tvDone;
    private ProgressDialog mProgressDialog;
    public UpcomingAppointmentsAdapter(FragmentActivity activity, List<Payload> payload) {
        this.mcontext = activity;
        this.cuurentPayloadList = payload;
        mProgressDialog=new ProgressDialog(mcontext);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_list_swap, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(mview);
        return viewHolder;
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public int getItemViewType(int position){
        return position;
    }
    @Override
    public void onBindViewHolder(@NonNull final UpcomingAppointmentsAdapter.ViewHolder viewHolder, int pos) {
        final Payload payload = cuurentPayloadList.get(pos);
        viewHolder.in_date.setText(payload.getDate());
        viewHolder.in_time.setText(payload.getTime());
        viewHolder.tvTitle.setText(payload.getServicesNames());
        Picasso.get().load(payload.getProviderProfile()).error(R.drawable.noimage).into(viewHolder.profileImage);
        viewHolder.imageDots.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                viewHolder.imagesLayout.setVisibility(View.VISIBLE);
            }
        });
        viewHolder.imageCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                TastyToast.makeText(mcontext, "Cancelling", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                viewHolder.imagesLayout.setVisibility(View.GONE);
            }
        });
        viewHolder.imageMessage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                TastyToast.makeText(mcontext, "Messaging", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                viewHolder.imagesLayout.setVisibility(View.GONE);
            }
        });
        viewHolder.imageCall.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                TastyToast.makeText(mcontext, "Calling", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                viewHolder.imagesLayout.setVisibility(View.GONE);
            }
        });
        viewHolder.tvCencelr.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                interiorPaintingDialog(payload.getJobId());
            }
        });
viewHolder.tvChat.setOnClickListener(new View.OnClickListener(){
    @Override
    public void onClick(View v){
        Toast.makeText(mcontext,payload.getJobId().toString(),Toast.LENGTH_SHORT).show();
        mcontext.startActivity(new Intent(mcontext,ActivityChat.class).putExtra("jobid",payload.getJobId()).putExtra("pid",payload.getProviderId()));
    }
});
    }
    private void interiorPaintingDialog(int id){
        LayoutInflater li = LayoutInflater.from(mcontext);
        View dialogView = li.inflate(R.layout.dialog_interior_painting, null);
        findingLogoutDialodIdsHere(dialogView,id);
        alertDialogBuilder = new AlertDialog.Builder(mcontext);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    private void findingLogoutDialodIdsHere(View dialogView,int id){
        tvCancel = dialogView.findViewById(R.id.tvCancel);
        tvDone = dialogView.findViewById(R.id.tvDone);
        takingClicks(id);
    }
    private void takingClicks(final int id) {

        tvDone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    mProgressDialog.setMessage("Wait..");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.show();
                    HttpModule.provideRepositoryService().cancelAppointmentByCustomer(String.valueOf(id),String.valueOf(Hawk.get("savedUserId"))).enqueue(new Callback<CancelAppointmentByCustomerModel>() {
                        @Override
                        public void onResponse(Call<CancelAppointmentByCustomerModel> call, Response<CancelAppointmentByCustomerModel> response) {
                            mProgressDialog.dismiss();
                            if (response.body()!=null) {
                                if (response.body().getIsSuccess())
                                {
                                    Toast.makeText(mcontext,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                                    Fragment frg = null;
                                    frg = ((DashboardActivity)mcontext).getSupportFragmentManager().findFragmentById(R.id.content_frame);
                                    final FragmentTransaction ft =((DashboardActivity)mcontext).getSupportFragmentManager().beginTransaction();
                                    ft.detach(frg);
                                    ft.attach(frg);
                                    ft.commit();
                                }
                                else
                                {
                                    Toast.makeText(mcontext,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mcontext,"server error",Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<CancelAppointmentByCustomerModel> call, Throwable t) {
                            mProgressDialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                }
                alertDialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                alertDialog.dismiss();
                Toast.makeText(mcontext, "Thanks", Toast.LENGTH_SHORT).show();
            }
        }); }
    @Override
    public int getItemCount(){
        return cuurentPayloadList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView in_date, in_time, tvTitle, tvCencelr, tvChat;
        private CircleImageView profileImage;
        private ImageView imageDots, imageCancel, imageMessage, imageCall;
        private RelativeLayout imagesLayout;
        private LinearLayout delete_layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            in_date = itemView.findViewById(R.id.in_date);
            in_time = itemView.findViewById(R.id.in_time);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            profileImage = itemView.findViewById(R.id.profileImage);
            imageDots = itemView.findViewById(R.id.imageDots);
            imagesLayout = itemView.findViewById(R.id.imagesLayout);
            imageCancel = itemView.findViewById(R.id.imageCancel);
            imageMessage = itemView.findViewById(R.id.imageMessage);
            imageCall = itemView.findViewById(R.id.imageCall);
            delete_layout = itemView.findViewById(R.id.delete_layout);
            tvCencelr = itemView.findViewById(R.id.tvcancel);
            tvChat = itemView.findViewById(R.id.tvchat);
        }
    }
}