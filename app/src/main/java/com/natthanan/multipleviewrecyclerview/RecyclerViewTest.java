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
import android.widget.Toast;

import com.natthanan.multipleviewsrecyclerview.BaseAdapter;
import com.natthanan.multipleviewsrecyclerview.BaseViewHolder;
import com.natthanan.multipleviewsrecyclerview.Drag;
import com.natthanan.multipleviewsrecyclerview.LoadMoreListener;
import com.natthanan.multipleviewsrecyclerview.Swipe;
import com.natthanan.multipleviewsrecyclerview.ViewDataModel;
import com.natthanan.multipleviewsrecyclerview.intf.DataChangedCallback;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewTest extends AppCompatActivity implements DataChangedCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_test);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        final BaseAdapter baseAdapter = new BaseAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(baseAdapter);
//        recyclerView.addOnScrollListener(new LoadMoreListener(recyclerView) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//                for (int i = 0; i < 5; i++) {
//                    new ViewDataModel(ItemViewHolder.class, "page " + page + " " +Integer.toString((page-1) * 5 + i), "LoadMore", false, null);
//                    baseAdapter.notifyItemInserted(BaseAdapter.getViewDataModels().size());
//                }
//
//                if (page == 4) {
//                    stopLoading();
//                }
//
//            }
//        });


        for (int j = 0; j < 5; j++) {
            new ViewDataModel(HeaderViewHolder.class, "Group"+j+" HEADER", "HEADER", true, "Group"+j);
            for (int i = 0; i < 2; i++) {
                new ViewDataModel(ItemViewHolder.class, "Group"+j+" number " + i, "ITEM", false, "Group"+j);
            }
        }
        new Swipe(recyclerView, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public void onUpdateSwiped(int position, ViewDataModel viewDataModel, List<ViewDataModel> group, int action) {

            }

            @Override
            public void onSwipedRight(final int position, final ViewDataModel viewDataModel, List<ViewDataModel> group) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecyclerViewTest.this);
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
            public void onSwipedLeft(final int position, ViewDataModel viewDataModel, List<ViewDataModel> group) {
                removeItem(position, viewDataModel);
                Snackbar.make(getRecyclerView(), "Remove!!!", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        undoRemove(position, getOldViewDataModel(), getOldGroup());
                    }
                }).show();
            }

            @Override
            public void onSwipeUp(int position, ViewDataModel viewDataModel, List<ViewDataModel> group) {

            }

            @Override
            public void onSwipeDown(int position, ViewDataModel viewDataModel, List<ViewDataModel> group) {

            }


        };

        new Drag(recyclerView) {
            @Override
            public void onItemDropped(List<ViewDataModel> dataModels) {
            }
        };

    }

    @Override
    public void onDatachange(String tag, BaseViewHolder baseViewHolder, View view, Object data) {
        System.out.println(tag + " " + view.getClass().getSimpleName() + " at " + baseViewHolder.getAdapterPosition() + " position has changed to " + (String) data);
    }
}
