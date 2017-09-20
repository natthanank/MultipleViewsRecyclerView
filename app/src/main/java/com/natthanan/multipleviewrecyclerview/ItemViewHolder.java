package com.natthanan.multipleviewrecyclerview;

import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.natthanan.multipleviewsrecyclerview.BaseViewHolder;
import com.natthanan.multipleviewsrecyclerview.annotation.LayoutID;
import com.natthanan.multipleviewsrecyclerview.annotation.ViewID;
@LayoutID(R.layout.item)
public class ItemViewHolder extends BaseViewHolder {


    @ViewID(R.id.itemlist)
    private TextView item;

    @ViewID(R.id.aSwitch)
    Switch aSwitch;


    public ItemViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object data) {
        item.setText((String) data);
        item.addTextChangedListener(new TextWatcher() {
            String oldText;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                Toast.makeText(itemView.getContext(), onDataChanged(item, oldText, s.toString()).getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(onDataChanged(item, oldText, s.toString()).getMessage());
            }
        });
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Snackbar.make(itemView.getRootView(), onDataChanged(buttonView, "dont check", "check").getMessage(), Snackbar.LENGTH_LONG).show();
                } else Snackbar.make(itemView.getRootView(), onDataChanged(buttonView, "dont check", "check").getMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }

}
