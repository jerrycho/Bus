package com.jerry.bus.fragment;

import com.jerry.bus.domain.ComeEvent;
import com.jerry.bus.domain.OutEvent;

import org.greenrobot.eventbus.Subscribe;

public class OutTabFragment extends MyFragment {

    public String getType(){
        return "out";
    }

    @Subscribe
    public void onEvent(OutEvent event) {
        addList(event.param);
    };


}
