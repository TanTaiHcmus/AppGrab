package deadline.ntn.drive_mygrab;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

public class Confirm_Finish extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng positionFrom, positionTo;
    private String noiDi, noiDen;
    private static final int REQUEST_CODE_GPS_PERMISSION = 100;
    private String TAG = Confirm_Finish.class.getSimpleName();
    TextView text_tenkhach_confirm, text_sodt_confirm, text_sotien_confirm;
    private Polyline mPolyline;

    String IDChuyenxe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        noiDi="43 Nguyễn Tất Thành, Quận 4, Thành phố Hồ Chí Minh";
        noiDen="120 Nguyễn Tất Thành, Quận 4, Thành phố Hồ Chí Minh";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_confirm_finish);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.myMap1);
        mapFragment.getMapAsync(this);
        Intent intent_confirm_finish = this.getIntent();
        noiDen = intent_confirm_finish.getStringExtra("NOIDEN");
        noiDi = intent_confirm_finish.getStringExtra("NOIDI");
        text_tenkhach_confirm = (TextView) findViewById(R.id.txt_showtenkhachhang_confirm);
        text_sotien_confirm = (TextView) findViewById(R.id.txt_showsotien_confirm);
        text_sodt_confirm = (TextView) findViewById(R.id.txt_showsdt_confirm);

        text_tenkhach_confirm.setText(intent_confirm_finish.getStringExtra("TENTAIKHOANKHAC"));
        text_sotien_confirm.setText(intent_confirm_finish.getStringExtra("SOTIEN"));
        text_sodt_confirm.setText(intent_confirm_finish.getStringExtra("SODIENTHOAI"));
        IDChuyenxe = intent_confirm_finish.getStringExtra("IDChuyenXe");
        Log.d("xxxxxxxidc",IDChuyenxe);




        positionFrom = getLocationFromString(noiDi);
        positionTo = getLocationFromString(noiDen);


    }

    //Ham nay khi nhan button tro ve trang truoc


    public void Backto(View view) {

        this.finish();
    }


    //Ham thuc hien khi nhan vao nut xac nhan thục hien tuc la tai xe dong y nhan cuoc xe cua khach hang do
    public void ToAgreeConfirm(View view) {
        Toast.makeText(Confirm_Finish.this, "Hoàn Thành Chuyến Đi !!!", Toast.LENGTH_LONG).show();


        RequestQueue queue_next = Volley.newRequestQueue(this);  // this = context

        String encodeName = null;

        String url3 = getResources().getString(R.string.Server_Address) + "ChuyenXe/" + this.IDChuyenxe;
        url3 = url3.replace(" ", "%20");
        Log.d("xxxxxx", url3);


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
        this.finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        mMap.addMarker(new MarkerOptions().
                position(positionFrom).
                title(noiDi).
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).showInfoWindow();
        mMap.addMarker(new MarkerOptions().
                position(positionTo).
                title(noiDen).
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))).showInfoWindow();
        // Getting URL to the Google Directions API
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionFrom, 16));

        String url = getDirectionsUrl(positionFrom, positionTo);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
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

            ParserTask parserTask = new ParserTask();

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
                mMap.addPolyline(lineOptions);
            }
            // Drawing polyline in the Google Map for the i-th route
        }
    }







    private LatLng getLocationFromString(String location) {

        Geocoder geocoder = new Geocoder(Confirm_Finish.this, Locale.getDefault());
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

}
