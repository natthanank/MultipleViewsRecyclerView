package com.natthanan.multipleviewrecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

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
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        final BaseAdapter baseAdapter = new BaseAdapter(viewDataModels, recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(baseAdapter);

        for (int i = 0; i < 100; i++) {
            if (i % 5 == 0) {
                viewDataModels.add(new ViewDataModel(HeaderViewHolder.class, Integer.toString(i)));
            } else if (i % 5 == 4) {
                viewDataModels.add(new ViewDataModel(FooterViewHolder.class, Integer.toString(i)));
            } else {
                viewDataModels.add(new ViewDataModel(ItemViewHolder.class, Integer.toString(i)));
            }
        }
        new Swipe(recyclerView, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public void onSwipedRight(int position, ViewDataModel viewDataModel) {
                viewDataModel.setModel("qwewr");
                getAdapter().getViewDataModels().set(position, viewDataModel);
                getAdapter().notifyItemChanged(position);
            }

            @Override
            public void onSwipedLeft(int position, ViewDataModel viewDataModel) {
                getAdapter().getViewDataModels().remove(viewDataModel);
                getAdapter().notifyItemRemoved(position);
            }

            @Override
            public void onSwipeUp(int position, ViewDataModel viewDataModel) {

            }

            @Override
            public void onSwipeDown(int position, ViewDataModel viewDataModel) {

            }
        };

        new Drag(recyclerView) {
            @Override
            public void onItemMove(int fromPosition, int toPosition, ViewDataModel fromViewDataModel, ViewDataModel toViewDataModel) {


            }
        };
    }
}
