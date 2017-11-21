package com.alessandro.easygarbagecollection;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

public class EGCMapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private ArrayList<LatLng> markerPoints;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMapAsync(this);
        mDatabase= FirebaseDatabase.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
        markerPoints = new ArrayList<LatLng>();

       /*  FirebaseMarker marker = new FirebaseMarker("Start", "start", 41.103466, 16.8786148, "24/12/1940", "02/07/2016" );
        FirebaseMarker marker2 = new FirebaseMarker("secondo", "Campanello", 41.104069,16.8785068, "20/11/2017", "02/07/2016" );
        FirebaseMarker marker3 = new FirebaseMarker("terzo", "Campanello", 41.107092,16.8792508, "20/11/2017", "02/07/2016" );
        FirebaseMarker marker4 = new FirebaseMarker("end", "Campanello", 41.212305,16.9817258, "20/11/2017", "02/07/2016" );
        mRef.push().setValue(marker);
        Log.d("ADebugTag", "Value: " + "aggiunto");
        mRef.push().setValue(marker2);
        mRef.push().setValue(marker3);
        mRef.push().setValue(marker4);*/

    }


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
        Log.d("ADebugTag", "Value: " + "entrato");
        mMap = googleMap;
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(41.1101687,16.8788819) , 15.0f) );


       /* mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMap.clear();
                for (DataSnapshot markerSnapshot : dataSnapshot.getChildren() ){
                    FirebaseMarker marker = markerSnapshot.getValue(FirebaseMarker.class);
                    Double latitude = marker.getLatitude();
                    Double longitude = marker.getLongitude();
                    String code = marker.getCode();
                    Double fillingLevel = marker.getFillingLevel();
                    LatLng location = new LatLng(latitude,longitude);
                    Log.d("ADebugTag", "Value: " + location);


                    //select Marker color according with filling level. If >25 adds it to markerPoints
                    if(fillingLevel > 25.0 ){
                        mMap.addMarker(new MarkerOptions().position(location).title(code).snippet("Filling level: "+fillingLevel + "%").icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        markerPoints.add(location);
                    }
                    else mMap.addMarker(new MarkerOptions().position(location).title(code).snippet("Filling level: "+fillingLevel + "%").icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                }
                fill();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


           *//* public void fill(){
                LatLng origin = markerPoints.get(0);
                LatLng dest = markerPoints.get(markerPoints.size()-1);
                String url = getDirectionsUrl(origin, dest);
                DownloadTask downloadTask = new DownloadTask();
                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
            }*//*
        });
*/

        mRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("ADebugTag", "ON CHILD ADDED");
                mMap.clear();
                markerPoints = new ArrayList<LatLng>();
                for (DataSnapshot markerSnapshot : dataSnapshot.getChildren() ){
                    FirebaseMarker marker = markerSnapshot.getValue(FirebaseMarker.class);
                    Double latitude = marker.getLatitude();
                    Double longitude = marker.getLongitude();
                    String code = marker.getCode();
                    Double fillingLevel = marker.getFillingLevel();
                    LatLng location = new LatLng(latitude,longitude);



                    //select Marker color according with filling level. If >25 adds it to markerPoints
                    if(fillingLevel > 25.0 ){
                        mMap.addMarker(new MarkerOptions().position(location).title(code).snippet("Filling level: "+fillingLevel + "%").icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        markerPoints.add(location);
                    }
                    else mMap.addMarker(new MarkerOptions().position(location).title(code).snippet("Filling level: "+fillingLevel + "%").icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                }
                LatLng origin = markerPoints.get(0);
                LatLng dest = markerPoints.get(markerPoints.size()-1);
                String url = getDirectionsUrl(origin, dest);
                DownloadTask downloadTask = new DownloadTask();
                // Start downloading json data from Google Directions API
                downloadTask.execute(url);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("ADebugTag", "ON CHILD CHANGED");
                mMap.clear();
                markerPoints = new ArrayList<LatLng>();
                for (DataSnapshot markerSnapshot : dataSnapshot.getChildren() ){
                    FirebaseMarker marker = markerSnapshot.getValue(FirebaseMarker.class);
                    Double latitude = marker.getLatitude();
                    Double longitude = marker.getLongitude();
                    String code = marker.getCode();
                    Double fillingLevel = marker.getFillingLevel();
                    LatLng location = new LatLng(latitude,longitude);



                    //select Marker color according with filling level. If >25 adds it to markerPoints
                    if(fillingLevel > 25.0 ){
                        mMap.addMarker(new MarkerOptions().position(location).title(code).snippet("Filling level: "+fillingLevel + "%").icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        markerPoints.add(location);
                    }
                    else mMap.addMarker(new MarkerOptions().position(location).title(code).snippet("Filling level: "+fillingLevel + "%").icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                }
                LatLng origin = markerPoints.get(0);
                LatLng dest = markerPoints.get(markerPoints.size()-1);
                String url = getDirectionsUrl(origin, dest);
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


    }


    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Waypoints
        String waypoints = "";
        for(int i=2;i<markerPoints.size();i++){
            LatLng point  = (LatLng) markerPoints.get(i);
            if(i==2)
                waypoints = "waypoints=";
            waypoints += point.latitude + "," + point.longitude + "|";
        }

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+waypoints;

        // Output format
        String output = "json";

        // Building the url to the web service
       // String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }



    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downlo", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
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


        /** A class to parse the Google Places in JSON format */
        private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

            // Parsing the data in non-ui thread
            @Override
            protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

                JSONObject jObject;
                List<List<HashMap<String, String>>> routes = null;

                try{
                    jObject = new JSONObject(jsonData[0]);
                    DirectionsJSONParser parser = new DirectionsJSONParser();

                    // Starts parsing data
                    routes = parser.parse(jObject);
                }catch(Exception e){
                    e.printStackTrace();
                }
                return routes;
            }

            // Executes in UI thread, after the parsing process
            @Override
            protected void onPostExecute(List<List<HashMap<String, String>>> result) {

                ArrayList<LatLng> points = new ArrayList<LatLng>();;
                PolylineOptions lineOptions = new PolylineOptions();

                // Traversing through all the routes
                for(int i=0;i<result.size();i++){
                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for(int j=0;j<path.size();j++){
                        HashMap<String,String> point = path.get(j);

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
                if(points.size()!=0)mMap.addPolyline(lineOptions);
                //mMap.addPolyline(lineOptions);
            }
        }
    }


