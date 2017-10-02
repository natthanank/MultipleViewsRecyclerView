package com.natthanan.multipleviewsrecyclerview;

import android.support.design.internal.NavigationMenuPresenter;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import com.natthanan.multipleviewsrecyclerview.annotation.LayoutID;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 28/08/2560.
 */

public class ViewDataModel implements Cloneable {
    private int viewTypes;
    private Object model;
    private String tag;
    private boolean isGroup;
    private boolean isParent;
    private String groupName;
    private BaseViewHolder baseViewHolderClass;
    private static RecyclerView recyclerView;
    private static SparseArray<Class<? extends BaseViewHolder>> baseViewHolderSparseArray = new SparseArray<>();

    public ViewDataModel(Class viewHolderClass, Object model) {
        setBaseViewHolderClass(createViewHolder(viewHolderClass, recyclerView));
        setModel(model);
    }

    // add tag when notify
    public ViewDataModel(Class viewHolderClass, Object model, String tag) {
        setBaseViewHolderClass(createViewHolder(viewHolderClass, recyclerView));
        setModel(model);
        setTag(tag);
    }

    public ViewDataModel(Class viewHolderClass, Object model, String tag, boolean isGroup, boolean isParent, String groupName) {
        setBaseViewHolderClass(createViewHolder(viewHolderClass, recyclerView));
        setModel(model);
        setTag(tag);
        setGroup(isGroup);
        setParent(isParent);
        setGroupName(groupName);
        List<ArrayList<ViewDataModel>> groupList = BaseAdapter.getGroupList();
        addGroup(groupList);
    }

    public void addGroup(List<ArrayList<ViewDataModel>> groupList) {
        boolean hasGroup = false;
        if (groupName == null) {
            for (int i = 0; i < groupList.size(); i++) {
                if (groupList.get(i).get(0).getGroupName() == null) {
                    groupList.get(i).add(this);
                    hasGroup = true;
                    break;
                }
            }
            if (!hasGroup) {
                ArrayList<ViewDataModel> newGroup = new ArrayList<>();
                newGroup.add(this);
                groupList.add(newGroup);
            }

        } else {
            for (int i = 0; i < groupList.size(); i++) {
                if (groupName.equals(groupList.get(i).get(0).getGroupName())) {
                    groupList.get(i).add(this);
                    hasGroup = true;
                    break;
                }
            }
            if (!hasGroup) {
                ArrayList<ViewDataModel> newGroup = new ArrayList<>();
                newGroup.add(this);
                groupList.add(newGroup);
            }
        }
        List<ViewDataModel> list = new ArrayList<>();
        for (ArrayList<ViewDataModel> array : groupList) {
            list.addAll(array);
        }
        ((BaseAdapter) recyclerView.getAdapter()).setViewDataModels(list);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public int getViewTypes() {
        return viewTypes;
    }

    public void setViewTypes(int viewTypes) {
        this.viewTypes = viewTypes;
    }

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    public BaseViewHolder getBaseViewHolderClass() {
        return baseViewHolderClass;
    }

    public void setBaseViewHolderClass(BaseViewHolder baseViewHolderClass) {
        this.baseViewHolderClass = baseViewHolderClass;
    }

    private Integer getLayoutId(Class myClass) {
        Annotation annotation = myClass.getAnnotation(LayoutID.class);

        if (annotation instanceof LayoutID) {
            LayoutID layoutIdAnnotation = (LayoutID) annotation;
            return layoutIdAnnotation.value();
        }

        return null;
    }

    public BaseViewHolder createViewHolder(Class<? extends BaseViewHolder> viewHolderClass, RecyclerView recyclerView) {
        try {
            Class<?> c = Class.forName(viewHolderClass.getName());
            Constructor<?> constructor = c.getConstructor(View.class);

            if (baseViewHolderSparseArray.indexOfValue(viewHolderClass) == -1) {
                baseViewHolderSparseArray.put(baseViewHolderSparseArray.size(), viewHolderClass);
            }

            int layout = getLayoutId(viewHolderClass);
            int type = baseViewHolderSparseArray.indexOfValue(viewHolderClass);

            Object instance = constructor.newInstance(inflateView(layout));

            ((BaseViewHolder) instance).setType(type);
            ((BaseViewHolder) instance).setLayout(layout);
            setViewTypes(type);

            return  ((BaseViewHolder) instance);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public View inflateView(int layout) {
        return LayoutInflater.from(this.getRecyclerView().getContext()).inflate(layout, this.getRecyclerView(), false);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public static void setRecyclerView(RecyclerView mRecyclerView) {
        recyclerView = mRecyclerView;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getStableID() {
        return this.hashCode();
    }


}
