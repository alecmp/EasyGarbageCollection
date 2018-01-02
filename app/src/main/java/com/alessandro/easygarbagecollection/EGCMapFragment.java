package com.alessandro.easygarbagecollection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EGCMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference mRef;
    private ArrayList<LatLng> markerPoints;
    private static final Double TRESHOLD = 25.0;
    FloatingActionButton fab;
    String url = null;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_egcmap, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapview);
        mapFragment.getMapAsync(this);
        mRef = FirebaseDatabase.getInstance().getReference();
        markerPoints = new ArrayList<>();
        fab = v.findViewById(R.id.fab);



        /*mRef = FirebaseDatabase.getInstance().getReference();
        TrashCan marker = new TrashCan("0001", 34, 41.103466, 16.8786148, "AAA" );
        mRef.child("Navigation").child("TC001").setValue(marker);*/


        mRef = FirebaseDatabase.getInstance().getReference();
        TrashCan marker = new TrashCan("0001", 34, 41.102333, 16.87356, "AAA" );
        TrashCan marker2 = new TrashCan("0004", 34, 41.122514, 16.875075, "AAA" );
        TrashCan marker3 = new TrashCan("0002", 34, 41.109548, 16.874069, "AAA" );
        TrashCan marker4 = new TrashCan("0003", 34, 41.116094, 16.879183, "AAA" );

        mRef.child("Navigation").child("TC001").setValue(marker);
        mRef.child("Navigation").child("TC004").setValue(marker2);
        mRef.child("Navigation").child("TC002").setValue(marker3);
        mRef.child("Navigation").child("TC003").setValue(marker4);





        return  v;
    }




    /*@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMapAsync(this);
        mRef = FirebaseDatabase.getInstance().getReference();
        markerPoints = new ArrayList<>();
       *//*  TrashCan marker = new TrashCan("Start", "start", 41.103466, 16.8786148, "24/12/1940", "02/07/2016" );
        TrashCan marker2 = new TrashCan("secondo", "Campanello", 41.104069,16.8785068, "20/11/2017", "02/07/2016" );
        TrashCan marker3 = new TrashCan("terzo", "Campanello", 41.107092,16.8792508, "20/11/2017", "02/07/2016" );
        TrashCan marker4 = new TrashCan("end", "Campanello", 41.212305,16.9817258, "20/11/2017", "02/07/2016" );
        mRef.push().setValue(marker);
        Log.d("ADebugTag", "Value: " + "aggiunto");
        mRef.push().setValue(marker2);
        mRef.push().setValue(marker3);
        mRef.push().setValue(marker4);*//*
    }*/


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.1101687, 16.8788819), 15.0f));
        mRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("Background Task", "ON CHILD ADDED");
                mMap.clear();
                markerPoints = new ArrayList<>();
                for (DataSnapshot markerSnapshot : dataSnapshot.getChildren()){
                    TrashCan marker = markerSnapshot.getValue(TrashCan.class);
                    double latitude = 0;
                    double longitude = 0;
                    String code = null;
                    String lastUpdate = null;
                    double fillingLevel = 0;
                    if (marker != null) {
                        latitude = marker.getLatitude();
                        longitude = marker.getLongitude();
                        code = marker.getCode();
                        lastUpdate = marker.getLastUpdate();
                        fillingLevel = marker.getFillingLevel();
                    }

                    LatLng location = new LatLng(latitude,longitude);

                    //select Marker color according with filling level. If >25 adds it to markerPoints
                    if (fillingLevel > TRESHOLD) {
                        mMap.addMarker(new MarkerOptions().position(location).title(code).snippet("Filling level: " + fillingLevel + "%" + "\n" + "Last update: " + lastUpdate).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        markerPoints.add(location);
                    } else
                        mMap.addMarker(new MarkerOptions().position(location).title(code).snippet("Filling level: " + fillingLevel + "%" + "\n" + "Last update: " + lastUpdate).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                }
                LatLng origin = markerPoints.get(0);
                LatLng dest = markerPoints.get(1);
                url = getDirectionsUrl(origin, dest);
                DownloadTask downloadTask = new DownloadTask();
                // Start downloading json data from Google Directions API
                downloadTask.execute(url);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                mMap.clear();
                markerPoints = new ArrayList<>();
                for (DataSnapshot markerSnapshot : dataSnapshot.getChildren()) {
                    TrashCan marker = markerSnapshot.getValue(TrashCan.class);
                    double latitude = 0;
                    double longitude = 0;
                    String code = null;
                    String lastUpdate = null;
                    double fillingLevel = 0;
                    if (marker != null) {
                        latitude = marker.getLatitude();
                        longitude = marker.getLongitude();
                        code = marker.getCode();
                        lastUpdate = marker.getLastUpdate();
                        fillingLevel = marker.getFillingLevel();
                    }
                    LatLng location = new LatLng(latitude, longitude);
                    //select Marker color according with filling level. If >25 adds it to markerPoints
                    if (fillingLevel > TRESHOLD) {
                        mMap.addMarker(new MarkerOptions().position(location).title(code).snippet("Filling level: " + fillingLevel + "%" +"\n"+ "Last update: " + lastUpdate).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        markerPoints.add(location);
                    } else
                        mMap.addMarker(new MarkerOptions().position(location).title(code).snippet("Filling level: " + fillingLevel + "%" +"\n"+ "Last update: " + lastUpdate).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                }
                LatLng origin = markerPoints.get(0);
                LatLng dest = markerPoints.get(1);
                url = getDirectionsUrl(origin, dest);
                DownloadTask downloadTask = new DownloadTask();
                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(url != null) {
                    String newUrl = url.replaceAll("https://maps.googleapis.com/maps/api/directions/json\\?", "https://www.google.com/maps/dir/?api=1&");
                    Log.w("URIIII", " " + newUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(newUrl));
                    startActivity(intent);
                }
            }
        });
    }



    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";

        // Waypoints
        StringBuilder waypoints = new StringBuilder();
        //for(int i=2;i<markerPoints.size();i++){
        for (int i = 2; i < markerPoints.size(); i++) {
            LatLng point = markerPoints.get(i);
            if (i == 2)
                waypoints = new StringBuilder("waypoints=");
            waypoints.append(point.latitude).append(",").append(point.longitude).append("|");
        }

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + waypoints;
        // Output format
        String output = "json";
        // Building the url to the web service
        // String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.d("url", "is " + url);
        return url;
    }




    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        } catch (Exception e) {
            Log.d("Exception w download", e.toString());
        } finally {
            if (iStream != null) {
                iStream.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return data;
    }


    // Fetches data from url passed
    @SuppressLint("StaticFieldLeak")
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    @SuppressLint("StaticFieldLeak")
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {


            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(16);
                lineOptions.color(Color.parseColor("#2196F3")).geodesic(true).zIndex(8);
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions!= null) mMap.addPolyline(lineOptions);
        }
    }
}