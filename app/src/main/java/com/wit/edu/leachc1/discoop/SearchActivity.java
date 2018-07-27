package com.wit.edu.leachc1.discoop;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    DBHandler dbHandler = new DBHandler(this);

    private Toolbar tbMainSearch;
    private ListView lvToolbarSerch;
    private String TAG2 = MainActivity.class.getSimpleName();
    List<Discount> arrays = new ArrayList<Discount>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUpViews();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout4);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view4);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setUpViews() {
        tbMainSearch = (Toolbar)findViewById(R.id.tb_toolbarsearch);
        lvToolbarSerch =(ListView) findViewById(R.id.search);
        arrays = dbHandler.getAllDiscounts();
        List<String> stringArrays = new ArrayList<String>();
        for(Discount d : arrays) {
            stringArrays.add(d.getType() + " | " + d.getName() + " | " + d.getDetails() + " | " + d.getExpiration());
        }
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,stringArrays);
        lvToolbarSerch.setAdapter(adapter);
        setSupportActionBar(tbMainSearch);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
                Discount d = dbHandler.getDiscount(position);
                Toast.makeText(getApplicationContext(), "Showing on map...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SearchActivity.this, MapsActivity.class);
                intent.putExtra("address", d.getAddress());
                intent.putExtra("name", d.getName());
                intent.putExtra("type", d.getType());
                intent.putExtra("details", d.getDetails());
                intent.putExtra("expr", d.getExpiration());
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
