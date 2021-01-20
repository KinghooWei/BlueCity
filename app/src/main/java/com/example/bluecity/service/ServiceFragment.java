package com.example.bluecity.service;

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

public class ServiceFragment extends Fragment {

    private ServiceViewModel mViewModel;
    private RecyclerView mRvService;

    public static ServiceFragment newInstance() {
        return new ServiceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.service_fragment, container, false);
        mRvService = view.findViewById(R.id.grid_service);
        mRvService.setLayoutManager(new GridLayoutManager(getActivity(),3));
        mRvService.setAdapter(new ServiceAdapter(getActivity(), 7, new ServiceAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {

            }
        }));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ServiceViewModel.class);
        // TODO: Use the ViewModel
    }

}
