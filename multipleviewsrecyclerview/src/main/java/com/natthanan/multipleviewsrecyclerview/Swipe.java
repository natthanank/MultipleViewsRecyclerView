package com.natthanan.multipleviewsrecyclerview;

import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.natthanan.multipleviewsrecyclerview.exception.NullRecyclerViewException;
import com.natthanan.multipleviewsrecyclerview.util.GroupUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by DELL on 29/08/2560.
 */

public abstract class Swipe extends ItemTouchHelper.Callback {
    public static boolean isSwiped = true;
    public static final String ACTION_REMOVE = "remove";
    public static final String ACTION_UPDATE = "update";
    public String action;
    private BaseAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isSwipeEnabled;
    private int swipeFlags;
    private ItemTouchHelper itemTouchHelper;
    private int movementFlags;
    private ViewDataModel viewDataModel;
    private ViewDataModel oldViewDataModel;
    private ArrayList<ViewDataModel> oldGroup;
    private boolean isUndo = false;
    private int groupPosition;
    private int undoDelay = 3500;
    private boolean isGroupRemove;
    private ArrayList<ViewDataModel> group;

    public Swipe(RecyclerView recyclerView, int movementFlags) {
        this.recyclerView = recyclerView;
        try {
            adapter = (BaseAdapter) recyclerView.getAdapter();
        } catch (NullPointerException e) {
            throw new NullRecyclerViewException();
        }

        layoutManager = recyclerView.getLayoutManager();
        isSwipeEnabled = true;
        this.movementFlags = movementFlags;

        itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (layoutManager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            } else {
                swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            }
        }
        return makeMovementFlags(0, movementFlags & swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    protected abstract void onChildSwiped(int position, ViewDataModel viewDataModel, int direction);

    protected abstract void onParentSwiped(int position, ArrayList<ViewDataModel> group, int direction);

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int direction) {
        Drag.isDrag = false;
        isUndo = false;
        final int position = viewHolder.getAdapterPosition();
        viewDataModel = BaseAdapter.getViewDataModels().get(viewHolder.getAdapterPosition());
        try {
            setOldViewDataModel((ViewDataModel) viewDataModel.clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        group = BaseAdapter.getGroupList().get(GroupUtil.getGroupByPosition(position));
        try {
            setOldGroup((ArrayList<ViewDataModel>) GroupUtil.cloneGroup(group));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (viewDataModel.isParent()) {
            onParentSwiped(position, group, direction);
        } else {
            onChildSwiped(position, viewDataModel, direction);
        }
        new CountDownTimer(getUndoDelay(), getUndoDelay()) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (isUndo == false) {
                    if (Objects.equals(action, ACTION_REMOVE)) {
                        if (isGroupRemove) {
                            BaseAdapter.getGroupList().remove(group);
                        } else {
                            group.remove(viewDataModel);
                        }
                    }
                    if (viewDataModel.isParent()) {
                        afterParentSwiped(position, group, direction);
                    } else {
                        afterChildSwiped(position, viewDataModel, direction);
                    }
                }
            }
        }.start();

    }

    protected abstract void afterChildSwiped(int position, ViewDataModel viewDataModel, int direction);

    protected abstract void afterParentSwiped(int position, ArrayList<ViewDataModel> group, int direction);



    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        viewHolder.itemView.setAlpha(1);
        viewHolder.itemView.setTranslationX(dX);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        Drag.isDrag = true;
    }

    protected void dontDoAnything(int position) {
        adapter.notifyItemChanged(position);
    }

    protected void undoUpdate(int position) {
        BaseAdapter.getViewDataModels().set(position, oldViewDataModel);
        adapter.notifyItemChanged(position);
        int ingroup = GroupUtil.getPositionInGroup(position);
        BaseAdapter.getGroupList().get(groupPosition).set(ingroup, oldViewDataModel);
        getRecyclerView().scrollToPosition(position);
        viewDataModel = null;
        isUndo = true;
    }

    protected void undoRemove(int position) {
        if (oldViewDataModel.isParent()) {
            for (int i = 0; i < oldGroup.size(); i++) {
                BaseAdapter.getViewDataModels().add(position + i, oldGroup.get(i));
                adapter.notifyItemInserted(position + i);
            }
            getRecyclerView().scrollToPosition(position);
        } else {
            BaseAdapter.getViewDataModels().add(position, oldViewDataModel);
            adapter.notifyItemInserted(position);
            getRecyclerView().scrollToPosition(position);
        }
        viewDataModel = null;
        isUndo = true;
    }

    protected void remove(int position, ViewDataModel viewDataModel) {
        action = ACTION_REMOVE;
        isGroupRemove = false;
        BaseAdapter.getViewDataModels().remove(viewDataModel);
        adapter.notifyItemRemoved(position);
    }

    protected void remove(int parentPosition, List<ViewDataModel> group) {
        action = ACTION_REMOVE;
        isGroupRemove = true;
        for (int i = 0; i < group.size(); i++) {
            BaseAdapter.getViewDataModels().remove(parentPosition);
            adapter.notifyItemRemoved(parentPosition);
        }
    }

    protected void update(int position) {
        action = ACTION_UPDATE;
        groupPosition = GroupUtil.getGroupByPosition(position);
        BaseAdapter.getViewDataModels().set(position, viewDataModel);
        adapter.notifyItemChanged(position);
    }


    @Override
    public boolean isItemViewSwipeEnabled() {
        return isSwipeEnabled();
    }


    public boolean isSwipeEnabled() {
        return isSwipeEnabled && isSwiped;
    }

    public void setSwipeEnabled(boolean swipeEnabled) {
        isSwipeEnabled = swipeEnabled;
    }


    public ItemTouchHelper getItemTouchHelper() {
        return itemTouchHelper;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public BaseAdapter getAdapter() {
        return adapter;
    }

    public ViewDataModel getOldViewDataModel() {
        return oldViewDataModel;
    }

    public void setOldViewDataModel(ViewDataModel oldViewDataModel) {
        this.oldViewDataModel = oldViewDataModel;
    }

    public int getUndoDelay() {
        return undoDelay;
    }

    public void setUndoDelay(int undoDelay) {
        this.undoDelay = undoDelay;
    }

    public ArrayList<ViewDataModel> getOldGroup() {
        return oldGroup;
    }

    public void setOldGroup(ArrayList<ViewDataModel> oldGroup) {
        this.oldGroup = oldGroup;
    }


    public BaseViewHolder getViewHolder(int position) {
        return ((BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(position));
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }
}
