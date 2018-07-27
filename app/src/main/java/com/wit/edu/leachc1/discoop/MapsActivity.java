package com.wit.edu.leachc1.discoop;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    String getAddress;
    String getType;
    String getName;
    String getDetails;
    String getExpr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd!=null) {
            getAddress = (String) bd.get("address");
            getType = (String) bd.get("type");
            getName = (String) bd.get("name");
            getDetails = (String) bd.get("details");
            getExpr = (String) bd.get("expr");
            mapFragment.getMapAsync(this);
        }
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
        mMap = googleMap;

        if (getAddress != null) {
            LatLng latLng = getLocationFromAddress(this, getAddress);
            mMap.addMarker(new MarkerOptions().position(latLng).title(getAddress).snippet("Address"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        } else {
            LatLng latLng = new LatLng(42.3375687, -71.096264);
            mMap.addMarker(new MarkerOptions().position(latLng).title("Wentworth").snippet("Default"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

        /** Current crashes the map
        //Add current location marker
        double currentLatitude = Double.parseDouble(MainActivity.getLatitude());
        double currentLongitude = Double.parseDouble(MainActivity.getLongitude());
        LatLng currentLocation = new LatLng(currentLatitude,currentLongitude);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
**/
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return p1;
    }
}