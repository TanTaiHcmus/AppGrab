package deadline.ntn.drive_mygrab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.service.dreams.DreamService;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Driver_Status1 extends AppCompatActivity implements OnMapReadyCallback {

    int check_status =  0;
    private GoogleMap MAP_M;
    private RadioGroup radioGroupStatus;
    private RadioButton radioReady;
    private RadioButton radioNoReady;
    private static final int REQUEST_CODE_GPS_PERMISSION = 100;
    private LatLng positionFrom, positionTo;
    private String noiDi, noiDen;
    private String TAG = Confirm_Finish.class.getSimpleName();
    private Polyline mPolyline;
    private String username_textshow;
    CountDownTimer countDownTimer;
    Location location_now;
    String tentaikhoankhachhang;
    String IDchuyenxe;
    int Check_Confirm_ac = 0;

    Intent intent_confirm;

    TextView text_showten, text_sdt, text_sotien;

    Integer IDXe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        positionFrom = getLocationFromString(noiDi);
//        positionTo = getLocationFromString(noiDen);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver__status1);
        this.radioGroupStatus = (RadioGroup) this.findViewById(R.id.radioGroup_Status);
        this.radioNoReady = (RadioButton) this.findViewById(R.id.radioButton_noready);
        this.radioReady = (RadioButton) this.findViewById(R.id.radioButton_ready);
        text_showten = (TextView) findViewById(R.id.txt_showtenkhachhang_driver);
        text_sdt = (TextView) findViewById(R.id.txt_showsdt_driver);
        text_sotien = (TextView) findViewById(R.id.txt_showsotien_driver);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.namMap);
        mapFragment.getMapAsync(this);

        Intent intent_getmain = this.getIntent();
        username_textshow = intent_getmain.getStringExtra("USERNAME");

        this.radioGroupStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton_noready:
                        countDownTimer.cancel();
                        MAP_M.clear();
                        getCurrentLocation();
                        text_showten.setText(" ");
                        text_sdt.setText(" ");
                        text_sotien.setText(" ");
                        break;

                    case R.id.radioButton_ready:
                        Requestoserver();
                        break;

                }
            }
        });



    }

    //Gui


    @Override
    protected void onStart() {
        super.onStart();

    }

    public void Requestoserver(){
        countDownTimer= new CountDownTimer(1000000, 5000) {

            public void onTick(long millisUntilFinished) {
                RequestQueue queue = Volley.newRequestQueue(Driver_Status1.this.getBaseContext());
                  // this = context
                String encodeName = null;

                String url = getResources().getString(R.string.Server_Address) + "YeuCauDatXe/DriverCheck/" + username_textshow + "/" + location_now.getLatitude() + "/" + location_now.getLongitude();
                url = url.replace(" ", "%20");
                Log.d("xxxxx", username_textshow);
                Log.d("xxxxx", url);


                StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                JSONObject json = null;
                                try {
                                    json = new JSONObject(response);
                                    Log.d("xxxx", "vo duoc");
                                    text_showten.setText(json.getString("TenTaiKhoanKhachHang"));
                                    tentaikhoankhachhang = json.getString("TenTaiKhoanKhachHang");
                                    text_sdt.setText(json.getString("SoDT"));
                                    text_sotien.setText(json.getString("Gia"));
                                    noiDen = json.getString("DiaDiemDen");
                                    noiDi=json.getString("DiaDiemDi");
                                    Check_Confirm_ac++;
                                    MAP_M.clear();
                                    positionFrom = getLocationFromString(noiDi);
                                    positionTo = getLocationFromString(noiDen);
                                    MAP_M.addMarker(new MarkerOptions().
                                            position(positionFrom).
                                            title(noiDi).
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).showInfoWindow();
                                    MAP_M.addMarker(new MarkerOptions().
                                            position(positionTo).
                                            title(noiDen).
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))).showInfoWindow();
                                    // Getting URL to the Google Directions API
                                    MAP_M.moveCamera(CameraUpdateFactory.newLatLngZoom(positionFrom, 16));

                                    String url = getDirectionsUrl(positionFrom, positionTo);

                                    Driver_Status1.DownloadTask downloadTask = new Driver_Status1.DownloadTask();

                                    // Start downloading json data from Google Directions API
                                    downloadTask.execute(url);

                                    countDownTimer.cancel();
                                    Log.d("xxxxxx", noiDen);
                                    Log.d("xxxxxx",noiDi);

                                    //txtJson.setText(cityName);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("errror", e.toString());// Not getting any error
                                }
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Error.NoThing.Response",
                                        Toast.LENGTH_SHORT);

                                toast.show();
                            }
                        }
                );
                queue.add(postRequest);



            }

            public void onFinish() {
                //mTextField.setText("done!");
            }
        }.start();
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
        MAP_M = googleMap;
        if(noiDen == null && noiDi == null){
            MAP_M.setMyLocationEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(noiDen == null && noiDi == null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //TODO: Get current location
                getCurrentLocation();
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_GPS_PERMISSION);
            }
        }
        else{
            Log.d("222", "chonam");

            MAP_M.addMarker(new MarkerOptions().
                    position(positionFrom).
                    title(noiDi).
                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).showInfoWindow();
            MAP_M.addMarker(new MarkerOptions().
                    position(positionTo).
                    title(noiDen).
                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))).showInfoWindow();
            // Getting URL to the Google Directions API
            MAP_M.moveCamera(CameraUpdateFactory.newLatLngZoom(positionFrom, 16));

            String url = getDirectionsUrl(positionFrom, positionTo);

            Driver_Status1.DownloadTask downloadTask = new Driver_Status1.DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
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
                        location_now = location;
                        String address = getStringFromLocation(location);
                        //mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocation).zoom(15).bearing(90).tilt(30).build();
                        MAP_M.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        MAP_M.addMarker(new MarkerOptions().position(currentLocation).title(address)).showInfoWindow();

                    }
                });
    }

    private String getStringFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(Driver_Status1.this, Locale.getDefault());
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

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getResources().getString(R.string.google_maps_key);

        return url;
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Error!", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Driver_Status1.ParserTask parserTask = new Driver_Status1.ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            String distance = "";
            String duration = "";

            if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(15);
                lineOptions.color(Color.RED);
            }
            if (lineOptions != null )
            {
                MAP_M.addPolyline(lineOptions);
            }
            // Drawing polyline in the Google Map for the i-th route
        }
    }







    private LatLng getLocationFromString(String location) {

        Geocoder geocoder = new Geocoder(Driver_Status1.this, Locale.getDefault());
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


    //Khi nhan vao lua chon chuyen xe tiep theo
    public void NextToSelect(View view){
        if(text_showten.getText().toString().equals(" ")==true){
            Toast.makeText(Driver_Status1.this, "Chưa có chuyến đi nào để hủy !!!", Toast.LENGTH_LONG).show();
        }
        else {
            RequestQueue queue_next = Volley.newRequestQueue(this);  // this = context

            String encodeName = null;

            String url3 = getResources().getString(R.string.Server_Address) + "YeuCauDatXe/DriverDeny/" + username_textshow + "/" + tentaikhoankhachhang;
            url3 = url3.replace(" ", "%20");


            StringRequest postRequest = new StringRequest(Request.Method.GET, url3,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            //Log.d("xxxxxxxx", "Response add successfully");
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    response,
                                    Toast.LENGTH_SHORT);

                            toast.show();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Error.NextSelect.Response",
                                    Toast.LENGTH_SHORT);

                            toast.show();
                        }
                    }
            );
            queue_next.add(postRequest);
            Requestoserver();
        }
    }

    public void AccpectIt(View view) {
        Log.d("xxxxxxxx",text_showten.getText().toString());
        if(text_showten.getText().toString().equals(" ")==true){
            Toast.makeText(Driver_Status1.this, "Chưa có chuyến đi nào cả !!!", Toast.LENGTH_LONG).show();
            noiDi=null;
            noiDen=null;
        }
        else {

            Toast.makeText(Driver_Status1.this, "Chấp Nhận Chuyến Đi !!!", Toast.LENGTH_LONG).show();
            countDownTimer.cancel();
            intent_confirm = new Intent(Driver_Status1.this, Confirm_Finish.class);
            intent_confirm.putExtra("TENTAIKHOANKHAC", text_showten.getText().toString());
            intent_confirm.putExtra("SODIENTHOAI", text_sdt.getText().toString());
            intent_confirm.putExtra("SOTIEN", text_sotien.getText().toString());
            intent_confirm.putExtra("NOIDI", noiDi);
            intent_confirm.putExtra("NOIDEN", noiDen);
            RequestQueue queue_next1 = Volley.newRequestQueue(this);  // this = context

            String encodeName = null;

            String url3 = getResources().getString(R.string.Server_Address) + "YeuCauDatXe/DriverAccept/" + username_textshow + "/" + tentaikhoankhachhang;
            url3 = url3.replace(" ", "%20");


            StringRequest postRequest = new StringRequest(Request.Method.GET, url3,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            //Log.d("xxxxxxxx", "Response add successfully");
                            Log.d("xxxxxxxx",response);
                            String ID = response.replaceAll("[^-?0-9]+", "");
                            Log.d("xxxxxxxx2",ID);
                            IDXe = Integer.parseInt(ID) + 1;

                            intent_confirm.putExtra("IDChuyenXe", IDXe.toString());
                            startActivity(intent_confirm);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Error.NextSelect.Response",
                                    Toast.LENGTH_SHORT);

                            toast.show();
                        }
                    }
            );
            queue_next1.add(postRequest);


        }
    }

    public void InformationUser(View view){


        Intent intent_driver_status = new Intent(Driver_Status1.this, Information_Account.class);
        intent_driver_status.putExtra("USERNAME1", username_textshow);
        startActivity(intent_driver_status);
    }


    public void ToOut(View view){
        this.finish();
    }


}
