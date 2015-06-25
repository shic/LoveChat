package com.atemeo.lovechat.wadii.maps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.atemeo.lovechat.wadii.database.LocationDatabaseActivity;
import com.atemeo.lovechat.wadii.maps.MapMarkers;
import com.atemeo.lovechat.wadii.maps.IndoorExample;
import com.atemeo.lovechat.wadii.maps.ShowMap;
import com.atemeo.lovechat.R;

import android.location.Address;
import android.location.Geocoder;


public class MapsActivity extends Activity implements View.OnClickListener {

    static final String TAG = "Mapper";
    private double lon;
    private double lat;
    private EditText placeText;
    private String placeName;
    static final int numberOptions = 10;
    String[] optionArray = new String[numberOptions];
    EditText geocodeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);

        geocodeField = (EditText) findViewById(R.id.geocode_input);

        // Add Click listeners for all buttons
        View firstButton = findViewById(R.id.geocode_button);
        firstButton.setOnClickListener(this);
        View secondButton = findViewById(R.id.latlong_button);
        secondButton.setOnClickListener(this);
        View thirdButton = findViewById(R.id.honolulu_button);
        thirdButton.setOnClickListener(this);
        View fourthButton = findViewById(R.id.indoor_map_button);
        fourthButton.setOnClickListener(this);
        View fifthButton = findViewById(R.id.location_database_button);
        fifthButton.setOnClickListener(this);

        // This prevents my Nexus 7 running Android 4.4.2 from opening
        // the soft keyboard when the app is launched rather than when
        // an input field is selected.

        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to action bar if present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuItem1:
                // Actions for help page
                Intent i = new Intent(this, Help.class);
                startActivity(i);
                return true;
            case R.id.menuItem2:
                // Actions for settings page
                Intent j = new Intent(this, Settings.class);
                startActivity(j);
                return true;
            // Note: A Quit button is redundant in Android because it duplicates the
            // functionality of the Back button.  But some people feel more comfortable
            // with one, so here is how to add one.
            case R.id.menuItem3:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.geocode_button:
                Log.i(TAG, "Button 1 pushed");

                // Test whether geocoder is present on platform
                if (Geocoder.isPresent()) {
                    placeText = (EditText) findViewById(R.id.geocode_input);
                    placeName = placeText.getText().toString();
                    // Break from execution if the user has not entered anything in the field
                    if (placeName.compareTo("") == 0) break;

                    geocodeLocation(placeName);

                    ShowMap.putMapData(lat, lon, 18, true);

                    Intent j = new Intent(this, ShowMap.class);
                    startActivity(j);
                } else {
                    String noGoGeo = "FAILURE: No Geocoder on this platform.";
                    Toast.makeText(this, noGoGeo, Toast.LENGTH_LONG).show();
                    geocodeField.setText(noGoGeo);
                    return;
                }
                break;

            case R.id.latlong_button:
                Log.i(TAG, "Button 2 pushed");

                // Read the latitude and longitude from the input fields
                EditText latText = (EditText) findViewById(R.id.lat_input);
                EditText lonText = (EditText) findViewById(R.id.lon_input);
                String latString = latText.getText().toString();
                String lonString = lonText.getText().toString();

                // Only execute if user has put entries in both lat and long fields.
                if (latString.compareTo("") != 0 && lonString.compareTo("") != 0) {
                    lat = Double.parseDouble(latString);
                    lon = Double.parseDouble(lonString);
                    ShowMap.putMapData(lat, lon, 13, true);
                    Intent k = new Intent(this, ShowMap.class);
                    startActivity(k);
                }
                break;

            case R.id.honolulu_button:
                Log.i(TAG, "Button 3 pushed");
                Intent m = new Intent(MapsActivity.this, MapMarkers.class);
                startActivity(m);
                break;

            case R.id.indoor_map_button:
                Intent n = new Intent(this, IndoorExample.class);
                startActivity(n);
                break;

            case R.id.location_database_button:
                Intent o = new Intent(this, LocationDatabaseActivity.class);
                startActivity(o);
                break;

            //case R.id.mapme_button:
            //Intent q = new Intent(this, MapMe.class);
            //startActivity(q);
            //break;

        }

    }

    // Method to joked location passed as string (e.g., "Pentagon"), which
    // places the corresponding latitude and longitude in the variables lat and
    // lon.

    private void geocodeLocation(String placeName) {

        // Following adapted from Conder and Darcey, pp.321 ff.
        Geocoder gcoder = new Geocoder(this);

        // Note that the Geocoder uses synchronous network access, so in a serious application
        // it would be best to put it on a background thread to prevent blocking the main UI if network
        // access is slow. Here we are just giving an example of how to use it so, for simplicity, we
        // don't put it on a separate thread.  See the class RouteMapper in this package for an example
        // of making a network access on a background thread. Geocoding is implemented by a backend
        // that is not part of the core Android framework, so we use the static method
        // Geocoder.isPresent() to test for presence of the required backend on the given platform.

        try {
            List<Address> results = null;
            if (Geocoder.isPresent()) {
                results = gcoder.getFromLocationName(placeName, numberOptions);
            } else {
                Log.i(TAG, "No geocoder accessible on this platform");
                return;
            }
            Iterator<Address> locations = results.iterator();
            String raw = "\nRaw String:\n";
            String country;
            int opCount = 0;
            while (locations.hasNext()) {
                Address location = locations.next();
                if (opCount == 0 && location != null) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                }
                country = location.getCountryName();
                if (country == null) {
                    country = "";
                } else {
                    country = ", " + country;
                }
                raw += location + "\n";
                optionArray[opCount] = location.getAddressLine(0) + ", "
                        + location.getAddressLine(1) + country + "\n";
                opCount++;
            }
            Log.i(TAG, raw);
            Log.i(TAG, "\nOptions:\n");
            for (int i = 0; i < opCount; i++) {
                Log.i(TAG, "(" + (i + 1) + ") " + optionArray[i]);
            }
            Log.i(TAG, "lat=" + lat + " lon=" + lon);

        } catch (IOException e) {
            Log.e(TAG, "I/O Failure; is network available?", e);
        }
    }
}
