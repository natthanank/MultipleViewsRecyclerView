package com.natthanan.multipleviewsrecyclerview.util;

import com.natthanan.multipleviewsrecyclerview.BaseAdapter;
import com.natthanan.multipleviewsrecyclerview.ViewDataModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by natthanan on 10/20/2017.
 */

public class GroupUtil {

    public static int groupFromPosition;
    public static int groupToPosition;

    public static final int getGroupByPosition(int position) {
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

    public static final int getPositionInGroup(int position) {
        for (List<ViewDataModel> group : BaseAdapter.getGroupList()) {
            if (position >= group.size()) {
                position -= group.size();
            } else {
                return position;
            }
        }
        return -1;
    }

    public static final void removeBlankGroup() {
        int groupPosition = 0;
        boolean isRemove = false;
        for (List<ViewDataModel> group : BaseAdapter.getGroupList()) {
            if (group.size() == 0) {
                isRemove = true;
                groupPosition = BaseAdapter.getGroupList().indexOf(group);
            }
        }
        if (isRemove) BaseAdapter.getGroupList().remove(groupPosition);
    }

    public static final boolean isGroupSwapped(int fromPosition) {
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

    public static final boolean isFromPositionGreater(int fromPosition, int toPosition) {
        if (fromPosition > toPosition) {
            return true;
        }
        return false;
    }

    public static final void createNewGroup(int fromPosition) {
        groupFromPosition = GroupUtil.getGroupByPosition(fromPosition);
        fromPosition = GroupUtil.getPositionInGroup(fromPosition);
        BaseAdapter.getGroupList().add(0, new ArrayList<ViewDataModel>());
        ViewDataModel v = BaseAdapter.getGroupList().get(groupFromPosition + 1).remove(fromPosition);
        BaseAdapter.getGroupList().get(0).add(v);
    }

    public static final void setNewGroupName(int fromPosition, int toPosition) {
        if (toPosition != 0) {
            (BaseAdapter.getViewDataModels().get(toPosition)).setGroupName(BaseAdapter.getViewDataModels().get(toPosition - 1).getGroupName());
        } else {
            (BaseAdapter.getViewDataModels().get(toPosition)).setGroupName(BaseAdapter.getViewDataModels().get(fromPosition).getGroupName());
        }
    }

    public static final void swapGroup(int groupFromPosition, int groupToPosition) {
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

    public static final List<ViewDataModel> getMatchGroup(ViewDataModel viewDataModel) {
        for (List<ViewDataModel> group : BaseAdapter.getGroupList()) {
            if (Objects.equals(viewDataModel.getGroupName(), group.get(0).getGroupName())) {
                return group;
            } else if (Objects.equals(viewDataModel.getGroupName(), group.get(group.size()-1).getGroupName())) {
                return group;
            }
        }
        return null;
    }

    public static List<ViewDataModel> cloneGroup(List<ViewDataModel> group) {
        List<ViewDataModel> clonedList = new ArrayList<>(group.size());
        for (ViewDataModel viewDataModel : group) {
            clonedList.add(new ViewDataModel(viewDataModel));
        }
        return clonedList;
    }
}
