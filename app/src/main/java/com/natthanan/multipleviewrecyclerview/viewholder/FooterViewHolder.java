package com.natthanan.multipleviewrecyclerview.viewholder;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.natthanan.multipleviewrecyclerview.R;
import com.natthanan.multipleviewrecyclerview.activity.RecyclerViewTest;
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
    public TextView item;

    @ViewID(R.id.drag_handler)
    Button dragHandler;


    public FooterViewHolder(View itemView) {
        super(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void bind(Object data, final String tag) {

        dragHandler.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent, null));
        dragHandler.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorPrimaryDark, null));
        item.setText(((String) data));
        item.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ((RecyclerViewTest) itemView.getContext()).onDatachange(tag, getViewHolder(), item, s.toString());
            }
        });
        dragHandler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                startDrag();
                return true;
            }
        });
//        dragHandler.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
//                builder.setMessage("รับขนมจีบซาลาเปาเพิ่มมั้ยครับ?");
//                builder.setPositiveButton("รับ", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        item.setText("รับขนมจีบมาแล้วงับ");
//                    }
//                });
//                builder.setNegativeButton("ไม่รับ", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builder.show();
//            }
//        });

    }

}
