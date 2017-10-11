package com.natthanan.multipleviewrecyclerview.viewholder;

import android.support.design.internal.NavigationMenuPresenter;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.natthanan.multipleviewrecyclerview.R;
import com.natthanan.multipleviewsrecyclerview.BaseAdapter;
import com.natthanan.multipleviewsrecyclerview.BaseViewHolder;
import com.natthanan.multipleviewsrecyclerview.annotation.LayoutID;
import com.natthanan.multipleviewsrecyclerview.annotation.ViewID;

/**
 * Created by DELL on 09/09/2560.
 */


@LayoutID(R.layout.footer)
public class FooterViewHolder extends BaseViewHolder {

    @ViewID(R.id.item)
    TextView item;

    @ViewID(R.id.drag_handler)
    Button dragHandler;


    public FooterViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object data, String tag) {

        item.setText((String) data);
        dragHandler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((BaseAdapter) getRecyclerView().getAdapter()).getItemTouchHelper().startDrag(FooterViewHolder.this);
                return true;
            }
        });

    }

}
