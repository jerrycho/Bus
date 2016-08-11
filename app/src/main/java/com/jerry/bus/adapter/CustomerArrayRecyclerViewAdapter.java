package com.jerry.bus.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jerry.bus.BusApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class CustomerArrayRecyclerViewAdapter<T> extends RecyclerView.Adapter<CustomerArrayRecyclerViewAdapter.ViewHolder> {

    protected Context context;
    protected ArrayList<T> list = new ArrayList<>();
    protected BusApplication app;

    protected RecyclerItemClickListener recyclerItemClickListener;

    public CustomerArrayRecyclerViewAdapter(Context context, ArrayList<T> al) {
        this.context = context;
        this.list.addAll(al);
        this.app = (BusApplication)context.getApplicationContext();
    }

    public RecyclerItemClickListener getRecyclerItemClickListener() {
        return recyclerItemClickListener;
    }

    public void setRecyclerItemClickListener(RecyclerItemClickListener recyclerItemClickListener) {
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(getLayoutId(), parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);

        contactView.setTag(viewHolder);
        return viewHolder;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(ArrayList<T> collection) {
//        synchronized (list) {
            list.addAll(collection);
//        }
        notifyDataSetChanged();
    }

    public void addOne(T t) {
        synchronized (list) {
            list.add(t);
        }
        notifyDataSetChanged();
    }

    public void assignRecord(ArrayList<T> al){
        list.clear();
        addAll(al);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final View view = holder.baseView;
        if (view!=null){
            if (recyclerItemClickListener!=null){
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerItemClickListener.onItemClick(view, position);
                    }
                });

                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        recyclerItemClickListener.onItemLongClick(view, position);
                        return true;
                    }
                });
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View baseView;
        protected Map<Integer, View> viewMap;

        public ViewHolder(final View baseView) {
            super(baseView);
            this.baseView = baseView;
            /*
            if (recyclerItemClickListener!=null){
                baseView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerItemClickListener.onItemClick(baseView, getAdapterPosition());
                    }
                });

                baseView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        recyclerItemClickListener.onItemLongClick(baseView, getAdapterPosition());
                        return true;
                    }
                });
            }
            */
            viewMap = new HashMap<Integer, View>();
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T getView(Class<T> clazz, Integer id) {
            T t = (T) viewMap.get(id);
            if (t == null) {
                t = (T) baseView.findViewById(id);
                if (t != null) {
                    viewMap.put(id, t);
                }
            }
            return t;
        }

        public LinearLayout getLinearLayout(Integer id) {
            return getView(LinearLayout.class, id);
        }

        public TextView getTextView(Integer id) {
            return getView(TextView.class, id);
        }

        public ImageView getImageView(Integer id) {
            return getView(ImageView.class, id);
        }

        public ImageButton getImageButton(Integer id) {
            return getView(ImageButton.class, id);
        }

        public EditText getEditText(Integer id) {
            return getView(EditText.class, id);
        }
    }

    protected abstract int getLayoutId();
}


