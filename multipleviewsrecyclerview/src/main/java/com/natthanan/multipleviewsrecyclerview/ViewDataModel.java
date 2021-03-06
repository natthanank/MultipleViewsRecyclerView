package com.natthanan.multipleviewsrecyclerview;

import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import com.natthanan.multipleviewsrecyclerview.annotation.LayoutID;
import com.natthanan.multipleviewsrecyclerview.exception.LayoutNotFoundException;
import com.natthanan.multipleviewsrecyclerview.exception.NotBaseViewHolderClassException;
import com.natthanan.multipleviewsrecyclerview.exception.NullBaseAdapterException;
import com.natthanan.multipleviewsrecyclerview.exception.NullRecyclerViewException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ViewDataModel(Class viewHolderClass, Object model, String tag) {
        this(viewHolderClass, model, tag, false, null);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ViewDataModel(Class viewHolderClass, Object model, String tag, boolean isParent, String groupName) {
        setBaseViewHolderClass(createViewHolder(viewHolderClass, recyclerView));
        setModel(model);
        setTag(tag);
        setParent(isParent);
        setGroupName(groupName);
        setGroup(true);
        addGroup();
    }

    // for cloning
    public ViewDataModel(ViewDataModel oldViewDataModel) {
        setBaseViewHolderClass(oldViewDataModel.getBaseViewHolderClass());
        setModel(oldViewDataModel.getModel());
        setTag(oldViewDataModel.getTag());
        setGroup(oldViewDataModel.isGroup());
        setParent(oldViewDataModel.isParent());
        setViewTypes(oldViewDataModel.getViewTypes());
        setGroupName(oldViewDataModel.getGroupName());
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void addGroup() {
        boolean hasGroup = false;
            for (int i = 0; i < BaseAdapter.getGroupList().size(); i++) {
                if (Objects.equals(groupName, BaseAdapter.getGroupList().get(i).get(0).getGroupName())) {
                    BaseAdapter.getGroupList().get(i).add(this);
                    hasGroup = true;
                    break;
                }
            }
            if (!hasGroup) {
                ArrayList<ViewDataModel> newGroup = new ArrayList<>();
                newGroup.add(this);
                BaseAdapter.getGroupList().add(newGroup);
            }
        List<ViewDataModel> list = new ArrayList<>();
        for (ArrayList<ViewDataModel> array : BaseAdapter.getGroupList()) {
            list.addAll(array);
        }
        BaseAdapter.setViewDataModels(list);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private int getLayoutId(Class myClass) {
        Annotation annotation = myClass.getAnnotation(LayoutID.class);

        if (annotation instanceof LayoutID) {
            LayoutID layoutIdAnnotation = (LayoutID) annotation;
            return layoutIdAnnotation.value();
        }

        return -1;
    }

    public BaseViewHolder createViewHolder(Class<? extends BaseViewHolder> viewHolderClass, RecyclerView recyclerView) {
        try {
            Constructor<?> constructor = viewHolderClass.getConstructor(View.class);

            if (baseViewHolderSparseArray.indexOfValue(viewHolderClass) == -1) {
                baseViewHolderSparseArray.put(baseViewHolderSparseArray.size(), viewHolderClass);
            }

            int layout = getLayoutId(viewHolderClass);
            int type = baseViewHolderSparseArray.indexOfValue(viewHolderClass);

            Object instance = constructor.newInstance(inflateView(layout, viewHolderClass));

            ((BaseViewHolder) instance).setType(type);
            ((BaseViewHolder) instance).setLayout(layout);
            setViewTypes(type);

            return  ((BaseViewHolder) instance);

        } catch (InvocationTargetException e) {
            throw new NotBaseViewHolderClassException(viewHolderClass.getSimpleName() + " is not BaseViewHolder subclass.");
        } catch (NoSuchMethodException e) {
            throw new NotBaseViewHolderClassException(viewHolderClass.getSimpleName() + " is not BaseViewHolder subclass.");
        } catch (IllegalAccessException e) {
            throw new NotBaseViewHolderClassException(viewHolderClass.getSimpleName() + " is not BaseViewHolder subclass.");
        } catch (InstantiationException e) {
            throw new NotBaseViewHolderClassException(viewHolderClass.getSimpleName() + " is not BaseViewHolder subclass.");
        }
    }

    public View inflateView(int layout, Class<? extends BaseViewHolder> baseViewHolderClass) {
        if (BaseAdapter.isInitialize == false) throw new NullBaseAdapterException();
        try {
            return LayoutInflater.from(this.getRecyclerView().getContext()).inflate(layout, this.getRecyclerView(), false);
        } catch (NullPointerException e) {
            throw new NullRecyclerViewException();
        } catch (Resources.NotFoundException e) {
            throw new LayoutNotFoundException(baseViewHolderClass);
        }
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

}
