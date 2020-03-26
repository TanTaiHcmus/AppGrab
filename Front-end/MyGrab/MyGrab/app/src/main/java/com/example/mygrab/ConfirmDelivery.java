package com.example.mygrab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmDelivery extends AppCompatActivity {

    private TextView tenTaiXe, tuoiTaiXe, sdtTaiXe, bienSoXe;
    private String ten, tuoi, sdt, bienso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_delivery);
        tenTaiXe = (TextView) findViewById(R.id.txt_tentaixe);
        tuoiTaiXe = (TextView) findViewById(R.id.txt_tuoitaixe);
        sdtTaiXe = (TextView) findViewById(R.id.txt_sodttaixe);
        bienSoXe = (TextView) findViewById(R.id.txt_biensotaixe);
        Intent intent = this.getIntent();
        ten = intent.getStringExtra("HOTEN");
        tuoi = intent.getStringExtra("TUOI");
        sdt = intent.getStringExtra("SODT");
        bienso = intent.getStringExtra("BIENSO");
        tenTaiXe.setText("Họ và tên: "+ ten);
        tuoiTaiXe.setText("Tuổi: " + String.valueOf(2020 - Integer.valueOf(tuoi)));
        bienSoXe.setText(bienso);
        sdtTaiXe.setText("Số ĐT: " + sdt);
    }

    public void backToLogin(View view) {
        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);
    }
}
