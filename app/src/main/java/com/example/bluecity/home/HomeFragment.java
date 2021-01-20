package com.example.bluecity.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bluecity.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private RecyclerView mRvHome;
    private BottomNavigationView bottomNavigationView;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        //底部导航栏可见性
        AppCompatActivity mainActivity = (AppCompatActivity) getActivity();
        bottomNavigationView = mainActivity.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.VISIBLE);

        mRvHome = view.findViewById(R.id.grid_home);
        mRvHome.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mRvHome.setAdapter(new HomeAdapter(getActivity(), 2, new HomeAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                AppCompatActivity mainActivity = (AppCompatActivity) getActivity();
                bottomNavigationView = mainActivity.findViewById(R.id.bottomNavigationView);
                bottomNavigationView.setVisibility(View.GONE);

            }
        }));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }

}
