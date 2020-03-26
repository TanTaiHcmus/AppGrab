package com.example.mygrab;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class CarDelivery extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng positionFrom, positionTo;
    private String noiDi, noiDen;
    private TextView textView;
    private String TAG = CarDelivery.class.getSimpleName();
    private Polyline mPolyline;
    private String username = null, distance, gia, ho_ten, so_dt, bienSo, tuoi;
    private double latiFrom, longiFrom, latiTo, longiTo;
    private double gia_xe;
    private boolean check = true;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_delivery);
        Intent intent = this.getIntent();
        latiFrom = intent.getDoubleExtra("latiFrom", 0);
        longiFrom = intent.getDoubleExtra("longiFrom", 0);
        latiTo = intent.getDoubleExtra("latiTo", 0);
        longiTo = intent.getDoubleExtra("longiTo", 0);
        noiDi = intent.getStringExtra("noiDi");
        noiDen = intent.getStringExtra("noiDen");
        positionFrom = new LatLng(latiFrom, longiFrom);
        positionTo = new LatLng(latiTo, longiTo);
        textView = (TextView) findViewById(R.id.txt_tienCar);
        Intent intent_infor = this.getIntent();
        username = intent_infor.getStringExtra("USERNAME");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map3);
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

        String url = getResources().getString(R.string.Server_Address) + "giaxe/" + "1";
        RequestQueue queue = Volley.newRequestQueue(this);
        url = url.replace(" ", "%20");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                            String giaxe = json.getString("GiaXe");
                            gia_xe = Double.parseDouble(giaxe);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("errror",e.toString());// Not getting any error
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(stringRequest);

        url = getDirectionsUrl(positionFrom, positionTo);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    public void toCompleteDelivery(View view) {
        RequestQueue queue = Volley.newRequestQueue(this);  // this = context

        String lati = String.valueOf(latiFrom), longi = String.valueOf(longiFrom);

        Date date =java.util.Calendar.getInstance().getTime();
        String currentTime = date.toString();

        String encodeFrom = null;
        String encodeTo = null;
        try {
            encodeFrom = URLEncoder.encode(noiDi, "UTF-8");
            encodeFrom = encodeFrom.replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            encodeTo = URLEncoder.encode(noiDen, "UTF-8");
            encodeTo = encodeTo.replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = getResources().getString(R.string.Server_Address) + "yeucaudatxe/" + username + "/" + "1" + "/" + lati + "/" + longi + "/" + encodeFrom + "/" + encodeTo + "/" + distance + "/" + gia + "/0/" + currentTime;
        url = url.replace(" ", "%20");
        Log.d("***", url);

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        Toast toast = Toast.makeText(getApplicationContext(),
                                response,
                                Toast.LENGTH_SHORT);

                        toast.show();
                        if (response.equals("Order successfully"))
                            requetToServer();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Error.Order.Response",
                                Toast.LENGTH_SHORT);

                        toast.show();
                    }
                }
        );
        queue.add(postRequest);
    }

    private void requetToServer() {
         countDownTimer = new CountDownTimer(1000000, 5000) {

            public void onTick(long millisUntilFinished) {
                RequestQueue queue = Volley.newRequestQueue(CarDelivery.this.getBaseContext());

                String url = getResources().getString(R.string.Server_Address) + "YeuCauDatXe/Check/" + username;
                url = url.replace(" ", "%20");
                Log.d("xxxxx", url);

                StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {

                                if (response.equals("Waiting"))
                                {
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Không có sẵn tài xế!",
                                            Toast.LENGTH_SHORT);

                                    toast.show();
                                }
                                else if (response.equals("Waiting driver accept"))
                                {
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Waiting driver accept!",
                                            Toast.LENGTH_SHORT);

                                    toast.show();
                                }
                                else
                                {
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Đã tìm được tài xế!",
                                            Toast.LENGTH_SHORT);

                                    toast.show();
                                    Log.d("username", response);
                                    getInformationTaiXe(response);
                                    countDownTimer.cancel();
                                }

                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Error.Order.Response",
                                        Toast.LENGTH_SHORT);

                                toast.show();
                            }
                        }
                );
                queue.add(postRequest);
            }

            public void onFinish() {
                Toast.makeText(getApplicationContext(), "Driver không có sẵn", Toast.LENGTH_SHORT);
            }
        }.start();
    }

    private void getInformationTaiXe(String username_taixe) {

        String url = getResources().getString(R.string.Server_Address) + "yeucauthongtintaixe/" + username_taixe;
        Log.d("tai", url);
        RequestQueue queue = Volley.newRequestQueue(this);
        url = url.replace(" ", "%20");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                            ho_ten = json.getString("HoTen");
                            tuoi = json.getString("NgaySinh");
                            tuoi = tuoi.substring(0, 4);
                            so_dt = json.getString("SoDT");
                            bienSo = json.getString("BienSoXe");

                            Log.d("**", ho_ten + tuoi);

                            Intent intent = new Intent(CarDelivery.this, ConfirmDelivery.class);
                            intent.putExtra("HOTEN", ho_ten);
                            intent.putExtra("TUOI", tuoi);
                            intent.putExtra("SODT", so_dt);
                            intent.putExtra("BIENSO", bienSo);
                            check = false;
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("errror",e.toString());// Not getting any error
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(stringRequest);
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

    /**
     * A method to download json data from url
     */
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

    // Fetches data from url passed
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

    /**
     * A class to parse the Google Places in JSON format
     */
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
            distance = "";
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

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null )
            {
                distance = distance.substring(0, distance.length() - 3);
                gia = String.valueOf(gia_xe * Double.valueOf(distance));
                textView.setText(gia);
                mMap.addPolyline(lineOptions);
            }
        }
    }
}
