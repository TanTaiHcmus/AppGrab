package com.example.mygrab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import androidx.appcompat.app.AppCompatActivity;

public class ChangeInformation extends AppCompatActivity {

    private TextView Id_user, username_show;   //Hien ID cua tai xe, moi tai xe co mot ID rieng do he thong tu cai dat
    private EditText hoten_change, ngaysinh_change, cmnd_change, gioitinh_change, sdt_change, diachi_change, loaixe_change, soxe_change;
    private String text_hoten, text_ngaysinh, text_cmnd, text_gioitinh, text_sdt, text_diachi, text_loaixe, text_soxe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_information);
        Id_user =  (TextView) findViewById(R.id.txt_showid1);
        hoten_change = (EditText) findViewById(R.id.edit_hovaten);
        ngaysinh_change = (EditText) findViewById(R.id.edit_ngaysinh);
        gioitinh_change = (EditText) findViewById(R.id.edit_gioitinh);
        sdt_change = (EditText) findViewById(R.id.edit_sodt);
        diachi_change = (EditText) findViewById(R.id.edit_diachi);
        username_show = (TextView) findViewById(R.id.txt_show_username1);

        Intent intent_infor = this.getIntent();
        String username_show_text = intent_infor.getStringExtra("USERNAME2");

        username_show.setText(username_show_text);
    }

    public void backToInformation(View view) {

        text_hoten = hoten_change.getText().toString();
        text_ngaysinh = ngaysinh_change.getText().toString();
        text_gioitinh = gioitinh_change.getText().toString();
        text_sdt = sdt_change.getText().toString();
        text_diachi = diachi_change.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);  // this = context

        String encodeName = null;
        String encodeDiaChi = null;
        try {
            encodeName = URLEncoder.encode(text_hoten, "UTF-8");
            encodeName = encodeName.replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            encodeDiaChi = URLEncoder.encode(text_diachi, "UTF-8");
            encodeDiaChi = encodeDiaChi.replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = getResources().getString(R.string.Server_Address) + "khachhang/change_information/" + username_show.getText() + "/" + encodeName + "/" + text_ngaysinh +"/" + text_gioitinh +"/" + text_sdt +"/" + encodeDiaChi;
        url = url.replace(" ", "%20");
        Log.d("xxxxxxxxx", url);

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        //Log.d("xxxxxxxx", "Response add successfully");
                        Toast toast = Toast.makeText(getApplicationContext(),
                                response,
                                Toast.LENGTH_SHORT);

                        toast.show();
                        if(response.equals("Change successfully"))
                            ChangeInformation.this.finish();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Error.Change.Response",
                                Toast.LENGTH_SHORT);

                        toast.show();
                    }
                }
        );
        queue.add(postRequest);
        this.finish();
    }
}
