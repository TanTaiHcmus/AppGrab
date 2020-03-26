package com.example.mygrab;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class OrderCar extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int REQUEST_CODE_GPS_PERMISSION = 100;
    private EditText editNoiDi, editNoiDen;
    private Button button;
    private String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_car);
        editNoiDi = (EditText) findViewById(R.id.edit_car_from);
        editNoiDen = (EditText) findViewById(R.id.edit_car_to);
        button = (Button) findViewById(R.id.btn_datxe_car);
        Intent intent_infor = this.getIntent();
        username = intent_infor.getStringExtra("USERNAME");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noiDi = editNoiDi.getText().toString();
                String noiDen = editNoiDen.getText().toString();
                if (noiDi != null && noiDen != null)
                {
                    LatLng positionFrom = getLocationFromString(noiDi);
                    LatLng positionTo = getLocationFromString(noiDen);
                    if (positionFrom != null && positionTo != null)
                    {
                        Intent intent = new Intent(OrderCar.this, CarDelivery.class);
                        intent.putExtra("latiFrom", positionFrom.latitude);
                        intent.putExtra("longiFrom", positionFrom.longitude);
                        intent.putExtra("noiDi", noiDi);
                        intent.putExtra("latiTo", positionTo.latitude);
                        intent.putExtra("longiTo", positionTo.longitude);
                        intent.putExtra("noiDen", noiDen);
                        intent.putExtra("USERNAME", username);
                        startActivity(intent);
                    }
                }
            }
        });
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

    private LatLng getLocationFromString(String location) {

        Geocoder geocoder = new Geocoder(OrderCar.this, Locale.getDefault());
        List<Address> addressList;
        Address address = null;
        try {
            addressList = geocoder.getFromLocationName(location, 1);
            if (addressList != null)
            {
                address = addressList.get(0);
                double lati = address.getLatitude();
                double longi = address.getLongitude();
                LatLng diaDiem = new LatLng(lati, longi);
                return diaDiem;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
        Geocoder geocoder = new Geocoder(OrderCar.this, Locale.getDefault());
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
}
