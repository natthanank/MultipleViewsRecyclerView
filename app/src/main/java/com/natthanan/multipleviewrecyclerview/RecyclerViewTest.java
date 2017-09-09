package com.natthanan.multipleviewrecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.natthanan.multipleviewsrecyclerview.BaseAdapter;
import com.natthanan.multipleviewsrecyclerview.Effect;
import com.natthanan.multipleviewsrecyclerview.ViewDataModel;

import java.util.ArrayList;


public class RecyclerViewTest extends AppCompatActivity {

    ArrayList<ViewDataModel> viewDataModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_test);

        for (int i = 0; i < 100; i++) {
            if (i % 5 == 0) {
                viewDataModels.add(new ViewDataModel(1, Integer.toString(i)));
            } else if (i % 5 == 4) {
                viewDataModels.add(new ViewDataModel(2, Integer.toString(i)));
            } else{
                viewDataModels.add(new ViewDataModel(0, Integer.toString(i)));
            }
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        BaseAdapter baseAdapter = new BaseAdapter(viewDataModels, recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(baseAdapter);
        baseAdapter.addViewHolder(ItemViewHolder.class, 0, R.layout.item);
        baseAdapter.addViewHolder(FooterViewHolder.class, 1, R.layout.footer);
        baseAdapter.addViewHolder(HeaderViewHolder.class, 2, R.layout.header);

        new Effect(baseAdapter,recyclerView,true,true);
    }
}
