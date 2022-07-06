package com.kusumaningrat.projectuas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class AdapterBarang extends RecyclerView.Adapter<AdapterBarang.ViewHolder> {


    private final ArrayList<ModelBarang> localDataSet;
    private Activity activity;


    public AdapterBarang(ArrayList<ModelBarang> listBarang, MainActivity mainActivity) {
        localDataSet = listBarang;
        activity = mainActivity;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nama, harga, customer, tgl_beli;
        private final LinearLayout tvTampil;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            nama = view.findViewById(R.id.nama_barang);
            harga =view.findViewById(R.id.harga_barang);
            customer =view.findViewById(R.id.nama_customer);
            tgl_beli =  view.findViewById(R.id.tgl_beli);
            tvTampil =view.findViewById(R.id.card_hasil);
        }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public AdapterBarang(ArrayList<ModelBarang> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_barang, viewGroup, false);

        return new ViewHolder(view);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.nama.setText(localDataSet.get(position).getNama());
        viewHolder.harga.setText(localDataSet.get(position).getHarga());
        viewHolder.customer.setText(localDataSet.get(position).getCustomer());
        viewHolder.tgl_beli.setText(localDataSet.get(position).getTgl());

        viewHolder.tvTampil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(view, localDataSet.get(position));
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    private void showDialog(View view, ModelBarang barang) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View dialogView = inflater.inflate(R.layout.dialog_detail, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        Button btnEdit = dialogView.findViewById(R.id.btn_edit);
        Button btnDelete = dialogView.findViewById(R.id.btn_delete);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(activity, Create.class);
                myIntent.putExtra("isEdit", true);
                myIntent.putExtra("idBarang", barang.getKey());
                view.getContext().startActivity(myIntent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference().child("Barang");
                        // we are use add listerner
                        // for event listener method
                        // which is called with query.
                        Query query=dbref.child(barang.getKey());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // remove the value at reference
                                dataSnapshot.getRef().removeValue();
                                Toast.makeText(view.getContext(), "Data dengan nama " + barang.getNama() + " berhasil dihapus", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(view.getContext(), "Data dengan nama " + barang.getNama() + " gagal dihapus", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setMessage("Apakah anda yakin mau menghapus ? " + barang.getNama());
                builder.show();
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
