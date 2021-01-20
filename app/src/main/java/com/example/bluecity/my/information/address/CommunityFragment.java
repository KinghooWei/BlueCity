package com.example.bluecity.my.information.address;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bluecity.R;
import com.example.bluecity.databinding.FragmentCommunityBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommunityFragment extends Fragment {


    public CommunityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //实例化ViewModel，并关联activity
        final AddressViewModel addressViewModel = new ViewModelProvider(getActivity()).get(AddressViewModel.class);
//        final AddressViewModel addressViewModel = ViewModelProviders.of(this, new SavedStateViewModelFactory(getActivity().getApplication(), this))
//                .get(AddressViewModel.class);
        //实例化binding，并关联布局
        FragmentCommunityBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_community, container, false);
        binding.setData(addressViewModel);
        binding.setLifecycleOwner(getActivity());
        binding.rvCommunity.setLayoutManager(new LinearLayoutManager(getActivity()));
        final String city = addressViewModel.getCity().getValue();        //获取点击的城市
        final List<String> communityList = initData(city);
        binding.rvCommunity.setAdapter(new AddressAdapter(communityList, new AddressAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                String community = communityList.get(position);
                addressViewModel.community.setValue(community);      //保存点击的社区
                NavController controller = Navigation.findNavController(getActivity(), R.id.rv_community);
                controller.navigate(R.id.action_communityFragment_to_buildingFragment);
            }
        }));
        //binding.rvCommunity.setAdapter(new InfoAdapter(,));

        return binding.getRoot();
    }

    private List<String> initData(String city) {
        List<String> communityList = new ArrayList<>();
        switch (city) {
            case "广州市":
                communityList.add("华南理工大学");
                communityList.add("豪利花园");
                communityList.add("凯旋新世界广粤尊府");
                communityList.add("翡翠绿洲森林半岛");
                communityList.add("中海康城花园");
                communityList.add("云溪四季");
                break;
            case "深圳市":
                communityList.add("国展苑");
                communityList.add("龙珠花园");
                communityList.add("万科清林径");
                communityList.add("英郡年华");
                communityList.add("万象天成");
                break;
        }
        return communityList;
    }
}
