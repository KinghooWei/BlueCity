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
import com.example.bluecity.databinding.FragmentBuildingBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuildingFragment extends Fragment {


    public BuildingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //实例化ViewModel，关联activity
        final AddressViewModel addressViewModel = new ViewModelProvider(getActivity()).get(AddressViewModel.class);
//        final AddressViewModel addressViewModel = ViewModelProviders.of(this, new SavedStateViewModelFactory(getActivity().getApplication(), this))
//                .get(AddressViewModel.class);
        //实例化binding，关联布局
        final FragmentBuildingBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_building, container, false);
        binding.setData(addressViewModel);
        binding.setLifecycleOwner(getActivity());

        binding.rvBuilding.setLayoutManager(new LinearLayoutManager(getActivity()));
        final String community = addressViewModel.getCommunity().getValue();   //获取点击的社区
        final List<String> buildingList = initData(community);
        binding.rvBuilding.setAdapter(new AddressAdapter(buildingList, new AddressAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                addressViewModel.isFromBuilding.setValue(true);
                String building = buildingList.get(position);
                addressViewModel.building.setValue(building);
                NavController controller = Navigation.findNavController(getActivity(), R.id.rv_building);
                controller.navigate(R.id.action_buildingFragment_to_addressFragment);
            }
        }));
        return binding.getRoot();
    }

    private List<String> initData(String community) {
        List<String> buildingList = new ArrayList<>();
        switch (community) {
            case "华南理工大学":
                buildingList.add("3号楼");
                break;
            case "豪利花园":
            case "凯旋新世界广粤尊府":
            case "翡翠绿洲森林半岛":
            case "中海康城花园":
            case "云溪四季":
            case "国展苑":
            case "龙珠花园":
            case "万科清林径":
            case "英郡年华":
            case "万象天成":
                buildingList.add("1幢");
                buildingList.add("2幢");
                buildingList.add("3幢");
                buildingList.add("4幢");
                buildingList.add("5幢");
                buildingList.add("6幢");
                buildingList.add("7幢");
                buildingList.add("8幢");
                buildingList.add("9幢");
                buildingList.add("10幢");
                buildingList.add("11幢");
                buildingList.add("12幢");
                buildingList.add("13幢");
                buildingList.add("14幢");
                buildingList.add("15幢");
                break;
        }
        return buildingList;
    }
}
