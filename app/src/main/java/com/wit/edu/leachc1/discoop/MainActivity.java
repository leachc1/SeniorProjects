package com.wit.edu.leachc1.discoop;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Discoop
 * Senior Project - Computer Science
 * Created by Crissy Leach and Sam Kanner
 * Wentworth Institute of Technology
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected Location mLastLocation;

    private String mLatitudeLabel;
    private String mLongitudeLabel;
    private static TextView mLatitudeText;
    private static TextView mLongitudeText;
    private FusedLocationProviderClient mFusedLocationClient;

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar(toolbar);

        mLatitudeLabel = "Latitude";
        mLongitudeLabel = "Longitude";
        mLatitudeText = (TextView) findViewById((R.id.latitude_text));
        getLatitude();
        mLongitudeText = (TextView) findViewById((R.id.longitude_text));
        getLongitude();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    public static String getLatitude(){
        String currentLatitude = mLatitudeText.getText().toString();
        return currentLatitude;
    }

    public static String getLongitude(){
        String currentLongitude = mLongitudeText.getText().toString();
        return currentLongitude;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_submit) {
            Intent intent = new Intent(this, submit.class);
            startActivity(intent);

        } else if (id == R.id.nav_map) {
            Intent intent = new Intent(this, MapsActivity.class);

            // Get current location
            double currentLatitude = Double.valueOf(getLatitude());
            double currentLongitude = Double.valueOf(getLongitude());

            DBHandler dbHandler = new DBHandler(this);
            List<Discount> discounts = dbHandler.getAllDiscounts();
            double distance = 0;
            String addr = null;
            double lat;
            double lng;
            String extraName = null;
            String extraType = null;
            String extraDetails = null;
            String extraExpr = null;
            String extraAddr = null;

            for (Discount d : discounts) {
                if (distance == 0) {
                    addr = d.getAddress();
                    lat = getLatFromAddress(this, addr);
                    lng = getLngFromAddress(this, addr);
                    distance = distance(lat, lng, currentLatitude, currentLongitude);
                    extraName = d.getName();
                    extraExpr = d.getExpiration();
                    extraType = d.getType();
                    extraDetails = d.getDetails();
                    extraAddr = d.getAddress();
                } else {
                    addr = d.getAddress();
                    lat = getLatFromAddress(this, addr);
                    lng = getLngFromAddress(this, addr);
                    double tmp = distance(lat, lng, currentLatitude, currentLongitude);
                    if (tmp < distance) {
                        distance = tmp;
                        extraName = d.getName();
                        extraExpr = d.getExpiration();
                        extraType = d.getType();
                        extraDetails = d.getDetails();
                        extraAddr = d.getAddress();
                    }
                }
            }
            intent.putExtra("name", extraName);
            intent.putExtra("type", extraType);
            intent.putExtra("details", extraDetails);
            intent.putExtra("address", extraAddr);
            intent.putExtra("expr", extraExpr);
            startActivity(intent);

        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showMap(View v) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void showClosest(View v) {
        Intent intent = new Intent(this, MapsActivity.class);

        // Get current location
        double currentLatitude = Double.valueOf(getLatitude());
        double currentLongitude = Double.valueOf(getLongitude());

        DBHandler dbHandler = new DBHandler(this);
        List<Discount> discounts = dbHandler.getAllDiscounts();
        double distance = 0;
        String addr = null;
        double lat;
        double lng;
        String extraName = null;
        String extraType = null;
        String extraDetails = null;
        String extraExpr = null;
        String extraAddr = null;

        for (Discount d : discounts) {
            if (distance == 0) {
                addr = d.getAddress();
                lat = getLatFromAddress(this, addr);
                lng = getLngFromAddress(this, addr);
                distance = distance(lat, lng, currentLatitude, currentLongitude);
                extraName = d.getName();
                extraExpr = d.getExpiration();
                extraType = d.getType();
                extraDetails = d.getDetails();
                extraAddr = d.getAddress();
            } else {
                addr = d.getAddress();
                lat = getLatFromAddress(this, addr);
                lng = getLngFromAddress(this, addr);
                double tmp = distance(lat, lng, currentLatitude, currentLongitude);
                if (tmp < distance) {
                    distance = tmp;
                    extraName = d.getName();
                    extraExpr = d.getExpiration();
                    extraType = d.getType();
                    extraDetails = d.getDetails();
                    extraAddr = d.getAddress();
                }
            }
        }
        intent.putExtra("name", extraName);
        intent.putExtra("type", extraType);
        intent.putExtra("details", extraDetails);
        intent.putExtra("address", extraAddr);
        intent.putExtra("expr", extraExpr);
        startActivity(intent);
    }

    public double getLatFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        double lat = 0;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return 0;
            }
            Address location = address.get(0);
            lat = location.getLatitude();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return lat;
    }

    public double getLngFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        double lng = 0;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return 0;
            }
            Address location = address.get(0);
            lng = location.getLongitude();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return lng;
    }

    private double distance(double lat1, double lng1, double lat2, double lng2) {
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lng1);

        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lng2);

        double distanceInMeters = loc1.distanceTo(loc2);
        return distanceInMeters;
    }

    public boolean aboutPage(MenuItem item) {
        Intent intent = new Intent(this, AboutDiscoop.class);
        startActivity(intent);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {
                // Permission denied.

                showSnackbar(R.string.textwarn, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.textwarn, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();


                            mLatitudeText.setText(String.format(Locale.ENGLISH, "%f",
                                    mLastLocation.getLatitude()));
                            mLongitudeText.setText(String.format(Locale.ENGLISH, "%f",
                                    mLastLocation.getLongitude()));
                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());

                        }
                    }
                });
    }

}
