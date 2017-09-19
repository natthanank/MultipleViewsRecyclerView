package com.natthanan.multipleviewrecyclerview;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.natthanan.multipleviewsrecyclerview.BaseAdapter;
import com.natthanan.multipleviewsrecyclerview.BaseViewHolder;
import com.natthanan.multipleviewsrecyclerview.Drag;
import com.natthanan.multipleviewsrecyclerview.OnDataChangedListener;
import com.natthanan.multipleviewsrecyclerview.Swipe;
import com.natthanan.multipleviewsrecyclerview.ViewDataModel;

import java.util.ArrayList;


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
                viewDataModels.add(new ViewDataModel(HeaderViewHolder.class, "HEADER"));
                viewDataModels.add(new ViewDataModel(ItemViewHolder.class, Integer.toString(i)));
            } else if (i % 5 == 4) {
                viewDataModels.add(new ViewDataModel(ItemViewHolder.class, Integer.toString(i)));
                viewDataModels.add(new ViewDataModel(FooterViewHolder.class, "FOOTER"));
            } else {
                viewDataModels.add(new ViewDataModel(ItemViewHolder.class, Integer.toString(i)));
            }
        }
        new Swipe(recyclerView, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public void onUpdateSwiped(int position, ViewDataModel viewDataModel, int action) {

            }

            @Override
            public void onSwipedRight(final int position, ViewDataModel viewDataModel) {
                viewDataModel.setModel("Changed!!!");
                updateItem(position, viewDataModel);
                Snackbar.make(recyclerView, "Change!!!", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        undoUpdate(position, getOldViewDataModel());
                    }
                }).show();
            }

            @Override
            public void onSwipedLeft(final int position, ViewDataModel viewDataModel) {
                removeItem(position, viewDataModel);
                Snackbar.make(recyclerView, "Remove!!!", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        undoRemove(position, getOldViewDataModel());
                    }
                }).show();
            }

            @Override
            public void onSwipeUp(int position, ViewDataModel viewDataModel) {

            }

            @Override
            public void onSwipeDown(int position, ViewDataModel viewDataModel) {

            }


        };

//        new Drag(recyclerView) {
//            @Override
//            public void onItemMove(int fromPosition, int toPosition, ViewDataModel fromViewDataModel, ViewDataModel toViewDataModel) {
//
//
//            }
//        };
    }
}
