package com.example.bluecity.my;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluecity.R;
import com.example.bluecity.SharedPreferencesUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

public class MyFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private MyViewModel mViewModel;
    private View vInfo;
    private RecyclerView mRvMy;
    private ImageView ivPortrait;
    private TextView tvName;

    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/blueCity_portrait.jpg");

    public static MyFragment newInstance() {
        return new MyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_fragment, container, false);
        mRvMy = view.findViewById(R.id.linear_my);
        vInfo = view.findViewById(R.id.v_info);
        ivPortrait = view.findViewById(R.id.iv_portrait);
        tvName = view.findViewById(R.id.tv_name);

        //底部导航栏可见性
        AppCompatActivity mainActivity = (AppCompatActivity) getActivity();
        bottomNavigationView = mainActivity.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.VISIBLE);

        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(getActivity(), "userInfo");
        String name = sharedPreferencesUtil.loadString("name");
        tvName.setText(name);

        mRvMy.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvMy.setAdapter(new MyAdapter(getActivity(), 3, new MyAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                AppCompatActivity mainActivity = (AppCompatActivity) getActivity();
                bottomNavigationView = mainActivity.findViewById(R.id.bottomNavigationView);
                bottomNavigationView.setVisibility(View.GONE);

            }
        }));

        vInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), InfoActivity.class);
//                startActivity(intent);
                AppCompatActivity mainActivity = (AppCompatActivity) getActivity();
                bottomNavigationView = mainActivity.findViewById(R.id.bottomNavigationView);
                bottomNavigationView.setVisibility(View.GONE);
                NavController controller = Navigation.findNavController(getActivity(), R.id.v_info);
                controller.navigate(R.id.action_myFragment_to_infoFragment);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        Uri cropImageUri = Uri.fromFile(fileCropUri);
//        Bitmap bitmap = PhotoUtils.getBitmapfromUri(cropImageUri,getActivity());
//        if (bitmap != null) {
//            ivPortrait.setImageBitmap(bitmap);
//        }
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(getActivity(), "userInfo");
        String portraitBase64 = sharedPreferencesUtil.loadString("portrait");
        if (!TextUtils.isEmpty(portraitBase64)) {
            byte[] bytes = Base64.decode(portraitBase64, Base64.NO_WRAP);
            Bitmap portraitBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            ivPortrait.setImageBitmap(portraitBitmap);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        // TODO: Use the ViewModel
    }

}
