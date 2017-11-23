package com.alessandro.easygarbagecollection;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by alessandro.campanell on 23/11/2017.
 */

public class TrashCanViewHolder  extends RecyclerView.ViewHolder {

    private final TextView mCode,mFillingLevel;

    public TrashCanViewHolder(View itemView) {
        super(itemView);
        this.mCode = itemView.findViewById(R.id.code);
        this.mFillingLevel = itemView.findViewById(R.id.fillingLevel);
    }

    public void bind(TrashCan trashCan) {
        setCode(trashCan.getCode());
        setFillingLevel(trashCan.getFillingLevel());

    }

    public void setCode(String code) {
        mCode.setText(code);
    }

    public void setFillingLevel(Double fillingLevel) {
        mFillingLevel.setText(String.valueOf(fillingLevel));
    }
}
