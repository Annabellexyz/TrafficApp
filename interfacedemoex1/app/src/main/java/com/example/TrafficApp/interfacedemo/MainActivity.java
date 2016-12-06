package com.example.rasheduzzaman.interfacedemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends FragmentActivity implements CustomDialog.CustomDialogListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 60000;  /* 60 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */
    Location location_sms;

    /*
     * Define a request code to send to Google Play services This code is
     * returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        Intent intent = getIntent();

        userName= intent.getStringExtra("uname");

        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                }
            });
        } else {
            Toast.makeText(this, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void onSearch(View view)
    {
        EditText location_tf = (EditText)findViewById(R.id.editText9);

        //Begin: added by annabelle
        String location = location_tf.getText().toString();
        List<Address> addressList = null;
        if(location != null || !location.equals(""))
        {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location , 1);


            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());
            //map.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            //map.animateCamera(CameraUpdateFactory.newLatLng(latLng));

            String markerTitle = address.getAddressLine(0)+" "+address.getLocality();
            map.addMarker(new MarkerOptions().position(latLng).title(markerTitle));
            map.animateCamera(CameraUpdateFactory.newLatLng(latLng));

            //store the address to db
            AddressRepo addressRepo = new AddressRepo(this);
            AddressTb addressTb = new AddressTb();
            addressTb.address_data = address.toString();
            addressTb.modify_time = Long.toString(System.currentTimeMillis());
            int addr_id = addressRepo.insert(addressTb);
            // End: added by Annabelle

            try  {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {}

        }
    }
    // Begin: added by Annabelle
    public void onList(View view) {

        Toast.makeText(getApplicationContext(), "List the Marker...", Toast.LENGTH_LONG).show();
        AddressRepo repo = new AddressRepo(this);


        ArrayList<HashMap<String, String>> addrList = repo.getAddressList();
        for (int i = 0; i < addrList.size()&&i<5; ++i) {
            /*ListView lv = getListView();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                    student_Id = (TextView) view.findViewById(R.id.student_Id);
                    String studentId = student_Id.getText().toString();
                    Intent objIndent = new Intent(getApplicationContext(),StudentDetail.class);
                    objIndent.putExtra("student_Id", Integer.parseInt( studentId));
                    startActivity(objIndent);
                }
            });*/
            //ListAdapter adapter = new SimpleAdapter( MainActivity.this,studentList, R.layout.view_student_entry, new String[] { "id","name"}, new int[] {R.id.student_Id, R.id.student_name});
            //setListAdapter(adapter);
            Toast.makeText(this, addrList.get(i).toString(), Toast.LENGTH_SHORT).show();
            List<Address> addressList = null;
            //if(!location.equals("")){
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(addrList.get(i).toString(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!addressList.isEmpty()) {
                Address address = addressList.get(0);

                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                String markerTitle = address.getAddressLine(0)+" "+address.getThoroughfare()+" "+address.getLocality();
                Toast.makeText(getApplicationContext(), markerTitle, Toast.LENGTH_SHORT).show();
                map.addMarker(new MarkerOptions().position(latLng).title(markerTitle));
                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
        if (addrList.size() == 0) {
            Toast.makeText(this, "Address history is empty!", Toast.LENGTH_SHORT).show();
        }
    }
    // End: added by Annabelle

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onDialogPositiveClick(String value) {
        // User touched the dialog's positive button
        Toast.makeText(this, value + ", you clicked positive button", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()){
            case R.id.main_menu:
                Intent intent1=new Intent(MainActivity.this,homepageactivity.class);
                intent1.putExtra("uname", userName);
                startActivity(intent1);
                return true;
            case  R.id.items:
                if(map.isTrafficEnabled())
                    map.setTrafficEnabled(false);
                else
                    map.setTrafficEnabled(true);
                return true;
            case R.id.custom_dialog:
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(ComponentName.unflattenFromString("com.example.administrator.cand"));
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            case R.id.map_traffic:
                Intent intent2=new Intent(MainActivity.this,listview.class);
                startActivity(intent2);
                return true;
            case R.id.Send_location:
                CharSequence colors[] = new CharSequence[] {"send SMS", "send mail"};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select type");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        if(which==0) {
                            Intent intent3 = new Intent(MainActivity.this, Send_sms.class);
                            intent3.putExtra("uname", "Hi! Can you pick me up please http://maps.google.com/maps?q=" + Double.toString(location_sms.getLatitude()) + "," +
                                    Double.toString(location_sms.getLongitude()) + "&z=17");
                            startActivity(intent3);
                        }
                        else{
                            Intent intent4 = new Intent(MainActivity.this, send_mail.class);
                            intent4.putExtra("uname1", "Hi! Can you pick me up please http://maps.google.com/maps?q=" + Double.toString(location_sms.getLatitude()) + "," +
                                    Double.toString(location_sms.getLongitude()) + "&z=17");
                            startActivity(intent4);
                        }
                    }
                });
                builder.show();


                return true;
            case R.id.log_out:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        EditNameDialog alertDialog = EditNameDialog.newInstance("Some title");
        alertDialog.show(fm, "fragment_alert");

    }

    private void showItemListDialog(){
        FragmentManager fm = getSupportFragmentManager();
        DialogList listDialog = DialogList.newInstance("Dialog List");
        listDialog.show(fm, "fragment_list");
    }


    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Map is ready
            Toast.makeText(this, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
            map.setMyLocationEnabled(true);

            // Begin: added by Annabelle
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng latLng) {
                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Setting the position for the marker
                    markerOptions.position(latLng);

                    List<Address> addressList = null;
                    String markerTitle = "";
                    //if(!location.equals("")){
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!addressList.isEmpty()) {
                        Address address = addressList.get(0);
                        markerTitle = address.getAddressLine(0) + " " + address.getLocality();
                        //store the address to db
                        AddressRepo addressRepo = new AddressRepo(getApplicationContext());
                        AddressTb addressTb = new AddressTb();
                        addressTb.address_data = address.toString();
                        addressTb.modify_time = Long.toString(System.currentTimeMillis());
                        int addr_id = addressRepo.insert(addressTb);
                    }
                    // Setting the title for the marker.
                    // This will be displayed on taping the marker
                    markerOptions.title(markerTitle);

                    // Clears the previously touched position
                    //mMap.clear();

                    // Animating to the touched position
                    map.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    // Placing a marker on the touched position
                    map.addMarker(markerOptions);


                }
            });
            // End:  added by Annabelle
            // Now that map has loaded, let's get our location!
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();

            connectClient();
        } else {
            Toast.makeText(this, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    protected void connectClient() {
        // Connect the client.
        if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    /*
     * Called when the Activity becomes visible.
    */
    @Override
    protected void onStart() {
        super.onStart();
        connectClient();
    }

    /*
	 * Called when the Activity is no longer visible.
	 */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    /*
     * Handle results returned to the FragmentActivity by Google Play services
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {

            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mGoogleApiClient.connect();
                        break;
                }

        }
    }

    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getSupportFragmentManager(), "Location Updates");
            }

            return false;
        }
    }

    /*
     * Called by Location Services when the request to connect the client
     * finishes successfully. At this point, you can request the current
     * location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            Toast.makeText(this, "GPS location was found!", Toast.LENGTH_SHORT).show();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            map.animateCamera(cameraUpdate);
            location_sms=location;
            startLocationUpdates();
        } else {
            Toast.makeText(this, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
        }
    }

    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    public void onLocationChanged(Location location) {
        // Report to the UI that the location was updated
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    /*
     * Called by Location Services if the connection to the location client
     * drops because of an error.
     */
    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * Called by Location Services if the attempt to Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
        }
    }

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
}