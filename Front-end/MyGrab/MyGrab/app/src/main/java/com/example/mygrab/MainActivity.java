package com.example.mygrab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText username_log, password_log;
    String text_username, text_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username_log = (EditText) findViewById(R.id.edit_username);
        password_log = (EditText) findViewById(R.id.edit_password);

        text_password = password_log.getText().toString();
        text_username = username_log.getText().toString();
    }

    public void toDangNhap(View view) {

        text_password = password_log.getText().toString();
        text_username = username_log.getText().toString();

        // MySql add new student here
        //
        RequestQueue queue = Volley.newRequestQueue(this);  // this = context

        String encodeName = null;

        String url = getResources().getString(R.string.Server_Address) + "TaiKhoan/Login/" + text_username + "/" + text_password + "/" + getResources().getString(R.string.type_account);
        url = url.replace(" ", "%20");


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
                        if(response.equals("Login successfully")) {
                            Intent intent_main = new Intent(MainActivity.this, LoginScreen.class);
                            intent_main.putExtra("USERNAME", text_username);

                            //Intent intent_new = new Intent(MainActivity.this, Driver_Status1.class);
                            startActivity(intent_main);
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Error.Login.Response",
                                Toast.LENGTH_SHORT);

                        toast.show();
                    }
                }
        );
        queue.add(postRequest);

    }

    public void toDangKyTaiKhoan(View view) {

        Intent intent = new Intent(this, RegisterAccount.class);
        startActivity(intent);
    }
}
