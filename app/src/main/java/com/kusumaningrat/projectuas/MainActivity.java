package com.kusumaningrat.projectuas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton tambah;
    AdapterBarang adapterBarang;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<ModelBarang> listBarang;
    RecyclerView tv_tampil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tambah = findViewById(R.id.btn_tambah);
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Create.class));
            }
        });
        tv_tampil = findViewById(R.id.tv_tampil);
        RecyclerView.LayoutManager mLayout = new LinearLayoutManager(this);
        tv_tampil.setLayoutManager(mLayout);
        tv_tampil.setItemAnimator(new DefaultItemAnimator());
        tampilData();
    }

    private void tampilData() {
        database.child("Barang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listBarang = new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()){
                   ModelBarang brg = item.getValue(ModelBarang.class);
                    assert brg != null;
                    brg.setKey(item.getKey());
                   listBarang.add(brg);
                }
                adapterBarang = new AdapterBarang(listBarang, MainActivity.this);
                tv_tampil.setAdapter(adapterBarang);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}