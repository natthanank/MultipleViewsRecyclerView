package com.natthanan.multipleviewrecyclerview.viewholder;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.natthanan.multipleviewrecyclerview.R;
import com.natthanan.multipleviewrecyclerview.model.UserModel;
import com.natthanan.multipleviewsrecyclerview.BaseViewHolder;
import com.natthanan.multipleviewsrecyclerview.annotation.LayoutID;
import com.natthanan.multipleviewsrecyclerview.annotation.ViewID;
@LayoutID(R.layout.item)
public class ItemViewHolder extends BaseViewHolder {


    @ViewID(R.id.name)
    private TextView name;

    @ViewID(R.id.surname)
    private TextView surname;

    @ViewID(R.id.aSwitch)
    Switch aSwitch;


    public ItemViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object data, final String tag) {
        name.setText(((UserModel) data).getName());
        surname.setText(((UserModel) data).getSurname());
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
