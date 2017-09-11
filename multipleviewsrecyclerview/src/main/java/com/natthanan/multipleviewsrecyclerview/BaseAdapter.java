package com.natthanan.multipleviewsrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;


public class BaseAdapter extends RecyclerView.Adapter implements ItemTouchHelperAdapter{

    private List<ViewDataModel> dataSet;

    public BaseAdapter(List<ViewDataModel> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        for (int i = 0; i < dataSet.size(); i++) {
            if (dataSet.get(i).getViewTypes() == viewType) {
                return dataSet.get(i).getBaseViewHolderClass().createViewHolder(dataSet.get(i), dataSet.get(i).getBaseViewHolderClass().getLayout());
            }
        }
        return null;
    }

    protected void onBind(RecyclerView.ViewHolder holder, Object data, int viewType) {
        for (int i = 0; i < dataSet.size(); i++) {
            if (dataSet.get(i).getViewTypes() == viewType) {
                dataSet.get(i).getBaseViewHolderClass().getClass().cast(holder).bind(data);
                break;
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        onBind(holder, dataSet.get(position).getModel(), dataSet.get(position).getViewTypes());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position).getViewTypes();
    }

    @Override
    public void onItemDismiss(int position) {
        dataSet.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(dataSet, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(dataSet, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }
}
