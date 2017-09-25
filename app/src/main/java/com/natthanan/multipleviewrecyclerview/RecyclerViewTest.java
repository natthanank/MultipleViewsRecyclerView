package com.natthanan.multipleviewrecyclerview;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.natthanan.multipleviewsrecyclerview.BaseAdapter;
import com.natthanan.multipleviewsrecyclerview.BaseViewHolder;
import com.natthanan.multipleviewsrecyclerview.Drag;
import com.natthanan.multipleviewsrecyclerview.LoadMoreListener;
import com.natthanan.multipleviewsrecyclerview.Swipe;
import com.natthanan.multipleviewsrecyclerview.ViewDataModel;
import com.natthanan.multipleviewsrecyclerview.intf.DataChangedCallback;

import java.util.ArrayList;


public class RecyclerViewTest extends AppCompatActivity implements DataChangedCallback {

    ArrayList<ViewDataModel> viewDataModels = new ArrayList<>();

    DataModel model = new DataModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_test);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        final BaseAdapter baseAdapter = new BaseAdapter(viewDataModels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(baseAdapter);
        recyclerView.addOnScrollListener(new LoadMoreListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                for (int i = 0; i < 100; i++) {
                    if (i % 5 == 0) {
                        viewDataModels.add(new ViewDataModel(HeaderViewHolder.class, "HEADER", "Header"));
                        viewDataModels.add(new ViewDataModel(HeaderViewHolder.class, "HEADER", "Header"));
                        viewDataModels.add(new ViewDataModel(ItemViewHolder.class, Integer.toString(i)));
                    } else if (i % 5 == 4) {
                        viewDataModels.add(new ViewDataModel(ItemViewHolder.class, Integer.toString(i)));
                        viewDataModels.add(new ViewDataModel(FooterViewHolder.class, "FOOTER", "Footer"));
                        viewDataModels.add(new ViewDataModel(FooterViewHolder.class, "FOOTER", "Footer"));
                    } else {
                        viewDataModels.add(new ViewDataModel(ItemViewHolder.class, Integer.toString(i), "Item"));
                    }
                    baseAdapter.notifyItemInserted(baseAdapter.getItemCount());
                }
            }
        });

        for (int i = 0; i < 100; i++) {
            if (i % 5 == 0) {
                viewDataModels.add(new ViewDataModel(HeaderViewHolder.class, "HEADER", "Header"));
                viewDataModels.add(new ViewDataModel(HeaderViewHolder.class, "HEADER", "Header"));
                viewDataModels.add(new ViewDataModel(ItemViewHolder.class, Integer.toString(i)));
            } else if (i % 5 == 4) {
                viewDataModels.add(new ViewDataModel(ItemViewHolder.class, Integer.toString(i)));
                viewDataModels.add(new ViewDataModel(FooterViewHolder.class, "FOOTER", "Footer"));
                viewDataModels.add(new ViewDataModel(FooterViewHolder.class, "FOOTER", "Footer"));
            } else {
                viewDataModels.add(new ViewDataModel(ItemViewHolder.class, Integer.toString(i), "Item"));
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
                Snackbar.make(getRecyclerView(), "Change!!!", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        undoUpdate(position, getOldViewDataModel());
                    }
                }).show();
            }

            @Override
            public void onSwipedLeft(final int position, ViewDataModel viewDataModel) {
                removeItem(position, viewDataModel);
                Snackbar.make(getRecyclerView(), "Remove!!!", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
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

        Drag drag = new Drag(recyclerView) {
            @Override
            public void onItemMove(int fromPosition, int toPosition, ViewDataModel fromViewDataModel, ViewDataModel toViewDataModel) {

            }
        };

        drag.addGroup(FooterViewHolder.class);
        drag.addGroup(HeaderViewHolder.class);
        drag.addGroup(ItemViewHolder.class);

    }

    @Override
    public void onDatachange(String tag, BaseViewHolder baseViewHolder, View view, Object data) {
        System.out.println(tag + " " + view.getClass().getSimpleName() + " at " + baseViewHolder.getAdapterPosition() + " position has changed to " + (String) data);
    }
}
