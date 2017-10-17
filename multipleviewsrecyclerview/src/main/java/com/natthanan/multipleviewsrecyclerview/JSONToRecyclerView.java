package com.natthanan.multipleviewsrecyclerview;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LinkedTreeMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.type;

/**
 * Created by natthanan on 10/5/2017.
 */

public class JSONToRecyclerView {

    List<Class<?>> basicClass = new ArrayList<>();

    public JSONToRecyclerView(final Activity activity, String json) {
        // add standard class of object to list
        addBasicClass();
        // create gson instance
        Gson gson = new Gson();
        // create recyclerviewgsonmodel instance from json file
        final RecyclerViewGSONModel recyclerViewGSONModel = gson.fromJson(json, RecyclerViewGSONModel.class);
        // create recyclerview
        final RecyclerView recyclerView = (RecyclerView) activity.findViewById(recyclerViewGSONModel.getId());
        // create layoutmanager
        RecyclerView.LayoutManager layoutManager = null;
        int orientation = 2;
        int spanCount = 0;
        boolean reverseLayout = false;
        switch (recyclerViewGSONModel.getLayoutManager().getType()) {
            case "LinearLayoutManager":
                reverseLayout = recyclerViewGSONModel.getLayoutManager().isReverseLayout();
                switch (recyclerViewGSONModel.getLayoutManager().getOrientation()) {
                    case "Vertical":
                        orientation = LinearLayoutManager.VERTICAL;
                        break;
                    case "Horizontal":
                        orientation = LinearLayoutManager.HORIZONTAL;
                        break;
                }
                layoutManager = new LinearLayoutManager(activity, orientation, reverseLayout);
                break;
            case "GridLayoutManager":
                if (recyclerViewGSONModel.getLayoutManager().getOrientation() != null) {
                    switch (recyclerViewGSONModel.getLayoutManager().getOrientation()) {
                        case "Vertical":
                            orientation = LinearLayoutManager.VERTICAL;
                            break;
                        case "Horizontal":
                            orientation = LinearLayoutManager.HORIZONTAL;
                            break;
                    }
                }
                spanCount = recyclerViewGSONModel.getLayoutManager().getSpanCount();
                if (orientation != 2) {
                    reverseLayout = recyclerViewGSONModel.getLayoutManager().isReverseLayout();
                    layoutManager = new GridLayoutManager(activity, spanCount, orientation, reverseLayout);
                } else {
                    layoutManager = new GridLayoutManager(activity, spanCount);
                }
                break;
            case "StaggeredGridLayoutManager":
                switch (recyclerViewGSONModel.getLayoutManager().getOrientation()) {
                    case "Vertical":
                        orientation = LinearLayoutManager.VERTICAL;
                        break;
                    case "Horizontal":
                        orientation = LinearLayoutManager.HORIZONTAL;
                        break;
                }
                spanCount = recyclerViewGSONModel.getLayoutManager().getSpanCount();
                layoutManager = new StaggeredGridLayoutManager(spanCount, orientation);
        }
        // set layoutmanager to recyclerview
        recyclerView.setLayoutManager(layoutManager);

        // create baseadapter instance
        BaseAdapter baseAdapter = new BaseAdapter();
        // set adapter to recyclerview
        recyclerView.setAdapter(baseAdapter);
        // swipe
        if (recyclerViewGSONModel.getSwipe().isSwipe()) {
            // get swipe flag from json
            int[] flag = {0, 0, 0, 0};
            if (recyclerViewGSONModel.getSwipe().getSwipeFlag().isRight()) flag[0] = ItemTouchHelper.RIGHT;
            if (recyclerViewGSONModel.getSwipe().getSwipeFlag().isLeft()) flag[1] = ItemTouchHelper.LEFT;
            if (recyclerViewGSONModel.getSwipe().getSwipeFlag().isUp()) flag[2] = ItemTouchHelper.UP;
            if (recyclerViewGSONModel.getSwipe().getSwipeFlag().isDown()) flag[3] = ItemTouchHelper.DOWN;

            // create instance of swipe
            new Swipe(recyclerView, flag[0] | flag[1] | flag[2] | flag[3]) {
                @Override
                public void onSwipedRight(final int position, ViewDataModel viewDataModel, List<ViewDataModel> viewDataModels) {
                    if (recyclerViewGSONModel.getSwipe().getSwipeRight() != null) {
                        switch (recyclerViewGSONModel.getSwipe().getSwipeRight().getAction()) {
                            case Swipe.ACTION_REMOVE:
                                removeItem(position, viewDataModel);
                                if (recyclerViewGSONModel.getSwipe().getSwipeRight().isUndo()) {
                                    Snackbar.make(getRecyclerView(), "Remove!!!", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            undoRemove(position, getOldViewDataModel(), getOldGroup());
                                        }
                                    }).show();
                                }
                                break;
//                            case Swipe.ACTION_UPDATE:

                        }
                    } else {
                        dontDoAnything(position);
                    }


                }

                @Override
                public void onSwipedLeft(final int position, ViewDataModel viewDataModel, List<ViewDataModel> viewDataModels) {
                    if (recyclerViewGSONModel.getSwipe().getSwipeLeft() != null) {
                        switch (recyclerViewGSONModel.getSwipe().getSwipeLeft().getAction()) {
                            case Swipe.ACTION_REMOVE:
                                removeItem(position, viewDataModel);
                                if (recyclerViewGSONModel.getSwipe().getSwipeLeft().isUndo()) {
                                    Snackbar.make(getRecyclerView(), "Remove!!!", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            undoRemove(position, getOldViewDataModel(), getOldGroup());
                                        }
                                    }).show();
                                }
                                break;
//                            case Swipe.ACTION_UPDATE:

                        }
                    } else {
                        dontDoAnything(position);
                    }
                }

                @Override
                public void onSwipeUp(final int position, ViewDataModel viewDataModel, List<ViewDataModel> viewDataModels) {
                    if (recyclerViewGSONModel.getSwipe().getSwipeUp() != null) {
                        switch (recyclerViewGSONModel.getSwipe().getSwipeUp().getAction()) {
                            case Swipe.ACTION_REMOVE:
                                removeItem(position, viewDataModel);
                                if (recyclerViewGSONModel.getSwipe().getSwipeUp().isUndo()) {
                                    Snackbar.make(getRecyclerView(), "Remove!!!", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            undoRemove(position, getOldViewDataModel(), getOldGroup());
                                        }
                                    }).show();
                                }
                                break;
//                            case Swipe.ACTION_UPDATE:

                        }
                    } else {
                        dontDoAnything(position);
                    }
                }

                @Override
                public void onSwipeDown(final int position, ViewDataModel viewDataModel, List<ViewDataModel> viewDataModels) {
                    if (recyclerViewGSONModel.getSwipe().getSwipeDown() != null) {
                        switch (recyclerViewGSONModel.getSwipe().getSwipeDown().getAction()) {
                            case Swipe.ACTION_REMOVE:
                                removeItem(position, viewDataModel);
                                if (recyclerViewGSONModel.getSwipe().getSwipeDown().isUndo()) {
                                    Snackbar.make(getRecyclerView(), "Remove!!!", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            undoRemove(position, getOldViewDataModel(), getOldGroup());
                                        }
                                    }).show();
                                }
                                break;
//                            case Swipe.ACTION_UPDATE:

                        }
                    } else {
                        dontDoAnything(position);
                    }
                }

                @Override
                public void onUpdateSwiped(int position, ViewDataModel viewDataModel, List<ViewDataModel> viewDataModels, String action) {
                }
            };
        }

        // drag
        if (recyclerViewGSONModel.getDrag().isDrag()) {
            new Drag(recyclerView) {
                @Override
                public void onItemDropped(List<ViewDataModel> dataModels) {

                }
            };
        }

        // traverse to viewdatamodels and create instance of viewdatamodel
        ArrayList<RecyclerViewGSONModel.ViewDataModel> viewDataModels = recyclerViewGSONModel.getViewDataModels();
        for (RecyclerViewGSONModel.ViewDataModel viewDataModel : viewDataModels) {
            Class<?> viewHolderClass;
            Class<?> modelClass;
            Object model;
            try {
                viewHolderClass = Class.forName(activity.getPackageName() + ".viewholder." + viewDataModel.getViewHolderType());
                modelClass = getModelClass(activity, viewDataModel.getModel().getClassName());
                if (basicClass.contains(modelClass)) {
                   model = modelClass.cast(viewDataModel.getModel().getData());
                } else {
                    model = modelClass.cast(gson.fromJson(viewDataModel.getModel().getData(), modelClass));
                }
                new ViewDataModel(viewHolderClass, model, viewDataModel.getTag(), viewDataModel.isParent(), viewDataModel.getGroupName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    private Class<?> getModelClass(Activity activity, String className) {
        Class<?> modelClass = null;
        try {
            switch (className){
                case "String":
                    modelClass = String.class;
                    break;
                case "Integer":
                    modelClass = Integer.class;
                    break;
                case "int":
                    modelClass = int.class;
                    break;
                case "Long":
                    modelClass = Long.class;
                    break;
                case "long":
                    modelClass = long.class;
                    break;
                case "Double":
                    modelClass = Double.class;
                    break;
                case "double":
                    modelClass = double.class;
                    break;
                case "Boolean":
                    modelClass = Boolean.class;
                    break;
                case "boolean":
                    modelClass = boolean.class;
                    break;
                case "Byte":
                    modelClass = Byte.class;
                    break;
                case "byte":
                    modelClass = byte.class;
                    break;
                case "Short":
                    modelClass = Short.class;
                    break;
                case "short":
                    modelClass = short.class;
                    break;
                case "Float":
                    modelClass = Float.class;
                    break;
                case "float":
                    modelClass = float.class;
                    break;
                case "Character":
                    modelClass = Character.class;
                    break;
                case "char":
                    modelClass = char.class;
                    break;
                default:
                    modelClass = Class.forName(activity.getPackageName() + ".model." + className);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return modelClass;
    }

    private void addBasicClass() {
        basicClass.add(String.class);
        basicClass.add(Integer.class);
        basicClass.add(int.class);
        basicClass.add(Double.class);
        basicClass.add(double.class);
        basicClass.add(Boolean.class);
        basicClass.add(boolean.class);
        basicClass.add(Long.class);
        basicClass.add(long.class);
        basicClass.add(Byte.class);
        basicClass.add(byte.class);
        basicClass.add(Short.class);
        basicClass.add(short.class);
        basicClass.add(Float.class);
        basicClass.add(float.class);
        basicClass.add(Character.class);
        basicClass.add(char.class);
    }

    private String decapitalize(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }
        char c[] = string.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }

    private Object createInstance(Class<?> klass) {
        Class<?> c = null;
        try {
            c = Class.forName(klass.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Constructor<?> constructor = null;
        try {
            constructor = c.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Object instance = null;
        try {
            instance = constructor.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        return instance;
    }
}
