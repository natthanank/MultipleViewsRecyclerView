package com.natthanan.multipleviewrecyclerview;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.natthanan.multipleviewsrecyclerview.BaseAdapter;
import com.natthanan.multipleviewsrecyclerview.Drag;
import com.natthanan.multipleviewsrecyclerview.Swipe;
import com.natthanan.multipleviewsrecyclerview.ViewDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class JSONTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsontest);
        try {
            // get json
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset());
            // initialize recyclerview
            RecyclerView recyclerView = (RecyclerView) findViewById(jsonObject.getInt("recyclerView"));
            // check layoutmanager type and set layoutmanager to recyclerview
            JSONObject layoutManagerJSON = jsonObject.getJSONObject("layoutManager");
            if ("LinearLayoutManager".equals(layoutManagerJSON.getString("type"))) {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }
            // set baseadapter
            recyclerView.setAdapter(new BaseAdapter());
            // swipe
            JSONObject swipe = jsonObject.getJSONObject("swipe");
            if (swipe.getBoolean("isSwipe") == true) {
                int[] flag = {0, 0, 0, 0};
                JSONObject swipeFlag = swipe.getJSONObject("swipeFlag");
                if (swipeFlag.getBoolean("right")) flag[0] = ItemTouchHelper.RIGHT;
                if (swipeFlag.getBoolean("left")) flag[1] = ItemTouchHelper.LEFT;
                if (swipeFlag.getBoolean("up")) flag[2] = ItemTouchHelper.UP;
                if (swipeFlag.getBoolean("down")) flag[3] = ItemTouchHelper.DOWN;
                new Swipe(recyclerView, flag[0] | flag[1] | flag[2] | flag[3]) {
                    @Override
                    public void onSwipedRight(final int position, ViewDataModel viewDataModel, List<ViewDataModel> viewDataModels) {
                        removeItem(position, viewDataModel);
                        Snackbar.make(getRecyclerView(), "Remove!!!", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                undoRemove(position, getOldViewDataModel(), getOldGroup());
                            }
                        }).show();
                    }

                    @Override
                    public void onSwipedLeft(final int position, final ViewDataModel viewDataModel, List<ViewDataModel> viewDataModels) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JSONTest.this);
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
            if (drag.getBoolean("isDrag") == true) {
                new Drag(recyclerView) {
                    @Override
                    public void onItemDropped(List<ViewDataModel> dataModels) {

                    }
                };
            }
            // traversal to viewdatamodel list and create viewdatamodel
            JSONArray viewDataModels = jsonObject.getJSONArray("viewDataModels");
            for (int i = 0; i < viewDataModels.length(); i++) {
                JSONObject viewDataModel = (JSONObject) viewDataModels.get(i);
                String simpleClass = viewDataModel.getString("viewHolderType");
                String model = viewDataModel.getString("model");
                String tag = viewDataModel.getString("tag");
                boolean isParent = viewDataModel.getBoolean("isParent");
                String groupName = viewDataModel.getString("groupName");
                Class<?> viewHolderClass = Class.forName(simpleClass);
                new ViewDataModel(viewHolderClass, model, tag, isParent, groupName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("jsonTest.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
