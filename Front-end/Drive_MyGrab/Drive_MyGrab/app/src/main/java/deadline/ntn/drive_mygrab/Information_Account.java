package deadline.ntn.drive_mygrab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Information_Account extends AppCompatActivity {
    TextView id, hoten, ngaysinh, socmnd, gioitinh, sdt, diachi, loaixe, Soxe, usernameshow_infor;
    String Username_Infor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_account);

        id = (TextView) findViewById(R.id.txt_showid_infor);
        hoten = (TextView) findViewById(R.id.txt_showhovaten_infor);
        ngaysinh = (TextView) findViewById(R.id.txt_showngaysinh_infor);
        socmnd = (TextView) findViewById(R.id.txt_showsocmnd_infor);
        gioitinh = (TextView) findViewById(R.id.txt_showgioitinh_infor);
        sdt = (TextView) findViewById(R.id.txt_showsodt_infor);
        diachi = (TextView) findViewById(R.id.txt_showdiachi_infor);
        loaixe = (TextView) findViewById(R.id.txt_showloaixe_infor);
        Soxe = (TextView) findViewById(R.id.txt_showsoxe_infor);
        usernameshow_infor = (TextView) findViewById(R.id.txt_showusername_infor);

        Intent intent_infor = this.getIntent();
        String username_textshow = intent_infor.getStringExtra("USERNAME1");


        Log.d("xxxxxx", username_textshow);

        usernameshow_infor.setText(username_textshow);
        //Can lay cac thong tin va hien thi no len cac bien tren
        Username_Infor = usernameshow_infor.getText().toString();


    }

    @Override
    protected void onStart() {
        super.onStart();
        String url = getResources().getString(R.string.Server_Address) + "TaiXe/" + Username_Infor;
        url = url.replace(" ", "%20");
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d("xxxx", url);
        //RequestQueue queue;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);

                            Log.d("xxxx", "vo duoc");

                            String test = json.getString("HoTen");

                            Log.d("xxxx", "vo duoc");

                            hoten.setText(json.getString("HoTen"));
                            ngaysinh.setText(json.getString("NgaySinh"));
                            socmnd.setText(json.getString("CMND"));
                            gioitinh.setText(json.getString("GioiTinh"));
                            sdt.setText(json.getString("SoDT"));
                            diachi.setText(json.getString("DiaChi"));
                            //txtJson.setText(cityName);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("errror", e.toString());// Not getting any error
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //txtJson.setText("That didn't work!");
            }

        });
        queue.add(stringRequest);

        //

        String url1 = getResources().getString(R.string.Server_Address) + "TaiXe/Xe/" + Username_Infor;
        url1 = url1.replace(" ", "%20");
        Log.d("xxxxxxx", url1);
        RequestQueue queue1 = Volley.newRequestQueue(this);
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject json2 = null;
                        try {
                            json2 = new JSONObject(response);
                            Soxe.setText(json2.getString("BienSoXe"));
                            loaixe.setText(json2.getString("LoaiXe"));
                            Log.d("xxxx", "vo duoc");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("errror", e.toString());// Not getting any error
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //txtJson.setText("That didn't work!");
            }
        });
        queue1.add(stringRequest1);
    }

    //Ham thay doi mk
    public void ToChangePassWord(View view) {
        Intent intent_changepass = new Intent(Information_Account.this, Change_Password.class);
        intent_changepass.putExtra("USERNAME3", Username_Infor);
        startActivity(intent_changepass);
    }

    //Khi user muon chinh sua bam vao day
    public void toEditInformation(View view) {
        Intent intent_main = new Intent(Information_Account.this, Change_Information_Account.class);
        intent_main.putExtra("USERNAME2", Username_Infor);
        startActivity(intent_main);
    }

    public void ToChangeAvatar(View view) {

    }

    //Tro ve app truoc
    public void ToBackIt(View view){

        this.finish();
    }
}