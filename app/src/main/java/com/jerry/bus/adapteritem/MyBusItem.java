package com.jerry.bus.adapteritem;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jerry.bus.R;
import com.jerry.bus.domain.MyBus;
import com.mikepenz.fastadapter.items.AbstractItem;

/**
 * Created by jerry.cho on 5/8/2016.
 */
public class MyBusItem  extends AbstractItem<MyBusItem, MyBusItem.ViewHolder> {

    public MyBus myBus;

    @Override
    public int getType() {
        return R.id.name;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.row_routestop;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(ViewHolder viewHolder) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder);

        //bind our data
        //set the text for the name
        //viewHolder.name.setText(name);
        //set the text for the description or hide
        //viewHolder.description.setText(description);
        viewHolder.name.setText(myBus.name);
        if (myBus.coming!=null) {
            if (myBus.coming.getResponse()!=null && myBus.coming.getResponse().length>0)
                viewHolder.nearTime1.setText(myBus.coming.getResponse()[0].getT());
            if (myBus.coming.getResponse()!=null && myBus.coming.getResponse().length>1)
                viewHolder.nearTime2.setText(myBus.coming.getResponse()[1].getT());
            if (myBus.coming.getResponse()!=null && myBus.coming.getResponse().length>2)
                viewHolder.nearTime3.setText(myBus.coming.getResponse()[2].getT());
        }

    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView nearTime1,nearTime2,nearTime3;
        protected ImageButton btnRefresh;

        public ViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.name);
            this.nearTime1 = (TextView) view.findViewById(R.id.nearTime1);
            this.nearTime2 = (TextView) view.findViewById(R.id.nearTime2);
            this.nearTime3 = (TextView) view.findViewById(R.id.nearTime3);
            this.btnRefresh = (ImageButton) view.findViewById(R.id.btnRefresh);
        }
    }

}
