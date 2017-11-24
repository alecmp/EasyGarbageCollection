package com.alessandro.easygarbagecollection;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
 * Created by alessandro.campanell on 23/11/2017.
 */

public class TrashCanViewHolder  extends RecyclerView.ViewHolder implements OnMapReadyCallback {

    private final TextView mCode,mFillingLevel, mAddress;
    Double longitude;
    Double latitude;
    private Context mContext;
    private GoogleMap mMap;
    private MapView map;

    public TrashCanViewHolder(View itemView, Context context) {
        super(itemView);
        this.mContext = context;
        this.mCode = itemView.findViewById(R.id.code);
        this.mFillingLevel = itemView.findViewById(R.id.fillingLevel);
        this.mAddress = itemView.findViewById(R.id.address);
        this.map = itemView.findViewById(R.id.mapImageView);

        if (map != null)
        {
            map.onCreate(null);
            map.onResume();
            map.getMapAsync(this);
        }

    }

    public void bind(TrashCan trashCan) {
        setCode(trashCan.getCode());
        setFillingLevel(trashCan.getFillingLevel());

        //trasforms lat and lang into address
        longitude = trashCan.getLongitude();
        latitude = trashCan.getLatitude();

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(latitude,longitude);
        //initialize the Google Maps Android API if features need to be used before obtaining a map
        MapsInitializer.initialize(mContext);
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 17.0f));
        mMap.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));

    }
}
