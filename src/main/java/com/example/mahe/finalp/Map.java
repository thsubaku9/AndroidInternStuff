package com.example.mahe.finalp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationServices;

import java.util.List;



public class Map extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    static double lat1,lat2,lon1,lon2,lat,lon;
    private final static int My_PERMISSION_FINE_LOCATION = 101;
    Button str, en,clr,tst;
    EditText txt;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    public String uid;
    protected static String TAG = "MapsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        str = (Button) findViewById(R.id.button2cool);
        en = (Button) findViewById(R.id.button1and);
        clr=(Button) findViewById(R.id.button4more);
        tst=(Button) findViewById(R.id.button3tree);
        txt=(EditText) findViewById(R.id.editText2cool);
        Intent cl=getIntent();
        uid=cl.getStringExtra("uid");
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(15 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //Utilizes more power

        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
            }
        });

        str.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lat1=lat;lon1=lon;
                LatLng mypl = new LatLng(lat1,lon1);
                mMap.addMarker(new MarkerOptions().position(mypl).title("Starting Location"));

            }
        });

        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lat2=lat;lon2=lon;
                LatLng mypl = new LatLng(lat2,lon2);
                mMap.addMarker(new MarkerOptions().position(mypl).title("Ending Location"));

            }
        });

        tst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kk=getIntent();
                try{
                //Toast.makeText(getApplicationContext(),String.format("%f - %f and %f - %f",lat1,lon1,lat2,lon2),Toast.LENGTH_SHORT).show();
                String core=kk.getStringExtra("fromActivity");
                double[] data={lat1,lon1,lat2,lon2};
                if(core.equals("search")) {
                    Intent k = new Intent(Map.this, Search.class);
                    k.putExtra("coord", data);
                    k.putExtra("uid",uid);
                    startActivity(k);
                    finish();
                }
                else if(core.equals("create"))
                {
                    Intent k = new Intent(Map.this, CreatePool.class);
                    k.putExtra("coord", data);
                    k.putExtra("uid",uid);
                    startActivity(k);
                    finish();

                }
                }
                catch (Exception e)
                {Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();}
            }
        });

        txt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                          @Override
                                          public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                              if(actionId== EditorInfo.IME_ACTION_DONE)
                                              {
                                                  String v1=txt.getText().toString();
                                                  List<Address> addressList=null;
                                                  Geocoder geocoder=new Geocoder(Map.this);
                                                  try{
                                                      addressList=geocoder.getFromLocationName(v1,1);
                                                      Address address=addressList.get(0);
                                                      LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
                                                      mMap.addCircle(new CircleOptions().center(latLng).radius(25).fillColor(Color.CYAN).strokeWidth(0.3f));
                                                      mMap.addCircle(new CircleOptions().center(latLng).radius(15).fillColor(Color.RED).strokeWidth(0.3f));
                                                      mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                                      lat=latLng.latitude;
                                                      lon=latLng.longitude;

                                                  }
                                                  catch(Exception e)
                                                  {Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();}

                                              }
                                              return false;
                                          }
                                      }

        );


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


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            mMap.setMyLocationEnabled(true);
            return;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, My_PERMISSION_FINE_LOCATION);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case My_PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, My_PERMISSION_FINE_LOCATION);
                    }
                }
                break;
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Connected");
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG,"Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG,"Error occured"+connectionResult.getErrorMessage());
    }

    @Override
    public void onLocationChanged(Location location) {
        lat=location.getLatitude();
        lon=location.getLongitude();


    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(googleApiClient.isConnected())
        {
            requestLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

/*
    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        double[] data={lat1,lon1,lat2,lon2};
        i.putExtra("coord",data);
        setResult(RESULT_OK, i);

        finish();
    }

    */
}
