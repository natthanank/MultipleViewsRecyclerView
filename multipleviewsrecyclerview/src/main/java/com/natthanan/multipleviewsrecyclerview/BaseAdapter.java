package com.natthanan.multipleviewsrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class BaseAdapter extends RecyclerView.Adapter{

    private static List<ViewDataModel> viewDataModels;
    private RecyclerView recyclerView;
    private boolean isDrag=false;
    private ItemTouchHelper itemTouchHelper;
    private static List<ArrayList<ViewDataModel>> groupList;

    public BaseAdapter() {

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;ViewDataModel.setRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        for (List<ViewDataModel> viewDataModelGroup: groupList) {
//            for (int i = 0; i < viewDataModelGroup.size(); i++) {
//                if (viewDataModelGroup.get(i).getViewTypes() == viewType) {
//                    return viewDataModelGroup.get(i).getBaseViewHolderClass().createViewHolder(viewDataModelGroup.get(i), viewDataModelGroup.get(i).getBaseViewHolderClass().getLayout(), recyclerView);
//                }
//            }
//        }
//        return null;

        for (int i = 0; i < viewDataModels.size(); i++) {
            if (viewDataModels.get(i).getViewTypes() == viewType) {
                return viewDataModels.get(i).getBaseViewHolderClass().createViewHolder(viewDataModels.get(i), viewDataModels.get(i).getBaseViewHolderClass().getLayout(), recyclerView);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        for (List<ViewDataModel> group : getGroupList()) {
//            if (position < group.size()) {
//                group.get(position).getBaseViewHolderClass().getClass().cast(holder).bind(group.get(position).getModel(), group.get(position).getTag());
//                return;
//            }
//            position -= group.size();
//        }
        try {
            viewDataModels.get(position).getBaseViewHolderClass().getClass().cast(holder).bind(viewDataModels.get(position).getModel(), viewDataModels.get(position).getTag());
        } catch (Exception e) {
            holder = viewDataModels.get(position).getBaseViewHolderClass().getViewHolder();
            System.out.println("new holder class = " + holder.getClass().getSimpleName());
            viewDataModels.get(position).getBaseViewHolderClass().getClass().cast(holder).bind(viewDataModels.get(position).getModel(), viewDataModels.get(position).getTag());

        }
    }

    @Override
    public int getItemCount() {
//        int size = 0;
//        for (List<ViewDataModel> viewDataModelGroup: groupList) {
//            size += viewDataModelGroup.size();
//        }
//        return size;

        return viewDataModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return ((ViewDataModel) getViewDataModels().get(position)).getViewTypes();
//        for (List<ViewDataModel> group : getGroupList()) {
//            if (position < group.size()) {
//                return group.get(position).getViewTypes();
//            }
//            position -= group.size();
//        }
//        return super.getItemViewType(position);
    }

    public static void setViewDataModels(List mViewDataModels) {
        viewDataModels = mViewDataModels;

    }
    public static List getViewDataModels() {
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
