package com.example.ezwaste;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
                Toast.makeText(this,"About Selected",Toast.LENGTH_SHORT).show();
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

        /*
        // Create a LatLngBounds that includes the city of Adelaide in Australia.
        LatLngBounds sriLanka = new LatLngBounds(new LatLng(5.872396, 81.987547), new LatLng(9.970554, 79.572412));
        // Constrain the camera target to the Adelaide bounds.
        mMap.setLatLngBoundsForCameraTarget(sriLanka);
        */

        // Add a marker in Sydney and move the camera
        LatLng kandy = new LatLng(7.265, 80.597);
        mMap.addMarker(new MarkerOptions().position(kandy).title("Kandy Area"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kandy,15));
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void openLogInActivity() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }
}
