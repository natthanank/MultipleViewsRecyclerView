package com.natthanan.multipleviewsrecyclerview;

import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

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
    private boolean isDragEnabled;
    private int dragFlags;
    private ItemTouchHelper itemTouchHelper;
    private int dragFrom = -1;
    private int dragTo = -1;
    private int groupFromPosition;
    private int groupToPosition;
    public static boolean isDrag = true;
    public static final String PARENT_ONLY = "PARENT_ONLY";
    public static final String PARENT_AND_GROUP = "PARENT_AND_GROUP";
    public static String parentDragType = null;
    public static boolean isMoving = false;

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
        isMoving = true;
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

    private void reallyMoved(int fromPosition, int toPosition) {
        boolean isFromPositionGreaterThanToPosition = isFromPositionGreater(fromPosition, toPosition);

        if (isGroupSwapped(fromPosition)) {
            // check togroup position
            groupToPosition = getGroupByPosition(toPosition);
            // swap group
            swapGroup(groupFromPosition, groupToPosition);
            // create new dataset and set to adapter
            createNewViewDataModels();
        } else if (toPosition == 0) {

            ((ViewDataModel) BaseAdapter.getViewDataModels().get(toPosition)).setGroupName(Integer.toString(BaseAdapter.getViewDataModels().get(toPosition).hashCode()));
            createNewGroup(fromPosition, toPosition);
            createNewViewDataModels();

        } else {
            // set new groupName
            setNewGroupName(fromPosition, toPosition);

            List<ViewDataModel> toGroup = getMatchGroup((ViewDataModel) BaseAdapter.getViewDataModels().get(toPosition));
            ViewDataModel viewDataModelTemp = null;

            // check group position
            groupFromPosition = getGroupByPosition(fromPosition);
            groupToPosition = getGroupByPosition(toPosition);
            // change position to position in group
            fromPosition = getPositionInGroup(fromPosition);
            toPosition = getPositionInGroup(toPosition);

            try {
                if (groupFromPosition < groupToPosition) {
                    viewDataModelTemp = (ViewDataModel) toGroup.get(toGroup.size() - 1).clone();
                    toGroup.add(viewDataModelTemp);
                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }


            if (isFromPositionGreaterThanToPosition && groupFromPosition != groupToPosition && toPosition != 0) {
                System.out.println("inf");
                ViewDataModel temp = BaseAdapter.getGroupList().get(groupFromPosition).remove(fromPosition + 1);
                BaseAdapter.getGroupList().get(groupToPosition).add(toPosition, temp);
            } else if (groupFromPosition != groupToPosition) {
                System.out.println("elseif");
                ViewDataModel temp = BaseAdapter.getGroupList().get(groupFromPosition).remove(fromPosition);
                BaseAdapter.getGroupList().get(groupToPosition).add(toPosition + 1, temp);
            } else {
                System.out.println("else");
                System.out.println(groupFromPosition + " " + fromPosition);
                System.out.println(groupToPosition + " " + toPosition);
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(BaseAdapter.getGroupList().get(groupFromPosition), i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(BaseAdapter.getGroupList().get(groupFromPosition), i, i - 1);
                    }
                }

            }
            toGroup.remove(viewDataModelTemp);
            createNewViewDataModels();

        }
        removeGroup(groupFromPosition);
//        for (List<ViewDataModel> group : BaseAdapter.getGroupList()) {
//            for (int i = 0; i < group.size(); i++) {
//                System.out.println(group.get(i).getModel() + " | " + group.get(i).getGroupName());
//            }
//            System.out.println("=================");
//        }
        onItemDropped(BaseAdapter.getViewDataModels());
    }

    private boolean isGroupSwapped(int fromPosition) {
        groupFromPosition = -1;
        groupToPosition = -1;
        boolean isGroupSwap = false;
        for (int i = 0; i < BaseAdapter.getGroupList().size(); i++) {
            if (isGroupSwap) {
                break;
            }
            ArrayList<ViewDataModel> group = BaseAdapter.getGroupList().get(i);
            if (fromPosition < group.size()) {
                if (group.get(fromPosition).isParent()) {
                    groupFromPosition = i;
                    isGroupSwap = true;
                }
                break;
            } else {
                fromPosition -= group.size();
            }
        }
        return isGroupSwap;
    }

    private boolean isFromPositionGreater(int fromPosition, int toPosition) {
        if (fromPosition > toPosition) {
            return true;
        }
        return false;
    }

    private void removeGroup(int groupFromPosition) {
        if (BaseAdapter.getGroupList().get(groupFromPosition).size() == 0) {
            BaseAdapter.getGroupList().remove(groupFromPosition);
        }
    }

    private void createNewGroup(int fromPosition, int toPosition) {
        groupFromPosition = getGroupByPosition(fromPosition);
        fromPosition = getPositionInGroup(fromPosition);
        BaseAdapter.getGroupList().add(0, new ArrayList<ViewDataModel>());
        ViewDataModel v = BaseAdapter.getGroupList().get(groupFromPosition + 1).remove(fromPosition);
        BaseAdapter.getGroupList().get(0).add(v);
    }

    private void setNewGroupName(int fromPosition, int toPosition) {
        if (toPosition != 0) {
            ((ViewDataModel) BaseAdapter.getViewDataModels().get(toPosition)).setGroupName(((ViewDataModel) BaseAdapter.getViewDataModels().get(toPosition - 1)).getGroupName());
        } else {
            ((ViewDataModel) BaseAdapter.getViewDataModels().get(toPosition)).setGroupName(((ViewDataModel) BaseAdapter.getViewDataModels().get(fromPosition)).getGroupName());
        }
        System.out.println("toPosition = " + toPosition + " fromPosition = " + fromPosition);
    }

    private void createNewViewDataModels() {
        List<ViewDataModel> list = new ArrayList<>();
        for (ArrayList<ViewDataModel> array : BaseAdapter.getGroupList()) {
            list.addAll(array);
        }
        BaseAdapter.setViewDataModels(list);
        adapter.notifyDataSetChanged();
    }

    private void swapGroup(int groupFromPosition, int groupToPosition) {
        if (groupFromPosition < groupToPosition) {
            for (int i = groupFromPosition; i < groupToPosition; i++) {
                Collections.swap(BaseAdapter.getGroupList(), i, i + 1);
            }
        } else {
            for (int i = groupFromPosition; i > groupToPosition; i--) {
                Collections.swap(BaseAdapter.getGroupList(), i, i - 1);
            }
        }
    }

    private List<ViewDataModel> getMatchGroup(ViewDataModel v) {
        for (List<ViewDataModel> group : BaseAdapter.getGroupList()) {
            if (v.getGroupName().equals(group.get(0).getGroupName())) {
                return group;
            }
        }
        return null;
    }

    private int getGroupByPosition(int position) {
        for (List<ViewDataModel> group : BaseAdapter.getGroupList()) {
            if (position >= group.size()) {
                position -= group.size();
            } else {
                return BaseAdapter.getGroupList().indexOf(group);
            }
        }
        return -1;
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

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
            reallyMoved(dragFrom, dragTo);
        }

        dragFrom = dragTo = -1;
        Swipe.isSwiped = true;
        isDrag = false;
        isMoving = false;
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

    public abstract void onItemDropped(List<ViewDataModel> dataModels);

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
