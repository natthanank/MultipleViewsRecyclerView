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

    ArrayList<ViewDataModel> viewDataModels = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_test);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        final BaseAdapter baseAdapter = new BaseAdapter(viewDataModels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(baseAdapter);
        recyclerView.addOnScrollListener(new LoadMoreListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        viewDataModels.add(new ViewDataModel(HeaderViewHolder.class, "HEADER", "HEADER", true, true, null));
                        for (int i = 30; i > 0; i--) {
                            viewDataModels.add(new ViewDataModel(ItemViewHolder.class, Integer.toString(i), "ITEM", true, false, null));
                        }
                        viewDataModels.add(new ViewDataModel(FooterViewHolder.class, "FOOTER", "FOOTER", true, false, null));
                        baseAdapter.notifyDataSetChanged();
                    }
                });

            }
        });

        for (int j = 0; j < 20; j++) {
            viewDataModels.add(new ViewDataModel(HeaderViewHolder.class, "Group"+j+" HEADER", "HEADER", true, true, "Group"+j));
            for (int i = 0; i < 3; i++) {
                viewDataModels.add(new ViewDataModel(ItemViewHolder.class, "Group"+j+" number " + i, "ITEM", true, false, "Group"+j));
            }
            viewDataModels.add(new ViewDataModel(FooterViewHolder.class, "Group"+j+" FOOTER", "FOOTER", true, false, "Group"+j));
        }
        viewDataModels.add(new ViewDataModel(FooterViewHolder.class, "Group"+0+" FOOTER", "FOOTER", true, false, "Group"+0));

        new Swipe(recyclerView, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public void onUpdateSwiped(int position, ViewDataModel viewDataModel, int action) {

            }

            @Override
            public void onSwipedRight(final int position, final ViewDataModel viewDataModel) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecyclerViewTest.this);
                builder.setMessage("รับขนมจีบซาลาเปาเพิ่มมั้ยครับ?");
                builder.setPositiveButton("รับ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        viewDataModel.setModel("รับขนมจีบซาลาเปา");
                        updateItem(position, viewDataModel);
                        Snackbar.make(getRecyclerView(), "คุณได้รับขนมจีบซาลาเปา", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                undoUpdate(position, getOldViewDataModel());
                            }
                        }).show();
                    }
                });
                builder.setNegativeButton("ไม่รับ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.dismiss();
                    }
                });
                builder.show();


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

        new Drag(recyclerView) {
            @Override
            public void onItemDropped(List<ViewDataModel> dataModels) {
                System.out.println(dataModels.get(0).getGroupName());
            }
        };

    }

    @Override
    public void onDatachange(String tag, BaseViewHolder baseViewHolder, View view, Object data) {
        System.out.println(tag + " " + view.getClass().getSimpleName() + " at " + baseViewHolder.getAdapterPosition() + " position has changed to " + (String) data);
    }
}
