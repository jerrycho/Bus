package com.jerry.bus.adapter;


import android.content.Context;
import android.view.View;
import android.widget.ImageButton;

import com.jerry.bus.R;
import com.jerry.bus.domain.MyBus;

import java.util.ArrayList;

public class MyBusAdatper extends CustomerArrayRecyclerViewAdapter<MyBus>{

    public MyBusAdatper(Context context, ArrayList<MyBus> al) {
        super(context, al);
    }

    MyBusAdatperListner listner;

    public interface MyBusAdatperListner {
        void onRefreshClick(MyBus contact, int position);
    }

    public void setMyBusAdatperListner(MyBusAdatperListner listner) {
        this.listner = listner;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.row_routestop;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        final MyBus myBus = getItem(position);
        holder.getTextView(R.id.name).setText(myBus.name);
        if (myBus.coming!=null) {
            if (myBus.coming.getResponse()!=null && myBus.coming.getResponse().length>0)
                holder.getTextView(R.id.nearTime1).setText(myBus.coming.getResponse()[0].getT());
            else
                holder.getTextView(R.id.nearTime1).setText("--");
            if (myBus.coming.getResponse()!=null && myBus.coming.getResponse().length>1)
                holder.getTextView(R.id.nearTime2).setText(myBus.coming.getResponse()[1].getT());
            else
                holder.getTextView(R.id.nearTime2).setText("--");
            if (myBus.coming.getResponse()!=null && myBus.coming.getResponse().length>2)
                holder.getTextView(R.id.nearTime3).setText(myBus.coming.getResponse()[2].getT());
            else
                holder.getTextView(R.id.nearTime3).setText("--");
        }
        holder.getView(ImageButton.class, R.id.btnRefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listner!=null){
                    listner.onRefreshClick(myBus, position);
                }
            }
        });
    }
}
