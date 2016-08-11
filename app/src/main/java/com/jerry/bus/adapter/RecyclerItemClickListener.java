package com.jerry.bus.adapter;

import android.view.View;

public interface RecyclerItemClickListener {
    void onItemClick(View view, int position);
    void onItemLongClick(View view, int position);
}

