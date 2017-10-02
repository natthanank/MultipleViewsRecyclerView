package com.natthanan.multipleviewsrecyclerview;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by DELL on 09/09/2560.
 */

public abstract class Drag extends ItemTouchHelper.Callback {
    private BaseAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isDragEnabled;
    private int dragFlags;
    private ItemTouchHelper itemTouchHelper;
    private int dragFrom = -1;
    private int dragTo = -1;
    public static boolean isDrag = true;

    public Drag(RecyclerView recyclerView) {
        layoutManager = recyclerView.getLayoutManager();
        adapter = (BaseAdapter) recyclerView.getAdapter();
        adapter.setDrag(true);
        isDragEnabled = true;

        itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter.setItemTouchHelper(itemTouchHelper);

    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        if (layoutManager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            } else {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            }
        } else if (layoutManager instanceof GridLayoutManager || layoutManager instanceof StaggeredGridLayoutManager) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        }
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        isDrag = true;
        if(dragFrom == -1) {
            dragFrom =  fromPosition;
        }
        dragTo = toPosition;

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(adapter.getViewDataModels(), i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(adapter.getViewDataModels(), i, i - 1);
            }
        }
        adapter.notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    private boolean isGropSwapped(int fromPosition, int toPosition) {
        int groupFromPosition = -1;
        int groupToPosition = -1;
        boolean isGroupSwap = false;
        for (int i = 0; i < BaseAdapter.getGroupList().size(); i++) {
            if (isGroupSwap) {
                break;
            }
            ArrayList<ViewDataModel> group = BaseAdapter.getGroupList().get(i);
            if (fromPosition < group.size()) {
                if (group.get(0).getGroupName().equals(group.get(fromPosition).getGroupName())) {
                    for (int j = 0; j < group.size(); j++) {
                        if (group.get(j).isParent()) {
                            groupFromPosition = i;
                            isGroupSwap = true;
                            break;
                        }
                    }
                }
            } else {
                fromPosition -= group.size();
            }
        }
        for (int i = 0; i < BaseAdapter.getGroupList().size(); i++) {
            ArrayList<ViewDataModel> group = BaseAdapter.getGroupList().get(i);
            if (toPosition >= group.size()) {
                toPosition -= group.size();
            } else {
                groupToPosition = i;
                break;
            }
        }
        if (groupFromPosition < groupToPosition) {
            for (int i = groupFromPosition; i < groupToPosition; i++) {
                Collections.swap(BaseAdapter.getGroupList(), i, i + 1);
            }
        } else {
            for (int i = groupFromPosition; i > groupToPosition; i--) {
                Collections.swap(BaseAdapter.getGroupList(), i, i - 1);
            }
        }

        return isGroupSwap;
    }

    private void reallyMoved(int fromPosition, int toPosition) {

        if (isGropSwapped(fromPosition, toPosition)) {

            List<ViewDataModel> list = new ArrayList<>();
            for (ArrayList<ViewDataModel> array : BaseAdapter.getGroupList()) {
                list.addAll(array);
            }
            adapter.setViewDataModels(list);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if(dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
            reallyMoved(dragFrom, dragTo);
        }

        dragFrom = dragTo = -1;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return isDragEnabled();
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (isCurrentlyActive) {
            if (isDrag) {
                float alpha = 0.5f - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            }
        } else {
            viewHolder.itemView.setAlpha(1);
            viewHolder.itemView.setTranslationX(dX);
        }
    }

    public abstract void onItemMove(int fromPosition, int toPosition, ViewDataModel fromViewDataModel, ViewDataModel toViewDataModel);

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }


    public boolean isDragEnabled() {
        return isDragEnabled;
    }

    public void setDragEnabled(boolean dragEnabled) {
        isDragEnabled = dragEnabled;
    }

    public ItemTouchHelper getItemTouchHelper() {
        return itemTouchHelper;
    }

    public BaseAdapter getAdapter() {
        return adapter;
    }

}
