package com.natthanan.multipleviewsrecyclerview;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by natthanan on 10/5/2017.
 */

public class JSONToRecyclerView {

    List<Class<?>> basicClass = new ArrayList<>();

    public JSONToRecyclerView(final Activity activity, String json) {

        addBasicClass();
        try {
            // create json object
            JSONObject jsonObject = new JSONObject(json);
            // initialize recyclerview
            RecyclerView recyclerView = (RecyclerView) activity.findViewById(jsonObject.getInt("recyclerView"));
            // check layoutmanager type and set layoutmanager to recyclerview
            JSONObject layoutManagerJSON = jsonObject.getJSONObject("layoutManager");
            if ("LinearLayoutManager".equals(layoutManagerJSON.getString("type"))) {
                int orientation;
                boolean reverseLayout;
                if (layoutManagerJSON.getString("orientation") != null) {
                    if ("Vertical".equals(layoutManagerJSON.getString("orientation"))) {
                        orientation = LinearLayoutManager.VERTICAL;
                    } else orientation = LinearLayoutManager.HORIZONTAL;
                    reverseLayout = layoutManagerJSON.getBoolean("reverseLayout");
                    recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), orientation, reverseLayout));
                } else recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            }
            // set baseadapter
            recyclerView.setAdapter(new BaseAdapter());
            // swipe
            final JSONObject swipe = jsonObject.getJSONObject("swipe");
            if (swipe.getBoolean("isSwipe")) {
                // get swipe flag and set to Swipe class constructor
                int[] flag = {0, 0, 0, 0};
                JSONObject swipeFlag = swipe.getJSONObject("swipeFlag");
                if (swipeFlag.getBoolean("right")) flag[0] = ItemTouchHelper.RIGHT;
                if (swipeFlag.getBoolean("left")) flag[1] = ItemTouchHelper.LEFT;
                if (swipeFlag.getBoolean("up")) flag[2] = ItemTouchHelper.UP;
                if (swipeFlag.getBoolean("down")) flag[3] = ItemTouchHelper.DOWN;
                new Swipe(recyclerView, flag[0] | flag[1] | flag[2] | flag[3]) {
                    @Override
                    public void onSwipedRight(final int position, ViewDataModel viewDataModel, List<ViewDataModel> viewDataModels) {
                        try {
                            if (swipe.getString("swipeRight") != null) {
                                if ("removeItem".equals(swipe.getString("swipeRight"))) {
                                    removeItem(position, viewDataModel);
                                    Snackbar.make(getRecyclerView(), "Remove!!!", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            undoRemove(position, getOldViewDataModel(), getOldGroup());
                                        }
                                    }).show();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onSwipedLeft(final int position, final ViewDataModel viewDataModel, List<ViewDataModel> viewDataModels) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getRecyclerView().getContext());
                        builder.setMessage("รับขนมจีบซาลาเปาเพิ่มมั้ยครับ?");
                        builder.setPositiveButton("รับ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                viewDataModel.setModel("รับขนมจีบซาลาเปา");
                                updateItem(position, viewDataModel);
                                Snackbar.make(getRecyclerView(), "คุณได้รับขนมจีบซาลาเปา", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        undoUpdate(position, getOldViewDataModel(), getOldGroup());
                                    }
                                }).show();
                            }
                        });
                        builder.setNegativeButton("ไม่รับ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                dontDoAnything(position);
                            }
                        });
                        builder.show();
                    }

                    @Override
                    public void onSwipeUp(int position, ViewDataModel viewDataModel, List<ViewDataModel> viewDataModels) {

                    }

                    @Override
                    public void onSwipeDown(int position, ViewDataModel viewDataModel, List<ViewDataModel> viewDataModels) {

                    }

                    @Override
                    public void onUpdateSwiped(int position, ViewDataModel viewDataModel, List<ViewDataModel> viewDataModels, int action) {

                    }
                };
            }
            // drag
            JSONObject drag = jsonObject.getJSONObject("drag");
            if (drag.getBoolean("isDrag")) {
                new Drag(recyclerView) {
                    @Override
                    public void onItemDropped(List<ViewDataModel> dataModels) {

                    }
                };
            }

            // traversal to viewdatamodels list and create viewdatamodel
            JSONArray viewDataModels = jsonObject.getJSONArray("viewDataModels");
            for (int i = 0; i < viewDataModels.length(); i++) {
                JSONObject viewDataModel = (JSONObject) viewDataModels.get(i);
                String className = viewDataModel.getString("viewHolderType");
                String tag = viewDataModel.getString("tag");
                boolean isParent = viewDataModel.getBoolean("isParent");
                String groupName = viewDataModel.getString("groupName");
                Class<?> viewHolderClass = Class.forName(activity.getPackageName() + ".viewholder." + className);
                JSONObject model = viewDataModel.getJSONObject("model");
                // get Class of model
                Object newModel;
                if (basicClass.contains(getModelClass(activity, model.getString("class")))){
                    newModel = model.get("data");
                } else {
                    Class<?> modelClass = getModelClass(activity, model.getString("class"));
                    Method[] classMethod = modelClass.getDeclaredMethods();
                    Constructor<?> constructor = modelClass.getConstructor();
                    newModel = constructor.newInstance();
                    for (Method m : classMethod) {
                        if (m.getName().startsWith("set")) {
                            JSONObject data = model.getJSONObject("data");
                            m.invoke(newModel, data.get(decapitalize(m.getName().substring(3))));
                        }
                    }
                }
                new ViewDataModel(viewHolderClass, newModel, tag, isParent, groupName);
            }
        } catch (JSONException | InvocationTargetException | IllegalAccessException | NoSuchMethodException | InstantiationException | ClassNotFoundException | RuntimeException e) {
            e.printStackTrace();
        }
    }

    private Class<?> getModelClass(Activity activity, String className) {
        Class<?> modelClass = null;
        try {
            switch (className){
                case "String":
                    modelClass = String.class;
                    break;
                case "int":
                    modelClass = int.class;
                    break;
                case "long":
                    modelClass = long.class;
                    break;
                case "double":
                    modelClass = double.class;
                    break;
                case "boolean":
                    modelClass = boolean.class;
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
        basicClass.add(int.class);
        basicClass.add(double.class);
        basicClass.add(boolean.class);
        basicClass.add(long.class);
    }

    private String decapitalize(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }
        char c[] = string.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }
}
