package com.natthanan.multipleviewsrecyclerview;

import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
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
        this.recyclerView = recyclerView;
        ViewDataModel.setRecyclerView(recyclerView);
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

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (isDrag()) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Drag.isDrag = true;
                    Swipe.isSwiped = false;

                    if (((ViewDataModel) getViewDataModels().get(position)).isParent() == true) {
                        Drag.parentDragType = Drag.PARENT_AND_GROUP;
                        Snackbar.make(recyclerView, Drag.parentDragType, Snackbar.LENGTH_SHORT).show();

                        new CountDownTimer(2500, 2500) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                                if (!Drag.isMoving && Drag.isDrag) {
                                    Drag.parentDragType = Drag.PARENT_ONLY;
                                    Snackbar.make(recyclerView, Drag.parentDragType, Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        }.start();
                    }
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
        return ((ViewDataModel) getViewDataModels().get(position)).getViewTypes();
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
