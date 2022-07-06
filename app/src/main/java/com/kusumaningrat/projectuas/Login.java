package com.kusumaningrat.projectuas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText username, password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        username = (EditText) findViewById(R.id.edt_email);
        password = (EditText) findViewById(R.id.edt_email2);
        login = (Button) findViewById(R.id.button2);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MLogin();
            }
        });
    }

    private void MLogin() {
        if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
            startActivity(new Intent(this, MainActivity.class));
        }else{
            Toast.makeText(Login.this, "Gagal Login, Cek Username dan Password", Toast.LENGTH_SHORT).show();
        }
    }
}