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

import androidx.appcompat.app.AppCompatActivity;

public class ChangePassword extends AppCompatActivity {

    TextView username;
    EditText mkcu, mkmoi, xnmkmoi;
    String text_mkcu = null, text_mkmoi = null, text_xnmkmoi = null, username_textshow = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mkcu = (EditText) findViewById(R.id.txt_matkhaucu);
        mkmoi = (EditText) findViewById(R.id.txt_matkhaumoi);
        xnmkmoi = (EditText) findViewById(R.id.txt_xacnhanmk);
        username = (TextView) findViewById(R.id.txt_show_username2);
        Intent intent_infor = this.getIntent();
        username_textshow = intent_infor.getStringExtra("USERNAME2");

        username.setText(username_textshow);

    }

    public void backToInformation(View view) {

        text_mkcu = mkcu.getText().toString();
        text_mkmoi = mkmoi.getText().toString();
        text_xnmkmoi = xnmkmoi.getText().toString();
        if(text_xnmkmoi.equals(text_mkmoi) == false){
            Toast.makeText(ChangePassword.this, "Mật Khẩu Xác Nhân Không Trùng Mật Khẩu Mới !!!", Toast.LENGTH_LONG).show();
        }
        else {
            RequestQueue queue = Volley.newRequestQueue(this);  // this = context

            String encodeName = null;

            String url = getResources().getString(R.string.Server_Address) + "TaiKhoan/change_password/" + username_textshow + "/" + text_mkcu + "/" + text_mkmoi;
            url = url.replace(" ", "%20");

            Log.d("xxxxxx", url);
            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            //Log.d("xxxxxxxx", "Response add successfully");
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    response,
                                    Toast.LENGTH_SHORT);

                            toast.show();

                            if (response.equals("Change successfully")) {
                                Toast.makeText(ChangePassword.this, "Thay Đổi Mật Khẩu Thành Công!!!", Toast.LENGTH_LONG).show();
                                ChangePassword.this.finish();
                            } else {
                                Toast.makeText(ChangePassword.this, response, Toast.LENGTH_LONG).show();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                        }
                    }
            );
            queue.add(postRequest);
            this.finish();
        }
    }
}
