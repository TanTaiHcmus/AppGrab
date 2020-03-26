package com.example.mygrab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class information_user extends AppCompatActivity {

    TextView id, hoten, ngaysinh, socmnd, gioitinh, sdt, diachi, loaixe, Soxe, usernameshow_infor;
    String Username_Infor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_user);
        id = (TextView) findViewById(R.id.txt_showid);
        hoten = (TextView) findViewById(R.id.txt_showhovaten);
        ngaysinh = (TextView) findViewById(R.id.txt_showngaysinh);
        gioitinh = (TextView) findViewById(R.id.txt_showgioitinh);
        sdt = (TextView) findViewById(R.id.txt_showsodt);
        diachi = (TextView) findViewById(R.id.txt_showdiachi);
        usernameshow_infor = (TextView) findViewById(R.id.txt_show_username);

        Intent intent_infor = this.getIntent();
        String username_textshow = intent_infor.getStringExtra("USERNAME1");
        usernameshow_infor.setText(username_textshow);
        //Can lay cac thong tin va hien thi no len cac bien tren
        Username_Infor = usernameshow_infor.getText().toString();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String url = getResources().getString(R.string.Server_Address) + "khachhang/" + Username_Infor;
        RequestQueue queue = Volley.newRequestQueue(this);
        url = url.replace(" ", "%20");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                            String ho_ten = json.getString("HoTen");
                            hoten.setText(ho_ten);
                            String gioi_tinh = json.getString("GioiTinh");
                            gioitinh.setText(gioi_tinh);
                            String so_dt = json.getString("SoDT");
                            sdt.setText(so_dt);
                            String dia_chi = json.getString("DiaChi");
                            diachi.setText(dia_chi);
                            String ngay_sinh = json.getString("NgaySinh");
                            ngaysinh.setText(ngay_sinh);

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

    public void toDoiMatKhau(View view) {

        Intent intent = new Intent(this, ChangePassword.class);
        intent.putExtra("USERNAME2", Username_Infor);
        startActivity(intent);
    }

    public void toEditInformation(View view) {

        Intent intent = new Intent(this, ChangeInformation.class);
        intent.putExtra("USERNAME2", Username_Infor);
        startActivity(intent);
    }

    public void backToLogin(View view) {
        this.finish();
    }
}
