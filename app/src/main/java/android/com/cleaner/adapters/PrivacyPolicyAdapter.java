package android.com.cleaner.adapters;

import android.com.cleaner.R;
import android.com.cleaner.activities.ActivityPrivacyPolicy;
import android.com.cleaner.models.PrivacyPolicy;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class PrivacyPolicyAdapter extends RecyclerView.Adapter<PrivacyPolicyAdapter.ViewHolder> {

    private Context mCtx;
    View view;
    List<PrivacyPolicy> policyList;

    public PrivacyPolicyAdapter(ActivityPrivacyPolicy activityPrivacyPolicy, List<PrivacyPolicy> policyList) {


        this.mCtx = activityPrivacyPolicy;
        this.policyList = policyList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.privacy_policy_adapter_row, parent, false);
        return new PrivacyPolicyAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        PrivacyPolicy policy = policyList.get(position);

        holder.tvTitle.setText(policy.getTitle());
        holder.tvDescription.setText(policy.getDescription());


    }


    @Override
    public int getItemCount() {
        return policyList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvDescription;


        public ViewHolder(View itemView) {
            super(itemView);


            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);

        }
    }


}
