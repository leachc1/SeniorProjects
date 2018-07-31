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
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Discoop
 * Senior Project - Computer Science
 * Created by Crissy Leach and Sam Kanner
 * Wentworth Institute of Technology
 */

public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    DBHandler dbHandler = new DBHandler(this);

    private Toolbar tbMainSearch;
    private ListView lvToolbarSerch;
    private String TAG2 = MainActivity.class.getSimpleName();
    List<Discount> arrays = new ArrayList<Discount>();
    ArrayAdapter<String> adapter;

    Double currentLatitude = new Double(MainActivity.getLatitude());
    Double currentLongitude = new Double(MainActivity.getLongitude());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUpViews();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout4);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view4);
        navigationView.setNavigationItemSelectedListener(this);

    }

    // set up list view
    private void setUpViews() {
        tbMainSearch = (Toolbar)findViewById(R.id.tb_toolbarsearch);
        lvToolbarSerch =(ListView) findViewById(R.id.search);
        arrays = dbHandler.getAllDiscounts();

        LinkedHashMap<String, Double> locationMap = new LinkedHashMap<>();

        double lat;
        double lng;
        double dist;

        for(Discount d : arrays) {
            String address = d.getAddress();

            lat = getLatFromAddress(this, address);
            lng = getLngFromAddress(this, address);
            dist = (distance(currentLatitude, currentLongitude, lat, lng));

            locationMap.put(address, dist);
            String log = String.valueOf(dist);
            Log.d("Dist", log);
        }

        Object[] a = locationMap.entrySet().toArray();
        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<String, Double>) o2).getValue()
                        .compareTo(((Map.Entry<String, Double>) o1).getValue());
            }
        });

        List<String> locationList = new ArrayList<>(locationMap.keySet());

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,locationList);
        lvToolbarSerch.setAdapter(adapter);
        setSupportActionBar(tbMainSearch);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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

//    private double distance(double lat1, double lng1, double lat2, double lng2) {
//        double d = Math.sqrt(Math.pow(lat2 - lat1, 2) - Math.pow(lng2 - lng1, 2));
//        return d;
//    }

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

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG2, "onQueryTextSubmit: query->"+query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(TAG2, "onQueryTextChange: newText->" + newText);
        adapter.getFilter().filter(newText);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout4);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem mSearchmenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) mSearchmenuItem.getActionView();
        searchView.setQueryHint("enter Text");
        searchView.setOnQueryTextListener(this);
        final ListView list = (ListView) findViewById(R.id.search);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String addr = (String) parent.getAdapter().getItem(position);

                Discount d = dbHandler.getDiscount(position);
                Toast.makeText(getApplicationContext(), "Showing on map...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SearchActivity.this, MapsActivity.class);
                intent.putExtra("address", addr);
//                intent.putExtra("address", d.getAddress());
//                intent.putExtra("name", d.getName());
//                intent.putExtra("type", d.getType());
//                intent.putExtra("details", d.getDetails());
//                intent.putExtra("expr", d.getExpiration());
                startActivity(intent);
            }
        });
        Log.d(TAG2, "onCreateOptionsMenu: mSearchmenuItem->" + mSearchmenuItem.getActionView());
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

    public boolean aboutPage(MenuItem item) {
        Intent intent = new Intent(this, AboutDiscoop.class);
        startActivity(intent);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_submit) {
            Intent intent = new Intent(this, submit.class);
            startActivity(intent);
        } else if (id == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout4);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
