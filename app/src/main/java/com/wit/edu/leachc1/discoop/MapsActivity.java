package com.wit.edu.leachc1.discoop;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.wit.edu.leachc1.discoop.MainActivity.getLatitude;
import static com.wit.edu.leachc1.discoop.MainActivity.getLongitude;

/**
 * Discoop
 * Senior Project - Computer Science
 * Created by Crissy Leach and Sam Kanner
 * Wentworth Institute of Technology
 */

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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
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
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        DBHandler db = new DBHandler(this);
        List<Discount> discList = new ArrayList<>();
        discList = db.getAllDiscounts();

        if (getAddress != null) {
            LatLng latLng = getLocationFromAddress(this, getAddress);
            double currentLatitude = Double.valueOf(getLatitude());
            double currentLongitude = Double.valueOf(getLongitude());
            LatLng currentLocation = new LatLng(currentLatitude, currentLongitude);

            if (getName != null && getDetails != null && getType != null && getExpr != null) {
                mMap.addMarker(new MarkerOptions().position(latLng).title(getName).snippet(getDetails + ", " + getAddress + ", " + getExpr));
            } else {
                mMap.addMarker(new MarkerOptions().position(latLng).title(getAddress).snippet("Address"));
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            mMap.moveCamera(center);
            mMap.animateCamera(zoom);

            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Current Location"));
        } else if (db.getAllDiscounts()== null) {
            double currentLatitude = Double.valueOf(getLatitude());
            double currentLongitude = Double.valueOf(getLongitude());
            LatLng currentLocation = new LatLng(currentLatitude, currentLongitude);
            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Current Location"));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            CameraUpdate center = CameraUpdateFactory.newLatLng(currentLocation);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            mMap.moveCamera(center);
            mMap.animateCamera(zoom);
        } else {
            for (Discount d : discList) {
                LatLng latLng = getLocationFromAddress(this, d.getAddress());
                mMap.addMarker(new MarkerOptions().position(latLng).title(d.getName()).snippet(d.getAddress() + ", " + d.getDetails()));

                double currentLatitude = Double.valueOf(getLatitude());
                double currentLongitude = Double.valueOf(getLongitude());
                LatLng currentLocation = new LatLng(currentLatitude, currentLongitude);
                mMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Current Location"));

                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                CameraUpdate center = CameraUpdateFactory.newLatLng(currentLocation);
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                mMap.moveCamera(center);
                mMap.animateCamera(zoom);
            }
        }

        //Add current location marker
//        Double currentLatitude = new Double(MainActivity.getLatitude());
//        Double currentLongitude = new Double(MainActivity.getLongitude());
//        LatLng currentLocation = new LatLng(currentLatitude,currentLongitude);
//        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Current Location"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
//        CameraUpdate center = CameraUpdateFactory.newLatLng(currentLocation);
//        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
//        mMap.moveCamera(center);
//        mMap.animateCamera(zoom);

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
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return p1;
    }
}