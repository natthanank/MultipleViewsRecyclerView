package com.natthanan.multipleviewsrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;


public class BaseAdapter extends RecyclerView.Adapter implements ItemTouchHelperAdapter{

    private List<ViewDataModel> dataSet;
    private SparseArray<BaseViewHolder> baseViewHolders;
    private RecyclerView recyclerView;

    public BaseAdapter(List<ViewDataModel> dataSet, RecyclerView recyclerView) {
        this.dataSet = dataSet;
        this.recyclerView = recyclerView;
        this.baseViewHolders = new SparseArray<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        for (int i = 0; i < baseViewHolders.size(); i++) {
            if (baseViewHolders.get(i).getType() == viewType) {
                return baseViewHolders.get(viewType).createViewHolder(this, baseViewHolders.get(viewType).getLayout());
            }
        }
        return null;
    }

    protected void onBind(RecyclerView.ViewHolder holder, Object data, int viewType) {
        for (int i = 0; i < baseViewHolders.size(); i++) {
            if (baseViewHolders.get(i).getType() == viewType) {
                baseViewHolders.get(viewType).getClass().cast(holder).bind(data);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        onBind(holder, dataSet.get(position).object, dataSet.get(position).viewTypes);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position).viewTypes;
    }


    @Override
    public void onItemDismiss(int position) {
        dataSet.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(dataSet, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(dataSet, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }


    public View inflateView(int layout) {
        return LayoutInflater.from(recyclerView.getContext()).inflate(layout, recyclerView, false);
    }


    public void addViewHolder(Class<? extends BaseViewHolder> viewHolderClass, int type, int layout) {
        try {
            Class<?> c = Class.forName(viewHolderClass.getName());
            Constructor<?> constructor = c.getConstructor(View.class);
            Object instance = constructor.newInstance(inflateView(layout));
            ((BaseViewHolder) instance).setType(type);
            ((BaseViewHolder) instance).setLayout(layout);
            baseViewHolders.put(type, ((BaseViewHolder) instance));

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
    }


}
