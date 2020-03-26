package deadline.ntn.drive_mygrab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class Change_Password extends AppCompatActivity {
    EditText mkcu, mkmoi, xnmkmoi;
    TextView username_changepass;
    String username_textshow;
    String text_mkcu, text_mkmoi, text_xnmkmoi;
    //cac bien Mat khau cu, moi, va xac nhan mat khau
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword_account);
        mkcu = (EditText) findViewById(R.id.txt_matkhaucu_changepass);
        mkmoi = (EditText) findViewById(R.id.txt_matkhaumoi_changepass);
        xnmkmoi = (EditText) findViewById(R.id.txt_xacnhanmk_changepass);
        username_changepass = (TextView) findViewById(R.id.txt_showusername_changepass);

        Intent intent_get = this.getIntent();
        username_textshow = intent_get.getStringExtra("USERNAME3");




        username_changepass.setText(username_textshow);


    }

    public void ToChangePassWord(View view) {
        text_mkcu = mkcu.getText().toString();
        text_mkmoi = mkmoi.getText().toString();
        text_xnmkmoi = xnmkmoi.getText().toString();
        //Ham tien hanh kiem tra mkmoi va xnmkmoi co giong nhau kg va luu mkmoi xuong database va tro ve trang truoc
        String matkhaucu = "1"; //mat khau cu duoc lay tu server
        if(text_xnmkmoi.equals(text_mkmoi) == false){
            Toast.makeText(Change_Password.this, "Mật Khẩu Xác Nhân Không Trùng Mật Khẩu Mới !!!", Toast.LENGTH_LONG).show();
        }
        else {
            RequestQueue queue = Volley.newRequestQueue(this);  // this = context

            String encodeName = null;

            String url = getResources().getString(R.string.Server_Address) + "TaiKhoan/change_password/" + username_textshow + "/" + text_mkcu + "/" + text_mkmoi;
            url = url.replace(" ", "%20");

            Log.d("xxxxxx", url);
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

                            if(response.equals("Change successfully")) {
                                Toast.makeText(Change_Password.this, "Thay Đổi Mật Khẩu Thành Công!!!", Toast.LENGTH_LONG).show();
                                Change_Password.this.finish();
                            }
                            else {
                                Toast.makeText(Change_Password.this, response , Toast.LENGTH_LONG).show();
                            }

                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                        }
                    }
            );
            queue.add(postRequest);

        }
    }
}