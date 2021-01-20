package com.example.bluecity.access;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluecity.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;

public class AccessFragment extends Fragment {

    private AccessViewModel mViewModel;
    private RecyclerView mRvAccess;
    private BottomNavigationView bottomNavigationView;

    public static AccessFragment newInstance() {
        return new AccessFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.access_fragment, container, false);

        //底部导航栏可见性
        AppCompatActivity mainActivity = (AppCompatActivity) getActivity();
        bottomNavigationView = mainActivity.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.VISIBLE);

        mRvAccess = view.findViewById(R.id.grid_access);
        mRvAccess.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        List<String> iconInfos = Arrays.asList("临时密码", "居住成员", "呼叫转接", "呼叫记录", "二维码", "门禁记录");
        List<Integer> iconSrcs = Arrays.asList(R.drawable.ic_access_temporary, R.drawable.ic_access_member,
                R.drawable.ic_access_transfer, R.drawable.ic_access_directary, R.drawable.ic_access_fast, R.drawable.ic_access_record);
        AccessAdapter accessAdapter = new AccessAdapter(this,iconSrcs,iconInfos);
        accessAdapter.setOnItemClickListener(new AccessAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                AppCompatActivity mainActivity = (AppCompatActivity) getActivity();
                bottomNavigationView = mainActivity.findViewById(R.id.bottomNavigationView);
                bottomNavigationView.setVisibility(View.GONE);
                NavController controller = Navigation.findNavController(view);
                switch (pos) {
                    case 0:
                        controller.navigate(R.id.action_accessFragment_to_tempPwdFragment);
                        break;
                    case 4:
                        controller.navigate(R.id.action_accessFragment_to_QRCodeFragment);
                        break;
                    case 5:
                        controller.navigate(R.id.action_accessFragment_to_recordFragment);
                        break;
                }
            }
        });
        mRvAccess.setAdapter(accessAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AccessViewModel.class);
        // TODO: Use the ViewModel
    }

}
