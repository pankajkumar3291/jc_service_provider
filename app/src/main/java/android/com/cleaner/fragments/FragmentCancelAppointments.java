package android.com.cleaner.fragments;
import android.com.cleaner.R;
import android.com.cleaner.apiResponses.cancelled_appintments.EOCancelledPayload;
import android.com.cleaner.apiResponses.cancelled_appintments.GetCancelledAppointments;
import android.com.cleaner.httpRetrofit.HttpModule;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class FragmentCancelAppointments extends Fragment {

    private View mView;
    private Context mContext;
    private TextView noData;
    private  RecyclerView recCancelledList;
    private List<EOCancelledPayload> cancelledList=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragmen_cancelled_appointments_layout, container, false);
        Hawk.init(getContext()).build();
        recCancelledList=mView.findViewById(R.id.rec_cancelled_list);
        noData=mView.findViewById(R.id.no_data);
        callApi();
//        findingAboutIdsHere(view);
//        settingValuesHere(view);
        return mView;
    }
    private void callApi() {
        try {
            HttpModule.provideRepositoryService().getCanelledAppointments(Hawk.get("spanish",false)?"es":"en",String.valueOf(Hawk.get("USER_ID"))).enqueue(new Callback<GetCancelledAppointments>() {
                @Override
                public void onResponse(Call<GetCancelledAppointments> call, Response<GetCancelledAppointments> response) {
                    if (response.body()!=null)
                    {
                     if (response.body().getIsSuccess() && response.body().getPayload().size()>0)
                     {
                         cancelledList.addAll(response.body().getPayload());
                         recCancelledList.setLayoutManager(new LinearLayoutManager(getContext()));
                         recCancelledList.setHasFixedSize(true);
                         recCancelledList.setAdapter(new CancelAppointmentAdapter(cancelledList,getContext()));
                         noData.setVisibility(View.GONE);
                         recCancelledList.setVisibility(View.VISIBLE);
                     }
                     else
                     {
                         noData.setVisibility(View.VISIBLE);
                         recCancelledList.setVisibility(View.GONE);
                     }
                    }
                    else
                    {
                        noData.setVisibility(View.VISIBLE);
                        recCancelledList.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onFailure(Call<GetCancelledAppointments> call, Throwable t) {
                    noData.setVisibility(View.VISIBLE);
                    recCancelledList.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            noData.setVisibility(View.VISIBLE);
            recCancelledList.setVisibility(View.GONE);
        }
    }
    //todo ===================================== Adapter class =====================================

   public class CancelAppointmentAdapter extends RecyclerView.Adapter<CancelAppointmentAdapter.CancelAppointmentsViewHolder>
   {
       private List<EOCancelledPayload> appointmentList;
       private Context context;
       public CancelAppointmentAdapter(List<EOCancelledPayload> appointmentList, Context context) {
           this.appointmentList = appointmentList;
           this.context = context;
       }
       @NonNull
       @Override
       public CancelAppointmentsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
           View view  = LayoutInflater.from(context).inflate(R.layout.row_cancel_appontments,viewGroup,false);
           return new CancelAppointmentsViewHolder(view);
       }
       @Override
       public void onBindViewHolder(@NonNull CancelAppointmentsViewHolder cancelAppointmentsViewHolder, int i) {
           EOCancelledPayload eoCancelledPayload=appointmentList.get(i);
           cancelAppointmentsViewHolder.tvTitle.setText(eoCancelledPayload.getProviderName());
           cancelAppointmentsViewHolder.discription.setText(eoCancelledPayload.getServicesNames());
           cancelAppointmentsViewHolder.in_date.setText(eoCancelledPayload.getDate());
           cancelAppointmentsViewHolder.in_time.setText(eoCancelledPayload.getTime());
           if (!TextUtils.isEmpty(eoCancelledPayload.getProviderProfile()))
           {
               Picasso.get().load(eoCancelledPayload.getProviderProfile()).resize(100,100).error(R.drawable.noimage).into(cancelAppointmentsViewHolder.profileImage);
           }
           else
           {
               cancelAppointmentsViewHolder.profileImage.setImageResource(R.drawable.noimage);
           }
       }
       @Override
       public int getItemCount() {
           return appointmentList.size();
       }
       public class CancelAppointmentsViewHolder extends RecyclerView.ViewHolder{
           private TextView in_date, in_time, tvTitle,discription;
           private CircleImageView profileImage;
           private ImageView imageDots, imageCancel, imageMessage, imageCall;
           private RelativeLayout imagesLayout;
           private FrameLayout delete_layout;

           public CancelAppointmentsViewHolder(@NonNull View itemView) {
               super(itemView);
               in_date = itemView.findViewById(R.id.in_date);//
               in_time = itemView.findViewById(R.id.in_time);//
               tvTitle = itemView.findViewById(R.id.tvTitle);//
               profileImage = itemView.findViewById(R.id.profileImage);//
               imageDots = itemView.findViewById(R.id.imageDots);
               imagesLayout = itemView.findViewById(R.id.imagesLayout);
               imageMessage = itemView.findViewById(R.id.imageMessage);
               imageCall = itemView.findViewById(R.id.imageCall);
               delete_layout = itemView.findViewById(R.id.delete_layout);
               discription=itemView.findViewById(R.id.textView);
           }
       }
   }

}
