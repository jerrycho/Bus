package com.jerry.bus.fragment;

import com.jerry.bus.domain.BackEvent;
import com.jerry.bus.domain.OutEvent;

import org.greenrobot.eventbus.Subscribe;

public class BackTabFragment extends MyFragment {

    public String getType(){
        return "back";
    }

    @Subscribe
    public void onEvent(BackEvent event) {
        addList(event.param);
    };


}
