package com.example.ezwaste;

import androidx.appcompat.app.AppCompatActivity;

import com.example.models.Dustbin;
import com.example.util.DataReceivedListener;
import com.example.util.DirectionsJSONParser;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MainActivity";
    private GoogleMap mMap;
    private LayoutInflater inflater;
    private LinearLayout options_layout;
    private ScrollView scroll_dustbin;
    private Context homeContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //UI Constraints
        homeContext = this;
        scroll_dustbin = (ScrollView) findViewById(R.id.scroll_dustbin);
        scroll_dustbin.setVerticalScrollBarEnabled(false);

        //sendFirebaseData();
        getFirebaseData();
        //populateDustBinData();
        setupGoogleMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.side_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.option1:
                Toast.makeText(this,"Refreshed",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.option2:
                openSettingsActivity();
                return true;
            case R.id.option3:
                openAboutActivity();
                return true;
            case R.id.option4:
                openLogInActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);


    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void openLogInActivity() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    private void openAboutActivity() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void setupGoogleMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void sendFirebaseData(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("dustbin_table");
        Map<String, Dustbin> dustBinData = new HashMap<String, Dustbin>();
        Dustbin dustbin;
        for(int i=0; i<10; i++){
            dustbin = new Dustbin();
            dustbin.setDustbinID(i);
            dustbin.setDustbinName("Dustbin "+(i+1));
            dustbin.setLongitude(ThreadLocalRandom.current().nextDouble(80.066360, 81.500564));
            dustbin.setLatitude(ThreadLocalRandom.current().nextDouble(6.587693, 7.984075));
            dustbin.setFillLevel(ThreadLocalRandom.current().nextDouble(10, 100));
            dustBinData.put(dustbin.toString(), dustbin);
        }
        ref.setValue(dustBinData);
    }

    private void getFirebaseData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("dustbin_table");
        System.out.println("risith : " + ref);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Dustbin> dustBinList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Dustbin upload = postSnapshot.getValue(Dustbin.class);
                    dustBinList.add(upload);
                }
                populateDustBinData(dustBinList);
                populateMapData(dustBinList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(homeContext, "Database Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populateDustBinData(ArrayList<Dustbin> dustBinList) {
        options_layout = (LinearLayout) findViewById(R.id.list_dustbin);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        System.out.println("risith Size  222 : "+DataReceivedListener.getInstance().getDataReceived().size());
        for (Dustbin dustbin : dustBinList) {
            System.out.println("Risith "+dustbin.getDustbinName());
            View to_add = inflater.inflate(R.layout.dustbin_details, options_layout, false);
            TextView text_dustbin = (TextView) to_add.findViewById(R.id.text_dustbin);
            TextView text_fill_level = (TextView) to_add.findViewById(R.id.text_fill_level);
            text_dustbin.setText(dustbin.getDustbinName());
            text_fill_level.setText("Filled Level: "+String.format("%.2f", dustbin.getFillLevel()) + "%");
            options_layout.addView(to_add);
        }
    }

    private void populateMapData(ArrayList<Dustbin> dustBinList){
        MarkerOptions options;
        ArrayList<Dustbin> dustBinFullList = new ArrayList<>();
        for (Dustbin dustbin : dustBinList) {
            options = new MarkerOptions();
            options.position(new LatLng(dustbin.getLatitude(),dustbin.getLongitude()));
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.dustbin_icon_25x25));
            mMap.addMarker(options);
            if(dustbin.getFillLevel() >= 80){
                dustBinFullList.add(dustbin);
            }
        }
        //LatLng origin = new LatLng(dustBinList.get(0).getLatitude(),dustBinList.get(0).getLongitude());
        //LatLng dest =  new LatLng(dustBinList.get(1).getLatitude(),dustBinList.get(1).getLongitude());

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(dustBinFullList);
        System.out.println("Risith: "+ url);
        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = new ArrayList<LatLng>();
            PolylineOptions lineOptions = new PolylineOptions();
            lineOptions.width(12);
            lineOptions.color(Color.BLUE);
            MarkerOptions markerOptions = new MarkerOptions();
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
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

            }
            // Drawing polyline in the Google Map for the i-th route
            if (points.size() != 0) {
                mMap.addPolyline(lineOptions);//to avoid crash
            }
        }
    }

    private String getDirectionsUrl( ArrayList<Dustbin> dustBinFullList) {
        String url = "https://maps.googleapis.com/maps/api/directions/";
        int stops = dustBinFullList.size();
        if(stops >= 2) {
            // Origin of route
            String str_origin = "origin=" + dustBinFullList.get(0).getLatitude() + "," + dustBinFullList.get(0).getLongitude();

            // Destination of route
            String str_dest = "destination=" + dustBinFullList.get(stops-1).getLatitude() + "," + dustBinFullList.get(stops-1).getLongitude();;

            for(int i = 1; i<stops;i++) {
                String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
            }
            // Sensor enabled
            String sensor = "sensor=false";
            String mode = "mode=driving";
            // Building the parameters to the web service
            String parameters = str_origin + "&" + str_dest + "&" + mode;

            // Output format
            String output = "json";
            String apiKey = "AIzaSyARLV5CJFbz1rL4MmzIsoAlh7x5TiIv99Y";
            // Building the url to the web service
            url += output + "?" + parameters + "&key=" + apiKey + "&" + sensor;
        }
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

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

}
