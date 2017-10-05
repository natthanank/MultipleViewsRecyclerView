package com.natthanan.multipleviewsrecyclerview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 29/08/2560.
 */

public abstract class Swipe extends ItemTouchHelper.Callback{
    public static final int ACTION_REMOVE = 0;
    public static final int ACTION_UPDATE = 1;

    private final BaseAdapter adapter;
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
    private int action;
    private int groupPosition;
    private int duration = 3500;
    public static boolean isSwiped = true;

    public Swipe(RecyclerView recyclerView, int movementFlags) {
        this.recyclerView = recyclerView;
        adapter = (BaseAdapter) recyclerView.getAdapter();
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

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int direction) {
        Drag.isDrag = false;
        isUndo = false;
        final int position = viewHolder.getAdapterPosition();
        BaseAdapter baseAdapter = (BaseAdapter) recyclerView.getAdapter();
        viewDataModel = (ViewDataModel) BaseAdapter.getViewDataModels().get(viewHolder.getAdapterPosition());
        try {
            setOldViewDataModel((ViewDataModel) viewDataModel.clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        final ArrayList<ViewDataModel> group = BaseAdapter.getGroupList().get(getGroupByPosition(position));
        try {
            setOldGroup((ArrayList<ViewDataModel>) cloneGroup(group));
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (direction) {
            case ItemTouchHelper.LEFT:
                onSwipedLeft(position, viewDataModel, group);
                break;
            case ItemTouchHelper.RIGHT:
                onSwipedRight(position, viewDataModel, group);
                break;
            case ItemTouchHelper.UP:
                onSwipeUp(position, viewDataModel, group);
                break;
            case ItemTouchHelper.DOWN:
                onSwipeDown(position, viewDataModel, group);
                break;
            default:
        }
        new CountDownTimer(getDuration(), getDuration()) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (isUndo == false) {
                    onUpdateSwiped(position, viewDataModel, group, action);
                }
            }
        }.start();

    }

    private List<ViewDataModel> cloneGroup(List<ViewDataModel> group) {
        List<ViewDataModel> clonedList = new ArrayList<ViewDataModel>(group.size());
        for (ViewDataModel viewDataModel : group) {
            clonedList.add(new ViewDataModel(viewDataModel));
        }
        return clonedList;
    }

    private int getGroupByPosition(int position) {
        for (int i = 0; i < BaseAdapter.getGroupList().size(); i++) {
            ArrayList<ViewDataModel> group = BaseAdapter.getGroupList().get(i);
            if (position >= group.size()) {
                position -= group.size();
            } else {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        viewHolder.itemView.setAlpha(1);
        viewHolder.itemView.setTranslationX(dX);
    }

    public void undoRemove(int position, ViewDataModel oldViewDataModel, ArrayList<ViewDataModel> oldGroup) {
        if (oldViewDataModel.isParent()) {
            BaseAdapter.getGroupList().add(groupPosition, getOldGroup());
            for (int i = 0; i < oldGroup.size(); i++) {
                BaseAdapter.getViewDataModels().add(position + i, oldGroup.get(i));
                adapter.notifyItemInserted(position + i);
            }
            getRecyclerView().scrollToPosition(position);
        } else {
            BaseAdapter.getViewDataModels().add(position, oldViewDataModel);
            getAdapter().notifyItemInserted(position);
            getRecyclerView().scrollToPosition(position);
        }
        viewDataModel=null;
        isUndo = true;
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        Drag.isDrag = true;
    }

    public void dontDoAnything(int position) {
        getAdapter().notifyItemChanged(position);
    }
    public void undoUpdate(int position, ViewDataModel oldViewDataModel, ArrayList<ViewDataModel> oldGroup) {
        BaseAdapter.getViewDataModels().set(position, oldViewDataModel);
        getAdapter().notifyItemChanged(position);
        int ingroup = getPositionInGroup(position);
        BaseAdapter.getGroupList().get(groupPosition).set(ingroup, oldViewDataModel);
        getRecyclerView().scrollToPosition(position);
        viewDataModel=null;
        isUndo = true;
    }

    public void removeItem(int position, ViewDataModel viewDataModel) {
        action = 0;
        if (viewDataModel.isParent()) {
            groupPosition = getGroupByPosition(position);
            BaseAdapter.getGroupList().remove(groupPosition);
            for (int i = 0; i < oldGroup.size(); i++) {
                BaseAdapter.getViewDataModels().remove(position);
                adapter.notifyItemRemoved(position);
            }
        } else {
            BaseAdapter.getViewDataModels().remove(viewDataModel);
            getAdapter().notifyItemRemoved(position);
        }
    }

    public void updateItem(int position, ViewDataModel viewDataModel) {
        action = 1;
        groupPosition = getGroupByPosition(position);
        BaseAdapter.getViewDataModels().set(position, viewDataModel);
        getAdapter().notifyItemChanged(position);
    }

    public abstract void onSwipedRight(int position, ViewDataModel viewDataModel, List<ViewDataModel> viewDataModels);
    public abstract void onSwipedLeft(int position, ViewDataModel viewDataModel, List<ViewDataModel> viewDataModels);
    public abstract void onSwipeUp(int position, ViewDataModel viewDataModel, List<ViewDataModel> viewDataModels);
    public abstract void onSwipeDown(int position, ViewDataModel viewDataModel, List<ViewDataModel> viewDataModels);
    public abstract void onUpdateSwiped(int position, ViewDataModel viewDataModel, List<ViewDataModel> viewDataModels, int action);

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

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public ArrayList<ViewDataModel> getOldGroup() {
        return oldGroup;
    }

    public void setOldGroup(ArrayList<ViewDataModel> oldGroup) {
        this.oldGroup = oldGroup;
    }

    private int getPositionInGroup(int position) {
        for (List<ViewDataModel> group : BaseAdapter.getGroupList()) {
            if (position >= group.size()) {
                position -= group.size();
            } else {
                return position;
            }
        }
        return -1;
    }
}
