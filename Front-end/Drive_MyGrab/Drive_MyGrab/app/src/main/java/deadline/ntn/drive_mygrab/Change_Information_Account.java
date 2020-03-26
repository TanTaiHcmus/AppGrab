package deadline.ntn.drive_mygrab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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

public class Change_Information_Account extends AppCompatActivity {
    TextView Id_user, username_show;   //Hien ID cua tai xe, moi tai xe co mot ID rieng do he thong tu cai dat
    EditText hoten_change, ngaysinh_change, cmnd_change, gioitinh_change, sdt_change, diachi_change, loaixe_change, soxe_change;
    String text_hoten, text_ngaysinh, text_cmnd, text_gioitinh, text_sdt, text_diachi, text_loaixe, text_soxe;
    //Cac loai thuoc tinh ma tai xe muon thay doi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeinformation_account);
        Id_user = (TextView) findViewById(R.id.txt_showid_change);
        hoten_change = (EditText) findViewById(R.id.edit_hovaten_change);
        ngaysinh_change = (EditText) findViewById(R.id.edit_ngaysinh_change);
        cmnd_change = (EditText) findViewById(R.id.edit_socmnd_change);
        gioitinh_change = (EditText) findViewById(R.id.edit_gioitinh_change);
        sdt_change = (EditText) findViewById(R.id.edit_sodt_change);
        diachi_change = (EditText) findViewById(R.id.edit_diachi_change);
        loaixe_change = (EditText) findViewById(R.id.edit_loaixe_change);
        soxe_change = (EditText) findViewById(R.id.edit_soxe_change);
        username_show = (TextView) findViewById(R.id.txt_showusername_change);

        text_hoten = hoten_change.getText().toString();
        text_ngaysinh = ngaysinh_change.getText().toString();
        text_cmnd = cmnd_change.getText().toString();
        text_gioitinh = gioitinh_change.getText().toString();
        text_sdt = sdt_change.getText().toString();
        text_diachi = diachi_change.getText().toString();
        text_loaixe = loaixe_change.getText().toString();
        text_soxe = soxe_change.getText().toString();

        Intent intent_infor = this.getIntent();
        String username_show_text = intent_infor.getStringExtra("USERNAME2");

        username_show.setText(username_show_text);


    }

    //Khi nhan vao button chinh sua thong tin => thuc hien luu cac du lieu da lay duoc tu tren va quay lai trang truoc do
    public void backToInformation(View view) {
        //...
        text_hoten = hoten_change.getText().toString();
        text_ngaysinh = ngaysinh_change.getText().toString();
        text_cmnd = cmnd_change.getText().toString();
        text_gioitinh = gioitinh_change.getText().toString();
        text_sdt = sdt_change.getText().toString();
        text_diachi = diachi_change.getText().toString();
        text_loaixe = loaixe_change.getText().toString();
        text_soxe = soxe_change.getText().toString();

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
        String url = getResources().getString(R.string.Server_Address) + "TaiXe/change_information/" + username_show.getText() + "/" + encodeName + "/" + text_ngaysinh + "/" + text_gioitinh + "/" + text_sdt + "/" + text_diachi + "/" + text_cmnd + "/" + text_loaixe + "/" + text_soxe;
        url = url.replace(" ", "%20");
        Log.d("xxxxxxxxx", url);

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
                        if (response.equals("Change successfully"))
                            Change_Information_Account.this.finish();
                    }
                },
                new Response.ErrorListener() {
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


