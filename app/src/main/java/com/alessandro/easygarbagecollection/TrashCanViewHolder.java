package com.alessandro.easygarbagecollection;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by alessandro on 23/11/2017.
 * TrashCanViewHolder
 */

public class TrashCanViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {

    private final TextView mCode, mFillingLevel, mAddress, mLastUpdate;
    private final ImageView mFillingLevelIcon;
    private Double longitude;
    private Double latitude;
    private Context mContext;
    private static final Double TRESHOLD = 25.0;

    TrashCanViewHolder(View itemView, Context context) {
        super(itemView);
        this.mContext = context;
        this.mCode = itemView.findViewById(R.id.code);
        this.mFillingLevel = itemView.findViewById(R.id.fillingLevel);
        this.mAddress = itemView.findViewById(R.id.address);
        this.mLastUpdate = itemView.findViewById(R.id.lastUpdate);
        this.mFillingLevelIcon = itemView.findViewById(R.id.filling_level_icon);
        MapView map = itemView.findViewById(R.id.mapImageView);

        if (map != null) {
            map.onCreate(null);
            map.onResume();
            map.getMapAsync(this);
        }

    }

    void bind(TrashCan trashCan) {
        setCode(trashCan.getCode());
        setFillingLevel(trashCan.getFillingLevel());
        if (trashCan.getFillingLevel() > TRESHOLD) mFillingLevelIcon.setColorFilter(Color.RED);
        else mFillingLevelIcon.setColorFilter(Color.GREEN);
        setLastUpdate(trashCan.getLastUpdate());
        longitude = trashCan.getLongitude();
        latitude = trashCan.getLatitude();
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            Log.d("Exception w geocoding", e.toString());
        }
        String address = null;
        if (addresses != null) {
            address = addresses.get(0).getAddressLine(0);
        }
        setAddress(address);

    }

    private void setCode(String code) {
        mCode.setText(code);
    }

    private void setFillingLevel(Double fillingLevel) {
        if (fillingLevel != null){
            mFillingLevel.setText(String.valueOf(fillingLevel));
        }
    }

    private void setAddress(String address) {
        if(address != null) {
            mAddress.setText(address);
        }
    }

    private void setLastUpdate(String lastUpdate) {
        if(lastUpdate != null) {
            mLastUpdate.setText(lastUpdate);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(latitude, longitude);
        MapsInitializer.initialize(mContext);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17.0f));
        googleMap.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));

    }
}
