package com.idealtechcontrivance.ashish.smartway.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idealtechcontrivance.ashish.smartway.Adapter.BannerViewPagerAdapter;
import com.idealtechcontrivance.ashish.smartway.Common.Common;
import com.idealtechcontrivance.ashish.smartway.Model.Banner;
import com.idealtechcontrivance.ashish.smartway.Model.Category;
import com.idealtechcontrivance.ashish.smartway.R;
import com.idealtechcontrivance.ashish.smartway.ViewHolder.CategoryViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    Timer timer;
    private int page = 0;

    private SwipeRefreshLayout refreshLayout;
    private DatabaseReference bannerReference,categoryReference;
    private RecyclerView categoryRecyclerView;
    private FirebaseRecyclerAdapter<Category, CategoryViewHolder> categoryAdapter;

    private ViewPager bannerViewPager;
    private BannerViewPagerAdapter bannerPagerAdapter;
    private LinearLayout bannerDots;
    private int dotsCount;
    private ImageView[] dots;
    private List<Banner> bannerList;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        refreshLayout = rootView.findViewById(R.id.swipeToRefresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Common.isConnectedToInternet(getContext()))
                    loadCategory();
                else {
                    Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        //Default load for first time
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Common.isConnectedToInternet(getContext()))
                    loadCategory();
                else {
                    Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        //INITIALIZE FB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        bannerReference = database.getReference(Common.banner_tbl);
        categoryReference = database.getReference(Common.category_tbl);

        //INITIALIZE RV
        categoryRecyclerView = rootView.findViewById(R.id.categoryRecyclerView);
        //categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setNestedScrollingEnabled(false);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        //INITIALIZE VP
        bannerViewPager= (ViewPager) rootView.findViewById(R.id.bannerViewPager);
        bannerDots = (LinearLayout) rootView.findViewById(R.id.bannerDots);

        bannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page = position;
                for (int i=0; i<dotsCount; i++)
                {
                    dots[i].setImageDrawable(ActivityCompat.getDrawable(getContext(), R.drawable.dot_nonactive));
                }
                dots[position].setImageDrawable(ActivityCompat.getDrawable(getContext(),R.drawable.dot_active));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(),2000,4000);

        loadBanner();

        return rootView;
    }

    private void loadBanner(){
        bannerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bannerList = new ArrayList<>();
                // StringBuffer stringbuffer = new StringBuffer();
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    Banner banner = dataSnapshot1.getValue(Banner.class);
                    bannerList.add(banner);

                }
                bannerPagerAdapter = new BannerViewPagerAdapter(bannerList,getContext());
                bannerPagerAdapter.notifyDataSetChanged();
                bannerViewPager.setAdapter(bannerPagerAdapter);

                dotsCount = bannerPagerAdapter.getCount();
                dots = new ImageView[dotsCount];

                for (int i=0; i<dotsCount; i++)
                {
                    dots[i] = new ImageView(getContext());
                    dots[i].setImageDrawable(ActivityCompat.getDrawable(getActivity(),R.drawable.dot_nonactive));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8,0,8,0);
                    bannerDots.addView(dots[i],params);
                }
                dots[0].setImageDrawable(ActivityCompat.getDrawable(getActivity(),R.drawable.dot_active));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            if (getActivity() != null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bannerPagerAdapter != null){
                            if (bannerPagerAdapter.getCount() == page) {
                                page = 0;
                            } else {
                                page++;
                            }
                            bannerViewPager.setCurrentItem(page, true);
                        }
                    }
                });
            }
        }
    }

    private void loadCategory() {
        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>().setQuery(categoryReference, Category.class).build();
        categoryAdapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(CategoryViewHolder holder, final int position, final Category model) {
                holder.setName(model.getName());
                holder.setImage(getContext(), model.getImage());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String url = categoryAdapter.getRef(position).getKey();
                        Bundle bundle = new Bundle();
                        bundle.putString("categoryId",url);
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        OffersFragment offersFragment = new OffersFragment();
                        offersFragment.setArguments(bundle);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, offersFragment).addToBackStack(null).commit();
                    }
                });
            }

            @Override
            public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category, parent, false);
                return new CategoryViewHolder(view);
            }

        };
        categoryAdapter.notifyDataSetChanged();     //Refresh data if data have changed
        categoryAdapter.startListening();
        categoryRecyclerView.setAdapter(categoryAdapter);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        categoryAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (categoryAdapter!=null)
            categoryAdapter.startListening();
    }

}
