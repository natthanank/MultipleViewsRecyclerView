package com.natthanan.multipleviewrecyclerview;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.natthanan.multipleviewsrecyclerview.BaseAdapter;
import com.natthanan.multipleviewsrecyclerview.Drag;
import com.natthanan.multipleviewsrecyclerview.Swipe;
import com.natthanan.multipleviewsrecyclerview.ViewDataModel;

import java.util.ArrayList;
import java.util.Collections;


public class RecyclerViewTest extends AppCompatActivity {

    ArrayList<ViewDataModel> viewDataModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_test);

        for (int i = 0; i < 100; i++) {
            if (i % 5 == 0) {
                viewDataModels.add(new ViewDataModel(2, Integer.toString(i)));
            } else if (i % 5 == 4) {
                viewDataModels.add(new ViewDataModel(1, Integer.toString(i)));
            } else{
                viewDataModels.add(new ViewDataModel(0, Integer.toString(i)));
            }
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        BaseAdapter baseAdapter = new BaseAdapter(viewDataModels, recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(baseAdapter);
        baseAdapter.addViewHolder(ItemViewHolder.class);
        baseAdapter.addViewHolder(FooterViewHolder.class);
        baseAdapter.addViewHolder(HeaderViewHolder.class);

        new Swipe(baseAdapter, recyclerView) {
            @Override
            public void updateSwipedItem(final int position, int swipeDirection) {
                getItemTouchHelper().attachToRecyclerView(null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getRecyclerView().getContext());
                builder.setNegativeButton("cancel swipe", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getItemTouchHelper().attachToRecyclerView(getRecyclerView());
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("swipe!!!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getItemTouchHelper().attachToRecyclerView(getRecyclerView());
                        getAdapter().onItemDismiss(position);
                        dialog.dismiss();
                    }
                }).show();
            }
        };
        new Drag(baseAdapter, recyclerView) {
            @Override
            public void updateDraggedItem(int oldPosition, int newPosition) {

                getAdapter().onItemMove(oldPosition, newPosition);

            }
        };
    }
}
