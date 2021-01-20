package com.example.bluecity.my.information.address;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bluecity.HttpUtils;
import com.example.bluecity.MainActivity;
import com.example.bluecity.MyApplication;
import com.example.bluecity.R;
import com.example.bluecity.SharedPreferencesUtil;
import com.example.bluecity.ThreadToast;
import com.example.bluecity.Utils;
import com.example.bluecity.databinding.FragmentAddressBinding;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddressFragment extends Fragment {

    private AddressViewModel addressViewModel;
    FragmentAddressBinding binding;

    public AddressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        //实例化ViewModel，和Activity绑定
        addressViewModel = new ViewModelProvider(getActivity()).get(AddressViewModel.class);
        //实例化binding，和布局绑定
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_address, container, false);
        binding.setData(addressViewModel);
        //委托activity管理生命周期
        binding.setLifecycleOwner(getActivity());

        NavController controller;

        Boolean flag = addressViewModel.getIsFromBuilding().getValue();     //是否从building fragment跳转回来
        if (flag) { //是
            addressViewModel.getIsFromBuilding().setValue(false);
            String community = addressViewModel.getCommunity().getValue();
            String building = addressViewModel.getBuilding().getValue();
            binding.tvDistrict.setText(community + "  " + building);        //显示选择的社区楼号
            addressViewModel.finalCommunity.setValue(community);
            addressViewModel.finalBuilding.setValue(building);
        } else {    //不是
            SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(getContext(), "userInfo");
            String community = sharedPreferencesUtil.loadString("community");
            String building = sharedPreferencesUtil.loadString("building");
            String roomId = sharedPreferencesUtil.loadString("roomId");
            if (community != null && building != null & roomId != null) {
                binding.tvDistrict.setText(community + "  " + building);
                binding.etRoom.setText(roomId);
            }
        }

        binding.tvDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_addressFragment_to_provinceFragment);
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取填写的信息
                String community = addressViewModel.getFinalCommunity().getValue();
                String building = addressViewModel.getFinalBuilding().getValue();
                String roomId = binding.etRoom.getText().toString();
                if (!TextUtils.isEmpty(community) && !TextUtils.isEmpty(building) && !TextUtils.isEmpty(roomId)) {
//                    controller = Navigation.findNavController(v);
                    new Thread(new AddressThread(community, building, roomId)).start();
                } else {
                    Toast.makeText(getActivity(), "请先填写完整信息", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //返回binding根节点
        return binding.getRoot();
    }

    class AddressThread extends Thread {

        private int resultCode;
        private String mCommunity, mBuilding, mRoomId;
        private HttpUtils addressHttpUtil;

        AddressThread(String community, String building, String roomId) {
            this.mCommunity = community;
            this.mBuilding = building;
            this.mRoomId = roomId;
        }

        @Override
        public void run() {
//            super.run();
            SharedPreferences shp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String mPhoneNum = shp.getString("phoneNum", null);

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("service", "client.person.setAddress");
                jsonObject.put("phoneNum", mPhoneNum);
                jsonObject.put("community", mCommunity);
                jsonObject.put("building", mBuilding);
                jsonObject.put("roomId", mRoomId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String result = addressHttpUtil.sendJsonPost(jsonObject.toString());  //向服务器发送登录请求，并返回结果
            JSONObject jsonObjectRp = null;
            try {
                jsonObjectRp = new JSONObject(result);  //结果格式从string转json
                resultCode = jsonObjectRp.optInt("resultCode");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("resultCode", String.valueOf(resultCode));
            switch (resultCode) {
                case -1:
                    SharedPreferences.Editor editor = shp.edit();
                    editor.putString("community", mCommunity).putString("building", mBuilding).putString("roomId", mRoomId);
                    editor.apply();
                    Utils.sendMessage(handler, SUCCEED);
                    break;
                case 0:
                    Utils.sendMessage(handler, FAIL);
                    break;
            }

        }
    }

    private static final int SUCCEED = -1;
    private static final int FAIL = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    NavController controller = Navigation.findNavController(binding.btnSave);
                    controller.navigate(R.id.action_addressFragment_to_infoFragment);
                    break;
                case 0:
                    Utils.showToast("请检查网络连接");
                    break;
            }
        }
    };

}
