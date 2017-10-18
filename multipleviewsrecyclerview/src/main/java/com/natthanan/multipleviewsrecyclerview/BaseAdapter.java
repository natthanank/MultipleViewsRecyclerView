package com.natthanan.multipleviewsrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;

import com.natthanan.multipleviewsrecyclerview.exception.NullViewDataModelException;

import java.util.ArrayList;
import java.util.List;


public class BaseAdapter extends RecyclerView.Adapter{

    private static List<ViewDataModel> viewDataModels;
    private RecyclerView recyclerView;
    private boolean isDrag=false;
    private ItemTouchHelper itemTouchHelper;
    private static List<ArrayList<ViewDataModel>> groupList;
    public static boolean isInitialize = false;

    public BaseAdapter() {
        viewDataModels = new ArrayList<>();
        groupList = new ArrayList<>();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        ViewDataModel.setRecyclerView(recyclerView);
        isInitialize = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            for (int i = 0; i < viewDataModels.size(); i++) {
                if (viewDataModels.get(i).getViewTypes() == viewType) {
                    return viewDataModels.get(i).getBaseViewHolderClass().createViewHolder(viewDataModels.get(i), viewDataModels.get(i).getBaseViewHolderClass().getLayout(), recyclerView);
                }
            }
        } catch (NullPointerException e) {
            throw new NullViewDataModelException("ViewDataModel is null. Check parameter you pass to the ViewDataModel constructor.");
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (isDrag()) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Drag.isDrag = true;
                    Swipe.isSwiped = false;
                    return true;
                }
            });
        }
        viewDataModels.get(position).getBaseViewHolderClass().getClass().cast(holder).bind(viewDataModels.get(position).getModel(), viewDataModels.get(position).getTag());

    }

    @Override
    public int getItemCount() {

        return viewDataModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (getViewDataModels().get(position)).getViewTypes();
    }

    public static void setViewDataModels(List mViewDataModels) {
        viewDataModels = mViewDataModels;
    }
    public static List<ViewDataModel> getViewDataModels() {
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

    public static List<ArrayList<ViewDataModel>> getGroupList() {
        if (groupList == null) {
            groupList = new ArrayList<>();
        }
        return groupList;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return viewDataModels.get(position).getStableID();
    }
}
