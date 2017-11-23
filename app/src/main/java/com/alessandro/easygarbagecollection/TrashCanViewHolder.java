package com.alessandro.easygarbagecollection;

import android.app.Application;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

/**
 * Created by alessandro.campanell on 23/11/2017.
 */

public class TrashCanViewHolder  extends RecyclerView.ViewHolder {

    private final TextView mCode,mFillingLevel, mAddress;
    private Context mContext;

    public TrashCanViewHolder(View itemView, Context context) {
        super(itemView);
        this.mContext = context;
        this.mCode = itemView.findViewById(R.id.code);
        this.mFillingLevel = itemView.findViewById(R.id.fillingLevel);
        this.mAddress = itemView.findViewById(R.id.address);
    }

    public void bind(TrashCan trashCan) {
        setCode(trashCan.getCode());
        setFillingLevel(trashCan.getFillingLevel());

        //trasforms lat and lang into address
        Double longitude = trashCan.getLongitude();
        Double latitude = trashCan.getLatitude();

        Geocoder geocoder= new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0);
        setAddress(address);

        //--------------------------------------------------------





    }

    public void setCode(String code) {
        mCode.setText(code);
    }

    public void setFillingLevel(Double fillingLevel) {
        mFillingLevel.setText(String.valueOf(fillingLevel));
    }

    public void setAddress (String address){ mAddress.setText(address);}
}
