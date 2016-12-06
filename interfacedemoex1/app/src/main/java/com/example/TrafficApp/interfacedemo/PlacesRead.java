package com.example.rasheduzzaman.interfacedemo;

/**
 * Created by rasheduzzaman on 10/19/15.
 */

import android.os.AsyncTask;
import android.util.Log;
import android.net.http.*;
import com.google.android.gms.maps.GoogleMap;

public class PlacesRead extends AsyncTask<Object, Integer, String> {
    String googlePlacesData = null;
    GoogleMap googleMap;

    @Override
    protected String doInBackground(Object... inputObj) {
        try {
            googleMap = (GoogleMap) inputObj[0];
            String googlePlacesUrl = (String) inputObj[1];
            //HttpResponseCache http =new HttpResponseCache();
            //googlePlacesData = http.read(googlePlacesUrl);
        } catch (Exception e) {
            Log.d("Google Place Read Task", e.toString());
        }
        return googlePlacesData;
    }
    @Override
    protected void onPostExecute(String result) {
        PlacesDisplay placesDisplayTask = new PlacesDisplay();
        Object[] toPass = new Object[2];
        toPass[0] = googleMap;
        toPass[1] = result;
        placesDisplayTask.execute(toPass);
    }
}