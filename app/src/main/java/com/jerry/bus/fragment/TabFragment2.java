package com.jerry.bus.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jerry.bus.utils.GsonUtil;
import com.jerry.bus.R;
import com.jerry.bus.adapter.MyBusAdatper;
import com.jerry.bus.domain.Coming;
import com.jerry.bus.domain.MyBus;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by jerry.cho on 5/8/2016.
 */
public class TabFragment2 extends Fragment {

    ArrayList<MyBus> alBackBus;

    RecyclerView recyclerView;

    MyBusAdatper myBusAdatper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_1, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        alBackBus =  new ArrayList<MyBus>();

        MyBus myBus = new MyBus();
        myBus.callURL = "http://etav2.kmb.hk?action=geteta&lang=tc&route=99&bound=1&stop=WU01T10500&stop_seq=5";
        myBus.name = "99(烏溪沙站/WU KAI SHA STATION)";
        alBackBus.add(myBus);

        myBus = new MyBus();
        myBus.callURL = "http://etav2.kmb.hk?action=geteta&lang=tc&route=99&bound=1&stop=SA03S10500&stop_seq=2";
        myBus.name = "99(海柏 Bayshore)";
        alBackBus.add(myBus);

        myBus = new MyBus();
        myBus.callURL = "http://etav2.kmb.hk?action=geteta&lang=tc&route=299X&bound=1&stop=SH05T12000&stop_seq=0";
        myBus.name = "299(Shatin Station)";
        alBackBus.add(myBus);

//        MyBus myBus = new MyBus();
//        myBus.callURL = "http://etav2.kmb.hk?action=geteta&lang=tc&route=680&bound=2&stop=KI04E11100&stop_seq=8";
//        myBus.name = "680(七海)";
//        alBackBus.add(myBus);
//
//        myBus = new MyBus();
//        myBus.callURL = "http://etav2.kmb.hk/?action=geteta&lang=tc&route=299X&bound=2&stop=SA03N13000&stop_seq=9";
//        myBus.name = "680(電廠)";
//        alBackBus.add(myBus);
//
//        myBus = new MyBus();
//        myBus.callURL = "http://etav2.kmb.hk/?action=geteta&lang=tc&route=681&bound=1&stop=CA08E14500&stop_seq=10";
//        myBus.name = "681(維園)";
//        alBackBus.add(myBus);

        myBusAdatper = new MyBusAdatper(getActivity(),alBackBus);
        myBusAdatper.setMyBusAdatperListner(new MyBusAdatper.MyBusAdatperListner() {
            @Override
            public void onRefreshClick(final MyBus item, int position) {
                OkHttpUtils
                        .get()
                        .url(item.callURL)
//                        .addParams("username", "hyman")
//                        .addParams("password", "123")
                        .build()
                        .execute(new StringCallback()
                        {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Coming coming = GsonUtil.fromJson(response, Coming.class);
                                if (coming!=null){
                                    item.coming = coming;
                                    myBusAdatper.notifyDataSetChanged();
                                }
                            }
                        });
            }
        });
        recyclerView.setAdapter(myBusAdatper);

    }

}
