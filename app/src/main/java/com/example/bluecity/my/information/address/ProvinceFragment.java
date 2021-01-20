package com.example.bluecity.my.information.address;


import android.os.Bundle;
import android.util.Log;
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
import com.example.bluecity.databinding.FragmentProvinceBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProvinceFragment extends Fragment {

    private List<String> list;

    public ProvinceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //实例化ViewModel，并绑定Activity
        final AddressViewModel addressViewModel = new ViewModelProvider(getActivity()).get(AddressViewModel.class);
        //实例化binding，并绑定布局
        FragmentProvinceBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_province, container, false);
        binding.setData(addressViewModel);
        //委托activity管理生命周期
        binding.setLifecycleOwner(getActivity());

        initData();

        binding.rvProvince.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvProvince.setAdapter(new AddressAdapter(list, new AddressAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                String province = list.get(position);
                addressViewModel.province.setValue(province);       //保存点击的省份为province
                Log.e("保存的点击的省份", province);
                NavController controller = Navigation.findNavController(getActivity(), R.id.rv_province);
                controller.navigate(R.id.action_provinceFragment_to_cityFragment);
            }
        }));

        return binding.getRoot();
    }

    void initData() {
        list = new ArrayList<>();
        list.add("北京市");
        list.add("天津市");
        list.add("河北省");
        list.add("山西省");
        list.add("内蒙古自治区");
        list.add("辽宁省");
        list.add("吉林省");
        list.add("黑龙江省");
        list.add("上海市");
        list.add("江苏省");
        list.add("浙江省");
        list.add("安徽省");
        list.add("福建省");
        list.add("江西省");
        list.add("山东省");
        list.add("河南省");
        list.add("湖北省");
        list.add("湖南省");
        list.add("广东省");
        list.add("广西壮族自治区");
        list.add("海南省");
        list.add("重庆市");
        list.add("四川省");
        list.add("贵州省");
        list.add("云南省");
        list.add("西藏自治区");
        list.add("陕西省");
        list.add("甘肃省");
        list.add("青海省");
        list.add("宁夏回族自治区");
        list.add("台湾省");
        list.add("香港特别行政区");
        list.add("澳门特别行政区");
    }
}
