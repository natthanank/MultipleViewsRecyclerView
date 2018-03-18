package com.natthanan.multipleviewsrecyclerview;

import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.natthanan.multipleviewsrecyclerview.exception.NullBaseAdapterException;
import com.natthanan.multipleviewsrecyclerview.exception.NullRecyclerViewException;
import com.natthanan.multipleviewsrecyclerview.util.GroupUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by DELL on 09/09/2560.
 */

public abstract class Drag extends ItemTouchHelper.Callback {

    private BaseAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isLongPressedDragEnabled;
    private int dragFlags;
    private ItemTouchHelper itemTouchHelper;
    private int dragFrom = -1;
    private int dragTo = -1;

    public static boolean isDrag = true;

    public Drag(RecyclerView recyclerView) {
        layoutManager = recyclerView.getLayoutManager();
        try {
            adapter = (BaseAdapter) recyclerView.getAdapter();
            itemTouchHelper = new ItemTouchHelper(this);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        } catch (NullPointerException e) {
            throw new NullRecyclerViewException();
        }
        try {
            adapter.setDrag(isLongPressDragEnabled());
        } catch (NullPointerException e) {
            throw new NullBaseAdapterException();
        }
        isLongPressedDragEnabled = true;
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
        if (dragFrom == -1) {
            dragFrom = fromPosition;
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void reallyMoved(int fromPosition, int toPosition) {
        boolean isFromPositionGreaterThanToPosition = GroupUtil.isFromPositionGreater(fromPosition, toPosition);
        // remove blank group
        GroupUtil.removeBlankGroup();
        if (GroupUtil.isGroupSwapped(fromPosition)) {
            // check togroup position
            GroupUtil.groupToPosition = GroupUtil.getGroupByPosition(toPosition);
            // swap group
            GroupUtil.swapGroup(GroupUtil.groupFromPosition, GroupUtil.groupToPosition);
            // create new dataset and set to adapter
            createNewViewDataModels();
        } else if (toPosition == 0) {

            (BaseAdapter.getViewDataModels().get(toPosition)).setGroupName(Integer.toString(BaseAdapter.getViewDataModels().get(toPosition).hashCode()));
            GroupUtil.createNewGroup(fromPosition);
            createNewViewDataModels();

        } else {
            List<ViewDataModel> fromGroup = GroupUtil.getMatchGroup(BaseAdapter.getViewDataModels().get(toPosition));
            List<ViewDataModel> toGroup = GroupUtil.getMatchGroup(BaseAdapter.getViewDataModels().get(toPosition - 1));
            // set new groupName
            GroupUtil.setNewGroupName(fromPosition, toPosition);

            ViewDataModel viewDataModelTemp = null;



            try {

                    viewDataModelTemp = (ViewDataModel) toGroup.get(toGroup.size() - 1).clone();
                    toGroup.add(viewDataModelTemp);

            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            // change position to position in group
            if (fromGroup.size() == 1) {
                fromPosition = GroupUtil.getPositionInGroup(fromPosition + 1);
            } else fromPosition = GroupUtil.getPositionInGroup(fromPosition);
            toPosition = GroupUtil.getPositionInGroup(toPosition);

            if (Objects.equals(fromGroup, toGroup)) {
                // swap item in group
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(toGroup, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(toGroup, i, i - 1);
                    }
                }
            } else if (isFromPositionGreaterThanToPosition && toPosition != 0){
                // swap across group from bottom to top
                ViewDataModel temp;
                if (fromGroup.size() != 1) {
                    temp = fromGroup.remove(fromPosition + 1);
                } else temp = fromGroup.remove(fromPosition);
                toGroup.add(toPosition, temp);
            } else {
                // swap across group from top to bottom
                ViewDataModel temp = fromGroup.remove(fromPosition);
                toGroup.add(toPosition + 1, temp);

            }
            toGroup.remove(viewDataModelTemp);
            createNewViewDataModels();
        }

        // remove blank group
        GroupUtil.removeBlankGroup();

        onItemDropped(BaseAdapter.getViewDataModels());
    }


    private void createNewViewDataModels() {
        List<ViewDataModel> list = new ArrayList<>();
        for (ArrayList<ViewDataModel> array : BaseAdapter.getGroupList()) {
            list.addAll(array);
        }
        BaseAdapter.setViewDataModels(list);
        adapter.notifyDataSetChanged();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
            reallyMoved(dragFrom, dragTo);
        }

        dragFrom = dragTo = -1;
        Swipe.isSwiped = true;
        isDrag = true;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return isLongPressedDragEnabled();
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

    public abstract void onItemDropped(List<ViewDataModel> viewDataModels);

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    public boolean isLongPressedDragEnabled() {
        return isLongPressedDragEnabled;
    }

    public void setLongPressedDragEnabled(boolean dragEnabled) {
        isLongPressedDragEnabled = dragEnabled;
        isDrag = dragEnabled;
    }

    public ItemTouchHelper getItemTouchHelper() {
        return itemTouchHelper;
    }

    public BaseAdapter getAdapter() {
        return adapter;
    }

}
