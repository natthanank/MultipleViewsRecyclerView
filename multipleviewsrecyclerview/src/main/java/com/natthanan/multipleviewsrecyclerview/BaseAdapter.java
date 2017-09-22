package com.natthanan.multipleviewsrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;


public class BaseAdapter extends RecyclerView.Adapter{

    private List<ViewDataModel> viewDataModels;
    private RecyclerView recyclerView;

    public BaseAdapter(List<ViewDataModel> viewDataModels) {
        this.viewDataModels = viewDataModels;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;ViewDataModel.setRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        for (int i = 0; i < viewDataModels.size(); i++) {
            if (viewDataModels.get(i).getViewTypes() == viewType) {
                return viewDataModels.get(i).getBaseViewHolderClass().createViewHolder(viewDataModels.get(i), viewDataModels.get(i).getBaseViewHolderClass().getLayout(), recyclerView);
            }
        }
        return null;
    }

    protected void onBind(RecyclerView.ViewHolder holder, Object data, int viewType) {
        for (int i = 0; i < viewDataModels.size(); i++) {
            if (viewDataModels.get(i).getViewTypes() == viewType) {
                viewDataModels.get(i).getBaseViewHolderClass().getClass().cast(holder).bind(data);
                break;
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        onBind(holder, viewDataModels.get(position).getModel(), viewDataModels.get(position).getViewTypes());

    }

    @Override
    public int getItemCount() {
        return viewDataModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return viewDataModels.get(position).getViewTypes();
    }

    public List getViewDataModels() {
        return viewDataModels;
    }

}
