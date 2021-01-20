package com.example.bluecity.access;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.bluecity.HttpUtils;
import com.example.bluecity.MyApplication;
import com.example.bluecity.R;
import com.example.bluecity.SharedPreferencesUtil;
import com.example.bluecity.ThreadToast;
import com.example.bluecity.enity.Record;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.example.bluecity.HttpUtils.sendJsonPost;

public class RecordFragment extends Fragment {
    List<Record> recordList = new ArrayList<>();
    RecyclerView rvRecord;
    RecordAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new GetRecordListThread()).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        rvRecord = view.findViewById(R.id.rv_record);
        final ImageView ivScene = view.findViewById(R.id.iv_scene);
        rvRecord.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new RecordAdapter(this, recordList);
        adapter.setOnItemClickListener(new RecordAdapter.OnItemClickListener() {
            @Override
            public void onclick(int position) {
                Glide.with(MyApplication.context).load(recordList.get(position).getScene()).into(ivScene);
                ivScene.setVisibility(View.VISIBLE);
            }
        });
        rvRecord.setAdapter(adapter);
        ivScene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivScene.setVisibility(View.GONE);
            }
        });
        return view;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle data;
            switch (msg.what) {
                case 0:
                    Log.i("recordList", recordList.toString());
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    class GetRecordListThread implements Runnable {
        @Override
        public void run() {
            SharedPreferencesUtil spUtil = new SharedPreferencesUtil(MyApplication.context, "userInfo");
            String phoneNum = spUtil.loadString("phoneNum");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("service", "door.attendance.getRecordList");
                jsonObject.put("phoneNum", phoneNum);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String result = sendJsonPost(jsonObject.toString());
            JSONObject jsonObjectRp = null;
            try {
                jsonObjectRp = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int resultCode = jsonObjectRp.optInt("resultCode");
            switch (resultCode) {
                case 0:
                    ThreadToast.toast(MyApplication.context, "请检查网络");
                    break;
                case -1:
                    JSONArray recordInfos = null;
                    try {
                        recordInfos = (JSONArray) jsonObjectRp.get("recordInfos");
                        for (int i = 0; i < recordInfos.length(); i++) {
                            String recordInfo = (String) recordInfos.get(i);
                            String[] infos = recordInfo.split(",");
                            String time = infos[0];
                            String community = infos[1];
                            String building = infos[2];
                            String face = infos[3];
                            byte[] bytes = Base64.decode(face, Base64.NO_WRAP);
                            Record record = new Record(time, community, building, bytes);
                            recordList.add(record);
                        }
                        Message msg = handler.obtainMessage();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    }
}