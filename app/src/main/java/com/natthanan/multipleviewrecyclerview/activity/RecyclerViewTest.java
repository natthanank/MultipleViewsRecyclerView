package com.natthanan.multipleviewrecyclerview.activity;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.natthanan.multipleviewrecyclerview.R;
import com.natthanan.multipleviewrecyclerview.viewholder.FooterViewHolder;
import com.natthanan.multipleviewrecyclerview.viewholder.HeaderViewHolder;
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
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        final BaseAdapter baseAdapter = new BaseAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(baseAdapter);
        recyclerView.addOnScrollListener(new LoadMoreListener(recyclerView) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                new ViewDataModel(HeaderViewHolder.class, "LoadMore"+ page, "LoadMore" , true, "OnLoadMore" + page);
                for (int i = 0; i < 5; i++) {
                    new ViewDataModel(FooterViewHolder.class, "page " + page + " " + Integer.toString((page - 1) * 5 + i), "LoadMore", false, "OnLoadMore" + page);
                    baseAdapter.notifyItemInserted(BaseAdapter.getViewDataModels().size());
                }

                if (page == 4) {
                    stopLoading();
                }

            }
        });

        for (int j = 0; j < 5; j++) {
            new ViewDataModel(HeaderViewHolder.class, "Group" + j + " HEADER", "HEADER", true, "Group" + j);
            for (int i = 0; i < 2; i++) {
                new ViewDataModel(FooterViewHolder.class, "Group" + j + " number " + i, "FOOTER", false, "Group" + j);
            }
        }
        new Swipe(recyclerView, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public void onChildSwiped(final int position, final ViewDataModel viewDataModel, int direction) {
                if (direction == ItemTouchHelper.RIGHT) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecyclerViewTest.this);
                    builder.setMessage("รับขนมจีบซาลาเปาเพิ่มมั้ยครับ?");
                    builder.setPositiveButton("รับ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            viewDataModel.setModel("รับขนมจีบซาลาเปา");
                            update(position);
                            ((FooterViewHolder) getViewHolder(position)).item.setText(((String) viewDataModel.getModel()));
                            Snackbar.make(getRecyclerView(), "คุณได้รับขนมจีบซาลาเปา", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    undoUpdate(position);
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

                if (direction == ItemTouchHelper.LEFT) {
                    remove(position, viewDataModel);
                    Snackbar.make(getRecyclerView(), "Remove!!!", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            undoRemove(position);
                        }
                    }).show();
                }
            }

            @Override
            public void onParentSwiped(final int position, final ArrayList<ViewDataModel> group, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    remove(position, group);
                    Snackbar.make(getRecyclerView(), "Remove!!!", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            undoRemove(position);
                        }
                    }).show();
                }

                if (direction == ItemTouchHelper.RIGHT) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecyclerViewTest.this);
                    builder.setMessage("รับขนมจีบซาลาเปาเพิ่มมั้ยครับ?");
                    builder.setPositiveButton("รับ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            group.get(0).setModel("รับขนมจีบซาลาเปา");
                            update(position);
                            Snackbar.make(getRecyclerView(), "คุณได้รับขนมจีบซาลาเปา", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    undoUpdate(position);
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
            }

            @Override
            protected void afterParentSwiped(int position, ArrayList<ViewDataModel> group, int direction) {

            }

            @Override
            protected void afterChildSwiped(int position, ViewDataModel viewDataModel, int direction) {
                System.out.println(viewDataModel.getModel());
            }
        };

        new Drag(recyclerView) {
            @Override
            public void onItemDropped(List<ViewDataModel> viewDataModels) {

            }
        };


    }


    @Override
    public void onDataChanged(String tag, BaseViewHolder viewHolder, View view, Object data) {
        Log.i(tag, view + " at position " + viewHolder.getAdapterPosition() + " data change to " + data.toString());
    }
}
