package com.wit.edu.leachc1.discoop;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;
import android.app.ProgressDialog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static com.wit.edu.leachc1.discoop.MainActivity.getLatitude;
import static com.wit.edu.leachc1.discoop.MainActivity.getLongitude;

/**
 * Discoop
 * Senior Project - Computer Science
 * Created by Crissy Leach and Sam Kanner
 * Wentworth Institute of Technology
 */

public class submit extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button btnShowCoord;
    EditText edtAddress;
    String locationCoordinates;

    boolean checkboxBar = false;
    boolean checkboxRetail = false;
    boolean checkboxOther = false;
    int keyIdInc = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnShowCoord = (Button) findViewById(R.id.submitBtn);
        edtAddress = (EditText) findViewById(R.id.editText2);

        btnShowCoord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new submit.GetCoordinates().execute(edtAddress.getText().toString().replace(" ", "+"));
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view3);
        navigationView.setNavigationItemSelectedListener(this);

        // when the submit button is clicked, info from input fields are sent to database
        final Button submitButton = findViewById(R.id.submitBtn);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Getting type input
                String typeString;
                if (checkboxBar == true) {
                    typeString = "Restaurant/Bar";
                } else if (checkboxRetail == true) {
                    typeString = "Retail";
                } else if (checkboxOther == true) {
                    typeString = "Other";
                } else {
                    typeString = null;
                }

                // Getting Address input
                EditText addressStreetInput = (EditText) findViewById(R.id.editText2);
                String addressStreetString = addressStreetInput.getText().toString();
                EditText addressStateInput = (EditText) findViewById(R.id.editText4);
                String addressStateString = addressStateInput.getText().toString();
                String addressString = addressStreetString + " " + addressStateString;

                // Getting Details input
                TextInputEditText detailsInput = (TextInputEditText) findViewById(R.id.textInputEditText);
                String detailsString = detailsInput.getText().toString();

                // Getting Establishment Name input
                TextInputEditText nameInput = (TextInputEditText) findViewById(R.id.textInputEditText2);
                String nameString = nameInput.getText().toString();

                // Getting Expiration Date input
                DatePicker datePicker = (DatePicker) findViewById(R.id.simpleDatePicker);
                int day = datePicker.getDayOfMonth();
                Integer.toString(day);
                int month = datePicker.getMonth();
                Integer.toString(month);
                int year = datePicker.getYear();
                Integer.toString(year);
                String dateString = month + "/" + day + "/" + year;

                // add the inputs to the database
                insertRows(keyIdInc, typeString, addressString, dateString, detailsString, nameString);

                // refresh the submit page
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
    }

    // method to insert the submit inputs to the database
    public void insertRows(int id, String type, String address, String expr, String details, String name) {
        DBHandler db = new DBHandler(this);
        Log.d("Insert: ", "Inserting..");
        // database handler method to add a discount
        db.addDiscount(new Discount(id, type, address, expr, details, name));
        // increment key_id
        keyIdInc++;
        // Reading all discounts
        Log.d("Reading: ", "Reading all discounts..");
        // put all discounts in a list to output to the log
        List<Discount> discounts = db.getAllDiscounts();
        for (Discount discount : discounts) {
            String log = "Id: " + discount.getId() + ", Type: " + discount.getType() + ", Address: "
                    + discount.getAddress() + ", Expr Date: " + discount.getExpiration() + ", Details: "
                    + discount.getDetails() + ", Name: " + discount.getName();
            Log.d("Discount::", log);
        }
    }

    // for nav drawer
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.submit, menu);
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

        if (id == R.id.nav_map) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // toasts for error checking
    public void toastSubmitMsg(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    public void toastSubmit(View v) {
        toastSubmitMsg("Your discount has been submitted");
    }

    public void toastAdded(View v) {
        toastSubmitMsg("Added");
    }

    public void toastRemoved(View v) {
        toastSubmitMsg("Removed");
    }

    public void returnBtn(View v) {
        finish();
    }

    // to get checkbox information
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.checkbox_bar:
                if (checked) {
                    checkboxBar = true;
                    toastAdded(view);
                } else
                    checkboxBar = false;
                toastRemoved(view);
                break;
            case R.id.checkbox_retail:
                if (checked) {
                    checkboxRetail = true;
                    toastAdded(view);
                } else
                    checkboxRetail = false;
                toastRemoved(view);
                break;
            case R.id.checkbox_other:
                if (checked) {
                    checkboxOther = true;
                    toastAdded(view);
                } else
                    checkboxOther = false;
                toastRemoved(view);
                break;
        }
    }

    // getting coordinates
    private class GetCoordinates extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(submit.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait....");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String response;
            try {
                String address = strings[0];
                HttpDataHandler http = new HttpDataHandler();
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s", address);
                response = http.getHTTPData(url);
                return response;
            } catch (Exception ex) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);

                String lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lat").toString();
                String lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lng").toString();
                locationCoordinates = lat + lng;

                if (dialog.isShowing())
                    dialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // about page
    public boolean aboutPage(MenuItem item) {
        Intent intent = new Intent(this, AboutDiscoop.class);
        startActivity(intent);
        return true;
    }
}
