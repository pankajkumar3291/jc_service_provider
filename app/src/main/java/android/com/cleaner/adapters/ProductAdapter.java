package android.com.cleaner.adapters;

import android.com.cleaner.R;
import android.com.cleaner.interfaces.ItemClickListener;
import android.com.cleaner.models.Products;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {


    // https://github.com/FlyingPumba/SimpleRatingBar


    private Context mCtx;
    private List<Products> productList;


    private ItemClickListener clickListener;


    public ProductAdapter(Context mCtx, List<Products> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.row_home_fragment, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Products product = productList.get(position);

        //binding the data with the viewholder views
//        holder.textViewTitle.setText(product.getTitle());
//        holder.textViewShortDesc.setText(product.getShortdesc());
//        holder.textViewRating.setText(String.valueOf(product.getRating()));
//        holder.textViewPrice.setText(String.valueOf(product.getPrice()));
//
//        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(product.getImage()));

//        holder.tvCleanerName.setText(product.getName());
//        holder.tvCleanerLocation.setText(product.getLocation());
//        holder.tvCleanerCharge.setText((int) product.getPrice());

//
//        SimpleRatingBar.AnimationBuilder builder = myRatingBar.getAnimationBuilder()
//                .setRatingTarget(3)
//                .setDuration(2000)
//                .setInterpolator(new BounceInterpolator());
//        builder.start();


    }


    @Override
    public int getItemCount() {

        return productList.size();
    }


    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircleImageView profile_image;
        private TextView tvCleanerName, tvCleanerLocation, tvCleanerCharge;


        public ProductViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {


            if (clickListener != null) clickListener.onItemClick(v, getAdapterPosition());

        }
    }
}
