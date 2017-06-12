package com.jerry.bus.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jerry.bus.R;
import com.jerry.bus.adapter.MyBusAdatper;
import com.jerry.bus.adapter.RecyclerItemClickListener;
import com.jerry.bus.domain.Coming;
import com.jerry.bus.domain.MyBus;
import com.jerry.bus.domain.MyBusCallParam;
import com.jerry.bus.utils.GsonUtil;
import com.pixplicity.easyprefs.library.Prefs;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;

/**
 * Created by jerry.cho on 9/8/2016.
 */
public class MyFragment extends Fragment {

    public ArrayList<MyBus> alBus;

    public RecyclerView recyclerView;

    public MyBusAdatper myBusAdatper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_1, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);

        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        alBus = new ArrayList<MyBus>();

        List<MyBusCallParam> list = getMyBusCallParam();
        //List<MyBusCallParam> list = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (MyBusCallParam p : list) {
                MyBus myBus = new MyBus();
                String lang = Prefs.getString("lang", "EN");
                if ("EN".equals(lang)) {
                    myBus.name = p.searchBusNo + "(" + p.busStopNameEN + ")";
                } else {
                    myBus.name = p.searchBusNo + "(" + p.busStopNameTC + ")";
                }
                myBus.myBusCallParam = p;
                alBus.add(myBus);
            }
        }

        myBusAdatper = new MyBusAdatper(getActivity(), alBus);
        myBusAdatper.setRecyclerItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {
                final MyBus myBus = myBusAdatper.getItem(position);
                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("Are you sure you delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyBusCallParam.delete(myBus.myBusCallParam);

                                List<MyBusCallParam> list = getMyBusCallParam();

                                alBus.clear();
                                if (list != null && list.size() > 0) {
                                    for (MyBusCallParam p : list) {
                                        MyBus myBus = new MyBus();
                                        String lang = Prefs.getString("lang", "EN");
                                        if ("EN".equals(lang)) {
                                            myBus.name = p.searchBusNo + "(" + p.busStopNameEN + ")";
                                        } else {
                                            myBus.name = p.searchBusNo + "(" + p.busStopNameTC + ")";
                                        }
                                        myBus.myBusCallParam = p;
                                        alBus.add(myBus);
                                    }
                                }
                                myBusAdatper.assignRecord(alBus);

                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        myBusAdatper.setMyBusAdatperListner(new MyBusAdatper.MyBusAdatperListner() {
            @Override
            public void onRefreshClick(final MyBus item, int position) {
                String lang = Prefs.getString("lang", "EN");
                OkHttpUtils
                        .get()
                        .url("http://etav3.kmb.hk/")
                        .addParams("t", UUID.randomUUID().toString())
                        .addParams("action", "geteta")
                        .addParams("lang", "EN".equals(lang) ? "en" : "tc")
                        .addParams("route", item.myBusCallParam.searchBusNo)
                        .addParams("bound", item.myBusCallParam.bound)
                        .addParams("stop", item.myBusCallParam.stopCode)
                        .addParams("stop_seq", item.myBusCallParam.stopSeq)
                        .build()
                        .connTimeOut(1000)
                        .execute(new StringCallback() {

                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                        "Server error / no internet",
                                        Toast.LENGTH_SHORT);
                                toast.show();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Coming coming = GsonUtil.fromJson(response, Coming.class);
                                if (coming != null) {
                                    item.coming = coming;
                                    myBusAdatper.notifyDataSetChanged();
                                }
                            }
                        });
            }
        });
        recyclerView.setAdapter(myBusAdatper);
    }

    public String getType(){
        return "back";
    }

    public List<MyBusCallParam> getMyBusCallParam() {
        return MyBusCallParam.find(MyBusCallParam.class, "direction = ?", getType() );
    }

    public void addList(MyBusCallParam p){
        MyBus myBus = new MyBus();
        String lang = Prefs.getString("lang", "EN");
        if ("EN".equals(lang)) {
            myBus.name = p.searchBusNo + "(" + p.busStopNameEN + ")";
        } else {
            myBus.name = p.searchBusNo + "(" + p.busStopNameTC + ")";
        }
        myBus.myBusCallParam = p;
        alBus.add(myBus);
        myBusAdatper.addOne(myBus);
        myBusAdatper.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
