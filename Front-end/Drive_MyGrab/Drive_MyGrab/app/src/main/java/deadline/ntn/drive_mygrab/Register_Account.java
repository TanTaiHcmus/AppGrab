package deadline.ntn.drive_mygrab;

import androidx.appcompat.app.AppCompatActivity;

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

import java.net.URLEncoder;

import deadline.ntn.drive_mygrab.R;

public class Register_Account extends AppCompatActivity {
    String username, password, confirm_password;
    EditText text1, text2, text3;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);
        text1 = (EditText) findViewById(R.id.edit_username_reg);
        text2 = (EditText) findViewById(R.id.edit_password_reg);
        text3 = (EditText) findViewById(R.id.edit_confirmpassword_reg);


    }

    public void Register_Account(View view) {
        //Kiem tra username da ton tai chua
        //if(username.equals())
        username = text1.getText().toString();
        password = text2.getText().toString();
        confirm_password = text3.getText().toString();
        if(password.equals(confirm_password)==false){
            Toast.makeText(Register_Account.this, "Mật Khẩu Xác nhận không khớp !!!!", Toast.LENGTH_LONG).show();
        }
        else{
            // MySql add new student here
            //
            RequestQueue queue = Volley.newRequestQueue(this);  // this = context

        String encodeName = null;

            String url = getResources().getString(R.string.Server_Address) + "TaiKhoan/Register/" + username + "/" + password + "/" + getResources().getString(R.string.type_account);
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
                            if(response.equals("Register successfully"))
                                Register_Account.this.finish();
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Error.Register.Response",
                                    Toast.LENGTH_SHORT);

                            toast.show();
                        }
                    }
            );
            queue.add(postRequest);

        }
    }
}
