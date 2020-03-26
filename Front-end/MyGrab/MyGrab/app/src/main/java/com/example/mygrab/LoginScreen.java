package com.example.mygrab;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class LoginScreen extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ImageButton imgCar, imgBike;
    private static final int REQUEST_CODE_GPS_PERMISSION = 100;
    private String username_textshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        imgCar = (ImageButton) findViewById(R.id.img_car);
        imgBike = (ImageButton) findViewById(R.id.img_bike);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent_getmain = this.getIntent();
        username_textshow = intent_getmain.getStringExtra("USERNAME");
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
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //TODO: Get current location
            getCurrentLocation();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_GPS_PERMISSION);
        }
    }

    private void getCurrentLocation() {
        FusedLocationProviderClient mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            return;
                        }
                        LatLng currentLocation =
                                new LatLng(location.getLatitude(), location.getLongitude());
                        String address = getStringFromLocation(location);
                        //mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocation).zoom(15).bearing(90).tilt(30).build();
                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        mMap.addMarker(new MarkerOptions().position(currentLocation).title(address)).showInfoWindow();
                    }
                });
    }

    private String getStringFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(LoginScreen.this, Locale.getDefault());
        List<Address> addressList;
        String address = null;
        try {
            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            if (addressList != null)
            {
                address = addressList.get(0).getAddressLine(0);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_GPS_PERMISSION:
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        //TODO: Action when permission denied
                    }
                }
                break;
        }
    }

    public void toHome(View view) {

    }

    public void toShowInformation(View view) {

        Intent intent_driver_status = new Intent(LoginScreen.this, information_user.class);
        intent_driver_status.putExtra("USERNAME1", username_textshow);
        startActivity(intent_driver_status);
    }

    public void toOrderCar(View view) {
        Intent intent = new Intent(this, OrderCar.class);
        intent.putExtra("USERNAME", username_textshow);
        startActivity(intent);
    }

    public void toOrderBike(View view) {

        Intent intent = new Intent(this, OrderBike.class);
        startActivity(intent);
    }
}
