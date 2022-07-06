package com.kusumaningrat.projectuas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Create extends AppCompatActivity {

    final Calendar myCalendar= Calendar.getInstance();

    Boolean isEdit = false;
    String idBarang;
    EditText edtNama, edtHarga, edtCustomer, edtTanggal;
    Button btn_simpan;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);


        Intent intent = getIntent();

        isEdit = intent.getBooleanExtra("isEdit", false);
        idBarang = intent.getStringExtra("idBarang");

        if(isEdit){
            setText();
        }

        edtNama = findViewById(R.id.edt_namabarang);
        edtHarga = findViewById(R.id.edt_harga);
        edtCustomer = findViewById(R.id.edt_namacustomer);
        edtTanggal = findViewById(R.id.edt_tglbeli);
        btn_simpan = findViewById(R.id.btn_simpan);
        btn_simpan.setText(isEdit?"UPDATE":"CREATE");
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        edtTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Create.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getNama = edtNama.getText().toString();
                String getHarga = edtHarga.getText().toString();
                String getCustomer = edtCustomer.getText().toString();
                String getTanggal = edtTanggal.getText().toString();

                if(getNama.isEmpty()){
                    edtNama.setError("Form Tidak Boleh Kosong");
                }else if(getHarga.isEmpty()) {
                    edtHarga.setError("Form Tidak Boleh Kosong");
                }else if(getCustomer.isEmpty()){
                    edtHarga.setError("Form Tidak Boleh Kosong");
                }else if(getTanggal.isEmpty()){
                    edtHarga.setError("Form Tidak Boleh Kosong");
                }else {
                    if(isEdit){
                        database.child("Barang").child(idBarang).setValue(new ModelBarang(getNama, getHarga, getCustomer, getTanggal)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Create.this, "Data Berhasil DiUpdate", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Create.this, MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Create.this, "Data Gagal Diupdate", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Create.this, MainActivity.class));
                                finish();

                            }
                        });
                    }else {
                        database.child("Barang").push().setValue(new ModelBarang(getNama, getHarga, getCustomer, getTanggal)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Create.this, "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Create.this, MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Create.this, "Data Gagal Disimpan!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

    }
    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        edtTanggal.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void setText(){
        if (isEdit) {
            FirebaseDatabase.getInstance().getReference().child("Barang").child(idBarang).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ModelBarang barang = snapshot.getValue(ModelBarang.class);
                    if(barang != null) {
                        edtNama.setText(barang.getNama());
                        edtHarga.setText(barang.getHarga());
                        edtCustomer.setText(barang.getCustomer());
                        edtTanggal.setText(barang.getTgl());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Create.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        }
}