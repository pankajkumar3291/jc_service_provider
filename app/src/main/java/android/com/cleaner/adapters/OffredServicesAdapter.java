package android.com.cleaner.adapters;
import android.com.cleaner.R;
import android.com.cleaner.activities.ActivityOffredServices;
import android.com.cleaner.apiResponses.offerredServices.Payload;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
public class OffredServicesAdapter extends RecyclerView.Adapter<OffredServicesAdapter.ViewHolder> {
    private Context mCtx;
    View view;
    private List<Payload> offeredServices;
    public OffredServicesAdapter(ActivityOffredServices activityOffredServices, List<Payload> jobsList) {
        this.mCtx = activityOffredServices;
        this.offeredServices = jobsList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_offered_services_adapter, parent, false);
        return new OffredServicesAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Payload services = offeredServices.get(position);
        holder.tvTitle.setText(services.getName());
        holder.tvDescription.setText(services.getDescription());
        holder.tvDescription.setText(Html.fromHtml(Html.fromHtml(services.getDescription()).toString()));
//        holder.tvDescription.setMovementMethod(new ScrollingMovementMethod());
        Picasso.get().load(services.getImage())
                .placeholder(R.drawable.placeholder)   // optional
                .error(R.drawable.noimage)      // optional
//                .resize(600, 600)                        // optional
                .into(holder.profileImage);
//        if (position % 2 == 0) {
//            holder.reParent.setBackgroundColor(Color.parseColor("#FF3EA9F9"));
//        } else {
//            holder.reParent.setBackgroundColor(Color.parseColor("#cb00cb"));
//        }
        String[] mColors = {"#DA676F", "#01D1BB", "#2C66AD", "#333C73"};
        holder.reParent.setBackgroundColor(Color.parseColor(mColors[position % 4]));
    }
    @Override
    public int getItemCount() {
        return offeredServices.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDescription, tvTitle;
        private CircleImageView profileImage;
        private RelativeLayout reParent;
        public ViewHolder(View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            profileImage = itemView.findViewById(R.id.profileImage);
            reParent = itemView.findViewById(R.id.reParent);
        }
    }
}
