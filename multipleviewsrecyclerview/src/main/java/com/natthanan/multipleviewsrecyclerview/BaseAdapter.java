package com.natthanan.multipleviewsrecyclerview;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;

import com.natthanan.multipleviewsrecyclerview.intf.iCallback;

import java.util.List;


public class BaseAdapter extends RecyclerView.Adapter{

    private iCallback callback;

    private List<ViewDataModel> viewDataModels;
    private RecyclerView recyclerView;
    private boolean isDrag=false;
    private ItemTouchHelper itemTouchHelper;

    public BaseAdapter(List<ViewDataModel> viewDataModels, iCallback callBack) {
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

    protected void onBind(final RecyclerView.ViewHolder holder, Object data, int viewType, String tag) {
        for (int i = 0; i < viewDataModels.size(); i++) {
            if (viewDataModels.get(i).getViewTypes() == viewType) {
                if (isDrag()) {
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            itemTouchHelper.startDrag(holder);
                            return true;
                        }
                    });
                }
                viewDataModels.get(i).getBaseViewHolderClass().getClass().cast(holder).bind(data, tag);
                break;
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        onBind(holder, viewDataModels.get(position).getModel(), viewDataModels.get(position).getViewTypes(), viewDataModels.get(position).getTag());

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

    public boolean isDrag() {
        return isDrag;
    }

    public void setDrag(boolean drag) {
        isDrag = drag;
    }

    public ItemTouchHelper getItemTouchHelper() {
        return itemTouchHelper;
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper;
    }
}
