package android.com.cleaner.fragments;

import android.com.cleaner.R;
import android.com.cleaner.activities.ActivityCleanerProfile;
import android.com.cleaner.adapters.ProductAdapter;
import android.com.cleaner.interfaces.ItemClickListener;
import android.com.cleaner.models.Products;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeFragment extends Fragment implements ItemClickListener {


    private Context context;
    private EditText edMyLocationSearch;
    private RecyclerView recyclerViewHomeFragment;

    List<Products> productList;

    private NoInternetDialog noInternetDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        noInternetDialog = new NoInternetDialog.Builder(getContext()).build();

        findingIdsHere(view); // we are finding ids here
        adapterInit();


        return view;


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(CalligraphyContextWrapper.wrap(context));

    }

    private void adapterInit() {

        recyclerViewHomeFragment.setHasFixedSize(true);
        recyclerViewHomeFragment.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

    private void findingIdsHere(View view) {

        edMyLocationSearch = view.findViewById(R.id.edMyLocationSearch);
        recyclerViewHomeFragment = view.findViewById(R.id.recyclerViewHomeFragment);

        productList = new ArrayList<>();


        //adding some items to our list
        productList.add(
                new Products(
                        1,
                        "Shahzeb",
                        "Ropar Punjab",
                        4.3,
                        20,
                        R.drawable.customer_image));

        productList.add(
                new Products(
                        1,
                        "Bhupender",
                        "Chandigarh",
                        4.3,
                        60,
                        R.drawable.customer_image));

        productList.add(
                new Products(
                        1,
                        "Charnveer",
                        "Anantpur Sahib",
                        4.3,
                        50,
                        R.drawable.provider_image));


        productList.add(
                new Products(
                        1,
                        "Charnveer",
                        "Anantpur Sahib",
                        4.3,
                        50,
                        R.drawable.provider_image));


        productList.add(
                new Products(
                        1,
                        "Charnveer",
                        "Anantpur Sahib",
                        4.3,
                        50,
                        R.drawable.provider_image));


        productList.add(
                new Products(
                        1,
                        "Charnveer",
                        "Anantpur Sahib",
                        4.3,
                        50,
                        R.drawable.provider_image));


        ProductAdapter adapter = new ProductAdapter(getActivity(), productList);
        recyclerViewHomeFragment.setAdapter(adapter);
        adapter.setClickListener(this);


    }


    @Override
    public void onItemClick(View view, int position) {


        Context context1 = view.getContext();
        Intent intent = new Intent();

        switch (position) {

            case 0:
                intent = new Intent(context1, ActivityCleanerProfile.class);
                context1.startActivity(intent);
                break;

        }


    }
}
